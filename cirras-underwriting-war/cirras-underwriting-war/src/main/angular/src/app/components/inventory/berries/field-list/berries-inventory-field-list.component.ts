import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';
import { BERRY_COMMODITY } from 'src/app/utils/constants';


export function showRowSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry ) {
    return false
  }
  return true
}

export function showPlantSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry ) {
    return false
  }
  return true
}

export function showTotalPlantsForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry ) {
    return false
  }
  return true
}

export function showIsPlantInsuredForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry ) {
    return false
  }
  return true
}

export function setTableHeaderStyleForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Blueberry ) {
    return {
      'width': `1520px`
    };
  }
  
  if (cmdty == BERRY_COMMODITY.Raspberry ) {
    return {
      'width': `1200px`
    };
  }
}

@Component({
  selector: 'berries-inventory-field-list',
  templateUrl: './berries-inventory-field-list.component.html',
  styleUrl: './berries-inventory-field-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldListComponent  {

  @Input() fields: Array<AnnualField>;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() defaultCommodity;

  setTableHeaderStyle() {
    return setTableHeaderStyleForBerries(this.defaultCommodity)
  }

  setInnerWrapperStyle() {
    if (this.defaultCommodity == BERRY_COMMODITY.Blueberry ) {
      return {
          'width': `1535px`
      };
    }

    if (this.defaultCommodity == BERRY_COMMODITY.Raspberry ) {
      return {
          'width': `1215px` 
      };
    }    
  }

  showRowSpacing() {
    return showRowSpacingForBerries(this.defaultCommodity)
  }

  showPlantSpacing() {
    return showPlantSpacingForBerries(this.defaultCommodity)
  }

  showTotalPlants() {
    return showTotalPlantsForBerries(this.defaultCommodity)
  }

  showIsPlantInsured() {
    return showIsPlantInsuredForBerries(this.defaultCommodity)
  }

}
