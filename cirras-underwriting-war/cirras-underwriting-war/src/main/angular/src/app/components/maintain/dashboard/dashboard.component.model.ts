import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";

export class DashboardComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            selectedCropYear: [],
            underwritingYears: new FormArray([])
        });
    }

    public clone(): DashboardComponentModel {
        let clonedModel: DashboardComponentModel = new DashboardComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
