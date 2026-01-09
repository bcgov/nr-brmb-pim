import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { AnnualField } from 'src/app/conversion/models';

@Component({
  selector: 'berries-dop-field',
  templateUrl: './field.component.html',
  styleUrl: './field.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesDopFieldComponent {
  @Input() field: AnnualField;
  @Input() fieldsFormArray: UntypedFormArray;


  onInventoryCommentsDone(uwComments: UnderwritingComment[]) {
    // TODO
    // this.field.uwComments = uwComments;
    // this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
  }


  setTableHeaderStyle() {
    return {
      'width': `1520px`
    };
  }

  setPlantingStyles() {
    return {
        'display': 'grid',
        'align-items': 'stretch',
        'width': `830px`
    };
  }
  

}
