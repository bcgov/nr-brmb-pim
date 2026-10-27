import {RootState} from "../index";
import {UwContractsList} from "../../conversion/models";

export const selectUwContractsList = () => (state: RootState): UwContractsList =>  ((state.uwContractsList) ? state.uwContractsList.uwContractsList : null );

export const selectInventoryContractList = () => (state: RootState): UwContractsList => ((state.inventoryContract) ? state.uwContractsList.inventoryContractList : null);