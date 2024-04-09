import {Injectable} from "@angular/core";
import {Actions, createEffect, Effect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, InventoryContractRsrc, UwContractRsrc} from "@cirras/cirras-underwriting-api";
import {TokenService} from "@wf1/core-ui";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import { convertToErrorState, convertToInventoryContract, convertToUwContract, convertToUwContractsList} from "../../conversion/conversion-from-rest";  

import {RootState} from "../index";
import {  LOAD_GROWER_CONTRACT, LoadGrowerContractAction, LoadGrowerContractSuccess, LoadGrowerContractError } from "./grower-contract.actions";

import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";

@Injectable()
export class GrowerContractInfoEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService,
    private snackbarService: MatSnackBar
    ) {
  }

  getGrowerContract: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_GROWER_CONTRACT),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadGrowerContractAction>action;
  
        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
  
        const policyId = typedAction.payload.value;
  
        return this.CirrasUnderwritingAPIService.getTheUwContract(
          policyId,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          "true"
        ).pipe(
          map((response: UwContractRsrc) => {
            return LoadGrowerContractSuccess(typedAction.componentId, convertToUwContract(response ) ); 
          }),
          
          catchError(
            error => of( LoadGrowerContractError(typedAction.componentId, convertToErrorState(error, "Load Grower Contract ")))
            )
  
        );
      }
    )
  ))
}