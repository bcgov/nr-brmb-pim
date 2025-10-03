import { Component, Input, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { InventoryField } from '@cirras/cirras-underwriting-api';

@Component({
  selector: 'berries-inventory-planting',
  imports: [],
  templateUrl: './berries-inventory-planting.component.html',
  styleUrl: './berries-inventory-planting.component.scss',
  standalone: false
})
export class BerriesInventoryPlantingComponent {

  @Input() planting: InventoryField;
  @Input() plantingFormArray: UntypedFormArray;
  @Input() fieldHiddenOnPrintoutInd: boolean;

  plantingFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) {}

  ngOnInit(): void {
    this.refreshForm()
  }

  ngOnChanges(changes: SimpleChanges) {

    if (changes.planting && changes.planting.currentValue) {
        this.refreshForm()
    }
  }

  refreshForm() {
     this.plantingFormGroup = this.fb.group({
          inventoryFieldGuid: [this.planting.inventoryFieldGuid],
          // commodityTypeCode: [this.yieldField.commodityTypeCode],
          // commodityTypeDescription: [this.yieldField.commodityTypeDescription],
          // cropVarietyName: [this.yieldField.cropVarietyName],
          // cropVarietyId: [this.yieldField.cropVarietyId],
          // plantDurationTypeCode: [this.yieldField.plantDurationTypeCode],
          // isQuantityInsurableInd: {
          //     value: this.yieldField.isQuantityInsurableInd,
          //     disabled: true
          // },
          // fieldAcres: [this.yieldField.fieldAcres],
          // insurancePlanId: [this.yieldField.insurancePlanId],
          // fieldId: [this.yieldField.fieldId],
          // cropYear: [this.yieldField.cropYear],
          // isHiddenOnPrintoutInd: {
          //     value: this.yieldField.isHiddenOnPrintoutInd,
          //     disabled: true
          // },
          // plantInsurabilityTypeCode: this.yieldField.plantInsurabilityTypeCode,
          // seedingYear: this.yieldField.seedingYear,
          // seedingDate: this.yieldField.seedingDate,
          // dopYieldFieldForageCuts: this.fb.array([]),
      });

      this.plantingFormArray.push(this.plantingFormGroup);
  }



}
