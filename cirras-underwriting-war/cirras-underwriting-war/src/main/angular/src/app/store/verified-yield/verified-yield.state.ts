import { VerifiedYieldContract } from "src/app/conversion/models-yield";
 
  export const VERIFIED_COMPONENT_ID = "verifiedYieldContract";

  export interface VerifiedYieldState {
    verifiedYieldContract?: VerifiedYieldContract;
  }
  
  export function getDefaultVerifiedYieldState(): VerifiedYieldState {
    return {
      verifiedYieldContract: null,
    };
  }
