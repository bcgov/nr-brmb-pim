
import { ChangeDetectionStrategy, Component, SimpleChanges } from '@angular/core';
import { FormArray, FormGroup } from '@angular/forms';
import { isBaseCommodity, makeTitleCase } from 'src/app/utils';
import { CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';
import { GrainInventoryComponent } from "../grain-inventory.component";
import { CropCommodityVarietyOptionsType, roundUpDecimalAcres } from '../../inventory-common';
import {ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'grain-unseeded-inventory',
  templateUrl: './grain-unseeded-inventory.component.html',
  styleUrls: ['./grain-unseeded-inventory.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})

export class GrainUnseededInventoryComponent extends GrainInventoryComponent { 

  filteredCommodityVarietyOptions: CropCommodityVarietyOptionsType[];

  ngOnInit(): void {
    super.ngOnInit()
    this.setOtherChangesComment();
  }

 ngOnChanges(changes: SimpleChanges) {
  super.ngOnChanges(changes);

  this.ngOnChanges3(changes);

  }

  ngOnChanges3(changes: SimpleChanges) {

    if ( changes.inventoryContract && this.inventoryContract && this.inventoryContract.commodities ) {
      this.addAllCommodities()
    }
  }

  commodityVarietyFocus(fieldIndex, plantingIndex) {
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    const lastYearCropCommodityVarietyName = pltg.controls['lastYearCropCommodityVarietyName'].value?.toLowerCase();

    if (lastYearCropCommodityVarietyName) {
      this.filteredCommodityVarietyOptions = this.lastYearsCropOptions.filter(option => {
        const name = (option.cropCommodityVarietyName || '').toLowerCase();
        return name.includes(lastYearCropCommodityVarietyName);
      });
    } else {
      this.filteredCommodityVarietyOptions = this.lastYearsCropOptions.slice();
    }
  }

  searchCommodityVariety(value) {
    value = value.toLowerCase();
    this.filteredCommodityVarietyOptions = this.lastYearsCropOptions.filter(option => {
      const name = (option.cropCommodityVarietyName || '').toLowerCase();
      return name.includes(value);
    });
  }

  displayCommodityVarietyFn(value: string): string {
    return value ? makeTitleCase(value) : '';
  }

  commodityVarietySelected(event, fieldIndex, plantingIndex) {
    const lastYearCropCommodityVarietyName = event.option.value;

    // find the corresponding commodity variety id
    let lastYearCropCommodityVarietyId = '0_0';
    let isUnseededInsurableInd = null;
    for (const option of this.lastYearsCropOptions) {
      if (option.cropCommodityVarietyName === lastYearCropCommodityVarietyName) {
        lastYearCropCommodityVarietyId = option.cropCommodityVarietyId;
        isUnseededInsurableInd = option.isUnseededInsurableInd;
        break;
      }
    }

    // set the commodity variety id and name
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray;
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex];
    pltg.controls['lastYearCropCommodityVarietyId'].setValue(lastYearCropCommodityVarietyId);
    pltg.controls['lastYearCropCommodityVarietyName'].setValue(lastYearCropCommodityVarietyName);
    if (isUnseededInsurableInd !== null) {
      pltg.controls['isUnseededInsurableInd'].setValue(isUnseededInsurableInd);
    }
  }


  addAllCommodities() {
    // first add all commodities
    // then update them based on what's coming from the backend

    let cmdtiesFA: FormArray = this.viewModel.formGroup.controls.commodities as FormArray
    cmdtiesFA.clear()

    var self = this
    this.cropCommodityList.collection.forEach( ccm => {
      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [ccm.cropCommodityId],
        cropCommodityName:              [ccm.commodityName],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [false],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     [0],
        isVisible:                     [false]
      } ) )
    } )

    // add unspecified commodity
    cmdtiesFA.push( this.fb.group( {
      cropCommodityId:                [CROP_COMMODITY_UNSPECIFIED.ID],          
      cropCommodityName:              [CROP_COMMODITY_UNSPECIFIED.OTHER_NAME],
      inventoryContractCommodityGuid: [ ],
      inventoryContractGuid:          [ ],
      totalSeededAcres:               [0], 
      totalSpotLossAcres:             [0],
      isPedigreeInd:                  [false],
      totalUnseededAcres:             [0],
      totalUnseededAcresOverride:     [0],
      isVisible:                     [false],
    } ) )

    this.inventoryContract.commodities.forEach( cmdt => this.updateCommodity( cmdt ) )
  }

  updateCommodity( cmdty ) {

    const cmdtiesFA: FormArray = this.viewModel.formGroup.controls.commodities as FormArray

      cmdtiesFA.controls.forEach ( function(cmdtyFC: FormGroup){

        if (cmdtyFC.value.cropCommodityId == cmdty.cropCommodityId) {

          cmdtyFC.controls.inventoryContractCommodityGuid.setValue(cmdty.inventoryContractCommodityGuid)
          cmdtyFC.controls.inventoryContractGuid.setValue(cmdty.inventoryContractGuid)
          cmdtyFC.controls.totalSeededAcres.setValue(cmdty.totalSeededAcres)
          cmdtyFC.controls.totalUnseededAcres.setValue(cmdty.totalUnseededAcres)
          cmdtyFC.controls.totalUnseededAcresOverride.setValue(cmdty.totalUnseededAcresOverride)
        }
      })    
  }

  isChecked(val):boolean{

    this.calculateSumTotals()

    this.isMyFormDirty()

    if (val && val.toString().toUpperCase() === "Y") 
      return true;
    else
      return false;

  }


  isAddPlantingVisible(planting) {

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    const fld: FormArray  =  flds.controls.find( f => f.value.fieldId == planting.value.fieldId ) as FormArray 

    //in order to show Add Planting button, find the max planting number for that field that hasn't been deleted 
    let numPlantings = 0
    let maxNotDeletedPlantingNumber = 0
    for (let i = 0; i < fld.value.plantings.length; i++) {
      if (fld.value.plantings.value[i].deletedByUserInd != true ) {
        maxNotDeletedPlantingNumber = fld.value.plantings.value[i].plantingNumber
        numPlantings ++
      }
    }

    // if the planting row is empty do not show Add New Planting button
    if ( isNaN( parseFloat(planting.value.acresToBeSeeded)) || !planting.value.cropCommodityId) {
      return false
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

  onDeletePlanting(planting) {
    var self = this

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    flds.controls.forEach( function(field : FormGroup) {

      if (field.value.fieldId == planting.value.fieldId) { 
        // we have found the field in the form that contains the planting to be deleted
        // count the number of plantings for that field that have not been deleted
        let numPlantings = 0      

        for (let i = 0; i < field.value.plantings.length; i++) {
          if (field.value.plantings.value[i].deletedByUserInd != true ) {
            numPlantings ++
          }
        } 

        // now find the planting to be deleted from the form and set its values to null 
        for( var i = 0; i < field.value.plantings.length; i++){

          if (field.value.plantings.value[i].plantingNumber == planting.value.plantingNumber && field.value.plantings.value[i].deletedByUserInd != true) {
            
            let unseededControls = field.value.plantings['controls'][i].controls 
            // check for seeded data

            if ( field.value.plantings.value[i].inventorySeededGrains && field.value.plantings.value[i].inventorySeededGrains.length > 0 && 
              ( field.value.plantings.value[i].inventorySeededGrains.controls[0].controls.seededAcres.value > 0 ||
                field.value.plantings.value[i].inventorySeededGrains.controls[0].controls.cropCommodityId.value > 0)) {

              if ( confirm("There is seeded data for that planting. The unseeded data will be deleted but the seeded data will remain. Proceed with deletion?") ) {
                self.resetUnseededDataForDeletePlanting(unseededControls, numPlantings, true)
              }

            } else { // no seeded data
              self.resetUnseededDataForDeletePlanting(unseededControls, numPlantings, false)
            }

          }
        }                
      }
    })

    this.calculateSumTotals()

    this.isMyFormDirty()
  }

  resetUnseededDataForDeletePlanting( unseededControls, numPlantings, hasSeededData) {

    unseededControls.acresToBeSeeded.setValue(null)
    unseededControls.cropCommodityId.setValue(CROP_COMMODITY_UNSPECIFIED.ID)
    unseededControls.cropCommodityName.setValue(CROP_COMMODITY_UNSPECIFIED.NAME)
    unseededControls.isUnseededInsurableInd.setValue(true)
            
    if(numPlantings > 1 && !hasSeededData) {
      // if there is more than one planting then mark the planting as deleted
      unseededControls.deletedByUserInd.setValue(true)
      // // console.log("deletedByUserInd is set to true ")
    }

  }

  roundUpAcres(fieldIndex, plantingIndex){
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
     
    let acres = pltg.controls['acresToBeSeeded'].value

    pltg.controls['acresToBeSeeded'].setValue(roundUpDecimalAcres(acres))

    this.isMyFormDirty()
  }

  roundUpProjectedAcres(cmdtyIndex){
    
    const frmCmdty: FormArray = this.viewModel.formGroup.controls.commodities['controls'][cmdtyIndex] as FormArray

    let acres = frmCmdty['controls']['totalUnseededAcresOverride'].value

    frmCmdty.controls['totalUnseededAcresOverride'].setValue(roundUpDecimalAcres(acres))

    this.isMyFormDirty()
  }

  showBaseCmdty(cropCommodityId: number) {
    if (cropCommodityId == CROP_COMMODITY_UNSPECIFIED.ID) {
      return true
    }

    return isBaseCommodity(cropCommodityId, this.cropCommodityList)
  }
    
  otherChangesIndClicked(){
      
    this.setOtherChangesComment();

    this.isMyFormDirty();
  }

  setOtherChangesComment(){
    if(this.viewModel.formGroup.controls.otherChangesInd.value){
      this.viewModel.formGroup.controls.otherChangesComment.enable();
    } else {
      this.viewModel.formGroup.controls.otherChangesComment.disable();
      this.viewModel.formGroup.controls.otherChangesComment.setValue("");
    }
  }

  changeCommentRequired(){

    if(this.viewModel.formGroup.controls.otherChangesInd && this.viewModel.formGroup.controls.otherChangesInd.value){
      if(this.viewModel.formGroup.controls.otherChangesComment && this.viewModel.formGroup.controls.otherChangesComment.value.trim().length == 0 ) {
        return true
      }
    }
    return false;
  }

  // deferred for later
  // isDeletePlantingVisible(planting) {
  //   const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
  //   let numPlantings = 0 
  //   let hasSeededData = false 

  //   flds.controls.forEach( function(field : FormGroup) {

  //     if (field.value.fieldId == planting.value.fieldId) { 
  //       // we have found the field in the form that contains the planting to be deleted
  //       // count the number of plantings for that field that have not been deleted
            
  //       for (let i = 0; i < field.value.plantings.length; i++) {
  //         if (field.value.plantings.value[i].deletedByUserInd != true ) {
  //           numPlantings ++
  //         }

  //         if ( field.value.plantings.value[i].inventorySeededGrains && field.value.plantings.value[i].inventorySeededGrains.length > 0 && 
  //           ( field.value.plantings.value[i].inventorySeededGrains.controls[0].controls.seededAcres.value > 0 ||
  //             field.value.plantings.value[i].inventorySeededGrains.controls[0].controls.cropCommodityId.value > 0)) {
                
  //                 hasSeededData = true
  //         }
  //       } 

  //       return
  //     }
  //   })

  //   if (isNaN(parseFloat(planting.value.acresToBeSeeded))&& isNaN(parseFloat(planting.value.cropCommodityId)) &&
  //       planting.value.isUnseededInsurableInd == true ) {

  //       if (hasSeededData == false && numPlantings > 1)
  //         return true
  //       else
  //         return false
  //   }

  //   return true

  // }

  setStyles(){

    let styles = {
      'width' : '1286px',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr',
    }

    return styles;
  }

  setPlantingStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr',
      'align-items': 'stretch',
      'width': '676px'
    }

    return styles;
  }
  
  setFormSeededStyles(){
    return {
      'grid-template-columns':  'auto 140px 150px 12px 190px'
    }
  }

  onAddPlantingLocal(planting) {
    if (this.securityUtilService.canEditInventory() && this.isAddPlantingVisible(planting)) {
      this.onAddPlanting(planting)
    }
  }


}
