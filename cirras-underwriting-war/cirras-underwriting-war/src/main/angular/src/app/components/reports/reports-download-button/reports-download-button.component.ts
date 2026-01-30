import { Component, Input } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { INSURANCE_PLAN, REPORT_CHOICES } from "src/app/utils/constants";
import { ReportsDownloadDialogComponent } from "../reports-download-dialog/reports-download-dialog.component";
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { GetInventoryReport } from "src/app/store/inventory/inventory.actions";
import { GetDopReport } from "src/app/store/dop/dop.actions";

@Component({
    selector: 'reports-download-button',
    templateUrl: './reports-download-button.component.html',
    styleUrls: ['./reports-download-button.component.scss'],
    standalone: false
})
export class ReportsDownloadButtonComponent {
    @Input() reportChoice: string;
    @Input() policyId: string;
    @Input() cropYear: string;
    @Input() insurancePlanId: string;
    @Input() officeId: string;
    @Input() policyStatusCode: string;
    @Input() policyNumber: string;
    @Input() growerInfo: string;

    constructor(protected dialog: MatDialog,
        protected store: Store<RootState>) { }

    isPrintDisabled(): boolean {
        
        if (Number(this.insurancePlanId) == INSURANCE_PLAN.GRAIN || Number(this.insurancePlanId) == INSURANCE_PLAN.FORAGE) {
            return false
        }

        if (Number(this.insurancePlanId) == INSURANCE_PLAN.BERRIES && this.reportChoice == REPORT_CHOICES.INVENTORY) {
            // only the inventory report for berries is ready; the DOP report is not ready yet
            return false
        }

        return true // default
    }

    onReportPrint() {
        const dialogRef = this.dialog.open(ReportsDownloadDialogComponent, {
            width: '341px',
            data: {
                insurancePlanId: this.insurancePlanId,
                reportChoice: this.reportChoice
            },
            autoFocus: false,
            closeOnNavigation: false,
            panelClass: 'wf-dialog'
        });

        const self = this;
        dialogRef.afterClosed().subscribe(result => {
            if (result?.event == 'Download' && self.isSearchValid()) {
                switch (this.reportChoice) {
                    case REPORT_CHOICES.INVENTORY:
                        self.inventoryBatchPrint(result.sortColumn, result.reportType);
                        break;
                    case REPORT_CHOICES.DOP:
                        self.dopReportPrint(result.sortColumn);
                        break;
                }
            }
        });
    }

    isSearchValid() {
        if (!this.growerInfo && !this.policyNumber) {
            return true;
        }

        if (this.growerInfo && this.growerInfo.length > 2) {
            return true
        }

        if (this.policyNumber && this.policyNumber.length > 4) {
            return true
        }

        return false
    }

    inventoryBatchPrint(sortColumn: string, reportType: string) {
        this.store.dispatch(GetInventoryReport("Inventory-batch.pdf",
            this.policyId,
            this.cropYear,
            this.insurancePlanId,
            this.officeId,
            this.policyStatusCode,
            this.policyNumber,
            this.growerInfo,
            sortColumn,
            reportType
        ));
    }

    dopReportPrint(sortColumn: string) {
        this.store.dispatch(GetDopReport("DOP-batch.pdf",
            this.policyId,
            this.cropYear,
            this.insurancePlanId,
            this.officeId,
            this.policyStatusCode,
            this.policyNumber,
            this.growerInfo,
            sortColumn
        ));
    }
}