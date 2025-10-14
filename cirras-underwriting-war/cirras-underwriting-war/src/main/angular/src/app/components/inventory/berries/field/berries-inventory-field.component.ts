import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { AnnualField } from 'src/app/conversion/models';
import { addAnnualFieldObject } from '../../inventory-common';
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
      }
    }
  }

  refreshForm(){
    this.fieldFormGroup = this.fb.group(
      addAnnualFieldObject(this.field, this.fb.array([]), this.field.uwComments)
    );
    this.fieldsFormArray.push(this.fieldFormGroup);
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

  isAddPlantingVisible() {

    // if there is more than one non-deleted and non-empty planting then show add planting button
    for (let i = 0; i < this.field.plantings.length; i++) {
      let pltg = this.field.plantings[i]

      if ( !pltg.inventoryBerries.plantedYear && !pltg.inventoryBerries.plantedAcres && !pltg.inventoryBerries.cropVarietyId &&
          !pltg.inventoryBerries.rowSpacing && !pltg.inventoryBerries.plantSpacing) {

            return false
          }
    }
    return true 
  }

  onAddPlanting() {

    if (this.securityUtilService.canEditInventory() && this.isAddPlantingVisible()) {

      let inventoryBerries: InventoryBerries = {
        inventoryBerriesGuid: null,
        inventoryFieldGuid: null,
        cropCommodityId: this.defaultCommodity,
        cropVarietyId: null,
        plantedYear: null,
        plantedAcres: null,
        rowSpacing: null,
        plantSpacing: null,
        totalPlants: null,
        isQuantityInsurableInd: false,
        isPlantInsurableInd: false,
        cropCommodityName: null,
        cropVarietyName: null,
        deletedByUserInd: false
      }

      let pltg: InventoryField = {
        inventoryFieldGuid: null,
        insurancePlanId: INSURANCE_PLAN.BERRIES,
        fieldId: this.field.fieldId,
        lastYearCropCommodityId: null,
        lastYearCropCommodityName: null,
        lastYearCropVarietyId: null,
        lastYearCropVarietyName: null,
        cropYear: this.field.cropYear,
        plantingNumber: this.field.plantings.length + 1, 
        isHiddenOnPrintoutInd: false,  // default
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
      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, true));

    }
  }

}
