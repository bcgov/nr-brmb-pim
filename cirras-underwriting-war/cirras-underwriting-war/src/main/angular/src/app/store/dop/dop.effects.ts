import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, DopYieldContractRsrc} from "@cirras/cirras-underwriting-api";
import {AppConfigService, TokenService} from "@wf1/core-ui";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import { convertToErrorState} from "../../conversion/conversion-from-rest";  

import {RootState} from "../index";
import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";
import { 
  LoadDopYieldContractAction, 
  LoadDopYieldContractError, 
  LoadDopYieldContractSuccess, 
  LoadYieldMeasUnitListAction, 
  loadYieldMeasUnitListError, 
  loadYieldMeasUnitListSuccess, 
  LOAD_DOP, LOAD_YIELD_MEAS_UNITS, 
  RolloverDopYieldContractAction, 
  ROLLOVER_DOP, 
  ADD_NEW_DOP, 
  DopYieldContractAction, 
  UPDATE_DOP, 
  DELETE_DOP, 
  DeleteDopYieldContractError, 
  DeleteDopYieldContractAction, 
  RolloverDopYieldContract, 
  GET_DOCUMENT_BYTES, 
  GetDopReportAction, 
  GetReportError,
  GetReportSuccess
} from "./dop.actions";
import { convertToDopContract, convertToYieldMeasUnitList } from "src/app/conversion/conversion-from-rest-yield";
import { DopYieldContract } from "src/app/conversion/models-yield";
import { convertToDopYieldContractRsrc } from "src/app/conversion/conversion-to-rest-yield";
import { displayDeleteSuccessSnackbar, displaySaveSuccessSnackbar, displayErrorMessage } from "src/app/utils/user-feedback-utils";

import { HttpClient } from '@angular/common/http';

@Injectable()
export class DopEffects {
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

  getYieldMeasUnitTypeCodeList: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_YIELD_MEAS_UNITS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadYieldMeasUnitListAction>action;
        
        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
  
        let insurancePlanId = typedAction.payload.insurancePlanId ? typedAction.payload.insurancePlanId : ""
        
        return this.CirrasUnderwritingAPIService.getAListOfYieldMeasurementUnitTypeCodes(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          insurancePlanId
        ).pipe(
          map((response: any) => {
  
              return loadYieldMeasUnitListSuccess(typedAction.componentId, convertToYieldMeasUnitList(response ) ); 
          }),
          
          catchError(
            error => of( loadYieldMeasUnitListError(typedAction.componentId, convertToErrorState(error, "Yeild Measurement Units ")))
            )
  
        );
      }
    )
  ))


  getRolledOverDop: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(ROLLOVER_DOP),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <RolloverDopYieldContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let policyId = typedAction.payload.policyId 

        return this.CirrasUnderwritingAPIService.rolloverTheDopYieldContract(
          policyId,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          "response"
        ).pipe(
          map((response: any) => {

              return LoadDopYieldContractSuccess(typedAction.componentId, convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "Rollover DOP ")))
            )

        );
      }
    )
  ))

  getDop: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_DOP),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadDopYieldContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let declaredYieldContractGuid = typedAction.payload.declaredYieldContractGuid 
        // comment this once the backend is implemented
        if ( declaredYieldContractGuid.length == 0) {
          declaredYieldContractGuid = ""
        }

        return this.CirrasUnderwritingAPIService.getTheDopYieldContract(
          declaredYieldContractGuid,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          'response'
        ).pipe(
          map((response: any) => {

              return LoadDopYieldContractSuccess(typedAction.componentId, convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "Load DOP ")))
            )

        );
      }
    )
  ))

addNewDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(ADD_NEW_DOP),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <DopYieldContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

            const dopYieldContract = typedAction.payload.dopYieldContract 
            const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
            
              return this.CirrasUnderwritingAPIService.addANewDopYieldContract(
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "DOP Yield ");

                    let newdopYieldContract: DopYieldContract = convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      LoadDopYieldContractSuccess(typedAction.componentId, newdopYieldContract)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "New DOP Yield "))) 
                  }
              ),
              )   
        }                  
    )
))

updateDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(UPDATE_DOP),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <DopYieldContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

            const dopYieldContract = typedAction.payload.dopYieldContract 
            const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
            
              return this.CirrasUnderwritingAPIService.updateDopYieldContract(
                payload.etag,
                dopYieldContract.declaredYieldContractGuid,
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "DOP Yield ");

                    let newInventoryContract: DopYieldContract = convertToDopContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      LoadDopYieldContractSuccess(typedAction.componentId, newInventoryContract)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(LoadDopYieldContractError(typedAction.componentId, convertToErrorState(error, "New inventory "))) 
                  }
              ),
              )            
        }                  
    )
));


deleteDopYield: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(DELETE_DOP),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <DeleteDopYieldContractAction>action;
          let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let payload = <DopYieldContract>typedAction.payload.dopYieldContract;

          const dopYieldContract = typedAction.payload.dopYieldContract 
          const body: DopYieldContractRsrc = convertToDopYieldContractRsrc(dopYieldContract)
          
          return this.CirrasUnderwritingAPIService.deleteDopYieldContract(
            payload.etag,
            dopYieldContract.declaredYieldContractGuid,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            "response")
            .pipe(
                map((response: any) => {

                  displayDeleteSuccessSnackbar(this.snackbarService, "DOP Yield ");
                  
                  return RolloverDopYieldContract(typedAction.componentId, typedAction.payload.policyId);

                }),
                catchError(error =>{
                    return of(DeleteDopYieldContractError(typedAction.componentId, convertToErrorState(error, "Delete DOP Yield "))) 
                }
            ),
            )   
      }                  
  )
));

getDocumentBytes: Observable<Action> = createEffect (() =>  this.actions.pipe(
  ofType(GET_DOCUMENT_BYTES),
  debounceTime(500),
  
  switchMap(
      (action) => {
          
          let typedAction = <GetDopReportAction>action;
          let reportName = typedAction.payload.reportName ? typedAction.payload.reportName : "report.pdf";
          let policyId = typedAction.payload.policyId ? typedAction.payload.policyId : "";
          let cropYear = typedAction.payload.cropYear ? typedAction.payload.cropYear : "";
          let insurancePlanId = typedAction.payload.insurancePlanId ? typedAction.payload.insurancePlanId : "";
          let officeId = typedAction.payload.officeId ? typedAction.payload.officeId : "";
          let policyStatusCode = typedAction.payload.policyStatusCode ? typedAction.payload.policyStatusCode : "";
          let policyNumber = typedAction.payload.policyNumber ? typedAction.payload.policyNumber : "";
          let growerInfo = typedAction.payload.growerInfo ? typedAction.payload.growerInfo : "";
          let sortColumn = typedAction.payload.sortColumn ? typedAction.payload.sortColumn : "";
 
          let endpoint = this.appConfig.getConfig().rest["cirras_underwriting"] +"/dopYieldContracts/report?policyIds=" + policyId +
            "&cropYear=" + cropYear + "&insurancePlanId=" + insurancePlanId + "&officeId=" + officeId + "&policyStatusCode=" + policyStatusCode +
            "&policyNumber=" + policyNumber + "&growerInfo=" + encodeURI(growerInfo) + "&sortColumn=" + sortColumn

          return this.httpClient
          .get(endpoint, {observe: 'response', responseType:'blob'})
          .pipe(
              map((response: any) => {
                
                  const blob = new Blob([response.body], {type: response.headers.get('Content-Type')});
                  const url= window.URL.createObjectURL(blob);
                  
                  var link = document.createElement('a');
                  link.href = url;
                  link.download = reportName 

                  // this is necessary as link.click() does not work on the latest firefox
                  link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

                  setTimeout(function () {
                      // For Firefox it is necessary to delay revoking the ObjectURL
                      window.URL.revokeObjectURL(url);
                      link.remove();
                  }, 100);

                  return GetReportSuccess(response)
              }),
              catchError(error => {
                  
                  displayErrorMessage(this.snackbarService, "There was a problem with downloading the printout: " )

                  return of(GetReportError(error))
                }
              )
          )
      }
  )
));




}

