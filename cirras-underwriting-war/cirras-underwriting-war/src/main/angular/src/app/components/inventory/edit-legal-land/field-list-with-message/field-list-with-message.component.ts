import { Component, Input } from '@angular/core';
import { getThePolicyAndPlan } from '../../inventory-common';
import { AnnualFieldRsrc } from '@cirras/cirras-underwriting-api';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'field-list-with-message',
  templateUrl: './field-list-with-message.component.html',
  styleUrl: './field-list-with-message.component.scss',
  standalone: false
})
export class FieldListWithMessageComponent {
  @Input() fieldList 
  @Input() message
  @Input() insurancePlanId

  getPolicyAndPlan(field: AnnualFieldRsrc) {
    return getThePolicyAndPlan(field)
  }

  isFieldAddressVisible() {
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return true
    } else {
      return false
    }
  }
}
