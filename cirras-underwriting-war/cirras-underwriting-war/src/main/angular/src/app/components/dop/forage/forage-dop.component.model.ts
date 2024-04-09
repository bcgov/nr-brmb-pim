import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";


export class ForageDopComponentModel extends BaseComponentModel {
    

    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder) {

        super(sanitizer);
        
        this.formGroup = this.fb.group({

            yieldMeasUnitTypeCode: [''],  
            declarationOfProductionDate: [''],
            fields: new FormArray([]), // subform responsible for the field / commodity/estimated yield grid 
            // dopYieldContractCommodities: new FormArray([]),
            uwComments: [] //new FormArray([])
        });
    }

    public clone(): ForageDopComponentModel {
        let clonedModel: ForageDopComponentModel = new ForageDopComponentModel(this.sanitizer, this.fb);
        return clonedModel;
    }
}
