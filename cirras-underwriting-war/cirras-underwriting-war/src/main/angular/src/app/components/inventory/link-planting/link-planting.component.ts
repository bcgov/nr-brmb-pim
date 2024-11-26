import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { AddFieldValidationRsrc, AnnualFieldRsrc, LinkedPlanting, UwContractListRsrc } from '@cirras/cirras-underwriting-api';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { InventoryContract, UwContract } from 'src/app/conversion/models';
import { makeTitleCase, setHttpHeaders } from 'src/app/utils';
import { INSURANCE_PLAN, LAND_UPDATE_TYPE, LINK_PLANTING_TYPE, PLANT_INSURABILITY_TYPE_CODE } from 'src/app/utils/constants';
import { getInventorySeededForagesObjForSave } from '../inventory-common';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { RemovePlantingDialogComponent } from './remove-planting-dialog/remove-planting-dialog.component';

export interface AddPlantingPopupData {
  fieldId: number;
  fieldLabel?: string;
  legalLandId?: number;
  otherLegalDescription? : string;
  cropYear?: number;
  policyId?: string;
  underSeededCropVarietyId: number;
  underSeededCropCommodityId?: number,
  underSeededCropCommodityType?: string,
  underSeededAcres: number;
  grainInventoryFieldGuid: string; // GUID of the grain inventory field record to be linked
  linkedPolicies? : Array<UwContract>;
  isFieldLinked? : boolean;
  isPlantingLinked? : boolean;
  policyNumber?: string;
  growerName?: string;
  linkedForageInventoryContractGuid ?: string;
  inventorySeededForageGuid? : string; // useful when removing the planting
}


@Component({
  selector: 'cirras-link-planting',
  templateUrl: './link-planting.component.html',
  styleUrls: ['./link-planting.component.scss']
})
export class LinkPlantingComponent implements OnInit {

  titleLabel = "Link Planting for Field"

  httpOptions; // = setHttpHeaders(this.tokenService.getOauthToken())

  dataReceived : AddPlantingPopupData;

  linkPlantingForm: UntypedFormGroup;

  uwContract: UwContract;

  validationMessages = <AddFieldValidationRsrc>{};

  showAddNewLinkedPlantingMsg = false;
  hasSaveStarted = false;

  constructor(
    public dialogRef: MatDialogRef<AddPlantingPopupData>,
    @Inject(MAT_DIALOG_DATA) public data: AddPlantingPopupData,
    private fb: UntypedFormBuilder,
    private tokenService: TokenService,
    private appConfig: AppConfigService, 
    private http: HttpClient,
    protected store: Store<RootState>, 
    protected dialog: MatDialog,  ) {  

      if (data) {

        this.httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

        //capture the data that comes from the main page
        this.dataReceived = data;

        if (this.dataReceived && this.dataReceived.policyNumber) {
          this.searchPolicy(this.dataReceived.policyNumber)
        }

        this.titleLabel = "Link Planting for Field " + 
        ( (this.dataReceived)  ?  
        ": " + this.dataReceived.fieldLabel + " (Field Id: " + this.dataReceived.fieldId + "), Location: " + this.dataReceived.otherLegalDescription
            : "" )
        
      } 
    }

  ngOnInit(): void {

    this.linkPlantingForm = this.fb.group({
      searchPolicyNumber:    [],
      selectedPolicyNumber: []
    })
  }

  onSelectPolicy(policyNumber) {
    if (!policyNumber) {
      return
    }

    this.uwContract = null
    this.showAddNewLinkedPlantingMsg = false

    if (policyNumber == -1) {
      // possibly show policy search, if it's hidden
    } else {
      this.searchPolicy(policyNumber)
    }
  }


  onPolicyValidate() {

    this.uwContract = null
    this.showAddNewLinkedPlantingMsg = false

    const searchPolicyNumber = this.linkPlantingForm.controls.searchPolicyNumber.value

    // start the search when 9 symbols are entered
    if (!searchPolicyNumber || searchPolicyNumber.length < 9) {
      return
    } 

    this.searchPolicy(searchPolicyNumber)
  }

  searchPolicy(searchPolicyNumber) {

    // uwcontracts?cropYear=2023&policyNumber=140913-23&sortColumn=policyNumber&sortDirection=ASC&pageNumber=1&pageRowCount=20
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/uwcontracts?cropYear=&policyNumber=" + encodeURI(searchPolicyNumber) + "&sortColumn=policyNumber&sortDirection=ASC&pageNumber=1&pageRowCount=20"

    this.http.get<UwContractListRsrc>(url, this.httpOptions).subscribe({
      next: (data : any) => {
          this.validatePolicy(data)
      },
      error: error => {
          this.handleApiError(error)
      }
    })
  }

  validatePolicy(data: UwContractListRsrc) {

    if (data && data.collection && data.collection.length == 1) {

      if (data.collection[0].insurancePlanId != INSURANCE_PLAN.FORAGE) {
        alert(" This policy is not a Forage policy. ")
        return
      } 

      if (data.collection[0].cropYear != this.dataReceived.cropYear) {
        alert(" This policy is from a different crop year.")
        return
      } 

      for (let i=0; i < data.collection[0].links.length; i++) {

        if ( data.collection[0].links[i].href.toLocaleLowerCase().indexOf("rolloverinventorycontract") > -1 ) {

          alert(" No inventory was found for that policy - the policy was rolled over but the inventory was not. " + 
                " Please search for the policy number on the home screen," + 
                " then go to the inventory screen and click Save.")
          return
        }
      }

      this.uwContract = data.collection[0];
      this.showAddNewLinkedPlantingMsg = true

    } else {
      alert ("The policy was not found") 
    }
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onCreateLink() {
    
    if (!this.uwContract) {
      alert("Please select a Forage policy.")
      return
    }

    // get the inventory link 
    let inventoryContractLink = this.getInventoryContractLink()

    if (!inventoryContractLink ) {
      // if for whatever reason the forage inventory link is missing
      alert ("Error: Could not find Forage Inventory. The planting was not linked.")
      return
    }    

    this.hasSaveStarted = true

    // call the api to get the forage inventory for that policy
    this.http.get<InventoryContract>(inventoryContractLink, {headers: this.httpOptions.headers, observe: "response"}).subscribe({
      next: resp => {
        let invContract = resp.body
        if (resp.headers && invContract) {
            invContract.etag = resp.headers.get("ETag")
          }
          
          this.processForageInventoryContractData(inventoryContractLink, invContract)
      },
      error: error => {
          this.handleApiError(error)
      }
    })
  }

  getInventoryContractLink() : string {
    for (let i=0; i < this.uwContract.links.length; i++) {

      if (  this.uwContract.links[i].href.toLocaleLowerCase().indexOf("inventorycontracts/" + this.uwContract.inventoryContractGuid ) > -1 ) {
        return this.uwContract.links[i].href
      }
    }
  }

  processForageInventoryContractData(inventoryContractLink: string, data: InventoryContract ) {

    if ( data ) {
    
      let fld = data.fields.find( f => f.fieldId == this.dataReceived.fieldId )

      if (fld) {
        let inventoryContractData = this.addFieldToForagInventoryContract(data, 1)
        // call update forage inventory api
        this.updateForageInventory(inventoryContractLink, inventoryContractData, 'LinkPlanting')
      } else {
        // the field does not exist on the Forage policy
        // run Add field validation , if no error message then save inventory
        this.validateAddField(inventoryContractLink, data)
      }

    } else {
      alert ("Forage inventory for that policy was not found and the field and planting cannot be linked")
    }
  }

  addFieldToForagInventoryContract(data, plantingNumberForNewFields ) { 
    // the plantingNumberForNewFields is the planting number for a field new to the forage policy
    // plantingNumberForNewFields = 1 by default 
    // but it could be larger if the field has inventory and it's not on a policy  

    let inventoryContract = JSON.parse(JSON.stringify(data));; // data is read only, take a full copy
        // add field (if it's not on the forage policy) and planting

        // add the planting
        let inventorySeededForages = []
 
        inventorySeededForages.push (
          getInventorySeededForagesObjForSave("newGUID_01", null, 
            this.dataReceived.underSeededCropCommodityId, this.dataReceived.underSeededCropVarietyId, this.dataReceived.underSeededCropCommodityType, 
            this.dataReceived.underSeededAcres, this.dataReceived.cropYear, null, false, false, PLANT_INSURABILITY_TYPE_CODE.Establishment1, false, 
            false, LINK_PLANTING_TYPE.ADD_LINK, this.dataReceived.grainInventoryFieldGuid)
        )

        let fld = inventoryContract.fields.find( f => f.fieldId == this.dataReceived.fieldId )

        if (fld) {
          // get the next planting number for field 
          let nextPlantingNumber = this.getNextPlantingNumber(fld);

          fld.plantings.push(
            this.createPlantingObjectForSave( nextPlantingNumber,  inventorySeededForages)
          )

        } else {
          // add the field + planting
          let tmpPlantings = []
          tmpPlantings.push(
            this.createPlantingObjectForSave(plantingNumberForNewFields, inventorySeededForages)
          )

          inventoryContract.fields.push({
            cropYear:                         this.dataReceived.cropYear,
            displayOrder:                     inventoryContract.fields.length + 1,
            fieldId:                          this.dataReceived.fieldId,
            fieldLabel:                       this.dataReceived.fieldLabel, 
            landUpdateType:                   LAND_UPDATE_TYPE.ADD_EXISTING_LAND,
            legalLandId:                      this.dataReceived.legalLandId ,
            otherLegalDescription:            this.dataReceived.otherLegalDescription,
            transferFromGrowerContractYearId: null,
            plantings:                        tmpPlantings,
            uwComments:                       [],
            '@type':                          'AnnualFieldRsrc'
          })
        }
        return inventoryContract
  }

  createPlantingObjectForSave( plantingNumber, inventorySeededForages) {
    return {
      cropYear:                   this.dataReceived.cropYear,
      fieldId:                    this.dataReceived.fieldId,
      insurancePlanId:            INSURANCE_PLAN.FORAGE,
      inventoryFieldGuid:         null,
      inventorySeededForages:     inventorySeededForages, // [],
      inventorySeededGrains:      [],
      inventoryUnseeded:          null,
      isHiddenOnPrintoutInd:      false,
      lastYearCropCommodityId:    null,
      lastYearCropCommodityName:  null,
      plantingNumber:             plantingNumber,
      underseededAcres:           this.dataReceived.underSeededAcres,
      underseededCropVarietyId:   this.dataReceived.underSeededCropVarietyId,
    }
  }

  validateAddField(inventoryContractLink: string, originalForageInventoryContractData: InventoryContract) {
    
    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    // "/uwcontracts/{policyId}/validateAddField"

    url = url + "/uwcontracts/" + this.uwContract.policyId + "/validateAddField"
    url = url + "?policyId=" +  this.uwContract.policyId 
    url = url + "&fieldId=" +  this.dataReceived.fieldId.toString()
    url = url + "&transferFromPolicyId=" // this doesn't need to be set

    this.http.get<InventoryContract>(url, this.httpOptions).subscribe({
      next: data => {
        
        if (this.hasAddFieldErrorMessages(data)) {
          // the error messages should already be on the screen
          // do not add the field to the policy 
          return

        } else {
          // the rollover endpoint should return plantings and comments 
          // in case the field has inventory but it's not on the forage policy
          this.getPlantingsForField(inventoryContractLink, originalForageInventoryContractData)
        }
      },
      error: error => {
          this.handleApiError(error)
      }
    })
  }

  hasAddFieldErrorMessages(data): boolean {
    this.validationMessages = data

        if (data.errorMessages && data.errorMessages.length > 0) {

          // we have to ignore one particular error message 
          for (let i=0; i< data.errorMessages.length ; i++) {

            if ( data.errorMessages[i].message == "This Field is already on this Policy. Please add a new planting instead." ) {
              return false 
            }
            
          }

          return true

        } else {
          // add the field and the planting to the forage policy
          return false
        }

  }

  getPlantingsForField(inventoryContractLink: string, originalForageInventoryContractData: InventoryContract) {
    // here we are looking for any existing forage plantings 

    let url = this.appConfig.getConfig().rest["cirras_underwriting"]
    url = url +"/annualField/" + this.dataReceived.fieldId.toString() + "/rolloverInventory"
    url = url + "?rolloverToCropYear=" +  this.dataReceived.cropYear 
    url = url + "&insurancePlanId=" +  INSURANCE_PLAN.FORAGE

    this.http.get<AnnualFieldRsrc>(url, this.httpOptions).subscribe({
      next: (data : any) => {
          // the rollover endpoint should return plantings and comments 
          // in case the field has inventory but it's not on the policy to which we want to add it 
          
          const maxPlantingNumber = this.getNextPlantingNumber(data)

          const inventoryContractData = this.addFieldToForagInventoryContract(originalForageInventoryContractData, maxPlantingNumber)

          // add the field and the planting to the forage policy
          this.updateForageInventory(inventoryContractLink, inventoryContractData, 'LinkPlanting')
        
      },
      error: error => {
          this.handleApiError(error)
      }
    })
  }

  getNextPlantingNumber(fieldData: AnnualFieldRsrc) : number {
    let maxPlantingNumber = 1

    if (fieldData && fieldData.plantings && fieldData.plantings.length > 0 ) {

      // find the max planting number
      for ( let i = 0; i < fieldData.plantings.length; i++ ) {

        if( maxPlantingNumber < fieldData.plantings[i].plantingNumber) {

          maxPlantingNumber = fieldData.plantings[i].plantingNumber

        }
      }

      maxPlantingNumber = maxPlantingNumber + 1

    }

    return maxPlantingNumber
  }

  updateForageInventory(inventoryContractLink, inventoryContractData, operationType) {

    let httpReqHeaders = {
     headers: new HttpHeaders({
        'If-Match': inventoryContractData.etag ? inventoryContractData.etag : ""
      })
    }

    this.http.put<any>(inventoryContractLink, inventoryContractData, httpReqHeaders)
      .subscribe({
          next: data => {

            // update left menu side navigation links
            this.store.dispatch(LoadGrowerContract(INVENTORY_COMPONENT_ID, this.dataReceived.policyId)) 

            if (operationType == 'LinkPlanting') {
              alert ("A new planting was successfully added to the field on the forage policy and linked to this grain planting.")
            } else {
              alert ("The link between the Forage and Grain planting was sucessfully removed")
            }
            
            this.dialogRef.close({event:'LinkPlanting'});
          },
          error: error => {
              console.error('There was an error!', error);
              alert("There was an error when trying to link the plantings. Error: " + error.message)
              // this.dialogRef.close({event:'Cancel'});
          }
      });
  }

  onRemoveLink() {

    if (!this.dataReceived.linkedForageInventoryContractGuid) {
      alert("The linked Forage policy number is missing")
      return
    }

    const dialogRef = this.dialog.open(RemovePlantingDialogComponent, {
      width: '600px',
      data: this.dataReceived.policyNumber
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'DeletePlanting'){

        this.processPlantingRemoval(true)

      } else if (result && result.event == 'KeepPlanting'){

        this.processPlantingRemoval(false)
      }
    });
    
  }

  processPlantingRemoval(deletePlanting) {

    let inventoryContractLink = this.appConfig.getConfig().rest["cirras_underwriting"]
    inventoryContractLink = inventoryContractLink + "/inventoryContracts/" + this.dataReceived.linkedForageInventoryContractGuid 

    // call the api to get the forage inventory for that policy
    this.http.get<InventoryContract>(inventoryContractLink, {headers: this.httpOptions.headers, observe: "response"}).subscribe({
      next: resp => {
        
        let respInvContract = resp.body
        if (resp.headers && respInvContract) {
          respInvContract.etag = resp.headers.get("ETag")
        }

        let inventoryContract = this.removeForagePlanting(respInvContract, deletePlanting)

        // call update forage inventory api
        this.updateForageInventory(inventoryContractLink, inventoryContract, 'RemoveLink')

      },
      error: error => {
          this.handleApiError(error)
      }
    })
  }

  removeForagePlanting(data, shouldDeletePlanting) {

    let inventoryContract = JSON.parse(JSON.stringify(data));; // data is read only, take a full copy

    let fld = inventoryContract.fields.find( f => f.fieldId == this.dataReceived.fieldId )

    if (fld) {
      // find the linked planting 
      let pltg

      for (let i=0; i < fld.plantings.length; i++) {
        let invSeededForage = fld.plantings[i].inventorySeededForages.find( el => el.inventorySeededForageGuid == this.dataReceived.inventorySeededForageGuid)

        if (invSeededForage) {
          pltg = fld.plantings[i]
        }
      }

      if (!pltg) {
        return
      }
      
      if ( shouldDeletePlanting == false ) {

        // just remove the link to the planting but don't delete
        pltg.inventorySeededForages[0].linkPlantingType = "REMOVE_LINK"
        pltg.inventorySeededForages[0].deletedByUserInd = false

      } else {
        // delete the planting, but first       
        // find out how many plantings does this forage field have
        let numOfPlantings = fld.plantings.length

        if (numOfPlantings > 1) {
          // mark planting for deletion
          pltg.inventorySeededForages[0].deletedByUserInd = true
          pltg.inventorySeededForages[0].linkPlantingType = "REMOVE_LINK"
        }

        if (numOfPlantings == 1) {
          // then just clear our the planting data but without removing it from the field
          pltg.inventorySeededForages[0].cropCommodityId = null
          pltg.inventorySeededForages[0].cropVarietyId = null
          pltg.inventorySeededForages[0].commodityTypeCode = null
          pltg.inventorySeededForages[0].fieldAcres = null 
          pltg.inventorySeededForages[0].seedingYear = null
          pltg.inventorySeededForages[0].seedingDate = null
          pltg.inventorySeededForages[0].isIrrigatedInd = false
          pltg.inventorySeededForages[0].isQuantityInsurableInd = false
          pltg.inventorySeededForages[0].plantInsurabilityTypeCode = null
          pltg.inventorySeededForages[0].isAwpEligibleInd = false
          pltg.inventorySeededForages[0].deletedByUserInd = false
          pltg.inventorySeededForages[0].linkPlantingType = "REMOVE_LINK"
        }
      }
    } 

    return inventoryContract
  }
  //////////////////////////////


  handleApiError(error) {
    console.error('There was an error!', error)
    alert("There was an error: " + error.message)

    this.hasSaveStarted = false
  }

  isAddNewLinkedPlantingMsgVisible() {
  
    if (this.showAddNewLinkedPlantingMsg &&  
       this.dataReceived && !this.dataReceived.isPlantingLinked) {

        return true
    } else {
      return false
    }
  }

}
