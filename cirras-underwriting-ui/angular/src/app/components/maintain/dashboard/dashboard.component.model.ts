import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";

export class DashboardComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            selectedCropYear: [],
            underwritingYears: new UntypedFormArray([])
        });
    }

    public clone(): DashboardComponentModel {
        let clonedModel: DashboardComponentModel = new DashboardComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
