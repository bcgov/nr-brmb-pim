import {Action} from "@ngrx/store";
import {
  CLEAR_DOP,
  CLEAR_YIELD_MEAS_UNITS,
  LoadDopYieldContractSuccessAction,
  LoadYieldMeasUnitListSuccessAction,
  LOAD_DOP_SUCCESS,
  LOAD_YIELD_MEAS_UNITS_SUCCESS,
  DELETE_DOP_SUCCESS,
  ADD_FORAGE_DOP_YIELD_FIELD_CUT,
  DEL_FORAGE_DOP_YIELD_FIELD_CUT,
  AddForageDopYieldFieldCutAction,
  DelForageDopYieldFieldCutAction,
} from "./dop.actions";
import { DopState, getDefaultDopState } from "./dop.state";
import { DopYieldContract, DopYieldFieldForage, DopYieldFieldForageCut } from "src/app/conversion/models-yield";
import { AnnualField } from "src/app/conversion/models";

export function dopReducer(state: DopState = getDefaultDopState(), action: Action): DopState {

  switch (action.type) {

    case CLEAR_DOP: {
      // const typedAction = <ClearDopYieldContractAction>action;
      return {...state, dopYieldContract: null }
    }

    case ADD_FORAGE_DOP_YIELD_FIELD_CUT: {
      const typedAction = <AddForageDopYieldFieldCutAction>action;
      const maxNumCuts = getMaxNumCuts(typedAction.payload.dopYieldContract);
      return {...state, dopYieldContract: generateDopYieldContract(typedAction.payload.dopYieldContract, maxNumCuts+1, action)};
    }

    case DEL_FORAGE_DOP_YIELD_FIELD_CUT: {
      const typedAction = <DelForageDopYieldFieldCutAction>action;
      const maxNumCuts = getMaxNumCuts(typedAction.payload.dopYieldContract);
      return {...state, dopYieldContract: generateDopYieldContract(typedAction.payload.dopYieldContract, maxNumCuts, action)};
    }

    case LOAD_DOP_SUCCESS: {
      const typedAction = <LoadDopYieldContractSuccessAction>action;
      const maxNumCuts = Math.max(getMaxNumCuts(typedAction.payload.value), 1); // ensure minimum of one cut
      return {...state, dopYieldContract: generateDopYieldContract(typedAction.payload.value, maxNumCuts)};
    }

    case DELETE_DOP_SUCCESS: {
      return {...state, dopYieldContract: null};
    }
    // yield measurements units
    case LOAD_YIELD_MEAS_UNITS_SUCCESS: {
      const typedAction = <LoadYieldMeasUnitListSuccessAction>action;
      return {...state, yieldMeasUnitList: typedAction.payload.value};
    }

    case CLEAR_YIELD_MEAS_UNITS: {
        return {...state, yieldMeasUnitList: null};
    }

    default: {
      return state;
    }
  }
}

function getMaxNumCuts(dopYieldContract: DopYieldContract): number {
  let maxNumCuts = 0;
  for (const field of dopYieldContract.fields) {
    for (const yieldField of field.dopYieldFieldForageList) {
      maxNumCuts = Math.max(maxNumCuts, yieldField.dopYieldFieldForageCuts.filter(yieldFieldCut => !yieldFieldCut.deletedByUserInd).length);
    }
  }
  return maxNumCuts;
}

function generateDopYieldContract(dopYieldContract: DopYieldContract, maxNumCuts: number, action?: Action) {
  return {
    ...dopYieldContract,
    fields: dopYieldContract.fields.map(field => generateField(field, maxNumCuts, action)),
  };
}

function generateField(annualField: AnnualField, maxNumCuts: number, action?: Action) {
  return {
    ...annualField,
    dopYieldFieldForageList: annualField.dopYieldFieldForageList.map(yieldField => generateYieldField(yieldField, maxNumCuts, action)),
  };
}

function generateYieldField(dopYieldFieldForage: DopYieldFieldForage, maxNumCuts: number, action?: Action) {
  
  // make the number of cuts equal to maxNumCuts
  let dopYieldFieldForageCuts = [...dopYieldFieldForage.dopYieldFieldForageCuts];
  for (let i = dopYieldFieldForageCuts.length; i < maxNumCuts; i++) {
    dopYieldFieldForageCuts.push({
      declaredYieldFieldForageGuid: null,
      inventoryFieldGuid: dopYieldFieldForage.inventoryFieldGuid,
      cutNumber: i + 1,
      totalBalesLoads: null,
      weight: null,
      weightDefaultUnit: null,
      moisturePercent: null,
      deletedByUserInd: false,
    });
  }

  // handle the situation where the user deletes a cut and then adds a new cut
  if (action && action.type === ADD_FORAGE_DOP_YIELD_FIELD_CUT) {
    dopYieldFieldForageCuts = dopYieldFieldForageCuts.map<DopYieldFieldForageCut>(yieldFieldCut => {
      if (yieldFieldCut.cutNumber === maxNumCuts) {
        return {...yieldFieldCut, deletedByUserInd: false};
      } else {
        return yieldFieldCut;
      }
    });
  }

  // remove any cuts greater than maxNumCuts
  if (action && action.type === DEL_FORAGE_DOP_YIELD_FIELD_CUT) {
    dopYieldFieldForageCuts = dopYieldFieldForageCuts.map<DopYieldFieldForageCut>(yieldFieldCut => {
      if (yieldFieldCut.cutNumber === maxNumCuts) {
        return {...yieldFieldCut, totalBalesLoads: null, weight: null, moisturePercent: null, deletedByUserInd: true};
      } else {
        return yieldFieldCut;
      }
    });    
  }

  return {
    ...dopYieldFieldForage,
    dopYieldFieldForageCuts: dopYieldFieldForageCuts,
  };
}