import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { Action, Store } from "@ngrx/store";
import { DefaultService as CirrasUnderwritingAPIService } from "@cirras/cirras-underwriting-api";
import { AppConfigService, TokenService } from "@wf1/core-ui";
import { UUID } from "angular2-uuid";
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, map, switchMap, withLatestFrom } from 'rxjs/operators';
import { convertToErrorState } from "../../conversion/conversion-from-rest";  
import { RootState } from "../index";
import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";
import { convertToVerifiedYieldContract } from "src/app/conversion/conversion-from-rest-yield";
import { HttpClient } from '@angular/common/http';
import { LOAD_VERIFIED_YIELD, LoadVerifiedYieldContractAction, LoadVerifiedYieldContractError, LoadVerifiedYieldContractSuccess, ROLLOVER_VERIFIED_YIELD, RolloverVerifiedYieldContractAction } from "./verified-yield.actions";

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

//   // TODO:
// addNewDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
//     ofType(ADD_NEW_DOP),
//     withLatestFrom(this.store),
//     debounceTime(500),
//     switchMap(
//         ([action, store]) => {
//             let typedAction = <DopYieldContractAction>action;
//             let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
//             let authToken = this.tokenService.getOauthToken();
//             let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

//             const dopYieldContract = typedAction.payload.dopYieldContract 
//             const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
            
//               return this.CirrasUnderwritingAPIService.addANewDopYieldContract(
//                 body,
//                 requestId,
//                 REST_VERSION, 
//                 "no-cache",  
//                 "no-cache",  
//                 `Bearer ${authToken}`,
//                 "response")
//               .pipe(
//                   concatMap((response: any) => {

//                     displaySaveSuccessSnackbar(this.snackbarService, "DOP Yield ");

//                     let newdopYieldContract: DopYieldContract = convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null) 

//                     return [                                                                         
//                       LoadDopYieldContractSuccess(typedAction.componentId, newdopYieldContract)                             
//                     ]
//                   }),
//                   catchError(error =>{
//                       return of(LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "New DOP Yield "))) 
//                   }
//               ),
//               )   
//         }                  
//     )
// ))

// // TODO:
// updateDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
//     ofType(UPDATE_DOP),
//     withLatestFrom(this.store),
//     debounceTime(500),
//     switchMap(
//         ([action, store]) => {
//             let typedAction = <DopYieldContractAction>action;
//             let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
//             let authToken = this.tokenService.getOauthToken();
//             let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

//             const dopYieldContract = typedAction.payload.dopYieldContract 
//             const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
            
//               return this.CirrasUnderwritingAPIService.updateDopYieldContract(
//                 payload.etag,
//                 dopYieldContract.declaredYieldContractGuid,
//                 body,
//                 requestId,
//                 REST_VERSION, 
//                 "no-cache",  
//                 "no-cache",  
//                 `Bearer ${authToken}`,
//                 "response")
//               .pipe(
//                   concatMap((response: any) => {

//                     displaySaveSuccessSnackbar(this.snackbarService, "DOP Yield ");

//                     let newInventoryContract: DopYieldContract = convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null) 

//                     return [                                                                         
//                       LoadDopYieldContractSuccess(typedAction.componentId, newInventoryContract)                             
//                     ]
//                   }),
//                   catchError(error =>{
//                       return of(LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "New inventory "))) 
//                   }
//               ),
//               )            
//         }                  
//     )
// ));

// // TODO:
// deleteDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
//   ofType(DELETE_DOP),
//   withLatestFrom(this.store),
//   debounceTime(500),
//   switchMap(
//       ([action, store]) => {
//           let typedAction = <DeleteDopYieldContractAction>action;
//           let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
//           let authToken = this.tokenService.getOauthToken();
//           let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

//           const dopYieldContract = typedAction.payload.dopYieldContract 
//           const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
          
//           return this.CirrasUnderwritingAPIService.deleteDopYieldContract(
//             payload.etag,
//             dopYieldContract.declaredYieldContractGuid,
//             requestId,
//             REST_VERSION, 
//             "no-cache",  
//             "no-cache",  
//             `Bearer ${authToken}`,
//             "response")
//             .pipe(
//                 map((response: any) => {

//                   displayDeleteSuccessSnackbar(this.snackbarService, "DOP Yield ");
                  
//                   return RolloverDopYieldContract(typedAction.componentId, typedAction.payload.policyId);

//                 }),
//                 catchError(error =>{
//                     return of(DeleteDopYieldContractError(typedAction.componentId, convertToErrorState(error, "Delete DOP Yield "))) 
//                 }
//             ),
//             )   
//       }                  
//   )
// ));


}

