import {BaseComponentModel} from "../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";

export class UwContractsListComponentModel extends BaseComponentModel {
    constructor(protected sanitizer: DomSanitizer) {
        super(sanitizer);
    }

    public clone(): UwContractsListComponentModel {
        let clonedModel: UwContractsListComponentModel = new UwContractsListComponentModel(this.sanitizer);
        return clonedModel;
    }
}
