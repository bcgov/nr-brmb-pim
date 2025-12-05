import { Component, Input, SimpleChanges } from '@angular/core';

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

      if( // !this.replaceLegalLandList || 
          ( this.replaceLegalLandList && 
            this.replaceLegalLandList.isWarningFieldOnOtherPolicy == false &&
            this.replaceLegalLandList.isWarningOtherFieldsOnLegal == false &&
            this.replaceLegalLandList.isWarningFieldHasOtherLegalLand == false )) {

              this.showNewLegalLandMessage = true
            }      
    }
  }

}
