import { Component, Inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UntypedFormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RemoveFieldValidationRsrc } from '@cirras/cirras-underwriting-api';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { setHttpHeaders } from 'src/app/utils';
import { LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { DIALOG_TYPE } from '../../dialogs/base-dialog/base-dialog.component';


export interface RemoveFieldPopupData {
  fieldId: number;
  fieldLabel: string;
  policyId: string;
  hasInventory: boolean;
  hasComments: boolean;
  landData?: {
    landUpdateType?: string;
  }
}


@Component({
  selector: 'cirras-remove-field',
  templateUrl: './remove-field.component.html',
  styleUrls: ['./remove-field.component.scss']
})
export class RemoveFieldComponent implements OnInit {

  titleLabel = "Remove or Delete Field" 
  dialogType = DIALOG_TYPE.INFO;

  dataReceived : RemoveFieldPopupData;
  validation: RemoveFieldValidationRsrc;
  deleteToolTip: String;

  constructor(
    public dialogRef: MatDialogRef<RemoveFieldComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RemoveFieldPopupData,
    private fb: UntypedFormBuilder,
    private tokenService: TokenService,
    private appConfig: AppConfigService, 
    private http: HttpClient   ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;

        this.titleLabel = "Remove or Delete Field " + 
          ((this.dataReceived && this.dataReceived.fieldId) ? 
            (": " + this.dataReceived. fieldLabel + " (" + this.dataReceived.fieldId + ")") : "")
      } 
    }

  ngOnInit(): void {
    this.validateFieldForRemoval()
  }

  validateFieldForRemoval() {

    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url + "/uwcontracts/" + this.dataReceived.policyId.toString()
    url = url +"/validateRemoveField?fieldId=" + this.dataReceived.fieldId.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this

    return this.http.get(url,httpOptions).toPromise().then((data: RemoveFieldValidationRsrc) => {
      
      self.validation = data;

      if(data && data.isDeleteFieldAllowed) {
        this.deleteToolTip = "Deletes the field from the system";
      } else {
        this.deleteToolTip = "Delete the field from the system is only possible if it's not associated with another policy or contract and has no inventory, comments and yield in any year";
      }

    })


  }

  onRemoveFieldFromPolicy() {
    let dataToSend : RemoveFieldPopupData = this.dataReceived  

    dataToSend.landData = {
      landUpdateType : LAND_UPDATE_TYPE.REMOVE_FIELD_FROM_POLICY
    }

    // send the results to the main page
    this.dialogRef.close({event: 'Proceed', data: dataToSend});
  }

  onDeleteField() {
    let dataToSend : RemoveFieldPopupData = this.dataReceived  

    dataToSend.landData = {
      landUpdateType : LAND_UPDATE_TYPE.DELETE_FIELD
    }

    // send the results to the main page
    this.dialogRef.close({event: 'Proceed', data: dataToSend});
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }
}
