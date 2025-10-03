import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { AnnualField } from 'src/app/conversion/models';
import { addSimplePlantingObject } from '../../inventory-common';

@Component({
  selector: 'berries-inventory-field',
  templateUrl: './berries-inventory-field.component.html',
  styleUrl: './berries-inventory-field.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class BerriesInventoryFieldComponent implements OnInit{

  @Input() field: AnnualField;
  @Input() fieldsFormArray: UntypedFormArray;

  fieldFormGroup: UntypedFormGroup;
  
  constructor(private fb: UntypedFormBuilder) {}

  ngOnInit(): void {

    let fldPlantings = new UntypedFormArray ([]) 

    this.field.plantings.forEach ( pltg => {
      
      let pltgInventoryBerries = new UntypedFormArray([])  // TODO
      let pltgInventoryUnseeded = new UntypedFormArray([])
      let pltgInventorySeededForages =  new UntypedFormArray ([])
      let pltgInventorySeededGrains = new UntypedFormArray ([])

      fldPlantings.push( this.fb.group( 
        addSimplePlantingObject( pltg, false, 
          pltgInventoryUnseeded, pltgInventoryBerries, pltgInventorySeededForages , pltgInventorySeededGrains) ))
      })

    this.fieldFormGroup = this.fb.group({
      annualFieldDetailId: [this.field.annualFieldDetailId],
      displayOrder: [this.field.displayOrder],
      fieldId: [this.field.fieldId],
      fieldLabel: [this.field.fieldLabel],
      otherLegalDescription: [this.field.otherLegalDescription],
      dopYieldFieldForageList: this.fb.array([]),
      plantings: [ fldPlantings ],
      uwComments: [this.field.uwComments],
    });

    this.fieldsFormArray.push(this.fieldFormGroup);
  }


  get fieldHiddenOnPrintoutInd(): boolean {
    return false; // for now

    // TODO: check if all plantings are hidden
  }


      setPlantingStyles() {
        return {
            'display': 'grid',
            'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
            'align-items': 'stretch',
            'width': `1200px`
        };
    }

}
