import {APP_BOOTSTRAP_LISTENER, Inject, InjectionToken, Renderer2, Type} from "@angular/core";
import {EffectSources} from "@ngrx/effects";
import {PagingInfoRequest} from "../store/application/application.state";
import {SortDirection} from "@wf1/wfcc-core-lib";
import moment, { Moment } from "moment";
import {Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { CropCommodityList, RelLink, UwContract } from "../conversion/models";
import { BASE_COMMODITIES, CODE_TABLE_CACHE, CROP_COMMODITY_UNSPECIFIED, INSURANCE_NAME, INSURANCE_PLAN, ResourcesRoutes, REST_VERSION } from "./constants";
import { UUID } from "angular2-uuid";
import { HttpHeaders } from '@angular/common/http';
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { SCOPES_UI } from "./scopes";

export function getCodeTableCache() {
    return CODE_TABLE_CACHE;
}

export const CONSTANTS = {
    NO_RECORDS_MESSAGE: "No records to display.",
    PHONE_MASK: '000-000-0000',
    DATE_MASK: [ /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, '-', /\d/, /\d/ ],
    DATE_RANGE_MASK: [ /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, '-', /\d/, /\d/ ].concat([' - ']).concat([ /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, '-', /\d/, /\d/ ]),
    DATE_TIME_MASK: [ /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, '-', /\d/, /\d/, ' ', /\d/, /\d/, ':', /\d/, /\d/  ],
    EMAIL_PATTERN: "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
};

export const DATE_FORMATS = {
    fullPickerInput: "Y-MM-DD HH:mm",
    datePickerInput: "Y-MM-DD",
    timePickerInput: "HH:mm",
    monthYearLabel: "Y-MM",
    dateA11yLabel: "Y-MMM-DD",
    monthYearA11yLabel: "YYYY-MMM",
    simplifiedDate: "MMM DD",
    API_DATE: "Y-MM-DD",
    API_TIMESTAMP: 'Y-MM-DD HH:mm:ss',
    API_TIMESTAMP_WITH_SEP: 'Y-MM-DDTHH:mm:ss'
};

export const BOOTSTRAP_EFFECTS = new InjectionToken("Bootstrap Effects");

export function bootstrapEffects(effects: Type<any>[], sources: EffectSources) {
    return () => {
        effects.forEach(effect => sources.addEffects(effect));
    };
}

export function createInstances(...instances: any[]) {
    return instances;
}

export function provideBootstrapEffects(effects: Type<any>[]): any {
    return [
        effects,
        {
            provide: BOOTSTRAP_EFFECTS, deps: effects, useFactory: createInstances
        },
        {
            provide: APP_BOOTSTRAP_LISTENER,
            multi: true,
            useFactory: bootstrapEffects,
            deps: [[new Inject(BOOTSTRAP_EFFECTS)], EffectSources]
        }
    ];
}

export function getPageInfoRequestForSearchState(searchState: any): PagingInfoRequest {
    return {
        pageRowCount: searchState.pageSize,
        pageNumber: searchState.pageIndex,
        sortColumn: searchState.sortParam,
        sortDirection: searchState.sortDirection,
        query: searchState.query
    };
}

export const formatSort = (param: string, direction: SortDirection) => param && direction ? `${param} ${direction}` : undefined;

export function addRemoveCdkOverlayClass(isMobileResolution) {
    let cdkOverlayContainer = <HTMLElement>document.getElementsByClassName("cdk-overlay-container")[0];
    if (cdkOverlayContainer) {
        cdkOverlayContainer.classList.remove("desktop");
        cdkOverlayContainer.classList.remove("mobile");

        cdkOverlayContainer.classList.add(isMobileResolution ? "mobile" : "desktop");
    }
}

export interface DayOfPeriod {
    dayName: string;
    date: number;
    moment: Moment;
}

export function getDaysArray (year, month): DayOfPeriod[] {
    let monthIndex = month;
    let names = [ 'SU', 'MO', 'TU', 'WE', 'TH', 'FR', 'SA' ];
    let date = new Date(year, monthIndex, 1);
    let result = [];
    while (date.getMonth() == monthIndex) {
        result.push({
            date: date.getDate(),
            dayName: names[date.getDay()],
            moment: moment(date)
        });
        date.setDate(date.getDate() + 1);
    }
    return result;
}

export function getWeekRange(date: Moment, numWeeks: number): Moment[] {
    let weekRange: Moment[] = [];
    let includingDate = date.startOf('day');
    let startOfWeek: Moment = includingDate.clone().isoWeekday(4); // week must start on Thurs for Roster business logic
    if (includingDate.isBefore(startOfWeek)) { // if selected date was Sun-Wed, we need to shift back one week due to the Thursday week start logic
        startOfWeek = startOfWeek.subtract(1, 'week');
    }
    for (let day = 0; day < 7 * numWeeks; day++) {
        let dateToAdd = startOfWeek.clone().add(day, "day");
        weekRange.push(dateToAdd);
    }

    return weekRange;
}

export interface WFSnackbarData {
    message: string;
    type: string;
}

// export function convertToAPITime(dateTimeLocalMoment: Moment): string {
//     return momenttz(dateTimeLocalMoment).local().tz("America/Vancouver").format(DATE_FORMATS.API_TIMESTAMP);
// }

// export function convertToAPITimeWithTZFormat(dateTimeLocalMoment: Moment): string {
//     return momenttz(dateTimeLocalMoment).local().tz("America/Vancouver").format(DATE_FORMATS.API_TIMESTAMP_WITH_SEP);
// }

export const WF_SNACKBAR_TYPES = {SUCCESS: "success", ERROR: "error", WARNING: "warning", UPDATE: "update"};

export  const hasValues = (obj) => Object.values(obj).some(v => v !== null && typeof v !== "undefined");

export function compare(a: number | string, b: number | string, isAsc: boolean): number {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

export function compareMoment(a: Moment, b: Moment, isAsc: boolean): number {
    return (a.isBefore(b) ? -1 : 1) * (isAsc ? 1 : -1);
}

export function makeFormFieldReadonly(renderer: Renderer2, formFieldEl: HTMLElement) {
    renderer.addClass(formFieldEl, "readonly");
    let editableElements;
    //make input element readonly
    let inputHtmlElement: HTMLInputElement;
    editableElements = formFieldEl.getElementsByTagName('input');
    if (editableElements && editableElements.length) {
        inputHtmlElement = editableElements[0];
        setTimeout(() => {
            renderer.setAttribute(inputHtmlElement, "readonly", "readonly");
        });
    }

    //make textarea element readonly
    let textareaHtmlElement: HTMLTextAreaElement;
    editableElements = formFieldEl.getElementsByTagName('textarea');
    if (editableElements && editableElements.length) {
        textareaHtmlElement = editableElements[0];
        setTimeout(() => {
            renderer.setAttribute(textareaHtmlElement, "readonly", "readonly");
        });
    }

    //make select element readonly
    let selectHtmlElement: HTMLSelectElement;
    editableElements = formFieldEl.getElementsByTagName('select');
    if (editableElements && editableElements.length) {
        selectHtmlElement = editableElements[0];
        setTimeout(() => {
            renderer.addClass(selectHtmlElement, "select-read-only-cursor");
            renderer.setAttribute(selectHtmlElement, "readonly", "readonly");
            for (let i = 0; i < selectHtmlElement.options.length; i++) {
                selectHtmlElement.options[i].disabled = true;
            }
        });
    }

    //make radio element readonly
    // let radioHtmlElement: MatRadioButton;
    // editableElements = formFieldEl.getElementsByTagName('mat-radio-button');
    // if (editableElements && editableElements.length) {
    //     radioHtmlElement = editableElements[0];
    //     renderer.setAttribute(radioHtmlElement, "disabled", "true");
    // }
    // -- dont need to do this here as radio group will be disabled separately via form config

    //make datepicker buttons disabled
    let buttonHtmlElement: HTMLButtonElement;
    editableElements = formFieldEl.getElementsByTagName('button');
    if (editableElements && editableElements.length) {
        buttonHtmlElement = editableElements[0];
        renderer.setAttribute(buttonHtmlElement, "disabled", "true");
    }
}

export function getElementInnerText(el: HTMLElement): string {
    return el.innerText;
}

export function isElementTruncated(el: HTMLElement): boolean {
    return el.offsetWidth < el.scrollWidth;
}

export function arrayEquals(a, b) {
    return Array.isArray(a) &&
        Array.isArray(b) &&
        a.length === b.length &&
        a.every((val, index) => val === b[index]);
}

export function requiredIfValidator(predicate) {
    return (formControl => {
        if (!formControl.parent) {
            return null;
        }
        if (predicate()) {
            return Validators.required(formControl);
        }
        return null;
    });
}

export function dollars( val ) {

    if ( val == null ) return null

    if (isNaN(val)) {
        // if the val is not a number 
        return null
    }

    var rounded = parseFloat( val )

    return rounded.toLocaleString('en-CA', {
        style: 'currency',
        currency: 'CAD',
      }) 
}

export function roundedDollars( val ) {

    if ( val == null ) return null

    if (isNaN(val)) {
        // if the val is not a number 
        return null
    }

    var rounded = Math.round( parseFloat( val ) )

    return rounded.toLocaleString('en-CA', {
        style: 'currency',
        currency: 'CAD',
      }) 
  }

export function dollarsToNumber( val ) {

    if ( val == null ) return 0;        

    let strVal = val.toLocaleString();

    if (strVal.indexOf("$") > -1) {
        strVal = strVal.substring(1);
    }

    if (strVal.indexOf(",") > -1) {             
        strVal = strVal.replace(",", "");
    } 
    
    if (isNaN(strVal)) return 0;
    var rounded = parseFloat( strVal );     
    return rounded;
}


export function makeTitleCase(str) {
    if (str) {
        return str
        .split(' ')
        .map((word) => word[0].toUpperCase() + word.slice(1).toLowerCase())
        .join(' ');
    } else {
        return '';
    }
    
}

export function removeDuplicateWords(str) {

    if (str) {
        return str.split(' ').filter(function(item, i, allItems){
            return i==allItems.indexOf(item);
        }).join(' ');
    }    
}


export function makeNumberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if ( (charCode > 31 && ( charCode < 46 || charCode > 57)) || charCode == 47) {
      return false;
    }
    return true;
}


export function getPrintTitle (commodityName, coverageName, claimNumber, policyNumber, growerNumber): string {
 
    let title = removeDuplicateWords( makeTitleCase(commodityName + ' ' + coverageName)) + ' Loss Claim '
    title = title + claimNumber + '_Policy ' + policyNumber
    title = title + '_Grower ' + growerNumber

    return title
}



export function setHttpHeaders(authToken) {
    
    
    let requestId = `cirras-underwritingE${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

    return {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'RequestId': requestId,
        'Rest-Version': REST_VERSION.toString(),
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'Authorization': `Bearer ${authToken}`,
      })
    };

}

  export function isBaseCommodity(cropCommodityId: number, cropCommodityList: CropCommodityList) {
    
    let cmdty = cropCommodityList.collection.find(el => el.cropCommodityId == cropCommodityId)

    if (cmdty && cmdty.isCropInsuranceEligibleInd && cmdty.isInventoryCropInd) {

      return true

    } else {

      return false

    }
  }

  

  // these functions are used to compare whether the values in a form input are equal to the original value from the database 
  export function areNotEqual(a, b) { 
    // a, b -> string, boolean, or numeric values; it also handles null and empty strings

    if ( (a && !b) || (!a && b) || ( a && b && a != b ) ) {
        return true
    }

    return false
  }

  export function areDatesNotEqual(a, b) { 
    // a, b -> dates or null

    if ( (!a || a.toString() == 'Invalid Date') && (!b || b.toString() == 'Invalid Date') )  {
      return false
    } 

    if (a && b) { 

      const a1 = new Date(a)
      const b1= new Date(b)

      if ( a1.getTime() == b1.getTime()) {
        return false
      } else {
        return true
      }
        
    }

    return true
  }

  export function areNullableBooleanNotEqual(a: boolean | null, b: boolean | null) { 
    // a, b -> nullable boolean

    if ( (a == null && b != null) || (a != null && b == null) || ( a != null && b != null && a != b ) ) {
        return true
    }

    return false
  }

  export function userCanAccessInventory(securityUtilService, links: Array<RelLink>){
  
    let isUserPermittedToAccessInventory = false

    for (let i=0; i < links.length; i++) {

        // Does the user have permissions to see the inventory and/or create new inventory?
        if ( ( securityUtilService.doesUserHaveScope(SCOPES_UI.GET_INVENTORY_CONTRACT) 
        || securityUtilService.doesUserHaveScope(SCOPES_UI.CREATE_INVENTORY_CONTRACT)) 
        && links[i].href.toLocaleLowerCase().indexOf("rolloverinventorycontract") > -1 ) {

            isUserPermittedToAccessInventory =  true;

        } else if ( ( securityUtilService.doesUserHaveScope(SCOPES_UI.GET_INVENTORY_CONTRACT)  
            || securityUtilService.doesUserHaveScope(SCOPES_UI.UPDATE_INVENTORY_CONTRACT))
            && links[i].href.toLocaleLowerCase().indexOf("inventorycontracts") > -1 ) {
            // Does the user have permissions to see and/or update existing inventory?

            isUserPermittedToAccessInventory =  true;

        }
    }

    return isUserPermittedToAccessInventory
  }

  export function userCanAccessDop(securityUtilService, links: Array<RelLink>){

    for (let i = 0; i< links.length; i++ ) {

      if ( ( securityUtilService.doesUserHaveScope(SCOPES_UI.CREATE_DOP_YIELD_CONTRACT) 
            || securityUtilService.doesUserHaveScope(SCOPES_UI.GET_DOP_YIELD_CONTRACT) ) 
        && (links[i].href.toLocaleLowerCase().indexOf("rolloverdopyieldcontract") > -1 
            || links[i].href.toLocaleLowerCase().indexOf("dopyieldcontracts") > -1  ) ) {

          return true;
      } 
    }

    return false
  }

  export function userCanAccessVerifiedYield(securityUtilService, links: Array<RelLink>){

    for (let i = 0; i< links.length; i++ ) {

      if ( ( securityUtilService.doesUserHaveScope(SCOPES_UI.CREATE_VERIFIED_YIELD_CONTRACT) 
            || securityUtilService.doesUserHaveScope(SCOPES_UI.GET_VERIFIED_YIELD_CONTRACT) ) 
        && (links[i].href.toLocaleLowerCase().indexOf("rolloververifiedyieldcontract") > -1 
            || links[i].href.toLocaleLowerCase().indexOf("verifiedyieldcontracts") > -1  ) ) {

          return true;
      } 
    }

    return false
  }

  export function goToLinkGlobal (item: UwContract, linkType: string, router: Router) {

    let resourceRoute = ""

    switch (linkType) {
      case 'unseeded':
        resourceRoute = ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED

        router.navigate([resourceRoute, 
          item.insurancePlanId.toString(), 
          item.cropYear.toString(), 
          item.policyId.toString(), 
          (item.inventoryContractGuid ) ? item.inventoryContractGuid.toString() : '' 
        ]);

        break;
      
      case 'seeded':

        if (item.insurancePlanId == INSURANCE_PLAN.GRAIN) {

          resourceRoute = ResourcesRoutes.INVENTORY_GRAIN_SEEDED

        } else if (item.insurancePlanId == INSURANCE_PLAN.FORAGE) {

          resourceRoute = ResourcesRoutes.INVENTORY_FORAGE
          
        } else if (item.insurancePlanId == INSURANCE_PLAN.BERRIES) {

          resourceRoute = ResourcesRoutes.INVENTORY_BERRIES
          
        }
        
        router.navigate([resourceRoute, 
          item.insurancePlanId.toString(), 
          item.cropYear.toString(), 
          item.policyId.toString(), 
          (item.inventoryContractGuid ) ? item.inventoryContractGuid.toString() : '' 
        ]);

        break;

      case 'dop':

        if (item.insurancePlanId == INSURANCE_PLAN.GRAIN) {

            resourceRoute = ResourcesRoutes.DOP_GRAIN

        } else if (item.insurancePlanId == INSURANCE_PLAN.FORAGE) {

            resourceRoute = ResourcesRoutes.DOP_FORAGE
            
        }

        router.navigate([resourceRoute,
          item.insurancePlanId.toString(), 
          item.cropYear.toString(),
          item.policyId.toString(),
          (item.declaredYieldContractGuid ) ? item.declaredYieldContractGuid.toString() : '' 
        ]);

        break;

      case 'verifiedYield': 

        resourceRoute = ResourcesRoutes.VERIFIED_YIELD

        // extract verifiedYieldContractGuid from the haetus link here
        let verifiedYieldContractGuid = ""

        let vyLinkHref = item.links.find(x => x.href.indexOf("verifiedYieldContracts") > -1 )?.href
        
        if (vyLinkHref) {
          verifiedYieldContractGuid = vyLinkHref.substring(vyLinkHref.indexOf("verifiedYieldContracts") + 23 )
        }

        router.navigate([resourceRoute,
          item.insurancePlanId.toString(), 
          item.cropYear.toString(),
          item.policyId.toString(),
          verifiedYieldContractGuid
        ]);

    break;
      default:
        resourceRoute = ""
    }
  }


  export function getUniqueKey(){
    return Math.floor(Math.random() * 100000)
  }

  export function addUwCommentsObject(uwComment: UnderwritingComment) { 
    return {
      underwritingCommentGuid:        uwComment.underwritingCommentGuid ,
      annualFieldDetailId:            uwComment.annualFieldDetailId,
      growerContractYearId:           uwComment.growerContractYearId,
      declaredYieldContractGuid:      uwComment.declaredYieldContractGuid,
      underwritingCommentTypeCode:    uwComment.underwritingCommentTypeCode,
      underwritingCommentTypeDesc:    uwComment.underwritingCommentTypeDesc, 
      underwritingComment:            uwComment.underwritingComment,
      createUser:                     uwComment.createUser,
      createDate:                     uwComment.createDate,
      updateUser:                     uwComment.updateUser,
      updateDate:                     uwComment.updateDate,
      deletedByUserInd:               uwComment.deletedByUserInd,
      userCanEditInd:                 uwComment.userCanEditInd,
      userCanDeleteInd:               uwComment.userCanDeleteInd
    }
  }
  

  export function getInsurancePlanName(insurancePlanId){

    if(insurancePlanId) {

      switch (insurancePlanId) {

        case INSURANCE_PLAN.GRAPES:
            return INSURANCE_NAME.GRAPES
            
        case INSURANCE_PLAN.TREEFRUITS:
            return INSURANCE_NAME.TREEFRUITS
    
        case INSURANCE_PLAN.BERRIES:
            return INSURANCE_NAME.BERRIES
        
        case INSURANCE_PLAN.GRAIN:
            return INSURANCE_NAME.GRAIN
        
        case INSURANCE_PLAN.FORAGE:
            return INSURANCE_NAME.FORAGE
        
        case INSURANCE_PLAN.VEGETABLES:
            return INSURANCE_NAME.VEGETABLES
        
        case INSURANCE_PLAN.FLOWERS:
            return INSURANCE_NAME.FLOWERS
        
        default:
            return ""
      }
    }
    return ""
  }

  export function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  export function replaceNonAlphanumericCharacters(str: string) : string {
    return str.replace(/[^a-zA-Z0-9]/g, ' ').trim()
  }
  
  export function isInt(value) {
    if (isNaN(value)) {
      return false;
    }
    var x = parseFloat(value);
    return (x | 0) === x;
}