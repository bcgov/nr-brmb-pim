import { InventoryContract} from "../../conversion/models";
  
  export const INVENTORY_COMPONENT_ID = "inventoryContract";

  export interface InventoryState {
    inventoryContract?: InventoryContract;
  }
  
  export function getDefaultInventoryState(): InventoryState {
    return {
      inventoryContract: null,
    };

  }
