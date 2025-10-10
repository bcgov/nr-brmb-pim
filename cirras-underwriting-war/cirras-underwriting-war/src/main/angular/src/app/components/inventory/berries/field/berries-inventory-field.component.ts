import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { AnnualField } from 'src/app/conversion/models';
import { addAnnualFieldObject } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';

@Component({
  selector: 'berries-inventory-field',
  templateUrl: './berries-inventory-field.component.html',
  styleUrl: './berries-inventory-field.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldComponent implements OnChanges{

  @Input() field: AnnualField;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;

  fieldFormGroup: UntypedFormGroup;
  
  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState> ) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.field && changes.field.currentValue) {
      if (this.field) {
        this.refreshForm()
      }
    }
  }

  refreshForm(){
    this.fieldFormGroup = this.fb.group(
      addAnnualFieldObject(this.field, this.fb.array([]), this.field.uwComments)
    );
    this.fieldsFormArray.push(this.fieldFormGroup);
  }


  setPlantingStyles() {
    return {
        'display': 'grid',
        'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
        'align-items': 'stretch',
        'width': `740px`
    };
  }

  updateFieldLocation() {
    this.field.fieldLocation = this.fieldFormGroup.value.fieldLocation
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateIsLeasedInd() {
    this.field.isLeasedInd = this.fieldFormGroup.value.isLeasedInd
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

}
