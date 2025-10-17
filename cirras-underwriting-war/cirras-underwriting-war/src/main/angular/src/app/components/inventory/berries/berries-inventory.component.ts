import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { ParamMap } from '@angular/router';
import { BaseComponent } from '../../common/base/base.component';
import { CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { BerriesInventoryComponentModel } from './berries-inventory.component.model';
import { CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';
import { AddNewInventoryContract, DeleteInventoryContract, LoadInventoryContract, RolloverInventoryContract, UpdateInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { isInt } from 'src/app/utils';

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

  // TODO: defaultCommodity should be based on the commodity tab that the user chooses
  defaultCommodity = 10 // Blueberry is the default commodity for now

  policyId
  cropCommodityOptions = [];
  cropVarietyOptions = [];

  hasYieldData = false; // TODO

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
      this.getViewModel().formGroup.controls.defaultCommodity.setValue(10) // Blueberries
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
        let plantedYear = planting.inventoryBerries.plantedYear
        let plantedAcres = planting.inventoryBerries.plantedAcres
        let variety = planting.inventoryBerries.cropVarietyId

        let rowSpacing = planting.inventoryBerries.rowSpacing
        let plantSpacing = planting.inventoryBerries.plantSpacing
        let isQuantityInsurableInd = planting.inventoryBerries.isQuantityInsurableInd
        let isPlantInsurableInd = planting.inventoryBerries.isPlantInsurableInd

        // saving max 1 empty planting is allowed
        let totalPlantingsToSave = (field.plantings.filter(x => x.inventoryBerries.deletedByUserInd !== true )) //.length 
        if ( totalPlantingsToSave.length > 1 && planting.inventoryBerries.deletedByUserInd !== true && 
              !plantedYear && !plantedAcres && !variety && !rowSpacing && !plantSpacing ) {

                alert("There is an empty planting for field ID: " + field.fieldId + ". Please delete it.")
                return false
              }

        // All user entered fields are mandatory: if at least one field has a value or one of the checkboxes is checked then all should have a value
        let message = "Partial data entry is not accepted. Please fill in all values for field ID " + field.fieldId + " or none of them."
        if (plantedYear && (!plantedAcres || !variety || !rowSpacing || !plantSpacing ) ) {
          alert(message)
          return false
        }

        if (!plantedYear && (plantedAcres || variety || rowSpacing || plantSpacing ) ) {
          alert(message)
          return false
        }

        if ( (isQuantityInsurableInd || isPlantInsurableInd) && (!plantedAcres || !plantedYear || !variety || !rowSpacing || !plantSpacing ) ) {
          alert(message)
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
    
    return true // all checks have passed successfully
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
    this.defaultCommodity = this.getViewModel().formGroup.controls.defaultCommodity.value
    console.log( "defaultCommodity: " + this.defaultCommodity)
  }
}
