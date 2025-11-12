import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { InventoryField } from '@cirras/cirras-underwriting-api';
import { addSimplePlantingObject } from '../../inventory-common';
import { BERRY_COMMODITY } from 'src/app/utils/constants';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';


@Component({
  selector: 'berries-inventory-planting',
  templateUrl: './berries-inventory-planting.component.html',
  styleUrl: './berries-inventory-planting.component.scss',
  standalone: false,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BerriesInventoryPlantingComponent implements OnChanges { 

  @Input() planting: InventoryField;
  @Input() plantingFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() selectedCommodity;
  @Input() numPlantingsToSave;
  @Input() isFieldHiddenOnPrintout;
  @Output() recalcNumPlantings = new EventEmitter();
  @Output() hidePlantingOnPrintout = new EventEmitter();

  plantingFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.planting && changes.planting.currentValue) {
      if (this.planting) {
        this.refreshForm()
      }
    }
  }

  refreshForm() {

    // add planting subform
    this.plantingFormGroup = this.fb.group( 
      addSimplePlantingObject( this.planting, false, 
        this.fb.array([]), this.fb.array([]), this.fb.array([]) , this.fb.array([])) 
    )

    this.plantingFormArray.push(this.plantingFormGroup);
  }

  onNumPlantingsChanged() {
    // emit an event to make the parent component recalc the numPlantingsToSave
    this.recalcNumPlantings.emit(); 
  }

  setWidth() {
    
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry ) {
      return {
          'width': `790px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Raspberry ) {
      return {
          'width': `470px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry ) {
      return {
          'width': `610px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Cranberry ) {
      return {
          'width': `770px`
      };
    }

  }

  plantingHasCommodity() {
    // TODO - remove the check for empty commodity after add field is done
    if (this.planting && 
        ( this.planting.inventoryBerries.cropCommodityId == this.selectedCommodity || this.planting.inventoryBerries.cropCommodityId == null )) {
      return true
    } else {
      return false
    }
  }

  toggleHiddenOnPrintout() {
    this.planting.isHiddenOnPrintoutInd = !this.planting.isHiddenOnPrintoutInd

    // emit an event to make the parent component check if all plantings are hidden on printout
    this.hidePlantingOnPrintout.emit(); 
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }
}
