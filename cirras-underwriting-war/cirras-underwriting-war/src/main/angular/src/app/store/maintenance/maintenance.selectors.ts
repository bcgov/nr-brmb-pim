import { CropVarietyInsurabilityList, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYearList, YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";
import {RootState} from "../index";

export const selectUnderwritingYears = () => (state: RootState): UnderwritingYearList => 
((state.maintenance) ? state.maintenance.underwritingYears : null );  

export const selectseedingDeadlineList = () => (state: RootState): SeedingDeadlineList => 
((state.maintenance) ? state.maintenance.seedingDeadlineList : null );  

export const selectGradeModifierTypeList = () => (state: RootState): GradeModifierTypeList => {
    return ((state.maintenance) ? state.maintenance.gradeModifierTypesList : null );  
}

export const selectGradeModifierList = () => (state: RootState): GradeModifierList => 
((state.maintenance) ? state.maintenance.gradeModifierList : null );  

export const selectCropVarietyInsurabilityList = () => (state: RootState): CropVarietyInsurabilityList => 
((state.maintenance) ? state.maintenance.cropVarietyInsurabilityList : null );  

export const selectYieldMeasUnitConversionList = () => (state: RootState): YieldMeasUnitConversionList => 
((state.maintenance) ? state.maintenance.yieldMeasUnitConversionList : null );  