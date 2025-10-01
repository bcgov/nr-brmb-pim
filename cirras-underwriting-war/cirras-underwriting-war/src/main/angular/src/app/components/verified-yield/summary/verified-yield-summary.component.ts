import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { Store } from '@ngrx/store';
import { VerifiedYieldSummary } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { VERIFIED_YIELD_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';

@Component({
    selector: 'verified-yield-summary',
    templateUrl: './verified-yield-summary.component.html',
    styleUrl: './verified-yield-summary.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class VerifiedYieldSummaryComponent {
  @Input() summary: VerifiedYieldSummary;
  @Input() summariesFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;

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
        productionAcres: [this.summary.productionAcres],
        harvestedYield: [this.summary.harvestedYield],
        harvestedYieldPerAcre: [this.summary.harvestedYieldPerAcre],
        appraisedYield: [this.summary.appraisedYield],
        assessedYield: [this.summary.assessedYield],
        yieldToCount: [this.summary.yieldToCount],
        yieldPercentPy: [this.summary.yieldPercentPy],
        productionGuarantee: [this.summary.productionGuarantee],
        probableYield: [this.summary.probableYield],
        cropCommodityName: [this.summary.cropCommodityName],
        totalInsuredAcres: [this.summary.totalInsuredAcres],
        uwComments: [this.summary.uwComments] 
      });
      this.summariesFormArray.push(this.summaryFormGroup);
    }

      onVerifiedYieldCommentsDone(uwComments: UnderwritingComment[]) {
        this.summary.uwComments = uwComments;
        this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
      }

    getYieldPercentPy(){
      
      if(this.summary.yieldPercentPy != null){
        return this.summary.yieldPercentPy * 100;
      }
      
      return this.summary.yieldPercentPy;
      
    }

    ytcIsLessThanProductionGuarantee():boolean {
      if(this.summary.yieldToCount != null && this.summary.productionGuarantee != null && this.summary.yieldToCount < this.summary.productionGuarantee ){
        return true
      } 
      return false
    }
    
    productionAcresMatchInsuredAcres():boolean {
      if(this.summary.productionAcres && this.summary.totalInsuredAcres && this.summary.productionAcres !== this.summary.totalInsuredAcres ){
        return false
      } 
      return true
    }

}
