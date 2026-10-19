import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray} from "@angular/forms";
import {BaseComponentModel} from "../../common/base/base.component.model";
import {InventoryContract} from "../../../conversion/models";

export class ForageInventoryComponentModel extends BaseComponentModel {
    inventoryContract: InventoryContract

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private inventoryContractData: InventoryContract) {

        super(sanitizer);
        this.inventoryContract = inventoryContractData;

        this.formGroup = this.fb.group({

            fields: new UntypedFormArray([]),
            commodities: new UntypedFormArray([]),
        });
    }

    public clone(): ForageInventoryComponentModel {
        let clonedModel: ForageInventoryComponentModel = new ForageInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
        return clonedModel;
    }
}
