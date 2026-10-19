import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";

export class ForageVarietyInsurabilityComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            varietyInsurability: new UntypedFormArray([]),
        });
    }

    public clone(): ForageVarietyInsurabilityComponentModel {
        let clonedModel: ForageVarietyInsurabilityComponentModel = new ForageVarietyInsurabilityComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
