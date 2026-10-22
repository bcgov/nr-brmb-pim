import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'rename-legal-land',
  templateUrl: './rename-legal-land.component.html',
  styleUrl: './rename-legal-land.component.scss',
  standalone: false
})
export class RenameLegalLandComponent implements OnChanges {
  @Input() renameLegalLandList 
  @Input() insurancePlanId

  showNewLegalLandMessage = false;
  otherLegalData = []

  ngOnChanges(changes: SimpleChanges) {

    if (changes.renameLegalLandList) {
      
      this.showNewLegalLandMessage = false
      this.otherLegalData = []

      if( // !this.renameLegalLandList || 
          ( this.renameLegalLandList && 
            this.renameLegalLandList.isWarningLegalsWithSameLoc == false &&
            this.renameLegalLandList.isWarningOtherFieldOnPolicy == false &&
            this.renameLegalLandList.isWarningFieldOnOtherPolicy == false &&
            this.renameLegalLandList.isWarningOtherLegalData == false )) {

              this.showNewLegalLandMessage = true
            }
      
      if (this.renameLegalLandList?.isWarningOtherLegalData == true) {
        this.otherLegalData.push(this.renameLegalLandList.otherLegalData) // making it into array so I can reuse the legal land component
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
