import {AfterViewInit, OnChanges, SimpleChanges} from "@angular/core";
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CollectionComponent } from "../../common/base-collection/collection.component";
import { ClearLegalLand, clearLandSearch, searchLand } from "src/app/store/land-management/land-management.actions";
import { LAND_SEARCH_COMPONENT_ID, initLandSearchPaging } from "src/app/store/land-management/land-management.state";
import { LandListComponentModel } from "./land-list.component.model";
import { ResourcesRoutes } from "src/app/utils/constants";
import { BaseWrapperComponent } from "../../common/base-wrapper/base-wrapper.component";
import { NgIf, NgFor } from "@angular/common";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { MatButton, MatIconButton } from "@angular/material/button";
import { MatIcon } from "@angular/material/icon";
import { MatLabel, MatSuffix } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatTooltip } from "@angular/material/tooltip";
import { SingleSelectDirective } from "../../../directives/singleselect.directive";
import { MatTable, MatColumnDef, MatHeaderCellDef, MatHeaderCell, MatCellDef, MatCell, MatHeaderRowDef, MatHeaderRow, MatRowDef, MatRow } from "@angular/material/table";
import { MatSort, MatSortHeader } from "@angular/material/sort";
import { NgxPaginationModule } from "ngx-pagination";

@Component({
    selector: "cirras-land-list",
    templateUrl: "./land-list.component.html",
    styleUrls: ["../../common/base/base.component.scss",
        "../../common/base-collection/collection.component.desktop.scss",
        "./land-list.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [BaseWrapperComponent, NgIf, MatProgressSpinner, MatButton, MatIcon, MatLabel, FormsModule, MatIconButton, MatSuffix, MatTooltip, SingleSelectDirective, MatTable, MatSort, MatColumnDef, MatHeaderCellDef, MatHeaderCell, MatSortHeader, MatCellDef, MatCell, MatHeaderRowDef, MatHeaderRow, MatRowDef, MatRow, NgxPaginationModule, NgFor]
})

export class LandListComponent extends CollectionComponent implements OnChanges, AfterViewInit {

  columnsToDisplay = [ "otherDescription", "primaryPropertyIdentifier", "legalDescription", "totalAcres", "isActive", "actions" ]; 

  loadPage() {
    this.componentId = LAND_SEARCH_COMPONENT_ID;
    this.updateView();
    this.initSortingAndPaging(initLandSearchPaging);
    this.config = this.getPagingConfig();
    this.doSearch();
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  initModels() {
    this.model = new LandListComponentModel(this.sanitizer);
    this.viewModel = new LandListComponentModel(this.sanitizer);
  }

  getViewModel(): LandListComponentModel {
    return <LandListComponentModel>this.viewModel;
  }

  ngOnChanges(changes: SimpleChanges): void {
    super.ngOnChanges(changes);
  }

  onChangeFilters() {
    super.onChangeFilters();
    this.doSearch();
  }

  doSearch() {

    // clear the previous search
    this.store.dispatch(clearLandSearch())

    if (this.isSearchValid()) {

      this.store.dispatch(
        searchLand(
                this.componentId,
                {
                  pageNumber: this.config.currentPage,
                  pageRowCount: this.config.itemsPerPage,
                  sortColumn: this.currentSort,
                  sortDirection: this.currentSortDirection,
                  query: this.searchText
                },
                (this.searchLandIdentifier ? this.searchLandIdentifier.trim() : ""),
                (this.searchLegalLocation ? this.searchLegalLocation.trim() : ""),
                (this.searchGrower ? this.searchGrower.trim() : ""),
                this.selectedDataset,
                'true',
                'false'
                ))
              }    
  }

  isSearchValid() {
    if ( !this.searchGrower && !this.searchLandIdentifier && !this.searchLegalLocation ) {
      return true;
    }

    if (this.searchGrower && this.searchGrower.length > 2 ) { 
      return true
    }

    if (this.searchLandIdentifier && this.searchLandIdentifier.length > 2  )  { 
      return true
    }

    if (this.searchLegalLocation && this.searchLegalLocation.length > 2  )  { 
      return true
    }

    return false
  }
  
  clearSearchAndFilters() {

    this.searchLandIdentifier = undefined;
    this.searchLegalLocation = undefined;
    this.searchGrower = undefined;
    this.selectedDataset = undefined;

    super.onChangeFilters();
    this.doSearch();
  } 

  onAddLegalLand() {

    this.store.dispatch( ClearLegalLand() )

    this.router.navigate([ResourcesRoutes.LAND_MANAGE, ""]) 
  } 
  
  isActive(activeFromYear, activeToYear){

    const currentYear = (new Date()).getFullYear()

    if (activeToYear) {

      if (activeFromYear <= currentYear && currentYear <= activeToYear) {
        return true
      } else {
        return false
      }

    } else { // activeToYear is null

      if (activeFromYear <= currentYear) {
        return true
      } else {
        return false
      }
    }
  }

  goToLegalLand(legalLandId) {
    this.router.navigate([ResourcesRoutes.LAND_MANAGE, legalLandId.toString()]) 
  }

}
