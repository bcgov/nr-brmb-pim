import {
    AfterViewInit,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    EventEmitter,
    Inject,
    Injector,
    Input,
    OnChanges,
    OnDestroy,
    Output,
    SimpleChanges
} from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DomSanitizer } from "@angular/platform-browser";
import { Store } from "@ngrx/store";
import { ApplicationStateService } from "../../../services/application-state.service";
//import { NavigationUtilService } from "../../../services/navigation-util.service";
import { SecurityUtilService } from "../../../services/security-util.service";
import { RootState } from "../../../store";
import { setFormStateUnsaved } from "../../../store/application/application.actions";
import { ErrorState, LoadState } from "../../../store/application/application.state";
import { CONSTANTS, addRemoveCdkOverlayClass, hasValues } from "../../../utils";
import { ErrorMessages } from "../../../utils/error-messages";
import { BaseComponentModel } from "../../common/base/base.component.model";

export enum DIALOG_TYPE {
    INFO,
    ERROR
}

@Component({
    selector: "cuws-base-dialog",
    templateUrl: "./base-dialog.component.html",
    styleUrls: ["../../common/base/base.component.scss", "./base-dialog.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class BaseDialogComponent implements AfterViewInit, OnChanges, OnDestroy {
    @Input() disableDrag?: boolean = false;
    @Input() loadState?: LoadState;
    @Input() errorState?: ErrorState[];
    @Input() titleLabel: string;
    @Input() type?: DIALOG_TYPE = DIALOG_TYPE.INFO;

    @Output() preconditionOkButtonClick?: EventEmitter<any> = new EventEmitter();

    model: BaseComponentModel;
    viewModel: BaseComponentModel;
    isLocalSaving: boolean = false;
    isLoading: boolean = false;
    isUnsaved = false;
    componentId = null;
    unsavedChangesMessage = "Unsaved Changes";
    mobile: boolean;

    DIALOG_TYPE_OBJ = DIALOG_TYPE;
    ERROR_MESSAGES = ErrorMessages;
    CONSTANTS = CONSTANTS;

    constructor(
        public dialogRef: MatDialogRef<BaseDialogComponent>,
        protected applicationStateService: ApplicationStateService,
        protected sanitizer: DomSanitizer,
        protected fb: UntypedFormBuilder,
        public cdr: ChangeDetectorRef,
        public securityUtilService: SecurityUtilService,
        //public navigationUtilService: NavigationUtilService,
        protected store: Store<RootState>,
        protected injector: Injector,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        dialogRef.disableClose = true;        
        this.mobile = applicationStateService.getIsMobileResolution();

        this.initModels();
    }

    initModels() {}

    handleAccessibiltyOnLoad() {
        let mainEl = document.getElementById("wfrmMain");
        if (mainEl) {
            mainEl.removeAttribute("role");
            mainEl.setAttribute("inert", "");
            setTimeout(() =>
                mainEl.setAttribute("aria-hidden", "true")
            );
        }

        let overlayEl = document.querySelector(`.cdk-overlay-container`);
        if (overlayEl) {
            overlayEl.setAttribute("role", "main");
        }

        document.querySelector(`.cdk-overlay-container`)?.querySelectorAll(`[aria-hidden="true"]`).forEach(el => {
            el.setAttribute("inert", "");
        });
    }

    handleAccessibiltyOnDestroy() {
        let mainEl = document.getElementById("wfrmMain");
        if (mainEl) {
            mainEl.setAttribute("role", "main");
            mainEl.removeAttribute("inert");
            setTimeout(() =>
                mainEl.removeAttribute("aria-hidden")
            );

        }

        document.querySelector(`.cdk-overlay-container`)?.querySelectorAll(`[aria-hidden="true"]`).forEach(el => {
            el.removeAttribute("inert");
        });
    }

    ngAfterViewInit() {
        addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
        this.handleAccessibiltyOnLoad();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.loadState) {
            this.isLoading = changes.loadState.currentValue ? changes.loadState.currentValue.isLoading : false;
        }
        if (changes.errorState) {
            this.errorState = changes.errorState.currentValue as ErrorState[];
        }
    }

    ngOnDestroy() {
        this.handleAccessibiltyOnDestroy();
    }

    protected updateView(): void {
        this.viewModel = this.model.clone();
    }

    protected convertFromForm() {
        return this.viewModel.formGroup.value;
    }

    ok(ret?: any): void {
        this.dialogRef.close(ret);
    }

    cancel() {
        this.dialogRef.close();
    }

    setLocalSaving(value: boolean) {
        this.isLocalSaving = value;
    }

    setLoading(value: boolean) {
        this.isLoading = value;
    }

    disableSaveForm(form?: UntypedFormGroup): boolean {
        let fg = form ? form : this.viewModel.formGroup;
        // console.log(fg);
        return !fg.dirty || !fg.valid;
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

    protected touchAndValidateAllControls() {
        if (this.viewModel) {
            Object.keys(this.viewModel.formGroup.controls).forEach(key => {
                let control = this.viewModel.formGroup.get(key);
                if (control instanceof UntypedFormArray) {
                    if (control.controls.length > 0) {
                        control["controls"].forEach(fg => {
                            let formGroup = fg as UntypedFormGroup;
                            Object.keys(formGroup.controls).forEach(key2 => {
                                let arrayControlItem = formGroup.get(key2);
                                arrayControlItem.markAsTouched();
                                arrayControlItem.updateValueAndValidity();
                            });

                        });
                    }
                }
                control.markAsTouched();
                control.updateValueAndValidity();
            });
        }
    }

    preconditionOkButtonClicked(eventData): void {
        this.preconditionOkButtonClick.emit(eventData);
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
}
