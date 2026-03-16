import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { roundUpDecimal } from 'src/app/components/inventory/inventory-common';
import { DopYieldFieldCommodityBerries } from 'src/app/conversion/models-yield';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { makeNumberOnly } from 'src/app/utils';
import { BERRY_COMMODITY } from 'src/app/utils/constants';

@Component({
  selector: 'berries-dop-commodity-list',
  templateUrl: './commodity-list.component.html',
  styleUrl: './commodity-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})

export class BerriesDopCommodityListComponent {
  @Input() dopYieldFieldCommodityBerries: DopYieldFieldCommodityBerries
  @Input() dopYieldFieldCommodityBerriesFormArray: UntypedFormArray
  @Input() filterByCropCommodityId: number;


  fieldCommodityFormGroup: UntypedFormGroup;
  
  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
  ) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.dopYieldFieldCommodityBerries && changes.dopYieldFieldCommodityBerries.currentValue) ) {
      if (this.dopYieldFieldCommodityBerries) {
        this.refreshForm() 
      }
    }
  }

  refreshForm(){
    
    this.fieldCommodityFormGroup = this.fb.group({
      totalProductionOverride: [ this.dopYieldFieldCommodityBerries.totalProductionOverride ], 
      dopYieldFieldVarietyBerriesList: this.fb.array([])
    });
    this.dopYieldFieldCommodityBerriesFormArray.push(this.fieldCommodityFormGroup);
  }

  isSelectedCommodity() {
    if (this.dopYieldFieldCommodityBerries.cropCommodityId == this.filterByCropCommodityId) {
      return true
    } else {
      return false
    }
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  updatetotalProductionOverride() {
    const totalProductionOverride = this.fieldCommodityFormGroup.value.totalProductionOverride
    const roundUpTotalProductionOverride = roundUpDecimal(totalProductionOverride, 2)
    
    this.fieldCommodityFormGroup.controls['totalProductionOverride'].setValue(roundUpTotalProductionOverride) 
    this.dopYieldFieldCommodityBerries.totalProductionOverride = this.fieldCommodityFormGroup.value.totalProductionOverride
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }
  
  setTableHeaderStyle() {
    if (this.isStrawberry()) {
      return {
        'width': `1020px`
      };
    } else {
      return {
        'width': `1120px`
      };
    }  
  }

  // setCommodityTotalStyle() {
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

  isStrawberry() {
    if (this.filterByCropCommodityId == BERRY_COMMODITY.Strawberry ) {
      return true
    } else {
      return false
    }
  }

}
