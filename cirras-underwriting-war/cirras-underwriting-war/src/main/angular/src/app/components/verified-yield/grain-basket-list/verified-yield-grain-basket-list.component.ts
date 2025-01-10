import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { VerifiedYieldGrainBasket } from '@cirras/cirras-underwriting-api';

@Component({
  selector: 'verified-yield-grain-basket-list',
  templateUrl: './verified-yield-grain-basket-list.component.html',
  styleUrl: './verified-yield-grain-basket-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldGrainBasketListComponent {  
  @Input() grainBaskets: Array<VerifiedYieldGrainBasket>
  @Input() grainBasketsFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;  

}
