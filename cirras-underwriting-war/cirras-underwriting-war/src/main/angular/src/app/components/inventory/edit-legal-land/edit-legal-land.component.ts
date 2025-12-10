import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AnnualFieldRsrc, AnnualFieldListRsrc, LegalLandRsrc, RenameLegalValidationRsrc, ReplaceLegalValidationRsrc } from '@cirras/cirras-underwriting-api';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { convertToLegalLandList } from 'src/app/conversion/conversion-from-rest';
import { LegalLandList } from 'src/app/conversion/models';
import { setHttpHeaders } from 'src/app/utils';
import { INSURANCE_PLAN, LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { DIALOG_TYPE } from '../../dialogs/base-dialog/base-dialog.component';
import { AddLandPopupData } from '../add-field/add-field.component';
import { lastValueFrom } from 'rxjs';

@Component({
    selector: 'cirras-edit-land',
    templateUrl: './edit-legal-land.component.html',
    styleUrls: ['./edit-legal-land.component.scss'],
    standalone: false
})

export class EditLegalLandInInventoryComponent implements OnInit {
  titleLabel = "Edit Legal Location";
  dialogType = DIALOG_TYPE.INFO;

  dataReceived : AddLandPopupData;
  dataToSend : AddLandPopupData

  editLandForm: UntypedFormGroup;
  
  choiceExplanation = "";

  renameLegalLandList : RenameLegalValidationRsrc;

  replaceLegalLandList : ReplaceLegalValidationRsrc;
  legalLandList : LegalLandList = {};

  showSearchLegalMsg = false;
  showNewLegalLandMessage = false; 
  showProceedButton = false;

  constructor(
    public dialogRef: MatDialogRef<EditLegalLandInInventoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: AddLandPopupData,
    private fb: UntypedFormBuilder,
    private tokenService: TokenService,
    private appConfig: AppConfigService, 
    private http: HttpClient   ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;
        this.dataToSend = this.dataReceived 
      } 
    }

  ngOnInit(): void {

    if (this.dataReceived && this.dataReceived.insurancePlanId) {
      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
        this.titleLabel = "Edit Legal Location"
      }

      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
        this.titleLabel = "Edit PID"
      }
    }
    
    // add fields to the form
    this.editLandForm = this.fb.group({
      choiceSelected: ['rename'],
      searchLegal:    [],
      legalLandIdSelected: [],
    })

    this.onChoiceClick('rename')
  }

  getEditLegalLandTitle() {
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      return "Location " + this.dataReceived?.otherLegalDescription
    }

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return "PID " + this.dataReceived?.primaryPropertyIdentifier
    }
  }

  getSearchLegalLabel() {
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      return  "Legal Location"
    }

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return "PID"
    }
  }

  onChoiceClick( choice ) {
    this.choiceExplanation = ""

    this.clearAllForm()
  
    if (choice == "replace") 
    {
      this.choiceExplanation = "The change would affect only this field, in only this crop year. " + 
      "It is intended to change the legal location from one that was valid in prior years to a new one that is valid in this crop year."

    }

    if (choice == "rename") 
    {
      this.choiceExplanation = 
        "This change will be applied to all fields associated with that legal land, for all years. " +
        "It is intended to fix a typo on a legal location that was never valid. This change will take place when the inventory is saved."
    }

  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onLegalLandValidate() {

    let searchLegal = this.editLandForm.controls.searchLegal.value
    
    if (searchLegal) {
      searchLegal = searchLegal.trim()
    }

    // start the search when least 5 symbols are entered
    if (!searchLegal || searchLegal.length < 3) {
  
      this.showSearchLegalMsg = true
      return
    } else {
      this.showSearchLegalMsg = false
    }
    
    if (this.editLandForm.controls.choiceSelected.value == 'rename' ) {
      this.validateRenameLegalLand(searchLegal)
    }

    if (this.editLandForm.controls.choiceSelected.value == 'replace' ) {
      this.editLandForm.controls.legalLandIdSelected.setValue("")
      this.legalLandSearch(searchLegal)
    }    

  }

  validateRenameLegalLand(searchLegal) {

    // /uwcontracts/{policyId}/validateRenameLegal
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/uwcontracts/" + this.dataReceived.policyId + "/validateRenameLegal" 
    url = url + "?policyId=" + this.dataReceived.policyId
    url = url + "&annualFieldDetailId=" + this.dataReceived.annualFieldDetailId
    
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      url = url + "&newLegalLocation=" + encodeURI(searchLegal) 
      url = url + "&primaryPropertyIdentifier=" 
    }
    
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      url = url + "&newLegalLocation=" 
      url = url + "&primaryPropertyIdentifier=" + encodeURI(searchLegal) 
    }

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: RenameLegalValidationRsrc) => {

      self.renameLegalLandList = data
      this.showProceedButton = true
     })
  }


  legalLandSearch(searchLegal) {

    let url = this.appConfig.getConfig().rest["cirras_underwriting"]

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      url = url +"/legallands?legalLocation=" + encodeURI(searchLegal) + "&isWildCardSearch=true&searchByLegalLocOrLegalDesc=true" 
    }
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      url = url +"/legallands?primaryPropertyIdentifier=" + encodeURI(searchLegal) + "&isWildCardSearch=false&searchByLegalLocOrLegalDesc=false" 
    }

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: LegalLandRsrc) => {
      self.legalLandList = convertToLegalLandList(data)

      if (self.legalLandList && self.legalLandList.collection && self.legalLandList.collection.length > 0) {
        this.showNewLegalLandMessage = false
        // this.showProceedButton = false
      } else {
        // give the option to add new legal land
        this.showNewLegalLandMessage = true
        this.validateReplaceLegalLand(-1) // no legal land was found
        // this.showProceedButton = true
      }

     })
  }

  validateReplaceLegalLand(legalLandId) {      

    // /uwcontracts/{policyId}/validateReplaceLegal
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/uwcontracts/" + this.dataReceived.policyId + "/validateReplaceLegal" 
    url = url + "?policyId=" + this.dataReceived.policyId
    url = url + "&annualFieldDetailId=" + this.dataReceived.annualFieldDetailId
    url = url + "&legalLandId=" + ((legalLandId > -1) ? legalLandId : "" )
    url = url + "&fieldLabel=" + encodeURI(this.dataReceived.fieldLabel)   
    url = url + "&fieldLocation=" + encodeURI(this.dataReceived.fieldLocation)  
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: ReplaceLegalValidationRsrc) => {

      self.replaceLegalLandList = data
         
      this.showProceedButton = true
    })
  }

  onProceed(){

    const choiceSelected = this.editLandForm.controls.choiceSelected.value

    let eventType = ""
    let landUpdateType = ""

    if ( choiceSelected == 'rename') {
      eventType = 'RenameLand'
      landUpdateType = LAND_UPDATE_TYPE.RENAME_LEGAL_LOCATION 
    }

    if ( choiceSelected == 'replace') {
      eventType = 'ReplaceLand'

      if (this.dataToSend.landData.legalLandId && this.dataToSend.landData.legalLandId > -1) {
        landUpdateType = LAND_UPDATE_TYPE.REPLACE_LEGAL_LOCATION_EXISTING
      } else {
        landUpdateType = LAND_UPDATE_TYPE.REPLACE_LEGAL_LOCATION_NEW
      }
    }

    if (eventType == "" || landUpdateType == "") {
      alert ("Cannot determine event type or land update type")
      return
    }

    if ( choiceSelected == 'rename') {
      this.dataToSend.landData.legalLandId = null

      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
        this.dataToSend.landData.otherLegalDescription = (this.editLandForm.controls.searchLegal.value).trim()
      }

      if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES) {
        this.dataToSend.landData.primaryPropertyIdentifier = (this.editLandForm.controls.searchLegal.value).trim()
      }
    } 

    if ( choiceSelected == 'replace') {
      // acount for the case when replacing with new legal land
      if (this.dataToSend.landData.legalLandId == null || this.dataToSend.landData.legalLandId == -1) {
        this.setNewLegalLand()
      } 
      // if replacing with existing legal land then the params legalLandId, otherLegalDescription, and primaryPropertyIdentifier should already be set in onLegalLandReceived
    }

    this.dataToSend.landData.fieldId = this.dataReceived.fieldId
    this.dataToSend.landData.landUpdateType = landUpdateType

    // send the results to the main page
    this.dialogRef.close({event: eventType, data: this.dataToSend});

  }

  clearAllForm() {
    this.showSearchLegalMsg = false;
    this.showNewLegalLandMessage = false;
    this.showProceedButton = false;

    this.renameLegalLandList = null;
    //this.fieldList = <AnnualFieldListRsrc>{};
    this.replaceLegalLandList = null;
    this.legalLandList = {};

    this.dataToSend.landData.legalLandId = null
    this.dataToSend.landData.primaryPropertyIdentifier = null
    this.dataToSend.landData.otherLegalDescription = null
  }

  onLegalLandReceived(legalLand) { 
    this.dataToSend.landData.legalLandId = legalLand.legalLandId

    if (legalLand.legalLandId > -1 ) {

      this.dataToSend.landData.primaryPropertyIdentifier = legalLand.primaryPropertyIdentifier
      this.dataToSend.landData.otherLegalDescription = legalLand.otherLegalDescription
      this.validateReplaceLegalLand(legalLand.legalLandId) 
      
    } else { 
      // new legal land - allow Proceed
      this.showProceedButton = true;
    }
  }

  setNewLegalLand() {
    this.dataToSend.landData.legalLandId = -1

    let searchLegal = (this.editLandForm.controls.searchLegal.value).trim()
    
    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN || this.dataReceived.insurancePlanId == INSURANCE_PLAN.FORAGE) {
        this.dataToSend.landData.otherLegalDescription = searchLegal
    }

    if (this.dataReceived.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
        this.dataToSend.landData.primaryPropertyIdentifier = searchLegal
    }

  }


}
