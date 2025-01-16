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

import { USER_SETTINGS_COMPONENT_ID } from "src/app/store/maintenance/maintenance.state";
import { UserSetting } from "src/app/conversion/models-maintenance";
import { selectUserSetting } from "src/app/store/maintenance/maintenance.selectors";

@Component({
    selector: "user-settings-container",
    template: `
        <user-settings
            [userSettings]="userSettings$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"        
        ></user-settings>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})
export class UserSettingsContainer extends BaseContainer {

    userSettings$: Observable<UserSetting> = this.store.pipe(select(selectUserSetting()))

    loadState$: Observable<LoadState> = this.store.pipe(select(selectMaintainUwYearsLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectMaintainUwYearsErrorState()));
    
    getAssociatedComponentIds(): string[] {
        return [
            USER_SETTINGS_COMPONENT_ID
        ];
    }
}
