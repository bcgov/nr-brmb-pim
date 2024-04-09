import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";

export class LandListComponentModel extends BaseComponentModel {
    constructor(protected sanitizer: DomSanitizer) {
        super(sanitizer);
    }

    public clone(): LandListComponentModel {
        let clonedModel: LandListComponentModel = new LandListComponentModel(this.sanitizer);
        return clonedModel;
    }
}
