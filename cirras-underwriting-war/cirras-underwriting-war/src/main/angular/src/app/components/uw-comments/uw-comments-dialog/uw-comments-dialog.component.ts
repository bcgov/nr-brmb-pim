import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DialogData } from "../uw-comments-button/uw-comments-button.component";
import { DIALOG_TYPE } from "../../dialogs/base-dialog/base-dialog.component";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { FormArray } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { UW_COMMENT_TYPE_CODE } from "src/app/utils/constants";

@Component({
    selector: 'uw-comments-dialog',
    templateUrl: './uw-comments-dialog.component.html',
    styleUrls: ['./uw-comments-dialog.component.scss']
})
export class UwCommentsDialogComponent {
    dialogType = DIALOG_TYPE.INFO;
    uwCommentsFormArray = new FormArray([]);

    constructor(public dialogRef: MatDialogRef<UwCommentsDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DialogData,
        public securityUtilService: SecurityUtilService) { }

    get titleLabel(): string {
        switch (this.data.underwritingCommentTypeCode) {
            case UW_COMMENT_TYPE_CODE.INVENTORY_GENERAL:
                return "Inventory Comments";
            case UW_COMMENT_TYPE_CODE.DOP_GENERAL:
                return "DOP Comments";
            default:
                return "Underwriting Comments";
        }
    }

    hasComments(): boolean {
        return this.data.uwComments.filter(uwComment => !uwComment.deletedByUserInd).length > 0;
    }

    onAddNewComment() {
        this.data.uwComments.unshift({
            underwritingCommentGuid: Math.floor(Math.random() * 1000000).toString(),
            annualFieldDetailId: this.data.annualFieldDetailId,
            underwritingCommentTypeCode: this.data.underwritingCommentTypeCode,
            underwritingCommentTypeDesc: '',
            underwritingComment: '',
            growerContractYearId: this.data.growerContractYearId,
            declaredYieldContractGuid: this.data.declaredYieldContractGuid,
            createUser: '',
            createDate: '',
            updateUser: '',
            updateDate: '',
            deletedByUserInd: false,
            userCanEditInd: true,
            userCanDeleteInd: true
        } as UnderwritingComment);
    }

    onCancel() {
        this.dialogRef.close({ event: 'Cancel' });
    }

    onDone() {
        // check if any comments are empty
        if (this.data.uwComments.some(uwComment => !uwComment.underwritingComment)) {
            alert("Please enter a valid comment");
            return;
        }

        this.dialogRef.close({ event: 'Done' });
    }
}