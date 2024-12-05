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

@Component({
  selector: 'verified-yield-amendment',
  templateUrl: './verified-yield-amendment.component.html',
  styleUrls: ['./verified-yield-amendment.component.scss', '../../../../styles/_inventory.scss' ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class VerifiedYieldAmendmentComponent implements OnChanges {

  @Input() amendment: VerifiedYieldAmendment;
  @Input() amendmentsFormArray: UntypedFormArray;

  @Input() fieldOptions;
  @Input() cropCommodityOptions;

  amendmentFormGroup: UntypedFormGroup;

  amendmentOptions = getCodeOptions("verified_yield_amendment_code"); // get the amendment code
  
  filteredFieldOptions = [];
  filteredCropCommodityOptions = [];

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
        cropCommodityName: ( this.amendment.cropCommodityName) ? this.amendment.cropCommodityName :"",
        isPedigreeInd: ( ( this.amendment.isPedigreeInd) ? this.amendment.isPedigreeInd : false )
      } ],
      cropCommodityId: [this.amendment.cropCommodityId],
      isPedigreeInd: [this.amendment.isPedigreeInd],
      fieldCtrl:          [ { 
        fieldId: ( this.amendment.fieldId) ? this.amendment.fieldId : "", 
        fieldLabel: ( this.amendment.fieldLabel) ? this.amendment.fieldLabel :"",
        verifiableCommodities: []
      } ],
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

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

  updateYieldPerAcre() {
    let yieldPerAcre = roundUpDecimalYield(this.amendmentFormGroup.value.yieldPerAcre, 3)
    this.amendment.yieldPerAcre = parseFloat(yieldPerAcre.toString()) || null
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
    // TODO
    // deletes a row
    // if the guid is null delete the row from the array and the formArray
    // if there is a guid then hide it in the page and set deletedByUserInd to true

  }


  displayFieldsFn(fld: any): string {
    return fld && fld.fieldLabel ? fld.fieldLabel  : '';
  }

  getFieldOptions() {
    
    let fldOpt = [] 
    let cropCommodityId = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.cropCommodityId
    let isPedigreeInd = this.amendmentFormGroup.controls['cropCommodityCtrl'].value.isPedigreeInd

    if (cropCommodityId) {
      // return only the fields that have the selected commodity

      this.fieldOptions.forEach( fld => {
        let elem = fld.verifiableCommodities.filter(x => ( x.cropCommodityId == cropCommodityId && x.isPedigreeInd == isPedigreeInd))
        
        if (elem && elem.length > 0) {
          fldOpt.push({
            fieldId: fld.fieldId,
            fieldLabel: fld.fieldLabel,
            verifiableCommodities: fld.verifiableCommodities
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
  
    // prepare the list of fields based on the selected commodity id
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

  // commodities functions
  displayCommoditiesFn(cmdty: any): string {
    return cmdty && cmdty.cropCommodityName ? makeTitleCase(cmdty.cropCommodityName)  : '';
  }

  getCommodityOptions() {
    
    let cmdtOpt = [] 
    let fieldId = this.amendmentFormGroup.controls['fieldCtrl'].value.fieldId
    
    if (fieldId) {
      // return only the commodities for that field
      let verifiableCommodities = this.amendmentFormGroup.controls['fieldCtrl'].value.verifiableCommodities

      if (verifiableCommodities) {
       cmdtOpt = verifiableCommodities.slice()
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

  // TODO
  // onDeleteAmendment
  // rationale - resisable 
  // drop downs onChange should change the store value
  // validation -> probably on the main page
  // remove checkmarks from all autocompletes
}
