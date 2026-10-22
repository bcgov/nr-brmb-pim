import {RootState} from "../index";
import {CropCommodityList} from "../../conversion/models";

export const selectCropCommodityList = () => (state: RootState): CropCommodityList => 
((state.cropCommodityList) ? state.cropCommodityList.cropCommodityList : null );  


export const selectUnderSeededCropCommodityList = () => (state: RootState): CropCommodityList => 
((state.cropCommodityList) ? state.cropCommodityList.underSeededCropCommodityList : null );  

