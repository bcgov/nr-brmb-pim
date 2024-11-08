import {ChangeDetectorRef, Component, OnDestroy, OnInit} from "@angular/core";
import {BaseComponent} from "../common/base/base.component";
import { ActivatedRoute, Router } from "@angular/router";
import { DomSanitizer, Title } from "@angular/platform-browser";
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { UntypedFormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { ApplicationStateService } from "src/app/services/application-state.service";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { AppConfigService, TokenService } from "@wf1/core-ui";
import { ConnectionService } from "ngx-connection-service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Overlay } from "@angular/cdk/overlay";
import { HttpClient } from "@angular/common/http";
import { DecimalPipe } from "@angular/common";

@Component({
    selector: "cirras-underwriting-sign-out-page",
    templateUrl: "./sign-out-page.component.html",
    styleUrls: ["./sign-out-page.component.scss"]
})
export class SignOutPageComponent extends BaseComponent implements OnInit, OnDestroy {

    constructor(protected router: Router,
        protected route: ActivatedRoute,
        protected sanitizer: DomSanitizer,
        protected store: Store<RootState>,
        protected fb: UntypedFormBuilder,
        protected dialog: MatDialog,
        protected applicationStateService: ApplicationStateService,
        public securityUtilService: SecurityUtilService,                
        protected tokenService: TokenService,
        protected connectionService: ConnectionService,
        protected snackbarService: MatSnackBar,
        protected overlay: Overlay,
        protected cdr: ChangeDetectorRef,
        protected appConfigService: AppConfigService,
        protected http: HttpClient,
        protected titleService: Title,
        protected decimalPipe: DecimalPipe) {
        super(router, route, sanitizer, store, fb, dialog, applicationStateService, securityUtilService, tokenService, connectionService, snackbarService, overlay, cdr, appConfigService, http, titleService, decimalPipe);
    }

    ngOnInit() {
        localStorage.removeItem('oauth');
        const appConfig = (<any>this.appConfigService.getConfig()).application;
        const webadeConfig = (<any>this.appConfigService.getConfig()).webade;
        const siteminderUrlPrefix = webadeConfig.siteminderUrlPrefix
        let url = siteminderUrlPrefix + appConfig.baseUrl + "&retnow=1";
        window.location.href = url;
    }

    ngOnDestroy() {

    }

}
