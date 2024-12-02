import {SearchState} from "@wf1/wfcc-core-lib";
import {LegalLand, LegalLandList, RiskAreaList} from "../../conversion/models";
import {getDefaultPagingInfoRequest} from "../application/application.state";

export const LAND_SEARCH_COMPONENT_ID = "searchLand";
export const LAND_LIST_COMPONENT_ID = "landList";

const EMPTY_LEGALLANDLIST: LegalLandList = {
  pageNumber: null,
  pageRowCount: null,
  totalPageCount: null,
  totalRowCount: null,
  collection: []
};

export const initialLandSearchState: SearchState = {
  query: null,
  sortParam: "otherDescription",
  sortDirection: "ASC",
  sortModalVisible: false,
  filters: {},
  hiddenFilters: {},
  componentId: LAND_SEARCH_COMPONENT_ID
};

export const initLandSearchPaging = getDefaultPagingInfoRequest(1, 20, "otherDescription", "ASC", undefined);

export interface LandSearchState {
  legalLandList?: LegalLandList;
}

export function getDefaultLandSearchState(): LandSearchState {
  return {
    legalLandList: EMPTY_LEGALLANDLIST
  };
}

export const MANAGE_LEGAL_LAND_COMPONENT_ID = "manageLegalLand";

export interface ManageLegalLandState {
  legalLand?: LegalLand,
  riskAreaList?: RiskAreaList
}
  
export function getDefaultLegalLandState(): ManageLegalLandState {
  return {
    legalLand: null,
    riskAreaList: null
  };

}
