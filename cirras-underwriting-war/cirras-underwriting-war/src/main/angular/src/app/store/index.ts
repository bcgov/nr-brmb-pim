import {Action, ActionReducerMap} from "@ngrx/store";
import {routerReducer} from "@ngrx/router-store";

import {applicationReducer} from "./application/application.reducer";
import {ApplicationEffects} from "./application/application.effects";
import {ApplicationState, PagingSearchState} from "./application/application.state";

import {pageSearchReducer} from "./common/page-search.reducer";
import { UwContractsListEffects } from "./uw-contracts-list/uw-contracts-list.effects";
import { initialUwContractsListSearchState, UwContractsListState } from "./uw-contracts-list/uw-contracts-list.state";
import { initialLandSearchState, LandSearchState, ManageLegalLandState} from "./land-management/land-management.state";
import { uwContractsListReducer } from "./uw-contracts-list/uw-contracts-list.reducer";
import { InventoryEffects } from "./inventory/inventory.effects";
import { inventoryReducer } from "./inventory/inventory.reducer";
import { InventoryState } from "./inventory/inventory.state";
import { CropCommodityState } from "./crop-commodity/crop-commodity.state";
import { cropCommodityReducer } from "./crop-commodity/crop-commodity.reducer";
import { CropCommodityEffects } from "./crop-commodity/crop-commodity.effects";
import { GrowerContractState } from "./grower-contract/grower-contract.state";
import { growerContractInfoReducer } from "./grower-contract/grower-contract.reducer";
import { GrowerContractInfoEffects } from "./grower-contract/grower-contract.effects";
import { DopState } from "./dop/dop.state";
import { dopReducer } from "./dop/dop.reducer";
import { DopEffects } from "./dop/dop.effects";
import { LandListEffects } from "./land-management/land-management.effects";
import { landManagementReducer, manageLegalLandReducer } from "./land-management/land-management.reducer"
import { maintenanceReducer } from "./maintenance/maintenance.reducer";
import { MaintenanceState } from "./maintenance/maintenance.state";
import { MaintenanceEffects } from "./maintenance/maintenance.effects";


export const rootReducers: ActionReducerMap<any> = {
    router: routerReducer,
    searchUwContracts: pageSearchReducer,
    application: applicationReducer,
    uwContractsList: uwContractsListReducer,
    inventoryContract: inventoryReducer,
    cropCommodityList: cropCommodityReducer,
    growerContract: growerContractInfoReducer,
    dop: dopReducer,
    landSearch: landManagementReducer,
    searchLand: pageSearchReducer,
    manageLegalLand: manageLegalLandReducer,
    maintenance: maintenanceReducer
};

export interface RootState {
    application?: ApplicationState;    
    uwContractsList?: UwContractsListState;
    landSearch?: LandSearchState;
    searchLand?: PagingSearchState;
    searchUwContracts?:  PagingSearchState;
    inventoryContract?: InventoryState;
    cropCommodityList?: CropCommodityState;
    growerContract?: GrowerContractState;
    dop?:   DopState;
    manageLegalLand?: ManageLegalLandState;
    maintenance?: MaintenanceState;
}

export const initialRootState: RootState = {
    searchUwContracts:  initialUwContractsListSearchState,
    searchLand: initialLandSearchState
};

export const rootEffects: any[] = [
    ApplicationEffects,
    UwContractsListEffects,
    InventoryEffects,
    CropCommodityEffects,
    GrowerContractInfoEffects,
    DopEffects,
    LandListEffects,
    MaintenanceEffects
];

export interface LabeledAction extends Action {
    displayLabel: string;
}

