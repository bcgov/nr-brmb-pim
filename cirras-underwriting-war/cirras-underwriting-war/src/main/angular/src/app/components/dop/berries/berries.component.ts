import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from '@angular/core';
import { UwContract } from 'src/app/conversion/models';
import { DopYieldContract } from 'src/app/conversion/models-yield';

@Component({
  selector: 'berries-dop',
  imports: [],
  templateUrl: './berries.component.html',
  styleUrl: './berries.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesDopComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() isUnsaved: boolean;

}
