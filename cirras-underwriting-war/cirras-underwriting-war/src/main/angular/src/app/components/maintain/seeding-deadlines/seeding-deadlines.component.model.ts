import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import { PRIMARY_LAND_IDENTIFIER_TYPE_CODE, PRIMARY_REFERENCE_TYPE_CODE } from "src/app/utils/constants";

export class SeedingDeadlinesComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            selectedCropYear: [ new Date().getFullYear() ],
            rbChoice: ['changeDefault'],
            seedingDeadlines: new FormArray([]),
        });
    }

    public clone(): SeedingDeadlinesComponentModel {
        let clonedModel: SeedingDeadlinesComponentModel = new SeedingDeadlinesComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
