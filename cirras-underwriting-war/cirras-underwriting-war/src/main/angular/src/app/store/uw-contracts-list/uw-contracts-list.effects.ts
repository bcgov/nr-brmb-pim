import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasUnderwritingAPIService, UwContractListRsrc} from "@cirras/cirras-underwriting-api";
import {SortDirection, TokenService} from "@wf1/core-ui";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, map, switchMap, withLatestFrom} from 'rxjs/operators';
import {convertToErrorState, convertToInventoryContractList, convertToUwContractsList} from "../../conversion/conversion-from-rest"; //convertToCalculationList
// import {SEARCH_CALCULATIONS, SearchCalculationsAction, searchCalculationsSuccess, searchCalculationsError} from "./calculations.actions";
import {formatSort, getPageInfoRequestForSearchState} from "../../utils";
import {initUwContractsListPaging} from "./uw-contracts-list.state";
import {RootState} from "../index";
import { reportPrintError, reportPrintSuccess, REPORT_PRINT_UW_CONTRACTS, SearchUwContractsAction, searchUwContractsError, searchUwContractsSuccess, SEARCH_UW_CONTRACTS } from "./uw-contracts-list.actions";
import { REST_VERSION } from "src/app/utils/constants";

@Injectable()
export class UwContractsListEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private CirrasUnderwritingAPIService: CirrasUnderwritingAPIService
    ) {
  }

  getUwContracts: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(SEARCH_UW_CONTRACTS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <SearchUwContractsAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        let pagingInfoRequest = typedAction.payload.pageInfoRequest ? typedAction.payload.pageInfoRequest : getPageInfoRequestForSearchState(store.searchUwContracts);
        let savedFilters = store.searchUwContracts? store.searchUwContracts.filters: null;
      
        let sortParam = pagingInfoRequest.sortColumn;
        sortParam = sortParam ? sortParam : 'policyNumber'
        let sortDirection = <SortDirection>pagingInfoRequest.sortDirection ? <SortDirection>pagingInfoRequest.sortDirection : 'ASC';

        let pageNumber = pagingInfoRequest.pageNumber ? pagingInfoRequest.pageNumber : initUwContractsListPaging.pageNumber;
        let pageSize = pagingInfoRequest.pageRowCount ? pagingInfoRequest.pageRowCount : initUwContractsListPaging.pageRowCount;

        
        let savedSearchGrowerFilter = savedFilters && savedFilters.searchGrower ? savedFilters.searchGrower : undefined;
        let savedSearchPolicyNumberFilter = savedFilters && savedFilters.searchPolicyNumber ? savedFilters.searchPolicyNumber : undefined;
        let savedSearchInsurancePlanFilter = savedFilters && savedFilters.selectedInsurancePlan ? savedFilters.selectedInsurancePlan : undefined;        
        let savedCropYearFilter = savedFilters && savedFilters.selectedCropYear ? savedFilters.selectedCropYear : undefined;
        let savedPolicyStatusFilter = savedFilters && savedFilters.selectedPolicyStatus ? savedFilters.selectedPolicyStatus : undefined;
        let savedOfficeFilter = savedFilters && savedFilters.selectedOffice ? savedFilters.selectedOffice : undefined;
        
        let growerFilter = typedAction.payload.filters["searchGrower"] ? typedAction.payload.filters["searchGrower"] : savedSearchGrowerFilter;
        let policyNumberFilter = typedAction.payload.filters["searchPolicyNumber"] ? typedAction.payload.filters["searchPolicyNumber"] : savedSearchPolicyNumberFilter;
        let selectedInsurancePlan = typedAction.payload.filters["selectedInsurancePlan"] ? typedAction.payload.filters["selectedInsurancePlan"] : savedSearchInsurancePlanFilter;      
        let policyStatusFilter = typedAction.payload.filters["selectedPolicyStatus"] ? typedAction.payload.filters["selectedPolicyStatus"] : savedPolicyStatusFilter;
        let cropYearFilter = typedAction.payload.filters["selectedCropYear"] ? typedAction.payload.filters["selectedCropYear"] : savedCropYearFilter;
        let selectedOfficeFilter = typedAction.payload.filters["selectedOffice"] ? typedAction.payload.filters["selectedOffice"] : savedOfficeFilter;

        return this.CirrasUnderwritingAPIService.getListOfUwContracts(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          cropYearFilter ? cropYearFilter[0] : undefined,
          selectedInsurancePlan ? selectedInsurancePlan[0] : undefined,
          selectedOfficeFilter ? selectedOfficeFilter[0] : undefined,
          policyStatusFilter ? policyStatusFilter[0] : undefined,
          policyNumberFilter ? policyNumberFilter[0] : undefined,
          growerFilter ? growerFilter[0] : undefined,
          undefined, // dataset
          sortParam,
          sortDirection,
          `${pageNumber}`,
          `${pageSize}`
        ).pipe(
          map((response: any) => {

              return searchUwContractsSuccess(typedAction.componentId, convertToUwContractsList(response ) ); 
          }),
          
          catchError(
            error => of( searchUwContractsError(typedAction.componentId, convertToErrorState(error, "Underwriting Contracts")))
            )

        );
      }
    )
  ))

// REPORT PRINT
  getUwContractsForBatchPrint: Observable<Action> = createEffect (() => this.actions.pipe(
    ofType(REPORT_PRINT_UW_CONTRACTS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(
      ([action,store]) =>{
        let typedAction = <SearchUwContractsAction>action;

        let authToken = this.tokenService.getOauthToken();
        let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

        // let pagingInfoRequest = typedAction.payload.pageInfoRequest ? typedAction.payload.pageInfoRequest : getPageInfoRequestForSearchState(store.searchUwContracts);
        let savedFilters = store.searchUwContracts? store.searchUwContracts.filters: null;
      
        // let sortParam = pagingInfoRequest.sortColumn;
        // sortParam = sortParam ? sortParam : 'policyNumber'
        // let sortDirection = <SortDirection>pagingInfoRequest.sortDirection ? <SortDirection>pagingInfoRequest.sortDirection : 'ASC';

        // let pageNumber = pagingInfoRequest.pageNumber ? pagingInfoRequest.pageNumber : initUwContractsListPaging.pageNumber;
        // let pageSize = pagingInfoRequest.pageRowCount ? pagingInfoRequest.pageRowCount : initUwContractsListPaging.pageRowCount;

        
        let savedSearchGrowerFilter = savedFilters && savedFilters.searchGrower ? savedFilters.searchGrower : undefined;
        let savedSearchPolicyNumberFilter = savedFilters && savedFilters.searchPolicyNumber ? savedFilters.searchPolicyNumber : undefined;
        let savedSearchInsurancePlanFilter = savedFilters && savedFilters.selectedInsurancePlan ? savedFilters.selectedInsurancePlan : undefined;        
        let savedCropYearFilter = savedFilters && savedFilters.selectedCropYear ? savedFilters.selectedCropYear : undefined;
        let savedPolicyStatusFilter = savedFilters && savedFilters.selectedPolicyStatus ? savedFilters.selectedPolicyStatus : undefined;
        let savedOfficeFilter = savedFilters && savedFilters.selectedOffice ? savedFilters.selectedOffice : undefined;
        
        let growerFilter = typedAction.payload.filters["searchGrower"] ? typedAction.payload.filters["searchGrower"] : savedSearchGrowerFilter;
        let policyNumberFilter = typedAction.payload.filters["searchPolicyNumber"] ? typedAction.payload.filters["searchPolicyNumber"] : savedSearchPolicyNumberFilter;
        let selectedInsurancePlan = typedAction.payload.filters["selectedInsurancePlan"] ? typedAction.payload.filters["selectedInsurancePlan"] : savedSearchInsurancePlanFilter;      
        let policyStatusFilter = typedAction.payload.filters["selectedPolicyStatus"] ? typedAction.payload.filters["selectedPolicyStatus"] : savedPolicyStatusFilter;
        let cropYearFilter = typedAction.payload.filters["selectedCropYear"] ? typedAction.payload.filters["selectedCropYear"] : savedCropYearFilter;
        let selectedOfficeFilter = typedAction.payload.filters["selectedOffice"] ? typedAction.payload.filters["selectedOffice"] : savedOfficeFilter;

        //let selectedReportType = typedAction.payload.filters["selectedReportType"] ? typedAction.payload.filters["selectedReportType"].toString() : "Inventory";

        let selectedReportSortBy = typedAction.payload.filters["selectedReportSortBy"] ? typedAction.payload.filters["selectedReportSortBy"].toString() : "policyNumber";

        let inventoryContractGuidList = typedAction.payload.filters["inventoryContractGuidList"] ? typedAction.payload.filters["inventoryContractGuidList"].toString() : "";

        return this.CirrasUnderwritingAPIService.getListOfInventoryContracts(
          requestId,
          REST_VERSION,
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          cropYearFilter ? cropYearFilter[0] : undefined,
          selectedInsurancePlan ? selectedInsurancePlan[0] : undefined,
          selectedOfficeFilter ? selectedOfficeFilter[0] : undefined,
          policyStatusFilter ? policyStatusFilter[0] : undefined,
          policyNumberFilter ? policyNumberFilter[0] : undefined,
          growerFilter ? growerFilter[0] : undefined,
          selectedReportSortBy,
          inventoryContractGuidList
        ).pipe(
          map((response: any) => {
            // debugger;
              return reportPrintSuccess(typedAction.componentId, convertToInventoryContractList(response ) ); 
          }),
          
          catchError(
            error => of(reportPrintError(typedAction.componentId, convertToErrorState(error, "Report Print ")))
          )

        );
      }
    )
  ))


}

