import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AddFieldValidationRsrc, AnnualFieldListRsrc, AnnualFieldRsrc, LegalLandRsrc } from '@cirras/cirras-underwriting-api';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';
import { LegalLandList } from 'src/app/conversion/models';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { setHttpHeaders } from 'src/app/utils';
import { lastValueFrom } from 'rxjs';
import { convertToLegalLandList } from 'src/app/conversion/conversion-from-rest';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BERRY_COMMODITY, INSURANCE_PLAN, LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { AddLandPopupData } from '../../add-land/add-land.component';

@Component({
  selector: 'add-field',
  templateUrl: './add-field.component.html',
  styleUrl: './add-field.component.scss',
  standalone: false
})
export class AddFieldComponent implements OnInit{

  titleLabel = "Add Field";

  dialogType = DIALOG_TYPE.INFO;

  dataReceived : AddLandPopupData

  dataToSend : AddLandPopupData

  deafultSearchChoices = [
    { name: 'Legal Location', value: 'searchLegalLocation' , visible: [INSURANCE_PLAN.GRAIN, INSURANCE_PLAN.FORAGE] },
    { name: 'PID', value: 'searchPID' , visible: [INSURANCE_PLAN.BERRIES] },
    { name: 'Field Address', value: 'searchFieldLocation' , visible: [INSURANCE_PLAN.BERRIES] },
    { name: 'Field ID', value: 'searchFieldId' , visible: [INSURANCE_PLAN.BERRIES, INSURANCE_PLAN.GRAIN, INSURANCE_PLAN.FORAGE] }
  ];

  searchChoices = []

  showSearchLegalMsg = false;
  showNewLegalLandMessage = false;
  showNoFieldMessage = false;
  showProceedButton = false;

  addFieldForm : FormGroup 
  
  legalLandList : LegalLandList = {};
  fieldList: AnnualFieldListRsrc;
  validationMessages : AddFieldValidationRsrc;

  constructor( 
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private tokenService: TokenService,
    private appConfig: AppConfigService, 
    private http: HttpClient ) {

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;

        this.setSearchChoices()
      } 
  }

  ngOnInit(): void {

    let defaultChoice = ''

    if (this.dataReceived) {

      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
        defaultChoice = 'searchLegalLocation'
      }

      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
        defaultChoice = 'searchPID'
      }

      this.dataToSend = this.dataReceived 
    }

    this.addFieldForm = new FormGroup({
      choiceSelected: new FormControl(defaultChoice),
      searchLegalLandOrFieldId: new FormControl()
    });
  }

  setSearchChoices() { 

    for (let i = 0; i < this.deafultSearchChoices.length; i++ ) {
      let el = this.deafultSearchChoices[i].visible.find (x => x == this.dataReceived.insurancePlanId)
      if (el) {
        this.searchChoices.push (this.deafultSearchChoices[i])
      }
    }
  }

  get selectedSearch(): string {
    const selectedValue = this.addFieldForm.controls.choiceSelected.value
    const selectedOption = this.searchChoices.find(option => option.value === selectedValue)
    return selectedOption ? selectedOption.name : ''
  }
  onNewChoice() {
    this.addFieldForm.controls.searchLegalLandOrFieldId.setValue('')
    this.clearAllForm()
  }

  clearAllForm() {
    this.showSearchLegalMsg = false;
    this.showNewLegalLandMessage = false;
    this.showNoFieldMessage = false;
    this.showProceedButton = false;

    this.legalLandList = null
    this.fieldList = null
    this.validationMessages = null

    this.dataToSend.landData = {
      fieldId: null,
      legalLandId: null,
      fieldLabel: null,
      fieldLocation: null,
      primaryPropertyIdentifier: null,
      otherLegalDescription: null,
      landUpdateType: null,
      transferFromGrowerContractYearId: null,
      plantings: null,
      uwComments: null  
    }
  }

  onTypeInSearchBox() {

    this.clearAllForm()

    if (this.addFieldForm.controls.choiceSelected.value == 'searchFieldLocation') {
       
      const searchLegalLandOrFieldId = this.addFieldForm.controls.searchLegalLandOrFieldId.value

      // start the search when at least 3 symbols are entered
      if (searchLegalLandOrFieldId && searchLegalLandOrFieldId.length >= 3) {
        this.getFields(this.dataReceived.cropYear, "", "", searchLegalLandOrFieldId)
      }
    }
  }

  onSearch() {
    const searchLegalLandOrFieldId = (this.addFieldForm.controls.searchLegalLandOrFieldId.value).trim()

    // start the search when least 3 symbols are entered
    if (!searchLegalLandOrFieldId || searchLegalLandOrFieldId.length < 3) {
  
      this.showSearchLegalMsg = true
      return
    } else {
      this.showSearchLegalMsg = false
    }

    if (this.addFieldForm.controls.choiceSelected.value == 'searchLegalLocation' || this.addFieldForm.controls.choiceSelected.value == 'searchPID') {
      // search legal land
      this.searchLegalLand(searchLegalLandOrFieldId)
    } 

    // search field 
    if (this.addFieldForm.controls.choiceSelected.value == 'searchFieldId') {
      if (!isNaN(searchLegalLandOrFieldId)) {
        this.getFields(this.dataReceived.cropYear, "", searchLegalLandOrFieldId, "")
      } else {
        alert( searchLegalLandOrFieldId + " is not a valid Field ID")
      }
    }
  }

  getSearchLegalLandUrl(searchLegalLandOrFieldId) {
    let url = ""

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      url = url +"/legallands?legalLocation=" + encodeURI(searchLegalLandOrFieldId) + "&isWildCardSearch=true&searchByLegalLocOrLegalDesc=true" 
    }
    
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      url = url +"/legallands?primaryPropertyIdentifier=" + encodeURI(searchLegalLandOrFieldId) + "&isWildCardSearch=false" 
    }

    return url
  }

  searchLegalLand(searchLegalLandOrFieldId) {
  
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url + this.getSearchLegalLandUrl(searchLegalLandOrFieldId)
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: LegalLandRsrc) => {
      self.legalLandList = convertToLegalLandList(data)

      if (self.legalLandList && self.legalLandList.collection && self.legalLandList.collection.length > 0) {
        this.showNewLegalLandMessage = false
        this.showProceedButton = false
      } else {
        // give the option to add new legal land
        this.setNewLegalLand(searchLegalLandOrFieldId)

        this.showNewLegalLandMessage = true
        this.showProceedButton = true
      }
    })
  }

  setNewLegalLand(searchLegalLandOrFieldId) {
    this.dataToSend.landData.legalLandId = -1

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
        this.dataToSend.landData.otherLegalDescription = searchLegalLandOrFieldId
    }

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
        this.dataToSend.landData.primaryPropertyIdentifier = searchLegalLandOrFieldId
    }

    this.dataToSend.landData.fieldId = -1
  }

  getFields(cropYear, legalLandId, fieldId, fieldLocation){
    
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url + "/annualFields?legalLandId=" + legalLandId.toString()
    url = url + "&fieldId=" + fieldId.toString()
    url = url + "&fieldLocation=" + fieldLocation
    url = url + "&cropYear=" +  cropYear.toString()

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: AnnualFieldListRsrc) => {
      self.fieldList = data;

      if (self.fieldList && self.fieldList.collection && self.fieldList.collection.length > 0) {
        if (this.addFieldForm.controls.choiceSelected.value == 'searchFieldId') {
          // if the user searches by field id then validate the field here
          this.dataToSend.landData.legalLandId = self.fieldList.collection[0].legalLandId
          this.dataToSend.landData.fieldId = self.fieldList.collection[0].fieldId

          this.dataToSend.landData.fieldLabel = self.fieldList.collection[0].fieldLabel
          this.dataToSend.landData.fieldLocation = self.fieldList.collection[0].fieldLocation
          this.dataToSend.landData.primaryPropertyIdentifier = self.fieldList.collection[0].primaryPropertyIdentifier
          this.dataToSend.landData.otherLegalDescription = self.fieldList.collection[0].otherLegalDescription

          this.validateFields(self.fieldList.collection[0])
        }
      } else {
        this.showNoFieldMessage = true
      }
    })
  }

  validateFields(field) {

    this.validationMessages = null // clear any messages 
    
    if (!field ) { //the field would be added as new, no validations needed
      return
    }

    if (this.isBerryFieldOnCurrentPolicy() ){
      return
    }

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
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: AddFieldValidationRsrc) => {
      self.validationMessages = data

      if (self.validationMessages.errorMessages && self.validationMessages.errorMessages.length > 0) {
        this.showProceedButton = false
      } else {
        this.showProceedButton = true
      }
    })
  }

  onLegalLandReceived(legalLand) { 
    this.dataToSend.landData.legalLandId = legalLand.legalLandId
    this.dataToSend.landData.primaryPropertyIdentifier = legalLand.primaryPropertyIdentifier
    this.dataToSend.landData.otherLegalDescription = legalLand.otherLegalDescription

    this.fieldList = null

    if (legalLand.legalLandId > -1 ) {
      this.getFields(this.dataReceived.cropYear, legalLand.legalLandId, "", "") 
    } else { 
      // new legal land - allow Proceed
      this.dataToSend.landData.fieldId = -1
      this.showProceedButton = true;
    }
  }

  onFieldReceived(field: AnnualFieldRsrc) { 
    this.dataToSend.landData.fieldId = field.fieldId
    this.dataToSend.landData.fieldLabel = field.fieldLabel
    this.dataToSend.landData.fieldLocation = field.fieldLocation

    this.validationMessages = null

    if (field && field.fieldId > -1) {
      // run validation for existing fields
      this.validateFields(field)
    } else {
      //no validation for new fields 
      this.showProceedButton = true
    }
  }

  isBerryFieldOnCurrentPolicy() {
    // this check is done in the UI only for BERRIES
    // other plans should go through the API for validation

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {

      if (this.dataReceived.berries && this.dataReceived.berries.selectedCommodity && this.dataReceived.berries.fields && this.dataReceived.berries.fields.length > 0) {
        
        let fld = this.dataReceived.berries.fields.find (x => x.fieldId == this.dataToSend.landData.fieldId)

        if (fld) {
          let cmdty = fld.commodities.find (x => x == this.dataReceived.berries.selectedCommodity)

          if (cmdty) {
            const berryName = Object.entries(BERRY_COMMODITY).find(([key, value]) => value === cmdty)?.[0];

            let msg = "This Field is already on this Policy with " + berryName + ". Please add a new planting instead."

            this.validationMessages = {
                                        warningMessages: [],
                                        errorMessages: [
                                          {
                                            message: msg,
                                          }
                                        ]
                                      } as any

            // do not allow PROCEED button if the selected commodity is the same as the commodity on the field
            this.showProceedButton = false
            return true
          } else {
            // allow PROCEED button if the field is already on the policy but we're adding a different commodity on it
            this.showProceedButton = true
            return true
          }
        }
      }
    }

    return false // and go thru API to validate the field 
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onProceed(){

    let landUpdateType = ""
    let transferFromGrowerContractYearId 

    if (!this.dataToSend.landData.legalLandId || !this.dataToSend.landData.fieldId){
      alert ("Legal Land or Field were not selected. Proceed is not possible.")
      return
    }

    if (this.dataToSend.landData.legalLandId == -1) {

      landUpdateType = LAND_UPDATE_TYPE.NEW_LAND

    } else if ( this.dataToSend.landData.legalLandId > -1 && this.dataToSend.landData.fieldId == -1) {

      landUpdateType = LAND_UPDATE_TYPE.ADD_NEW_FIELD

    } else if (this.dataToSend.landData.legalLandId > -1 && this.dataToSend.landData.fieldId > -1) {
        
      let fld =  this.fieldList.collection.find(el => el.fieldId == this.dataToSend.landData.fieldId)
      if (fld) {

        fld.policies.forEach(policy => {

          if (this.dataReceived.insurancePlanId == policy.insurancePlanId && this.dataReceived.cropYear == policy.cropYear) {

            transferFromGrowerContractYearId = policy.growerContractYearId
            return
            
          }
        })

        landUpdateType = LAND_UPDATE_TYPE.ADD_EXISTING_LAND
      }
    } 

    if (landUpdateType == "") {
      alert ("Cannot determine land update type")
      return
    }

    if (this.dataToSend.landData.fieldId > 0) {
      // it's a field that was found in the system 
      // the rollover endpoint should return plantings and comments 

      let url = this.appConfig.getConfig().rest["cirras_underwriting"]
      url = url +"/annualField/" + this.dataToSend.landData.fieldId + "/rolloverInventory"
      url = url + "?rolloverToCropYear=" +  this.dataReceived.cropYear 
      url = url + "&insurancePlanId=" +  this.dataReceived.insurancePlanId

      const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

      var self = this
      return lastValueFrom(this.http.get(url,httpOptions)).then((data: AnnualFieldRsrc) => {

        let plantings = []
        let uwComments = []

        if (data && data.plantings && data.plantings.length > 0 ) {
          plantings = data.plantings
        }

        if (data && data.uwComments && data.uwComments.length > 0) {
          uwComments = data.uwComments
        }

        self.dataToSend.landData.transferFromGrowerContractYearId = transferFromGrowerContractYearId
        self.dataToSend.landData.landUpdateType = landUpdateType
        self.dataToSend.landData.plantings = plantings
        self.dataToSend.landData.uwComments = uwComments
    
        // send the results to the main page
        this.dialogRef.close({event:'AddLand', data: self.dataToSend});
      })

    } else {
      // new land / field, no need to get plantings and comments 
      // just close the popup

      this.dataToSend.landData.transferFromGrowerContractYearId = null
      this.dataToSend.landData.landUpdateType = landUpdateType
      this.dataToSend.landData.plantings = []
      this.dataToSend.landData.uwComments = []

      // send the results to the main page
      this.dialogRef.close({event:'AddLand', data: this.dataToSend});
    }
  }

}
