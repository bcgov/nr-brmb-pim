import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Action, Store } from "@ngrx/store";
import { DefaultService as CirrasUnderwritingAPIService, VerifiedYieldContractRsrc } from "@cirras/cirras-underwriting-api";
import { AppConfigService, TokenService } from "@wf1/wfcc-core-lib";
import { UUID } from "angular2-uuid";
import { Observable, of } from 'rxjs';
import { catchError, concatMap, debounceTime, map, switchMap, withLatestFrom } from 'rxjs/operators';
import { convertToErrorState } from "../../conversion/conversion-from-rest";  
import { RootState } from "../index";
import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";
import { convertToVerifiedYieldContract } from "src/app/conversion/conversion-from-rest-yield";
import { HttpClient } from '@angular/common/http';
import { ADD_NEW_VERIFIED_YIELD, DELETE_VERIFIED_YIELD, DeleteVerifiedYieldContractAction, DeleteVerifiedYieldContractError, LOAD_VERIFIED_YIELD, LoadVerifiedYieldContractAction, LoadVerifiedYieldContractError, LoadVerifiedYieldContractSuccess, ROLLOVER_VERIFIED_YIELD, RolloverVerifiedYieldContract, RolloverVerifiedYieldContractAction, UPDATE_VERIFIED_YIELD, VerifiedYieldContractAction } from "./verified-yield.actions";
import { VerifiedYieldContract } from "src/app/conversion/models-yield";
import { displayDeleteSuccessSnackbar, displaySaveSuccessSnackbar } from "src/app/utils/user-feedback-utils";
import { convertToVerifiedYieldContractRsrc } from "src/app/conversion/conversion-to-rest-yield";

@Injectable()
export class VerifiedYieldEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService,
    private appConfig: AppConfigService, 
    private snackbarService: MatSnackBar,
    private httpClient: HttpClient 
    ) {
  }


  getRolledVerifiedYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(ROLLOVER_VERIFIED_YIELD),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <RolloverVerifiedYieldContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwriting${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let policyId = typedAction.payload.policyId 

        return this.CirrasUnderwritingAPIService.rolloverTheVerifiedYieldContract(
          policyId,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          "response"
        ).pipe(
          map((response: any) => {

              return LoadVerifiedYieldContractSuccess(typedAction.componentId, convertToVerifiedYieldContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadVerifiedYieldContractError(typedAction.componentId, convertToErrorState(error, "Rollover Verified Yield ")))
            )

        );
      }
    )
  ))

  getVerifiedYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_VERIFIED_YIELD),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadVerifiedYieldContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwriting${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let verifiedYieldContractGuid = typedAction.payload.verifiedYieldContractGuid 
        // comment this once the backend is implemented
        if ( verifiedYieldContractGuid.length == 0) {
          verifiedYieldContractGuid = ""
        }

        return this.CirrasUnderwritingAPIService.getTheVerifiedYieldContract(
          verifiedYieldContractGuid,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          'response'
        ).pipe(
          map((response: any) => {

              return LoadVerifiedYieldContractSuccess(typedAction.componentId, convertToVerifiedYieldContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadVerifiedYieldContractError(typedAction.componentId, convertToErrorState(error, "Load Verified Yield ")))
            )

        );
      }
    )
  ))

addNewVerifiedYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(ADD_NEW_VERIFIED_YIELD),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <VerifiedYieldContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <VerifiedYieldContract>typedAction.payload.verifiedYieldContract;

            const verifiedYieldContract = typedAction.payload.verifiedYieldContract 
            const body: VerifiedYieldContractRsrc = convertToVerifiedYieldContractRsrc(verifiedYieldContract)
            
              return this.CirrasUnderwritingAPIService.addANewVerifiedYieldContract(
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "Verified Yield ");

                    let newVerifiedYieldContract: VerifiedYieldContract = convertToVerifiedYieldContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      LoadVerifiedYieldContractSuccess(typedAction.componentId, newVerifiedYieldContract)                             
                    ]
                  }),
                  catchError(error =>{
                     return of(LoadVerifiedYieldContractError(typedAction.componentId, convertToErrorState(error, "New Verified Yield "))) 
                  }
              ),
              )   
        }                  
    )
))

updateDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(UPDATE_VERIFIED_YIELD),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <VerifiedYieldContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <VerifiedYieldContract>typedAction.payload.verifiedYieldContract;

            const verifiedYieldContract = typedAction.payload.verifiedYieldContract 
            const body: VerifiedYieldContractRsrc = convertToVerifiedYieldContractRsrc(verifiedYieldContract)
            
              return this.CirrasUnderwritingAPIService.updateVerifiedYieldContract(
                payload.etag,
                verifiedYieldContract.verifiedYieldContractGuid,
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "Verified Yield ");

                    let newVerifiedYieldContract: VerifiedYieldContract = convertToVerifiedYieldContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      LoadVerifiedYieldContractSuccess(typedAction.componentId, newVerifiedYieldContract)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(LoadVerifiedYieldContractError(typedAction.componentId, convertToErrorState(error, "Update Verified Yield "))) 
                  }
              ),
              )            
        }                  
    )
));


deleteVerifiedYield: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(DELETE_VERIFIED_YIELD),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <DeleteVerifiedYieldContractAction>action;
          let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let payload = <VerifiedYieldContract>typedAction.payload.verifiedYieldContract;

          const verifiedYieldContract = typedAction.payload.verifiedYieldContract 
          const body: VerifiedYieldContractRsrc = convertToVerifiedYieldContractRsrc(verifiedYieldContract)
          
          return this.CirrasUnderwritingAPIService.deleteVerifiedYieldContract(
            payload.etag,
            verifiedYieldContract.verifiedYieldContractGuid,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            "response")
            .pipe(
                map((response: any) => {

                  displayDeleteSuccessSnackbar(this.snackbarService, "Verified Yield ");
                  
                  return RolloverVerifiedYieldContract(typedAction.componentId, typedAction.payload.policyId);

                }),
                catchError(error =>{
                    return of(DeleteVerifiedYieldContractError(typedAction.componentId, convertToErrorState(error, "Delete Verified Yield Yield "))) 
                }
            ),
            )   
      }                  
  )
));


}

