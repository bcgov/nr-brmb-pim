import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { roundUpDecimal } from 'src/app/components/inventory/inventory-common';
import { DopYieldFieldVarietyBerries } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { makeNumberOnly } from 'src/app/utils';
import { BERRY_COMMODITY } from 'src/app/utils/constants';

@Component({
  selector: 'berries-dop-variety-list',
  templateUrl: './variety-list.component.html',
  styleUrl: './variety-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesDopVarietyListComponent {
  @Input() dopYieldFieldVarietyBerries: DopYieldFieldVarietyBerries
  @Input() dopYieldFieldVarietyBerriesFormArray: UntypedFormArray
  @Input() cropCommodityName: String

  varietyFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
              public securityUtilService: SecurityUtilService,  
  ) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.dopYieldFieldVarietyBerries && changes.dopYieldFieldVarietyBerries.currentValue) ) {
      if (this.dopYieldFieldVarietyBerries) {
        this.refreshForm() 
      }
    }
  }

  refreshForm(){
    this.varietyFormGroup = this.fb.group({
      soldShippedYield: [ this.dopYieldFieldVarietyBerries.soldShippedYield],
      salesYield: [ this.dopYieldFieldVarietyBerries.salesYield],
      abandonmentYield: [ this.dopYieldFieldVarietyBerries.abandonmentYield],
      totalProductionOverride: [ this.dopYieldFieldVarietyBerries.totalProductionOverride ],
    });
    this.dopYieldFieldVarietyBerriesFormArray.push(this.varietyFormGroup);
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  updateSoldShippedYield() {
    const soldShippedYield = this.varietyFormGroup.value.soldShippedYield
    const roundUpSoldShippedYield = roundUpDecimal(soldShippedYield, 2)
    
    this.varietyFormGroup.controls['soldShippedYield'].setValue(roundUpSoldShippedYield) 
    this.dopYieldFieldVarietyBerries.soldShippedYield = this.varietyFormGroup.value.soldShippedYield
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  updateSalesYield() {
    const salesYield = this.varietyFormGroup.value.salesYield
    const roundUpSalesYield = roundUpDecimal(salesYield, 2)
    
    this.varietyFormGroup.controls['salesYield'].setValue(roundUpSalesYield) 
    this.dopYieldFieldVarietyBerries.salesYield = this.varietyFormGroup.value.salesYield
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  updateAbandonmentYield() {
    const abandonmentYield = this.varietyFormGroup.value.abandonmentYield
    const roundUpAbandonmentYield = roundUpDecimal(abandonmentYield, 2)
    
    this.varietyFormGroup.controls['abandonmentYield'].setValue(roundUpAbandonmentYield) 
    this.dopYieldFieldVarietyBerries.abandonmentYield = this.varietyFormGroup.value.abandonmentYield
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  updateTotalProductionOverride() {
    const totalProductionOverride = this.varietyFormGroup.value.totalProductionOverride
    const roundUpTotalProductionOverride = roundUpDecimal(totalProductionOverride, 2)
        
    this.varietyFormGroup.controls['totalProductionOverride'].setValue(roundUpTotalProductionOverride) 
    this.dopYieldFieldVarietyBerries.totalProductionOverride = this.varietyFormGroup.value.totalProductionOverride
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  onDeleteRowValues() {
    this.dopYieldFieldVarietyBerries.soldShippedYield = null
    this.dopYieldFieldVarietyBerries.salesYield = null
    this.dopYieldFieldVarietyBerries.abandonmentYield = null
    this.dopYieldFieldVarietyBerries.totalProductionOverride = null

    this.varietyFormGroup.controls['soldShippedYield'].setValue(null)
    this.varietyFormGroup.controls['salesYield'].setValue(null)
    this.varietyFormGroup.controls['abandonmentYield'].setValue(null)
    this.varietyFormGroup.controls['totalProductionOverride'].setValue(null)

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true))
  }

  setFieldVarietyStyle() {
    if (this.isStrawberry() ) { // strawberry doesn't have ME Acres
      return {
        'width': `1020px`
      };
    } else {
      return {
        'width': `1120px`
      };
    }  
  }

  isStrawberry() {
    if(this.cropCommodityName == BERRY_COMMODITY[13].toUpperCase() ) {
      return true
    } else {
      return false
    }
  }

}
