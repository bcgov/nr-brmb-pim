import { ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges, ViewEncapsulation } from '@angular/core';
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
export class VerifiedYieldAmendmentListComponent implements OnChanges{
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

  ngOnChanges(changes: SimpleChanges) {
  
      if ( changes.fields && this.fields ) {
        this.setFieldsOptions()
      }

      if ( changes.cropCommodityList && this.cropCommodityList ) {
        this.setCropCommodityOptions()
      }
  }

  setCropCommodityOptions() {
    var self = this

    self.cropCommodityOptions = []
    self.cropVarietyOptions = []

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

      cc.cropVariety.forEach( cv => self.setCropVarietyOptions(cv) )

    })
  }

  setCropVarietyOptions(opt) {
    this.cropVarietyOptions.push ({
      cropCommodityId: opt.cropCommodityId,
      cropVarietyId: opt.cropVarietyId,
      cropVarietyName: opt.varietyName
    })
  }
 
  setFieldsOptions() {
    var self = this
    
    self.fieldOptions = []

    self.fieldOptions.push({
      fieldId: null,
      fieldLabel: "",
      verifiableCommodities: [],
      verifiableVarieties: []
    })

    self.fields.forEach(f => {
      self.fieldOptions.push({
        fieldId: f.fieldId,
        fieldLabel: f.fieldLabel + "(ID: " + f.fieldId + ")",
        verifiableCommodities: f.verifiableCommodities,
        verifiableVarieties: f.verifiableVarieties
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
      cropVarietyId: null,
      isPedigreeInd: false, // default
      fieldId: null,
      yieldPerAcre: null,
      acres: null,
      rationale: null,
      cropCommodityName: null,
      cropVarietyName: null,
      fieldLabel: null,
      deletedByUserInd: false // default
    })

    this.amendmentFormGroup = this.fb.group({
      verifiedYieldAmendmentGuid: [],
      verifiedYieldAmendmentCode: [VERIFIED_YIELD_AMENDMENT_CODE.APPRAISAL],
      verifiedYieldContractGuid: [this.verifiedYieldContractGuid],
      cropCommodityId: [''],
      cropVarietyId: [''],
      isPedigreeInd: [false],
      fieldId: [''],
      yieldPerAcre: [''],
      acres: [''],
      rationale: [''],
      cropCommodityName: [''],
      cropVarietyName: [''],
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
