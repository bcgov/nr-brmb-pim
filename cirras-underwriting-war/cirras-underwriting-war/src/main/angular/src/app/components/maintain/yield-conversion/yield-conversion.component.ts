import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { YieldConversionComponentModel } from './yield-conversion.component.model';
import { YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { LoadYieldMeasUnitList } from 'src/app/store/dop/dop.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { YieldMeasUnitConversionList } from 'src/app/conversion/models-maintenance';
import { ClearYieldConversion, LoadYieldConversion, SaveYieldConversion } from 'src/app/store/maintenance/maintenance.actions';
import { FormArray } from '@angular/forms';
import { areNotEqual, makeNumberOnly } from 'src/app/utils';
import { etagFixer } from 'src/app/utils/etagFixer';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { YieldConversionUnitsContainer } from 'src/app/containers/maintenance/yield-conversion-units-container.component';

@Component({
  selector: 'yield-conversion',
  templateUrl: './yield-conversion.component.html',
  styleUrls: ['./yield-conversion.component.scss']
})
export class YieldConversionComponent extends BaseComponent implements OnChanges  {

  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;
  @Input() yieldMeasUnitConversionList: YieldMeasUnitConversionList

  componentId = MAINTENANCE_COMPONENT_ID;

  insurancePlansDefault = getCodeOptions("insurance_plan")
  insurancePlanOptions = []
  yieldMeasUnitOptions = []

  defaultUnitForPlan = "";

  hasDataChanged = false;

  etag = "";

  initModels() {
    this.viewModel = new YieldConversionComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): YieldConversionComponentModel  { //
    return <YieldConversionComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  loadPage() {
    
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.yieldMeasUnitList ) {

      this.defaultUnitForPlan = ""
      this.yieldMeasUnitOptions = []

      if (this.yieldMeasUnitList && this.yieldMeasUnitList.collection && this.yieldMeasUnitList.collection.length  > 0) {

        const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value;

        if ( selectedInsurancePlanId ) {
          
          this.defaultUnitForPlan = this.getDefaultUnitForPlan(selectedInsurancePlanId) 

          // populate the Units dropdown based on yieldMeasUnitList
          this.getUnitOptionsForPlan(selectedInsurancePlanId)       
        }
      }
    }

    // load the form
    if ( changes.yieldMeasUnitConversionList ) {

      // pre-fill the form
      
      let frmConversionList: FormArray = this.viewModel.formGroup.controls.yieldMeasUnitConversionList as FormArray
      frmConversionList.clear()

      if (this.yieldMeasUnitConversionList && this.yieldMeasUnitConversionList.collection && this.yieldMeasUnitConversionList.collection.length  > 0) {

        this.yieldMeasUnitConversionList.collection.forEach (el => {

          frmConversionList.push( this.fb.group( {  
            yieldMeasUnitConversionGuid:    [ el.yieldMeasUnitConversionGuid ],
            cropCommodityId:                [ el.cropCommodityId ],
            srcYieldMeasUnitTypeCode:       [ el.srcYieldMeasUnitTypeCode ],
            targetYieldMeasUnitTypeCode:    [ el.targetYieldMeasUnitTypeCode ], 
            versionNumber:                  [ el.versionNumber ],
            effectiveCropYear:              [ el.effectiveCropYear ],
            expiryCropYear:                 [ el.expiryCropYear ],
            conversionFactor:               [ el.conversionFactor ],
            insurancePlanId:                [ el.insurancePlanId ],
            commodityName:                  [ el.commodityName ]
          } ) )
        })

      }
    }
  }
 
  getDefaultUnitForPlan(insurancePlanId) {

    let el = this.yieldMeasUnitList.collection.find ( x => x.isDefaultYieldUnitInd == true )

    if (el) {
      return el.yieldMeasUnitTypeCode //  el.description
    } else {
      return ""
    }
  }

  getUnitOptionsForPlan(insurancePlanId) {
    var self = this

    // clear the insurance plan otptions 
    this.yieldMeasUnitList.collection.forEach( ymu => {

      if ( ymu.insurancePlanId == insurancePlanId && ! ymu.isDefaultYieldUnitInd ) {

        self.yieldMeasUnitOptions.push({
          yieldMeasUnitTypeCode: ymu.yieldMeasUnitTypeCode,
          description: ymu.yieldMeasUnitTypeCode // ymu.description
        })
      }
    } )
  }

  onPlanChange() {
    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value;

    this.store.dispatch(ClearYieldConversion())

    if (selectedInsurancePlanId) {
      this.store.dispatch( LoadYieldMeasUnitList(this.componentId, selectedInsurancePlanId) )
    }
  }

  onUnitChange() {
    this.reloadUnitConversionList()
  }

  reloadUnitConversionList() {
    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value;
    const selectedUnits = this.viewModel.formGroup.controls.selectedUnits.value;

    if (selectedInsurancePlanId && selectedUnits) {
      this.store.dispatch( LoadYieldConversion(this.componentId, selectedInsurancePlanId, this.defaultUnitForPlan, selectedUnits ))
    }
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.yieldMeasUnitConversionList) return false;

    const frmUnitConversion: FormArray = this.viewModel.formGroup.controls.yieldMeasUnitConversionList as FormArray

    for (let i = 0; i < frmUnitConversion.controls.length; i++) {

      let frmUC = frmUnitConversion.controls[i] as FormArray

      let originalUC = this.yieldMeasUnitConversionList.collection.find( el => el.cropCommodityId == frmUC.value.cropCommodityId)

      if (originalUC) {

        if ( areNotEqual (originalUC.conversionFactor, frmUC.value.conversionFactor )	) {
          return true
        }

      }
    }
    
    return false
  }

  onCancel() {
    // reload the page
    this.reloadUnitConversionList()
    this.hasDataChanged = false   

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }


  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }
  
  onSave() {

    if ( !this.isFormValid() ){
      return
    }
    
    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value;
    const selectedUnits = this.viewModel.formGroup.controls.selectedUnits.value;

    // prepare the updated seeding deadlines 
    const newUnitConversionList: YieldMeasUnitConversionList = this.getUpdatedUnitConversionList()

    // save
    this.store.dispatch(SaveYieldConversion(MAINTENANCE_COMPONENT_ID, selectedInsurancePlanId, this.defaultUnitForPlan, selectedUnits, newUnitConversionList))
    
    this.hasDataChanged = false   

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }

  isFormValid() {
    
    const frmUnitConversion: FormArray = this.viewModel.formGroup.controls.yieldMeasUnitConversionList as FormArray

    for (let i = 0; i < frmUnitConversion.controls.length; i++) {

      let frmUC = frmUnitConversion.controls[i] as FormArray

      // New commodities don’t need to have a conversion factor
      // Existing conversions cannot be deleted, conversion factor is mandatory
      if ( frmUC.value.yieldMeasUnitConversionGuid && (!frmUC.value.conversionFactor || isNaN(frmUC.value.conversionFactor))) {
        alert ("Conversion factor for commodity " + frmUC.value.commodityName + " cannot be empty.")
        return false
      }
      
      // Conversion Factor > 0
      if ( frmUC.value.yieldMeasUnitConversionGuid && frmUC.value.conversionFactor && !isNaN(frmUC.value.conversionFactor) && 
          parseInt(frmUC.value.conversionFactor) <= 0 ) {
        alert ("Conversion factor for commodity " + frmUC.value.commodityName + " should be positive.")
        return false
      }
    }

    return true
  }

  getUpdatedUnitConversionList() {

    //make a deep copy
    let updatedUnitConversionList : YieldMeasUnitConversionList = JSON.parse(JSON.stringify(this.yieldMeasUnitConversionList));

    // required only for rollover -> set the original etag
    if (this.etag !== "") {
      updatedUnitConversionList.etag = etagFixer(this.etag);
      this.etag = ""
    }

    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value;
    const selectedUnits = this.viewModel.formGroup.controls.selectedUnits.value;

    const frmUnitConversionList: FormArray = this.viewModel.formGroup.controls.yieldMeasUnitConversionList as FormArray

    var self = this
    frmUnitConversionList.controls.forEach( function(frmUC : FormArray) {

      // find the corresponding field in updatedDopYieldContract object
      let origUnitConversion = updatedUnitConversionList.collection.find( el => el.cropCommodityId == frmUC.value.cropCommodityId)
      
      if (origUnitConversion) { 
        // update
        origUnitConversion.conversionFactor = frmUC.value.conversionFactor 

        // add values that shouldn't be null for the new commodities
        if ( !origUnitConversion.effectiveCropYear && frmUC.value.conversionFactor) {
          origUnitConversion.effectiveCropYear = (new Date()).getFullYear() 
        }
        
        if ( !origUnitConversion.expiryCropYear && frmUC.value.conversionFactor) {
          origUnitConversion.expiryCropYear = 9999
        }
        
        if ( !origUnitConversion.srcYieldMeasUnitTypeCode && frmUC.value.conversionFactor) {
          origUnitConversion.srcYieldMeasUnitTypeCode = self.defaultUnitForPlan
        }
        
        if ( !origUnitConversion.targetYieldMeasUnitTypeCode && frmUC.value.conversionFactor) {
          origUnitConversion.targetYieldMeasUnitTypeCode = selectedUnits
        }
       
        if ( !origUnitConversion.versionNumber && frmUC.value.conversionFactor) {
          origUnitConversion.versionNumber = 1
        }
      } 
    })

    return updatedUnitConversionList
  }

  getSelectedPlanName() {
    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value
    
    let el = this.insurancePlansDefault.find( x => x.code == selectedInsurancePlanId )

    if (el) {
      return el.description
    } else {
      return ""
    }
  }
  
  onViewYieldUnits() {

    const selectedInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value

    if (!selectedInsurancePlanId ) {
      alert("Please select Insurance Plan.")
      return
    }
    const dialogRef = this.dialog.open(YieldConversionUnitsContainer, {
      width: '650px',
    });

  }
}
