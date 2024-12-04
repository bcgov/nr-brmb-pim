import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import { CROP_COMMODITY_UNSPECIFIED } from "src/app/utils/constants";

export class GradeModifiersComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            selectedCropYear: [ new Date().getFullYear() ],
            selectedCropCommodityId: [ CROP_COMMODITY_UNSPECIFIED.ID ],
            gradeModifiers: new UntypedFormArray([])
        });
    }

    public clone(): GradeModifiersComponentModel {
        let clonedModel: GradeModifiersComponentModel = new GradeModifiersComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
