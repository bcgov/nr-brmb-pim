import { CropCommodityList} from "../../conversion/models";
  
  export const CROP_COMMODITY_COMPONENT_ID = "cropCommodityComponent";

  const EMPTY_CROP_COMMODITY_LIST: CropCommodityList = {
    collection: []
  };

  export interface CropCommodityState {
    cropCommodityList?: CropCommodityList;
    underSeededCropCommodityList?: CropCommodityList;
  }
  
  export function getDefaultCropCommodityState(): CropCommodityState {
    return {
      cropCommodityList: EMPTY_CROP_COMMODITY_LIST,
      underSeededCropCommodityList: EMPTY_CROP_COMMODITY_LIST,
    };

  }
