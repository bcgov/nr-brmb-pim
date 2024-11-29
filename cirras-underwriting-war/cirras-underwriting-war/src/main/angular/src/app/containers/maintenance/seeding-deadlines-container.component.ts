import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import { Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectMaintainUwYearsErrorState,
    selectMaintainUwYearsLoadState,
} from "../../store/application/application.selectors";

import { MAINTENANCE_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { SeedingDeadlineList, UnderwritingYearList } from "src/app/conversion/models-maintenance";
import { selectUnderwritingYears, selectseedingDeadlineList } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "seeding-deadlines-container",
    template: `
        <seeding-deadlines
            [underwritingYears]="underwritingYears$ | async"
            [seedingDeadlineList]="seedingDeadlineList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></seeding-deadlines>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class SeedingDeadlinesContainer extends BaseContainer {

    underwritingYears$: Observable<UnderwritingYearList> = this.store.pipe(select(selectUnderwritingYears()))
    seedingDeadlineList$: Observable<SeedingDeadlineList> = this.store.pipe(select(selectseedingDeadlineList()))

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }
}
