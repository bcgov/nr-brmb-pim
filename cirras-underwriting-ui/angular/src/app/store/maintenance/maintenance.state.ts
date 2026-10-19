import { CropVarietyInsurabilityList, GradeModifierList, GradeModifierTypeList, SeedingDeadlineList, UnderwritingYearList, UserSetting, YieldMeasUnitConversionList } from "src/app/conversion/models-maintenance";
 
  export const MAINTENANCE_COMPONENT_ID = "maintenanceComponents";
  export const USER_SETTINGS_COMPONENT_ID = "userSettingsComponent";

  export interface MaintenanceState {
    underwritingYears?: UnderwritingYearList;
    seedingDeadlineList?: SeedingDeadlineList;
    gradeModifierTypesList?: GradeModifierTypeList;
    gradeModifierList?: GradeModifierList;
    cropVarietyInsurabilityList? : CropVarietyInsurabilityList,
    yieldMeasUnitConversionList? : YieldMeasUnitConversionList,
    userSetting?: UserSetting,
  }
  
  export function getDefaultMaintenanceState(): MaintenanceState {
    return {
        underwritingYears: null,
        seedingDeadlineList: null,
        gradeModifierTypesList: null,
        gradeModifierList: null,
        cropVarietyInsurabilityList: null,
        yieldMeasUnitConversionList: null,
        userSetting: null,
    };
  }
