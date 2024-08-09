import { BaseContainer } from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectMaintainUwYearsErrorState,
    selectMaintainUwYearsLoadState,
} from "../../store/application/application.selectors";

import { MAINTENANCE_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
import { selectYieldMeasUnit } from "src/app/store/dop/dop.selectors";
import { YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";
import { selectYieldMeasUnitConversionList } from "src/app/store/maintenance/maintenance.selectors";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

// [seedingDeadlineList]="seedingDeadlineList$ | async"

@Component({
    selector: "yield-conversion-container",
    template: `
        <yield-conversion
            [yieldMeasUnitList]="yieldMeasUnitList$ | async"
            [yieldMeasUnitConversionList]="yieldMeasUnitConversionList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></yield-conversion>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class YieldConversionContainer extends BaseContainer {

    yieldMeasUnitConversionList$: Observable<YieldMeasUnitConversionList> = this.store.pipe(select(selectYieldMeasUnitConversionList()))
    yieldMeasUnitList$: Observable<YieldMeasUnitTypeCodeList> = this.store.pipe(select(selectYieldMeasUnit()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }

    constructor(
        protected store: Store<RootState>,
        protected router: Router,
        public snackBar: MatSnackBar,
        protected applicationStateService: ApplicationStateService,
        protected cdr: ChangeDetectorRef
    ) {
        super(store, router, snackBar, applicationStateService, cdr);
    }
}
