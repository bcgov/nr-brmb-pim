import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import { YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
import { selectYieldMeasUnit } from "src/app/store/dop/dop.selectors";

@Component({
    selector: "yield-conversion-units-container",
    template: `
        <yield-conversion-units
            [yieldMeasUnitList]="yieldMeasUnitList$ | async"
        ></yield-conversion-units>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class YieldConversionUnitsContainer extends BaseContainer {

    yieldMeasUnitList$: Observable<YieldMeasUnitTypeCodeList> = this.store.pipe(select(selectYieldMeasUnit()));
}
