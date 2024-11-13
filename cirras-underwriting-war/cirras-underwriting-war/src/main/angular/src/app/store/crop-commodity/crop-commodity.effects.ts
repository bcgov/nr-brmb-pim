import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, InventoryContractRsrc, UwContractListRsrc} from "@cirras/cirras-underwriting-api";
import {TokenService} from "@wf1/wfcc-core-lib";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import {convertToCropCommodityList, convertToErrorState, convertToInventoryContract, convertToUwContract} from "../../conversion/conversion-from-rest";  

import {RootState} from "../index";
import { LOAD_CROP_COMMODITY, LoadCropCommodityListAction, LoadCropCommodityListSuccess, LOAD_UNDERSEEDED_CROP_COMMODITY, LoadUnderSeededCropCommodityListSuccess, LoadCropCommodityListError, LoadUnderSeededCropCommodityListError} from "./crop-commodity.actions";

import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";

@Injectable()
export class CropCommodityEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService,
    private snackbarService: MatSnackBar
    ) {
  }

getCropCommodityList: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_CROP_COMMODITY),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadCropCommodityListAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      let insurancePlanId = typedAction.payload.insurancePlanId ? typedAction.payload.insurancePlanId : ""
      let cropYear = typedAction.payload.cropYear ? typedAction.payload.cropYear : ""
      let commodityType = typedAction.payload.commodityType ? typedAction.payload.commodityType : ""
      let loadChildren = typedAction.payload.loadChildren ? typedAction.payload.loadChildren : "Y"
      
      return this.CirrasUnderwritingAPIService.getListOfCropCommodities(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        insurancePlanId,
        cropYear,
        commodityType,
        loadChildren
      ).pipe(
        map((response: any) => {

            return LoadCropCommodityListSuccess(typedAction.componentId, convertToCropCommodityList(response ) ); 
        }),
        
        catchError(
          error => of( LoadCropCommodityListError(typedAction.componentId, convertToErrorState(error, "Crop Commodity ")))
          )

      );
    }
  )
))

// same backend call but store it in different part of the ngrx store
getUnderSeededCropCommodityList: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_UNDERSEEDED_CROP_COMMODITY),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadCropCommodityListAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      let insurancePlanId = typedAction.payload.insurancePlanId ? typedAction.payload.insurancePlanId : ""
      let cropYear = typedAction.payload.cropYear ? typedAction.payload.cropYear : ""
      let commodityType = typedAction.payload.commodityType ? typedAction.payload.commodityType : ""
      let loadChildren = typedAction.payload.loadChildren ? typedAction.payload.loadChildren : "Y"
      
      return this.CirrasUnderwritingAPIService.getListOfCropCommodities(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        insurancePlanId,
        cropYear,
        commodityType,
        loadChildren
      ).pipe(
        map((response: any) => {

            return LoadUnderSeededCropCommodityListSuccess(typedAction.componentId, convertToCropCommodityList(response ) ); 
        }),
        
        catchError(
          error => of( LoadUnderSeededCropCommodityListError(typedAction.componentId, convertToErrorState(error, "Underseeded Crop Commodity ")))
          )

      );
    }
  )
))


}

