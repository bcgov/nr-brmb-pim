import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { CropCommodityList } from 'src/app/conversion/models';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { VERIFIED_YIELD_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';
import { makeNumberOnly, makeTitleCase } from 'src/app/utils';
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

  @Input() fieldOptions;
  @Input() cropCommodityOptions;

  amendmentFormGroup: UntypedFormGroup;

  amendmentOptions = getCodeOptions("verified_yield_amendment_code"); // get the amendment code
  
  filteredFieldOptions = [];


  constructor(private fb: UntypedFormBuilder,
    private store: Store<RootState>,
    public securityUtilService: SecurityUtilService, 
    private decimalPipe: DecimalPipe ){
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

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

  updateYieldPerAcre() {
    let yieldPerAcre = this.amendmentFormGroup.value.yieldPerAcre;
    yieldPerAcre = this.decimalPipe.transform(yieldPerAcre, '1.0-3')?.replace(',', '');
    this.amendment.yieldPerAcre = parseFloat(yieldPerAcre) || null;

    this.amendmentFormGroup.controls['yieldPerAcre'].setValue(yieldPerAcre)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

  updateAcres() {
    let acres = this.amendmentFormGroup.value.acres;
    acres = this.decimalPipe.transform(acres, '1.0-1')?.replace(',', '');
    this.amendment.acres = parseFloat(acres) || null;

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
    return fld && fld.fieldLabel ? makeTitleCase( fld.fieldLabel)  : '';
  }

  fieldFocus() {
    debugger
    
    // // prepare the list of varieties based on the selected crop id
    
    // const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    // const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    // const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]
	
    // let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value

    // if (selectedCropCommodityId) {

    //   this.filteredSeededVarietyOptions = 
    //         this.seededVarietyOptions.filter(option => 
    //                                         ( option.cropCommodityId == selectedCropCommodityId) )

    // } else {
      
      //return all fields
      // this.filteredSeededVarietyOptions = this.seededVarietyOptions.slice()
      this.filteredFieldOptions = this.fieldOptions.slice()
    // }
  }

}
