import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { DIALOG_TYPE } from "../../dialogs/base-dialog/base-dialog.component";
import { REPORT_SORT_BY, SORT_BY_CHOICES } from "src/app/utils/constants";

@Component({
    selector: 'reports-download-dialog',
    templateUrl: './reports-download-dialog.component.html',
    styleUrls: ['./reports-download-dialog.component.scss']
})
export class ReportsDownloadDialogComponent implements OnInit {
    titleLabel = "Report Details";
    dialogType = DIALOG_TYPE.INFO;

    reportSortByOptions: any;
    selectedReportSortBy: string;

    constructor(public dialogRef: MatDialogRef<ReportsDownloadDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) { }

    ngOnInit(): void {
        this.reportSortByOptions = REPORT_SORT_BY;
    }

    onSelectionChange(event) {
        this.selectedReportSortBy = event.value;
    }

    onCancel() {
        this.dialogRef.close({ event: 'Cancel' });
    }

    onDownload() {
        this.dialogRef.close({ event: 'Download', sortColumn: this.selectedReportSortBy || SORT_BY_CHOICES.POLICY_NUMBER });
    }
}