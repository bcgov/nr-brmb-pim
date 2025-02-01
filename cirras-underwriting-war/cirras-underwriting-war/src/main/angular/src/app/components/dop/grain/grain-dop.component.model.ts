import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";


export class GrainDopComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({

            yieldMeasUnitTypeCode: [''],  
            declarationOfProductionDate: [''],
            grainFromOtherSourceInd: [''],
            fields: new UntypedFormArray([]), // subform responsible for the field / commodity/estimated yield grid 
            dopYieldContractCommodities: new UntypedFormArray([]),
            uwComments: [] //new FormArray([])
        });
    }

    public clone(): GrainDopComponentModel {
        let clonedModel: GrainDopComponentModel = new GrainDopComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
