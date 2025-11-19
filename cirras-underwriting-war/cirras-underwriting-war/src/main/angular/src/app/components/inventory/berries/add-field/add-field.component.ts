import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AnnualFieldListRsrc, LegalLandRsrc } from '@cirras/cirras-underwriting-api';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';
import { LegalLandList } from 'src/app/conversion/models';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { setHttpHeaders } from 'src/app/utils';
import { lastValueFrom } from 'rxjs';
import { convertToLegalLandList } from 'src/app/conversion/conversion-from-rest';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

export function getSearchFieldUrl(cropYear, legalLandId, fieldId, fieldLocation){
    
  let url = "/annualFields?legalLandId=" + legalLandId.toString()
  url = url +"&fieldId=" + fieldId.toString()
  url = url +"&fieldLocation=" + fieldLocation
  url = url +"&cropYear=" +  cropYear.toString()

  return url
}

@Component({
  selector: 'add-field',
  templateUrl: './add-field.component.html',
  styleUrl: './add-field.component.scss',
  standalone: false
})
export class AddFieldComponent implements OnInit{

  titleLabel = "Add Field";

  dialogType = DIALOG_TYPE.INFO;

  dataReceived // : AddLandPopupData;

  deafultSearchChoices = [
    { name: 'Legal Location', value: 'searchLegalLocation' , visible: [INSURANCE_PLAN.GRAIN, INSURANCE_PLAN.FORAGE] },
    { name: 'PID', value: 'searchPID' , visible: [INSURANCE_PLAN.BERRIES] },
    { name: 'Field Location', value: 'searchFieldLocation' , visible: [INSURANCE_PLAN.BERRIES] },
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
  }

  onTypeInSearchBox() {

    this.clearAllForm()

    const searchLegalLandOrFieldId = this.addFieldForm.controls.searchLegalLandOrFieldId.value

    // start the search when least 3 symbols are entered
    if (!searchLegalLandOrFieldId || searchLegalLandOrFieldId.length < 3) {
      if (this.addFieldForm.controls.choiceSelected.value == 'searchFieldLocation') {
        this.getFields(this.dataReceived.cropYear, "", "", searchLegalLandOrFieldId)
      }
    }
  }

  onSearch() {
    const searchLegalLandOrFieldId = this.addFieldForm.controls.searchLegalLandOrFieldId.value

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
      url = url +"/legallands?primaryPropertyIdentifier=" + encodeURI(searchLegalLandOrFieldId) + "&isWildCardSearch=true" 
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
        this.showNewLegalLandMessage = true
        this.showProceedButton = true
      }
    })
  }

  getFields(cropYear, legalLandId, fieldId, fieldLocation){
    
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url + getSearchFieldUrl(cropYear, legalLandId, fieldId, fieldLocation)

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: AnnualFieldListRsrc) => {
      self.fieldList = data;

      // TODO 
      // this.validateFields(self.fieldList.collection[0])
    })
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }
}
