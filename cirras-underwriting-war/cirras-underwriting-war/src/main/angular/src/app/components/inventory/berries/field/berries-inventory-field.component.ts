import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { AnnualField } from 'src/app/conversion/models';
import { addAnnualFieldObject, getDefaultInventoryBerries, getDefaultPlanting, removeGapsInDisplayOrder } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { InventoryBerries, InventoryField, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { BERRY_COMMODITY, INSURANCE_PLAN, LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { setTableHeaderStyleForBerries } from '../field-list/berries-inventory-field-list.component';
import { RemoveFieldComponent, RemoveFieldPopupData } from '../../remove-field/remove-field.component';
import { MatDialog } from '@angular/material/dialog';
import { AddLandPopupData } from '../../add-field/add-field.component';
import { EditLegalLandInInventoryComponent } from '../../edit-legal-land/edit-legal-land.component';

@Component({
  selector: 'berries-inventory-field',
  templateUrl: './berries-inventory-field.component.html',
  styleUrl: './berries-inventory-field.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldComponent implements OnInit, OnChanges{  

  @Input() field: AnnualField;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() selectedCommodity;
  @Input() minNewFieldId;
  @Input() numComponentReloads;
  @Input() policyId;

  fieldFormGroup: UntypedFormGroup;
  numPlantingsToSave = 1 // default

  isFieldHiddenOnPrintout = false

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
              protected securityUtilService: SecurityUtilService,
              protected dialog: MatDialog,
              protected cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.field && changes.field.currentValue) || (changes.numComponentReloads && changes.numComponentReloads.currentValue) ) {
      if (this.field) {
        this.refreshForm()
        this.updateNumPlantings()  
      }
    }
  }

  refreshForm(){
    this.fieldFormGroup = this.fb.group(
      addAnnualFieldObject(this.field, this.fb.array([]), this.field.uwComments)
    );
    this.fieldsFormArray.push(this.fieldFormGroup);
    this.setIsFieldHiddenOnPrintout()
  }

  showField(){
    return (this.fieldHasCommodity() && (this.field.deletedByUserInd == null || this.field.deletedByUserInd == false));
  }

  fieldHasCommodity() {
    // display field if the field has plantings with the desired commodity
    // or the field has no commodity 
    if (this.field && this.field.plantings && this.selectedCommodity) {
      //Check if there are plantings with the selected commodity that are NOT marked for deletion
      let plantings = this.field.plantings.filter ( pltg => 
        pltg.inventoryBerries.cropCommodityId == this.selectedCommodity
          && (pltg.inventoryBerries.deletedByUserInd == null || pltg.inventoryBerries.deletedByUserInd == false))
      if (plantings && plantings.length > 0) {
        return true
      }

      plantings = this.field.plantings.filter ( pltg => pltg.inventoryBerries.cropCommodityId == null)
      if (plantings && plantings.length > 0) {
        return true
      }
    }

    return false
  }


  updateNumPlantings() {
    if (this.field.plantings) {
      this.numPlantingsToSave = this.field.plantings.filter(
        x => (x.inventoryBerries.deletedByUserInd !== true  && 
              x.inventoryBerries.cropCommodityId == this.selectedCommodity )).length
    }
  }

  setPlantingStyles() {
    
    if (this.selectedCommodity == BERRY_COMMODITY.Blueberry ) {
      return {
          'display': 'grid',
          'align-items': 'stretch',
          'width': `830px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Raspberry ) {
      return {
          'display': 'grid',
          'align-items': 'stretch',
          'width': `510px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Strawberry ) {
      return {
          'display': 'grid',
          'align-items': 'stretch',
          'width': `650px`
      };
    }

    if (this.selectedCommodity == BERRY_COMMODITY.Cranberry ) {
      return {
          'display': 'grid',
          'align-items': 'stretch',
          'width': `910px`
      };
    }

  }

  updateFieldLocation() {
    this.field.fieldLocation = this.fieldFormGroup.value.fieldLocation
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateFieldLabel() {
    this.field.fieldLabel = this.fieldFormGroup.value.fieldLabel
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  updateIsLeasedInd() {
    this.field.isLeasedInd = this.fieldFormGroup.value.isLeasedInd
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true))
  }

  onInventoryCommentsDone(uwComments: UnderwritingComment[]) {
      this.field.uwComments = uwComments;
      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
  }

  onAddPlanting() {

    if (this.securityUtilService.canEditInventory()) {
      let inventoryBerries: InventoryBerries = getDefaultInventoryBerries(null, null, this.selectedCommodity)

      let pltg: InventoryField = getDefaultPlanting(null, INSURANCE_PLAN.BERRIES, this.field.fieldId,  
                      this.field.cropYear, this.getMaxPlantingNumber() + 1, inventoryBerries, [], [])

      this.field.plantings.push(pltg)
      this.updateNumPlantings()
      this.setIsFieldHiddenOnPrintout()

      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
    }
  }

  getMaxPlantingNumber(){
    let maxNum = 1

    this.field.plantings.forEach((pltg: InventoryField) => {
      if (pltg.plantingNumber > maxNum ) {
        maxNum = pltg.plantingNumber
      }
    })

    return maxNum
  }

  onRecalcNumPlantings() {
    this.updateNumPlantings() 
  }


  setTableHeaderStyle() {
    return setTableHeaderStyleForBerries(this.selectedCommodity)
  }

  setIsFieldHiddenOnPrintout() {
    
    let elem = this.field.plantings.filter(x => x.isHiddenOnPrintoutInd !== true)

    if (elem.length == 0) {
      this.isFieldHiddenOnPrintout = true // all plantings are hidden
    } else {
      this.isFieldHiddenOnPrintout = false // at least one planting is not hidden
    } 
  }

  isCranberry() {
    if(this.selectedCommodity == BERRY_COMMODITY.Cranberry ) {
      return true
    } else {
      return false
    }
  }

  // LAND Management
  onDeleteField() {

    if (this.field.isNewFieldUI == true ) {

      this.deleteNewField()

    } else {

      const dataToSend : RemoveFieldPopupData = {
        fieldId: this.field.fieldId,
        fieldLabel: this.field.fieldLabel,
        policyId: this.policyId,
        hasInventory: this.fieldHasInventory(this.field),
        hasComments: this.isThereAnyCommentForField(this.field),
        showRemoveCommodity: this.fieldHasOtherCommodities(this.field), //Only shown if there are multiple commodities
        landData: {
          landUpdateType: ""
        }  
      }

      this.deleteFormField(this.field, this.fieldsFormArray, this.dialog, dataToSend)

      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
    }
  }

  deleteNewField() {

    this.field.deletedByUserInd = true // so we know not to send it to the API
    
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));

  }

  isThereAnyCommentForField(field: AnnualField) {
    // Checks if there are un-deleted comments
    if (field.uwComments && field.uwComments.length > 0) {

      for (let i = 0; i < field.uwComments.length; i++) {
        if (field.uwComments[i].deletedByUserInd == true) {

        } else {
          return true
        }
      }
      
    }
    return false
  }

  fieldHasInventory(field: AnnualField) : boolean {

    if(field.plantings) {
      field.plantings.forEach( pltg => {

        if(pltg.inventoryBerries && !pltg.inventoryBerries.deletedByUserInd ) {

          if ( pltg.inventoryBerries.cropVarietyId  ||
            pltg.inventoryBerries.plantedYear  ||
            pltg.inventoryBerries.plantedAcres) {
              return true
          }
        }
      })
    }
    return false
  }

  fieldHasOtherCommodities(field: AnnualField) : boolean {

    if (field && field.plantings && this.selectedCommodity) {
      let plantings = this.field.plantings.filter ( pltg => 
        pltg.inventoryBerries.cropCommodityId != this.selectedCommodity
        && (pltg.inventoryBerries.deletedByUserInd == null || pltg.inventoryBerries.deletedByUserInd == false))
      if (plantings && plantings.length > 0) {
        return true
      }
    }
    return false
  }
  
  deleteFormField(field: AnnualField, flds: UntypedFormArray, dialog: MatDialog, dataToSend: RemoveFieldPopupData) {

    let dialogRef = dialog.open(RemoveFieldComponent, {
      width: '800px',
      data: dataToSend
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'Proceed'){
        if (result.data && result.data.fieldId) {
          //If user selected remove from policy or delete field
          if(result.data.landData 
              && (result.data.landData.landUpdateType == LAND_UPDATE_TYPE.REMOVE_FIELD_FROM_POLICY
                  || result.data.landData.landUpdateType == LAND_UPDATE_TYPE.DELETE_FIELD)) {

            field.landUpdateType = result.data.landData.landUpdateType;

            // remove the field on the screen
            field.deletedByUserInd = true;

            // recalculate displayorder
            removeGapsInDisplayOrder(flds)

            this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));

          } else if (dataToSend.removeCommodityFromField && dataToSend.removeCommodityFromField == true){
            //Remove all plantings
            if(field.plantings) {
              field.plantings.forEach( pltg => {
                if(pltg.inventoryBerries.cropCommodityId == this.selectedCommodity){
                  pltg.inventoryBerries.deletedByUserInd = true;
                }
              })
              this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));
            }            
          }
          this.cdr.detectChanges()
        }
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });
  }

  onEditLegalLand(field){
    
    const dataToSend : AddLandPopupData = {
      fieldId: field.fieldId,
      fieldLabel: field.fieldLabel,
      cropYear: field.cropYear,
      policyId: this.policyId,
      insurancePlanId: INSURANCE_PLAN.BERRIES, 
      annualFieldDetailId: field.annualFieldDetailId,
      otherLegalDescription: field.otherLegalDescription,
      primaryPropertyIdentifier: field.primaryPropertyIdentifier
    }

    let dialogRef = this.dialog.open(EditLegalLandInInventoryComponent, {
        width: '1110px',
        data: dataToSend,
        autoFocus: false // if you remove this line of code then the first radio button would be selected
      });
      
    dialogRef.afterClosed().subscribe(result => {
    
      if (result && result.event == 'RenameLand'){
        
        // Rename legal land 
        if (result.data && result.data.fieldId && result.data.landData) {
          this.renameLegalLand (result.data.landData) 
        }
      } else if (result && result.event == 'ReplaceLand'){
        // TODO: Replace  legal land
        // replaceLegalLand(flds, result.data.fieldId, result.data.landData, cdr)
  
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });
  }

  renameLegalLand(landData) {

    this.field.primaryPropertyIdentifier = landData.primaryPropertyIdentifier
    this.field.landUpdateType = landData.landUpdateType

    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));

    this.cdr.detectChanges()
  }
  
}
