import { BaseContainer } from "../base/base-container.component";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import { Component } from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectFormStateUnsaved,
    selectMaintainUwYearsErrorState,
    selectMaintainUwYearsLoadState,
} from "../../store/application/application.selectors";

import { MAINTENANCE_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { CropVarietyInsurabilityList } from "src/app/conversion/models-maintenance";
import { selectCropVarietyInsurabilityList } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "forage-variety-insurability-container",
    template: `
        <forage-variety-insurability
            [cropVarietyInsurabilityList]="cropVarietyInsurabilityList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async" 
            [isUnsaved]="isUnsaved$ | async"          
        ></forage-variety-insurability>`,
    providers: [Location, { provide: LocationStrategy, useClass: PathLocationStrategy }],
    standalone: false
})
export class ForageVarietyInsurabilityContainer extends BaseContainer {

    cropVarietyInsurabilityList$: Observable<CropVarietyInsurabilityList> = this.store.pipe(select(selectCropVarietyInsurabilityList()))

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(MAINTENANCE_COMPONENT_ID)));

    getAssociatedComponentIds(): string[] {
        return [
            MAINTENANCE_COMPONENT_ID
        ];
    }
}
