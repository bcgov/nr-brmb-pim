import { ChangeDetectionStrategy, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { DopYieldFieldRollupForage } from 'src/app/conversion/models-yield';
import { RootState } from 'src/app/store';
import { makeNumberOnly } from 'src/app/utils';

@Component({
    selector: 'forage-dop-yield-summary',
    templateUrl: './forage-dop-yield-summary.component.html',
    styleUrls: ['./forage-dop-yield-summary.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    standalone: false
})
export class ForageDopYieldSummaryComponent{

  @Input() fieldRollup: DopYieldFieldRollupForage;
  @Input() fieldRollupFormArray: UntypedFormArray;

  fieldRollupFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder) {
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
}
