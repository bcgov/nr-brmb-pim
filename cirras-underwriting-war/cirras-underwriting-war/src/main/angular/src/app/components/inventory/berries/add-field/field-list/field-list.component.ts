import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AnnualFieldRsrc } from '@cirras/cirras-underwriting-api';
import { makeTitleCase } from 'src/app/utils';
import { INSURANCE_PLAN } from 'src/app/utils/constants';

@Component({
  selector: 'field-list',
  templateUrl: './field-list.component.html',
  styleUrl: './field-list.component.scss',
  standalone: false
})
export class FieldListComponent {
  @Input() fieldList 
  @Input() insurancePlanId
  @Input() searchBy // e.g. searchPID or searchFieldLocation, etc

  showSearchLegalMsg = false 
  fieldListForm = new FormGroup({
    fieldIdSelected: new FormControl(''),
    fieldLabel: new FormControl(''),
  });

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

  setStyles() {
      
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      if (this.searchBy == 'searchFieldId') {
        // the user is coming from add-field component
        return {
          'grid-template-columns': ' 1fr 2fr 2fr 3fr'
        }
      } else {
        // the user is coming from legal-land-list component
        // add one more column to allow the user to select a field
        return {
          'grid-template-columns': '1fr 1fr 2fr 2fr 3fr'
        };
      }
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      if (this.searchBy == 'searchFieldId') {
        return {
          'grid-template-columns': '1fr 2fr 2fr 2fr 2fr'
        }
      } else {
        // add one more column to allow the user to select a field
        // the user is coming from add-field component or legal-land-list component
        return {
          'grid-template-columns': '1fr 1fr 2fr 2fr 2fr 2fr'
        };
      }
    }
  }
  
  // TODO
  setNewFieldStyles() {
    debugger
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return {
        'grid-column-start': '2',
        'grid-column-end': '5'
      };
    }

    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES && this.searchBy == 'searchFieldLocation') {
      return {
        'grid-column-start': '2',
        'grid-column-end': '7'
      };
    }
    
  }

  isRadioBtnVisible() {
    if (this.searchBy == 'searchFieldId') {
      return false
    } else {
      return true
    }
  }

  isFieldAddressVisible() {
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      return true
    } else {
      return false
    }
  }

  isPIDVisible() {
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return true
    } else {
      return false
    }
  }

  isLegalLocationVisible() {
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return true
    } else {
      return false
    }
  }

  displayFieldName() {
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return "Field Name"
    } 
    
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES ) {
      return "Field/Bog Name"
    } 

    return ""
  }

  validateFields(field) {
    //TODO
  }


  checkLength() {
    // TODO
    // this.validationMessages = <AddFieldValidationRsrc>{};
    // this.addLandForm.controls.fieldIdSelected.setValue(-1)

    // if (this.addLandForm.get("fieldLabel").value && this.addLandForm.get("fieldLabel").value.length > 1) {
    //   this.showProceedButton = true
    // }
  }
}
