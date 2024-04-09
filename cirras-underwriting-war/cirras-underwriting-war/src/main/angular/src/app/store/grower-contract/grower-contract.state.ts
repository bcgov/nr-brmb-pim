import { UwContract} from "../../conversion/models";
  
  export const GROWER_CONTRACT_COMPONENT_ID = "growerContractInfoComponent";

  export interface GrowerContractState {
    growerContract?: UwContract;
  }
  
  export function getDefaultGrowerContractState(): GrowerContractState {
    return {
        growerContract: null
    };

  }
