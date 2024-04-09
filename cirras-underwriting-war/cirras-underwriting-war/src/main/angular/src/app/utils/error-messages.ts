import {ValidationError} from "../store/application/application.state";
import * as moment from "moment";
import {DATE_FORMATS} from "./index";
import {getCodeOptions, getDescriptionForCode} from "./code-table-utils";

const digits = () => `must contain only numbers`;
const required = () => `is required`;
const invalid = () => `is invalid`;
const invalidCode = (code) => `is invalid: ${code}`;
const notFound = () => `not found`;
const min = ([message, minVal]: any[]) => `cannot be less than ${minVal}`;
const max = ([message, maxVal]: any[]) => `cannot be more than ${maxVal}`;
const minMax = ([message, minVal, maxVal]: any[]) => `must be between ${minVal} and ${maxVal} characters`;
const formatDate = (value) => `${moment(value).format(DATE_FORMATS.API_DATE)}`;
const formatTime = (value) => `${moment(value).format(DATE_FORMATS.timePickerInput)}`;

export const ErrorMessages = {
    "required": () => "Value is required",
    "incorrectTime": () => "Time must be between 00:00 and 24:00. ",
    "mask": (x) => `Format must be ${x}`,
    "max": (x) => `Value cannot be more than ${x}`,
    "min": (x) => `Value cannot be less than ${x}`,
    "maxlength": (x) => `Value cannot exceed ${x} characters`,
    "minlength": (x) => `Value must be at least ${x} characters`,
    "email": () => "Invalid email format",
    "weight": () => "Value cannot have more than one decimal",

    "error.resource.communication.method.type.unique": ({messageArguments}: ValidationError) => `There are multiple communications of type '${getDescriptionForCode(messageArguments[0], getCodeOptions("COMMUNICATION_METHOD_TYPE_CODE"))}'. There can be only one communication of each type.`,
    "error.resource.group.duplicate": ({messageArguments}: ValidationError) => `A group with name '${messageArguments[0]}' and type '${messageArguments[1]}' already exists.`,

};

export function getDisplayErrorMessage(err: ValidationError) {

    if (err && ErrorMessages[err.message]) {
        return ErrorMessages[err.message](err);
    } else if (err && err.messageTemplate && err.messageTemplate != err.message) {
        let msg: string = err.messageTemplate;
        let args = err.messageArguments;
        if (args && args.length > 0) {
            err.messageArguments.forEach(function (arg, index) {
                let paramLookup = "{" + index + "}";
                msg = msg.replace(paramLookup, arg);
            });
        }
        return msg;
    } else {
        return err.message;
    }
}
