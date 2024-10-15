import { ChangeDetectorRef } from "@angular/core"
import { FormArray, FormBuilder, FormControl } from "@angular/forms"
import { InventorySeededForage, InventorySeededGrain, InventoryUnseeded, UnderwritingComment } from "@cirras/cirras-underwriting-api"
import { AnnualField, CropVarietyCommodityType } from "src/app/conversion/models"
import { CROP_COMMODITY_UNSPECIFIED, INSURANCE_PLAN } from "src/app/utils/constants"
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop";
import { AddLandComponent, AddLandPopupData } from "./add-land/add-land.component";
import { MatDialog } from "@angular/material/dialog";
import { EditLandComponent } from "./edit-land/edit-land.component";
import { addUwCommentsObject, getUniqueKey } from 'src/app/utils';
import { RemoveFieldComponent, RemoveFieldPopupData } from "./remove-field/remove-field.component"

export interface CropVarietyOptionsType {
  cropCommodityId: string;
  cropVarietyId: string;
  varietyName: string;
  cropVarietyCommodityTypes?: CropVarietyCommodityType;
}

export interface CropCommodityVarietyOptionsType {
  cropCommodityVarietyId: string;
  cropCommodityVarietyName: string;
}

export function addPlantingObject(cropYear, fieldId, insurancePlanId, inventoryFieldGuid, lastYearCropCommodityId, 
                  lastYearCropCommodityName, lastYearCropVarietyId, lastYearCropVarietyName, plantingNumber, isHiddenOnPrintoutInd, underseededInventorySeededForageGuid,
                  inventoryUnseeded: InventoryUnseeded, inventorySeededGrains: FormArray, inventorySeededForages: FormArray,) {

  return {
    cropYear:                  [ cropYear ],
    fieldId:                   [ fieldId ],
    insurancePlanId:           [ insurancePlanId ],
    inventoryFieldGuid:        [ inventoryFieldGuid ],
    lastYearCropCommodityId:   [ lastYearCropCommodityId ], 
    lastYearCropCommodityName: [ lastYearCropCommodityName ], 
    lastYearCropVarietyId:     [ lastYearCropVarietyId ],
    lastYearCropVarietyName:   [ lastYearCropVarietyName ],
    lastYearCropCommodityVarietyId: [ `${lastYearCropCommodityId||0}_${lastYearCropVarietyId||0}` ],
    lastYearCropCommodityVarietyName: [ lastYearCropVarietyName || lastYearCropCommodityName ],
    plantingNumber:            [ plantingNumber ],
    acresToBeSeeded:           [ (inventoryUnseeded && inventoryUnseeded.acresToBeSeeded) ? inventoryUnseeded.acresToBeSeeded : '' ],
    // to be consistent with the last year's crop
    cropCommodityId:           [ (inventoryUnseeded && inventoryUnseeded.cropCommodityId) ? inventoryUnseeded.cropCommodityId : CROP_COMMODITY_UNSPECIFIED.ID ],
    cropCommodityName:         [ (inventoryUnseeded && inventoryUnseeded.cropCommodityName) ? inventoryUnseeded.cropCommodityName : CROP_COMMODITY_UNSPECIFIED.NAME ],
    cropVarietyId:             [ (inventoryUnseeded && inventoryUnseeded.cropVarietyId) ? inventoryUnseeded.cropVarietyId : CROP_COMMODITY_UNSPECIFIED.ID ],
    cropVarietyName:           [ (inventoryUnseeded && inventoryUnseeded.cropVarietyName) ? inventoryUnseeded.cropVarietyName : CROP_COMMODITY_UNSPECIFIED.NAME ],
    cropCommodityVarietyId:    [ `${inventoryUnseeded.cropCommodityId||0}_${inventoryUnseeded.cropVarietyId||0}` ],
    cropCommodityVarietyName:  [ inventoryUnseeded.cropVarietyName ?  inventoryUnseeded.cropVarietyName : inventoryUnseeded.cropCommodityName ],
    deletedByUserInd:          [ (inventoryUnseeded && inventoryUnseeded.deletedByUserInd) ? inventoryUnseeded.deletedByUserInd : false ],
    isUnseededInsurableInd:    [ (!inventoryUnseeded || inventoryUnseeded.isUnseededInsurableInd == undefined) ? true : inventoryUnseeded.isUnseededInsurableInd], // defaults to true
    isHiddenOnPrintoutInd:     [ isHiddenOnPrintoutInd ],
    inventoryUnseededGuid:     [ (inventoryUnseeded && inventoryUnseeded.inventoryUnseededGuid) ? inventoryUnseeded.inventoryUnseededGuid : null ] ,
    inventorySeededGrains:     [ inventorySeededGrains ],
    inventorySeededForages:    [ inventorySeededForages ],
    underseededInventorySeededForageGuid: [ underseededInventorySeededForageGuid ]
  }
}

export function addAnnualFieldObject (field: AnnualField, fldPlantings: FormArray, fldComments: Array<UnderwritingComment>) {
  return {
    annualFieldDetailId:   [ field.annualFieldDetailId ],
    displayOrder:          [ field.displayOrder ],
    fieldId:               [ field.fieldId ],
    fieldLabel:            [ field.fieldLabel ],
    landUpdateType:        [],
    legalLandId:           [ field.legalLandId ],
    otherLegalDescription: [ field.otherLegalDescription ],
    transferFromGrowerContractYearId: [],
    plantings:             [ fldPlantings ],
    uwComments:            [ fldComments ],
    isNewFieldUI:          [ false ],
    deletedByUserInd:      [ false ]
  }
}

export function addSeededGrainsObject(inventoryFieldGuid, underseededAcres, underseededCropVarietyId, underseededVarietyName, isHiddenOnPrintoutInd, inventorySeededGrains: InventorySeededGrain) {
  return {
    inventorySeededGrainGuid: [ (inventorySeededGrains && inventorySeededGrains.inventorySeededGrainGuid ) ? 
                                  inventorySeededGrains.inventorySeededGrainGuid : 
                                  "newGUID_" + getUniqueKey().toString() ] , // create a local unique key which won't be sent to the backend
    inventoryFieldGuid:       [ (inventorySeededGrains && inventorySeededGrains.inventoryFieldGuid) ? inventorySeededGrains.inventoryFieldGuid : inventoryFieldGuid ],
    cropCommodityId:          [ (inventorySeededGrains && inventorySeededGrains.cropCommodityId) ? inventorySeededGrains.cropCommodityId : CROP_COMMODITY_UNSPECIFIED.ID ],
    cropCommodityName:        [ (inventorySeededGrains && inventorySeededGrains.cropCommodityName) ? inventorySeededGrains.cropCommodityName : CROP_COMMODITY_UNSPECIFIED.NAME ],
    cropVarietyCtrl:          [ { 
                                  cropVarietyId: ( inventorySeededGrains && inventorySeededGrains.cropVarietyId) ? inventorySeededGrains.cropVarietyId : CROP_COMMODITY_UNSPECIFIED.ID, 
                                  varietyName: ( inventorySeededGrains && inventorySeededGrains.cropVarietyName) ? inventorySeededGrains.cropVarietyName : CROP_COMMODITY_UNSPECIFIED.NAME
                                } ],
    commodityTypeCode:        [ (inventorySeededGrains && inventorySeededGrains.commodityTypeCode) ? inventorySeededGrains.commodityTypeCode : null],
    commodityTypeDesc:        [ (inventorySeededGrains && inventorySeededGrains.commodityTypeDesc) ? inventorySeededGrains.commodityTypeDesc : '' ],
    commodityTypeOptions:     [ ], 
    isQuantityInsurableInd:   [ ( !inventorySeededGrains || !inventorySeededGrains.isQuantityInsurableInd ) ? false : inventorySeededGrains.isQuantityInsurableInd ],
    isReplacedInd:            [ ( !inventorySeededGrains || !inventorySeededGrains.isReplacedInd) ? false : inventorySeededGrains.isReplacedInd ],
    isPedigreeInd:            [ ( !inventorySeededGrains || !inventorySeededGrains.isPedigreeInd ) ? false : inventorySeededGrains.isPedigreeInd ],
    seededDate:               new FormControl(new Date( (inventorySeededGrains && inventorySeededGrains.seededDate) ? inventorySeededGrains.seededDate : '' )),
    seededAcres:              [ (inventorySeededGrains && inventorySeededGrains.seededAcres) ? inventorySeededGrains.seededAcres : null],
    isSpotLossInsurableInd:   [ ( !inventorySeededGrains || !inventorySeededGrains.isSpotLossInsurableInd ) ? false : inventorySeededGrains.isSpotLossInsurableInd ], 
    isHiddenOnPrintoutInd:    [ isHiddenOnPrintoutInd ],
    // these two controls are needed to accomodate UNDERSEEDED information in inventorySeededGrains form in  GrainSeededInventoryComponent
    underSeededVrtyCtrl:      [ { 
                                  cropVarietyId:  underseededCropVarietyId ? underseededCropVarietyId : CROP_COMMODITY_UNSPECIFIED.ID, 
                                  varietyName:    underseededVarietyName ? underseededVarietyName : CROP_COMMODITY_UNSPECIFIED.NAME 
                                } ],
    underSeededAcres:         [ underseededAcres ], 
    deletedByUserInd:         [false], 
  }
}

export function addSeededForagesObject(inventoryFieldGuid, isHiddenOnPrintoutInd, acresToBeSeeded, isUnseededInsurableInd, inventorySeededForage: InventorySeededForage) {
  return {
    inventorySeededForageGuid: [ (inventorySeededForage && inventorySeededForage.inventorySeededForageGuid ) ? 
                                  inventorySeededForage.inventorySeededForageGuid : 
                                  "newGUID_" + getUniqueKey().toString() ] , // create a local unique key which won't be sent to the backend
    inventoryFieldGuid:       [ (inventorySeededForage && inventorySeededForage.inventoryFieldGuid) ? inventorySeededForage.inventoryFieldGuid : inventoryFieldGuid ],
    cropCommodityId:          [ (inventorySeededForage && inventorySeededForage.cropCommodityId) ? inventorySeededForage.cropCommodityId : CROP_COMMODITY_UNSPECIFIED.ID ],
    cropVarietyCtrl:          [ { 
                                  cropVarietyId: ( inventorySeededForage && inventorySeededForage.cropVarietyId) ? inventorySeededForage.cropVarietyId : CROP_COMMODITY_UNSPECIFIED.ID, 
                                  varietyName: ( inventorySeededForage && inventorySeededForage.cropVarietyName) ? inventorySeededForage.cropVarietyName : CROP_COMMODITY_UNSPECIFIED.NAME
                                } ],
    commodityTypeCode:        [ (inventorySeededForage && inventorySeededForage.commodityTypeCode) ? inventorySeededForage.commodityTypeCode : null],
    fieldAcres:               [ (inventorySeededForage && inventorySeededForage.fieldAcres) ? inventorySeededForage.fieldAcres : null],
    seedingYear:              [ (inventorySeededForage && inventorySeededForage.seedingYear) ? inventorySeededForage.seedingYear : null],
    seedingDate:              new FormControl(new Date( (inventorySeededForage && inventorySeededForage.seedingDate) ? inventorySeededForage.seedingDate : '' )),
    isIrrigatedInd:           [ (!inventorySeededForage || !inventorySeededForage.isIrrigatedInd) ? false : inventorySeededForage.isIrrigatedInd ],
    isQuantityInsurableInd:   [ (!inventorySeededForage || !inventorySeededForage.isQuantityInsurableInd ) ? false : inventorySeededForage.isQuantityInsurableInd ],
    plantInsurabilityTypeCode:[ (inventorySeededForage && inventorySeededForage.plantInsurabilityTypeCode) ? inventorySeededForage.plantInsurabilityTypeCode : null],
    isAwpEligibleInd:            [ ( !inventorySeededForage || !inventorySeededForage.isAwpEligibleInd ) ? false : inventorySeededForage.isAwpEligibleInd ], 
    isHiddenOnPrintoutInd:    [ isHiddenOnPrintoutInd ],
    deletedByUserInd:         [false], 
    acresToBeSeeded:          [ ( acresToBeSeeded) ? acresToBeSeeded : '' ],
    isUnseededInsurableInd:   [ (isUnseededInsurableInd == undefined ) ? false : isUnseededInsurableInd],
    linkPlantingType:         [ ]
  }
}

export function getInventorySeededForagesObjForSave(inventorySeededForageGuid, inventoryFieldGuid, cropCommodityId, cropVarietyId, commodityTypeCode,
  fieldAcres, seedingYear, seedingDate, isIrrigatedInd, isQuantityInsurableInd, plantInsurabilityTypeCode, isAwpEligibleInd,  deletedByUserInd,
  linkPlantingType, grainInventoryFieldGuid) {

  if (inventorySeededForageGuid.indexOf("newGUID_") > -1) {
    inventorySeededForageGuid = null      
  }

  return {
    inventorySeededForageGuid:  inventorySeededForageGuid,
    inventoryFieldGuid:         inventoryFieldGuid,
    cropCommodityId:            cropCommodityId,
    cropVarietyId:              cropVarietyId,
    commodityTypeCode:          commodityTypeCode, 
    fieldAcres:                 fieldAcres, 
    seedingYear:                seedingYear,
    seedingDate:                seedingDate,
    isIrrigatedInd:             isIrrigatedInd,
    isQuantityInsurableInd:     isQuantityInsurableInd,
    plantInsurabilityTypeCode:  plantInsurabilityTypeCode,
    isAwpEligibleInd:           isAwpEligibleInd,
    deletedByUserInd:           deletedByUserInd,
    linkPlantingType:           linkPlantingType,
    grainInventoryFieldGuid:    grainInventoryFieldGuid,
  }
}

export function roundUpDecimalAcres(acres) {
  if (!acres) {
    return ""
  }

  if (isNaN(parseFloat(acres))) {
    alert ("Acres must be a valid number" )
  } else {

    if (parseFloat(acres) % 1 == 0 ) {
      // return integer if it's an integer, no zeros after the decimal point
      return parseInt(acres)
    }
    
    return parseFloat(acres).toFixed(1)
  }
}

export function navigateUpDownTextbox(event, jumps) {

  if (event.key === "ArrowUp" || event.key === "ArrowDown") {

    const currentElementId = event.target.id
    if ( currentElementId.lastIndexOf("-")== -1 ) return
    
    let elemIndex = parseInt(currentElementId.substr(currentElementId.lastIndexOf("-") + 1))
    if (isNaN(elemIndex))  return

    if (event.key === "ArrowUp") {
      elemIndex = elemIndex - jumps
    }
    
    if (event.key === "ArrowDown") {
      elemIndex = elemIndex + jumps
    }

    const nextElementId = currentElementId.substr(0, currentElementId.lastIndexOf("-") ) + '-' + elemIndex.toString()
          
    if (document.getElementById(nextElementId)) {
      let nextElem = document.getElementById(nextElementId) as HTMLInputElement
      nextElem.focus();
      nextElem.select();
    }
  }
}

export function isThereAnyCommentForField(field) {
  // Checks if there are un-deleted comments
  if (field.value.uwComments && field.value.uwComments.length > 0) {

    for (let i = 0; i < field.value.uwComments.length; i++) {
      if (field.value.uwComments[i].deletedByUserInd == true) {

      } else {
        return true
      }
    }
    
  }
  return false
}

export function updateComments(fieldId: number, uwComments: UnderwritingComment[], flds: FormArray) {

  flds.controls.forEach( function(field : FormArray) {

    if (field.value.fieldId == fieldId) {
        
      if (uwComments) {
        
        let fldComments = [] 

        uwComments.forEach ( (comment : UnderwritingComment) => fldComments.push ( 
          addUwCommentsObject( comment )
        ))

        field.controls["uwComments"].setValue( fldComments )

      }

    }
  })
}

export function deleteNewFormField(field, flds) {
  if (field.value.isNewFieldUI) {

    let removedFieldDisplOrder = 99999;
    let indexToSplice = -1

    // delete this field and plantings and comments if they exist
    //const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    for ( let i = 0; i < flds.length; i++ ) {

      let dispOrder = flds['controls'][i].get("displayOrder").value

      if (flds['controls'][i].get("fieldId").value  ==  field.value.fieldId) {
        
        removedFieldDisplOrder = flds['controls'][i].get("displayOrder").value
        indexToSplice = i;
      }

      // recalc field order and field id for the fields that are after the removed field
      if ( dispOrder > removedFieldDisplOrder) {

        let dispOrder = flds['controls'][i].get("displayOrder").value
        flds['controls'][i].get("displayOrder").setValue( dispOrder - 1 )
      }
    }

    //actually remove the field
    if ( indexToSplice >= 0 ) {
      flds.controls.splice(indexToSplice,1) //removes the field 
      flds.value.splice(indexToSplice,1)
    }
  }
}

export function dragField(event: CdkDragDrop<string[]>, fields: FormArray) {

  if (event.previousIndex == event.currentIndex) {
    return;
  }

  const newDisplayOrder = fields['controls'][event.currentIndex].value.displayOrder

  fields['controls'][event.previousIndex]['controls'].displayOrder.setValue(newDisplayOrder)

  // move the row
  moveItemInArray(fields['controls'], event.previousIndex, event.currentIndex);

  // update the display order of the fields between the previous and current index
  if (event.previousIndex < event.currentIndex) { // the field was dragged down

    for (let i = event.previousIndex; i < event.currentIndex; i++) {

      let tmpDisplayOrder = fields['controls'][i].value.displayOrder
      fields['controls'][i]['controls'].displayOrder.setValue( tmpDisplayOrder - 1)  

    }
  }

  if (event.previousIndex > event.currentIndex) {// the field was dragged up

    for (let i = event.currentIndex + 1; i < event.previousIndex + 1; i++) {

      let tmpDisplayOrder = fields['controls'][i].value.displayOrder
      fields['controls'][i]['controls'].displayOrder.setValue( tmpDisplayOrder + 1) 

    }
  }

}

// Land Management
export function AddNewFormField(fb: FormBuilder, flds: FormArray, dialog: MatDialog, cropYear, policyId, insurancePlanId,  cdr: ChangeDetectorRef) {

      // find the max display_order
      let maxDisplayOrder = 0; // field order should start with 1
      let minFieldId = 0; // new fields should have negative field ids
  
      flds.controls.forEach( function(fld : FormArray) {
    
        if (fld.controls['displayOrder'].value > maxDisplayOrder) {
          maxDisplayOrder = fld.controls['displayOrder'].value
        }
  
        if (fld.controls['fieldId'].value < minFieldId) {
          minFieldId = fld.controls['fieldId'].value
        }
  
      });
   
      // creating a fake field id, so I can attach comments and plantings
      const dataToSend : AddLandPopupData = {
          fieldId: minFieldId - 1,
          fieldLabel: "",
          cropYear: cropYear,
          policyId: policyId,
          insurancePlanId: insurancePlanId,
          annualFieldDetailId: null,
          otherLegalDescription: ""
        }
  
      openAddEditLandPopup(fb, flds, dialog, dataToSend, maxDisplayOrder + 1, true, cdr)
}
export function openAddEditLandPopup(fb: FormBuilder, flds: FormArray,dialog: MatDialog, dataToSend: AddLandPopupData , displayOrder, isNewFieldUI, cdr: ChangeDetectorRef) {

  // open up the popup to get the legal land and field
  // const dataToSend : AddLandPopupData = {
  //   fieldId: fieldId,
  //   fieldLabel: fieldLabel,
  //   cropYear: this.cropYear,
  //   policyId: this.policyId,
  //   insurancePlanId: this.insurancePlanId,
  //   annualFieldDetailId: annualFieldDetailId ? annualFieldDetailId : null,
  //   otherLegalDescription: otherLegalDescription
  // }

  let dialogRef

  if (isNewFieldUI) {

    dialogRef = dialog.open(AddLandComponent , {
      width: '800px',
      data: dataToSend
    });

  } else {

    dialogRef = dialog.open(EditLandComponent, {
      width: '1110px',
      data: dataToSend,
      autoFocus: false // if you remove this line of code then the first radio button would be selected
    });
  }
  
  dialogRef.afterClosed().subscribe(result => {
  
    if (result && result.event == 'AddLand'){

      // add new land
      if (result.data && result.data.landData) {
        populateNewLand(fb, cdr, flds, result.data.landData, displayOrder, dataToSend.cropYear, dataToSend.insurancePlanId)
      }

    } else if (result && result.event == 'RenameLand'){
      
      // Rename legal land 
      if (result.data && result.data.fieldId && result.data.landData) {
        renameLegalLand(flds, result.data.fieldId, result.data.landData, cdr)
      }
    } else if (result && result.event == 'ReplaceLand'){
      // Replace  legal land
      replaceLegalLand(flds, result.data.fieldId, result.data.landData, cdr)

    } else if (result && result.event == 'Cancel'){
      // do nothing
    }
  });
}

export function populateNewLand( fb: FormBuilder, cdr: ChangeDetectorRef, flds: FormArray, landData, displayOrder, cropYear, insurancePlanId) {

  // var self = this
  // const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    // Now add the new field to the form
  let fldPlantings = new FormArray ([]) 

  let pltgInventorySeededGrains = new FormArray ([])

  let pltgInventorySeededForages =  new FormArray ([])

  if (landData.plantings && landData.plantings.length > 0 ) {
  
    // add existing plantings
    landData.plantings.forEach ( pltg => {

      let pltgInventorySeededGrains = new FormArray ([])

      let pltgInventorySeededForages =  new FormArray ([])

      // add inventory seeded grains if any
      if (pltg.inventorySeededGrains && pltg.inventorySeededGrains.length > 0) {
        // for each inventory seeded grain within the planting object
        pltg.inventorySeededGrains.forEach(item => {

          // Add seededGrainInventory
          pltgInventorySeededGrains.push( fb.group( 
            addSeededGrainsObject(pltg.inventoryFieldGuid, pltg.underseededAcres, pltg.underseededCropVarietyId, pltg.underseededCropVarietyName, false, item)
          ) )

        })

      } else {
        // no seeded grain inventory - prepare empty inventory seeded grain form controls
        pltgInventorySeededGrains.push( fb.group( 
          addSeededGrainsObject(pltg.inventoryFieldGuid, null, null, null, false, <InventorySeededGrain>[])
        ) )
      }
      
      // add inventory seeded forages if any
      if (pltg.inventorySeededForages && pltg.inventorySeededForages.length > 0) {
        // for each inventory seeded grain within the planting object
        pltg.inventorySeededForages.forEach(item => {
          
          // Add seededForageInventory
          pltgInventorySeededForages.push( fb.group( 
            addSeededForagesObject (pltg.inventoryFieldGuid, pltg.isHiddenOnPrintoutInd, 
                                    ( pltg.inventoryUnseeded ? pltg.inventoryUnseeded.acresToBeSeeded : null), 
                                    ( pltg.inventoryUnseeded ? pltg.inventoryUnseeded.isUnseededInsurableInd : false ), 
                                    item)
          ) )

        })


      } else {
        // no seeded grain inventory - prepare empty inventory seeded grain form controls
        pltgInventorySeededForages.push( fb.group( 
          addSeededForagesObject(pltg.inventoryFieldGuid, pltg.isHiddenOnPrintoutInd, 
                                ( pltg.inventoryUnseeded ? pltg.inventoryUnseeded.acresToBeSeeded : null), 
                                ( pltg.inventoryUnseeded ? pltg.inventoryUnseeded.isUnseededInsurableInd : false ), 
                                <InventorySeededForage>[])
        ) )

      }

      // Add the plantings to the newly created or transferred field
      fldPlantings.push( fb.group( addPlantingObject( pltg.cropYear, pltg.fieldId, pltg.insurancePlanId, pltg.inventoryFieldGuid, 
        pltg.lastYearCropCommodityId, pltg.lastYearCropCommodityName, pltg.lastYearCropCommodityVarietyId, pltg.lastYearCropCommodityVarietyName,
        pltg.plantingNumber, pltg.isHiddenOnPrintoutInd, 
        pltg.underseededInventorySeededForageGuid,
        pltg.inventoryUnseeded,  pltgInventorySeededGrains, pltgInventorySeededForages ) ))

    } )

  } else {

    // add empty inventory seeded grain form controls
    pltgInventorySeededGrains.push( fb.group( 
      addSeededGrainsObject(null, null, null, null, false, <InventorySeededGrain>[])
    ) )
    
    // add empty seeded forage
    pltgInventorySeededForages.push( fb.group( 
      addSeededForagesObject(null, false, null, false, <InventorySeededForage>[])
    ) )

    // add empty planting object
    fldPlantings.push( fb.group( 
      addPlantingObject( cropYear, landData.fieldId, insurancePlanId, '', '', '', '', '', 1, false, null, <InventoryUnseeded>{}, pltgInventorySeededGrains, pltgInventorySeededForages )
      ))

  }

  flds.push( fb.group( {
    annualFieldDetailId:   [],
    displayOrder:          [ displayOrder ],  
    fieldId:               [ landData.fieldId ],
    fieldLabel:            [ landData.fieldLabel ],
    landUpdateType:        [ landData.landUpdateType ],
    legalLandId:           [ landData.legalLandId ],
    otherLegalDescription: [ landData.otherLegalDescription ],
    transferFromGrowerContractYearId: [ landData.transferFromGrowerContractYearId ],
    plantings:             [ fldPlantings ],
    uwComments:            [ landData.uwComments ],
    isNewFieldUI:          [ true ],
    deletedByUserInd:      [ false ]
  } ) )

  cdr.detectChanges()

  // focus on the new field
  for (let i = flds.length - 1; i >= 0; i--) {
    if (flds['controls'][i].get("displayOrder").value  ==  displayOrder) {
      (<any>flds.controls[i].get('fieldLabel')).nativeElement.focus();
      break
    }
  }
}

export function renameLegalLand(flds: FormArray, fieldId, landData, cdr: ChangeDetectorRef) {
  // const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

  for (let i = 0 ; i < flds.length; i++) {
    if (flds['controls'][i].get("fieldId").value  ==  fieldId) {
      flds.controls[i].get('otherLegalDescription').setValue(landData.otherLegalDescription);
      flds.controls[i].get('landUpdateType').setValue(landData.landUpdateType);
      cdr.detectChanges()
      break
    }
  }
}

export function replaceLegalLand(flds: FormArray, fieldId, landData, cdr: ChangeDetectorRef) {
  // const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

  for (let i = 0 ; i < flds.length; i++) {
    if (flds['controls'][i].get("fieldId").value  ==  fieldId) {
      flds.controls[i].get('otherLegalDescription').setValue(landData.otherLegalDescription);
      flds.controls[i].get('legalLandId').setValue(landData.legalLandId);
      flds.controls[i].get('landUpdateType').setValue(landData.landUpdateType);
      cdr.detectChanges()
      break
    }
  }
}

export function deleteFormField(field, flds: FormArray, dialog: MatDialog, dataToSend: RemoveFieldPopupData , cdr: ChangeDetectorRef ) {

    let dialogRef = dialog.open(RemoveFieldComponent, {
      width: '800px',
      data: dataToSend
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'Proceed'){
        if (result.data && result.data.fieldId && result.data.landData) {

          field.get('landUpdateType').setValue(result.data.landData.landUpdateType);

          // remove the field on the screen
          field.get('deletedByUserInd').setValue(true);

          // recalculate displayorder
          removeGapsInDisplayOrder(flds)
          cdr.detectChanges()
        }
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });

}

export function removeGapsInDisplayOrder(flds: FormArray) {

  let counter = 0

  for ( let i = 0; i < flds.length; i++ ) {

    if (!flds['controls'][i].get("deletedByUserInd").value ) {

      counter++

      if (flds['controls'][i].get("displayOrder").value !== counter ) {

        flds['controls'][i].get("displayOrder").setValue( counter )

      }
    }
  }
}

// end of land management

export function fieldHasInventory(field) : boolean {

  for (let i = 0; i < field.value.plantings.length; i++) {

    // unseeded
    if (!field.value.plantings.value[i].deletedByUserInd ) {

      if ( field.value.plantings.value[i].acresToBeSeeded || field.value.plantings.value[i].cropCommodityId ) {
        return true
      }
    }

    // seeded grain
    for ( let k = 0; k < field.value.plantings.value[i].inventorySeededGrains.length; k++) {
      
      if (!field.value.plantings.value[i].inventorySeededGrains.value[k].deletedByUserInd ) { 

        if ( field.value.plantings.value[i].inventorySeededGrains.value[k].commodityTypeCode  ||
          field.value.plantings.value[i].inventorySeededGrains.value[k].cropCommodityId  ||
          field.value.plantings.value[i].inventorySeededGrains.value[k].cropVarietyCtrl.cropVarietyId ||
          field.value.plantings.value[i].inventorySeededGrains.value[k].seededAcres ) {

            return true
        }

        // check seeded date
        let currDate:any  = new Date()
        let formDate:any = field.value.plantings.value[i].inventorySeededGrains.value[k].seededDate;

        if ( formDate 
          && new Date(formDate).toString() != 'Invalid Date' //seededDate is valid
          && Math.floor((currDate - formDate) / (1000 * 60 * 60 * 24)) > 0 ) { //seededDate is not today - the default date

            return true
        }
      }
    }

    // seeded forage
    for ( let k = 0; k < field.value.plantings.value[i].inventorySeededForages.length; k++) {
      
      if (!field.value.plantings.value[i].inventorySeededForages.value[k].deletedByUserInd ) { 

        if ( field.value.plantings.value[i].inventorySeededForages.value[k].cropVarietyCtrl.cropVarietyId  ||
          field.value.plantings.value[i].inventorySeededForages.value[k].fieldAcres  ||
          field.value.plantings.value[i].inventorySeededForages.value[k].seedingYear) {

            return true
        }

        // check seeding date
        let currDate:any  = new Date()
        let formDate:any = field.value.plantings.value[i].inventorySeededForages.value[k].seedingDate;

        if ( formDate 
          && new Date(formDate).toString() != 'Invalid Date' //seededDate is valid
          && Math.floor((currDate - formDate) / (1000 * 60 * 60 * 24)) > 0 ) { //seedingDate is not today - the default date

            return true
        }
      }
    }
  }

  return false
}

/// Linked Fields and Plantings 
export function isLinkedFieldCommon(fieldId, inventoryContract): boolean {

  if ( inventoryContract && inventoryContract.fields) {

    let field = inventoryContract.fields.find(f => f.fieldId == fieldId)
    if ( field && field && field.policies.length > 0 ) {
      return true
    } 

  }
  return false
}

export function linkedFieldTooltipCommon(fieldId, inventoryContract): string {
  
  let result = ""
  if ( inventoryContract && inventoryContract.fields) {
    let field = inventoryContract.fields.find(f => f.fieldId == fieldId)

    if ( field && field && field.policies.length > 0 ) {
      result = "The field is linked to policy " + field.policies[0].policyNumber + " for grower " + field.policies[0].growerName
    }
  }
  return result
}

export function isLinkedPlantingCommon(fieldId, inventoryFieldGuid, inventoryContract) : boolean{

  if ( inventoryContract && inventoryContract.fields) {

    let field = inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
      // find the planting 
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )

      if (planting && planting.linkedPlanting) {
        return true
      }
    }
  }
  return false
}

export function linkedPlantingTooltipCommon(fieldId, inventoryFieldGuid, inventoryContract, insurancePlanId): string {
  let tooltip = ""

  if ( inventoryContract && inventoryContract.fields) {
    let field = inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
      // find the planting 
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )

      if (planting && planting.linkedPlanting) {
        tooltip = "This planting is linked to a " + (insurancePlanId == INSURANCE_PLAN.GRAIN ? "Forage" : "Grain") + " planting"
      }
    }
  }
  return tooltip
}
