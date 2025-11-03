import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';
import { BERRY_COMMODITY } from 'src/app/utils/constants';


export function showRowSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry || cmdty == BERRY_COMMODITY.Strawberry) {
    return false
  }
  return true
}

export function showPlantSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry || cmdty == BERRY_COMMODITY.Strawberry) {
    return false
  }
  return true
}

export function showTotalPlantsForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry || cmdty == BERRY_COMMODITY.Strawberry) {
    return false
  }
  return true
}

export function showIsPlantInsuredForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry) {
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
  
  if (cmdty == BERRY_COMMODITY.Raspberry) {
    return {
      'width': `1200px`
    };
  }

  if (cmdty == BERRY_COMMODITY.Strawberry) {
    return {
      'width': `1340px`
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
  @Input() selectedCommodity;

  setTableHeaderStyle() {
    return setTableHeaderStyleForBerries(this.selectedCommodity)
  }

  setInnerWrapperStyle() {
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry ) {
      return {
          'width': `1535px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Raspberry ) {
      return {
          'width': `1215px` 
      };
    }  

    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry) {
      return {
          'width': `1355px` 
      };
    }  
  }

  showRowSpacing() {
    return showRowSpacingForBerries(this.selectedCommodity)
  }

  showPlantSpacing() {
    return showPlantSpacingForBerries(this.selectedCommodity)
  }

  showTotalPlants() {
    return showTotalPlantsForBerries(this.selectedCommodity)
  }

  showIsPlantInsured() {
    return showIsPlantInsuredForBerries(this.selectedCommodity)
  }

    setPlantInsuredStyles() {
    if(this.selectedCommodity == BERRY_COMMODITY.Strawberry ) {
      return {
        'width': `140px`
      };
    } else {
      return {
        'width': `80px`
      };
    }
  }
}
