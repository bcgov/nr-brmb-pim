import { Component, Input } from "@angular/core";
import { FormArray } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";

@Component({
    selector: 'uw-comment-list',
    templateUrl: './uw-comment-list.component.html',
    styleUrls: ['./uw-comment-list.component.scss']
})
export class UwCommentListComponent {
    @Input() uwComments: UnderwritingComment[];
    @Input() uwCommentsFormArray: FormArray;

    notDeleted(uwComments: UnderwritingComment[]): UnderwritingComment[] {
        return uwComments.filter(uwComment => !uwComment.deletedByUserInd);
    }
}