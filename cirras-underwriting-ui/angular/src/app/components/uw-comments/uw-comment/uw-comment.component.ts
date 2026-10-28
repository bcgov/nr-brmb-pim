import { Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, FormsModule, ReactiveFormsModule } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { NgIf, DatePipe } from "@angular/common";
import { MatFormField } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { CdkTextareaAutosize } from "@angular/cdk/text-field";
import { MatIconButton } from "@angular/material/button";
import { MatTooltip } from "@angular/material/tooltip";
import { MatIcon } from "@angular/material/icon";

@Component({
    selector: 'uw-comment',
    templateUrl: './uw-comment.component.html',
    styleUrls: ['./uw-comment.component.scss'],
    imports: [FormsModule, ReactiveFormsModule, NgIf, MatFormField, MatInput, CdkTextareaAutosize, MatIconButton, MatTooltip, MatIcon, DatePipe]
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

        return this.uwComment.userCanEditInd

    }
}