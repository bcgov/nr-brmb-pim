import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray, FormGroup} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import { PRIMARY_LAND_IDENTIFIER_TYPE_CODE, PRIMARY_REFERENCE_TYPE_CODE } from "src/app/utils/constants";

export class ManageLandComponentModel extends BaseComponentModel {
     
    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            primaryReferenceTypeCode: [PRIMARY_REFERENCE_TYPE_CODE.DEFAULT],
            primaryLandIdentifierTypeCode: [PRIMARY_LAND_IDENTIFIER_TYPE_CODE.DEFAULT], 
            primaryPropertyIdentifier: [],
            legalDescription: [],
            otherDescription: [],
            activeFromCropYear: [],
            activeToCropYear: [],
            riskAreas: new FormArray([]),
        });
    }

    public clone(): ManageLandComponentModel {
        let clonedModel: ManageLandComponentModel = new ManageLandComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
