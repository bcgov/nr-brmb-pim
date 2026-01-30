import { Component, Inject, OnInit, } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';
import { getCodeOptions } from 'src/app/utils/code-table-utils';

@Component({
    selector: 'cirras-plant-insurability',
    templateUrl: './plant-insurability.component.html',
    styleUrls: ['./plant-insurability.component.scss'],
    standalone: false
})
export class PlantInsurabilityComponent implements OnInit {
  dialogType = DIALOG_TYPE.INFO;

  dataReceived: any;
  varietyPlantInsForm: UntypedFormGroup;

  plantInsurabilityOptions = getCodeOptions("forage_plant_insurability_type_code");  

  get titleLabel() {
    return `Edit Plant Insurability for ${this.dataReceived?.varietyName}`;
  }

  constructor(
    public dialogRef: MatDialogRef<PlantInsurabilityComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,  
    private fb: UntypedFormBuilder,  
    ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;
      } 
    }

  ngOnInit(): void {
    
    // initialize the form
    let plantIns = new UntypedFormArray([])

    for (let i = 0; i < this.plantInsurabilityOptions.length; i ++) {
      plantIns.push( this.fb.group( {  
        cb:                           ( this.isPlantInsSelected(this.plantInsurabilityOptions[i].code) ? true : false) ,
        plantInsurabilityTypeCode:    this.plantInsurabilityOptions[i].code ,
        description:                  this.plantInsurabilityOptions[i].description,
        isUsedInd:                    this.getIsUsedInd(this.plantInsurabilityOptions[i].code)
      } ) )
    }

    this.varietyPlantInsForm = this.fb.group({
      plantIns: plantIns
    })
    
  }


  isPlantInsSelected(code) {

    if ( this.dataReceived.cropVarietyPlantInsurabilities) {
      let el = this.dataReceived.cropVarietyPlantInsurabilities.find( x => x.plantInsurabilityTypeCode == code )
      if (el) {
        return true
      }
    }
    return false
  }

  getIsUsedInd(code) {

    if ( this.dataReceived.cropVarietyPlantInsurabilities) {
      let el = this.dataReceived.cropVarietyPlantInsurabilities.find( x => x.plantInsurabilityTypeCode == code )
      if (el) {
        return el.isUsedInd
      }
    }
    return false
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  

  onProceed() {

    let dataToSend = {
      cropVarietyId:                  this.dataReceived.cropVarietyId,
      cropVarietyPlantInsurabilities: this.getPlantInsurability()  
    }

    // send the results to the main page
    this.dialogRef.close({event:'Update', data: dataToSend});
  }

  getPlantInsurability(){

    let plantIns = []
    let frmPlantIns = this.varietyPlantInsForm.controls.plantIns['controls'] 

    for (let i = 0 ; i < frmPlantIns.length; i++ ) {

      // find the corresponding original plant insurability
      let el = null

      if (this.dataReceived.cropVarietyPlantInsurabilities) {
        el = this.dataReceived.cropVarietyPlantInsurabilities.find(x => x.plantInsurabilityTypeCode == frmPlantIns[i].controls.plantInsurabilityTypeCode.value )
      }

      if (el) {
        // this insurability type exists in the original list of plant insurability types        
          plantIns.push({
            cropVarietyId:                this.dataReceived.cropVarietyId,
            cropVarietyInsurabilityGuid:  this.dataReceived.cropVarietyInsurabilityGuid, 
            plantInsurabilityTypeCode:    frmPlantIns[i].controls.plantInsurabilityTypeCode.value,
            description:                  frmPlantIns[i].controls.description.value,
            deletedByUserInd:             !frmPlantIns[i].controls.cb.value, // setting this ind to true for the deleted insurabilities
            isUsedInd: 						        el.isUsedInd
          })

      } else {

        // the plant insurability type is new - just add it
        if (frmPlantIns[i].controls.cb.value) {

          plantIns.push({
            cropVarietyId:                this.dataReceived.cropVarietyId,
            cropVarietyInsurabilityGuid:  this.dataReceived.cropVarietyInsurabilityGuid, 
            plantInsurabilityTypeCode:    frmPlantIns[i].controls.plantInsurabilityTypeCode.value,
            description:                  frmPlantIns[i].controls.description.value,
            deletedByUserInd:             false,
            isUsedInd: 						        false
          })
          
        }
      }
    }
    return plantIns
  }


}
