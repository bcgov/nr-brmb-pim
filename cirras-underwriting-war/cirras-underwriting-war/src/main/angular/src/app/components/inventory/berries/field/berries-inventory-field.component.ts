import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { AnnualField } from 'src/app/conversion/models';
import { addAnnualFieldObject, getDefaultInventoryBerries } from '../../inventory-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { InventoryBerries, InventoryField, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

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
  @Input() defaultCommodity;

  fieldFormGroup: UntypedFormGroup;
  numPlantingsToSave = 1 // default

  constructor(private fb: UntypedFormBuilder,
              private store: Store<RootState>,
              protected securityUtilService: SecurityUtilService) {}

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
  }

  updateNumPlantings() {
    if (this.field.plantings) {
      this.numPlantingsToSave = this.field.plantings.filter(x => x.inventoryBerries.deletedByUserInd !== true ).length
    }
  }

  setPlantingStyles() {
    return {
        'display': 'grid',
       // 'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
        'align-items': 'stretch',
        'width': `830px`
    };
  }

  updateFieldLocation() {
    this.field.fieldLocation = this.fieldFormGroup.value.fieldLocation
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
      let inventoryBerries: InventoryBerries = getDefaultInventoryBerries(null, null, this.defaultCommodity)

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
        inventoryUnseeded: {},
        inventoryBerries: inventoryBerries,
        linkedPlanting: null,
        inventorySeededGrains: [],
        inventorySeededForages: []
      }

      this.field.plantings.push(pltg)
      this.updateNumPlantings()

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

}
