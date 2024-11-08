import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";

export class YieldConversionComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            yieldMeasUnitConversionList: new UntypedFormArray([]),
        });
    }

    public clone(): YieldConversionComponentModel {
        let clonedModel: YieldConversionComponentModel = new YieldConversionComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
