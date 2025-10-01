import { ChangeDetectionStrategy, Component, Input, OnChanges, ViewEncapsulation } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { BerriesInventoryComponentModel } from './berries-inventory.component.model';

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

  setFormSeededStyles(){
    return {
      'grid-template-columns':  'auto 140px 150px 12px 190px'
    }
  }
  
}
