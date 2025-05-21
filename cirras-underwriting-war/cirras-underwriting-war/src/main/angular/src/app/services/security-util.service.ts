import {Injectable, Injector} from "@angular/core";
import {TokenService} from "@wf1/wfcc-core-lib";
import {ROLES_UI, SCOPES_UI} from "../utils/scopes";


@Injectable({
    providedIn: "root"
})
export class SecurityUtilService {
    tokenService: TokenService;

    constructor(private injector: Injector) {
    }

    private getTokenService() {
      const tokenservice = this.tokenService;
      const injToken = this.injector.get(TokenService)      
        return this.tokenService ? this.tokenService : this.injector.get(TokenService);
    }

    public doesUserHaveScopes(scopes: string[]): boolean {
        return this.getTokenService().doesUserHaveApplicationPermissions(scopes);
    }

    public doesUserHaveScope(scope) {
        return this.doesUserHaveScopes([scope]);
    }

    hasScope(scope: string): boolean {
      return this.hasScopes([scope]);
    }

    hasScopes(scopes: string[]): boolean {
        return this.doesUserHaveScopes(scopes);
    }

    //role security---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    isSuperAdmin() {
      return this.hasScope(ROLES_UI.UNDERWRITING_SUPER_ADMIN);
  }

    isWriter() {
        return this.hasScope(ROLES_UI.UNDERWRITING_WRITE);
    }

    isViewer() {
        return this.hasScope(ROLES_UI.UNDERWRITING_READ);
    }

    // scope security
    canEditInventory() {
        return this.doesUserHaveScopes([SCOPES_UI.CREATE_INVENTORY_CONTRACT, SCOPES_UI.UPDATE_INVENTORY_CONTRACT])  
    }

    canDeleteInventory() {
        return this.doesUserHaveScopes ([SCOPES_UI.DELETE_INVENTORY_CONTRACT] ) 
    }

    canEditDop() {
        return this.doesUserHaveScopes([SCOPES_UI.CREATE_DOP_YIELD_CONTRACT, SCOPES_UI.UPDATE_DOP_YIELD_CONTRACT]) 
    }

    canDeleteDop() {
        return this.doesUserHaveScopes ([SCOPES_UI.DELETE_DOP_YIELD_CONTRACT] ) 
    }

    canEditVerifiedYield() {
        return this.doesUserHaveScopes([SCOPES_UI.CREATE_VERIFIED_YIELD_CONTRACT, SCOPES_UI.UPDATE_VERIFIED_YIELD_CONTRACT]) 
    }

    canDeleteVerifiedYield() {
        return this.doesUserHaveScopes ([SCOPES_UI.DELETE_VERIFIED_YIELD_CONTRACT] ) 
    }

    canEditLegalLand() {
        return this.doesUserHaveScopes([SCOPES_UI.CREATE_LEGAL_LAND, SCOPES_UI.UPDATE_LEGAL_LAND])  
    }

    canDeleteLegalLand() {
        return this.doesUserHaveScopes ([SCOPES_UI.DELETE_LEGAL_LAND] ) 
    }

    canEditSeedingDeadlines() {
        return this.doesUserHaveScopes ([SCOPES_UI.SAVE_SEEDING_DEADLINES] ) 
    }

    canEditGradeModifiers() {
        return this.doesUserHaveScopes ([SCOPES_UI.SAVE_GRADE_MODIFIERS] ) 
    }

    canCreateUwYear() {
        return this.doesUserHaveScopes ([SCOPES_UI.CREATE_UNDERWRITING_YEAR] ) 
    }

    canEditForageVarietyInsurability() {
        return this.doesUserHaveScopes ([SCOPES_UI.SAVE_CROP_VARIETY_INSURABILITIES] ) 
    }

    canEditYieldConversion() {
        return this.doesUserHaveScopes ([SCOPES_UI.SAVE_YIELD_MEAS_UNIT_CONVERSIONS] ) 
    }

    canEditUserSettings() {
        return this.doesUserHaveScopes ([SCOPES_UI.CREATE_USER_SETTING, SCOPES_UI.UPDATE_USER_SETTING] ) 
    }
}
