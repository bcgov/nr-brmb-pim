import { ChangeDetectionStrategy, Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'berries-inventory',
  templateUrl: './berries-inventory.component.html',
  styleUrl: './berries-inventory.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesInventoryComponent { // TODO: extends BaseComponent implements OnChanges 

}
