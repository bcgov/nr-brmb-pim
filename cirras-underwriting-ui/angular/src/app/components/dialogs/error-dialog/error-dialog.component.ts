import {ERROR_TYPE, ErrorState} from "../../../store/application/application.state";
import {ChangeDetectionStrategy, Component, Inject} from "@angular/core";
import { DIALOG_TYPE, BaseDialogComponent } from "../base-dialog/base-dialog.component";
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from "@angular/material/dialog";
import { CdkScrollable } from "@angular/cdk/scrolling";
import { NgIf } from "@angular/common";
import { MatRadioGroup, MatRadioButton } from "@angular/material/radio";
import { FormsModule } from "@angular/forms";
import { MatButton } from "@angular/material/button";

@Component({
    selector: 'base-error-dialog',
    templateUrl: './error-dialog.component.html',
    styleUrls: ['../../common/base/base.component.scss', './error-dialog.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [BaseDialogComponent, CdkScrollable, MatDialogContent, NgIf, MatRadioGroup, FormsModule, MatRadioButton, MatDialogActions, MatButton]
})
export class ErrorDialogComponent {
    titleLabel = "Error";
    selectedAction: string;
    type: ERROR_TYPE;
    message: string;
    ERROR_TYPE_OBJ = ERROR_TYPE;

    dialogType = DIALOG_TYPE.ERROR;

    constructor(
        public dialogRef: MatDialogRef<ErrorDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public error: ErrorState) {
        dialogRef.disableClose = true;
        this.type = error.type;
        this.message = error.message;
        if (this.type == ERROR_TYPE.FAILED_PRECONDITION) {
            this.titleLabel = "Error - Update Conflict";
        }
    }

    ok(): void {
        this.dialogRef.close(this.selectedAction);
    }

    cancel() {
        this.dialogRef.close("cancel");
    }

}
