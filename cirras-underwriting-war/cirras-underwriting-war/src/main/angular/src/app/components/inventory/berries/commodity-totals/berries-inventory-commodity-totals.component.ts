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
  @Input() insurabilityType : string;
  @Input() inventoryContractCommodityBerries : InventoryContractCommodityBerries;
  @Input() selectedCommodity;

  BERRY_COMMODITY = BERRY_COMMODITY

  calcTotals() {
    if ( this.inventoryContractCommodityBerries && this.insurabilityType == 'quantity') {
      return (this.inventoryContractCommodityBerries.totalQuantityInsuredAcres + this.inventoryContractCommodityBerries.totalQuantityUninsuredAcres).toFixed(2)
    }

    if ( this.inventoryContractCommodityBerries && this.insurabilityType == 'plant') {
      if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
        return (this.inventoryContractCommodityBerries.totalPlantInsuredAcres + this.inventoryContractCommodityBerries.totalPlantUninsuredAcres).toFixed(2)
      } else {
        return (this.inventoryContractCommodityBerries.totalInsuredPlants + this.inventoryContractCommodityBerries.totalUninsuredPlants)
      }
    }
  }

  getUninsuredPlants() {
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry) {
      return this.inventoryContractCommodityBerries.totalUninsuredPlants
    }
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      return this.inventoryContractCommodityBerries.totalPlantUninsuredAcres
    } 
    return ""
  }

  getInsuredPlants() {
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry) {
      return this.inventoryContractCommodityBerries.totalInsuredPlants
    }
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      return this.inventoryContractCommodityBerries.totalPlantInsuredAcres
    } 
    return ""
  }

  getTitle(type_column) {

    // type_column: insured or uninsured
    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {

      if ( this.insurabilityType == 'quantity') {
        return ("Total Quantity " + type_column + " acres" )
      } else {
        return ("Total Plant " + type_column + " acres" )
      }
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry || this.selectedCommodity == BERRY_COMMODITY.Raspberry) {
      if ( this.insurabilityType == 'quantity') {
        return ("Total Quantity " + type_column + " acres" )
      } else {
        return ("Total " + type_column + " plants" )
      }
    } 
  }

}
