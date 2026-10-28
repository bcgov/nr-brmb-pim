import { Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { NgIf, NgFor } from "@angular/common";
import { UwCommentComponent } from "../uw-comment/uw-comment.component";

@Component({
    selector: 'uw-comment-list',
    templateUrl: './uw-comment-list.component.html',
    styleUrls: ['./uw-comment-list.component.scss'],
    imports: [NgIf, NgFor, UwCommentComponent]
})
export class UwCommentListComponent {
    @Input() uwComments: UnderwritingComment[];
    @Input() uwCommentsFormArray: UntypedFormArray;
    @Input() showForcedCommentsColumn: Boolean;

    notDeleted(uwComments: UnderwritingComment[]): UnderwritingComment[] {
        return uwComments.filter(uwComment => !uwComment.deletedByUserInd);
    }
}