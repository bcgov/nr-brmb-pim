import { BaseContainer } from "../base/base-container.component";
import {select} from "@ngrx/store";
import {Observable} from "rxjs";
import { Component, OnInit} from "@angular/core";
import {Location, LocationStrategy, PathLocationStrategy} from "@angular/common";

import {ErrorState, LoadState} from "../../store/application/application.state";
import {
    selectDopYieldContractErrorState,
    selectDopYieldContractLoadState,
    selectFormStateUnsaved,
} from "../../store/application/application.selectors";

import { UwContract } from "src/app/conversion/models";
import { selectGrowerContract } from "src/app/store/grower-contract/grower-contract.selector";
import { DopYieldContract, YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
import { selectDopYieldContract, selectYieldMeasUnit } from "src/app/store/dop/dop.selectors";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";

@Component({
    selector: "dop-container",
    template: `
        <forage-dop
            [growerContract]="growerContract$ | async"
            [dopYieldContract]="dopYieldContract"
            [yieldMeasUnitList]="yieldMeasUnitList$ | async"
            [loadState]="loadState$ | async"
            [errorState]="errorState$ | async"            
        ></forage-dop>`, 
    providers: [Location, {provide: LocationStrategy, useClass: PathLocationStrategy}]
})

export class ForageDopContainer extends BaseContainer implements OnInit {

    dopYieldContract$: Observable<DopYieldContract> = this.store.pipe(select(selectDopYieldContract()))
    yieldMeasUnitList$: Observable<YieldMeasUnitTypeCodeList> = this.store.pipe(select(selectYieldMeasUnit()));
    growerContract$: Observable<UwContract> = this.store.pipe(select(selectGrowerContract()));
    
    loadState$: Observable<LoadState> = this.store.pipe(select(selectDopYieldContractLoadState()));
    errorState$: Observable<ErrorState[]> = this.store.pipe(select(selectDopYieldContractErrorState()));
    isUnsaved$: Observable<boolean> = this.store.pipe(select(selectFormStateUnsaved(DOP_COMPONENT_ID)));

    dopYieldContract: DopYieldContract;

    getAssociatedComponentIds(): string[] {
        return [
            DOP_COMPONENT_ID
        ];
    }

    ngOnInit(): void {
        this.dopYieldContract$.subscribe((dopYieldContract) => {
            // deep copy of dopYieldContract so that it's mutable
            this.dopYieldContract = JSON.parse(JSON.stringify(dopYieldContract));
            if (this.dopYieldContract) {
                this.dopYieldContract.grainFromOtherSourceInd = false;
            }
            this.cdr.detectChanges();
        });
    }
}
