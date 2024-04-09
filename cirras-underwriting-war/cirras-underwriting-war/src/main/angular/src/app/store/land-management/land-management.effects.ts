import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, LegalLandRsrc, UwContractListRsrc} from "@cirras/cirras-underwriting-api";
import {SortDirection, TokenService} from "@wf1/core-ui";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, concatMap, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import {convertToErrorState, convertToLegalLand, convertToLegalLandList, convertToRiskAreaList} from "../../conversion/conversion-from-rest";
import {formatSort, getPageInfoRequestForSearchState} from "../../utils";
import {MANAGE_LEGAL_LAND_COMPONENT_ID, initLandSearchPaging} from "./land-management.state";
import {RootState} from "../index";
import { SearchLandAction, searchLandError, searchLandSuccess, SEARCH_LAND, GET_LEGAL_LAND, GetLegalLandAction, getLegalLandSuccess, getLegalLandError, ADD_NEW_LEGAL_LAND, ManageLegalLandAction, getLegalLand, UPDATE_LEGAL_LAND, GET_RISK_AREA_LIST, GetRiskAreaListAction, getRiskAreaListSuccess, getRiskAreaListError, DELETE_LEGAL_LAND, ClearLegalLand, deleteLegalLandError } from "./land-management.actions";
import { REST_VERSION, ResourcesRoutes } from "src/app/utils/constants";
import { convertToLegalLandRsrc } from "src/app/conversion/conversion-to-rest";
import { displayDeleteSuccessSnackbar, displaySaveSuccessSnackbar } from "src/app/utils/user-feedback-utils";
import { MatSnackBar } from "@angular/material/snack-bar";
import { LegalLand } from "src/app/conversion/models";
import { Router } from "@angular/router";

@Injectable()
export class LandListEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService,
    private snackbarService: MatSnackBar,
    private router: Router
    ) {
  }

  getLand: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(SEARCH_LAND),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <SearchLandAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let pagingInfoRequest = typedAction.payload.pageInfoRequest ? typedAction.payload.pageInfoRequest : getPageInfoRequestForSearchState(store.searchLand);
        let savedFilters = store.searchLand? store.searchLand.filters: null;
      
        let sortParam = pagingInfoRequest.sortColumn;
        sortParam = sortParam ? sortParam : 'otherDescription'
        let sortDirection = <SortDirection>pagingInfoRequest.sortDirection ? <SortDirection>pagingInfoRequest.sortDirection : 'ASC';

        let pageNumber = pagingInfoRequest.pageNumber ? pagingInfoRequest.pageNumber : initLandSearchPaging.pageNumber;
        let pageSize = pagingInfoRequest.pageRowCount ? pagingInfoRequest.pageRowCount : initLandSearchPaging.pageRowCount;

        let savedSearchLegalLocationFilter = savedFilters && savedFilters.searchLegalLocation ? savedFilters.searchLegalLocation : undefined;
        let savedSearchLandIdentifierFilter = savedFilters && savedFilters.searchLandIdentifier ? savedFilters.searchLandIdentifier : undefined;
        let savedSearchGrowerFilter = savedFilters && savedFilters.searchGrower ? savedFilters.searchGrower : undefined;
        let savedSearchDatasetFilter = savedFilters && savedFilters.selectedDataset ? savedFilters.selectedDataset : undefined;     
        let savedIsWildCardSearch = savedFilters && savedFilters.isWildCardSearch ? savedFilters.isWildCardSearch : true;
        let savedSearchByLegalLocOrLegalDesc = savedFilters && savedFilters.searchByLegalLocOrLegalDesc ? savedFilters.searchByLegalLocOrLegalDesc : false;

        let searchLegalLocationFilter = typedAction.payload.filters["searchLegalLocation"] ? typedAction.payload.filters["searchLegalLocation"] : savedSearchLegalLocationFilter;
        let searchLandIdentifierFilter = typedAction.payload.filters["searchLandIdentifier"] ? typedAction.payload.filters["searchLandIdentifier"] : savedSearchLandIdentifierFilter;
        let growerFilter = typedAction.payload.filters["searchGrower"] ? typedAction.payload.filters["searchGrower"] : savedSearchGrowerFilter;
        let selectedDatasetFilter = typedAction.payload.filters["selectedDataset"] ? typedAction.payload.filters["selectedDataset"] : savedSearchDatasetFilter;      
        let isWildCardSearch = typedAction.payload.filters["isWildCardSearch"] ? typedAction.payload.filters["isWildCardSearch"] : savedIsWildCardSearch;      
        let searchByLegalLocOrLegalDesc = typedAction.payload.filters["searchByLegalLocOrLegalDesc"] ? typedAction.payload.filters["searchByLegalLocOrLegalDesc"] : savedSearchByLegalLocOrLegalDesc;      

        return this.CirrasUnderwritingAPIService.getListOfLegalLand(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          searchLegalLocationFilter ? searchLegalLocationFilter[0] : undefined,
          searchLandIdentifierFilter ? searchLandIdentifierFilter[0] : undefined,
          growerFilter ? growerFilter[0] : undefined,
          selectedDatasetFilter ? selectedDatasetFilter[0] : undefined,
          isWildCardSearch ? isWildCardSearch[0] : true,
          searchByLegalLocOrLegalDesc ? searchByLegalLocOrLegalDesc[0] : 'true',
          sortParam,
          sortDirection,
          `${pageNumber}`,
          `${pageSize}`
        ).pipe(
          map((response: any) => {
            return searchLandSuccess(typedAction.componentId, convertToLegalLandList(response ) ); 
          }),
          
          catchError(
            error => of( searchLandError(typedAction.componentId, convertToErrorState(error, "Land List ")))
            )

        );
      }
    )
  ))

  getLegalLand: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(GET_LEGAL_LAND),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <GetLegalLandAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let legalLandId = typedAction.payload.legalLandId 
  
        if ( !legalLandId) {
          legalLandId = ""
        }

        return this.CirrasUnderwritingAPIService.getTheLegalLand (
          legalLandId,
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,          
          'response'
        ).pipe(
          map((response: any) => {
            
              return getLegalLandSuccess(typedAction.componentId, convertToLegalLand(response.body, response.headers ? response.headers.get("ETag") : null ) ); 
          }),
          
          catchError(
            
            error => of( getLegalLandError(typedAction.componentId, convertToErrorState(error, "Load Legal Land ")))
            )

        );
      }
    )
  ))


  addNewLegalLand: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(ADD_NEW_LEGAL_LAND),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <ManageLegalLandAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();

            const legalLand = typedAction.payload.legalLand 
            const body: LegalLandRsrc = convertToLegalLandRsrc(legalLand)
            
              return this.CirrasUnderwritingAPIService.addANewLegalLand(
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "Legal Land ");

                    let newLegalLand: LegalLandRsrc = convertToLegalLand(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      getLegalLandSuccess(typedAction.componentId, newLegalLand)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(getLegalLandError(typedAction.componentId, convertToErrorState(error, "New legal land "))) 
                  }
              ),
              )            
        }                  
    )
));


updateLegalLand: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(UPDATE_LEGAL_LAND),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
        ([action, store]) => {
            let typedAction = <ManageLegalLandAction>action;
            let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
            let authToken = this.tokenService.getOauthToken();
            let payload = <LegalLand>typedAction.payload.legalLand;

            const legalLand = typedAction.payload.legalLand 
            const body: LegalLandRsrc = convertToLegalLandRsrc(legalLand)
            
              return this.CirrasUnderwritingAPIService.updateLegalLand(
                payload.etag,
                legalLand.legalLandId.toString(),
                body,
                requestId,
                REST_VERSION, 
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                  concatMap((response: any) => {

                    displaySaveSuccessSnackbar(this.snackbarService, "Legal Land ");

                    let newLegalLand: LegalLand = convertToLegalLand(response.body, response.headers ? response.headers.get("ETag") : null) 

                    return [                                                                         
                      getLegalLandSuccess(typedAction.componentId, newLegalLand)                             
                    ]
                  }),
                  catchError(error =>{
                      return of(getLegalLandError(typedAction.componentId, convertToErrorState(error, "Legal Land "))) 
                  }
              ),
              )  
        }                  
    )
));

deleteLegalLand: Observable<Action> = createEffect(() => this.actions.pipe(
  ofType(DELETE_LEGAL_LAND),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
      ([action, store]) => {
          let typedAction = <ManageLegalLandAction>action;
          let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
          let authToken = this.tokenService.getOauthToken();
          let payload = <LegalLand>typedAction.payload.legalLand;
          
          return this.CirrasUnderwritingAPIService.deleteLegalLand(
                payload.etag, 
                payload.legalLandId.toString(),
                requestId,
                REST_VERSION,
                "no-cache",  
                "no-cache",  
                `Bearer ${authToken}`,
                "response")
              .pipe(
                map((response: any) => {

                  displayDeleteSuccessSnackbar(this.snackbarService, "Legal Land ");

                  this.router.navigate(["/" + ResourcesRoutes.LAND_LIST]);

                  return ClearLegalLand();
                  
                }),
                catchError(error =>{
                    return of(deleteLegalLandError(typedAction.componentId, convertToErrorState(error, "Legal Land "))) 
                }
            ),
            )  
      }                  
  )
));


getRiskAreaList: Observable<Action> = createEffect (() => this.actions.pipe(
  ofType(GET_RISK_AREA_LIST),
  withLatestFrom(this.store),
  debounceTime(500),
  switchMap(
    ([action,store]) =>{
      let typedAction = <GetRiskAreaListAction>action;

      let authToken = this.tokenService.getOauthToken();
      let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      return this.CirrasUnderwritingAPIService.getAListOfRiskAreas (
        requestId,
        REST_VERSION,
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`,   
        "",       
        'response'
      ).pipe(
        map((response: any) => {
            return getRiskAreaListSuccess(typedAction.componentId, convertToRiskAreaList(response.body) ); 
        }),
        
        catchError(
          error => of( getRiskAreaListError(typedAction.componentId, convertToErrorState(error, "Load Risk Area List ")))
          )

      );
    }
  )
))
}
