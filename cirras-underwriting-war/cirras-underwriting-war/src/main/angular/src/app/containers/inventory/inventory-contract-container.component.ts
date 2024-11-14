import { BaseContainer } from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectInventoryContractErrorState,
    selectInventoryContractLoadState,
} from "../../store/application/application.selectors";

import { CropCommodityList, InventoryContract, UwContract } from "src/app/conversion/models";
import { selectInventoryContract } from "src/app/store/inventory/inventory.selectors";
import { INVENTORY_COMPONENT_ID } from "src/app/store/inventory/inventory.state";
import { selectCropCommodityList, selectUnderSeededCropCommodityList } from "src/app/store/crop-commodity/crop-commodity.selectors";
import { selectGrowerContract } from "src/app/store/grower-contract/grower-contract.selector";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "cirras-underwriting-inventory-contract-container",
    template: `
        <cirras-underwriting-inventory-selector 
            [inventoryContract]="inventoryContract$ | async"
            [growerContract]="growerContract$ | async"
            [cropCommodityList]="cropCommodityList$ | async"
            [underSeededCropCommodityList]="underSeededCropCommodityList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></cirras-underwriting-inventory-selector>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class InventoryContractContainer extends BaseContainer {

    inventoryContract$: Observable<InventoryContract> = this.store.pipe(select(selectInventoryContract()));
    growerContract$: Observable<UwContract> = this.store.pipe(select(selectGrowerContract()));
    cropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectCropCommodityList()));
    underSeededCropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectUnderSeededCropCommodityList()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectInventoryContractLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectInventoryContractErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            INVENTORY_COMPONENT_ID
        ];
    }

    // constructor(
    //     protected store: Store<RootState>,
    //     protected router: Router,
    //     public snackBar: MatSnackBar,
    //     protected applicationStateService: ApplicationStateService,
    //     protected cdr: ChangeDetectorRef
    // ) {
    //     super(store, router, snackBar, applicationStateService, cdr);
    // }
}
