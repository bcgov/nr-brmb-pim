import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
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
export class VerifiedYieldAmendmentListComponent implements OnInit{
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

  ngOnInit() {
    this.setCropCommodityOptions()
    this.setFieldsOptions()
  }


  setCropCommodityOptions() {
    var self = this

    self.cropCommodityList.collection.forEach(cc => {
      self.cropCommodityOptions.push({
        cropCommodityId: cc.cropCommodityId,
        cropCommodityName: cc.commodityName,
        isPedigreeInd: false
      })

      // add pedigree flag for insurable crops
      if (cc.isCropInsuranceEligibleInd) {
        self.cropCommodityOptions.push({
          cropCommodityId: cc.cropCommodityId,
          cropCommodityName: cc.commodityName + " - Pedigree",
          isPedigreeInd: true
        })
      }

    })
  }

  setFieldsOptions() {
    var self = this
    
    self.fieldOptions.push({
      fieldId: null,
      fieldLabel: "",
      verifiableCommodities: []
    })

    self.fields.forEach(f => {
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
