import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";


export class ForageDopComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({

            yieldMeasUnitTypeCode: [''],  
            declarationOfProductionDate: [''],
            balerWagonInfo: [''],
            totalLivestock: [''],
            fields: new UntypedFormArray([]), // subform responsible for the field / commodity/estimated yield grid 
            dopYieldContractCommodityForageList: new UntypedFormArray([]),
            dopYieldFieldRollupForageList: new UntypedFormArray([]),

        });
    }

    public clone(): ForageDopComponentModel {
        let clonedModel: ForageDopComponentModel = new ForageDopComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
