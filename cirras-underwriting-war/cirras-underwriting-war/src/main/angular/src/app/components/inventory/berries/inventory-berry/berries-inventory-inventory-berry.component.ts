import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { InventoryBerries } from '@cirras/cirras-underwriting-api';
import { makeNumberOnly, makeTitleCase } from 'src/app/utils';
import { addBerriesObject, CropVarietyOptionsType, roundUpDecimal } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { BERRY_COMMODITY, CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';
import { showIsPlantInsuredForBerries, showPlantSpacingForBerries, showRowSpacingForBerries, showTotalPlantsForBerries } from '../field-list/berries-inventory-field-list.component';

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
  @Input() selectedCommodity;
  @Input() numPlantingsToSave;
  @Output() recalcNumPlantings = new EventEmitter();

  inventoryBerriesFormGroup: UntypedFormGroup;

  filteredVarietyOptions: CropVarietyOptionsType[];  

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
              protected securityUtilService: SecurityUtilService) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.planting && changes.planting.currentValue) {
      if (this.inventoryBerry) {
        this.refreshForm()
      }
    }

    if (changes.selectedCommodity && changes.selectedCommodity.currentValue && this.inventoryBerry && this.inventoryBerriesFormGroup && this.inventoryBerriesFormGroup.controls) {
      this.setDefaultValuesForCommodity()
    }
  }

  refreshForm() {
    this.inventoryBerriesFormGroup = this.fb.group(
      addBerriesObject(
        ( this.inventoryBerry && this.inventoryBerry.inventoryFieldGuid ? this.inventoryBerry.inventoryFieldGuid : null), 
        this.inventoryBerry ) 
    )
    this.setDefaultValuesForCommodity()
  }

  setDefaultValuesForCommodity() {
    // set commodity specific default values
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry || this.selectedCommodity == BERRY_COMMODITY.Raspberry) {
      // make IsQuantityInsurableInd checked by default
      if (this.inventoryBerry.isQuantityInsurableInd == null) {
        this.inventoryBerry.isQuantityInsurableInd = true
        this.inventoryBerriesFormGroup.controls['isQuantityInsurableInd'].setValue(true)
      }
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry) {
      // make IsPlantInsurableCheckbox checked by default
      if (this.inventoryBerry.isPlantInsurableInd == null) {
        this.inventoryBerry.isPlantInsurableInd = true
        this.inventoryBerriesFormGroup.controls['isPlantInsurableInd'].setValue(true)
      }
    } else { 
      if (this.inventoryBerry.isPlantInsurableInd == null) {
        this.inventoryBerry.isPlantInsurableInd = false
        this.inventoryBerriesFormGroup.controls['isPlantInsurableInd'].setValue(false)
      }
    }
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  getBerryCommodity() {
    return (this.inventoryBerry && this.inventoryBerry.cropCommodityId) ? this.inventoryBerry.cropCommodityId : this.selectedCommodity
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
    this.inventoryBerry.cropCommodityId = this.inventoryBerriesFormGroup.controls['cropVarietyCtrl'].value.cropCommodityId
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

  onDeletePlanting() {

    if (this.numPlantingsToSave < 2 )  {
      // clear the values
      this.inventoryBerry.plantedYear = null
      this.inventoryBerry.plantedAcres = null
      this.inventoryBerry.isQuantityInsurableInd = false
      this.inventoryBerry.cropVarietyId = null
      this.inventoryBerry.rowSpacing = null
      this.inventoryBerry.plantSpacing = null
      this.inventoryBerry.isPlantInsurableInd = false
      this.inventoryBerry.totalPlants = null
      
      this.inventoryBerriesFormGroup.controls['plantedYear'].setValue(null)
      this.inventoryBerriesFormGroup.controls['plantedAcres'].setValue(null)
      this.inventoryBerriesFormGroup.controls['isQuantityInsurableInd'].setValue(false)
      this.inventoryBerriesFormGroup.controls['cropVarietyCtrl'].setValue({      
              cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
              cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
              varietyName: CROP_COMMODITY_UNSPECIFIED.NAME   
            })
      this.inventoryBerriesFormGroup.controls['rowSpacing'].setValue(null)
      this.inventoryBerriesFormGroup.controls['plantSpacing'].setValue(null)
      this.inventoryBerriesFormGroup.controls['isPlantInsurableInd'].setValue(false)

    } else {
      // mark for deletion
      this.inventoryBerry.deletedByUserInd = true
    }
    
    this.recalcNumPlantings.emit() // emit an event to make the parent component recalc the numPlantingsToSave
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }


  showRowSpacing() {
    return showRowSpacingForBerries(this.selectedCommodity)
  }

  showPlantSpacing() {
    return showPlantSpacingForBerries(this.selectedCommodity)
  }

  showTotalPlants() {
    return showTotalPlantsForBerries(this.selectedCommodity)
  }

  showIsPlantInsured() {
    return showIsPlantInsuredForBerries(this.selectedCommodity)
  }

}
