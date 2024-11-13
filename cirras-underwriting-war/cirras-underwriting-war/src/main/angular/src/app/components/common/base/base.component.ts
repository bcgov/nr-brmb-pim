import {
    AfterViewInit,
    ChangeDetectorRef,
    Directive,
    Injectable,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges, ElementRef
} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {DomSanitizer, Title} from "@angular/platform-browser";
import {State, Store} from "@ngrx/store";
import {RootState} from "../../../store";
import {BaseComponentModel} from "./base.component.model";
import {
    CONSTANTS,
    DATE_FORMATS,
    getElementInnerText,
    hasValues,
    isElementTruncated
} from "../../../utils";
import { ResourcesRoutes } from "src/app/utils/constants";
import {AbstractControl, UntypedFormBuilder, FormControlDirective, FormControlName, UntypedFormGroup} from "@angular/forms";
import {ErrorMessages, getDisplayErrorMessage} from "../../../utils/error-messages";
import {ERROR_TYPE, ErrorState, LoadState, ApplicationState} from "../../../store/application/application.state";
import {ConnectionService} from "ngx-connection-service";
import {ActionItem} from "../base-wrapper/base-wrapper.component";
import {Overlay} from "@angular/cdk/overlay";
import {ApplicationStateService} from "../../../services/application-state.service";
import {AppConfigService, TokenService} from "@wf1/wfcc-core-lib";
import {PaginationInstance} from "ngx-pagination";
import {HttpClient} from "@angular/common/http";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {SCOPES_UI} from "../../../utils/scopes";
import {ErrorDialogComponent} from "../../dialogs/error-dialog/error-dialog.component";
import {setFormStateUnsaved} from "../../../store/application/application.actions";
import {SecurityUtilService} from "../../../services/security-util.service";
import { UwContract } from "src/app/conversion/models";
import { DecimalPipe } from "@angular/common";

const originFormControlNgOnChanges = FormControlDirective.prototype.ngOnChanges;
FormControlDirective.prototype.ngOnChanges = function () {
    if (this.valueAccessor && this.valueAccessor._elementRef && this.valueAccessor._elementRef.nativeElement ) {
        this.form.nativeElement = this.valueAccessor._elementRef.nativeElement;
    }
    return originFormControlNgOnChanges.apply(this, arguments);
};

const originFormControlNameNgOnChanges = FormControlName.prototype.ngOnChanges;
FormControlName.prototype.ngOnChanges = function () {
    const result = originFormControlNameNgOnChanges.apply(this, arguments);
    
    if (this.valueAccessor && this.valueAccessor._elementRef && this.valueAccessor._elementRef.nativeElement) {
        this.control.nativeElement = this.valueAccessor._elementRef.nativeElement;
    }
    return result;  
};

@Directive()
@Injectable()
export class BaseComponent implements OnInit, OnChanges, AfterViewInit {
    @Input() loadState: LoadState;
    @Input() errorState: ErrorState[];
    @Input() saveErrorState: ErrorState[];
    //@Input() growerContract: UwContract;

   //@Input() severeErrorState?: ErrorState[];
    displayLabel = "Data";
    showingErrorDialog = false;
    backRoute: ResourcesRoutes = null;
    backRouteQueryParams: any;
    backRouteLabel: string = null;
    summaryString: string = null;
    actionItems: ActionItem[] = null;
    ERROR_MESSAGES = ErrorMessages;
    isLocalSaving: boolean = false;
    isConnected: boolean = true;
    protected model: BaseComponentModel;
    public viewModel: BaseComponentModel;
    isLoading: boolean;
    componentId = "";

    CONSTANTS = CONSTANTS;

    SAVE_FAIL_TEXT = "Save failed";
    SAVE_SUCCESS_TEXT = "Saved Successfully";

    DISPLAY_ERROR_MESSAGE = getDisplayErrorMessage;

    DATE_FORMATS = DATE_FORMATS;

    SCOPES_UI = SCOPES_UI;

    unsavedChangesMessage = "Unsaved Changes";

    isUnsaved = false;
    // editable = this.userHasEditPermissions();
    getElementInnerText = getElementInnerText;
    isElementTruncated = isElementTruncated;

    config: PaginationInstance = {
        id: "Paginator",
        itemsPerPage: 5,
        currentPage: 1,
        totalItems: 0
    };

    public paginatorLabels: any = {
        previousLabel: "",
        nextLabel: "",
    };

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
        this.initModels();
        this.initializeConnectionService();
    }

    initComponentPreferences() {

    }

    initializeConnectionService() {
        this.connectionService.monitor().subscribe(isConnected => {
            this.isConnected = isConnected.hasNetworkConnection;
        });
    }

    getPagingConfig(): PaginationInstance {
        return this.config;
    }


    initModels() {

    }

    loadPage() {

    }

    save(etag?: string) {
        this.isLocalSaving = true;
    }

    reload() {
    }

    saveOverwrite(etag: string) {
        this.save(etag);
    }

    ngOnInit() {
        this.loadPage();
    }

    ngAfterViewInit() {
    }

    ngOnChanges(changes: SimpleChanges) {
        //console.log(changes);
        if (changes.loadState) {
            this.isLoading = changes.loadState.currentValue.isLoading;
            let previousValue = changes.loadState.previousValue;
            if (!this.isLoading && previousValue && previousValue.isLoading && !this.isLocalSaving) {
                this.updateView();
                this.invokeAfterLoaded();
            }
        }
        // if (changes.severeErrorState) { //severeErrorState doesn't seem to be populated
        //     this.isLocalSaving = false;
        // }
        if (changes.errorState) {
            this.errorState = changes.errorState.currentValue as ErrorState[];
            if (this.viewModel.formGroup) {
                this.viewModel.setErrorState(this.errorState);
            }
            if (this.isLocalSaving) {
                if (this.errorState && this.errorState.length > 0) {
                    this.isLocalSaving = false;
                    this.errorState.forEach(err2 => {
                        if (err2.type == ERROR_TYPE.FAILED_PRECONDITION) {
                            setTimeout(() => this.openErrorDialog(err2));
                            this.showingErrorDialog = true;
                        }
                    });
                    // if (this.SAVE_FAIL_TEXT && !this.showingErrorDialog) {
                    //     setTimeout(() => {
                    //         this.snackbarService.openFromComponent(WFSnackbarComponent, getSnackbarConfig(this.SAVE_FAIL_TEXT, WF_SNACKBAR_TYPES.ERROR));
                    //     });
                    // }

                }
            }
            // let err = this.errorState.find(err2 => err2.type == ERROR_TYPE.VALIDATION);
            // if (err) {
            //     // has a valphoneation error, scroll to top of screen to display error panel
            //     window.scrollTo(0, 0);
            // }
        }

        // console.log('end base is local saving', this.isLocalSaving);
    }

    openErrorDialog(err: ErrorState): void {
        let config = {
            data: err,
            autoFocus: false
        };
        //console.log(err);
        this.applicationStateService.resetViewportScale();
        const dialogRef = this.dialog.open(ErrorDialogComponent, config);

        dialogRef.afterClosed().subscribe(result => {
            this.showingErrorDialog = false;
            if ("refresh" === result) {
                this.reload();
            } else if ("overwrite" === result) {
                this.saveOverwrite(err.responseEtag);
            }
        });
    }

    invokeAfterLoaded() {

    }

    protected updateView(): void {
        //this.viewModel = this.model.clone();
    }


    // getIsMobileResolution(): boolean {
    //     return this.applicationStateService.getIsMobileResolution();
    // }

    redirectWithOAuth(redirectRoutePath: string) {
      let baseUrl = this.appConfigService.getConfig().application.baseUrl;
      let clientId = this.appConfigService.getConfig().webade.clientId;
      let authorizeUrl = this.appConfigService.getConfig().webade.oauth2Url;
      let authScopes = this.appConfigService.getConfig().webade.authScopes;
      let url = baseUrl;
      window.location.href = url;
    }

    getAsFormGroup(ac: AbstractControl): UntypedFormGroup {
        return ac as UntypedFormGroup;
    }

    disableSaveForm(form?: UntypedFormGroup): boolean {
        let fg = form ? form : this.viewModel.formGroup;
        return !fg.dirty || !fg.valid;
    }

    disableBatchSaveForm(arrayProperty: string, form?: UntypedFormGroup): boolean {
        let fg = form ? form : this.viewModel.formGroup;
        //Check form array for dirty flag
        let fgArray: UntypedFormGroup[] = fg?.controls[arrayProperty]['controls'];

        let arrayHasDirtyFlag = fgArray.some(contactFg => contactFg.dirty);
        let arrayHasInvalidFlag = fgArray.some(contactFg => !contactFg.valid);

        /*
         * Disable if
         * 1) the form array and main form is not dirty OR
         * 2) a form array form group is invalid
         */
        if (!arrayHasDirtyFlag && !fg.dirty) {
            return true;
        }
        if (arrayHasInvalidFlag) {
            return true;
        }

        return false;
    }

    unsavedForm(form?: UntypedFormGroup, arrayProperty?: string): boolean {
        //console.log("unsaved", this.componentId);
        let fg = form ? form : this.viewModel.formGroup;
        if (arrayProperty) {
            this.unsavedBatchForm(arrayProperty);
        } else {
            this.doUnsavedStateUpdateIfNeeded(this.componentId, fg.dirty);
        }
        return fg.dirty;
    }

    unsavedBatchForm(arrayProperty: string): boolean {
        let fg = this.viewModel.formGroup;
        //Check form array for dirty flag
        let fgArray: UntypedFormGroup[] = fg?.controls[arrayProperty]['controls'];
        let arrayHasDirtyFlag = fgArray.some(contactFg => contactFg.dirty);
        let hasAddedUnsavedItem = this.hasAddedUnsavedItemNotBlank(fg, arrayProperty);
        //console.log("arrayHasDirtyFlag", arrayHasDirtyFlag, "fgDirty", fg.dirty, "hasAddedUnsavedItem", hasAddedUnsavedItem);
        this.doUnsavedStateUpdateIfNeeded(this.componentId, arrayHasDirtyFlag || fg.dirty || hasAddedUnsavedItem);
        return this.isUnsaved;
    }

    doUnsavedStateUpdateIfNeeded(componentId: string, newUnsavedState: boolean) {
        let prevUnsaved = this.isUnsaved; //save old value for comparison
        //console.log(componentId, "prev", prevUnsaved, "new", newUnsavedState);
        this.isUnsaved = newUnsavedState;
        if (componentId && prevUnsaved != this.isUnsaved) { //check if first time set to unsaved
            this.store.dispatch(setFormStateUnsaved(componentId, this.isUnsaved));
        }
    }

    hasAddedUnsavedItemNotBlank(fgMain: UntypedFormGroup, arrayProperty: string) {
        let controls = fgMain?.controls[arrayProperty]['controls'];
        let ret = controls.some(ac => {
                let fg: UntypedFormGroup = <UntypedFormGroup>ac;
                if (!fg.get("id").value && controls.length > 1) { //not a default empty entry
                    //console.log("not default entry");
                    return true;
                } else if (!fg.get("id").value && controls.length == 1) { //check if empty entry
                    let item = fg.getRawValue();
                    if (!hasValues(item)) {
                        //console.log("is default empty entry");
                        return false;
                    } else {
                        //console.log("default entry with info");
                        return true;
                    }
                } else {
                    //console.log("existing entry");
                    return false;
                }
            }
        );
        return ret;
    }

    getActionRowClass() {
        return 'space-between';
    }
}
