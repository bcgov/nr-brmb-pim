import {Action} from "@ngrx/store";
import { InventoryContract } from "src/app/conversion/models";
import { ClearCropCommodityAction, CLEAR_CROP_COMMODITY, CLEAR_UNDERSEEDED_CROP_COMMODITY, LoadCropCommodityListSuccessAction, LOAD_CROP_COMMODITY_SUCCESS, LOAD_UNDERSEEDED_CROP_COMMODITY_SUCCESS} from "./crop-commodity.actions";
import { getDefaultCropCommodityState, CropCommodityState } from "./crop-commodity.state";

export function cropCommodityReducer(state: CropCommodityState = getDefaultCropCommodityState(), action: Action): CropCommodityState {

  switch (action.type) {

    case LOAD_CROP_COMMODITY_SUCCESS: {
      const typedAction = <LoadCropCommodityListSuccessAction>action;
      return {...state, cropCommodityList: typedAction.payload.value};
    }

    case CLEAR_CROP_COMMODITY: {
      const typedAction = <ClearCropCommodityAction>action;
      return {...state, cropCommodityList: null }
    }

    case LOAD_UNDERSEEDED_CROP_COMMODITY_SUCCESS: {
      const typedAction = <LoadCropCommodityListSuccessAction>action;
      return {...state, underSeededCropCommodityList: typedAction.payload.value};
    }

    case CLEAR_UNDERSEEDED_CROP_COMMODITY: {
      const typedAction = <ClearCropCommodityAction>action;
      return {...state, underSeededCropCommodityList: null }
    }

    default: {
      return state;
    }
  }
}