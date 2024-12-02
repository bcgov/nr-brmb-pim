import {ErrorState, LoadState} from "./application.state";
import {RootState} from "../index";
import {SearchState} from "@wf1/wfcc-core-lib";

export const selectSearchState = (componentId) => (state: RootState): SearchState => ((state[componentId]) ? state[componentId] : undefined);

export const selectFormStateUnsaved = (componentId: string) => (state: RootState): boolean => ((state.application.formStates[componentId]) ? state.application.formStates[componentId].isUnsaved : false);

export const selectFormStatesUnsaved = (componentIds: string[]) => (state: RootState): boolean => {
  let ret = false;
  if (componentIds && componentIds.length) {
    componentIds.forEach(componentId => {
      let formUnsaved = state.application.formStates[componentId] ? state.application.formStates[componentId].isUnsaved : false;
      ret = ret || formUnsaved;
    });
  }
  return ret;
};

// export const selectUwContractsListState = (componentId) => (state: RootState): SearchState => ((state[componentId]) ? state[componentId] : undefined);
export const selectUwContractsListLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.uwContractsList) ? state.application.loadStates.uwContractsList : undefined);
export const selectUwContractsListErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.uwContractsList) ? state.application.errorStates.uwContractsList : undefined);

export const selectInventoryContractLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.inventoryContract) ? state.application.loadStates.inventoryContract : undefined);
export const selectInventoryContractErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.inventoryContract) ? state.application.errorStates.inventoryContract : undefined);

export const selectDopYieldContractLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.dopYieldContract) ? state.application.loadStates.dopYieldContract : undefined);
export const selectDopYieldContractErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.dopYieldContract) ? state.application.errorStates.dopYieldContract : undefined);

export const selectVerifiedYieldContractLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.verifiedYieldContract) ? state.application.loadStates.verifiedYieldContract : undefined);
export const selectVerifiedYieldContractErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.verifiedYieldContract) ? state.application.errorStates.verifiedYieldContract : undefined);

export const selectLegalLandListLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.legalLandList) ? state.application.loadStates.legalLandList : undefined);
export const selectLegalLandListErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.legalLandList) ? state.application.errorStates.legalLandList : undefined);

export const selectMaintainUwYearsLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.maintenance) ? state.application.loadStates.maintenance : undefined);
export const selectMaintainUwYearsErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.maintenance) ? state.application.errorStates.maintenance : undefined);

