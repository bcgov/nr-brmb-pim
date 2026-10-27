import {RootState} from "../index";
import {LegalLand, LegalLandList, RiskAreaList} from "../../conversion/models";
import { ManageLegalLandState } from "./land-management.state";

export const selectLandList = () => (state: RootState): LegalLandList =>  ((state.landSearch) ? state.landSearch.legalLandList : null );

export const selectLegalLand = () => (state: RootState): LegalLand => ((state.manageLegalLand) ? state.manageLegalLand.legalLand : null ); 

export const selectRiskAreaList = () => (state: RootState): RiskAreaList => ((state.manageLegalLand) ? state.manageLegalLand.riskAreaList : null ); 