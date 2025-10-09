import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { ParamMap } from '@angular/router';
import { BaseComponent } from '../../common/base/base.component';
import { CropCommodityList, CropVarietyCommodityType, InventoryContract, UwContract } from 'src/app/conversion/models';
import { BerriesInventoryComponentModel } from './berries-inventory.component.model';
import { CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';
import { AddNewInventoryContract, LoadInventoryContract, RolloverInventoryContract, UpdateInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';

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

  policyId
  cropVarietyOptions = [];

  hasDataChanged = false;

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
      this.populateCropVarieryOptions()
    }
  }

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


  setFormSeededStyles(){
    return {
      'grid-template-columns':  'auto 140px 150px 12px 190px'
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
      this.hasDataChanged = false
      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }
  
  onSave() {
  
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

  isFormValid() {
    //TODO
    return true
  }

  // TODO add onblur functions for each form control

}
