import { Component, Input, SimpleChanges } from '@angular/core';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'replace-legal-land',
  templateUrl: './replace-legal-land.component.html',
  styleUrl: './replace-legal-land.component.scss',
  standalone: false
})
export class ReplaceLegalLandComponent {
  @Input() replaceLegalLandList
  @Input() insurancePlanId

  showNewLegalLandMessage = false 

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
