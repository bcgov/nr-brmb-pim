import {AbstractControl, ValidatorFn} from "@angular/forms";
import * as moment from "moment";


export function phoneNumberValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        // only validate primaryContact if the value is there to validate (if not required this will now pass)
        if (!control.value) {
            return null;
        }
        const pattern = /^\d{10}$/.test(control.value);
        if (!pattern) {
            return {phoneNumber: "This does not seem to match the pattern for a phone number"};
        }
        return null;
    };
}

export function emailValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        // only validate emailAddress if the value is there to validate (if not required this will now pass)
        if (!control.value) {
            return null;
        }
        const pattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(control.value.trim());
        if (!pattern) {
            return {emailAddress: "This does not seem to match the pattern for an email address"};
        }
        return null;
    };
}

// Found this validator here: https://stackblitz.com/edit/comparison-custom-validator
export function comparisonValidator(field: string, predicate: (fieldVal, fieldToCompareVal) => boolean): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        const group = control.parent;
        let valid = true;
        if (group) {
            const fieldToCompare = group.get(field);
            valid = predicate(control.value, fieldToCompare.value);
        }
        return valid ? null : {"comparison": {value: control.value}};
    };
}

/**
 * Validates that the start date, if set, is less than the end date, if set, otherwise, returns true.
 * @param startDate Moment object containing the start date
 * @param endDate Moment object containing the end date
 */
export function validateDateRange(startDate, endDate) {
    return startDate === null
        || startDate === ""
        || endDate === null
        || endDate === ""
        || moment(startDate).isBefore(moment(endDate));
}

export function requireMatch() {
    return (c: AbstractControl): { [key: string]: any } => {
        if (typeof c.value == "string") {
            return {"autoComplete.invalid": {valid: false}};
        } else {
            return null;
        }
    };
}

export function validateErrorState(key: string, errorMap: Map<string, string>): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
        let error = errorMap.get(key);
        return error ? {key: true} : null;
    };
}
