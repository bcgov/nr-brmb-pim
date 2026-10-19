
import {RootState} from "../index";
import {UwContract} from "../../conversion/models";

export const selectGrowerContract = () => (state: RootState): UwContract  => 
((state.growerContract.growerContract ) ? state.growerContract.growerContract : null );  