import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { VerifiedYieldSummary } from 'src/app/conversion/models-yield';

@Component({
    selector: 'verified-yield-summary-list',
    templateUrl: './verified-yield-summary-list.component.html',
    styleUrl: './verified-yield-summary-list.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class VerifiedYieldSummaryListComponent {
    @Input() summaries: Array<VerifiedYieldSummary>
    @Input() summariesFormArray: UntypedFormArray;
    @Input() isUnsaved: boolean;
}
