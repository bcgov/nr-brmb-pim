import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectFormStateUnsaved,
    selectInventoryContractErrorState,
    selectInventoryContractLoadState,
} from "../../store/application/application.selectors";

import { CropCommodityList, InventoryContract, UwContract } from "src/app/conversion/models";
import { selectInventoryContract } from "src/app/store/inventory/inventory.selectors";
import { INVENTORY_COMPONENT_ID } from "src/app/store/inventory/inventory.state";
import { selectCropCommodityList, selectUnderSeededCropCommodityList } from "src/app/store/crop-commodity/crop-commodity.selectors";
import { selectGrowerContract } from "src/app/store/grower-contract/grower-contract.selector";

@Component({
    selector: "cirras-underwriting-inventory-contract-container",
    template: `
        <cirras-underwriting-inventory-selector 
            [inventoryContract]="inventoryContract"
            [growerContract]="growerContract$ | async"
            [cropCommodityList]="cropCommodityList$ | async"
            [underSeededCropCommodityList]="underSeededCropCommodityList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
            [isUnsaved]="isUnsaved$ | async"
        ></cirras-underwriting-inventory-selector>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})

export class InventoryContractContainer extends BaseContainer {

    inventoryContract$: Observable<InventoryContract> = this.store.pipe(select(selectInventoryContract()));
    growerContract$: Observable<UwContract> = this.store.pipe(select(selectGrowerContract()));
    cropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectCropCommodityList()));
    underSeededCropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectUnderSeededCropCommodityList()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectInventoryContractLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectInventoryContractErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(INVENTORY_COMPONENT_ID)));
    
    inventoryContract: InventoryContract;

    getAssociatedComponentIds(): string[] {
        return [
            INVENTORY_COMPONENT_ID
        ];
    }

    ngOnInit(): void {
        this.inventoryContract$.subscribe((inventoryContract) => {
            // deep copy of inventoryContract so that it's mutable
            this.inventoryContract = JSON.parse(JSON.stringify(inventoryContract));
            this.cdr.detectChanges();
        });
    }
}
