import {Injectable} from "@angular/core";
import {SwUpdate} from "@angular/service-worker";
import {interval} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";
import {WFSnackbarComponent} from "../components/common/snackbars/wf-snackbar.component";
import {getSnackbarConfig} from "../utils/user-feedback-utils";
import {WF_SNACKBAR_TYPES} from "../utils";

@Injectable({providedIn: "root"})
export class UpdateService {
    constructor(public swUpdate: SwUpdate, public snackbarService: MatSnackBar) {
        console.log("cirras-underwriting swUpdate enabled:", swUpdate.isEnabled);
        if (swUpdate.isEnabled) {
            swUpdate.checkForUpdate(); //Check on load up, then check every interval
            interval(30 * 1000).subscribe(() => {
                return swUpdate.checkForUpdate();
            });
        }
    }

    public checkForUpdates(): void {

        if (this.swUpdate.isEnabled) {
            console.log("checkForUpdates -> swUpdate is enabled");

            this.swUpdate.versionUpdates.subscribe(event => {
                console.log("checkForUpdates -> versionUpdates.subscribe -> event.type = " + event.type );

                if (event.type === 'VERSION_READY') { // VERSION_DETECTED
                    console.log("current version is: ", event.currentVersion);
                    console.log("available version is: ", event.latestVersion);

                    let snackbarRef = this.snackbarService.openFromComponent(WFSnackbarComponent, getSnackbarConfig("A new version is available", WF_SNACKBAR_TYPES.UPDATE));
                    snackbarRef.onAction().subscribe(
                        () => {
                            this.swUpdate.activateUpdate().then(() => document.location.reload());
                        }
                    );
                }                
            });
        }
    }
}
