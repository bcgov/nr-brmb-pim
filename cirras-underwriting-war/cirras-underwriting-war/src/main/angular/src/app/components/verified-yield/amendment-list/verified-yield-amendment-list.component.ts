import { ChangeDetectionStrategy, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { AnnualField, CropCommodityList } from 'src/app/conversion/models';
import { INSURANCE_PLAN, VERIFIED_YIELD_AMENDMENT_CODE } from 'src/app/utils/constants';

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
  @Input() insurancePlanId: number;

  amendmentFormGroup: UntypedFormGroup;
  fieldOptions = [];
  cropCommodityOptions = [];
  cropVarietyOptions = []

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

  // TODO
  // also add autocomplete for forage varieties and show them in case of forafe
  // ensure they are added to the save process
  // setCropVarietyOptions() {
  //   var self = this

  //   self.cropCommodityList.collection.forEach(cc => {

  //     self.cropVarietyOptions.push({
  //       cropCommodityId: cc.cropCommodityId,
  //       cropVarietyId: this.cropVarietyOptions.cropViretyid.ID,
  //       varietyName: CROP_COMMODITY_UNSPECIFIED.NAME
  //     })

  //   })
  // }


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
      verifiedYieldAmendmentGuid: null,
      verifiedYieldAmendmentCode: VERIFIED_YIELD_AMENDMENT_CODE.APPRAISAL, // default
      verifiedYieldContractGuid: this.verifiedYieldContractGuid, 
      cropCommodityId: null,
      isPedigreeInd: false, // default
      fieldId: null,
      yieldPerAcre: null,
      acres: null,
      rationale: null,
      cropCommodityName: null,
      fieldLabel: null,
      deletedByUserInd: false // default
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

      isGrainPolicy() {
        if (this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
          return true
        } else {
          return false
        }
      }

}
