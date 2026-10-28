import { Component, Input } from '@angular/core';
import { NgFor, NgStyle } from '@angular/common';
import { MatIcon } from '@angular/material/icon';

@Component({
    selector: 'field-validations',
    templateUrl: './field-validations.component.html',
    styleUrl: './field-validations.component.scss',
    imports: [NgFor, MatIcon, NgStyle]
})
export class FieldValidationsComponent {
  @Input() validationMessages 
  
}
