import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { FormArray } from '@angular/forms';
import { DopYieldFieldRollupForage } from 'src/app/conversion/models-yield';

@Component({
  selector: 'forage-dop-yield-summary-list',
  templateUrl: './forage-dop-yield-summary-list.component.html',
  styleUrls: ['./forage-dop-yield-summary-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})


export class ForageDopYieldSummaryListComponent {

  @Input() fieldRollupList: Array<DopYieldFieldRollupForage>;
  @Input() fieldRollupFormArray: FormArray;
  @Input() decimalPrecision: number;
  @Input() isUnsaved: boolean;

  get totalFieldAcresTotal(): number {
      return this.fieldRollupList.reduce((acc, curr) => acc + curr.totalFieldAcres || 0, 0);
  }

  get harvestedAcresTotal(): number {
      return this.fieldRollupList.reduce((acc, curr) => acc + curr.harvestedAcres || 0, 0);
  }

  // get harvestedAcresOverrideTotal(): number {
  //     return this.commodities.reduce((acc, curr) => acc + curr.harvestedAcresOverride || 0, 0);
  // }

}
