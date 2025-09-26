import {ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, Output} from "@angular/core";

import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder} from "@angular/forms";
import {Store} from "@ngrx/store";
import {ApplicationStateService} from "../../../services/application-state.service";
import {RootState} from "../../../store";
import {ErrorState} from "../../../store/application/application.state";
import {MatExpansionPanel} from "@angular/material/expansion";

@Component({
    selector: "cirras-underwriting-base-expansion-panel",
    templateUrl: "./base-expansion-panel.component.html",
    styleUrls: ["../base/base.component.scss", "./base-expansion-panel.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class BaseExpansionPanelComponent {
    mobile: boolean;

    @Output() primaryActionButtonClick: EventEmitter<any> = new EventEmitter();
    @Output() secondaryActionButtonClick: EventEmitter<any> = new EventEmitter();
    @Output() headerActionButtonClick: EventEmitter<any> = new EventEmitter();

    @Input() errorState: ErrorState[];
    @Input() isLoading: boolean = false;
    @Input() expanded?: boolean = true;

    @Input() titleLabel: string;
    @Input() panelDisabled?: boolean = false;

    @Input() primaryButtonDisabled?: boolean = false;
    @Input() primaryButtonLabel?: string;
    @Input() primaryButtonHidden?: boolean = false;

    @Input() secondaryButtonDisabled?: boolean = false;
    @Input() secondaryButtonLabel?: string;
    @Input() secondaryButtonHidden?: boolean = false;
    @Input() secondaryButtonIcon?: string = 'cancel';

    @Input() headerButtonDisabled?: boolean = false;
    @Input() headerButtonLabel?: string;
    @Input() headerButtonHidden?: boolean = false;

    @Input() warningMessage?: string;
    @Input() simplePanel?: boolean = false;

    constructor(
        protected applicationStateService: ApplicationStateService,
        protected sanitizer: DomSanitizer,
        protected cdr: ChangeDetectorRef,
        protected fb: UntypedFormBuilder,
        protected store: Store<RootState>
    ) {
        this.mobile = applicationStateService.getIsMobileResolution();
        this.initModels();
    }

    initModels() {

    }

    secondaryActionButtonClicked(): void {
      this.secondaryActionButtonClick.emit();
    }

    primaryActionButtonClicked(): void {
        this.primaryActionButtonClick.emit();
    }

    headerActionButtonClicked(panel: MatExpansionPanel): void {
        if (!this.panelDisabled && !panel.expanded) {
            panel.open();
            this.cdr.detectChanges();
        }
        this.headerActionButtonClick.emit();
    }
}
