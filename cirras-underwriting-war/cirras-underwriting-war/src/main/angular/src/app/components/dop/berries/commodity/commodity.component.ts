import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';

@Component({
  selector: 'berries-dop-commodity',
  templateUrl: './commodity.component.html',
  styleUrl: './commodity.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})

export class BerriesDopCommodityComponent {
  @Input() commodityName: String;

  @Input() fields: Array<AnnualField>;
  @Input() fieldsFormArray: UntypedFormArray;

}
