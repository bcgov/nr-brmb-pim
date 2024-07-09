import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { UwCommentsDialogComponent } from "../uw-comments-dialog/uw-comments-dialog.component";

export interface DialogData {
    underwritingCommentTypeCode: string;
    annualFieldDetailId: number;
    growerContractYearId: number;
    declaredYieldContractGuid: string;
    policyNumber: string;
    growerName: string;
    growerNumber: string;
    fieldId: number;
    fieldName: string;
    legalLocation: string;
    uwComments: UnderwritingComment[];
}

@Component({
    selector: 'uw-comments-button',
    templateUrl: './uw-comments-button.component.html',
    styleUrls: ['./uw-comments-button.component.scss']
})
export class UwCommentsButtonComponent {
    @Input() underwritingCommentTypeCode: string;
    @Input() annualFieldDetailId: number;
    @Input() growerContractYearId: number;
    @Input() declaredYieldContractGuid: string;
    @Input() policyNumber: string;
    @Input() growerName: string;
    @Input() growerNumber: string;
    @Input() fieldId: number;
    @Input() fieldName: string;
    @Input() legalLocation: string;
    @Input() uwComments: UnderwritingComment[];
    @Output() onDone = new EventEmitter<UnderwritingComment[]>();

    uwCommentsForDialog: UnderwritingComment[];
    dialogRef: MatDialogRef<UwCommentsDialogComponent, any>;

    constructor(protected dialog: MatDialog) { }

    ngOnChanges(): void {
        this.uwCommentsForDialog = JSON.parse(JSON.stringify(this.uwComments)) || [];

        // refresh dialog data if dialog is open
        if (this.dialogRef && this.dialogRef.componentInstance) {
            this.dialogRef.componentInstance.data = this.data;
        }
    }

    hasComments(): boolean {
        return this.uwCommentsForDialog.filter(uwComment => !uwComment.deletedByUserInd).length > 0;
    }

    get data(): DialogData {
        return {
            underwritingCommentTypeCode: this.underwritingCommentTypeCode,
            annualFieldDetailId: this.annualFieldDetailId,
            growerContractYearId: this.growerContractYearId,
            declaredYieldContractGuid: this.declaredYieldContractGuid,
            policyNumber: this.policyNumber,
            growerName: this.growerName,
            growerNumber: this.growerNumber,
            fieldId: this.fieldId,
            fieldName: this.fieldName,
            legalLocation: this.legalLocation,
            uwComments: this.uwCommentsForDialog
        };
    }

    onLoadComments() {
        this.dialogRef = this.dialog.open(UwCommentsDialogComponent, {
            width: '1136px',
            data: this.data,
            autoFocus: false,
            closeOnNavigation: false,
            panelClass: 'wf-dialog'
        });

        const self = this;
        this.dialogRef.afterClosed().subscribe(result => {
            if (result?.event == 'Done') {
                this.onDone.emit(self.uwCommentsForDialog.map(comment => {
                    return {
                        ...comment,
                        // erase guid for new comments
                        underwritingCommentGuid: comment.createDate ? comment.underwritingCommentGuid : null,
                    };
                }));
            } else if (result?.event == 'Cancel') {
                // restore uwCommentsForDialog to its original state
                this.uwCommentsForDialog = JSON.parse(JSON.stringify(this.uwComments)) || [];
            }
        });
    }
}