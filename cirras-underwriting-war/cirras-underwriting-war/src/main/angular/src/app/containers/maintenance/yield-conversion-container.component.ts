import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {Component} from "@angular/core";
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

@Component({
    selector: "yield-conversion-container",
    template: `
        <yield-conversion
            [yieldMeasUnitList]="yieldMeasUnitList$ | async"
            [yieldMeasUnitConversionList]="yieldMeasUnitConversionList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></yield-conversion>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
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
}
