import { CropCommodityList } from "src/app/conversion/models";
import {RootState} from "../index";
import { VerifiedYieldContract } from "src/app/conversion/models-yield";

export const selectVerifiedYieldContract = () => (state: RootState): VerifiedYieldContract => 
((state.verifiedYield) ? state.verifiedYield.verifiedYieldContract: null );  

export const selectCropCommodityList = () => (state: RootState): CropCommodityList => 
    ((state.cropCommodityList) ? state.cropCommodityList.cropCommodityList : null );  