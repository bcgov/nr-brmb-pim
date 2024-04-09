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
import { GradeModifierList, GradeModifierTypeList, UnderwritingYearList } from "src/app/conversion/models-maintenance";
import { selectGradeModifierList, selectGradeModifierTypeList, selectUnderwritingYears } from "src/app/store/maintenance/maintenance.selectors";
import { CropCommodityList } from "src/app/conversion/models";
import { selectCropCommodityList } from "src/app/store/crop-commodity/crop-commodity.selectors";

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

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }

}
