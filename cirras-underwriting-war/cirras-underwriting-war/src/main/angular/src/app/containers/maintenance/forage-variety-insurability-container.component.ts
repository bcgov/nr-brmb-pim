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
import { CropVarietyInsurabilityList, SeedingDeadlineList, UnderwritingYearList } from "src/app/conversion/models-maintenance";
import { selectCropVarietyInsurabilityList, selectUnderwritingYears, selectseedingDeadlineList } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "forage-variety-insurability-container",
    template: `
        <forage-variety-insurability
            [cropVarietyInsurabilityList]="cropVarietyInsurabilityList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></forage-variety-insurability>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class ForageVarietyInsurabilityContainer extends BaseContainer {

    cropVarietyInsurabilityList$: Observable<CropVarietyInsurabilityList> = this.store.pipe(select(selectCropVarietyInsurabilityList()))

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }

}
