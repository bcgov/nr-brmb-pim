import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import { Component, OnInit} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectFormStateUnsaved,
    selectVerifiedYieldContractErrorState,
    selectVerifiedYieldContractLoadState,
} from "../../store/application/application.selectors";

import { UwContract } from "src/app/conversion/models";
import { selectGrowerContract } from "src/app/store/grower-contract/grower-contract.selector";
import { VerifiedYieldContract } from "src/app/conversion/models-yield";
import { VERIFIED_YIELD_COMPONENT_ID } from "src/app/store/verified-yield/verified-yield.state";
import { selectVerifiedYieldContract } from "src/app/store/verified-yield/verified-yield.selectors";

@Component({
    selector: "grain-vy-container",
    template: `
        <verified-yield
            [growerContract]="growerContract$ | async"
            [verifiedYieldContract]="verifiedYieldContract"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"
        ></verified-yield>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})
//  [isUnsaved]="isUnsaved$ | async"
export class VerifiedYieldContainer extends BaseContainer implements OnInit {

    growerContract$: Observable<UwContract> = this.store.pipe(select(selectGrowerContract()));
    verifiedYieldContract$: Observable<UwContract> = this.store.pipe(select(selectVerifiedYieldContract()));

    loadState$: Observable<LoadState> = this.store.pipe(select(selectVerifiedYieldContractLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectVerifiedYieldContractErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID)));

    verifiedYieldContract: VerifiedYieldContract;

    getAssociatedComponentIds(): string[] {
        return [
            VERIFIED_YIELD_COMPONENT_ID
        ];
    }

    ngOnInit(): void {
        this.verifiedYieldContract$.subscribe((verifiedYieldContract) => {
            // deep copy of verifiedYieldContract so that it's mutable
            this.verifiedYieldContract = JSON.parse(JSON.stringify(verifiedYieldContract));
            this.cdr.detectChanges();
        });
    }

    
}
