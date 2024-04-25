import { GradeModifierList } from "src/app/conversion/models-yield";
import { setHttpHeaders } from "src/app/utils";

export interface GradeModifierOptionsType {
    cropCommodityId: string;
    gradeModifierTypeCode: string;
    description: string;
  }



export function roundUpDecimals(numberToRound, precision) {

  if (!numberToRound) {
    return ""
  }

  if (isNaN(parseFloat(numberToRound))) {
    return ""
  } else {

    if (parseFloat(numberToRound) % 1 == 0 ) {
      // return integer if it's an integer, no zeros after the decimal point
      return Math.abs(parseInt(numberToRound))
    }
    
    return Math.abs(parseFloat(numberToRound)).toFixed(precision)
  }
}

