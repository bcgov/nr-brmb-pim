import {Action} from "@ngrx/store";
import {CropCommodityList} from "../../conversion/models";
import {ErrorState} from "../application/application.state";


export const LOAD_CROP_COMMODITY = "LOAD_CROP_COMMODITY";
export const LOAD_CROP_COMMODITY_SUCCESS = "LOAD_CROP_COMMODITY_SUCCESS";
export const LOAD_CROP_COMMODITY_ERROR = "LOAD_CROP_COMMODITY_ERROR";
export const CLEAR_CROP_COMMODITY = "CLEAR_CROP_COMMODITY";

export const LOAD_UNDERSEEDED_CROP_COMMODITY = "LOAD_UNDERSEEDED_CROP_COMMODITY";
export const LOAD_UNDERSEEDED_CROP_COMMODITY_SUCCESS = "LOAD_UNDERSEEDED_CROP_COMMODITY_SUCCESS";
export const LOAD_UNDERSEEDED_CROP_COMMODITY_ERROR = "LOAD_UNDERSEEDED_CROP_COMMODITY_ERROR";
export const CLEAR_UNDERSEEDED_CROP_COMMODITY = "CLEAR_UNDERSEEDED_CROP_COMMODITY";

// Crop Commodity
export interface LoadCropCommodityListAction extends Action {
  componentId: string;
  payload: {
    insurancePlanId: string,
    cropYear: string,
    commodityType: string,
    loadChildren: string
  };
}

export interface LoadCropCommodityListSuccessAction extends Action {
  componentId: string;
  payload: {
    value: CropCommodityList;
  };
}

export interface LoadCropCommodityListErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function LoadCropCommodityList(componentId: string, 
                              insurancePlanId: string,
                              cropYear: string,
                              commodityType: string,
                              loadChildren: string
                             ): LoadCropCommodityListAction {

  return {
    type: LOAD_CROP_COMMODITY,
    componentId: componentId,
    payload: {
      insurancePlanId,
      cropYear,
      commodityType,
      loadChildren
    }
  };
}

export function LoadCropCommodityListSuccess(componentId: string, value: CropCommodityList): LoadCropCommodityListSuccessAction {
  return {
    type: LOAD_CROP_COMMODITY_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function LoadCropCommodityListError(componentId: string, error: ErrorState): LoadCropCommodityListErrorAction {
  return {
    type: LOAD_CROP_COMMODITY_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

// Clear Crop Commodity
export interface ClearCropCommodityAction extends Action {
  payload: {
    value: CropCommodityList;
  };

}

export function ClearCropCommodity(): ClearCropCommodityAction {
  return {
    type: CLEAR_CROP_COMMODITY,
    payload: {
      value: <CropCommodityList>[]
    }
  };
}

//UNDERSEEDED CROP COMMODITY 
export function LoadUnderSeededCropCommodityList(componentId: string, 
    insurancePlanId: string,
    cropYear: string,
    commodityType: string,
    loadChildren: string
  ): LoadCropCommodityListAction {

  return {
    type: LOAD_UNDERSEEDED_CROP_COMMODITY,
    componentId: componentId,
    payload: {
      insurancePlanId,
      cropYear,
      commodityType,
      loadChildren
    }
  };
}

export function LoadUnderSeededCropCommodityListSuccess(componentId: string, value: CropCommodityList): LoadCropCommodityListSuccessAction {
  return {
    type: LOAD_UNDERSEEDED_CROP_COMMODITY_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function LoadUnderSeededCropCommodityListError(componentId: string, error: ErrorState): LoadCropCommodityListErrorAction {
  return {
    type: LOAD_UNDERSEEDED_CROP_COMMODITY_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}

// Clear UNDERSEEDED Crop Commodity
export function ClearUnderSeededCropCommodity(): ClearCropCommodityAction {
  return {
    type: CLEAR_UNDERSEEDED_CROP_COMMODITY,
    payload: {
      value: <CropCommodityList>[]
    }
  };
}

