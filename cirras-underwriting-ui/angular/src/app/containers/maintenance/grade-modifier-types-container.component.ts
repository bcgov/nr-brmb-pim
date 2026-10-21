import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import {Component} from "@angular/core";
import { Location, LocationStrategy, PathLocationStrategy, AsyncPipe } from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectMaintainUwYearsErrorState,
    selectMaintainUwYearsLoadState,
} from "../../store/application/application.selectors";

import { MAINTENANCE_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { GradeModifierTypeList } from "src/app/conversion/models-maintenance";
import { selectGradeModifierTypeList } from "src/app/store/maintenance/maintenance.selectors";
import { GradeModifiersTypesComponent } from "../../components/maintain/grade-modifiers/grade-modifiers-types/grade-modifiers-types.component";

@Component({
    selector: "seeding-deadlines-container",
    template: `
        <grade-modifiers-types
            [gradeModifierTypesList]="gradeModifierTypesList$ | async"
        ></grade-modifiers-types>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    imports: [GradeModifiersTypesComponent, AsyncPipe]
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
}
