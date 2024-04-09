import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    Output,
    SimpleChanges,
    ViewChild
} from "@angular/core";
import {ERROR_TYPE, ErrorState} from "../../../store/application/application.state";
import {getDisplayErrorMessage} from "../../../utils/error-messages";
import scrollIntoView from "scroll-into-view-if-needed";

@Component({
    selector: "cirras-underwriting-base-error-panel",
    templateUrl: "./error-panel.component.html",
    styleUrls: ["../base/base.component.scss", "./error-panel.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErrorPanelComponent implements OnChanges {
    @ViewChild("errorContainer") errorContainer: ElementRef;
    @Input() errorState: ErrorState[];
    @Input() titleLabel: string;
    @Input() errorType: "WARNING" | "ERROR";
    ERROR_TYPE = ERROR_TYPE;
    DISPLAY_ERROR_MESSAGE = getDisplayErrorMessage;
    errorStatesToDisplay: ErrorState[];
    selectedAction: string;

    constructor(protected cdr: ChangeDetectorRef) {

    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.errorState) {
            this.errorState = <ErrorState[]>changes.errorState.currentValue;
            //don't show 412 errors since thtose are shown in a dialog
            if (this.errorState && this.errorState.length) {
                this.errorStatesToDisplay = [...this.errorState];

                const index: number = this.errorState.findIndex(err => err.type == ERROR_TYPE.FAILED_PRECONDITION);
                if (index !== -1) {
                    this.errorStatesToDisplay.splice(index, 1);
                    this.errorState = this.errorStatesToDisplay;
                }
                if (this.errorState && this.errorState.length) {
                    this.cdr.detectChanges();
                    const node = document.getElementsByClassName('error-panel')[0];
                    scrollIntoView(node, { behavior: 'smooth', scrollMode: 'if-needed' });
                }
            }

        }
    }

    //Allow setting error state manually - used for dialogs
    setErrorState(errorState: ErrorState[]) {
        this.errorState = errorState;
        this.cdr.detectChanges();
    }

    clearErrorState() {
        this.errorState = [];
        this.cdr.detectChanges();
    }
}
