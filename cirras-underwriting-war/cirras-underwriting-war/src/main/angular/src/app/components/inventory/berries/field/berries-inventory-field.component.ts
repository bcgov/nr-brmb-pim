import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { AnnualField } from 'src/app/conversion/models';
import { addAnnualFieldObject, getDefaultInventoryBerries, fieldHasInventory, isThereAnyCommentForField, deleteFormField, deleteNewFormField, removeGapsInDisplayOrder } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { InventoryBerries, InventoryField, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { BERRY_COMMODITY, INSURANCE_PLAN, LAND_UPDATE_TYPE } from 'src/app/utils/constants';
import { setTableHeaderStyleForBerries } from '../field-list/berries-inventory-field-list.component';
import { RemoveFieldComponent, RemoveFieldPopupData } from '../../remove-field/remove-field.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'berries-inventory-field',
  templateUrl: './berries-inventory-field.component.html',
  styleUrl: './berries-inventory-field.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldComponent implements OnChanges{

  @Input() field: AnnualField;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
  @Input() selectedCommodity;
  @Input() policyId;

  fieldFormGroup: UntypedFormGroup;
  numPlantingsToSave = 1 // default

  isFieldHiddenOnPrintout = false

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
              protected securityUtilService: SecurityUtilService,
              protected dialog: MatDialog) {}

  ngOnInit() {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.field && changes.field.currentValue) {
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

  fieldHasCommodity() {
    // display field if the field has plantings with the desired commodity
    // or the field has no commodity 
    if (this.field && this.field.plantings && this.selectedCommodity) {

      let plantings = this.field.plantings.filter ( pltg => pltg.inventoryBerries.cropCommodityId == this.selectedCommodity)
      if (plantings && plantings.length > 0) {
        return true
      }

      // TODO: remove the check for no commodity after add field is complete
      plantings = this.field.plantings.filter ( pltg => pltg.inventoryBerries.cropCommodityId == null)
      if (plantings && plantings.length > 0) {
        return true
      }
    }

    return false
  }

  updateNumPlantings() {
    if (this.field.plantings) {
      // TODO when ADD Field is ready -> filter by commodity is as well
      // this.numPlantingsToSave = this.field.plantings.filter(
      //   x => (x.inventoryBerries.deletedByUserInd !== true  && 
      //         x.inventoryBerries.cropCommodityId == this.selectedCommodity )).length

      // but for now, all fields have the same commodity
      this.numPlantingsToSave = this.field.plantings.filter(
        x => (x.inventoryBerries.deletedByUserInd !== true )).length
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

      let pltg: InventoryField = {
        inventoryFieldGuid: null,
        insurancePlanId: INSURANCE_PLAN.BERRIES,
        fieldId: this.field.fieldId,
        lastYearCropCommodityId: null,
        lastYearCropCommodityName: null,
        lastYearCropVarietyId: null,
        lastYearCropVarietyName: null,
        cropYear: this.field.cropYear,
        plantingNumber: this.getMaxPlantingNumber() + 1, 
        isHiddenOnPrintoutInd: false, 
        underseededCropVarietyId: null, 
        underseededCropVarietyName: null, 
        underseededAcres: null,
        underseededInventorySeededForageGuid: null,
        inventoryUnseeded: null,
        inventoryBerries: inventoryBerries,
        linkedPlanting: null,
        inventorySeededGrains: [],
        inventorySeededForages: []
      }

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

    deleteNewFormField(this.field.fieldId, this.field.isNewFieldUI, this.fieldsFormArray)
    
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
      let plantings = this.field.plantings.filter ( pltg => pltg.inventoryBerries.cropCommodityId != this.selectedCommodity)
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
        }
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });
  }

}
