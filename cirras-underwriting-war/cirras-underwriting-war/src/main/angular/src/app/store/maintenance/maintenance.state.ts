import { CropVarietyInsurabilityList, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYearList, YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";
 
  export const MAINTENANCE_COMPONENT_ID = "maintenanceComponents";

  export interface MaintenanceState {
    underwritingYears?: UnderwritingYearList;
    seedingDeadlineList?: SeedingDeadlineList;
    gradeModifierTypesList?: GradeModifierTypeList;
    gradeModifierList?: GradeModifierList;
    cropVarietyInsurabilityList? : CropVarietyInsurabilityList,
    yieldMeasUnitConversionList? : YieldMeasUnitConversionList,
  }
  
  export function getDefaultMaintenanceState(): MaintenanceState {
    return {
        underwritingYears: null,
        seedingDeadlineList: null,
        gradeModifierTypesList: null,
        gradeModifierList: null,
        cropVarietyInsurabilityList: null,
        yieldMeasUnitConversionList: null,
    };
  }
