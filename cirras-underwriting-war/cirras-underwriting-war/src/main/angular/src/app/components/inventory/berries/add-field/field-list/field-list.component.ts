import { Component, Input } from '@angular/core';
import { AnnualFieldRsrc } from '@cirras/cirras-underwriting-api';
import { makeTitleCase } from 'src/app/utils';

@Component({
  selector: 'field-list',
  templateUrl: './field-list.component.html',
  styleUrl: './field-list.component.scss',
  standalone: false
})
export class FieldListComponent {
  @Input() fieldList 


  getPolicyAndPlan(field: AnnualFieldRsrc){
    let policyAndPlan = ""

    if (field && field.policies) {

      field.policies.forEach(policy => {

        if ( policyAndPlan.length > 0 ) {
          policyAndPlan = policyAndPlan + "\n" // add a line break
        }
        policyAndPlan = policyAndPlan + policy.policyNumber + " (" + makeTitleCase(policy.insurancePlanName) + ") - " + policy.growerName
      })
    } 

    return policyAndPlan
  }
}
