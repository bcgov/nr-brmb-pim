import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnnualFieldRsrc, AnnualFieldListRsrc, LegalLandRsrc, AddFieldValidationRsrc, InventoryContractRsrc, InventoryField, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { convertToLegalLandList } from 'src/app/conversion/conversion-from-rest';
import { LegalLandList } from 'src/app/conversion/models';
import { makeTitleCase, setHttpHeaders } from 'src/app/utils';
import { LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { DIALOG_TYPE } from '../../dialogs/base-dialog/base-dialog.component';

export interface AddLandPopupData {
  fieldId: number;
  fieldLabel?: string;
  cropYear: number;
  policyId: string;
  insurancePlanId: number; 
  annualFieldDetailId?: number;
  otherLegalDescription? : string;
  landData?: {
    fieldId?: number;
    legalLandId?: number;
    fieldLabel?: string;
    otherLegalDescription?: string;
    landUpdateType?: string;
    transferFromGrowerContractYearId? : number;
    plantings?: Array<InventoryField>;
    uwComments?: Array<UnderwritingComment>;
  }
}

@Component({
    selector: 'cirras-add-land',
    templateUrl: './add-land.component.html',
    styleUrls: ['./add-land.component.scss'],
    standalone: false
})
export class AddLandComponent implements OnInit {

  titleLabel = "Add Field";

  dialogType = DIALOG_TYPE.INFO;
  
  dataReceived : AddLandPopupData;

  addLandForm: UntypedFormGroup;
  
  legalLandList : LegalLandList = {};
  fieldList: AnnualFieldListRsrc;
  //plantings?: Array<InventoryField>;
  validationMessages : AddFieldValidationRsrc;

  searchChoice = {
    searchLegal : 'searchLegalLand',
    searchField : 'searchFieldId'
  };

  showSearchLegalMsg = false;
  showNewLegalLandMessage = false;
  showNoFieldMessage = false;
  showProceedButton = false;

  selectedChoice = this.searchChoice.searchLegal;

  constructor(
    public dialogRef: MatDialogRef<AddLandComponent>,
    @Inject(MAT_DIALOG_DATA) public data: AddLandPopupData,
    private fb: UntypedFormBuilder,
    private tokenService: TokenService,
    private appConfig: AppConfigService, 
    private http: HttpClient   ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;
      } 

    }


  ngOnInit(): void {

    // add fields to the form
    this.addLandForm = this.fb.group({
      choiceSelected: [ this.searchChoice.searchLegal ],
      searchLegalLandOrFieldId:    [],
      fieldLabel:       [],
      legalLandIdSelected: [],
      otherLegalDescription: [],
      fieldIdSelected: []
    })

  }

  onChoiceClick( choice ) {
    this.selectedChoice = choice

    this.clearAllForm()
  }


  onLegalLandSearch() {
    const searchLegalLandOrFieldId = this.addLandForm.controls.searchLegalLandOrFieldId.value

    // start the search when least 3 symbols are entered
    if (!searchLegalLandOrFieldId || searchLegalLandOrFieldId.length < 3) {
  
      this.showSearchLegalMsg = true
      return
    } else {
      this.showSearchLegalMsg = false
    }

    if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchLegal) {
      this.searchLegalLand(searchLegalLandOrFieldId)
    }

    if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchField) {
      
      if (!isNaN(searchLegalLandOrFieldId)) {
        this.getFields("", searchLegalLandOrFieldId)
      } else {
        alert( searchLegalLandOrFieldId + " is not a valid Field ID")
      }
    }    
  }
  
  searchLegalLand(searchLegalLandOrFieldId) {

    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/legallands?legalLocation=" + encodeURI(searchLegalLandOrFieldId) + "&isWildCardSearch=true&searchByLegalLocOrLegalDesc=true" 

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return this.http.get(url,httpOptions).toPromise().then((data: LegalLandRsrc) => {
      self.legalLandList = convertToLegalLandList(data)

      if (self.legalLandList && self.legalLandList.collection && self.legalLandList.collection.length > 0) {
        this.showNewLegalLandMessage = false
        this.showProceedButton = false
      } else {
        // give the option to add new legal land
        this.showNewLegalLandMessage = true
        this.showProceedButton = true
      }

     })
  }

  getPolicyAndPlan(field: AnnualFieldRsrc){
    let policyAndPlan = ""

    if (field && field.policies) {

      field.policies.forEach(policy => {

        if ( policyAndPlan.length > 0 ) {
          policyAndPlan = policyAndPlan + "\n" // add a line break
        }
        policyAndPlan = policyAndPlan + policy.policyNumber + " (" + makeTitleCase(policy.insurancePlanName) + ") - " + policy.growerName
      })
    } 

    return policyAndPlan
  }

  getFields(legalLandId, fieldId){

    this.clearFieldsAndWarnings()

    if (legalLandId == "-1" ) { //the legal land would be added as new
      this.showProceedButton = true
      return
    }
    
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/annualFields?legalLandId=" + legalLandId.toString()
    url = url +"&fieldId=" + fieldId.toString()
    url = url +"&cropYear=" +  this.dataReceived.cropYear.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return this.http.get(url,httpOptions).toPromise().then((data: AnnualFieldListRsrc) => {
      self.fieldList = data;

      if (self.fieldList && self.fieldList.collection && self.fieldList.collection.length > 0) {

        if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchLegal) {
          this.showNoFieldMessage = false
          this.showProceedButton = false
        } else {
          this.addLandForm.get("legalLandIdSelected").setValue(self.fieldList.collection[0].legalLandId)
          this.addLandForm.get("otherLegalDescription").setValue(self.fieldList.collection[0].otherLegalDescription)
          this.addLandForm.get("fieldIdSelected").setValue(self.fieldList.collection[0].fieldId)
          // validate field
          this.validateFields(self.fieldList.collection[0])
        }

      } else {

        if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchLegal) {
          // give the option to add new field 
          this.showNoFieldMessage = true
          this.showProceedButton = true
        } else {
          // field id was not found
          alert ("The field id was not found") // or show a message on the form
          this.showProceedButton = false
        }

      }

    })

  }

  validateFields(field) {
    
    if (field == -1 ) { //the field would be added as new
      this.showProceedButton = false // it needs a field label too
      this.validationMessages = <AddFieldValidationRsrc>{};
      return
    }

    this.addLandForm.controls.fieldLabel.setValue("")

    // we will be transfering field from the policy which is on the same plans as the inventoryContract's plan
    let policyId = ""  
    
    field.policies.forEach(policy => {

      if (this.dataReceived.insurancePlanId == policy.insurancePlanId && this.dataReceived.cropYear == policy.cropYear) {
        policyId = policy.policyId
        return
      }
    })

    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    // "/uwcontracts/{policyId}/validateAddField"

    url = url + "/uwcontracts/" + this.dataReceived.policyId + "/validateAddField"
    url = url + "?policyId=" +  this.dataReceived.policyId // policyId
    url = url + "&fieldId=" +  field.fieldId.toString()
    url = url + "&transferFromPolicyId=" + policyId

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return this.http.get(url,httpOptions).toPromise().then((data: AddFieldValidationRsrc) => {
      self.validationMessages = data

      if (self.validationMessages.errorMessages && self.validationMessages.errorMessages.length > 0) {
        this.showProceedButton = false
      } else {
        this.showProceedButton = true
      }
    })
  }

  clearAllForm() {
    this.addLandForm.controls.legalLandIdSelected.setValue(0)
    this.addLandForm.controls.fieldIdSelected.setValue(0)
    this.showSearchLegalMsg = false;
    this.showNewLegalLandMessage = false;
    this.showNoFieldMessage = false;
    this.showProceedButton = false;

    this.legalLandList = <LegalLandList>{};
    this.fieldList = <AnnualFieldListRsrc>{};
    this.validationMessages = <AddFieldValidationRsrc>{};
  }

  clearFieldsAndWarnings() {
    this.fieldList = <AnnualFieldListRsrc>{};
    this.validationMessages = <AddFieldValidationRsrc>{};
  }

  checkLength() {

    this.validationMessages = <AddFieldValidationRsrc>{};
    this.addLandForm.controls.fieldIdSelected.setValue(-1)

    if (this.addLandForm.get("fieldLabel").value && this.addLandForm.get("fieldLabel").value.length > 1) {
      this.showProceedButton = true
    }
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onProceed(){

    let dataToSend : AddLandPopupData = this.dataReceived  

    let landUpdateType = ""

    const legalLandId = this.addLandForm.get("legalLandIdSelected").value
    let otherLegalDescription = ""

    let fieldId = this.addLandForm.get("fieldIdSelected").value
    let fieldLabel = ""

    let transferFromGrowerContractYearId 

    if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchLegal) {

      if (legalLandId && legalLandId > -1) {
        otherLegalDescription = this.legalLandList.collection.find(el => el.legalLandId == legalLandId).otherDescription
      } else {
        otherLegalDescription = this.addLandForm.get("searchLegalLandOrFieldId").value
        landUpdateType = LAND_UPDATE_TYPE.NEW_LAND
      }

    } else {
      // the user has searched for field id
      otherLegalDescription = this.addLandForm.get("otherLegalDescription").value
    }


    if (fieldId && fieldId > -1) {
      fieldLabel = this.fieldList.collection.find(el => el.fieldId == fieldId).fieldLabel

      let fld =  this.fieldList.collection.find(el => el.fieldId == fieldId)
      if (fld) {
        fieldLabel = fld.fieldLabel

        fld.policies.forEach(policy => {

          if (this.dataReceived.insurancePlanId == policy.insurancePlanId && this.dataReceived.cropYear == policy.cropYear) {

            transferFromGrowerContractYearId = policy.growerContractYearId
            return
            
          }

        })

        landUpdateType = LAND_UPDATE_TYPE.ADD_EXISTING_LAND
      }
    } else {
      fieldId = this.dataReceived.fieldId
      fieldLabel = this.addLandForm.get("fieldLabel").value
      if (fieldLabel && fieldLabel.length > 0) {
        landUpdateType = LAND_UPDATE_TYPE.ADD_NEW_FIELD
      }
    }

    if (landUpdateType == "") {
      alert ("Cannot determine land update type")
      return
    }

    if (fieldId > 0) {
      // it's a field that was found in the system 
      // the rollover endpoint should return plantings and comments 

      let url = this.appConfig.getConfig().rest["cirras_underwriting"]
      url = url +"/annualField/" + fieldId + "/rolloverInventory"
      url = url + "?rolloverToCropYear=" +  this.dataReceived.cropYear 
      url = url + "&insurancePlanId=" +  this.dataReceived.insurancePlanId

      const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

      return this.http.get(url,httpOptions).toPromise().then((data: AnnualFieldRsrc) => {

        let plantings = []
        let uwComments = []

        if (data && data.plantings && data.plantings.length > 0 ) {
          plantings = data.plantings
        }

        if (data && data.uwComments && data.uwComments.length > 0) {
          uwComments = data.uwComments
        }

        dataToSend.landData = {
          legalLandId : (legalLandId) ? legalLandId : "",
          otherLegalDescription : otherLegalDescription,
          fieldId : fieldId,
          fieldLabel : fieldLabel,
          transferFromGrowerContractYearId : transferFromGrowerContractYearId,
          landUpdateType : landUpdateType,
          plantings: plantings,
          uwComments: uwComments
        }
    
        // send the results to the main page
        this.dialogRef.close({event:'AddLand', data: dataToSend});
      })

    } else {
      // new land / field, no need to get plantings and comments 
      // just close the popup

      dataToSend.landData = {
        legalLandId : (legalLandId) ? legalLandId : "",
        otherLegalDescription : otherLegalDescription,
        fieldId : fieldId,
        fieldLabel : fieldLabel,
        transferFromGrowerContractYearId : transferFromGrowerContractYearId,
        landUpdateType : landUpdateType,
        plantings: [],
        uwComments: []
      }
  
      // send the results to the main page
      this.dialogRef.close({event:'AddLand', data: dataToSend});

    }

  }

  getOtherDescription() {
    const legalLandId = this.addLandForm.get("legalLandIdSelected").value
    let otherLegalDescription = ""

    
    if (this.addLandForm.controls.choiceSelected.value == this.searchChoice.searchLegal) {

      if (legalLandId && legalLandId > -1) {
        otherLegalDescription = this.legalLandList.collection.find(el => el.legalLandId == legalLandId).otherDescription
      } else {
        otherLegalDescription = this.addLandForm.get("searchLegalLandOrFieldId").value
      }
    }
    return otherLegalDescription
  }
  
}
