import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import {InventoryContract} from "../../../conversion/models";

export class ForageInventoryComponentModel extends BaseComponentModel {
    inventoryContract: InventoryContract

    constructor(protected sanitizer: DomSanitizer,
                private fb: FormBuilder,
                private inventoryContractData: InventoryContract) {

        super(sanitizer);
        this.inventoryContract = inventoryContractData;

        this.formGroup = this.fb.group({

            fields: new FormArray([]),
            commodities: new FormArray([]),
        });
    }

    public clone(): ForageInventoryComponentModel {
        let clonedModel: ForageInventoryComponentModel = new ForageInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
        return clonedModel;
    }
}
