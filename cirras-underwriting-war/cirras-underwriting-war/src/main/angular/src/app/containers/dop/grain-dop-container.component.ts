import { BaseContainer } from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectDopYieldContractErrorState,
    selectDopYieldContractLoadState,
} from "../../store/application/application.selectors";

import { UwContract } from "src/app/conversion/models";
import { selectGrowerContract } from "src/app/store/grower-contract/grower-contract.selector";
import { DopYieldContract, YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
import { selectDopYieldContract, selectYieldMeasUnit } from "src/app/store/dop/dop.selectors";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "dop-container",
    template: `
        <grain-dop
            [growerContract]="growerContract$ | async"
            [dopYieldContract]="dopYieldContract$ | async"
            [yieldMeasUnitList]="yieldMeasUnitList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></grain-dop>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class GrainDopContainer extends BaseContainer {

    dopYieldContract$: Observable<DopYieldContract> = this.store.pipe(select(selectDopYieldContract()))
    yieldMeasUnitList$: Observable<YieldMeasUnitTypeCodeList> = this.store.pipe(select(selectYieldMeasUnit()));
    growerContract$: Observable<UwContract> = this.store.pipe(select(selectGrowerContract()));
    
    loadState$: Observable<LoadState> = this.store.pipe(select(selectDopYieldContractLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectDopYieldContractErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            DOP_COMPONENT_ID
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
