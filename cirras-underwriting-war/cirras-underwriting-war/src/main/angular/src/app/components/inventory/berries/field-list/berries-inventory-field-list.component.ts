import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';

@Component({
  selector: 'berries-inventory-field-list',
  templateUrl: './berries-inventory-field-list.component.html',
  styleUrl: './berries-inventory-field-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldListComponent {

  @Input() fields: Array<AnnualField>;
  @Input() fieldsFormArray: UntypedFormArray;
  @Input() cropVarietyOptions;
}
