import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";


export class GrainDopComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({

            yieldMeasUnitTypeCode: [''],  
            declarationOfProductionDate: [''],
            grainFromOtherSourceInd: [''],
            fields: new FormArray([]), // subform responsible for the field / commodity/estimated yield grid 
            dopYieldContractCommodities: new FormArray([]),
            uwComments: [] //new FormArray([])
        });
    }

    public clone(): GrainDopComponentModel {
        let clonedModel: GrainDopComponentModel = new GrainDopComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
