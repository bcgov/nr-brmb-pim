import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";


export class BerriesDopComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            fields: new UntypedFormArray([]), // subform responsible for the field / commodity/estimated yield grid 
        });
    }

    public clone(): BerriesDopComponentModel {
        let clonedModel: BerriesDopComponentModel = new BerriesDopComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
