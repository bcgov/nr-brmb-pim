

import { Component } from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import { ErrorState, LoadState } from "src/app/store/application/application.state";
import { selectLegalLandListErrorState, selectLegalLandListLoadState } from "src/app/store/application/application.selectors";
import { selectLegalLand, selectRiskAreaList } from "src/app/store/land-management/land-management.selectors";
import { MANAGE_LEGAL_LAND_COMPONENT_ID } from "src/app/store/land-management/land-management.state";

@Component({
    selector: "manage-land-container",
    template: `<manage-land
                [legalLand]="legalLand$ | async"
                [riskAreaList]="riskAreaList$ | async"
                [loadState]="loadState$ | async"
                [errorState]="errorState$ | async"
        ></manage-land>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})

export class ManageLandComponentContainer extends BaseContainer {
    legalLand$: Observable<any> = this.store.pipe(select(selectLegalLand()));
    riskAreaList$: Observable<any> = this.store.pipe(select(selectRiskAreaList()));
    loadState$: Observable<LoadState> = this.store.pipe(select(selectLegalLandListLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectLegalLandListErrorState()));

    // this helps detect unsaved changes
    getAssociatedComponentIds(): string[] {
        return [
            MANAGE_LEGAL_LAND_COMPONENT_ID
        ];
    }
}
