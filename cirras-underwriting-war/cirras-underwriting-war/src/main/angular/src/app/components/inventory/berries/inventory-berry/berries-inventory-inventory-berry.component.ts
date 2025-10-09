import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { InventoryBerries } from '@cirras/cirras-underwriting-api';
import { makeNumberOnly, makeTitleCase } from 'src/app/utils';
import { addBerriesObject, CropVarietyOptionsType, roundUpDecimal } from '../../inventory-common';

@Component({
  selector: 'berries-inventory-inventory-berry',
  templateUrl: './berries-inventory-inventory-berry.component.html',
  styleUrl: './berries-inventory-inventory-berry.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})

export class BerriesInventoryInventoryBerryComponent implements OnChanges{
  @Input() inventoryBerry: InventoryBerries;
  @Input() cropVarietyOptions;

  defaultCommodity = 10 // Blueberry is the default commodity for now
  inventoryBerriesFormGroup: UntypedFormGroup;

  filteredVarietyOptions: CropVarietyOptionsType[];  

  constructor(private fb: UntypedFormBuilder) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.planting && changes.planting.currentValue) {
      if (this.inventoryBerry) {
        this.refreshForm()
      }
    }
  }

  refreshForm() {
    this.inventoryBerriesFormGroup = this.fb.group(
      addBerriesObject(( this.inventoryBerry && this.inventoryBerry.inventoryFieldGuid ? this.inventoryBerry.inventoryFieldGuid : null), false, this.inventoryBerry ) 
    )
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  getBerryCommodity() {
    return (this.inventoryBerry && this.inventoryBerry.cropCommodityId) ? this.inventoryBerry.cropCommodityId : this.defaultCommodity
  }

  varietyFocus() {
    // prepare a list of varieties for a specific commodity
    const cmdty = this.getBerryCommodity()
    
    this.filteredVarietyOptions = (this.cropVarietyOptions.filter (x => x.cropCommodityId == cmdty))
  }

  // crop variety search
  searchVariety(value) {
  
    const varietyName = (( typeof value === 'string') ? value : value?.varietyName)
    const cmdty = this.getBerryCommodity()

    if (varietyName) {

      const filterValue = varietyName.toLowerCase()
      this.filteredVarietyOptions = this.cropVarietyOptions.filter(option => (option.varietyName.toLowerCase().includes(filterValue) && option.cropCommodityId == cmdty))
  
    } else {

      this.filteredVarietyOptions = (this.cropVarietyOptions.filter (x => x.cropCommodityId == cmdty))
    }
  }

  displayVarietyFn(vrty: CropVarietyOptionsType): string {
    return vrty && vrty.varietyName ? makeTitleCase( vrty.varietyName)  : '';
  }

  roundUpAcres() {
    const plantedAcres = this.inventoryBerriesFormGroup.value.plantedAcres
    const roundUpAcres = roundUpDecimal(plantedAcres, 2)
    this.inventoryBerriesFormGroup.controls['plantedAcres'].setValue(roundUpAcres) 
  }


}
