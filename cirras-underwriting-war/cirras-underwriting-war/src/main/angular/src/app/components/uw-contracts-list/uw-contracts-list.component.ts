
import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input} from "@angular/core";
import { CropCommodityList, UwContract } from '../../conversion/models';
import {UwContractsListComponentModel} from "./uw-contracts-list.component.model";
import {CollectionComponent} from "../common/base-collection/collection.component";
import {AfterViewInit, OnChanges, SimpleChanges} from "@angular/core";
import {getCodeOptions} from "../../utils/code-table-utils";
import { initUwContractsListPaging, UW_CONTRACTS_SEARCH_COMPONENT_ID } from 'src/app/store/uw-contracts-list/uw-contracts-list.state';
import { clearUwContractsSearch, searchUwContracts, clearReportPrint } from 'src/app/store/uw-contracts-list/uw-contracts-list.actions';
import { INSURANCE_PLAN, REPORT_CHOICES, REPORT_SORT_BY, REPORT_TYPE, ResourcesRoutes, SORT_BY_CHOICES } from "src/app/utils/constants";
import { goToLinkGlobal, userCanAccessDop, userCanAccessInventory, userCanAccessVerifiedYield } from "src/app/utils";
import { GetDopReport } from "src/app/store/dop/dop.actions";
import { GetInventoryReport } from "src/app/store/inventory/inventory.actions";
import { ActivatedRoute, Router } from "@angular/router";
import { DomSanitizer, Title } from "@angular/platform-browser";
import { Store } from "@ngrx/store";
import { RootState } from "src/app/store";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { ApplicationStateService } from "src/app/services/application-state.service";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { AppConfigService, TokenService } from "@wf1/core-ui";
import { ConnectionService } from "ngx-connection-service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Overlay } from "@angular/cdk/overlay";
import { HttpClient } from "@angular/common/http";
import { DecimalPipe } from "@angular/common";



@Component({
  selector: "cirras-uw-contracts-list",
  templateUrl: "./uw-contracts-list.component.html",
  styleUrls: ["../common/base/base.component.scss",
  "../common/base-collection/collection.component.desktop.scss",
      "./uw-contracts-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UwContractsListComponent extends CollectionComponent implements OnChanges, AfterViewInit {

  @Input() cropCommodityList: CropCommodityList;

  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    protected store: Store<RootState>,
    protected fb: FormBuilder,
    protected dialog: MatDialog,
    protected applicationStateService: ApplicationStateService,
    public securityUtilService: SecurityUtilService,                
    protected tokenService: TokenService,
    protected connectionService: ConnectionService,
    protected snackbarService: MatSnackBar,
    protected overlay: Overlay,
    protected cdr: ChangeDetectorRef,
    protected appConfigService: AppConfigService,
    protected http: HttpClient,
    protected titleService: Title,
    protected decimalPipe: DecimalPipe) {
    super(router, route, sanitizer, store, fb, dialog, applicationStateService, securityUtilService, tokenService, connectionService, snackbarService, overlay, cdr, appConfigService, http, titleService, decimalPipe);
  }

  columnsToDisplay = [ "selectGUIDs", "policyNumber", "growerNumber", "growerName", "insurancePlanName", "policyStatus", "actions" ]; 

  cropYearOptions = getCodeOptions("policy_crop_year");
  policyStatusOptions = getCodeOptions("policy_status_code");  
  officeOptions = getCodeOptions("office"); 
  insurancePlans = getCodeOptions("insurance_plan")

  reportTypes = REPORT_TYPE
  reportSortByOptions = REPORT_SORT_BY

  isUserPermittedToAccessInventory : boolean = false;

  // check box stuff
  isMasterCheckboxSelected = false;

  collectionDataEditable;

  isPrintClicked = false;

  countSelectedRecords = 0;

  initModels() {
    this.model = new UwContractsListComponentModel(this.sanitizer);
    this.viewModel = new UwContractsListComponentModel(this.sanitizer);
  }

  loadPage() {

    this.componentId = UW_CONTRACTS_SEARCH_COMPONENT_ID;
    this.updateView();
    this.initSortingAndPaging(initUwContractsListPaging);
    this.config = this.getPagingConfig();
    this.selectedCropYear = new Date().getFullYear().toString()
    this.selectedReportType = REPORT_CHOICES.INVENTORY;
    this.selectedReportSortBy = SORT_BY_CHOICES.POLICY_NUMBER;
    this.doSearch();
  }

  checkUncheckAll() {
    for (let i = 0; i < this.collectionData.length; i++ ) {

      this.collectionDataEditable[i].isSelectedForPrint = this.isMasterCheckboxSelected;

    }
    this.getSelectedPolicyIdsForPrint()
    this.cdr.detectChanges();
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
    
    if(changes.collection){
      
      if(this.collectionData && this.collectionData.length > 0) {
        this.collectionDataEditable = JSON.parse(JSON.stringify(this.collectionData));

        for (let i = 0; i < this.collectionData.length; i++ ) {

          this.collectionDataEditable[i].isSelectedForPrint = false
        }

        this.countSelectedRecords = 0
        this.isMasterCheckboxSelected = false
      }
    }

    if(changes.reportCollection ){ 

      this.reportCollection = changes.reportCollection.currentValue;
      this.reportCollectionData = this.reportCollection ? this.reportCollection.collection : null ;

    }

    // when all starts align for printing
    if (this.reportCollectionData && this.reportCollection.collection && this.reportCollection.collection.length > 0 &&
      this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length) {

          this.cdr.detectChanges()
          window.print()
          this.isPrintClicked = false
    }
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  getViewModel(): UwContractsListComponentModel {
    return <UwContractsListComponentModel>this.viewModel;
  }

  isSearchValid() {
    if ( !this.searchGrower && !this.searchPolicyNumber ) {
      return true;
    }

    if (this.searchGrower && this.searchGrower.length > 2 ) { 
      return true
    }

    if (this.searchPolicyNumber && this.searchPolicyNumber.length > 4  )  { 
      return true
    }

    return false
  }

  doSearch() {

    this.isPrintClicked = false
    this.collectionDataEditable = {}

    // clear the previous search
    this.store.dispatch(clearUwContractsSearch())
    this.store.dispatch(clearReportPrint())

    if (this.isSearchValid()) {

      this.store.dispatch(
        searchUwContracts(
                this.componentId,
                {
                  pageNumber: this.config.currentPage,
                  pageRowCount: this.config.itemsPerPage,
                  sortColumn: this.currentSort,
                  sortDirection: this.currentSortDirection,
                  query: this.searchText
                },
                (this.searchGrower ? this.searchGrower.trim() : ""),
                (this.searchPolicyNumber ? this.searchPolicyNumber.trim() : ""),
                this.selectedInsurancePlan,
                this.selectedCropYear,
                this.selectedPolicyStatus,
                this.selectedOffice
                ))
    }    
  }

  onChangeFilters() {
    super.onChangeFilters();
    this.doSearch();
  }

  clearSearchAndFilters() {
    this.searchText = null;
    this.searchGrower = undefined;
    this.searchPolicyNumber = undefined;
    // this.selectedCropYear = undefined;
    this.selectedCropYear = new Date().getFullYear().toString()
    this.selectedReportType = REPORT_CHOICES.INVENTORY;
    this.selectedReportSortBy = SORT_BY_CHOICES.POLICY_NUMBER;
    this.selectedInsurancePlan = undefined;
    this.selectedPolicyStatus = undefined;
    this.selectedOffice = undefined;

    super.onChangeFilters();
    this.doSearch();
  } 

  defaultItemActionPermitted(item): boolean {
    // return this.securityUtilService.canViewCalculationDetail(item);
    return true;
  }    

  // doDefaultItemAction(item) {
  //   this.selectUwContract(item);
  // }

  // selectUwContract(item: UwContract) {
  //   navigateToUwContract(item, this.router, ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED);
  // }

  goToLink (item, linkType) {

    this.store.dispatch(clearReportPrint())
    
    goToLinkGlobal(item, linkType, this.router)

  }


  userCanAccessInventory(item: UwContract){
      return userCanAccessInventory (this.securityUtilService, item.links)  
  }

  userCanAccessDop(item: UwContract){
      return userCanAccessDop(this.securityUtilService, item.links)  
  }

  userCanAccessVerifiedYield(item: UwContract){
    return userCanAccessVerifiedYield(this.securityUtilService, item.links)  
  }


  isPrintDisabled() {
    return ( this.isPrintClicked || (this.selectedInsurancePlan != INSURANCE_PLAN.GRAIN && this.selectedInsurancePlan != INSURANCE_PLAN.FORAGE))
  }

  getSelectedPolicyIdsForPrint() {

    let policyIdListForPrint = "" 

    this.countSelectedRecords = 0

    for (let i = 0; i < this.collectionData.length; i++ ) {

      if (this.showPrintCheckbox(this.collectionData[i]) && this.collectionDataEditable[i]?.isSelectedForPrint == true ) {

        this.countSelectedRecords ++

        if (policyIdListForPrint.length < 1 ) {

          policyIdListForPrint = policyIdListForPrint + this.collectionData[i].policyId

        } else {

          policyIdListForPrint = policyIdListForPrint + "," + this.collectionData[i].policyId

        }
      }
    }

    return policyIdListForPrint
  }

  clearReportPrint() {
    this.store.dispatch(clearReportPrint())
  }

  showPrintCheckbox(item: UwContract) : boolean {

    if (this.selectedReportType == REPORT_CHOICES.INVENTORY && item.inventoryContractGuid) {

      return userCanAccessInventory (this.securityUtilService, item.links)  

    } else if (this.selectedReportType == REPORT_CHOICES.DOP) {

      return userCanAccessDop(this.securityUtilService, item.links)

    } else {

      return false

    }
  }

  isUnseededVisible(insurancePlanId){
    if (insurancePlanId == INSURANCE_PLAN.GRAIN) {
      return true
    } else {
      return false
    }

  }
}
