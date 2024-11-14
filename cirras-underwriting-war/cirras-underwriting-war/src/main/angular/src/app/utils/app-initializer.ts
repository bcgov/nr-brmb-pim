import {DefaultService as CirrasUnderwritingAPIService} from "@cirras/cirras-underwriting-api";

import {Injector} from "@angular/core";
import {AppConfigService, TokenService} from "@wf1/wfcc-core-lib";
import { HttpErrorResponse, HttpHandler } from "@angular/common/http";
import {catchError, defaultIfEmpty, map} from "rxjs/operators";
import {forkJoin, of} from "rxjs";
import {UUID} from "angular2-uuid";
import {DATE_FORMATS} from "./index";
import {CODE_TABLE_CACHE} from "./constants";
import {ApplicationStateService} from "../services/application-state.service";
// import * as moment from "moment";
import moment, { Moment } from "moment";

export function appInitFn(httpHandler: HttpHandler, injector: Injector): () => Promise<any> {
  const appStateService = injector.get(ApplicationStateService);
  //initialize the app gutter space styling based on device resolution
  document.documentElement.style.setProperty("--cirras-underwriting-gutter-space", appStateService.getIsMobileResolution() ? "8px" : "16px");

  //initialize token and code tables
  const appConfigService = injector.get(AppConfigService);
  return (): Promise<any> => {
      return new Promise((resolve, reject) => {
          appConfigService.configEmitter.subscribe(config => {
              const tokenService = injector.get(TokenService);
              setTimeout(() => {
                  if (tokenService.getOauthToken()) {
                      tokenService.authTokenEmitter.subscribe((token) => {
                          handleHasToken(token, appConfigService, tokenService, injector, resolve, reject);
                      });
                  } else {
                      //resolve();
                      resolve(defaultIfEmpty(''));
                  }
              }, 1000);
          });

      });
  };
}

export function handleHasToken(token, appConfigService: AppConfigService, tokenService: TokenService, injector: Injector, resolve, reject) {
  // Fetch code tables if there is access
  forkJoin(
      [
        getUnderwritingCodeTables(token, tokenService, injector),
      ]
  ).pipe(
      map(results => {
          if (results[0] instanceof HttpErrorResponse) {
              console.log("Failed to initialize app", results);
              reject();
              return null;
          } else {
              //console.log(results);
              return results;
          }
      }),
      catchError(error => of(error))
  ).subscribe(results => {
      if (results) {
          if (results[0]) { populateUnderwritingCodeTableCache(results[0].codeTableList); }
      }

      console.log(CODE_TABLE_CACHE);
      //console.log(JSON.stringify(CODE_TABLE_CACHE));
      setTimeout(() => { //the org code hdq loading takes some time...so wait some time for those to populate before resolving
          console.log("loading code table");
          resolve();
      }, 200);
  });
}

export function getUnderwritingCodeTables(token, tokenService: TokenService, injector: Injector): any {
  if (tokenService.doesUserHaveApplicationPermissions(["CIRRAS_UNDERWRITING.GET_TOP_LEVEL", "CIRRAS_UNDERWRITING.GET_CODE_TABLES"])) {
      let cirrasUnderwritingApiService = injector.get(CirrasUnderwritingAPIService);
      let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
      let ret = cirrasUnderwritingApiService.getCodeTableListResource(
          requestId,
          1,
          "no-cache",
          "no-cache",
          `Bearer ${token}`,
          moment().format(DATE_FORMATS.API_DATE),
          undefined,
      ).pipe(
          map(result => result),
          catchError(err => of(err))
      );
      return ret;
      
  } else {
      return of({codeTableList: []});
  }

}

export function populateUnderwritingCodeTableCache(codeTable) {
  try {
      CODE_TABLE_CACHE["codeTables"] = codeTable.reduce((acc, table) => ({...acc, [table.codeTableName]: table.codes}), {});
  } catch (error) {
      console.error(error);
  }
}
