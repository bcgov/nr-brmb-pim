import { BaseContainer } from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {ChangeDetectorRef, Component} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectFormStateUnsaved,
    selectMaintainUwYearsErrorState,
    selectMaintainUwYearsLoadState,
} from "../../store/application/application.selectors";

import { MAINTENANCE_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { GradeModifierList, GradeModifierTypeList, UnderwritingYearList } from "src/app/conversion/models-maintenance";
import { selectGradeModifierList, selectGradeModifierTypeList, selectUnderwritingYears } from "src/app/store/maintenance/maintenance.selectors";
import { CropCommodityList } from "src/app/conversion/models";
import { selectCropCommodityList } from "src/app/store/crop-commodity/crop-commodity.selectors";
import { RootState } from "src/app/store";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ApplicationStateService } from "src/app/services/application-state.service";

@Component({
    selector: "grade-modifiers-container",
    template: `
        <grade-modifiers
            [underwritingYears]="underwritingYears$ | async"
            [gradeModifierList]="gradeModifierList$ | async"
            [cropCommodityList]="cropCommodityList$ | async"
            [gradeModifierTypeList]="gradeModifierTypeList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
            [isUnsaved]="isUnsaved$ | async"
        ></grade-modifiers>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class GradeModifiersContainer extends BaseContainer {

    underwritingYears$: Observable<UnderwritingYearList> = this.store.pipe(select(selectUnderwritingYears()))
    gradeModifierList$: Observable<GradeModifierList> = this.store.pipe(select(selectGradeModifierList()))
    cropCommodityList$: Observable<CropCommodityList> = this.store.pipe(select(selectCropCommodityList()));
    gradeModifierTypeList$: Observable<GradeModifierTypeList> = this.store.pipe(select(selectGradeModifierTypeList()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(MAINTENANCE_COMPONENT_ID)));

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
