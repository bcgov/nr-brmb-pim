import { UntypedFormBuilder } from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";

export class UserSettingsComponentModel extends BaseComponentModel {
    constructor(protected sanitizer: DomSanitizer,
        private fb: UntypedFormBuilder
    ) {
        super(sanitizer);

        this.formGroup = this.fb.group({
            selectedCropYear: [''],
            selectedInsurancePlanId: [''],
            selectedOfficeId: [''],
            setDefaultSettings: [false]
        });

    }

    public clone(): UserSettingsComponentModel {
        let clonedModel: UserSettingsComponentModel = new UserSettingsComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
