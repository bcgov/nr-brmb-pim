import { DopYieldContract, YieldMeasUnitTypeCodeList } from "src/app/conversion/models-yield";
 
  export const DOP_COMPONENT_ID = "dopYieldContract";

  export interface DopState {
    dopYieldContract?: DopYieldContract;
    yieldMeasUnitList?: YieldMeasUnitTypeCodeList;
  }
  
  export function getDefaultDopState(): DopState {
    return {
      dopYieldContract: null,
      yieldMeasUnitList: null,
    };
  }
