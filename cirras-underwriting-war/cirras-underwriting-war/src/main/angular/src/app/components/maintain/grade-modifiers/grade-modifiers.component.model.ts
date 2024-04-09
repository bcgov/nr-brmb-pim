import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import { CROP_COMMODITY_UNSPECIFIED } from "src/app/utils/constants";

export class GradeModifiersComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            selectedCropYear: [ new Date().getFullYear() ],
            selectedCropCommodityId: [ CROP_COMMODITY_UNSPECIFIED.ID ],
            gradeModifiers: new FormArray([])
        });
    }

    public clone(): GradeModifiersComponentModel {
        let clonedModel: GradeModifiersComponentModel = new GradeModifiersComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
