import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";

export class ForageVarietyInsurabilityComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            varietyInsurability: new FormArray([]),
        });
    }

    public clone(): ForageVarietyInsurabilityComponentModel {
        let clonedModel: ForageVarietyInsurabilityComponentModel = new ForageVarietyInsurabilityComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
