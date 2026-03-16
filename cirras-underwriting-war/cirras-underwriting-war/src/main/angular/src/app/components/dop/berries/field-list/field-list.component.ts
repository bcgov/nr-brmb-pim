import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { roundUpDecimal } from 'src/app/components/inventory/inventory-common';
import { AnnualField } from 'src/app/conversion/models';
import { DopYieldContractCommodityBerries } from 'src/app/conversion/models-yield';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { makeNumberOnly } from 'src/app/utils';
import { BERRY_COMMODITY } from 'src/app/utils/constants';

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

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
  ) {}

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
    const totalProductionOverride = this.fieldsFormGroup.value.totalProductionOverride
    const roundUpTotalProductionOverride = roundUpDecimal(totalProductionOverride, 2)
    
    this.fieldsFormGroup.controls['totalProductionOverride'].setValue(roundUpTotalProductionOverride) 
    this.contractCommodityTotals.totalProductionOverride = this.fieldsFormGroup.value.totalProductionOverride
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  setTableHeaderStyle() {
    if (this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
      return {
        'width': `1695px`
      };
    } else {
      return {
        'width': `1795px`
      };
    }  
  }
  
  setInnerWrapperStyle() {
    if (this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
      return {
        'width': `1720px`
      };
    } else {
      return {
        'width': `1820px`
      };
    }  
  }

  isStrawberry() {
    if(this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
      return true
    } else {
      return false
    }
  }

  setFieldHeaderStyle() {
    if (this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
      return {
        'width': `1710px`
      };
    } else {
      return {
        'width': `1810px`
      };
    }  
  }

  // setFieldTotalStyle() {
  //   if (this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
  //     return {
  //       'width': `460px`
  //     };
  //   } else {
  //     return {
  //       'width': `460px`
  //     };
  //   }  
  // }

}
