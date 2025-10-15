import { ChangeDetectionStrategy, ChangeDetectorRef, Component, input, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { InventoryField } from '@cirras/cirras-underwriting-api';
import { addSimplePlantingObject, getDefaultInventoryBerries } from '../../inventory-common';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';


@Component({
  selector: 'berries-inventory-planting',
  imports: [],
  templateUrl: './berries-inventory-planting.component.html',
  styleUrl: './berries-inventory-planting.component.scss',
  standalone: false,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BerriesInventoryPlantingComponent implements OnChanges { 

  @Input() planting: InventoryField;
  @Input() plantingFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() defaultCommodity;
  @Input() numPlantingsToSave;
  
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

}
