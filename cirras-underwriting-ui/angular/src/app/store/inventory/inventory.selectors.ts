import {RootState} from "../index";
import {InventoryContract} from "../../conversion/models";

export const selectInventoryContract = () => (state: RootState): InventoryContract => 
((state.inventoryContract) ? state.inventoryContract.inventoryContract : null );  

