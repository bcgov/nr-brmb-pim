import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DialogData } from "../uw-comments-button/uw-comments-button.component";
import { DIALOG_TYPE } from "../../dialogs/base-dialog/base-dialog.component";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { UntypedFormArray } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { INSURANCE_PLAN, UW_COMMENT_TYPE_CODE } from "src/app/utils/constants";

@Component({
    selector: 'uw-comments-dialog',
    templateUrl: './uw-comments-dialog.component.html',
    styleUrls: ['./uw-comments-dialog.component.scss'],
    standalone: false
})
export class UwCommentsDialogComponent implements OnInit {
    dialogType = DIALOG_TYPE.INFO;
    uwCommentsFormArray = new UntypedFormArray([]);

    constructor(public dialogRef: MatDialogRef<UwCommentsDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DialogData,
        public securityUtilService: SecurityUtilService) { }

    get titleLabel(): string {
        switch (this.data.underwritingCommentTypeCode) {
            case UW_COMMENT_TYPE_CODE.INVENTORY_GENERAL:
                return "Inventory Comments";
            case UW_COMMENT_TYPE_CODE.DOP_GENERAL:
                if (this.data.insurancePlanId == INSURANCE_PLAN.BERRIES) {
                    return "Yield Comments";
                } else {
                    return "DOP Comments";
                }                
            case UW_COMMENT_TYPE_CODE.VERIFIED_YIELD_GENERAL:
                return "Verified Yield Summary Comments"
            default:
                return "Underwriting Comments";
        }
    }

    get legalDescriptionLabel(): string {
        switch (this.data.insurancePlanId) {
            case INSURANCE_PLAN.GRAIN:
            case INSURANCE_PLAN.FORAGE:
                return "Legal Location";
            case INSURANCE_PLAN.BERRIES:
                return "Legal Description"
            default:
                return "";
        }
    }

    get legalDescription(): string {
        switch (this.data.insurancePlanId) {
            case INSURANCE_PLAN.GRAIN:
            case INSURANCE_PLAN.FORAGE:
                return this.data.legalLocation;

            case INSURANCE_PLAN.BERRIES:
                return this.data.primaryPropertyIdentifier;
            default:
                return "";
        }
    }

    get fieldLabel(): string {
        switch (this.data.insurancePlanId) {
            case INSURANCE_PLAN.GRAIN:
            case INSURANCE_PLAN.FORAGE:
                return "Field Name";
            case INSURANCE_PLAN.BERRIES:
                if (this.data.fieldLocation) {
                    return "Field Address"
                } else {
                    return "Bog Name" 
                }
            default:
                return "";
        }
    }

    get fieldNameOrAddress(): string {
        switch (this.data.insurancePlanId) {
            case INSURANCE_PLAN.GRAIN:
            case INSURANCE_PLAN.FORAGE:
                return this.data.fieldName;
            case INSURANCE_PLAN.BERRIES:
                if (this.data.fieldLocation) {
                    return this.data.fieldLocation  
                } else {
                    return this.data.fieldName
                }
            default:
                return "";
        }
    }

    ngOnInit() {
        if (this.data && this.data.isForcedInd) {
            this.onAddNewComment()
        }
    }

    hasComments(): boolean {
        return this.data.uwComments.filter(uwComment => !uwComment.deletedByUserInd).length > 0;
    }

    showForcedCommentColumn(){
        // show is Forced Comment column only for Berries Yield for now
        if (this.data.insurancePlanId == INSURANCE_PLAN.BERRIES && 
            this.data.underwritingCommentTypeCode == UW_COMMENT_TYPE_CODE.DOP_GENERAL) {

                return true
        } else {

            return false
        }
    }

    onAddNewComment() {
        this.data.uwComments.unshift({
            underwritingCommentGuid: Math.floor(Math.random() * -1000000).toString(), // if it's negative it's an easy way to recognize that it's a new comment. It never gets passed to the backend
            annualFieldDetailId: this.data.annualFieldDetailId,
            underwritingCommentTypeCode: this.data.underwritingCommentTypeCode,
            underwritingCommentTypeDesc: '',
            underwritingComment: '',
            growerContractYearId: this.data.growerContractYearId,
            declaredYieldContractGuid: this.data.declaredYieldContractGuid,
            verifiedYieldSummaryGuid: this.data.verifiedYieldSummaryGuid,
            isForcedInd: this.data.isForcedInd,
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
        if (this.data.uwComments.some(uwComment => ( !uwComment.underwritingComment && !uwComment.deletedByUserInd))) {
            alert("Please enter a valid comment");
            return;
        }

        this.dialogRef.close({ event: 'Done' });
    }
}