import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { InventoryBerries } from '@cirras/cirras-underwriting-api';
import { makeNumberOnly, makeTitleCase } from 'src/app/utils';
import { addBerriesObject, CropVarietyOptionsType, roundUpDecimal } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';

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

  constructor(private fb: UntypedFormBuilder,
                private store: Store<RootState> ) {}

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
    // TODO: inventoryFieldGuid should be passed as Input value for the new plantings

    // make IsQuantityInsurableInd and IsPlantInsurableCheckbox checked by default, as it's in the form
    this.inventoryBerry.isQuantityInsurableInd = this.inventoryBerriesFormGroup.value.isQuantityInsurableInd
    this.inventoryBerry.isPlantInsurableInd = this.inventoryBerriesFormGroup.value.isPlantInsurableInd

    // set crop commodity id to Blueberries by default // TODO: this will have to change after we introduce Rasberries and Strawberries
    if (!this.inventoryBerry.cropCommodityId) {
      this.inventoryBerry.cropCommodityId = this.defaultCommodity
    }

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

  updatePlantedYear() {
    this.inventoryBerry.plantedYear = this.inventoryBerriesFormGroup.value.plantedYear
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updatePlantedAcres() {
    const plantedAcres = this.inventoryBerriesFormGroup.value.plantedAcres
    const roundUpAcres = roundUpDecimal(plantedAcres, 2)

    this.inventoryBerriesFormGroup.controls['plantedAcres'].setValue(roundUpAcres) 
    this.inventoryBerry.plantedAcres = this.inventoryBerriesFormGroup.value.plantedAcres
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateIsQuantityInsurableInd() {
    this.inventoryBerry.isQuantityInsurableInd = this.inventoryBerriesFormGroup.value.isQuantityInsurableInd
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateCropVariety() {
    this.inventoryBerry.cropVarietyId = this.inventoryBerriesFormGroup.controls['cropVarietyCtrl'].value.cropVarietyId
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateRowSpacing() {
    this.inventoryBerry.rowSpacing = this.inventoryBerriesFormGroup.value.rowSpacing
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updatePlantSpacing() {
    const plantSpacing = this.inventoryBerriesFormGroup.value.plantSpacing
    const roundUpPlantSpacing = roundUpDecimal(plantSpacing, 1)

    this.inventoryBerriesFormGroup.controls['plantSpacing'].setValue(roundUpPlantSpacing) 
    this.inventoryBerry.plantSpacing = this.inventoryBerriesFormGroup.value.plantSpacing
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateIsPlantInsurableInd() {
    this.inventoryBerry.isPlantInsurableInd = this.inventoryBerriesFormGroup.value.isPlantInsurableInd
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }
}
