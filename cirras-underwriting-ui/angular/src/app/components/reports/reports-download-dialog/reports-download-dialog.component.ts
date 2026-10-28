import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogActions } from "@angular/material/dialog";
import { DIALOG_TYPE, BaseDialogComponent } from "../../dialogs/base-dialog/base-dialog.component";
import { INSURANCE_PLAN, REPORT_CHOICES, REPORT_SORT_BY, REPORT_TYPE_OPTIONS, SORT_BY_CHOICES } from "src/app/utils/constants";
import { CdkScrollable } from "@angular/cdk/scrolling";
import { NgIf, NgFor } from "@angular/common";
import { MatLabel, MatFormField } from "@angular/material/form-field";
import { MatSelect } from "@angular/material/select";
import { MatOption } from "@angular/material/autocomplete";
import { MatButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";

@Component({
    selector: 'reports-download-dialog',
    templateUrl: './reports-download-dialog.component.html',
    styleUrls: ['./reports-download-dialog.component.scss'],
    imports: [BaseDialogComponent, CdkScrollable, MatDialogContent, NgIf, MatLabel, MatFormField, MatSelect, NgFor, MatOption, MatDialogActions, MatButton, MatIcon]
})
export class ReportsDownloadDialogComponent implements OnInit {
    titleLabel = "Report Details";
    dialogType = DIALOG_TYPE.INFO;

    dataReceived: any;

    reportSortByOptions: any;
    selectedReportSortBy: string;

    reportTypeOptions: any;
    selectedReportType = "";

    constructor(public dialogRef: MatDialogRef<ReportsDownloadDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) { 
            if (data) {
                //capture the data that comes from the main page
                this.dataReceived = data;
              } 
        }

    ngOnInit(): void {
        this.reportSortByOptions = REPORT_SORT_BY;
        this.reportTypeOptions = REPORT_TYPE_OPTIONS;
    }

    onSelectionChange(event) {
        this.selectedReportSortBy = event.value;
    }

    onReportTypeSelectionChange(event) {
        this.selectedReportType = event.value;
    }

    onCancel() {
        this.dialogRef.close({ event: 'Cancel' });
    }

    onDownload() {
        this.dialogRef.close({ event: 'Download', sortColumn: this.selectedReportSortBy || SORT_BY_CHOICES.POLICY_NUMBER , reportType: this.selectedReportType});
    }

    isGrainInventoryReport(){
        if (this.dataReceived && this.dataReceived.insurancePlanId == INSURANCE_PLAN.GRAIN && this.dataReceived.reportChoice == REPORT_CHOICES.INVENTORY ) {
            return true
        } else {
            return false
        }
    }

    isDownloadPossible(){
        
        if (!this.selectedReportSortBy) {
            return false
        }

        if (this.isGrainInventoryReport() && this.selectedReportType == "") {
            return false
        }

        return true
    }

}