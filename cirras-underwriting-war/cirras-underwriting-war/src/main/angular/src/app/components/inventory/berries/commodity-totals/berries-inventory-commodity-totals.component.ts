import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from '@angular/core';
import { InventoryContractCommodityBerries } from '@cirras/cirras-underwriting-api';

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

  calcTotals() {
    if ( this.inventoryContractCommodityBerries && this.type_total == 'acres') {
      return (this.inventoryContractCommodityBerries.totalQuantityInsuredAcres + this.inventoryContractCommodityBerries.totalQuantityUninsuredAcres).toFixed(2)
    }

    if ( this.inventoryContractCommodityBerries && this.type_total == 'plants') {
      return (this.inventoryContractCommodityBerries.totalInsuredPlants + this.inventoryContractCommodityBerries.totalUninsuredPlants)
    }
  }
}
