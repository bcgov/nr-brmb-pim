import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { InventoryField } from '@cirras/cirras-underwriting-api';
import { addSimplePlantingObject } from '../../inventory-common';


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
  @Output() recalcNumPlantings = new EventEmitter();

  plantingFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder ) {}

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

  plantingHasCommodity() {
    // TODO - remove the check for empty commodity after add field is done
    if (this.planting && 
        ( this.planting.inventoryBerries.cropCommodityId == this.selectedCommodity || this.planting.inventoryBerries.cropCommodityId == null )) {
      return true
    } else {
      return false
    }
  }

}
