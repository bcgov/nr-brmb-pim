import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';

@Component({
  selector: 'add-field',
  templateUrl: './add-field.component.html',
  styleUrl: './add-field.component.scss',
  standalone: false
})
export class AddFieldComponent {

  titleLabel = "Add Field";

  dialogType = DIALOG_TYPE.INFO;

  searchChoices = [
    { name: 'Search PID', value: 'searchPID' },
    { name: 'Search Field Location', value: 'searchFieldLocation' },
    { name: 'Search Field ID', value: 'searchFieldId' }
  ];

  showSearchLegalMsg = false;

  addFieldForm = new FormGroup({
    choiceSelected: new FormControl('searchPID'),
    searchLegalLandOrFieldId: new FormControl()
  });
  
  constructor() {}

  get selectedChoice(): string {
    const selectedValue = this.addFieldForm.controls.choiceSelected.value
    const selectedOption = this.searchChoices.find(option => option.value === selectedValue)
    return selectedOption ? selectedOption.name : ''
  }

  clearAllForm() {
    this.showSearchLegalMsg = false;
  }

}
