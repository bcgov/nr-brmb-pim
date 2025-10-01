import {Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import { selectInventoryContractList, selectUwContractsList } from "src/app/store/uw-contracts-list/uw-contracts-list.selectors";
import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectSearchState,
    selectUwContractsListErrorState,
    selectUwContractsListLoadState 
} from "../../store/application/application.selectors";

import { UW_CONTRACTS_SEARCH_COMPONENT_ID } from "src/app/store/uw-contracts-list/uw-contracts-list.state";
import {SearchState} from "@wf1/wfcc-core-lib";
import { CropCommodityList } from "src/app/conversion/models";
import {selectCropCommodityList} from "src/app/store/crop-commodity/crop-commodity.selectors";
import { UserSetting } from "src/app/conversion/models-maintenance";
import { selectUserSetting } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "cirras-uw-contracts-list-container",
    template: `
        <cirras-uw-contracts-list
                [collection]="collection$ | async"
                [searchState]="searchState$ | async"
                [cropCommodityList]="cropCommodityList$ | async"
                [userSettings]="userSettings$ | async"
                [loadState]="loadState$ | async"
                [errorState]="errorState$ | async"
                [reportCollection]="reportCollection$ | async"
        ></cirras-uw-contracts-list>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})
export class UwContractsListContainer extends BaseContainer {
    collection$: Observable<any> = this.store.pipe(select(selectUwContractsList()));
    searchState$: Observable<SearchState> = this.store.pipe(select(selectSearchState(UW_CONTRACTS_SEARCH_COMPONENT_ID)));
    cropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectCropCommodityList()));
    userSettings$: Observable<UserSetting> = this.store.pipe(select(selectUserSetting()))
    loadState$: Observable<LoadState> = this.store.pipe(select(selectUwContractsListLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectUwContractsListErrorState()));
    reportCollection$: Observable<any> = this.store.pipe(select(selectInventoryContractList()));
}
