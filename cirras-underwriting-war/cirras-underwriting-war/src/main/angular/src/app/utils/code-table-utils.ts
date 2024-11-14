import {CodeData, Option} from "../store/application/application.state";
import {CONSTANTS, getCodeTableCache} from "./index";
import { HttpClient, HttpHeaders } from "@angular/common/http";

export const sortByDisplayOrder = (a: CodeData, b: CodeData) => a.displayOrder - b.displayOrder;

export const sortByDescription = function (a, b) {
    let nameA = a.description.toUpperCase(); // ignore upper and lowercase
    let nameB = b.description.toUpperCase(); // ignore upper and lowercase
    if (nameA < nameB) {
        return -1;
    }
    if (nameA > nameB) {
        return 1;
    }

    // names must be equal
    return 0;
};

export function getCodeDataForCode(codeStr: string | number, options: (CodeData | Option)[]) {
    if (codeStr && options) {
        let code = options.find(obj => obj.code === codeStr);
        return code ? code : null;
    } else {
        return null;
    }
}

export function getDescriptionForCodeData(value: CodeData | Option) {
    return value ? value.description : null;
}

export function getDescriptionForCode(codeStr: string | number, options: (CodeData | Option)[]) {
    return getDescriptionForCodeData(getCodeDataForCode(codeStr, options));
}

export function getCodeOptions(codeType: string): ((CodeData | Option)[]) {
    let codes = getCodeTableCache()["codeTables"] ? getCodeTableCache()["codeTables"][codeType] : [];
    if (!codes) {
        return [];
    }
    return codes.slice().sort(sortByDisplayOrder);
}