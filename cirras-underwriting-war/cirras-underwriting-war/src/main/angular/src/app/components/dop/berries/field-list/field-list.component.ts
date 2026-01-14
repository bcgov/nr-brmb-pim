import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';
import { DopYieldContractCommodityBerries } from 'src/app/conversion/models-yield';
import { makeNumberOnly } from 'src/app/utils';

@Component({
  selector: 'berries-dop-field-list',
  templateUrl: './field-list.component.html',
  styleUrl: './field-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesDopFieldListComponent {
  @Input() fields: Array<AnnualField>;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() filterByCropCommodityId: number;
  @Input() filteredCropCommodityName: string;
  @Input() contractCommodityTotals: DopYieldContractCommodityBerries

  fieldsFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.fields && changes.fields.currentValue) ) {
      if (this.fields) {
        this.refreshForm() 
      }
    }
  }

  refreshForm(){
    // hmm... may need to change on Save - untroducing another subform 
    this.fieldsFormGroup = this.fb.group({
      totalProductionOverride: [ (this.contractCommodityTotals && this.contractCommodityTotals.totalProductionOverride) ? this.contractCommodityTotals.totalProductionOverride : "" ], 
      fields: this.fb.array([])
    });
    this.fieldsFormArray.push(this.fieldsFormGroup);
  }



  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }
  
  updatetotalProductionOverride() {
    // TODO on save: figure out the correct object and value to update
  }

}
