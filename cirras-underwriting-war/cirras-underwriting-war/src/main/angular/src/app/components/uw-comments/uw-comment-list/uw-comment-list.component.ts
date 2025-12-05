import { Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";

@Component({
    selector: 'uw-comment-list',
    templateUrl: './uw-comment-list.component.html',
    styleUrls: ['./uw-comment-list.component.scss'],
    standalone: false
})
export class UwCommentListComponent {
    @Input() uwComments: UnderwritingComment[];
    @Input() uwCommentsFormArray: UntypedFormArray;

    notDeleted(uwComments: UnderwritingComment[]): UnderwritingComment[] {
        return uwComments.filter(uwComment => !uwComment.deletedByUserInd);
    }
}