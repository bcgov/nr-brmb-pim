import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from '@angular/core';
import { InventoryContractCommodityBerries } from '@cirras/cirras-underwriting-api';
import { BERRY_COMMODITY } from 'src/app/utils/constants';

@Component({
  selector: 'berries-inventory-commodity-totals',
  templateUrl: './berries-inventory-commodity-totals.component.html',
  styleUrl: './berries-inventory-commodity-totals.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesInventoryCommodityListComponent {
  @Input() type_total : string;
  @Input() inventoryContractCommodityBerries : InventoryContractCommodityBerries;
  @Input() selectedCommodity;

  BERRY_COMMODITY = BERRY_COMMODITY

  calcTotals() {
    if ( this.inventoryContractCommodityBerries && this.type_total == 'acres') {
      if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
        return (this.inventoryContractCommodityBerries.totalPlantInsuredAcres + this.inventoryContractCommodityBerries.totalPlantUninsuredAcres).toFixed(2)
      } else {
        return (this.inventoryContractCommodityBerries.totalQuantityInsuredAcres + this.inventoryContractCommodityBerries.totalQuantityUninsuredAcres).toFixed(2)
      }
    }

    if ( this.inventoryContractCommodityBerries && this.type_total == 'plants') {
      return (this.inventoryContractCommodityBerries.totalInsuredPlants + this.inventoryContractCommodityBerries.totalUninsuredPlants)
    }
  }

  getUninsuredAcres() {
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      return this.inventoryContractCommodityBerries.totalPlantUninsuredAcres
    } else {
      return this.inventoryContractCommodityBerries.totalQuantityUninsuredAcres
    }
  }

  getInsuredAcres() {
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      return this.inventoryContractCommodityBerries.totalPlantInsuredAcres
    } else {
      return this.inventoryContractCommodityBerries.totalQuantityInsuredAcres
    }
  }

}
