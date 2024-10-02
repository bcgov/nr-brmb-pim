

import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import { ErrorState, LoadState } from "src/app/store/application/application.state";
import { selectLegalLandListErrorState, selectLegalLandListLoadState } from "src/app/store/application/application.selectors";
import { selectLandList, selectLegalLand, selectRiskAreaList } from "src/app/store/land-management/land-management.selectors";
import { MANAGE_LEGAL_LAND_COMPONENT_ID } from "src/app/store/land-management/land-management.state";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "manage-land-container",
    template: `<manage-land
                [legalLand]="legalLand$ | async"
                [riskAreaList]="riskAreaList$ | async"
                [loadState]="loadState$ | async"
                [errorState]="errorState$ | async"
        ></manage-land>`,
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
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
