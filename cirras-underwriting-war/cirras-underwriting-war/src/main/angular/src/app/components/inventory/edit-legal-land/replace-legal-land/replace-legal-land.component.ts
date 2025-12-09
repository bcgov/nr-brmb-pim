import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'replace-legal-land',
  templateUrl: './replace-legal-land.component.html',
  styleUrl: './replace-legal-land.component.scss',
  standalone: false
})
export class ReplaceLegalLandComponent implements OnInit, OnChanges {
  @Input() replaceLegalLandList
  @Input() insurancePlanId
  @Input() legalLandList

  showNewLegalLandMessage = false 

  replaceLegalLandForm: FormGroup;

  ngOnInit(): void {

    // add fields to the form
    this.replaceLegalLandForm = new FormGroup({
      legalLandIdSelected: new FormControl()
    });
  }

  ngOnChanges(changes: SimpleChanges) {

    if (changes.replaceLegalLandList) {
      
      this.showNewLegalLandMessage = false

      if( // TODO ?? - there might be something other condition here
          ( this.replaceLegalLandList && 
            this.replaceLegalLandList.isWarningFieldOnOtherPolicy == false &&
            this.replaceLegalLandList.isWarningOtherFieldsOnLegal == false &&
            this.replaceLegalLandList.isWarningFieldHasOtherLegalLand == false )) {

              this.showNewLegalLandMessage = true
            }      
    }
  }

  get legalLandSearchBy(): string {
    
    if (this.insurancePlanId == INSURANCE_PLAN.GRAIN || this.insurancePlanId == INSURANCE_PLAN.FORAGE) {
      return "Legal Location" 
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return "PID"
    }
  }


}
