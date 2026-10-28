import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { VerifiedYieldGrainBasket } from 'src/app/conversion/models-yield';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { RootState } from 'src/app/store';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { VERIFIED_YIELD_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';
import { NgIf, NgStyle, CurrencyPipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatTooltip } from '@angular/material/tooltip';
import { MatFormField } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';

@Component({
    selector: 'verified-yield-grain-basket',
    templateUrl: './verified-yield-grain-basket.component.html',
    styleUrl: './verified-yield-grain-basket.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [FormsModule, ReactiveFormsModule, NgIf, MatIcon, MatTooltip, NgStyle, MatFormField, MatInput, CdkTextareaAutosize, CurrencyPipe]
})
export class VerifiedYieldGrainBasketComponent {
  @Input() grainBasket: VerifiedYieldGrainBasket;
  @Input() grainBasketsFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;  

  grainBasketFormGroup: UntypedFormGroup;
    
    constructor(private fb: UntypedFormBuilder,
      private store: Store<RootState>,
      public securityUtilService: SecurityUtilService, 
      ) { }
  
    ngOnChanges(changes: SimpleChanges) {
        if (changes.grainBasket && this.grainBasket) {
          this.setupForm()
        }
      }
    
    setupForm() {
      this.grainBasketFormGroup = this.fb.group({
        verifiedYieldGrainBasketGuid: [this.grainBasket.verifiedYieldGrainBasketGuid],
        verifiedYieldContractGuid: [this.grainBasket.verifiedYieldContractGuid],
        basketValue: [this.grainBasket.basketValue],
        totalQuantityCoverageValue: [this.grainBasket.totalQuantityCoverageValue],
        totalCoverageValue: [this.grainBasket.totalCoverageValue],
        harvestedValue: [this.grainBasket.harvestedValue],
        comment: [this.grainBasket.comment]         
      });
      this.grainBasketsFormArray.push(this.grainBasketFormGroup);
    }

    updateComment(){
      let comment = this.grainBasketFormGroup.value.comment;
      this.grainBasket.comment = comment || null;
  
      this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true)); 
    }

}
