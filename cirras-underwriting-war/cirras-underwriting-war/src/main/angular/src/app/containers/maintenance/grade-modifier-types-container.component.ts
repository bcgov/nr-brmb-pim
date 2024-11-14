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
import { GradeModifierTypeList } from "src/app/conversion/models-maintenance";
import { selectGradeModifierTypeList } from "src/app/store/maintenance/maintenance.selectors";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "seeding-deadlines-container",
    template: `
        <grade-modifiers-types
            [gradeModifierTypesList]="gradeModifierTypesList$ | async"
        ></grade-modifiers-types>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class GradeModifierTypesContainer extends BaseContainer {

    gradeModifierTypesList$: Observable<GradeModifierTypeList> = this.store.pipe(select(selectGradeModifierTypeList()))
   
    // loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    // errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
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
