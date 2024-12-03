import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { AnnualField, CropCommodityList } from 'src/app/conversion/models';
import { VERIFIED_YIELD_AMENDMENT_CODE } from 'src/app/utils/constants';

@Component({
  selector: 'verified-yield-amendment-list',
  templateUrl: './verified-yield-amendment-list.component.html',
  styleUrl: './verified-yield-amendment-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldAmendmentListComponent implements OnChanges{
  @Input() verifiedYieldContractGuid: string;
  @Input() amendments: Array<VerifiedYieldAmendment>;
  @Input() amendmentsFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;
  @Input() cropCommodityList: CropCommodityList
  @Input() fields: Array<AnnualField>;

  amendmentFormGroup: UntypedFormGroup;
  fieldOptions = [];
  cropCommodityOptions = [];

  constructor(private fb: UntypedFormBuilder,
    ){
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.cropCommodityList && this.cropCommodityList) {
      this.setCropCommodityOptions()
    }

    if (changes.fields && this.fields) {
      this.setFieldsOptions()
    }

  }

  setCropCommodityOptions() {
    //TODO - create a list of crop commodity options including pedigree to load in the autocomplete
  }

  setFieldsOptions() {
    var self = this

    self.fieldOptions.push({
      fieldId: null,
      fieldLabel: "",
      verifiableCommodities: []
    })

    this.fields.forEach(f => {
      self.fieldOptions.push({
        fieldId: f.fieldId,
        fieldLabel: f.fieldLabel + "(ID: " + f.fieldId + ")",
        verifiableCommodities: f.verifiableCommodities
      })
    })
  }


  addAmendment() {

    if (!this.amendments) {
      this.amendments = []
    }
    
    this.amendments.push({
      verifiedYieldAmendmentGuid: null, // or create your own temp guid ??
      verifiedYieldAmendmentCode: VERIFIED_YIELD_AMENDMENT_CODE.APPRAISAL, // default
      verifiedYieldContractGuid: this.verifiedYieldContractGuid, 
      cropCommodityId: null,
      isPedigreeInd: false,
      fieldId: null,
      yieldPerAcre: null,
      acres: null,
      rationale: null,
      cropCommodityName: null,
      fieldLabel: null,
      deletedByUserInd: false
    })

    this.amendmentFormGroup = this.fb.group({
      verifiedYieldAmendmentGuid: [],
      verifiedYieldAmendmentCode: [VERIFIED_YIELD_AMENDMENT_CODE.APPRAISAL],
      verifiedYieldContractGuid: [this.verifiedYieldContractGuid],
      cropCommodityId: [''],
      isPedigreeInd: [false],
      fieldId: [''],
      yieldPerAcre: [''],
      acres: [''],
      rationale: [''],
      cropCommodityName: [''],
      fieldLabel: [''],
      deletedByUserInd: [false]
    });

    this.amendmentsFormArray.push(this.amendmentFormGroup);
  }
}
