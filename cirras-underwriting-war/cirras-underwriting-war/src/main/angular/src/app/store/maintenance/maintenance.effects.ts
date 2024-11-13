import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, CropVarietyInsurabilityListRsrc, GradeModifierListRsrc, SeedingDeadlineListRsrc, UnderwritingYearRsrc, YieldMeasUnitConversionListRsrc} from "@cirras/cirras-underwriting-api";
import {TokenService} from "@wf1/wfcc-core-lib";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import { convertToErrorState} from "../../conversion/conversion-from-rest";  

import {RootState} from "../index";
import { MatSnackBar } from "@angular/material/snack-bar";
import { REST_VERSION } from "src/app/utils/constants";

import { ADD_UW_YEAR, AddUwYearAction, LOAD_VARIETY_INSURABILITY, LOAD_GRADE_MODIFIERS, LOAD_GRADE_MODIFIIER_TYPES, LOAD_SEDING_DEADLINES, LOAD_UW_YEARS, LoadVarietyInsurabilityAction, LoadGradeModifierTypesAction, LoadGradeModifiersAction, LoadSeedingDeadlinesAction, LoadUwYearsAction, SAVE_GRADE_MODIFIERS, SAVE_GRADE_MODIFIER_TYPES, SAVE_SEDING_DEADLINES, SaveGradeModifierTypesAction, SaveGradeModifiersAction, SaveSeedingDeadlinesAction, 
    addUwYearError, 
    loadVarietyInsurabilityError, 
    loadVarietyInsurabilitySuccess, 
    loadGradeModifierTypesError, 
    loadGradeModifierTypesSuccess, 
    loadGradeModifiersError, 
    loadGradeModifiersSuccess, 
    loadSeedingDeadlinesError, loadSeedingDeadlinesSuccess, loadUwYears, loadUwYearsError, loadUwYearsSuccess, saveGradeModifierTypesSuccess, saveGradeModifiersSuccess, saveSeedingDeadlinesSuccess, SAVE_VARIETY_INSURABILITY, SaveVarietyInsurabilityAction, saveVarietyInsurabilitySuccess, LOAD_YIELD_CONVERSION, LoadYieldConversionAction, LoadYieldConversionError, LoadYieldConversionSuccess, SAVE_YIELD_CONVERSION, SaveYieldConversionAction, SaveYieldConversionSuccess } from "./maintenance.actions";
import { convertToCropVarietyInsurabilityList, convertToGradeModifierList, convertToGradeModifierTypesList, convertToSeedingDeadlinesList, convertToUnderwritingYear, convertToUnderwritingYearList, convertToYieldConversionList } from "src/app/conversion/conversion-from-rest-maintenance";
import { CropVarietyInsurabilityList, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";
import { convertToCropVarietyInsurabilityListRsrc, convertToGradeModifierListRsrc, convertToGradeModifierTypeListRsrc, convertToSeedingDeadlineListRsrc, convertToUnderwritingYearRsrc, convertToYieldConversionListRsrc } from "src/app/conversion/conversion-to-rest-maintenance";
import { displaySaveSuccessSnackbar } from "src/app/utils/user-feedback-utils";

@Injectable()
export class MaintenanceEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService,
    private snackbarService: MatSnackBar,
    ) {
  }

  getUwYearList: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_UW_YEARS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadUwYearsAction>action;
        
        let authToken = this.tokenService.getOauthToken();
        let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
    
        return this.CirrasUnderwritingAPIService.getListOfUnderwritingYears(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`
        ).pipe(
          map((response: any) => {
              return loadUwYearsSuccess(typedAction.componentId, convertToUnderwritingYearList(response ) ); 
          }),
          
          catchError(
            error => of( loadUwYearsError(typedAction.componentId, convertToErrorState(error, "Underwriting Years List ")))
            )
  
        );
      }
    )
  ))

  addUwYear: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(ADD_UW_YEAR),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <AddUwYearAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();

            const uwYear = typedAction.payload.uwYear 
            const body: UnderwritingYearRsrc = convertToUnderwritingYearRsrc(uwYear)
            
              return this.CirrasUnderwritingAPIService.addANewUnderwritingYear(
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "New Underwriting Year ");

                    return [ 
                      //Reload List
                      loadUwYears(typedAction.componentId)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(addUwYearError(typedAction.componentId, convertToErrorState(error, "New Underwriting Year "))) 
                  }
              ),
              )            
        }                  
    )
));  


  getSeedingDeadlinesList: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(LOAD_SEDING_DEADLINES),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <LoadSeedingDeadlinesAction>action;
        
        let authToken = this.tokenService.getOauthToken();
        let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
    
        return this.CirrasUnderwritingAPIService.getAListOfSeedingDeadlines(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          typedAction.payload.cropYear,
          'response'
        ).pipe(
          map((response: any) => {
              return loadSeedingDeadlinesSuccess(typedAction.componentId, convertToSeedingDeadlinesList(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            error => of( loadSeedingDeadlinesError(typedAction.componentId, convertToErrorState(error, "Seeding Deadline List ")))
            )
  
        );
      }
    )
  ))

  saveSeedingDeadlines: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(SAVE_SEDING_DEADLINES),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <SaveSeedingDeadlinesAction>action;
            let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let seedingDeadlineList = <SeedingDeadlineList>typedAction.payload.value;
            
            const body: SeedingDeadlineListRsrc = convertToSeedingDeadlineListRsrc(seedingDeadlineList)
            
            return this.CirrasUnderwritingAPIService.saveAListOfSeedingDeadlines(
              seedingDeadlineList.etag,
              body,
              requestId,
              REST_VERSION, 
              "no-cache",  
              "no-cache",  
              `Bearer ${authToken}`,
              typedAction.cropYear,
              "response")
            .pipe(
                concatMap((response: any) => {

                  displaySaveSuccessSnackbar(this.snackbarService, "Seeding Deadlines ");

                  let newSeedingDeadlineList: SeedingDeadlineList = convertToSeedingDeadlinesList(response.body, response.headers ? response.headers.get("ETag") : null) 

                  return [      
                    saveSeedingDeadlinesSuccess(typedAction.componentId, newSeedingDeadlineList)                     
                  ]
                }),
                catchError(error =>{
                    return of(loadSeedingDeadlinesError(typedAction.componentId, convertToErrorState(error, "Save Seeding Deadlines "))) 
                }
                ),
            )            
        }                  
    )
));

// GRADE MODIFIERS TYPES
getGradeModifierTypeList: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_GRADE_MODIFIIER_TYPES),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadGradeModifierTypesAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      return this.CirrasUnderwritingAPIService.getAListOfGradeModifierTypes(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        typedAction.payload.cropYear,
        'response'
      ).pipe(
        map((response: any) => {
            return loadGradeModifierTypesSuccess(typedAction.componentId, convertToGradeModifierTypesList(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
        }),
        
        catchError(
          error => of( loadGradeModifierTypesError(typedAction.componentId, convertToErrorState(error, "Grade Modifier Types List ")))
          )

      );
    }
  )
))

saveGradeModifierTypes: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(SAVE_GRADE_MODIFIER_TYPES),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <SaveGradeModifierTypesAction>action;
          let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let gradeModifierTypeList = <GradeModifierTypeList>typedAction.payload.value;
          
          const body: GradeModifierTypeList = convertToGradeModifierTypeListRsrc(gradeModifierTypeList)
          
          return this.CirrasUnderwritingAPIService.saveAListOfGradeModifierTypes(
            gradeModifierTypeList.etag,
            body,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            typedAction.cropYear,
            "response")
          .pipe(
              concatMap((response: any) => {

                displaySaveSuccessSnackbar(this.snackbarService, "Grade Modifier Types ");

                let newGradeModifierTypesList: GradeModifierTypeList = convertToGradeModifierTypesList(response.body, response.headers ? response.headers.get("ETag") : null) 

                return [      
                  saveGradeModifierTypesSuccess(typedAction.componentId, newGradeModifierTypesList)                     
                ]
              }),
              catchError(error =>{
                  return of(loadGradeModifierTypesError(typedAction.componentId, convertToErrorState(error, "Save Grade Modifier Types "))) 
              }
              ),
          )            
      }                  
  )
));

getGradeModifierList: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_GRADE_MODIFIERS),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadGradeModifiersAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
  
      return this.CirrasUnderwritingAPIService.getAListOfGradeModifiers(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        typedAction.payload.cropYear,
        typedAction.payload.insurancePlanId,
        typedAction.payload.cropCommodityId,
        'response'
      ).pipe(
        map((response: any) => {
            return loadGradeModifiersSuccess(typedAction.componentId, convertToGradeModifierList(response.body, 
                                                                                                 response.headers ? response.headers.get("ETag") : null, 
                                                                                                 parseInt(typedAction.payload.cropYear) ) ); 
        }),
        
        catchError(
          error => of( loadGradeModifiersError(typedAction.componentId, convertToErrorState(error, "Grade Modifier List ")))
          )

      );
    }
  )
))

saveGradeModifiers: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(SAVE_GRADE_MODIFIERS),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <SaveGradeModifiersAction>action;
          let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let gradeModifierList = <GradeModifierList>typedAction.payload.value;
          
          const body: GradeModifierListRsrc = convertToGradeModifierListRsrc(gradeModifierList)

          return this.CirrasUnderwritingAPIService.saveAListOfGradeModifiers(
            gradeModifierList.etag,
            body,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            typedAction.cropYear,
            typedAction.insurancePlanId,
            typedAction.cropCommodityId,
            "response")
          .pipe(
              concatMap((response: any) => {

                displaySaveSuccessSnackbar(this.snackbarService, "Grade Modifiers ");

                let newGradeModifierList: GradeModifierList = convertToGradeModifierList(response.body, 
                                                                                         response.headers ? response.headers.get("ETag") : null,
                                                                                         parseInt(typedAction.cropYear)) 

                return [      
                  saveGradeModifiersSuccess(typedAction.componentId, newGradeModifierList)                     
                ]
              }),
              catchError(error =>{
                  return of(loadGradeModifiersError(typedAction.componentId, convertToErrorState(error, "Save Grade Modifiers "))) 
              }
              ),
          )            
      }                  
  )
));

getCropVarietyInsurability: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_VARIETY_INSURABILITY),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadVarietyInsurabilityAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
  
      return this.CirrasUnderwritingAPIService.getAListOfCropVarietyInsurabilities(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        typedAction.payload.insurancePlanId,
        typedAction.payload.loadForEdit,
        'response'
      ).pipe(
        map((response: any) => {
            return loadVarietyInsurabilitySuccess(typedAction.componentId, convertToCropVarietyInsurabilityList(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
        }),
        
        catchError(
          error => of( loadVarietyInsurabilityError(typedAction.componentId, convertToErrorState(error, "Crop Variety Insurability List ")))
          )

      );
    }
  )
))

saveCropVarietyInsurability: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(SAVE_VARIETY_INSURABILITY),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <SaveVarietyInsurabilityAction>action;
          let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let cropVarietyInsurabilityList = <CropVarietyInsurabilityList>typedAction.payload.value;
          
          const body: CropVarietyInsurabilityListRsrc = convertToCropVarietyInsurabilityListRsrc(cropVarietyInsurabilityList)

          return this.CirrasUnderwritingAPIService.saveAListOfCropVarietyInsurabilities(
            typedAction.etag,
            body,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            typedAction.insurancePlanId,
            typedAction.loadForEdit,
            "response")
          .pipe(
              concatMap((response: any) => {

                displaySaveSuccessSnackbar(this.snackbarService, "Variety Insurability ");

                let newCropVarietyInsurabilityList: CropVarietyInsurabilityList = convertToCropVarietyInsurabilityList(response.body, 
                                                                                         response.headers ? response.headers.get("ETag") : null) 

                return [      
                  saveVarietyInsurabilitySuccess(typedAction.componentId, newCropVarietyInsurabilityList)                     
                ]
              }),
              catchError(error =>{
                  return of(loadVarietyInsurabilityError(typedAction.componentId, convertToErrorState(error, "Save Variety Insurability "))) 
              }
              ),
          )            
      }                  
  )
));


getYieldConversion: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(LOAD_YIELD_CONVERSION),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <LoadYieldConversionAction>action;
      
      let authToken = this.tokenService.getOauthToken();
      let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
  
      return this.CirrasUnderwritingAPIService.getAListOfYieldMeasurementUnitConversions(
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,
        typedAction.payload.insurancePlanId,
        typedAction.payload.srcYieldMeasUnitTypeCode,
        typedAction.payload.targetYieldMeasUnitTypeCode,
        'response'
      ).pipe(
        map((response: any) => {
            return LoadYieldConversionSuccess(typedAction.componentId, convertToYieldConversionList(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
        }),
        
        catchError(
          error => of( LoadYieldConversionError(typedAction.componentId, convertToErrorState(error, "Yield Conversion List ")))
          )

      );
    }
  )
))


saveYieldConversion: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(SAVE_YIELD_CONVERSION),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <SaveYieldConversionAction>action;
          let requestId = `${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let yieldConversionList = <YieldMeasUnitConversionList>typedAction.payload.value;
          
          const body: YieldMeasUnitConversionListRsrc = convertToYieldConversionListRsrc(yieldConversionList)
          
          return this.CirrasUnderwritingAPIService.saveAListOfYieldMeasurementUnitConversions(
            yieldConversionList.etag,
            body,
            requestId,
            REST_VERSION, 
            "no-cache",  
            "no-cache",  
            `Bearer ${authToken}`,
            typedAction.insurancePlanId,
            typedAction.srcYieldMeasUnitTypeCode,
            typedAction.targetYieldMeasUnitTypeCode,
            "response")
          .pipe(
              concatMap((response: any) => {

                displaySaveSuccessSnackbar(this.snackbarService, "Yield Conversion ");

                let newYieldConversionList: YieldMeasUnitConversionList = convertToYieldConversionList(response.body, response.headers ? response.headers.get("ETag") : null) 

                return [      
                  SaveYieldConversionSuccess(typedAction.componentId, newYieldConversionList)                     
                ]
              }),
              catchError(error =>{
                  return of(LoadYieldConversionError(typedAction.componentId, convertToErrorState(error, "Save Yield Conversion "))) 
              }
              ),
          )            
      }                  
  )
));

}