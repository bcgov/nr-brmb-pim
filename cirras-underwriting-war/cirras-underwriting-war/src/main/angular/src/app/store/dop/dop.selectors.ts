import {RootState} from "../index";
import { DopYieldContract, YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";

export const selectDopYieldContract = () => (state: RootState): DopYieldContract => 
((state.dop) ? state.dop.dopYieldContract : null );  

export const selectYieldMeasUnit = () => (state: RootState): YieldMeasUnitTypeCodeList  => 
((state.dop) ? state.dop.yieldMeasUnitList : null );  
