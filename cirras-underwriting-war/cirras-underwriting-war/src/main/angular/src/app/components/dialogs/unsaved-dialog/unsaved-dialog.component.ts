import {ChangeDetectionStrategy, Component, Inject} from "@angular/core";
import {DIALOG_TYPE} from "../base-dialog/base-dialog.component";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
    selector: 'base-unsaved-dialog',
    templateUrl: './unsaved-dialog.component.html',
    styleUrls: ['../../common/base/base.component.scss', './unsaved-dialog.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class UnsavedDialogComponent {
    titleLabel = "Unsaved Changes";

    dialogType = DIALOG_TYPE.INFO;

    constructor(
        public dialogRef: MatDialogRef<UnsavedDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public displayLabel: string) {
        dialogRef.disableClose = true;
    }

    ok(): void {
        this.dialogRef.close("continue");
    }

    cancel() {
        this.dialogRef.close("cancel");
    }

}
