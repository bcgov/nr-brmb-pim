import {ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {BaseContainer} from "../../containers/base/base-container.component";
import {UnsavedDialogComponent} from "../../components/dialogs/unsaved-dialog/unsaved-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ApplicationStateService} from "../application-state.service";
import {setFormStateUnsaved} from "../../store/application/application.actions";
import {RootState} from "../../store";
import {Store} from "@ngrx/store";
import { ResourcesRoutes } from "src/app/utils/constants";

@Injectable({
    providedIn: "root",
})
export class DeactivateGuard implements CanDeactivate<any> {
    constructor(
        protected dialog: MatDialog,
        protected store: Store<RootState>,
        protected applicationStateService: ApplicationStateService,
    ) {
    }

    canDeactivate(component: any,
                  currentRoute: ActivatedRouteSnapshot,
                  currentState: RouterStateSnapshot,
                  nextState?: RouterStateSnapshot): Observable<boolean> {
        if (this.dialog && this.dialog.openDialogs && this.dialog.openDialogs.length) {
            if (nextState.url.includes(ResourcesRoutes.ERROR_PAGE)) {
                return of(true);
            } else {
                this.dialog.closeAll();
                console.log("cancelled nav");
                return of(false);
            }
        }
        if (component instanceof BaseContainer) {
            let typedComp = <BaseContainer>component;
            if (typedComp.getHasUnsavedForms()) {
                return this.confirmUnsavedDialog(typedComp.getDisplayLabel(), component.getAssociatedComponentIds());
            } else {
                return of(true);
            }
        } else {
            return of(true);
        }
    }

    confirmUnsavedDialog(displayLabel: string, associatedComponentIds: string[]): Observable<boolean> {
        let config = {
            data: displayLabel,
            autoFocus: false,
            closeOnNavigation: false,
            panelClass: 'wf-dialog'
        };
        this.applicationStateService.resetViewportScale();
        const dialogRef = this.dialog.open(UnsavedDialogComponent, config);
        return dialogRef.afterClosed().pipe(map(result => {
            if ("cancel" === result) {
                return false;
            } else if ("continue" === result) {
                associatedComponentIds.forEach(item => {
                    this.store.dispatch(setFormStateUnsaved(item, false));
                });
                return true;
            } else {
                return false;
            }
        }));
    }

}
