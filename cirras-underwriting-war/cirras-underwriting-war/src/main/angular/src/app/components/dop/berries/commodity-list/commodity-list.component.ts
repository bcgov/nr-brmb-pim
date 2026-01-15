import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { DopYieldFieldCommodityBerries } from 'src/app/conversion/models-yield';
import { makeNumberOnly } from 'src/app/utils';

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

  constructor(private fb: UntypedFormBuilder) {}

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
    // TODO: I might need to uncomment additional columns
    this.fieldCommodityFormGroup = this.fb.group({
      // declaredYieldFieldCommodityBerriesGuid: [this.dopYieldFieldCommodityBerries.declaredYieldFieldCommodityBerriesGuid],
      // fieldId: [this.dopYieldFieldCommodityBerries.fieldId],
      // cropCommodityId: [this.dopYieldFieldCommodityBerries.cropCommodityId],
      // cropCommodityName: [this.dopYieldFieldCommodityBerries.cropCommodityName],
      // cropYear: [ this.dopYieldFieldCommodityBerries.cropYear],
      // totalProduction: [this.dopYieldFieldCommodityBerries.totalProduction],
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
    // TODO on save
  }
  
}
