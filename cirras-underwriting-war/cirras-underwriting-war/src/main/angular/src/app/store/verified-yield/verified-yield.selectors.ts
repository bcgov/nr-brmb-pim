import {RootState} from "../index";
import { VerifiedYieldContract } from "src/app/conversion/models-yield";

export const selectVerifiedYieldContract = () => (state: RootState): VerifiedYieldContract => 
((state.verifiedYield) ? state.verifiedYield.verifiedYieldContract: null );  


