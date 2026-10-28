import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { INSURANCE_PLAN } from 'src/app/utils/constants';
import { NgIf, NgStyle } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { LegalLandListWithMessageComponent } from '../legal-land-list-with-message/legal-land-list-with-message.component';
import { FieldListWithMessageComponent } from '../field-list-with-message/field-list-with-message.component';

@Component({
    selector: 'rename-legal-land',
    templateUrl: './rename-legal-land.component.html',
    styleUrl: './rename-legal-land.component.scss',
    imports: [NgIf, MatIcon, NgStyle, LegalLandListWithMessageComponent, FieldListWithMessageComponent]
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
