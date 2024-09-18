import { DecimalPipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { DopYieldFieldRollupForage } from 'src/app/conversion/models-yield';
import { RootState } from 'src/app/store';
import { makeNumberOnly } from 'src/app/utils';

@Component({
  selector: 'forage-dop-yield-summary',
  templateUrl: './forage-dop-yield-summary.component.html',
  styleUrls: ['./forage-dop-yield-summary.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopYieldSummaryComponent{

  @Input() fieldRollup: DopYieldFieldRollupForage;
  @Input() fieldRollupFormArray: FormArray;
  @Input() decimalPrecision: number;

  fieldRollupFormGroup: FormGroup;

  constructor(private fb: FormBuilder,
      private store: Store<RootState>,
      private decimalPipe: DecimalPipe) {
  }

  ngOnInit(): void {
      this.fieldRollupFormGroup = this.fb.group({
          declaredYieldContractCmdtyForageGuid: [this.fieldRollup.declaredYieldFieldRollupForageGuid],
          declaredYieldContractGuid: [this.fieldRollup.declaredYieldContractGuid],
          commodityTypeCode: [this.fieldRollup.commodityTypeCode],
          totalFieldAcres: [this.fieldRollup.totalFieldAcres],
          harvestedAcres: [this.fieldRollup.harvestedAcres],
          totalBalesLoads: [this.fieldRollup.totalBalesLoads],
          quantityHarvestedTons: [this.fieldRollup.quantityHarvestedTons],
          yieldPerAcre: [this.fieldRollup.yieldPerAcre],
          commodityTypeDescription: [this.fieldRollup.commodityTypeDescription]
      });
      this.fieldRollupFormArray.push(this.fieldRollupFormGroup);
  }

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

  // updateHarvestedAcresOverride(): void {
  //     let harvestedAcresOverride = this.commodityFormGroup.value.harvestedAcresOverride;
  //     harvestedAcresOverride = this.decimalPipe.transform(harvestedAcresOverride, '1.0-1')?.replace(',', '');
  //     this.commodity.harvestedAcresOverride = parseFloat(harvestedAcresOverride) || null;

  //     this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  // }

  // updateQuantityHarvestedTonsOverride(): void {
  //     let quantityHarvestedTonsOverride = this.commodityFormGroup.value.quantityHarvestedTonsOverride;
  //     quantityHarvestedTonsOverride = this.decimalPipe.transform(quantityHarvestedTonsOverride, `1.0-${this.decimalPrecision}`)?.replace(',', '');
  //     this.commodity.quantityHarvestedTonsOverride = parseFloat(quantityHarvestedTonsOverride) || null;

  //     this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  // }
}
