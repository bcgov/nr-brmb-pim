import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { CropCommodityList } from 'src/app/conversion/models';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { makeNumberOnly } from 'src/app/utils';
import { getCodeOptions } from 'src/app/utils/code-table-utils';

@Component({
  selector: 'verified-yield-amendment',
  templateUrl: './verified-yield-amendment.component.html',
  styleUrls: ['./verified-yield-amendment.component.scss', '../../../../styles/_inventory.scss' ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldAmendmentComponent implements OnChanges {

  @Input() amendment: VerifiedYieldAmendment;
  @Input() amendmentsFormArray: UntypedFormArray;
  @Input() decimalPrecision: number;
  @Input() cropCommodityList: CropCommodityList

  amendmentFormGroup: UntypedFormGroup;

  amendmentOptions = getCodeOptions("verified_yield_amendment_code"); // get the amendment code
  fieldOptions = [];
  cropCommodityOptions = [];

  constructor(private fb: UntypedFormBuilder,
    private store: Store<RootState>,
    public securityUtilService: SecurityUtilService, 
    private decimalPipe: DecimalPipe ){
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.amendment && this.amendment) {
      this.setupForm()
    }

    if (changes.cropCommodityList && this.cropCommodityList) {
      this.setupCropCommodityOptions()
    }
  }

  setupForm() {
    this.amendmentFormGroup = this.fb.group({
      verifiedYieldAmendmentGuid: [this.amendment.verifiedYieldAmendmentGuid],
      verifiedYieldAmendmentCode: [this.amendment.verifiedYieldAmendmentCode],
      verifiedYieldContractGuid: [this.amendment.verifiedYieldContractGuid],
      cropCommodityId: [this.amendment.cropCommodityId],
      isPedigreeInd: [this.amendment.isPedigreeInd],
      fieldId: [this.amendment.fieldId],
      yieldPerAcre: [this.amendment.yieldPerAcre],
      acres: [this.amendment.acres],
      rationale: [this.amendment.rationale],
      cropCommodityName: [this.amendment.cropCommodityName],
      fieldLabel: [this.amendment.fieldLabel],
      deletedByUserInd: [this.amendment.deletedByUserInd]
    });
    this.amendmentsFormArray.push(this.amendmentFormGroup);
  }

  setupCropCommodityOptions() {
    //TODO - create a list of crop commodity options including pedigree to load in the autocomplete

  }

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

  updateYieldPerAcre() {
    //TODO
  }

  updateAcres() {
    //TODO
  }

  onDeleteAmendment() {
    
  }
}
