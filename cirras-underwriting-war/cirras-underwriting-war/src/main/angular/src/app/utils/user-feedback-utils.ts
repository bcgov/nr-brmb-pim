import {WFSnackbarComponent} from "../components/common/snackbars/wf-snackbar.component";
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";
import {ErrorState} from "../store/application/application.state";
import {WF_SNACKBAR_TYPES} from "./index";

export interface ErrorHandlingInstructions {
    redirectToRoute?: string;
    redirectToRouteData?: any;
    snackBarErrorMsg?: string;
}

//
// export const SNACKBAR_INFO_CONFIG = {panelClass: "snackbar-success", duration: 8500};
//
// export const SNACKBAR_ERROR_CONFIG = {panelClass: "snackbar-error", duration: 8500};
// export const SNACKBAR_DISMISS_ERROR_CONFIG = {panelClass: "snackbar-error"};
// export const SNACKBAR_DISMISS_WARNING_CONFIG = {panelClass: "snackbar-warning"};
// export const SNACKBAR_WARNING_CONFIG = {panelClass: "snackbar-warning", duration: 8500};


export function getSnackbarConfig(message, type): MatSnackBarConfig {
    let config = {
        panelClass: "snackbar-" + type,
        data: {
            message: message,
            type: type
        }
    };
    if (type == WF_SNACKBAR_TYPES.SUCCESS) {
        config['duration'] = 5000;
    }
    return config;
}

export function displaySuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel, WF_SNACKBAR_TYPES.SUCCESS));
}

export function displaySaveSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel + " saved successfully.", WF_SNACKBAR_TYPES.SUCCESS));
}

export function displayUpdateSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
  service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel + " successfully.", WF_SNACKBAR_TYPES.SUCCESS));
}

export function displayDeleteSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel + " deleted successfully.", WF_SNACKBAR_TYPES.SUCCESS));
}

export function displayRemoveSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel + " removed successfully.", WF_SNACKBAR_TYPES.SUCCESS));
}

export function displayCreateSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(displayLabel + " created successfully.", WF_SNACKBAR_TYPES.SUCCESS));
}


export function displayErrorMessage(service: MatSnackBar, message: string) {
    service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(message, WF_SNACKBAR_TYPES.ERROR));
}

export function displayNotFound(service: MatSnackBar, error: ErrorState) {
    if (error && error.message) {
        setTimeout(() => {
            service.openFromComponent(WFSnackbarComponent, getSnackbarConfig(error.message, WF_SNACKBAR_TYPES.ERROR));
        });
    } else {
        setTimeout(() => {
            service.openFromComponent(WFSnackbarComponent, getSnackbarConfig("Not Found", WF_SNACKBAR_TYPES.ERROR));
        });
    }
}
