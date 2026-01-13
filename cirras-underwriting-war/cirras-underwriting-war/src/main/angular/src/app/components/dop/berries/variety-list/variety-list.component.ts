import { S } from '@angular/cdk/scrolling-module.d-ud2XrbF8';
import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { DopYieldFieldVarietyBerries } from 'src/app/conversion/models-yield';

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

  constructor(private fb: UntypedFormBuilder) {}

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
      declaredYieldFieldVarietyBerriesGuid: [this.dopYieldFieldVarietyBerries.declaredYieldFieldVarietyBerriesGuid],
      declaredYieldFieldCommodityBerriesGuid: [this.dopYieldFieldVarietyBerries.declaredYieldFieldCommodityBerriesGuid],
      cropVarietyId: [this.dopYieldFieldVarietyBerries.cropVarietyId],
      cropVarietyName: [this.dopYieldFieldVarietyBerries.cropVarietyName],
      plantedAcres: [ this.dopYieldFieldVarietyBerries.plantedAcres],
      soldShippedYield: [ this.dopYieldFieldVarietyBerries.soldShippedYield],
      salesYield: [ this.dopYieldFieldVarietyBerries.salesYield],
      abandonmentYield: [ this.dopYieldFieldVarietyBerries.abandonmentYield],
      totalProduction: [this.dopYieldFieldVarietyBerries.totalProduction],
      totalProductionOverride: [ this.dopYieldFieldVarietyBerries.totalProductionOverride ],
      isHiddenOnPrintoutInd: [ this.dopYieldFieldVarietyBerries.isHiddenOnPrintoutInd],
    });
    this.dopYieldFieldVarietyBerriesFormArray.push(this.varietyFormGroup);
  }


}
