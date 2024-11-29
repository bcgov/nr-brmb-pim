import {Component} from "@angular/core";
import {TokenService} from "@wf1/wfcc-core-lib";
import {BaseComponent} from "../common/base/base.component";
import { AppConfigService } from "@wf1/wfcc-core-lib";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer, Title} from "@angular/platform-browser";
import {State, Store} from "@ngrx/store";
import {UntypedFormBuilder} from "@angular/forms";
import {ConnectionService} from "ngx-connection-service";
import {Overlay} from "@angular/cdk/overlay";
import {ApplicationStateService} from "../../services/application-state.service";
import { HttpClient } from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {RootState} from "../../store";
import {SecurityUtilService} from "../../services/security-util.service";
import {
  ChangeDetectorRef,
} from "@angular/core";
import { ApplicationState } from "src/app/store/application/application.state";
import { DecimalPipe } from "@angular/common";

@Component({
    selector: "cirras-underwriting-unauthorized-page",
    templateUrl: "./unauthorized-page.component.html",
    styleUrls: ["./unauthorized-page.component.scss"]
}) 
export class UnauthorizedPageComponent extends BaseComponent {
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
    protected decimalPipe: DecimalPipe
    ) {
    super(
      router,
      route,
      sanitizer,
      store,
      fb,
      dialog,
      applicationStateService,
      securityUtilService,                
      tokenService,
      connectionService,
      snackbarService,
      overlay,
      cdr,
      appConfigService,
      http,
      titleService,
      decimalPipe
    )
}  
}