import { Component, Inject, Input, OnChanges, OnInit , SimpleChanges} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UntypedFormArray, UntypedFormBuilder, FormControl, UntypedFormGroup } from '@angular/forms';
import { Store} from "@ngrx/store";
import { RootState} from "../../../../store";
import { GradeModifierType, GradeModifierTypeList } from 'src/app/conversion/models-maintenance';
import { loadGradeModifierTypes, saveGradeModifierTypes } from 'src/app/store/maintenance/maintenance.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { INSURANCE_PLAN } from 'src/app/utils/constants';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { areNotEqual, makeNumberOnly } from 'src/app/utils';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { DIALOG_TYPE } from 'src/app/components/dialogs/base-dialog/base-dialog.component';

@Component({
  selector: 'grade-modifiers-types',
  templateUrl: './grade-modifiers-types.component.html',
  styleUrls: ['./grade-modifiers-types.component.scss']
})
export class GradeModifiersTypesComponent implements OnInit, OnChanges {

  @Input() gradeModifierTypesList: GradeModifierTypeList

  titleLabel = 'Maintain Grade Modifier Types';
  dialogType = DIALOG_TYPE.INFO;

  dataReceived: any;
  gradeModifierTypesForm: UntypedFormGroup;

  hasDataChanged = false;

  constructor(
    public dialogRef: MatDialogRef<GradeModifiersTypesComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,  
    private fb: UntypedFormBuilder,  
    protected store: Store<RootState>,
    public securityUtilService: SecurityUtilService, 
    ) {  

      if (data) {
        //capture the data that comes from the main page
        this.dataReceived = data;
      } 
    }

  ngOnInit(): void {
    
    // initialize the form
    this.gradeModifierTypesForm = this.fb.group({
      gradeModifierTypes: new UntypedFormArray([])
    })

    setTimeout(() => { 
      this.store.dispatch(loadGradeModifierTypes(MAINTENANCE_COMPONENT_ID, ""))
    })
  }


  ngOnChanges(changes: SimpleChanges): void {

    if (changes.gradeModifierTypesList) {
      
      if ( this.gradeModifierTypesList && this.gradeModifierTypesList.collection && this.gradeModifierTypesList.collection.length > 0 && this.gradeModifierTypesForm) {
        // pre-fill the form
        
        let frmgGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray
        frmgGradeModifierTypes.clear()

        this.gradeModifierTypesList.collection.forEach ((gmt: GradeModifierType) => {

          frmgGradeModifierTypes.push( this.fb.group( {  
            gradeModifierTypeCode:  [ gmt.gradeModifierTypeCode ],
            description:        [ gmt.description ],
            effectiveYear:      [ gmt.effectiveYear ],
            expiryYear:         [ gmt.expiryYear ], 
            deleteAllowedInd:   [ gmt.deleteAllowedInd ],
            deletedByUserInd:   [ gmt.deletedByUserInd ],
            maxYearUsed:        [ gmt.maxYearUsed ],
            addedByUserInd:     [ false ]
          } ) )
        })

        this.gradeModifierTypesForm.valueChanges.subscribe(val => {this.isMyFormDirty()})
      }
    }
  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onAddGradeModifierType() {
    const frmGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray

    frmGradeModifierTypes.push (this.fb.group({
      gradeModifierTypeCode: [],
      description:           [],
      effectiveYear:         [ this.dataReceived.cropYear ],
      expiryYear:            [ 9999 ],  
      deleteAllowedInd:      [true], 
      deletedByUserInd:      [ false ],
      addedByUserInd:        [ true ],
      maxYearUsed:           [ 0 ]
    }))
  }

  onDeleteGradeModifierType(rowIndex) {
    const frmGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray
    const frmGMT = frmGradeModifierTypes.controls[rowIndex] as UntypedFormArray

    frmGMT.controls['deletedByUserInd'].setValue(true)

    this.isMyFormDirty()
  }

  onSave() {
    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updated seeding deadlines 
    const newGradeModifierTypes: GradeModifierTypeList = this.getUpdatedGradeModifierTypes()

    // save
    this.store.dispatch(saveGradeModifierTypes(MAINTENANCE_COMPONENT_ID, "", newGradeModifierTypes))
    
    this.hasDataChanged = false   

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false));
  }

  isFormValid() {
    
    const frmGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray

    for (let i = 0; i < frmGradeModifierTypes.controls.length; i++) {

      let frmGMT = frmGradeModifierTypes.controls[i] as UntypedFormArray
      let maxYearUsed = (!isNaN(parseInt(frmGMT.value.maxYearUsed))) ? parseInt(frmGMT.value.maxYearUsed) : 0
      
      if (!frmGMT.value.gradeModifierTypeCode || !frmGMT.value.description ) {
        alert (" The Grade Modifier Type or the Description should not be empty.")
        return false
      }

      if ( isNaN(parseInt(frmGMT.value.effectiveYear)) || 
            ( !isNaN(parseInt(frmGMT.value.effectiveYear)) &&
              (2000 > parseInt(frmGMT.value.effectiveYear) ||
              parseInt(frmGMT.value.effectiveYear) > 9999 ) )) {

          // Effective year should not be empty 
          alert("The Effective Year for grade modifier type " + frmGMT.value.gradeModifierTypeCode + " should be a number between 2000 and 9999")
          return false
      }

      if ( isNaN(parseInt(frmGMT.value.expiryYear)) ||
            (
              !isNaN(parseInt(frmGMT.value.expiryYear)) && 
              ( parseInt(frmGMT.value.expiryYear) < maxYearUsed || parseInt(frmGMT.value.expiryYear) > 9999 )
            ) 
          ) {

          // Expiry year should not be empty 
          alert("The Expiry Year for grade modifier type " + frmGMT.value.gradeModifierTypeCode + 
                " should be a number between " + 
                (maxYearUsed > 0 && parseInt(frmGMT.value.effectiveYear) < maxYearUsed ? maxYearUsed : parseInt(frmGMT.value.effectiveYear) ) + " and 9999")
          return false
      }

      if ( !isNaN(parseInt(frmGMT.value.effectiveYear)) && !isNaN(parseInt(frmGMT.value.expiryYear) ) ) {  

        // expiryYear >= effectiveYear
        if ( parseInt(frmGMT.value.expiryYear) < parseInt(frmGMT.value.effectiveYear)) {
          alert("The expiry year for grade modifier type " + frmGMT.value.gradeModifierTypeCode + " should be bigger than " + frmGMT.value.effectiveYear)
          return false
        }
      }

      for (let j = i + 1; j < frmGradeModifierTypes.controls.length; j++) {

        if ( frmGradeModifierTypes.controls[i].value.gradeModifierTypeCode == frmGradeModifierTypes.controls[j].value.gradeModifierTypeCode ) {
          // gradeModifierTypeCode should be unique
          alert("Grade Modifier Type should be unique")
          return false
        }
      }
    }
    
    return true
  }

  getUpdatedGradeModifierTypes() {
     //make a deep copy
    let updatedGradeModifierTypes : GradeModifierTypeList = JSON.parse(JSON.stringify(this.gradeModifierTypesList));

    const frmGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray
 
    frmGradeModifierTypes.controls.forEach( function(frmGMT : UntypedFormArray) {
 
      // find the corresponding field in updatedDopYieldContract object
      let origGradeModifierType = updatedGradeModifierTypes.collection.find( el => el.gradeModifierTypeCode == frmGMT.value.gradeModifierTypeCode)
      
      if (origGradeModifierType) { 
        // update
        origGradeModifierType.description = frmGMT.value.description.trim()
        origGradeModifierType.expiryYear = frmGMT.value.expiryYear
        origGradeModifierType.deletedByUserInd = frmGMT.value.deletedByUserInd
      } else {
        // add new
        if (! (frmGMT.value.deletedByUserInd == true) ) {
        updatedGradeModifierTypes.collection.push({
          gradeModifierTypeCode:             frmGMT.value.gradeModifierTypeCode.trim(),
          description:                       frmGMT.value.description.trim(),
          effectiveYear:                     frmGMT.value.effectiveYear,
          expiryYear:                        frmGMT.value.expiryYear,
          deleteAllowedInd:                  true,
          deletedByUserInd:                  false,
          maxYearUsed:                        0,
          })
        }
      }
    })
 
    return updatedGradeModifierTypes
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.gradeModifierTypesList) return false;

    const frmGradeModifierTypes: UntypedFormArray = this.gradeModifierTypesForm.controls.gradeModifierTypes as UntypedFormArray

    for (let i = 0; i < frmGradeModifierTypes.controls.length; i++) {

      let frmGMT = frmGradeModifierTypes.controls[i] as UntypedFormArray

      if ( frmGMT.value.addedByUserInd == true) {
        return true
      }

      let originalGMT = this.gradeModifierTypesList.collection.find( gmt => gmt.gradeModifierTypeCode == frmGMT.value.gradeModifierTypeCode)

      if (originalGMT) {
        
        if ( frmGMT.value.deletedByUserInd == true) {
          return true
        }

        if ( areNotEqual (originalGMT.description, frmGMT.value.description.trim() )	
        || areNotEqual (originalGMT.expiryYear, frmGMT.value.expiryYear )	) {
          return true
        }
      }
    }
    return false
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

}
