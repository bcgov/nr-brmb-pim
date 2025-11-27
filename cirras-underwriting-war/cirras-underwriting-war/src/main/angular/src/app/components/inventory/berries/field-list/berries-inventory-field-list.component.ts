import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';
import { BERRY_COMMODITY } from 'src/app/utils/constants';


export function showRowSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Blueberry) {
    return true
  } else {
    return false
  }
}

export function showPlantSpacingForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Blueberry) {
    return true
  } else {
    return false
  }
}

export function showTotalPlantsForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Blueberry) {
    return true
  } else {
    return false
  }
}

export function showIsPlantInsuredForBerries(cmdty) {
  if (cmdty == BERRY_COMMODITY.Raspberry || cmdty == BERRY_COMMODITY.Cranberry) {
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

  if (cmdty == BERRY_COMMODITY.Cranberry) {
    return {
      'width': `1750px`
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
export class BerriesInventoryFieldListComponent {

  @Input() fields: Array<AnnualField>;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() selectedCommodity;
  @Input() numComponentReloads;

  minNewFieldId = 0

  getLatestAddedNewField() {
    var self = this
    this.fields.forEach( x => {

      if (x.isNewFieldUI == true && x.deletedByUserInd !== true && x.fieldId < self.minNewFieldId) {
        self.minNewFieldId = x.fieldId
      }
    })
  }


  ngOnChanges(changes: SimpleChanges) {
    if ((changes.fields && changes.fields.currentValue) || (changes.numComponentReloads && changes.numComponentReloads.currentValue)) {
      if (this.fields) {
        this.getLatestAddedNewField()
      }
    }
  }

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

    if (this.selectedCommodity == BERRY_COMMODITY.Cranberry) {
      return {
          'width': `1765px` 
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

  isCranberry() {
    if(this.selectedCommodity == BERRY_COMMODITY.Cranberry ) {
      return true
    } else {
      return false
    }
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

  getCropYear() {
    
    if (this.fields && this.fields.length > 0 ) {
      return this.fields[0].cropYear
    } else {
      return ""
    }
  }
}
