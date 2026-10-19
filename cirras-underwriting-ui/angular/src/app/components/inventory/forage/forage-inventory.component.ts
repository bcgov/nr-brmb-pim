import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from 'src/app/components/common/base/base.component';
import { AnnualField, CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { ForageInventoryComponentModel } from './forage-inventory.component.model';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { ParamMap } from '@angular/router';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CROP_COMMODITY_UNSPECIFIED, INSURANCE_PLAN, PLANT_DURATION, PLANT_INSURABILITY_TYPE_CODE } from 'src/app/utils/constants';
import { CropVarietyCommodityType, InventorySeededForage, InventoryUnseeded, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { addUwCommentsObject, areDatesNotEqual, areNotEqual, makeNumberOnly, makeTitleCase, replaceNonAlphanumericCharacters } from 'src/app/utils';
import { AddNewFormField, CropVarietyOptionsType, addAnnualFieldObject, addPlantingObject, addSeededForagesObject, deleteFormField, deleteNewFormField, dragField, fieldHasInventory, getInventorySeededForagesObjForSave, isLinkedFieldCommon, isLinkedPlantingCommon, isThereAnyCommentForField, linkedFieldTooltipCommon, linkedPlantingTooltipCommon, navigateUpDownTextbox, openAddEditLandPopup, roundUpDecimal, updateComments } from '../inventory-common';
import { AddNewInventoryContract, DeleteInventoryContract, GetInventoryReport, LoadInventoryContract, RolloverInventoryContract, UpdateInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { RemoveFieldPopupData } from '../remove-field/remove-field.component';
import {ViewEncapsulation } from '@angular/core';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { asapScheduler } from 'rxjs';
import { AddLandPopupData } from '../add-field/add-field.component';

@Component({
    selector: 'forage-inventory',
    templateUrl: './forage-inventory.component.html',
    styleUrls: ['./forage-inventory.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    standalone: false
})

export class ForageInventoryComponent extends BaseComponent implements OnChanges {

  @Input() inventoryContract: InventoryContract;
  @Input() cropCommodityList: CropCommodityList;
  @Input() growerContract: UwContract;

  cropVarietyOptions = [];
  filteredVarietyOptions: CropVarietyOptionsType[];  

  cropYear = 0;
  insurancePlanId = 0;
  policyId = '';

  hasDataChanged = false;
  hasYieldData = false;

  plantInsurabilityOptions = []

  isHiddenFieldInTotals = false;

  initModels() {
    this.viewModel = new ForageInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
  }

  loadPage() {

      this.componentId = INVENTORY_COMPONENT_ID;
    
      this.route.paramMap.subscribe(
        (params: ParamMap) => {
            this.policyId = params.get("policyId") ? params.get("policyId") : "";
        }
      );
  }

  getViewModel(): ForageInventoryComponentModel  { //
      return <ForageInventoryComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);

    if (changes.inventoryContract) {
        this.inventoryContract = changes.inventoryContract.currentValue;
        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }
    this.ngOnChanges2(changes);

    this.viewModel.formGroup.valueChanges.subscribe(val => {this.isMyFormDirty()})
  }

  ngOnInit() {
    super.ngOnInit()
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.growerContract && this.growerContract ) {

      this.hasYieldData = false

      // check for yield data
      for (let i = 0; i< this.growerContract.links.length; i++ ) {

        if ( this.growerContract.links[i].href.toLocaleLowerCase().indexOf("dopyieldcontracts") > -1  ) {
          this.hasYieldData = true
          break
        } 
      }
    }

    if ( changes.inventoryContract && this.inventoryContract ) {
      this.cropYear = this.inventoryContract.cropYear
    }

    if ( changes.inventoryContract && this.inventoryContract && this.inventoryContract.fields ) {

        let flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
        flds.clear()
        this.inventoryContract.fields.forEach( f => this.addField( f ) )

        // setup the plant insurability options for each field based on the variety
        this.setInitialPlantInsurabilityOptions()
        this.checkForHiddenFieldInTotals()
    }

    // populate commodity and variety lists
    if (changes.cropCommodityList && this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length ) {
      
      this.populateCropVarieryOptions()
      this.insurancePlanId = this.cropCommodityList.collection[0].insurancePlanId
    }
  }

  addField( field: AnnualField ) {

    let fldPlantings = new UntypedFormArray ([]) 

    let pltgInventorySeededForages = new UntypedFormArray ([]) 

    var self = this

    if (field.plantings.length > 0 ) {

      field.plantings.forEach( function(pltg) {

        // add seededForage if any
        pltgInventorySeededForages = new UntypedFormArray ([])

        let acresToBeSeeded = (pltg.inventoryUnseeded && pltg.inventoryUnseeded.acresToBeSeeded ) ? pltg.inventoryUnseeded.acresToBeSeeded : null
        let isUnseededInsurableInd = ( pltg.inventoryUnseeded ) ? pltg.inventoryUnseeded.isUnseededInsurableInd : false


        if (pltg.inventorySeededForages && pltg.inventorySeededForages.length > 0 ) {

          pltg.inventorySeededForages.forEach( invSeededForage =>
            pltgInventorySeededForages.push( self.fb.group( 
              addSeededForagesObject(pltg.inventoryFieldGuid, pltg.isHiddenOnPrintoutInd, acresToBeSeeded, isUnseededInsurableInd, invSeededForage)
            ) )
          )
        } else {

          pltgInventorySeededForages.push( self.fb.group( 
            addSeededForagesObject(pltg.inventoryFieldGuid, pltg.isHiddenOnPrintoutInd, acresToBeSeeded, isUnseededInsurableInd, <InventorySeededForage>[])
          ) )

        }

        // add plantings to the form
        fldPlantings.push( self.fb.group( 
          addPlantingObject(pltg.cropYear, pltg.fieldId, pltg.insurancePlanId, pltg.inventoryFieldGuid, 
            pltg.lastYearCropCommodityId, pltg.lastYearCropCommodityName, pltg.lastYearCropVarietyId, pltg.lastYearCropVarietyName,
            pltg.plantingNumber, pltg.isHiddenOnPrintoutInd, null,
            pltg.inventoryUnseeded, new UntypedFormArray ([]), pltgInventorySeededForages ) ) )
        }

      )

    } else { //empty plantings array

      fldPlantings.push( this.fb.group( 
        addPlantingObject( this.cropYear, field.fieldId , this.insurancePlanId, '', '', '', '', '', 1, false, null, <InventoryUnseeded>{}, new UntypedFormArray ([]), new UntypedFormArray ([]))
       ))

    }

    // set underwriting comments
    let fldComments = [] // no need to set them up as a form group
    
    if (field.uwComments.length > 0 ) {

      field.uwComments.forEach ( (comment: UnderwritingComment) => fldComments.push ( 
        addUwCommentsObject( comment )  
      ))
    } 
    
    let fld: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    fld.push( this.fb.group(addAnnualFieldObject(field, fldPlantings, fldComments) ) )
  }
  
  // disablePlantInsurabilityForAnnuals() {

  //   const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray

  //   for (let i = 0; i < formFields.controls.length; i++){
  //         let frmField = formFields.controls[i] as FormArray
        
  //       for (let k = 0; k < frmField.value.plantings.controls.length; k++){
  //         let frmPlanting = frmField.value.plantings.controls[k] as FormArray

  //         for (let n = 0; n < frmPlanting.value.inventorySeededForages.controls.length; n++) {
                  
  //           let frmInvSeededForages = frmPlanting.value.inventorySeededForages.controls[n] as FormArray
            
  //           let selectedCropVarietyId = frmInvSeededForages.controls['cropVarietyCtrl'].value.cropVarietyId

  //           if( selectedCropVarietyId && this.isCropAnnual(selectedCropVarietyId)) {
               
  //             // disable plantInsurabilityTypeCode
  //             frmInvSeededForages.controls['plantInsurabilityTypeCode'].disable()

  //           }

  //         }
          
  //       }
  //   }
  // }

  populateCropVarieryOptions() {
      // clear the crop options
      this.cropVarietyOptions = [] 

      // add empty variety
      this.cropVarietyOptions.push ({
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]
      })

      var self = this
      this.cropCommodityList.collection.forEach( ccm => 
                                                  ccm.cropVariety.forEach(cv => self.cropVarietyOptions.push ({
                                                          cropCommodityId: cv.cropCommodityId,
                                                          cropVarietyId: cv.cropVarietyId,
                                                          varietyName: cv.varietyName ,
                                                          cropVarietyCommodityTypes: cv.cropVarietyCommodityTypes, 
                                                        })) )
  }

  
  varietyFocus() {
    // prepare a list of all varieties
    this.filteredVarietyOptions = this.cropVarietyOptions.slice()
  }

  // crop variety search
  searchVariety(value) {
  
    const varietyName = (( typeof value === 'string') ? value : value?.varietyName)

    if (varietyName) {

      const filterValue = varietyName.toLowerCase()
      this.filteredVarietyOptions = this.cropVarietyOptions.filter(option => option.varietyName.toLowerCase().includes(filterValue) )
  
    } else {

      this.filteredVarietyOptions = this.cropVarietyOptions.slice()

    }
  }

  displayVarietyFn(vrty: CropVarietyOptionsType): string {
    return vrty && vrty.varietyName ? makeTitleCase( vrty.varietyName)  : '';
  }

  validateVariety(option, fieldIndex, plantingIndex, invSeededIndex){

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    if (!option.isOpen){

      if (!invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId){

        alert("Invalid variety. Please check your spelling or select Unspecified")
        // if I try to clear the variety then the menu stays opened
        invSeeded.controls['cropVarietyCtrl'].setValue({      
          cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
          cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
          varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
          cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
        })

        // clear plant insurability
        invSeeded.controls['plantInsurabilityTypeCode'].setValue(null) 

      } else {

        let selectedCropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId
        let variety = this.getVarietyById(selectedCropVarietyId)

        // check quantity Insurable checkbox
        invSeeded.controls['isQuantityInsurableInd'].setValue( (variety) ? variety.isQuantityInsurableInd : true) // default
        // check AWP Eligible checkbox
        invSeeded.controls['isAwpEligibleInd'].setValue( (variety) ? variety.isAwpEligibleInd : true) // default
        // check Unseeded Insurable checkbox
        invSeeded.controls['isUnseededInsurableInd'].setValue( (variety) ? variety.isUnseededInsurableInd : true) // default

        if (this.isAnnualPlanting(fieldIndex, plantingIndex, invSeededIndex)) {

          // silage corn was selected -> clear seedingYear and plantInsurability
          invSeeded.controls['seedingYear'].setValue('')
          invSeeded.controls['plantInsurabilityTypeCode'].setValue(null) 

        } else {
          invSeeded.controls['seedingDate'].setValue(null)
          invSeeded.controls['acresToBeSeeded'].setValue(null)
          // check plant insurability in case something was selected there 
          this.checkPlantInsurability(fieldIndex, plantingIndex, invSeededIndex)
        }
      }
    }

    this.checkForHiddenFieldInTotals()
    this.isMyFormDirty()
  }

  getVarietyById(cropVarietyId){
    
    if (cropVarietyId && this.cropCommodityList) {

      let vrty 

      for (let i = 0; i < this.cropCommodityList.collection.length; i++ ) {
        let cmdty = this.cropCommodityList.collection[i]

        vrty = cmdty.cropVariety.find(v => v.cropVarietyId == cropVarietyId )

        if (vrty) {
          return vrty
        }
      }
    }
  }

  private getVariety(fieldIndex: any, plantingIndex: any, invSeededIndex: any) {
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex];

    let selectedCropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId;
    let variety = this.getVarietyById(selectedCropVarietyId);

    return variety;
  }

  private getVarietySddYear(fieldIndex: any, plantingIndex: any, invSeededIndex: any) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex];

    let year = -1;
  
    if (invSeeded.controls['seedingYear'] && invSeeded.controls['seedingYear'].value) {
      year = invSeeded.controls['seedingYear'].value
    }
    
    return year
  }

  private getIsIrrigated(fieldIndex: any, plantingIndex: any, invSeededIndex: any) {
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex];

    let isIrrigatedInd = invSeeded.controls['isIrrigatedInd'].value;
    return isIrrigatedInd;
  }

  setPlantInsurability(fieldIndex, plantingIndex, invSeededIndex) {

    let variety = this.getVariety(fieldIndex, plantingIndex, invSeededIndex);
    let yearSdd = this.getVarietySddYear(fieldIndex, plantingIndex, invSeededIndex); // this is either the year(seedingDate) or seedingYear
    let isIrrigated = this.getIsIrrigated(fieldIndex, plantingIndex, invSeededIndex);

    let plantInsurabilityOptionsLocal = this.plantInsurabilityOptions.find(x => x.fieldIndex == fieldIndex && x.plantingIndex == plantingIndex && x.invSeededIndex == invSeededIndex)

    if (plantInsurabilityOptionsLocal) {

      plantInsurabilityOptionsLocal.options = this.setPlantingInsurabilityOptionsByVarietyAndSddYearOrDate(variety, yearSdd, isIrrigated)

    } else {
      // add an entry
      this.plantInsurabilityOptions.push({
          fieldIndex: fieldIndex,
          plantingIndex: plantingIndex,
          invSeededIndex: invSeededIndex,
          options: this.setPlantingInsurabilityOptionsByVarietyAndSddYearOrDate(variety, yearSdd, isIrrigated)
        })
    }  
  }

  setPlantingInsurabilityOptionsByVarietyAndSddYearOrDate(variety, yearSdd, isIrrigated){

    var self = this
    let options = []

    if ( variety && variety.isPlantInsurableInd == true) {

      variety.cropVarietyPlantInsurabilities.forEach( p => {
        // add the year logic
        if (self.isPlantInsurabilityAllowed(yearSdd, p.plantInsurabilityTypeCode, isIrrigated)) {
          options.push({
            code:         p.plantInsurabilityTypeCode,
            description:  p.description
          })
        }
        
      })
    }
    return options
  }

  isPlantInsurabilityAllowed(yearSdd, pitc, isIrrigated){
    //Insurability is only enabled if there is a seeded year/date
    if (yearSdd < 0) {
      return false 
    }

    // EST 1 coverage can only be selected with a seeded year of the CURRENT Plan year.
    if ( yearSdd == this.inventoryContract.cropYear && pitc == PLANT_INSURABILITY_TYPE_CODE.Establishment1) {
      return true
    }

    // EST 2 coverage is only applicable to Seeded dates of the Previous plan year
    if ( yearSdd == this.inventoryContract.cropYear - 1
        && pitc == PLANT_INSURABILITY_TYPE_CODE.Establishment2) {
      return true
    }

    // Winter 1 coverage is only applicable to 
    //    - Seeded dates of the Previous plan year
    //    - and if the field is irrigated
    if ( yearSdd == this.inventoryContract.cropYear - 1 && isIrrigated &&  pitc == PLANT_INSURABILITY_TYPE_CODE.WinterSurvival1) {
    return true
  }

    // Winter 2 coverage is applicable to 
    //    - Seeding dates 2 years prior to the Current Plan year
    //    - and if the field is irrigated
    if ( yearSdd == this.inventoryContract.cropYear - 2 && isIrrigated && pitc == PLANT_INSURABILITY_TYPE_CODE.WinterSurvival2) {
      return true
    }

    // Winter 3 coverage is applicable to 
    //    - Seeding dates 3 years prior to the Current Plan year
    //    - and if the field is irrigated
    if (yearSdd == this.inventoryContract.cropYear - 3 && isIrrigated && pitc == PLANT_INSURABILITY_TYPE_CODE.WinterSurvival3) {
      return true
    }

    return false // 
  }

  getPlantInsurabilityOptions(fieldIndex, plantingIndex, invSeededIndex) {
    let options = []
    this.plantInsurabilityOptions.forEach( x  => {
      if ( x.fieldIndex == fieldIndex && x.plantingIndex == plantingIndex && x.invSeededIndex == invSeededIndex) {
        
        options =  x.options 
        return
      }
    }) 
    return options
  }

  setInitialPlantInsurabilityOptions(){
    // for all fields
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    
    for (let fieldIndex = 0; fieldIndex < formFields.controls.length; fieldIndex++) { 

      let formField = formFields.controls[fieldIndex] as UntypedFormArray

      for (let plantingIndex = 0; plantingIndex < formField.value.plantings.length; plantingIndex++) {

        for (let invSeededIndex = 0; invSeededIndex < formField.value.plantings.value[plantingIndex].inventorySeededForages.length; invSeededIndex++){

          this.setPlantInsurability(fieldIndex, plantingIndex, invSeededIndex) 

        }
      }
    }
  }

  checkPlantInsurability(fieldIndex, plantingIndex, invSeededIndex) {
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex];

    let cropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId
    let seedingYear = invSeeded.controls['seedingYear'].value
    let isIrrigated = this.getIsIrrigated(fieldIndex, plantingIndex, invSeededIndex);
    let plantInsurabilityTypeCode = invSeeded.controls['plantInsurabilityTypeCode'].value

    let isPitcAllowed = false 
    
    if ( cropVarietyId && seedingYear && plantInsurabilityTypeCode) {

      let variety = this.getVarietyById(cropVarietyId)
      if ( variety && variety.isPlantInsurableInd == true) {

        let elem = variety.cropVarietyPlantInsurabilities.find( x => x.plantInsurabilityTypeCode == plantInsurabilityTypeCode)
 
        if (elem && this.isPlantInsurabilityAllowed(seedingYear, plantInsurabilityTypeCode, isIrrigated)) {
          isPitcAllowed = true
        }
      }
    } 
    
    if ( isPitcAllowed ==  false) {
      invSeeded.controls['plantInsurabilityTypeCode'].setValue(null) 
    }
    
  }


  setStyles(){

    let styles = {
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
      'width': '1799px' // '2075px'// '2025px' // '1805px'   
    }

    return styles;
  }

  setPlantingStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 6fr',
      'align-items': 'stretch',
      'width': '1189px' // '1420px' // '1370px'  // '1170px'    
    }

    return styles;
  }

  setSeededForageStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr',
      'align-items': 'stretch',
      'width': '1145px' // '1360px' // '1310px' // '1110px' 
    }


    return styles;
  }

  isAddPlantingVisible(planting, invSeededIndex) {

    // if the planting row is empty do not show Add New Planting button
    if ( isNaN( parseFloat(planting.value.inventorySeededForages.value[invSeededIndex].fieldAcres)) 
      || !planting.value.inventorySeededForages.value[invSeededIndex].cropVarietyCtrl.cropVarietyId) {

      return false

    }

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const fld: UntypedFormArray  =  flds.controls.find( f => f.value.fieldId == planting.value.fieldId ) as UntypedFormArray 

    //in order to show Add Planting button, find the max planting number for that field that hasn't been deleted 
    let numPlantings = 0
    let maxNotDeletedPlantingNumber = 0

    for (let i = 0; i < fld.value.plantings.length; i++) {

      for ( let k = 0; k < fld.value.plantings.value[i].inventorySeededForages.length; k++) {

        if (fld.value.plantings.value[i].inventorySeededForages.value[k].deletedByUserInd != true ) { 

          maxNotDeletedPlantingNumber = fld.value.plantings.value[i].plantingNumber
          numPlantings ++
          break // so we don't double count the planting numbers 
        }
      }
    }

    // if there are no plantings then don't show the button
    if (numPlantings < 1) {
      return false
    }

    // if there are more than 1 planting then show the button at the last planting
    if (numPlantings > 1) {
      if (maxNotDeletedPlantingNumber == planting.value.plantingNumber ) {
        return true
      }else{
        return false
      }
    }

    // most common case: one planting with acres to be seeded 
    return true;

  }

  onDeletePlanting(planting, invSeededIndex) {
    // find the field in the form that contains the planting to be deleted
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const field: UntypedFormArray  =  flds.controls.find( f => f.value.fieldId == planting.value.fieldId ) as UntypedFormArray

    // count the number of plantings for that field that have not been deleted
    let numPlantings = 0       

    for (let i = 0; i < field.value.plantings.length; i++) {

      for ( let k = 0; k < field.value.plantings.value[i].inventorySeededForages.length; k++) {

        if (field.value.plantings.value[i].inventorySeededForages.value[k].deletedByUserInd != true ) { 

          numPlantings ++
          break // so we don't double count the planting numbers 
        }
      }
    }

    // now find the planting to be deleted from the form and set its values to null 
    for( var i = 0; i < field.value.plantings.length; i++){

      let fieldPlanting = field.value.plantings.value[i]

      // get the count of undeleted inventory seeded records for that planting
      let numInvSeededRecords = 0
      
      for ( let k = 0; k < fieldPlanting.inventorySeededForages.length; k++) {

        if (fieldPlanting.inventorySeededForages.value[k].deletedByUserInd != true) {

          numInvSeededRecords ++ 
        }

      }

      // now do the actual delete
      fieldPlanting.acresToBeSeeded = "" // delete the unseeded planting object // these are not used in the forage
      fieldPlanting.cropCommodityId = CROP_COMMODITY_UNSPECIFIED.ID

      for ( let k = 0; k < fieldPlanting.inventorySeededForages.length; k++) {

        if (fieldPlanting.inventorySeededForages.value[k].inventorySeededForageGuid == 
          planting.value.inventorySeededForages.value[invSeededIndex].inventorySeededForageGuid       
          && fieldPlanting.inventorySeededForages.value[k].deletedByUserInd != true) {

            // if this is NOT the only inveSeeded record for that planting then delete this inv seeded record only 
            if ( numInvSeededRecords > 1 ) {
              field.value.plantings['controls'][i].controls['inventorySeededForages'].value.controls[k].controls.deletedByUserInd.setValue(true)
            }

            let invSeededForageControls = field.value.plantings['controls'][i].controls['inventorySeededForages'].value.controls[k].controls

            // if this is the ONLY inveSeeded record for that planting  
            if (numInvSeededRecords == 1) {

              // if this is the last planting then reset values
              if ( numPlantings == 1 ) {

                this.resetInvSeededForageForDelete(invSeededForageControls)

                field.value.plantings['controls'][i].controls['inventorySeededForages'].value.controls[k].controls.deletedByUserInd.setValue(false)
                field.value.plantings['controls'][i].controls['inventorySeededForages'].value.controls[k].controls.linkPlantingType.setValue("REMOVE_LINK")

              } else {
                
                // delete the whole planting
                this.resetInvSeededForageForDelete(invSeededForageControls)
                field.value.plantings['controls'][i].controls['inventorySeededForages'].value.controls[k].controls.deletedByUserInd.setValue(true)
                field.value.plantings['controls'][i].controls.deletedByUserInd.setValue(true)
              }
            
            }
        }
      }
    }

    this.isMyFormDirty()
  }

  resetInvSeededForageForDelete(invSeededForageControls) {

    invSeededForageControls.cropCommodityId.setValue(CROP_COMMODITY_UNSPECIFIED.ID)
    invSeededForageControls.cropVarietyCtrl.setValue({      
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
      })
      invSeededForageControls.commodityTypeCode.setValue(null)

      invSeededForageControls.fieldAcres.setValue(null)
      invSeededForageControls.seedingYear.setValue(null)
      invSeededForageControls.seedingDate.setValue(null)
      invSeededForageControls.isIrrigatedInd.setValue(false)
      invSeededForageControls.plantInsurabilityTypeCode.setValue(null)
      invSeededForageControls.isQuantityInsurableInd.setValue(false)
      invSeededForageControls.isAwpEligibleInd.setValue(false)
  }


  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  roundUpAcres(fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    let acres = invSeeded.controls['fieldAcres'].value

    invSeeded.controls['fieldAcres'].setValue(roundUpDecimal(acres,1))

    this.isMyFormDirty()
  }

  roundUpUnseededAcres(fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    let acres = invSeeded.controls['acresToBeSeeded'].value

    invSeeded.controls['acresToBeSeeded'].setValue(roundUpDecimal(acres,1))

    this.isMyFormDirty()
  }

  navigateUpDown(event, jumps) {
    navigateUpDownTextbox(event, jumps)
  }

  isChecked() {
    this.isMyFormDirty();  
  }

  changeIsHiddenOnPrintoutInd(event, fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    pltg.controls['isHiddenOnPrintoutInd'].setValue(event.checked) // isHiddenOnPrintoutInd is the same for seeded and unseeded. Keep the two flags in sync 

    this.isMyFormDirty()  
  }


  onAddPlanting(planting, invSeededIndex) {

    if (this.securityUtilService.canEditInventory() && this.isAddPlantingVisible(planting, invSeededIndex)) {

      var self = this

      const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

      flds.controls.forEach( function(field : UntypedFormControl) {

        if (field.value.fieldId == planting.value.fieldId) {

          let pltgInventorySeededForages = new UntypedFormArray ([])

          pltgInventorySeededForages.push( self.fb.group( 
            addSeededForagesObject(planting.value.inventoryFieldGuid, false, null, false, <InventorySeededForage>[])
          ) )

          field.value.plantings.push( self.fb.group( 

            addPlantingObject(planting.value.cropYear, planting.value.fieldId, planting.value.insurancePlanId, null , 
              planting.value.lastYearCropCommodityId, planting.value.lastYearCropCommodityName, planting.value.lastYearCropVarietyId, planting.value.lastYearCropVarietyName,
              planting.value.plantingNumber + 1, false, null,
              <InventoryUnseeded>{}, new UntypedFormArray ([]) , pltgInventorySeededForages)

          ) )          
        }
      })
    }
  }

  onInventoryCommentsDone(fieldId: number, uwComments: UnderwritingComment[]) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    updateComments(fieldId, uwComments, flds)

    this.cdr.detectChanges()

  }

  onCancel() {
    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      if (this.inventoryContract && this.inventoryContract.inventoryContractGuid ) {
        // load the existing one from the database
        this.store.dispatch(LoadInventoryContract(this.componentId, this.inventoryContract.inventoryContractGuid ))
      } else {
        // prepare the new inventory contract
        this.store.dispatch(RolloverInventoryContract(this.componentId, this.policyId))
      }
      this.hasDataChanged = false
      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }

  // LAND Management
  onDeleteField(field) {

    if (field.value.isNewFieldUI == true ) {

      this.deleteNewField(field)

    } else {

      const dataToSend : RemoveFieldPopupData = {
        fieldId: field.value.fieldId,
        fieldLabel: field.value.fieldLabel,
        policyId: this.policyId,
        hasInventory: fieldHasInventory(field),
        hasComments: isThereAnyCommentForField(field),
        landData: {
          landUpdateType: ""
        }  
      }

      const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

      deleteFormField(field, flds, this.dialog, dataToSend, this.cdr)
    }
  }

  deleteNewField(field) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    deleteNewFormField(field.value.fieldId, field.value.isNewFieldUI, flds)
    
    this.isMyFormDirty()
  }

  onEditLocation(field) {

    if (field.value.isNewFieldUI) {

      this.deleteNewField(field)
      this.onAddNewField()

    } else {

      const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

      const dataToSend : AddLandPopupData = {
        fieldId: field.value.fieldId,
        fieldLabel: field.value.fieldLabel,
        fieldLocation: "",
        cropYear: this.cropYear,
        policyId: this.policyId,
        insurancePlanId: this.insurancePlanId,
        annualFieldDetailId: field.value.annualFieldDetailId,
        otherLegalDescription: field.value.otherLegalDescription,
        primaryPropertyIdentifier: null,
        landData: {
          fieldId: null,
          legalLandId: null,
          fieldLabel: null,
          fieldLocation: null,
          primaryPropertyIdentifier: null,
          otherLegalDescription: null,
          landUpdateType: null,
          transferFromGrowerContractYearId : null,
          plantings: [],
          uwComments: []
        }
      }

      openAddEditLandPopup(this.fb, flds, this.dialog, dataToSend, field.value.maxDisplayOrder, false, this.cdr)

    }

  }

  onAddNewField() {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    AddNewFormField(this.fb, flds, this.dialog, this.cropYear, this.policyId, this.insurancePlanId, this.cdr)

  }
// end of land management

onSave() {

  if ( !this.isFormValid() ){
    return
  }
  
  // prepare the updatedInventoryContract 
  const newInventoryContract: InventoryContract = this.getUpdatedInventoryContract()

  if (this.inventoryContract.inventoryContractGuid) {
    this.store.dispatch(UpdateInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, newInventoryContract))
  } else {
    // add new
    this.store.dispatch(AddNewInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, newInventoryContract))
  }

  this.hasDataChanged = false
}



getUpdatedInventoryContract() {

  //make a deep copy
  let updatedInventoryContract = JSON.parse(JSON.stringify(this.inventoryContract));

  var self = this;   

  // update unseededInventory values
  const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

  formFields.controls.forEach( function(formField : UntypedFormArray) {
    // go through each field and update its planting information
    let updField = updatedInventoryContract.fields.find( f => f.fieldId == formField.value.fieldId)

    if (updField) {     
      // the field is updated

      updField.displayOrder = formField.value.displayOrder

      if (! formField.value.fieldLabel || formField.value.fieldLabel.trim() == '') {
        updField.fieldLabel = 'UNSPECIFIED'
      } else {
        updField.fieldLabel = formField.value.fieldLabel
      }

      updField.landUpdateType = formField.value.landUpdateType
      updField.legalLandId = formField.value.legalLandId

      updField.otherLegalDescription = formField.value.otherLegalDescription        
      updField.uwComments = formField.value.uwComments 

      for (let i = 0; i < formField.value.plantings.length; i++) {

        let updPlanting = updField.plantings.find( p => (p.fieldId == formField.value.plantings.value[i].fieldId 
                                                        && p.plantingNumber == formField.value.plantings.value[i].plantingNumber) )
  
        if (updPlanting) {

          updPlanting.isHiddenOnPrintoutInd = formField.value.plantings.value[i].isHiddenOnPrintoutInd

          updPlanting.inventorySeededForages = [] // only deal with the seeded records that currently exist in the form

          // Update fordage seeded part of the planting
          for (let k = 0; k < formField.value.plantings.value[i].inventorySeededForages.length; k++){

            let formInvSeededForages  = formField.value.plantings.value[i].inventorySeededForages.value[k]

              const cropCommodityId = self.getCropCommodityIdFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)
              const commodityTypeCode = self.getCropCommodityTypeFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)

              let tmpAcres = parseFloat(formInvSeededForages.acresToBeSeeded)

              if (updPlanting.inventoryUnseeded) {

                updPlanting.inventoryUnseeded.deletedByUserInd = formInvSeededForages.deletedByUserInd 

                if ( self.isCropAnnual(formInvSeededForages.cropVarietyCtrl.cropVarietyId) ) { // only send update if it's silage corn
                
                  updPlanting.inventoryUnseeded.acresToBeSeeded = ( isNaN( tmpAcres) ? null : tmpAcres)
                  updPlanting.inventoryUnseeded.cropCommodityId = cropCommodityId
                  updPlanting.inventoryUnseeded.isUnseededInsurableInd = formInvSeededForages.isUnseededInsurableInd
                }

              } else {
                // add the unseeded part only for silage corn
                if ( self.isCropAnnual(formInvSeededForages.cropVarietyCtrl.cropVarietyId) ) {
                  updPlanting.inventoryUnseeded = {
                    acresToBeSeeded:        (isNaN( tmpAcres) ? null : tmpAcres),
                    cropCommodityId:        cropCommodityId,
                    cropCommodityName:      "",
                    deletedByUserInd:       formInvSeededForages.deletedByUserInd,
                    inventoryFieldGuid:     formInvSeededForages.inventoryFieldGuid, 
                    inventoryUnseededGuid:  null,
                    isUnseededInsurableInd: formInvSeededForages.isUnseededInsurableInd,
                  }
                }
              }

              updPlanting.inventorySeededForages.push (
                getInventorySeededForagesObjForSave(formInvSeededForages.inventorySeededForageGuid, formInvSeededForages.inventoryFieldGuid, 
                  cropCommodityId, formInvSeededForages.cropVarietyCtrl.cropVarietyId, commodityTypeCode,
                  formInvSeededForages.fieldAcres, formInvSeededForages.seedingYear, formInvSeededForages.seedingDate, 
                  formInvSeededForages.isIrrigatedInd, formInvSeededForages.isQuantityInsurableInd, 
                  formInvSeededForages.plantInsurabilityTypeCode, formInvSeededForages.isAwpEligibleInd, 
                  formInvSeededForages.deletedByUserInd, formInvSeededForages.linkPlantingType, null)
              )
          }
          
        } else {
          
          // add new planting, only if it hasn't been deleted by the user before the planting was saved in the database
          // first deal with the seeded forages if any 
          if (formField.value.plantings.value[i].deletedByUserInd !== true ) {
            
            // get the seeded Forage inventory unless it is deleted by the user

            let inventorySeededForages = []

            let tmpAcres = null
            let cropCommodityId = null
            let isUnseededInsurableInd = false
            let cropVarietyId = 0

            if (formField.value.plantings.value[i].inventorySeededForages ) {

                for (let k = 0; k < formField.value.plantings.value[i].inventorySeededForages.length; k++){

                  let formInvSeededForages  = formField.value.plantings.value[i].inventorySeededForages.value[k]

                  if (formInvSeededForages.deletedByUserInd !== true ) {

                    cropCommodityId = self.getCropCommodityIdFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)
                    const commodityTypeCode = self.getCropCommodityTypeFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)
                    cropVarietyId = formInvSeededForages.cropVarietyCtrl.cropVarietyId

                    tmpAcres = parseFloat(formInvSeededForages.acresToBeSeeded)
                    isUnseededInsurableInd = formInvSeededForages.isUnseededInsurableInd

                    inventorySeededForages.push (
                      getInventorySeededForagesObjForSave(formInvSeededForages.inventorySeededForageGuid, formInvSeededForages.inventoryFieldGuid, 
                        cropCommodityId, formInvSeededForages.cropVarietyCtrl.cropVarietyId, commodityTypeCode,
                        formInvSeededForages.fieldAcres, formInvSeededForages.seedingYear, formInvSeededForages.seedingDate, 
                        formInvSeededForages.isIrrigatedInd, formInvSeededForages.isQuantityInsurableInd, 
                        formInvSeededForages.plantInsurabilityTypeCode, formInvSeededForages.isAwpEligibleInd, 
                        formInvSeededForages.deletedByUserInd, null, null)

                    )
                  }
                }
            }

            let inventoryUnseeded = null

            if (self.isCropAnnual(cropVarietyId)) {

              inventoryUnseeded = {
                acresToBeSeeded:        (isNaN( tmpAcres) ? null : tmpAcres),
                cropCommodityId:        cropCommodityId,
                cropCommodityName:      "",
                deletedByUserInd:       false,
                inventoryFieldGuid:     null, 
                inventoryUnseededGuid:  null,
                isUnseededInsurableInd: isUnseededInsurableInd,
              }
            }

            updField.plantings.push({
              cropYear:                   formField.value.plantings.value[i].cropYear,
              fieldId:                    formField.value.plantings.value[i].fieldId,
              insurancePlanId:            formField.value.plantings.value[i].insurancePlanId,
              inventoryFieldGuid:         null,
              inventorySeededForages:     inventorySeededForages, // [],
              inventorySeededGrains:      [],
              inventoryUnseeded:          inventoryUnseeded,
              isHiddenOnPrintoutInd:      formField.value.plantings.value[i].isHiddenOnPrintoutInd,
              lastYearCropCommodityId:    null,
              lastYearCropCommodityName:  null,
              plantingNumber:             formField.value.plantings.value[i].plantingNumber,
              underseededAcres:           null,
              underseededCropVarietyId:   null,
            })

          }
        }
      }

    } else {
      //the field does not exist in the original inventory, we have to add it   
      // first get all plantings and comments

      let tmpPlantings = []

      for (let i = 0; i < formField.value.plantings.length; i++) {

        // now construct the plantings object
        if (formField.value.plantings.value[i].deletedByUserInd !== true ) {

          // get the forage inventory unless it is deleted by the user
          let inventorySeededForages = []

          let tmpAcres = null
          let cropCommodityId = null
          let inventoryFieldGuid = null
          let isUnseededInsurableInd = false
          let cropVarietyId = 0
          let inventoryUnseededGuid = formField.value.plantings.value[i].inventoryUnseededGuid

          for (let k = 0; k < formField.value.plantings.value[i].inventorySeededForages.length; k++){

            let formInvSeededForages  = formField.value.plantings.value[i].inventorySeededForages.value[k]

            if (formInvSeededForages.deletedByUserInd !== true ) {

              cropCommodityId = self.getCropCommodityIdFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)
              const commodityTypeCode = self.getCropCommodityTypeFromVariety(formInvSeededForages.cropVarietyCtrl.cropVarietyId)
              cropVarietyId = formInvSeededForages.cropVarietyCtrl.cropVarietyId

              tmpAcres = parseFloat(formInvSeededForages.acresToBeSeeded)
              inventoryFieldGuid = formInvSeededForages.inventoryFieldGuid
              isUnseededInsurableInd = formInvSeededForages.isUnseededInsurableInd

              inventorySeededForages.push (
                getInventorySeededForagesObjForSave(formInvSeededForages.inventorySeededForageGuid, formInvSeededForages.inventoryFieldGuid, 
                  cropCommodityId, formInvSeededForages.cropVarietyCtrl.cropVarietyId, commodityTypeCode,
                  formInvSeededForages.fieldAcres, formInvSeededForages.seedingYear, formInvSeededForages.seedingDate,
                  formInvSeededForages.isIrrigatedInd, formInvSeededForages.isQuantityInsurableInd, 
                  formInvSeededForages.plantInsurabilityTypeCode, formInvSeededForages.isAwpEligibleInd, 
                  formInvSeededForages.deletedByUserInd, null, null)
              )
            }
          }


          let inventoryUnseeded = null
          if (self.isCropAnnual(cropVarietyId)) {

            inventoryUnseeded = {
              acresToBeSeeded:        (isNaN( tmpAcres) ? null : tmpAcres),
              cropCommodityId:        cropCommodityId,
              cropCommodityName:      "",
              deletedByUserInd:       false,
              inventoryFieldGuid:     inventoryFieldGuid, 
              inventoryUnseededGuid:  inventoryUnseededGuid,
              isUnseededInsurableInd: isUnseededInsurableInd,
            }

          } else if (inventoryUnseededGuid) { // send the guid to the back end so the annual unseeded record is deleted
            inventoryUnseeded = {
              acresToBeSeeded:        null,
              cropCommodityId:        null,
              cropCommodityName:      "",
              deletedByUserInd:       false,
              inventoryFieldGuid:     inventoryFieldGuid, 
              inventoryUnseededGuid:  inventoryUnseededGuid,
              isUnseededInsurableInd: false,
            }
          }

          tmpPlantings.push({
                    cropYear:                  formField.value.plantings.value[i].cropYear,
                    fieldId:                   formField.value.plantings.value[i].fieldId,
                    insurancePlanId:           formField.value.plantings.value[i].insurancePlanId,
                    inventoryFieldGuid:        (formField.value.plantings.value[i].inventoryFieldGuid && formField.value.plantings.value[i].inventoryFieldGuid.length > 0) ? formField.value.plantings.value[i].inventoryFieldGuid : null,
                    inventorySeededForages:    inventorySeededForages, 
                    inventorySeededGrains:     [],
                    inventoryUnseeded:         inventoryUnseeded,
                    isHiddenOnPrintoutInd:     formField.value.plantings.value[i].isHiddenOnPrintoutInd,
                    lastYearCropCommodityId:   null,
                    lastYearCropCommodityName: null,
                    plantingNumber:            formField.value.plantings.value[i].plantingNumber,
                    underseededAcres:          null,
                    underseededCropVarietyId:  null,
                  })
        }
      }

      let fieldLabel = 'UNSPECIFIED'
      if (formField.value.fieldLabel && formField.value.fieldLabel.trim() !== '') {
        fieldLabel = formField.value.fieldLabel
      }

      //then add the field to the original structure 
      updatedInventoryContract.fields.push({
        cropYear:                         self.cropYear,
        displayOrder:                     formField.value.displayOrder,
        fieldId:                          (formField.value.fieldId > 0) ? formField.value.fieldId : null,
        fieldLabel:                       fieldLabel, 
        landUpdateType:                   formField.value.landUpdateType,
        legalLandId:                      (formField.value.legalLandId > 0) ? formField.value.legalLandId : null,
        otherLegalDescription:            formField.value.otherLegalDescription,
        transferFromGrowerContractYearId: (formField.value.transferFromGrowerContractYearId) ? formField.value.transferFromGrowerContractYearId : null,
        plantings:                        tmpPlantings,
        uwComments:                       formField.value.uwComments
      })
    }
  })

  return updatedInventoryContract
}

isFormValid() {

    // check for empty legal land / field id for new fields
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    for (var i = 0;  i < flds.controls.length ; i++) {
  
      // check for duplicates values in field labels and other description
      for (var n = i + 1;  n < flds.controls.length ; n++) {

        if ( flds.controls[n].value.fieldLabel && flds.controls[i].value.fieldLabel && 
             (flds.controls[i].value.fieldLabel).toUpperCase().trim() == (flds.controls[n].value.fieldLabel).toUpperCase().trim() &&
              flds.controls[i].value.fieldLabel.toUpperCase() !== "UNSPECIFIED" &&
              // both legals have the same otherDescription 
              ( 
                ( 
                  flds.controls[i].value.otherLegalDescription && flds.controls[n].value.otherLegalDescription && // they might be empty
                  (flds.controls[i].value.otherLegalDescription).toUpperCase().trim() == (flds.controls[n].value.otherLegalDescription).toUpperCase().trim() 
                )
              || // or both have no values
                ( 
                  !flds.controls[i].value.otherLegalDescription && !flds.controls[n].value.otherLegalDescription
                )
              )
              ) {

          alert("The field name " + flds.controls[i].value.fieldLabel + 
          " and the legal location " + flds.controls[i].value.otherLegalDescription + 
          " exist more than once. The inventory cannot be saved.")

          return false
        }

      }

      // now check for invalid seedingYear 
      // go through each planting 
      for (let j = 0; j < flds.controls[i].value.plantings.length; j++) {

        // check that variety is entered for each selected crop  
        for ( let k = 0; k < flds.controls[i].value.plantings.value[j].inventorySeededForages.length; k++) {

          let seedingYear = flds.controls[i].value.plantings.value[j].inventorySeededForages.value[k].seedingYear
  
          if (seedingYear) {

            if (!isNaN(seedingYear) && parseInt(seedingYear) >= 1980 && parseInt(seedingYear) <= this.cropYear) {
              // all is good - continiue
            } else {
              alert("Year Seeded needs to be between 1980 and the current policy year. Please correct the highlighted field(s). The changes were not saved.")
              return false
            }

          }
        }
      }
    }

    return true
  }

  isMyFormDirty() {

    if (!this.hasYieldData) {

      this.hasDataChanged = this.isMyFormReallyDirty()
      // this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, this.hasDataChanged )); // this generates a bug surprisingly
      asapScheduler.schedule(() => this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, this.hasDataChanged ))); 
    }
  }

  isMyFormReallyDirty() : boolean {

    // compare the original inventory contract data with the one in the form and set the flag if anything changed
    if (!this.inventoryContract) return false

    const frmMain = this.viewModel.formGroup as UntypedFormGroup

    // check if the number of fields has changed
    if ( this.inventoryContract.fields.length !== frmMain.controls.fields.value.length  ) {
      return true
    }

    // start checking if the information for each field and planting was changed from the original one
    const formFields: UntypedFormArray = frmMain.controls.fields as UntypedFormArray

    for (let i = 0; i < formFields.controls.length; i++){
      let frmField = formFields.controls[i] as UntypedFormArray

      if (frmField.value.deletedByUserInd == true ) {
        return true
      }
      
      let originalField = this.inventoryContract.fields.find( f => f.fieldId == frmField.value.fieldId)

      if (originalField) {

        // check if the field name, legal location or number of plantings for each field have changed 
        if (frmField.value.displayOrder != originalField.displayOrder ||
            frmField.value.fieldLabel != originalField.fieldLabel ||
            frmField.value.legalLandId != originalField.legalLandId ||  
            frmField.value.otherLegalDescription != originalField.otherLegalDescription ) {

            //console.log("5")
            return true
        }

        // check the plantings
        for (let k = 0; k < frmField.value.plantings.controls.length; k++){
          let frmPlanting = frmField.value.plantings.controls[k] as UntypedFormArray

          let originalPlanting = originalField.plantings.find( p => p.plantingNumber == frmPlanting.value.plantingNumber)

          if (originalPlanting) {
            
            // the planting was found in the original resource, check if anything has changed
            if ( frmPlanting.value.deletedByUserInd == true ||
                areNotEqual(frmPlanting.value.isHiddenOnPrintoutInd, originalPlanting.isHiddenOnPrintoutInd)  
              ) {

                  return true
              }


            // now check inventory seeded forages 
            for (let n = 0; n < frmPlanting.value.inventorySeededForages.controls.length; n++) {
              
              let frmInvSeededForages = frmPlanting.value.inventorySeededForages.controls[n] as UntypedFormArray
              let originalInvSeededForages = originalPlanting.inventorySeededForages.find( p => p.inventorySeededForageGuid == frmInvSeededForages.value.inventorySeededForageGuid)

              if (originalInvSeededForages) {

                if ( frmInvSeededForages.value.deletedByUserInd == true ||
                      areNotEqual(frmInvSeededForages.value.cropCommodityId, originalInvSeededForages.cropCommodityId) ||
                      areNotEqual(frmInvSeededForages.value.cropVarietyCtrl.cropVarietyId, originalInvSeededForages.cropVarietyId) ||
                      areNotEqual(frmInvSeededForages.value.commodityTypeCode, originalInvSeededForages.commodityTypeCode) ||
                      areNotEqual(frmInvSeededForages.value.isQuantityInsurableInd, originalInvSeededForages.isQuantityInsurableInd) ||
                      areNotEqual(frmInvSeededForages.value.fieldAcres, originalInvSeededForages.fieldAcres) ||
                      areNotEqual(frmInvSeededForages.value.seedingYear, originalInvSeededForages.seedingYear)  ||
                      areDatesNotEqual(frmInvSeededForages.value.seedingDate, originalInvSeededForages.seedingDate)  ||
                      areNotEqual(frmInvSeededForages.value.isIrrigatedInd, originalInvSeededForages.isIrrigatedInd) ||
                      areNotEqual(frmInvSeededForages.value.plantInsurabilityTypeCode, originalInvSeededForages.plantInsurabilityTypeCode) ||
                      areNotEqual(frmInvSeededForages.value.isAwpEligibleInd, originalInvSeededForages.isAwpEligibleInd) ||
                      (originalPlanting.inventoryUnseeded && areNotEqual(frmInvSeededForages.value.acresToBeSeeded, originalPlanting.inventoryUnseeded.acresToBeSeeded)) ||
                      (originalPlanting.inventoryUnseeded && areNotEqual(frmInvSeededForages.value.isUnseededInsurableInd, originalPlanting.inventoryUnseeded.isUnseededInsurableInd) )
                  ) {
                    
                  return true
                }

                if (!originalPlanting.inventoryUnseeded ) {

                  if ( frmInvSeededForages.value.acresToBeSeeded || 
                    frmInvSeededForages.value.isUnseededInsurableInd !== false) {

                    return true

                  }
                }
              } else {
                // new inventorySeededForages record
                // check if it's not deleted by the user 
                
                if (frmInvSeededForages.value.deletedByUserInd !== true && 
                    frmInvSeededForages.value.cropVarietyCtrl.cropCommodityId ||  
                    frmInvSeededForages.value.fieldAcres ||  
                    frmInvSeededForages.value.seedingYear || 
                    frmInvSeededForages.value.seedingDate.toString() != 'Invalid Date' || 
                    frmInvSeededForages.value.isHiddenOnPrintoutInd !== false ||
                    frmInvSeededForages.value.isIrrigatedInd !== false ||
                    frmInvSeededForages.value.isQuantityInsurableInd !== false ||
                    frmInvSeededForages.value.plantInsurabilityTypeCode ||
                    frmInvSeededForages.value.isAwpEligibleInd !== true || 
                    frmInvSeededForages.value.acresToBeSeeded || 
                    frmInvSeededForages.value.isUnseededInsurableInd !== false) {

                    return true
                }

              }
            }

          } else {
            // the planting was not found in the original resourse, check if it's an empty or deleted planting
            if (frmPlanting.value.deletedByUserInd !== true && 
                ( frmPlanting.value.inventorySeededForages.controls[0].value.cropVarietyCtrl.cropCommodityId || 
                  frmPlanting.value.inventorySeededForages.controls[0].value.fieldAcres ||  
                  frmPlanting.value.inventorySeededForages.controls[0].value.seedingYear || 
                  frmPlanting.value.inventorySeededForages.controls[0].value.seedingDate.toString() != 'Invalid Date' || 
                  frmPlanting.value.inventorySeededForages.controls[0].value.acresToBeSeeded || 
                  frmPlanting.value.inventorySeededForages.controls[0].value.isUnseededInsurableInd !== false )) {
                  // a new planting was added

                  return true
                }
          }
        }

        //check the uwcomments
        for (let k = 0; k < frmField.value.uwComments.length; k++) {

          // now check if anything changed in the comments
          if (frmField.value.uwComments.length != originalField.uwComments.length) {

            return true
          }
          
          // go inside the comments and check for changes there
          for (let n = 0; n < frmField.value.uwComments.length; n++){
            
            let frmUwComments = frmField.value.uwComments[n] 

            let originalUwComment = originalField.uwComments.find( p => p.underwritingCommentGuid == frmUwComments.underwritingCommentGuid)

            if (originalUwComment && 
                (areNotEqual(frmUwComments.underwritingComment, originalUwComment.underwritingComment) || frmUwComments.deletedByUserInd == true )
               ) {

              return true
            }

          }

        }
      }
    }

    return false
  }


  drop(event: CdkDragDrop<string[]>) {
    
    let fields = this.getViewModel().formGroup.controls.fields as UntypedFormArray
    dragField(event, fields)

    // this.isMyFormDirty()

  }

  isYearValid(fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    let seedingYear = invSeeded.controls['seedingYear'].value
  
    if (seedingYear) {

      if (!isNaN(seedingYear) && parseInt(seedingYear) >= 1980 && parseInt(seedingYear) <= this.cropYear) {
        return false
      } else {
        return true
      }

    } else {
      return false
    }
  }

  getCropCommodityIdFromVariety(cropVarietyId) {
    let cropCommodityId = this.cropVarietyOptions.find(el => el.cropVarietyId == cropVarietyId).cropCommodityId
    return cropCommodityId
  }

  getCropCommodityTypeFromVariety(cropVarietyId) {

    if (cropVarietyId) {
      let commodityType = this.cropVarietyOptions.find(el => el.cropVarietyId == cropVarietyId).cropVarietyCommodityTypes[0].commodityTypeCode
      return commodityType
    }else {
      return null
    }
    
  }

  onPrint() {

    let reportName = replaceNonAlphanumericCharacters(this.growerContract.growerName) + "-Inventory" 
    this.store.dispatch(GetInventoryReport(reportName, this.policyId, "", INSURANCE_PLAN.FORAGE.toString(), "", "", "", "", "", ""))
    
  }

  isQuantityInsurable(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex];

    let variety = this.getVariety(fieldIndex, plantingIndex, invSeededIndex);

    // Only show Quantity Ins checkbox if variety is quantity insurable
    if(variety && variety.isQuantityInsurableInd) {
      return true
    } else {
      invSeeded.controls['isQuantityInsurableInd'].setValue(false)
      return false
    }
  }

  isUnseededInsurable(fieldIndex, plantingIndex, invSeededIndex) {

    let variety = this.getVariety(fieldIndex, plantingIndex, invSeededIndex);

    // Only show Unseeded Ins checkbox if variety is unseeded insurable
    if(variety && variety.isUnseededInsurableInd) {
      return true
    }

    return false

  }

  isAwpEligible(fieldIndex, plantingIndex, invSeededIndex) {

    let variety = this.getVariety(fieldIndex, plantingIndex, invSeededIndex);

    // Only show AWP checkbox if variety is AWP eligible
    if(variety){
      if(variety.isAwpEligibleInd) {
        return true
      }
    } else {
      return true //Also show checkbox if no variety is selected
    }

    return false

  }

  isAnnualPlanting(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    let selectedCropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId

    if( selectedCropVarietyId ) {
      return this.isCropAnnual(selectedCropVarietyId)
    }

  }

  isCropAnnual(cropVarietyId) {

    if (cropVarietyId) {

      let item = this.cropVarietyOptions.find( x => x.cropVarietyId == cropVarietyId)

      let cmdtyId = 0

      if (item) {
        cmdtyId = item.cropCommodityId
      }

      if ( cmdtyId > 0) {
        let plantDuration = this.cropCommodityList.collection.find (x => x.cropCommodityId == cmdtyId).plantDurationTypeCode

        if (plantDuration && plantDuration == PLANT_DURATION.ANNUAL ) {
          return true
        }
      }

    }
    
    return false // default
  }

  onDateChange(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    invSeeded.controls['seedingDate'].nativeElement.style.backgroundColor = 'transparent'

  }

  validateSeedingDate(event, fieldIndex, plantingIndex, invSeededIndex){
  
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededForages'].value.controls[invSeededIndex]

    // clear the selected plant insurability as it may change
    invSeeded.controls['plantInsurabilityTypeCode'].setValue(null) 

    if (event.target.value && new Date(event.target.value).toString() == 'Invalid Date') {
      alert("The Seeding Date is invalid and it will not be saved.")
      invSeeded.controls['seedingDate'].nativeElement.style.backgroundColor = 'yellow'
    } else {
      invSeeded.controls['seedingDate'].nativeElement.style.backgroundColor = 'transparent'
    }
  }

  onDeleteInventory() {

    //Ask for confirmation before deleting all Inventory data
    if ( confirm("You are about to delete all unseeded and seeded inventory data for the policy. Do you want to continue?") ) {

      if (this.inventoryContract && this.policyId) {
        
        this.store.dispatch(DeleteInventoryContract(INVENTORY_COMPONENT_ID, 
                                this.inventoryContract.inventoryContractGuid, 
                                this.policyId, 
                                this.inventoryContract.etag))
      }
      
    }
  }

  isLinkedField(fieldId): boolean {
    return isLinkedFieldCommon(fieldId, this.inventoryContract)
  }

  linkedFieldTooltip(fieldId): string {
    return linkedFieldTooltipCommon(fieldId, this.inventoryContract)
  }

  isLinkedPlanting(fieldId, inventoryFieldGuid, inventorySeededForages) : boolean{

    if (this.inventoryContract && this.inventoryContract.fields) {

      let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)
  
      if (field && field.plantings) {
        // find the planting 
        let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )
    
        if (planting && planting.inventorySeededForages[0] && planting.inventorySeededForages[0].linkedPlanting) {
          return true
        }
      }

    }
    return false
  }

  linkedPlantingTooltip(fieldId, inventoryFieldGuid, inventorySeededForages): string { 
    let tooltip = ""

    if (this.inventoryContract && this.inventoryContract.fields) {
      
      let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)
    
      if (field && field.plantings) {
        // find the planting 
        let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )
    
        if (planting && planting.inventorySeededForages[0] && planting.inventorySeededForages[0].linkedPlanting) {
          tooltip = "This planting is linked to a Grain planting"
        }
      }
    }
    return tooltip
  }

  isWarningVisible(fieldId, inventoryFieldGuid, inventorySeededForages): boolean {

    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return false
    }

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field && field.plantings) {
      // find the planting from the original api call
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )
      
      if (inventorySeededForages && inventorySeededForages.value.fieldAcres 
        && inventorySeededForages.value.cropVarietyCtrl.cropVarietyId 
        && planting && planting.inventorySeededForages[0] && planting.inventorySeededForages[0].linkedPlanting ) {
        
        // if the Underseeded Variety and/or Acres do not match the linked Seeded FORAGE Planting
        if (inventorySeededForages.value.fieldAcres!= planting.inventorySeededForages[0].linkedPlanting.acres ||
          inventorySeededForages.value.cropVarietyCtrl.cropVarietyId != planting.inventorySeededForages[0].linkedPlanting.cropVarietyId) {

            return true
        }
      }
    }
    return false
  }

  getWarningTooltip(fieldId, inventoryFieldGuid, inventorySeededForages): string {

    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return ""
    }

    let tooltip = ""

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field && field.plantings) {
      // find the planting from the original api call
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )
      
      if (inventorySeededForages && inventorySeededForages.value.fieldAcres 
        && inventorySeededForages.value.cropVarietyCtrl.cropVarietyId 
        && planting && planting.inventorySeededForages[0] && planting.inventorySeededForages[0].linkedPlanting ) {
        
        // if the Underseeded Variety and/or Acres do not match the linked Seeded FORAGE Planting
        if (inventorySeededForages.value.fieldAcres!= planting.inventorySeededForages[0].linkedPlanting.acres ||
          inventorySeededForages.value.cropVarietyCtrl.cropVarietyId != planting.inventorySeededForages[0].linkedPlanting.cropVarietyId) {

            tooltip = "The Crop Seeded and/or Field Acres do not match the linked underseeded Grain planting. "
        }
      }

    }
    return tooltip
  }

  isInfoVisible(fieldId): boolean {

    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return false
    }

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
 
      // if the FORAGE Policy has a different Grower than the GRAIN Policy
      if (
        field.policies.length > 0 && field.policies[0].growerNumber !== this.growerContract.growerNumber) {
        return true
      }
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    return false
  }

  setFormSeededStyles(){
    return {
      'grid-template-columns':  'auto 140px 150px 12px 190px'
    }
  }


  toggleHiddenOnPrintout(fieldIndex, plantingIndex) {  
    
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    
    // get the current value
    let isHiddenOnPrintoutInd = pltg.controls['isHiddenOnPrintoutInd'].value
    pltg.controls['isHiddenOnPrintoutInd'].setValue(!isHiddenOnPrintoutInd)
    
    this.isMyFormDirty()
  }

  isPlantingHiddenOnPrintout(fieldIndex, plantingIndex) {
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]

    return pltg.controls['isHiddenOnPrintoutInd'].value
  }

  isFieldHiddenOnPrintout(fieldIndex) {

    // return false unless at least one planting is not hidden
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    for (let i = 0; i < flds.controls[fieldIndex]['controls']['plantings'].value.controls.length; i++ ) {

      let pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[i]
      let isHiddenOnPrintoutInd = pltg.controls['isHiddenOnPrintoutInd'].value

      if ( isHiddenOnPrintoutInd == false ) {
        return false 
      }
    }

    return true // all plantings are hidden
  }

  checkForHiddenFieldInTotals() {

    // raises a flag if there is an insured field with acres that is marked as hidden 
    const frmMain = this.viewModel.formGroup as UntypedFormGroup
    const formFields: UntypedFormArray = frmMain.controls.fields as UntypedFormArray

    for (let i = 0; i < formFields.controls.length; i++){
      let frmField = formFields.controls[i] as UntypedFormArray
      	  
      for (let k = 0; k < frmField.value.plantings.controls.length; k++){
        let frmPlanting = frmField.value.plantings.controls[k] as UntypedFormArray
        
        // now check inventory seeded forages 
        for (let n = 0; n < frmPlanting.value.inventorySeededForages.controls.length; n++) {
                  
          let inventorySeededForages = frmPlanting.value.inventorySeededForages.controls[n] as UntypedFormArray
    
          let fieldAcres = !isNaN( parseFloat(inventorySeededForages.value.fieldAcres)) ?  parseFloat(inventorySeededForages.value.fieldAcres) : 0

          if ( frmPlanting.value.isHiddenOnPrintoutInd && fieldAcres > 0 && 
                (inventorySeededForages.value.isQuantityInsurableInd || inventorySeededForages.value.isUnseededInsurableInd ||
                  inventorySeededForages.value.plantInsurabilityTypeCode
                ) 
             ) {
            
            this.isHiddenFieldInTotals = true
            return
          }
        }
      }
	  }
    
    this.isHiddenFieldInTotals = false // default
  }
}

