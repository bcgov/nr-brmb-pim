import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'replace-legal-land',
  templateUrl: './replace-legal-land.component.html',
  styleUrl: './replace-legal-land.component.scss',
  standalone: false
})
export class ReplaceLegalLandComponent implements OnInit {
  @Input() replaceLegalLandList
  @Input() insurancePlanId
  @Input() legalLandList
  @Input() showNewLegalLandMessage
  @Output() legalLandChanged = new EventEmitter<{ legalLandId: number; primaryPropertyIdentifier: string; otherLegalDescription: string }>();

  replaceLegalLandForm: FormGroup;

  ngOnInit(): void {

    // add fields to the form
    this.replaceLegalLandForm = new FormGroup({
      legalLandIdSelected: new FormControl()
    });
  }

  get legalLandSearchBy(): string {
    
    if (this.insurancePlanId == INSURANCE_PLAN.GRAIN || this.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      return "Legal Location" 
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return "PID"
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
