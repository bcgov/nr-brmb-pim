import { Component, Input } from '@angular/core';
import { INSURANCE_PLAN } from 'src/app/utils/constants';
import { NgIf, NgStyle, NgFor } from '@angular/common';
import { MatIcon } from '@angular/material/icon';

@Component({
    selector: 'legal-land-list-with-message',
    templateUrl: './legal-land-list-with-message.component.html',
    styleUrl: './legal-land-list-with-message.component.scss',
    imports: [NgIf, MatIcon, NgStyle, NgFor]
})
export class LegalLandListWithMessageComponent {
  @Input() legalLandList 
  @Input() message
  @Input() insurancePlanId
  @Input() displayShortLegalDescription


  setStyles() {
    
    if (this.displayShortLegalDescription ) {
      return {
        'grid-template-columns': '1fr 2.5fr 2.5fr'
      };

    } else {

      return {
        'grid-template-columns': '1fr 2fr'
      }
    }

  }

  displayPID() {
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return true
    }
    
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return false
    }
  }

  displayOtherDescription() {
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return false
    }
    
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return true
    }
  }

}
