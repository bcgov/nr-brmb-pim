import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { SecurityUtilService } from 'src/app/services/security-util.service';


export interface FieldUwComment {
  fieldId?: number;
  annualFieldDetailId?: number;
  growerContractYearId?: number;
  declaredYieldContractGuid?: string;
  uwCommentTypeCode: string;
  uwComments: UnderwritingComment[]
}


@Component({
  selector: 'underwriting-comments',
  templateUrl: './underwriting-comments.component.html',
  styleUrls: ['./underwriting-comments.component.scss']
})

export class UnderwritingCommentsComponent implements OnInit {

  dataReceived: FieldUwComment;
  updatedComments: FieldUwComment = <FieldUwComment>{};
  uwCommentType: string;

  uwCommentForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<UnderwritingCommentsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FieldUwComment,
    private fb: FormBuilder,
    public securityUtilService: SecurityUtilService,   ) {  

      if (data) {
        //capture the comments data that comes from the main page
        this.dataReceived = data;
      } 

    }

  ngOnInit(): void {
 
      var self = this

      let fmaComments = new FormArray ([]) 

      if (this.dataReceived && this.dataReceived.uwComments && this.dataReceived.uwComments.length > 0) {
        // creates a form and puts the comments in the form, as they come from the main page 
        this.dataReceived.uwComments.forEach(function( comment: UnderwritingComment) {
          
          fmaComments.push( self.fb.group( {
            underwritingCommentGuid:     [ comment.underwritingCommentGuid],
            annualFieldDetailId:         [ self.dataReceived.annualFieldDetailId ],
            growerContractYearId:        [ comment.growerContractYearId ],
            declaredYieldContractGuid:   [ comment.declaredYieldContractGuid ],
            underwritingCommentTypeCode: [ comment.underwritingCommentTypeCode ],
            underwritingCommentTypeDesc: [ comment.underwritingCommentTypeDesc ],
            underwritingComment:         [ comment.underwritingComment ],
            createUser:                  [ comment.createUser ],
            createDate:                  [ comment.createDate.toString()] ,
            updateUser:                  [ comment.updateUser ],
            updateDate:                  [ comment.updateDate.toString() ] ,
            deletedByUserInd:            [ comment.deletedByUserInd ],
            userCanEditInd:              [ comment.userCanEditInd ] ,
            userCanDeleteInd:            [ comment.userCanDeleteInd ]
          }))
        })
      }      
      
      this.uwCommentForm = this.fb.group({
        comments:  fmaComments
      })

      if (this.dataReceived) {
        this.uwCommentType = self.dataReceived.uwCommentTypeCode;
      }
    }

  onAddNewComment() {
    var self = this
    let fmaComments: FormArray = this.uwCommentForm.controls.comments as FormArray

    fmaComments.push( self.fb.group( {
      underwritingCommentGuid:      [''],
      annualFieldDetailId:          [self.dataReceived.annualFieldDetailId ],
      growerContractYearId:         [self.dataReceived.growerContractYearId ],
      declaredYieldContractGuid:    [self.dataReceived.declaredYieldContractGuid ],
      underwritingCommentTypeCode:  [self.dataReceived.uwCommentTypeCode],  
      underwritingCommentTypeDesc:  [''],
      underwritingComment:          [''],
      createUser:                   [''],
      createDate:                   [''],
      updateUser:                   [''],
      updateDate:                   [''],
      deletedByUserInd:             [ false ],
      userCanEditInd:               [ true ],
      userCanDeleteInd:             [ true ] 
    } ) )

  }

  onCancelChanges() {
    this.dialogRef.close({event:'Cancel'});
  }

  onUpdateComments() {
    // updates all comments and passes the result to the main page
    if ( !this.isFormValid() ){
      return
    }

    var self = this

    this.updatedComments.fieldId = this.dataReceived.fieldId
    this.updatedComments.annualFieldDetailId = this.dataReceived.annualFieldDetailId
    this.updatedComments.growerContractYearId = this.dataReceived.growerContractYearId
    this.updatedComments.declaredYieldContractGuid = this.dataReceived.declaredYieldContractGuid
    this.updatedComments.uwCommentTypeCode = this.dataReceived.uwCommentTypeCode
    this.updatedComments.uwComments = []

    let fmaComments: FormArray = this.uwCommentForm.controls.comments as FormArray

    fmaComments.controls.forEach( function(fmComment : FormArray) {

      // don't send the deleted comments without underwritingCommentGuid to the main page
      // they were never in the database and they don't have to be deleted from there
      
      if (!fmComment.value.underwritingCommentGuid && fmComment.value.deletedByUserInd == true ) {
        // do nothing
      } else { 
        // send to the database for insert / update / delete
        self.updatedComments.uwComments.push({
          underwritingCommentGuid:      fmComment.value.underwritingCommentGuid, 
          annualFieldDetailId:          fmComment.value.annualFieldDetailId, 
          growerContractYearId:         fmComment.value.growerContractYearId,
          declaredYieldContractGuid:    fmComment.value.declaredYieldContractGuid,
          underwritingCommentTypeCode:  fmComment.value.underwritingCommentTypeCode, 
          underwritingCommentTypeDesc:  fmComment.value.underwritingCommentTypeDesc, 
          underwritingComment:          fmComment.value.underwritingComment, 
          createUser:                   fmComment.value.createUser, 
          createDate:                   fmComment.value.createDate, 
          updateUser:                   fmComment.value.updateUser, 
          updateDate:                   fmComment.value.updateDate, 
          deletedByUserInd:             fmComment.value.deletedByUserInd,
          userCanEditInd:               fmComment.value.userCanEditInd,
          userCanDeleteInd:             fmComment.value.userCanDeleteInd
        })
      }

    })

    this.dialogRef.close({event:'Update', data: this.updatedComments});
  }

  isFormValid() {

    let isValid = true;
    let frmComments: FormArray = this.uwCommentForm.controls.comments as FormArray

    frmComments.controls.forEach( function(fmComment : FormArray) {
      if (fmComment.value.userCanEditInd == true && (fmComment.value.deletedByUserInd == false || fmComment.value.deletedByUserInd == null) && fmComment.value.underwritingComment.trim().length == 0) {
        //Show message
        alert ("Please enter a valid comment");
        fmComment.controls['underwritingComment'].nativeElement.style.backgroundColor = 'yellow';
        isValid = false;
      }
    })

    return isValid;
  }

  onCommentChange(commentIndex) {

    const frmComments: FormArray = this.uwCommentForm.controls.comments as FormArray
    const fmComment: FormArray = frmComments.controls[commentIndex] as FormArray

    if (fmComment.value.underwritingComment.trim().length == 0) {
      //Show message
      alert ("Please enter a valid comment");
      fmComment.controls['underwritingComment'].nativeElement.style.backgroundColor = 'yellow';
    } else {
      fmComment.controls['underwritingComment'].nativeElement.style.backgroundColor = 'transparent';
    }
  }

  onDeleteComment(comment ){
      // mark the comment as deleted and don't show it in the popup
      comment.controls.deletedByUserInd.setValue(true)
  }

}
