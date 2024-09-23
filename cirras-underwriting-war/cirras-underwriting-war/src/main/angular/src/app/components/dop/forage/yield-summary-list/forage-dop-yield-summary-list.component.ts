import { ChangeDetectionStrategy, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { FormArray } from '@angular/forms';
import { DopYieldFieldRollupForage } from 'src/app/conversion/models-yield';

@Component({
  selector: 'forage-dop-yield-summary-list',
  templateUrl: './forage-dop-yield-summary-list.component.html',
  styleUrls: ['./forage-dop-yield-summary-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})


export class ForageDopYieldSummaryListComponent {

  @Input() fieldRollupList: Array<DopYieldFieldRollupForage>;
  @Input() fieldRollupFormArray: FormArray;
  @Input() isUnsaved: boolean;

  get totalFieldAcresTotal(): number {
      return this.fieldRollupList.reduce((acc, curr) => acc + curr.totalFieldAcres || 0, 0);
  }

  get harvestedAcresTotal(): number {
      return this.fieldRollupList.reduce((acc, curr) => acc + curr.harvestedAcres || 0, 0);
  }
}
