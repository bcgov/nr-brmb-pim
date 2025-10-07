import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { CropCommodityList, CropVarietyCommodityType, InventoryContract, UwContract } from 'src/app/conversion/models';
import { BerriesInventoryComponentModel } from './berries-inventory.component.model';
import { CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';

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

  cropVarietyOptions = [];

  hasDataChanged = false;

  initModels() {
    this.viewModel = new BerriesInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
  }

  loadPage() {
      // TODO
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
  
}
