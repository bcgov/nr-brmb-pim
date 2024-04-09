import {Injectable, Injector} from "@angular/core";
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AppConfigService, AuthenticationInterceptor, TokenService} from "@wf1/core-ui";
import {UUID} from "angular2-uuid";
import {catchError, mergeMap} from "rxjs/operators";
import {Router} from "@angular/router";
import {RouterExtService} from "../services/router-ext.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {WFSnackbarComponent} from "../components/common/snackbars/wf-snackbar.component";
import {displayErrorMessage, ErrorHandlingInstructions, getSnackbarConfig} from "../utils/user-feedback-utils";
import {WF_SNACKBAR_TYPES} from "../utils";
import { ResourcesRoutes } from "../utils/constants";

@Injectable()
export class ResourcesInterceptor extends AuthenticationInterceptor implements HttpInterceptor {
    private tokenService;
    private asyncTokenRefresh;
    private refreshSnackbar;

    constructor(protected appConfig: AppConfigService, private snackbarService: MatSnackBar, protected injector: Injector,
                private router: Router, private routerExtService: RouterExtService) {
        super(appConfig, injector);
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let processedRequest = req;
        if (this.isUrlSecured(req.url)) {
          if (!this.tokenService) {
              this.tokenService = this.injector.get(TokenService);
          }
          let headers = this.getProcessedRequestHeaders(req);
          let requestId = headers.get("RequestId");
          if (this.tokenService.getTokenDetails()) {
              if (this.tokenService.isTokenExpired(this.tokenService.getTokenDetails()) || processedRequest.url.includes("/redeem")) {
                  return this.refreshWindow().pipe(mergeMap((tokenResponse) => {
                      this.tokenService.updateToken(tokenResponse);
                      processedRequest = req.clone({headers});
                      if (this.asyncTokenRefresh.isComplete) {
                          this.asyncTokenRefresh = undefined;
                      }
                      if (this.refreshSnackbar) {
                          this.refreshSnackbar.dismiss();
                          this.refreshSnackbar = undefined;
                      }
                      return this.handleRequest(requestId, next, processedRequest);
                  }));
              } else {
                  processedRequest = req.clone({
                      headers: headers
                  });
                  return this.handleRequest(requestId, next, processedRequest);
              }
          } else {
              return this.handleRequest(requestId, next, processedRequest);
          }
        } else {
            let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            return this.handleRequest(requestId, next, processedRequest);
        }
    }

    getProcessedRequestHeaders(req: HttpRequest<any>) {
      let headers = req.headers;
      let headerRequestId = headers.get("RequestId");

      if (!headerRequestId) { //if the requestId is not already set (via the ng client endpoint params), then set it manually - typically needed for older apis like incidents api
          let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          headers = req.headers.set("RequestId", requestId);
          let authToken = this.tokenService.getOauthToken();
          headers = headers.set('Authorization', 'Bearer ' + authToken);

          // Need to explicitly disable caching, as IE11 caches by default
          if (req.method === "GET"
              && req.url.indexOf("/cirras-underwriting-api/") == -1) {
              headers = headers
                  .set("Cache-Control", "no-cache")
                  .set("Pragma", "no-cache");
          }
      }
      return headers;
  }    

    handleRequest(requestId, next, processedRequest): Observable<any> {
        return next.handle(processedRequest).pipe(
            catchError((response: HttpErrorResponse) => {
                const errorHandlingInstructions = this.retrieveErrorHandlingInstructions(response, processedRequest, requestId);
                this.handleError(errorHandlingInstructions);
                throw response;
            }));
    }

    retrieveErrorHandlingInstructions(response, processedRequest, requestId): ErrorHandlingInstructions {
        if (response.url && response.url.endsWith("codeTables")) {
            return this.createErrorHandlingInstructions(null, null, `Unable to initialize application (${response.status}). ${response.url}`);
        } else if (response.status === 0) {
            if (window.navigator.onLine) {
                return this.createErrorHandlingInstructions(null, null, "An unexpected error has occurred.");
            } else {
                return this.createErrorHandlingInstructions(null, null, "No Connectivity. Please try again when you have reconnected.");
            }
        } else if (response.status === 504) {
            return this.createErrorHandlingInstructions(null, null, "No Connectivity. Please try again when you have reconnected.");
        } else if (response.status === 500) {
            //return this.createErrorHandlingInstructions(null, null, requestId ? `Server Error (500). RequestId: ${requestId}` : "Server Error (500)");
            return this.createErrorHandlingInstructions(null, null,  `Server Error(500): ${response.error.messages[0].message}`);
        } else if (response.status >= 501) {
            return this.createErrorHandlingInstructions(null, null, `Server Error (${response.status}).`);
        } else if (response.status == 401) {
            return this.createErrorHandlingInstructions(null, null, `Insufficient Permissions (${response.status}). ${response.url}`);
        } else if (response.status == 403) {
            return this.createErrorHandlingInstructions(ResourcesRoutes.UNAUTHORIZED, null, null);
            //return this.createErrorHandlingInstructions(null, null, `Insufficient Permissions (${response.status}). ${response.url}`);
        } else if (response.status == 400  ) {
            return this.createErrorHandlingInstructions(null, null, `Validation Error: ${this.getAllErrorMessages(response.error)}`);
        } else if (response.status == 412  ) {
            return this.createErrorHandlingInstructions(null, null, `Data has changed since the last retrieve and it can't be saved. Press the Cancel button to get the latest changes.`);
        }
        return null;
    }

    handleError(errorHandlingInstructions: ErrorHandlingInstructions) {
        if (!errorHandlingInstructions) {
            // console.log("no error handling instructions");
            return;
        }

        if (errorHandlingInstructions.snackBarErrorMsg) {
            this.displayErrorMessage(errorHandlingInstructions.snackBarErrorMsg);
        }

        if (errorHandlingInstructions.redirectToRoute) {
            this.router.navigate([errorHandlingInstructions.redirectToRoute], {queryParams: {message: errorHandlingInstructions.redirectToRouteData}});
        }
    }


    createErrorHandlingInstructions(redirectToRoute, redirectToRouteData, snackBarErrorMsg): ErrorHandlingInstructions {
        return {
            redirectToRoute: redirectToRoute,
            redirectToRouteData: redirectToRouteData,
            snackBarErrorMsg: snackBarErrorMsg
        };

    }

    updateErrorPageRouteData(routeName, data) {
        // console.log("routeName: " + routeName);
        // console.log("data: " + data);
        let route = this.router.config.find(r => r.path === routeName);
        if (data) {
            route.data = {errorMsg: data};
        }
    }

    isUrlSecured(url: string): boolean {
        let isSecured = false;
        const config = this.appConfig.getConfig();

        if (config && config.rest) {
            for (let endpoint in config.rest) {
                if (url.startsWith(config.rest[endpoint])) {
                    isSecured = true;
                    break;
                }
            }
        }
        return isSecured;
    }

    displayErrorMessage(message: string) {
        setTimeout(() => {
            displayErrorMessage(this.snackbarService, message);
        });
    }

    displayRefreshErrorMessage(message: string) {
        if (!this.refreshSnackbar) {
            this.refreshSnackbar = this.snackbarService.openFromComponent(WFSnackbarComponent, getSnackbarConfig(message, WF_SNACKBAR_TYPES.ERROR));

            this.refreshSnackbar.onAction().subscribe(() => {
                this.refreshSnackbar = undefined;
            });
        }
    }

    refreshWindow() {
        // console.log("refresh window");
        if (this.asyncTokenRefresh) {
            return this.asyncTokenRefresh;
        }
        let baseUrl = this.appConfig.getConfig().application.baseUrl;
        let refreshPage = "refresh-token.html";
        if (baseUrl && !baseUrl.endsWith("/")) {
            refreshPage = `/${refreshPage}`;
        }
        let clientId = this.appConfig.getConfig().webade.clientId;
        let authorizeUrl = this.appConfig.getConfig().webade.oauth2Url;
        let authScopes = this.appConfig.getConfig().webade.authScopes;

        let redirectUrl = `${baseUrl}${refreshPage}`;
        this.asyncTokenRefresh = this.tokenService.initRefreshTokenImplicitFlow(`${authorizeUrl}?response_type=token&client_id=${clientId}&redirect_uri=${redirectUrl}&scope=${authScopes}`
            , "cirras-underwriting-token",
            (errorMessage) => {
                this.displayRefreshErrorMessage(errorMessage);
            });
        return this.asyncTokenRefresh;
    }

    getAllErrorMessages(responseError: any) {

        let tempMessage = ""

        if (responseError && responseError.messages) {

            for (let i = 0; i <  responseError.messages.length; i++ ) {
                tempMessage = tempMessage + " " + responseError.messages[i].message
            }
        }

        return tempMessage

    }
}
