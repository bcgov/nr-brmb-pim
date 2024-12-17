import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../common/base/base.component.model";


export class VerifiedYieldComponentModel extends BaseComponentModel {
    
    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            // todo - add the summary grid
            updateProductValuesInd: [false],
            verifiedYieldContractCommodities: new UntypedFormArray([]), // subform responsible for the commodities grid 
            verifiedYieldAmendments: new UntypedFormArray([]) , // subform responsible for the appraisals and assessments grid
        });
    }

    public clone(): VerifiedYieldComponentModel {
        let clonedModel: VerifiedYieldComponentModel = new VerifiedYieldComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
