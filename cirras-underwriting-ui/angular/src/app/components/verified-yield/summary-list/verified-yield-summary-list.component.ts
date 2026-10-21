import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { VerifiedYieldSummary } from 'src/app/conversion/models-yield';
import { NgIf, NgStyle, NgFor } from '@angular/common';
import { MatTooltip } from '@angular/material/tooltip';
import { MatIcon } from '@angular/material/icon';
import { VerifiedYieldSummaryComponent } from '../summary/verified-yield-summary.component';

@Component({
    selector: 'verified-yield-summary-list',
    templateUrl: './verified-yield-summary-list.component.html',
    styleUrl: './verified-yield-summary-list.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgIf, MatTooltip, MatIcon, NgStyle, NgFor, VerifiedYieldSummaryComponent]
})
export class VerifiedYieldSummaryListComponent {
    @Input() summaries: Array<VerifiedYieldSummary>
    @Input() summariesFormArray: UntypedFormArray;
    @Input() isUnsaved: boolean;
}
