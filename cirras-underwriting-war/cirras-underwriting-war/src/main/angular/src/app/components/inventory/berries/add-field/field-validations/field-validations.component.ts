import { Component, Input } from '@angular/core';

@Component({
  selector: 'field-validations',
  templateUrl: './field-validations.component.html',
  styleUrl: './field-validations.component.scss',
  standalone: false
})
export class FieldValidationsComponent {
  @Input() validationMessages 
}
