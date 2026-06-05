import { Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";

@Component({
    selector: 'uw-comment',
    templateUrl: './uw-comment.component.html',
    styleUrls: ['./uw-comment.component.scss'],
    standalone: false
})
export class UwCommentComponent implements OnInit {
    @Input() uwComment: UnderwritingComment;
    @Input() uwCommentsFormArray: UntypedFormArray;
    @Input() showForcedCommentsColumn: Boolean;

    uwCommentFormGroup: UntypedFormGroup;
    validComment: boolean = true;

    constructor(private fb: UntypedFormBuilder) {
    }

    ngOnInit(): void {
        this.uwCommentFormGroup = this.fb.group({
            underwritingCommentGuid: [this.uwComment.underwritingCommentGuid],
            createDate: [this.uwComment.createDate],
            updateDate: [this.uwComment.updateDate],
            updateUser: [this.uwComment.updateUser],
            underwritingComment: [this.uwComment.underwritingComment],
            isForcedInd: [this.uwComment.isForcedInd],
            userCanEditInd: [this.uwComment.userCanEditInd]
        });
        this.uwCommentsFormArray.push(this.uwCommentFormGroup);
    }

    onCommentChange() {
        const underwritingComment = this.uwCommentFormGroup.value.underwritingComment;
        this.validComment = underwritingComment && underwritingComment.trim().length > 0;
        if (!this.validComment) {
            alert("Please enter a valid comment");
        } else {
            this.uwComment.underwritingComment = underwritingComment;
        }
    }

    onDeleteComment() {
        // mark the underwriting comment as deleted
        this.uwComment.deletedByUserInd = true;

        // remove the underwriting comment from the form array
        const index = this.uwCommentsFormArray.value.findIndex(uwComment => uwComment.underwritingCommentGuid === this.uwComment.underwritingCommentGuid);
        if (index !== -1) {
            this.uwCommentsFormArray.removeAt(index);
        }
    }

    canUserEditComment() {

        if (!this.uwComment.userCanEditInd) {
            return false
        }

        if (this.uwComment.isForcedInd ) {
            if (this.uwComment.underwritingCommentGuid.charAt(0) == '-') {
                // it's a brand new comment , not saved in the database yet -> allow editing
                return true
            } else {
                return false
            }
        } else {
            return true
        }
    }
}