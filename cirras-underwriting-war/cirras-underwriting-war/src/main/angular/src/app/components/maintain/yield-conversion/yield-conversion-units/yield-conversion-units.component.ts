import { Component, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { getCodeOptions } from 'src/app/utils/code-table-utils';

@Component({
  selector: 'yield-conversion-units',
  templateUrl: './yield-conversion-units.component.html',
  styleUrls: ['./yield-conversion-units.component.scss']
})
export class YieldConversionUnitsComponent {

  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;

  insurancePlansDefault = getCodeOptions("insurance_plan")

  dataReceived: any;

  constructor(
    public dialogRef: MatDialogRef<YieldConversionUnitsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,  
    ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;
      } 
    }

  getSelectedPlanName() {

    if ( this.yieldMeasUnitList && this.yieldMeasUnitList.collection && this.yieldMeasUnitList.collection.length > 0) {

      let el = this.insurancePlansDefault.find( x => x.code == this.yieldMeasUnitList.collection[0].insurancePlanId.toString() )

      if (el) {
        return el.description
      } else {
        return ""
      }
    }
  }

  onCancelChanges() {
    this.dialogRef.close();
  }

}
