import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { ParamMap } from '@angular/router';
import { BaseComponent } from '../../common/base/base.component';
import { CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { BerriesInventoryComponentModel } from './berries-inventory.component.model';
import { BERRY_COMMODITY, CROP_COMMODITY_UNSPECIFIED, INSURANCE_PLAN } from 'src/app/utils/constants';
import { AddNewInventoryContract, DeleteInventoryContract, GetInventoryReport, LoadInventoryContract, RolloverInventoryContract, UpdateInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { isInt, replaceNonAlphanumericCharacters } from 'src/app/utils';
import { AddFieldComponent } from './add-field/add-field.component';
import { AddLandPopupData } from '../add-land/add-land.component';
import { createNewAnnualFieldObject, getDefaultInventoryBerries, getDefaultPlanting } from '../inventory-common';
import { AnnualFieldRsrc, InventoryBerries, InventoryField } from '@cirras/cirras-underwriting-api';

@Component({
  selector: 'berries-inventory',
  templateUrl: './berries-inventory.component.html',
  styleUrl: './berries-inventory.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesInventoryComponent extends BaseComponent implements OnChanges {
  @Input() inventoryContract: InventoryContract;
  @Input() cropCommodityList: CropCommodityList;
  @Input() growerContract: UwContract;
  @Input() isUnsaved: boolean;

  BERRY_COMMODITY = BERRY_COMMODITY

  selectedCommodity 
  selectedCommodityTooltip = "Select a commodity"
  policyId
  cropCommodityOptions = [];
  cropVarietyOptions = [];

  isHiddenPlantingInTotals = false; 
  hasYieldData = false; // TODO

  numComponentReloads = 0 // the field-list, etc..  components are not loaded for whatever reason, so I have to go thru an external variable to make it reload

  initModels() {
    this.viewModel = new BerriesInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
  }

  loadPage() {
    this.componentId = INVENTORY_COMPONENT_ID;
    
      this.route.paramMap.subscribe(
        (params: ParamMap) => {
            this.policyId = params.get("policyId") ? params.get("policyId") : "";
        }
      );
  }

  getViewModel(): BerriesInventoryComponentModel  { //
      return <BerriesInventoryComponentModel>this.viewModel;
  }

  ngOnChanges(changes: SimpleChanges) {

    // populate commodity and variety lists
    if (changes.cropCommodityList && this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length ) {

      this.populateCropAndVarietyOptions()
    }

    if (changes.inventoryContract) {
      this.inventoryContract = changes.inventoryContract.currentValue;

      if (this.inventoryContract && this.inventoryContract.fields && this.inventoryContract.fields.length > 0) {
        this.onCheckForHiddenPlantingsInTotals() // check for values that don't show in the report but are included in the totals

        // check if the selectedCommodity is already set, if yes, then check if the inventoryContract has fields with the same commodity
        // if yes, then no need to change the selected commodity, 
        // otherwise find out what commodities are on the policy and assign the default commodity to one them

        let shouldSetCommodity = true

        if (this.selectedCommodity) {
          for (let i = 0; i < this.inventoryContract.fields.length; i++){

            if (this.inventoryContract.fields[i].plantings && this.inventoryContract.fields[i].plantings.length > 0) {

              let pltg = this.inventoryContract.fields[i].plantings.find(x => x.inventoryBerries.cropCommodityId == this.selectedCommodity)

              if (pltg) {
                shouldSetCommodity = false
                break
              }
            }
          }
        }

        if (shouldSetCommodity) {
          for (let i = 0; i < this.inventoryContract.fields.length; i++){

            if (this.inventoryContract.fields[i].plantings && this.inventoryContract.fields[i].plantings.length > 0) {
              
              for (let j = 0; j < this.inventoryContract.fields[i].plantings.length; j++){
                
                let pltg = this.inventoryContract.fields[i].plantings[j]

                if( pltg.inventoryBerries && pltg.inventoryBerries.cropCommodityId ) {

                  this.selectedCommodity = pltg.inventoryBerries.cropCommodityId
                  this.getViewModel().formGroup.controls.selectedCommodity.setValue(this.selectedCommodity)

                  return
                }

              }
            }
          }
        }

        //if there are no commodities on the policy then show blank commodity in the commodity dropdown
        if (!this.selectedCommodity) {
          // Add empty commodity
          this.cropCommodityOptions = [
            {
              cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
			        commodityName: CROP_COMMODITY_UNSPECIFIED.NAME
            },
            ...this.cropCommodityOptions
          ]

          this.selectedCommodity = CROP_COMMODITY_UNSPECIFIED.ID // set as default for now otherwise no fields will show up
          this.getViewModel().formGroup.controls.selectedCommodity.setValue(this.selectedCommodity)  
        }
      }      
    }
  }

  populateCropAndVarietyOptions() {
    // clear the crop options
    this.cropCommodityOptions = []
    this.cropVarietyOptions = [] 

    // add empty variety
    this.cropVarietyOptions.push ({
      cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
      cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
      varietyName: CROP_COMMODITY_UNSPECIFIED.NAME
    })

    var self = this
    this.cropCommodityList.collection.forEach( ccm => {

        // populate cropCommodityOptions
        self.cropCommodityOptions.push({
          cropCommodityId: ccm.cropCommodityId,
          commodityName: ccm.commodityName
        })
        
        // populate cropVarietyOptions 
        ccm.cropVariety.forEach(cv => self.cropVarietyOptions.push ({
                                                            cropCommodityId: cv.cropCommodityId,
                                                            cropVarietyId: cv.cropVarietyId,
                                                            varietyName: cv.varietyName ,
                                                            cropVarietyCommodityTypes: cv.cropVarietyCommodityTypes, 
                                                          }))

      })
    }

  setFormSeededStyles(){
    return {
      'grid-template-columns':  '250px 200px auto  140px 150px 12px 190px'
    }
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

      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, false ));
      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }
  
  onSave() {
    
    this.setPlantingsForDeletion()
  
    if ( !this.isFormValid() ){
      return
    }

    this.manageNewFields()

    if (this.inventoryContract.inventoryContractGuid) {
      this.store.dispatch(UpdateInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, this.inventoryContract))
    } else {
      // add new
      this.store.dispatch(AddNewInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, this.inventoryContract))
    }
  
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, false ));
  }

  setPlantingsForDeletion() {

    for (let field of this.inventoryContract.fields) {
      for (let i = 0; i < field.plantings.length; i++ ) {

        if (field.plantings[i].inventoryBerries.deletedByUserInd && !field.plantings[i].inventoryFieldGuid) {
          // delete the planting from the inventory contract object and don't send it to the database

          field.plantings.splice(i,1)
          i--
        }
      }
    }
  }

  isFormValid() {

    for (let field of  this.inventoryContract.fields) {
      for (let planting of field.plantings) {

        if (planting && planting.inventoryBerries && planting.inventoryBerries.cropCommodityId == this.selectedCommodity) {
          let plantedYear = planting.inventoryBerries.plantedYear
          let rowSpacing = planting.inventoryBerries.rowSpacing
          
          if (this.hasPartialData(field.fieldId, planting)) {
            return false
          }

          // Planted Year: 4-digit positive integers are allowed
          if ( plantedYear && (!isInt(plantedYear) || plantedYear < 1000 || plantedYear > 9999 ) ) {
            alert("Planted Year for Field Id " + field.fieldId + " should be a 4-digit positive integer.")
            return false
          }

          // Row Spacing: only 0 and positive integer values up to 4 digits are accepted.
          if ( rowSpacing && (!isInt(rowSpacing) || rowSpacing < 0 || rowSpacing > 9999 )) {
            alert("Row Spacing for Field Id " + field.fieldId + " should be a positive integer.")
            return false
          }
        }
      }
    }
    
    return true // all checks have passed successfully
  }

  hasPartialData(fieldId, planting) {
    let plantedYear = planting.inventoryBerries.plantedYear
    let plantedAcres = planting.inventoryBerries.plantedAcres
    let variety = planting.inventoryBerries.cropVarietyId
    let rowSpacing = planting.inventoryBerries.rowSpacing
    let plantSpacing = planting.inventoryBerries.plantSpacing
    let isQuantityInsurableInd = planting.inventoryBerries.isQuantityInsurableInd
    let isPlantInsurableInd = planting.inventoryBerries.isPlantInsurableInd
    let bogId = planting.inventoryBerries.bogId
    let bogMowedDate = planting.inventoryBerries.bogMowedDate  // optional
    let bogRenovatedDate = planting.inventoryBerries.bogRenovatedDate // optional
    let isHarvestedInd = planting.inventoryBerries.isHarvestedInd

    // All user entered fields are mandatory: if at least one field has a value or one of the checkboxes is checked then all should have a value
    let message = "Partial data entry is not accepted. Please fill in all values for field ID " + fieldId + " or none of them."
    
    // Blueberry
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry) {
      if (plantedYear && (!plantedAcres || !variety || !rowSpacing || !plantSpacing ) ) {
        alert(message)
        return true
      }

      if (!plantedYear && (plantedAcres || variety || rowSpacing || plantSpacing ) ) {
        alert(message)
        return true
      }

      if ( (isQuantityInsurableInd || isPlantInsurableInd) && (!plantedAcres || !plantedYear || !variety || !rowSpacing || !plantSpacing ) ) {
        alert(message)
        return true
      }
    }

    // Raspberry
    if (this.selectedCommodity == BERRY_COMMODITY.Raspberry) {
      if (plantedYear && (!plantedAcres || !variety) ) {
        alert(message)
        return true
      }

      if (!plantedYear && (plantedAcres || variety ) ) {
        alert(message)
        return true
      }

      if ( isQuantityInsurableInd && (!plantedAcres || !plantedYear || !variety ) ) {
        alert(message)
        return true
      }
    }

    // Strawberry
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      if (plantedYear && (!plantedAcres || !variety) ) {
        alert(message)
        return true
      }

      if (!plantedYear && (plantedAcres || variety ) ) {
        alert(message)
        return true
      }

      if ( (isQuantityInsurableInd || isPlantInsurableInd) && (!plantedAcres || !plantedYear || !variety ) ) {
        alert(message)
        return true
      }
    }

    message = "Partial data entry is not accepted. Bog Id, Planted Year, Planted Acres and Variety are mandatory for Cranberries. " +
      "Please fill in these values for field ID " + fieldId + " or clear all planting values for that field."
    
    if (this.selectedCommodity == BERRY_COMMODITY.Cranberry) {
      if (plantedYear && (!bogId || !plantedAcres || !variety ) ) {
        alert(message)
        return true
      }

      if (!plantedYear && (bogId || plantedAcres || variety || bogMowedDate || bogRenovatedDate) ) {
        alert(message)
        return true
      }

      if ( (isQuantityInsurableInd || isHarvestedInd) && (!bogId || !plantedAcres || !plantedYear || !variety) ) {
        alert(message)
        return true
      }
    }

    return false
  }

  manageNewFields(){

    // TODO don't send the deleted new fields to the API 

    // sets the new field ids to null
    for (let field of  this.inventoryContract.fields) {
      if (field.fieldId < 0 && field.isNewFieldUI == true && field.deletedByUserInd !== true) {
        field.fieldId = null
      }

      if (field.legalLandId < 0 && field.isNewFieldUI == true && field.deletedByUserInd !== true) {
        field.legalLandId = null
      }
    }
  }

  onDeleteInventory() {
    //Ask for confirmation before deleting all Inventory data
    if ( confirm("You are about to delete all inventory data for the policy. Do you want to continue?") ) {

      if (this.inventoryContract && this.policyId) {
        
        this.store.dispatch(DeleteInventoryContract(INVENTORY_COMPONENT_ID, 
                                this.inventoryContract.inventoryContractGuid, 
                                this.policyId, 
                                this.inventoryContract.etag))
      }
      
    }
  }


  commoditySelectionChanged(){
    this.selectedCommodity = this.getViewModel().formGroup.controls.selectedCommodity.value
  }

  onCheckForHiddenPlantingsInTotals() {

    this.isHiddenPlantingInTotals = false

    for (let field of  this.inventoryContract.fields) {
      for (let planting of field.plantings) {
        if (planting.isHiddenOnPrintoutInd && planting.inventoryBerries.plantedAcres > 0 ) {
          this.isHiddenPlantingInTotals = true
        }
      }
    }
  }

  onPrint() {

    let reportName = replaceNonAlphanumericCharacters(this.growerContract.growerName) + "-Inventory" 
    this.store.dispatch(GetInventoryReport(reportName, this.policyId, "", INSURANCE_PLAN.BERRIES.toString(), "", "", "", "", "", ""))

  }
  

  getCurrentFieldsAndCommodities() {
    let result = []
  
    for (let field of  this.inventoryContract.fields) {

      let cmdties = []

      for (let planting of field.plantings) {

        if (planting.inventoryBerries.cropCommodityId) {
          cmdties.push (planting.inventoryBerries.cropCommodityId)
        }
      }

      result.push ({
        fieldId: field.fieldId,
        commodities: cmdties
      })
    }
    return result
  }

  // BEGIN LAND MANAGEMENT

  pltgsWithSelectedCommodityExist(pltgs) {
    // are there any existing plantings for the selected commodity 
    if (!pltgs || pltgs.length == 0 || !pltgs.inventoryBerries) {
      return false
    } 

    let el = pltgs.find(x => x.inventoryBerries.cropCommodityId == this.selectedCommodity)

    if (el && el.length > 0) {
      return true
    } else {
      return false
    }
  }

  getMaxPlantingNumber(pltgs){
    let maxNum = 0

    pltgs.forEach((pltg: InventoryField) => {
      if (pltg.plantingNumber > maxNum ) {
        maxNum = pltg.plantingNumber
      }
    })

    return maxNum
  }

  getPlantings(landData) {
    let pltgs: Array<InventoryField> = []

    if ( landData.plantings && landData.plantings.length > 0 ) {
      for ( let i=0; i < landData.plantings.length; i++) {
        if ( landData.plantings[i].inventoryBerries && !landData.plantings[i].inventoryBerries.cropCommodityId) {
          pltgs.push(landData.plantings[i]) // get the existing plantings with non-null commodities
        }
      }
    }
  
    // - if no plantings with the selected commodity exist then add one, so it's visible on the screen
    if ( !landData.plantings || landData.plantings.length == 0 || !this.pltgsWithSelectedCommodityExist(landData.plantings)) {

      let inventoryBerries: InventoryBerries = getDefaultInventoryBerries(null, null, this.selectedCommodity)

      pltgs.push( getDefaultPlanting(null, INSURANCE_PLAN.BERRIES, (landData.fieldId > 0 ? landData.fieldId : null),  
                this.inventoryContract.cropYear, this.getMaxPlantingNumber(landData.plantings) + 1, inventoryBerries, [], []))

    } 

    return pltgs
  }

  populateNewLand(landData) {

    // find the max display_order
    let maxDisplayOrder = 0; // field order should start with 1
    let minFieldId = 0; // new fields should have negative field ids

    this.inventoryContract.fields.forEach( function(fld : AnnualFieldRsrc) {
  
      if (fld.displayOrder > maxDisplayOrder) {
        maxDisplayOrder = fld.displayOrder
      }

      if (fld.fieldId < minFieldId) {
        minFieldId = fld.fieldId
      }
    });

    
    const fld = this.inventoryContract.fields.find (field => field.fieldId == landData.fieldId)

    if (fld) {
      // if the field is already on the policy then
      // remove empty plantings, they aren't visible anyway
      if (fld.plantings) {
        for (let i = 0; i < fld.plantings.length; i++){
          if (fld.plantings[i].inventoryBerries && fld.plantings[i].inventoryBerries.cropCommodityId == null ) {
            fld.plantings.splice (i, 1)
            i--
          }
        }
      }

      // add a planting with the selected commodity
      let inventoryBerries: InventoryBerries = getDefaultInventoryBerries(null, null, this.selectedCommodity)

      fld.plantings.push(getDefaultPlanting(null, INSURANCE_PLAN.BERRIES, fld.fieldId,  
        this.inventoryContract.cropYear, this.getMaxPlantingNumber(fld.plantings) + 1, inventoryBerries, [], []))

    } else {

      // get the plantings for the added field from landdata and create a new planting with the selected commodity
      let pltgs: Array<InventoryField> = this.getPlantings(landData)

      this.inventoryContract.fields.push( createNewAnnualFieldObject( (landData.fieldId > -1 ? landData.fieldId : (minFieldId - 1)), 
                                    (landData.legalLandId > -1 ? landData.legalLandId : null), 
                                    landData.fieldLabel, landData.otherLegalDescription, landData.primaryPropertyIdentifier,
                                    landData.fieldLocation, maxDisplayOrder + 1, this.inventoryContract.cropYear, false, 
                                    landData.landUpdateType, landData.transferFromGrowerContractYearId, 
                                    pltgs, landData.uwComments) )
    }

    this.numComponentReloads = this.numComponentReloads + 1 // to reload the field component

    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
  }

  onAddNewField() {

    if (this.inventoryContract && this.inventoryContract.fields) {

      // for now I am only setting the variables that I need for Berries Add Field
      const dataToSend : AddLandPopupData = {
        fieldId: null,  
        fieldLabel: null,
        cropYear: this.inventoryContract.cropYear,
        policyId: this.policyId,
        insurancePlanId: this.inventoryContract.insurancePlanId,
        annualFieldDetailId: null,
        otherLegalDescription : null,
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
        },
        berries: {
          selectedCommodity: this.selectedCommodity,
          fields: this.getCurrentFieldsAndCommodities()
        }
      }

      // open up the popup
      let dialogRef = this.dialog.open(AddFieldComponent , {
          width: '800px',
          data: dataToSend
        });
      
      dialogRef.afterClosed().subscribe(result => {
        if (result && result.event == 'AddLand'){
          
          // add new land
          if (result.data && result.data.landData) {
            this.populateNewLand(result.data.landData) 
          }
          
        } else if (result && result.event == 'Cancel'){
          // do nothing
        }
      });
    }
  }

  // END LAND MANAGEMENT

  shouldHighlightCommodity() {
    if (this.selectedCommodity) {
      this.selectedCommodityTooltip = "Select a commodity"
      return false // if a commodity is selected 
    } else {
      this.selectedCommodityTooltip = "Select a commodity then add field(s) and save"
      return true
    }
  }

  showQuantityTotals() {
    if (this.inventoryContract && this.inventoryContract.inventoryContractCommodityBerries.length > 0 ) {

      let el = this.inventoryContract.inventoryContractCommodityBerries.find(x => x.cropCommodityId == this.selectedCommodity)
      if (el) {
        return true
      }
    }

    return false
  }

  showPlantTotals() {
    if ((this.selectedCommodity == BERRY_COMMODITY.Blueberry || this.selectedCommodity == BERRY_COMMODITY.Strawberry) &&
        this.inventoryContract && this.inventoryContract.inventoryContractCommodityBerries.length > 0) {

      let el = this.inventoryContract.inventoryContractCommodityBerries.find(x => x.cropCommodityId == this.selectedCommodity)
      if (el) {
        return true
      }
    }

    return false
  }
}
