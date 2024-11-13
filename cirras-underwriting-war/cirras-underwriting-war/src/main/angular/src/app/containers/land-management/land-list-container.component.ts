import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {BaseContainer} from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";

import { selectLandList } from "src/app/store/land-management/land-management.selectors";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectSearchState,
    selectLegalLandListErrorState,
    selectLegalLandListLoadState 
} from "../../store/application/application.selectors";

import {SearchState} from "@wf1/wfcc-core-lib";
import { LAND_SEARCH_COMPONENT_ID } from "src/app/store/land-management/land-management.state";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "cirras-land-list-container",
    template: `<cirras-land-list
                [collection]="collection$ | async"
                [searchState]="searchState$ | async"
                [loadState]="loadState$ | async"
                [errorState]="errorState$ | async"
        ></cirras-land-list>`,
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})
 
//@Directive()
export class LandListContainer extends BaseContainer {
    collection$: Observable<any> = this.store.pipe(select(selectLandList()));
    searchState$: Observable<SearchState> = this.store.pipe(select(selectSearchState(LAND_SEARCH_COMPONENT_ID)));
    loadState$: Observable<LoadState> = this.store.pipe(select(selectLegalLandListLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectLegalLandListErrorState()));

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
