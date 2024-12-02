import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import {InventoryContract} from "../../../conversion/models";

export class GrainInventoryComponentModel extends BaseComponentModel {
    inventoryContract: InventoryContract

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private inventoryContractData: InventoryContract) {

        super(sanitizer);
        this.inventoryContract = inventoryContractData;

        this.formGroup = this.fb.group({
            fertilizerInd: [''],  
            herbicideInd: [''],    
            tilliageInd: [''],
            otherChangesInd:[''],
            otherChangesComment:[''],           
            grainFromPrevYearInd: [''] ,
            showMoreColumns:    [false],
            fields: new UntypedFormArray([]),
            commodities: new UntypedFormArray([]),
        });
    }

    public clone(): GrainInventoryComponentModel {
        let clonedModel: GrainInventoryComponentModel = new GrainInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
        return clonedModel;
    }
}
