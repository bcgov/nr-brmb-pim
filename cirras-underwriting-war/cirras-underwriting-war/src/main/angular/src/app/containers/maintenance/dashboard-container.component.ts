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
import { UnderwritingYearList } from "src/app/conversion/models-maintenance";
import { selectUnderwritingYears } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "dashboard-container",
    template: `
        <cirras-dashboard
            [underwritingYears]="underwritingYears$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></cirras-dashboard>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})

export class DashboardContainer extends BaseContainer {

    underwritingYears$: Observable<UnderwritingYearList> = this.store.pipe(select(selectUnderwritingYears()))

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }
}
