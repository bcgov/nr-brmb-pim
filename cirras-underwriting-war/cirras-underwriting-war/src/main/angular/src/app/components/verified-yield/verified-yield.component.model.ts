import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray} from "@angular/forms";
import {BaseComponentModel} from "../common/base/base.component.model";


export class VerifiedYieldComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({
            // todo
            // yieldMeasUnitTypeCode: [''],  
            // declarationOfProductionDate: [''],
            // balerWagonInfo: [''],
            // totalLivestock: [''],
            fields: new FormArray([]), // subform responsible for the field / commodity grid 
            // dopYieldContractCommodityForageList: new FormArray([]),
            // dopYieldFieldRollupForageList: new FormArray([]),

        });
    }

    public clone(): VerifiedYieldComponentModel {
        let clonedModel: VerifiedYieldComponentModel = new VerifiedYieldComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
