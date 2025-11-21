import { Component, EventEmitter, Input, Output } from '@angular/core';
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

  //TODO: EventEmitter<{ fieldId: number; fieldName: string; fieldLocation: string }>();
  @Output() fieldIdChanged = new EventEmitter<AnnualFieldRsrc>(); 

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
        return {
          'grid-template-columns': ' 1fr 2fr 2fr 3fr'
        }
      } else {
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
      } 
      if (this.searchBy == 'searchFieldLocation') {
        return {
          'grid-template-columns': '1fr 1fr 2fr 2fr 2fr 2fr'
        };
      }

      if (this.searchBy == 'searchPID') {
        return {
          'grid-template-columns': '1fr 1fr 2fr 2fr 2fr'
        };
      }

    }
  }
  
  setNewFieldStyles() {
  
    if (this.insurancePlanId == INSURANCE_PLAN.FORAGE || this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
      return {
        'grid-column-start': '2',
        'grid-column-end': '6'
      };
    }
    
    if (this.insurancePlanId == INSURANCE_PLAN.BERRIES) {
      if (this.searchBy == 'searchPID' || this.searchBy == 'searchFieldId') {
        return {
          'grid-column-start': '2',
          'grid-column-end': '6'
        };
      } else {
          return {
          'grid-column-start': '2',
          'grid-column-end': '7'
        };
      }
      
    }
  }

  isSearchByFieldId() {
    if (this.searchBy == 'searchFieldId') {
      return true
    } else {
      return false
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
      if (this.searchBy == 'searchPID') {
        return false
      } else {
        return true
      }
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

  checkLength() {
    // TODO
    // send field name and / or field location back to the add-field component

    // this.validationMessages = <AddFieldValidationRsrc>{};
    // this.addLandForm.controls.fieldIdSelected.setValue(-1)

    // if (this.addLandForm.get("fieldLabel").value && this.addLandForm.get("fieldLabel").value.length > 1) {
    //   this.showProceedButton = true
    // }
  }

  sendFieldId(field: AnnualFieldRsrc) {
    this.fieldIdChanged.emit(field);
  }
}
