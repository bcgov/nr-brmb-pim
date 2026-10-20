import {PitSnackbarComponent} from "../components/common/snackbars/pit-snackbar.component";
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";
import {ErrorState} from "../store/application/application.state";
import {PIT_SNACKBAR_TYPES} from "./index";

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
    if (type == PIT_SNACKBAR_TYPES.SUCCESS) {
        config['duration'] = 5000;
    }
    return config;
}

export function displaySuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel, PIT_SNACKBAR_TYPES.SUCCESS));
}

export function displaySaveSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel + " saved successfully.", PIT_SNACKBAR_TYPES.SUCCESS));
}

export function displayUpdateSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
  service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel + " successfully.", PIT_SNACKBAR_TYPES.SUCCESS));
}

export function displayDeleteSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel + " deleted successfully.", PIT_SNACKBAR_TYPES.SUCCESS));
}

export function displayRemoveSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel + " removed successfully.", PIT_SNACKBAR_TYPES.SUCCESS));
}

export function displayCreateSuccessSnackbar(service: MatSnackBar, displayLabel: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(displayLabel + " created successfully.", PIT_SNACKBAR_TYPES.SUCCESS));
}


export function displayErrorMessage(service: MatSnackBar, message: string) {
    service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(message, PIT_SNACKBAR_TYPES.ERROR));
}

export function displayNotFound(service: MatSnackBar, error: ErrorState) {
    if (error && error.message) {
        setTimeout(() => {
            service.openFromComponent(PitSnackbarComponent, getSnackbarConfig(error.message, PIT_SNACKBAR_TYPES.ERROR));
        });
    } else {
        setTimeout(() => {
            service.openFromComponent(PitSnackbarComponent, getSnackbarConfig("Not Found", PIT_SNACKBAR_TYPES.ERROR));
        });
    }
}
