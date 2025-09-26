import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { VERIFIED_YIELD_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';
import { makeNumberOnly, makeTitleCase } from 'src/app/utils';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { roundUpDecimalAcres, roundUpDecimalYield } from '../../inventory/inventory-common';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
    selector: 'verified-yield-amendment',
    templateUrl: './verified-yield-amendment.component.html',
    styleUrls: ['./verified-yield-amendment.component.scss', '../../../../styles/_inventory.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    standalone: false
})
export class VerifiedYieldAmendmentComponent implements OnChanges {

  @Input() amendment: VerifiedYieldAmendment;
  @Input() amendmentsFormArray: UntypedFormArray;

  @Input() fieldOptions;
  @Input() cropCommodityOptions;
  @Input() cropVarietyOptions;
  @Input() insurancePlanId: number;

  amendmentFormGroup: UntypedFormGroup;

  amendmentOptions = getCodeOptions("verified_yield_amendment_code"); // get the amendment code
  
  filteredFieldOptions = [];
  filteredCropCommodityOptions = [];
  filteredCropVarietyOptions = [];

  constructor(private fb: UntypedFormBuilder,
    private store: Store<RootState>,
    public securityUtilService: SecurityUtilService){
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.amendment && this.amendment) {
      this.setForm()
    }
  }

  setForm() {
    this.amendmentFormGroup = this.fb.group({
      verifiedYieldAmendmentGuid: [this.amendment.verifiedYieldAmendmentGuid],
      verifiedYieldAmendmentCode: [this.amendment.verifiedYieldAmendmentCode],
      verifiedYieldContractGuid: [this.amendment.verifiedYieldContractGuid],
      cropCommodityCtrl:          [ { 
        cropCommodityId: ( this.amendment.cropCommodityId) ? this.amendment.cropCommodityId : "", 
        cropCommodityName: ( this.amendment.cropCommodityName) ? this.amendment.cropCommodityName : "",
        isPedigreeInd: ( this.amendment.isPedigreeInd )
      } ],
      cropVarietyCtrl:          [ { 
        cropCommodityId: ( this.amendment.cropCommodityId) ? this.amendment.cropCommodityId : "", 
        cropVarietyId: ( this.amendment.cropVarietyId) ? this.amendment.cropVarietyId : "",
        cropVarietyName: ( this.amendment.cropVarietyName )
      } ],
      fieldCtrl:          [ { 
        fieldId: ( this.amendment.fieldId) ? this.amendment.fieldId : "", 
        fieldLabel: ( this.amendment.fieldLabel) ? (this.amendment.fieldLabel + "(ID: " + this.amendment.fieldId +")" ) :"",
      } ],
      yieldPerAcre: [this.amendment.yieldPerAcre],
      acres: [this.amendment.acres],
      rationale: [this.amendment.rationale],
      deletedByUserInd: [this.amendment.deletedByUserInd]
    });
    this.amendmentsFormArray.push(this.amendmentFormGroup);
  }

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

  updateAmendmentCode( event) {

    this.amendment.verifiedYieldAmendmentCode = event.value
    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true))

  }

  updateYieldPerAcre() {

    let yieldPerAcre

    if (this.amendmentFormGroup.value.yieldPerAcre == "") {
      yieldPerAcre = null
    } else {
      yieldPerAcre = roundUpDecimalYield(this.amendmentFormGroup.value.yieldPerAcre, 3)
    }

    this.amendment.yieldPerAcre = yieldPerAcre
    this.amendmentFormGroup.controls['yieldPerAcre'].setValue(yieldPerAcre)
    
    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true))
  }

  updateAcres() {
    let acres = roundUpDecimalAcres(this.amendmentFormGroup.value.acres) 
    this.amendment.acres = parseFloat(acres.toString()) || null;
    this.amendmentFormGroup.controls['acres'].setValue(acres)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

  updateRationale(){
    let rationale = this.amendmentFormGroup.value.rationale;
    this.amendment.rationale = rationale || null;

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true)); 
  }

  onDeleteAmendment() {
    // marks the row as deleted and hide it in the html
    this.amendment.deletedByUserInd = true 
    this.amendmentFormGroup.controls['deletedByUserInd'].setValue(true)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true))

  }

  // field autocomplete functions
  displayFieldsFn(fld: any): string {
    return fld && fld.fieldLabel ? fld.fieldLabel  : '';
  }

  getFieldOptions() {
    // prepare the list of fields based on the selected commodity id
    let fldOpt = [] 
    let cropCommodityId = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.cropCommodityId
    let isPedigreeInd = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.isPedigreeInd
    let cropVarietyId = this.amendmentFormGroup.controls['cropVarietyCtrl'].value.cropVarietyId
    
    if (cropVarietyId) {
      // return only the fields that have the selected variety
      // applicable to forage

      this.fieldOptions.forEach( fld => {
        
        let elem = fld.verifiableVarieties.filter(x =>  x.cropVarietyId == cropVarietyId)
        
        if (elem && elem.length > 0) {
          fldOpt.push({
            fieldId: fld.fieldId,
            fieldLabel: fld.fieldLabel,
          })
        }
      })

    } else if (cropCommodityId) {
      // return only the fields that have the selected commodity
      // applicable for grain

      this.fieldOptions.forEach( fld => {
        let elem = fld.verifiableCommodities.filter(x => ( x.cropCommodityId == cropCommodityId && x.isPedigreeInd == isPedigreeInd))
        
        if (elem && elem.length > 0) {
          fldOpt.push({
            fieldId: fld.fieldId,
            fieldLabel: fld.fieldLabel,
          })
        }
      })

    } else {
      
      //return all fields
      fldOpt = this.fieldOptions
    }

    return fldOpt.slice()
  }

  fieldFocus() {
    this.filteredFieldOptions = this.getFieldOptions()
  }
  
  searchField(value){

      const fieldLabel = (( typeof value === 'string') ? value : value?.fieldLabel)
      let fldOptions = this.getFieldOptions()

      if (fieldLabel) {
        const filterValue = fieldLabel.toLowerCase()

        this.filteredFieldOptions = fldOptions.filter(option => option.fieldLabel.toLowerCase().includes(filterValue) )
        
      } else {
        this.filteredFieldOptions = fldOptions
      }
  }

  updateField(){

    let fieldId = this.amendmentFormGroup.controls['fieldCtrl'].value.fieldId
    this.amendment.fieldId = fieldId || null;

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true)); 
  }

  // commodity autocomplete functions
  displayCommoditiesFn(cmdty: any): string {
    return cmdty && cmdty.cropCommodityName ? makeTitleCase(cmdty.cropCommodityName)  : '';
  }

  getCommodityOptions() {
    
    let cmdtOpt = [] 
    let fieldId = this.amendmentFormGroup.controls['fieldCtrl'].value.fieldId
    
    if (fieldId) {
      // return only the commodities for that field
      let elem = this.fieldOptions.find(fld => fld.fieldId == fieldId)

      if (elem && elem.verifiableCommodities.length > 0) {
        cmdtOpt = elem.verifiableCommodities.slice()
      }

    } else {
      
      //return all fields
      cmdtOpt = this.cropCommodityOptions.slice()
    }

    return cmdtOpt
  }

  commoditiesFocus() {
    this.filteredCropCommodityOptions = this.getCommodityOptions() 
  }

  searchCommodity(value){ 

    const cropCommodityName = (( typeof value === 'string') ? value : value?.cropCommodityName)

    let cmdtyOptions = this.getCommodityOptions()

    if (cropCommodityName) {
      const filterValue = cropCommodityName.toLowerCase()

      this.filteredCropCommodityOptions = cmdtyOptions.filter(option => option.cropCommodityName.toLowerCase().includes(filterValue) )

    } else {
      this.filteredCropCommodityOptions = cmdtyOptions
    }
  }

  updateCommodity(){

    let cropCommodityId = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.cropCommodityId
    let isPedigreeInd = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.isPedigreeInd

    this.amendment.cropCommodityId = cropCommodityId || null;
    this.amendment.isPedigreeInd = isPedigreeInd || false;

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true)); 
  }

  isGrainPolicy() {
    if (this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return true
    } else {
      return false
    }
  }

  // variety autocomplete functions
  displayVarietiesFn(vrty: any): string {
    return vrty && vrty.cropVarietyName ? makeTitleCase(vrty.cropVarietyName)  : '';
  }

  getVarietyOptions() {

    let vrtyOpt = [] 
    let fieldId = this.amendmentFormGroup.controls['fieldCtrl'].value.fieldId
    
    if (fieldId) {
      // return only the commodities for that field
      let elem = this.fieldOptions.find(fld => fld.fieldId == fieldId)
      
      if (elem && elem.verifiableVarieties && elem.verifiableVarieties.length > 0) {
        vrtyOpt = elem.verifiableVarieties.slice()
      }

    } else {
      
      //return all fields
      vrtyOpt = this.cropVarietyOptions.slice()
    }

    return vrtyOpt
  }

  varietiesFocus() {
    this.filteredCropVarietyOptions = this.getVarietyOptions() 
  }

  searchVariety(value){ 

    const cropVarietyName = (( typeof value === 'string') ? value : value?.cropVarietyName)

    let vrtyOptions = this.getVarietyOptions()

    if (cropVarietyName) {
      const filterValue = cropVarietyName.toLowerCase()

      this.filteredCropVarietyOptions = vrtyOptions.filter(option => option.cropVarietyyName.toLowerCase().includes(filterValue) )

    } else {
      this.filteredCropVarietyOptions = vrtyOptions
    }
  }

  updateVariety(){

    let cropCommodityId = this.amendmentFormGroup.controls['cropVarietyCtrl'].value.cropCommodityId
    let cropVarietyId = this.amendmentFormGroup.controls['cropVarietyCtrl'].value.cropVarietyId

    // verifiableVarieties do not contain the cropCommodityId, so we have to get cropCommodityId from the cropVarietyOptions list
    if (!cropCommodityId && cropVarietyId) {
      cropCommodityId = this.cropVarietyOptions.find(el => el.cropVarietyId == cropVarietyId).cropCommodityId
    }

    this.amendment.cropCommodityId = cropCommodityId || null;
    this.amendment.cropVarietyId = cropVarietyId || null;

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true)); 
  }


}
