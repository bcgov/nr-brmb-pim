import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'legal-land-list',
  templateUrl: './legal-land-list.component.html',
  styleUrl: './legal-land-list.component.scss',
  standalone: false
})
export class LegalLandListComponent {
  @Input() selectedSearch // e.g. PID
  @Input() selectedSearchValue  // e.g. the PID itself
  @Input() legalLandList 
  @Input() insurancePlanId
  @Output() legalLandChanged = new EventEmitter<{ legalLandId: number; primaryPropertyIdentifier: string; otherLegalDescription: string }>();

  legaLandListForm = new FormGroup({
    legalLandIdSelected: new FormControl('')
  });

  isLegalLocationVisible() {

    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN) {
      return true
    }

    return false
  }

  setStyles() {
    
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return {
        'grid-template-columns': '1fr 2fr 2fr 5fr'
      };
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return {
        'grid-template-columns': '1fr 2fr 5fr'
      };
    }
    
  }

  setNewLegalStyles() {
    
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return {
        'grid-column-start': '2',
        'grid-column-end': '5'
      };
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return {
        'grid-column-start': '2',
        'grid-column-end': '4'
      };
    }
    
  }

  sendLegalLandId(legalLandId: number, primaryPropertyIdentifier: string, otherLegalDescription: string) {
    this.legalLandChanged.emit({ 
      legalLandId: legalLandId, 
      primaryPropertyIdentifier: primaryPropertyIdentifier, 
      otherLegalDescription: otherLegalDescription 
    });
  }

}
