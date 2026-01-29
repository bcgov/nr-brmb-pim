import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
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
  @Input() filterByCropCommodityId: number;

  fieldFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.field && changes.field.currentValue) ) {
      if (this.field) {
        this.refreshForm() 
      }
    }
  }

  refreshForm(){
    this.fieldFormGroup = this.fb.group({
        dopYieldFieldCommodityBerriesList: this.fb.array([]),
    });
    this.fieldsFormArray.push(this.fieldFormGroup);
  }

  fieldHasCommodity() {
    let el = this.field.dopYieldFieldCommodityBerriesList.find(x => x.cropCommodityId == this.filterByCropCommodityId) 
    if ( el ) {
      return true
    } else {
      return false 
    }
    
  }
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
