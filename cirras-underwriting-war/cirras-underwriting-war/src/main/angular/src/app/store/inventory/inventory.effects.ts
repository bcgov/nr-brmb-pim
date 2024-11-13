import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, InventoryContractRsrc} from "@cirras/cirras-underwriting-api";
import {AppConfigService, TokenService} from "@wf1/wfcc-core-lib";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import { convertToErrorState, convertToInventoryContract} from "../../conversion/conversion-from-rest";  

import {RootState} from "../index";
import { InventoryContractAction, ADD_NEW_INVENTORY_CONTRACT, LoadInventoryContractAction, LoadInventoryContractError, LoadInventoryContractSuccess, LOAD_INVENTORY_CONTRACT, RolloverInventoryContractAction, ROLLOVER_INVENTORY_CONTRACT, UPDATE_INVENTORY_CONTRACT, GET_INVENTORY_REPORT_BYTES, GetInventoryReportAction, DELETE_INVENTORY_CONTRACT, RolloverInventoryContract, DeleteInventoryContractAction, DeleteInventoryContractError} from "./inventory.actions";
import { convertToInventoryContractRsrc } from "src/app/conversion/conversion-to-rest";
import { InventoryContract } from "src/app/conversion/models";
import { displayErrorMessage, displaySaveSuccessSnackbar, displayDeleteSuccessSnackbar, displaySuccessSnackbar } from "src/app/utils/user-feedback-utils";
import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";
import { HttpClient } from '@angular/common/http';
import { GetReportError, GetReportSuccess } from "../dop/dop.actions";
import { LoadGrowerContract } from "../grower-contract/grower-contract.actions";
import { INVENTORY_COMPONENT_ID } from "./inventory.state";

@Injectable()
export class InventoryEffects {
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

  getUnseededInventory: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_INVENTORY_CONTRACT),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadInventoryContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let inventoryContractGuid = typedAction.payload.inventoryContractGuid 
        // comment this once the backend is implemented
        if ( inventoryContractGuid.length == 0) {
          inventoryContractGuid = ""
        }

        return this.CirrasUnderwritingAPIService.getTheInventoryContract(
          inventoryContractGuid,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          'response'
        ).pipe(
          map((response: any) => {

              return LoadInventoryContractSuccess(typedAction.componentId, convertToInventoryContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadInventoryContractError(typedAction.componentId, convertToErrorState(error, "Load Inventory ")))
            )

        );
      }
    )
  ))

  getRolledOverInventory: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(ROLLOVER_INVENTORY_CONTRACT),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <RolloverInventoryContractAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let policyId = typedAction.payload.policyId 

        return this.CirrasUnderwritingAPIService.rolloverTheInventoryContract(
          policyId,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          "response"
        ).pipe(
          map((response: any) => {

              return LoadInventoryContractSuccess(typedAction.componentId, convertToInventoryContract(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( LoadInventoryContractError(typedAction.componentId, convertToErrorState(error, "Rollover Inventory ")))
            )

        );
      }
    )
  ))



addNewInventory: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(ADD_NEW_INVENTORY_CONTRACT),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <InventoryContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();

            const policyId = typedAction.policyId
            const inventoryContract = typedAction.payload.inventoryContract 
            const body: InventoryContractRsrc = convertToInventoryContractRsrc(inventoryContract)
            
              return this.CirrasUnderwritingAPIService.addANewInventoryContract(
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySuccessSnackbar(this.snackbarService, "Updates have been saved successfully.");

                    let newInventoryContract: InventoryContract = convertToInventoryContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    this.store.dispatch(LoadGrowerContract(INVENTORY_COMPONENT_ID, policyId)) // to update the side navigation links

                    return [                                                                         
                      LoadInventoryContractSuccess(typedAction.componentId, newInventoryContract)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(LoadInventoryContractError(typedAction.componentId, convertToErrorState(error, "New inventory "))) 
                  }
              ),
              )            
            // }
        }                  
    )
));


updateInventory: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(UPDATE_INVENTORY_CONTRACT),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <InventoryContractAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <InventoryContract>typedAction.payload.inventoryContract;

            const policyId = typedAction.policyId
            const inventoryContract = typedAction.payload.inventoryContract 
            const body: InventoryContractRsrc = convertToInventoryContractRsrc(inventoryContract)
            
              return this.CirrasUnderwritingAPIService.updateInventoryContract(
                payload.etag,
                payload.inventoryContractGuid,
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySuccessSnackbar(this.snackbarService, "Updates have been saved successfully.");

                    let newInventoryContract: InventoryContract = convertToInventoryContract(response.body, response.headers ? response.headers.get("ETag") : null) 

                    this.store.dispatch(LoadGrowerContract(INVENTORY_COMPONENT_ID, policyId)) // to update the side navigation links
                    return [                                                                         
                      LoadInventoryContractSuccess(typedAction.componentId, newInventoryContract)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(LoadInventoryContractError(typedAction.componentId, convertToErrorState(error, "Inventory "))) 
                  }
              ),
              )            
            // }
        }                  
    )
));

getInventoryReportBytes: Observable<Action> = createEffect (() =>  this.actions.pipe(
  ofType(GET_INVENTORY_REPORT_BYTES),
  debounceTime(500),
  
  switchMap(
      (action) => {
          
          let typedAction = <GetInventoryReportAction>action;
          let reportName = typedAction.payload.reportName ? typedAction.payload.reportName : "report.pdf";
          let policyId = typedAction.payload.policyId ? typedAction.payload.policyId : "";
          let cropYear = typedAction.payload.cropYear ? typedAction.payload.cropYear : "";
          let insurancePlanId = typedAction.payload.insurancePlanId ? typedAction.payload.insurancePlanId : "";
          let officeId = typedAction.payload.officeId ? typedAction.payload.officeId : "";
          let policyStatusCode = typedAction.payload.policyStatusCode ? typedAction.payload.policyStatusCode : "";
          let policyNumber = typedAction.payload.policyNumber ? typedAction.payload.policyNumber : "";
          let growerInfo = typedAction.payload.growerInfo ? typedAction.payload.growerInfo : "";
          let sortColumn = typedAction.payload.sortColumn ? typedAction.payload.sortColumn : "";
 
          let endpoint = this.appConfig.getConfig().rest["cirras_underwriting"] +"/inventoryContracts/report?policyIds=" + policyId +
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

                  displaySuccessSnackbar(this.snackbarService,"PDF downloaded successfully.")
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


// Delete inventory
deleteInventory: Observable<Action> = createEffect(() => this.actions.pipe(
  ofType(DELETE_INVENTORY_CONTRACT),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <DeleteInventoryContractAction>action;
          let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();

          let payload = typedAction.payload;

            return this.CirrasUnderwritingAPIService.deleteInventoryContract(
              payload.etag,
              payload.inventoryContractGuid,
              requestId,
              REST_VERSION, 
              "no-cache",  
              "no-cache",  
              `Bearer ${authToken}`,
              "response")
            .pipe(
                concatMap((response: any) => {

                  displayDeleteSuccessSnackbar(this.snackbarService, "Inventory ");

                  this.store.dispatch(LoadGrowerContract(typedAction.componentId, payload.policyId)) // to update the side navigation links

                  return  [
                    RolloverInventoryContract(typedAction.componentId, payload.policyId) 
                  ]                                                                      
                }),
                catchError(error =>{
                    return of(DeleteInventoryContractError(typedAction.componentId, convertToErrorState(error, "Delete inventory "))) 
                }
            ),
            ) 
      }                  
  )
));


}

