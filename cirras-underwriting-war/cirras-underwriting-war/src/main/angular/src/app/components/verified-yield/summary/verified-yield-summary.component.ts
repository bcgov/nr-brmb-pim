import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { VerifiedYieldSummary } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';

@Component({
  selector: 'verified-yield-summary',
  templateUrl: './verified-yield-summary.component.html',
  styleUrl: './verified-yield-summary.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldSummaryComponent {
  @Input() summary: VerifiedYieldSummary;
  @Input() summariesFormArray: UntypedFormArray;

  summaryFormGroup: UntypedFormGroup;
  
  constructor(private fb: UntypedFormBuilder,
    private store: Store<RootState>,
    public securityUtilService: SecurityUtilService, 
    ) { }

  ngOnChanges(changes: SimpleChanges) {
      if (changes.summary && this.summary) {
        this.setupForm()
      }
    }
  
    setupForm() {
      this.summaryFormGroup = this.fb.group({
        verifiedYieldSummaryGuid: [this.summary.verifiedYieldSummaryGuid],
        verifiedYieldContractGuid: [this.summary.verifiedYieldContractGuid],
        cropCommodityId: [this.summary.cropCommodityId],
        isPedigreeInd: [this.summary.isPedigreeInd],
        // TODO: insuread acres is missing
        harvestedYield: [this.summary.harvestedYield],
        harvestedYieldPerAcre: [this.summary.harvestedYieldPerAcre],
        appraisedYield: [this.summary.appraisedYield],
        assessedYield: [this.summary.assessedYield],
        yieldToCount: [this.summary.yieldToCount],
        yieldPercentPy: [this.summary.yieldPercentPy],
        productionGuarantee: [this.summary.productionGuarantee],
        probableYield: [this.summary.probableYield],
        cropCommodityName: [this.summary.cropCommodityName],
        uwComments: [this.summary.uwComments] // Array<UnderwritingComment>
      });
      this.summariesFormArray.push(this.summaryFormGroup);
    }


}
