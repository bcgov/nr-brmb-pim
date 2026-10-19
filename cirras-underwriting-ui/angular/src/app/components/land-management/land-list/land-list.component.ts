import {AfterViewInit, OnChanges, SimpleChanges} from "@angular/core";
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CollectionComponent } from "../../common/base-collection/collection.component";
import { ClearLegalLand, clearLandSearch, searchLand } from "src/app/store/land-management/land-management.actions";
import { LAND_SEARCH_COMPONENT_ID, initLandSearchPaging } from "src/app/store/land-management/land-management.state";
import { LandListComponentModel } from "./land-list.component.model";
import { ResourcesRoutes } from "src/app/utils/constants";

@Component({
    selector: "cirras-land-list",
    templateUrl: "./land-list.component.html",
    styleUrls: ["../../common/base/base.component.scss",
        "../../common/base-collection/collection.component.desktop.scss",
        "./land-list.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
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
