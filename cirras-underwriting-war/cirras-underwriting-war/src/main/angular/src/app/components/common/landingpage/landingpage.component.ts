import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import {AppConfigService, TokenService} from "@wf1/wfcc-core-lib";
import {Overlay} from "@angular/cdk/overlay";
import { HttpClient } from '@angular/common/http';
import { setHttpHeaders, userCanAccessDop, userCanAccessInventory } from 'src/app/utils';
import { UwContract, UwContractsList } from 'src/app/conversion/models';
import { INSURANCE_PLAN, ResourcesRoutes } from 'src/app/utils/constants';

@Component({
  selector: 'cirras-landingpage',
  templateUrl: './landingpage.component.html',
  styleUrls: ['./landingpage.component.scss']
})
export class LandingPageComponent implements OnInit {

  policyNumber: string;
  targetScreen: string;
  hasErrors: boolean = false;
  errorMessage: string;
  
  targetScreens = {
    INVENTORY: "inventory",  
    YIELD: "yield",
    VERIFIED_YIELD: "verified_yield" 
  }

  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    public securityUtilService: SecurityUtilService,                
    protected tokenService: TokenService,
    protected overlay: Overlay,
    protected cdr: ChangeDetectorRef,
    protected appConfigService: AppConfigService,
    protected http: HttpClient) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      (params: ParamMap) => {

        this.policyNumber = params.get("policyNumber") ? params.get("policyNumber") : "";
        this.targetScreen = params.get("targetScreen") ? params.get("targetScreen") : "";
      }
    );

    if(this.targetScreen && 
            (this.targetScreen == this.targetScreens.INVENTORY 
              || this.targetScreen == this.targetScreens.YIELD 
              || this.targetScreen == this.targetScreens.VERIFIED_YIELD)) {
      if(this.policyNumber){
        //Get uw contract
        this.redirectInventoryYield();
      } else {
        this.hasErrors = true;
        this.errorMessage = "No policy number";
      }
    } else {
      this.hasErrors = true;
      this.errorMessage = "No target screen or invalid target screen: " + this.targetScreen;
    }
  }

  redirectInventoryYield() {

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url + "/uwcontracts?policyNumber=" + this.policyNumber
    //url = url + "&sortColumn=policyNumber&sortDirection=ASC&pageNumber=1&pageRowCount=20"

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    this.http.get(url,httpOptions).toPromise().then((data: UwContractsList) => {

      if (data && data.collection.length == 1 ) {
       
        if(this.targetScreen && this.targetScreen == this.targetScreens.INVENTORY) {
          this.redirectToInventory(data.collection[0]);
        } else if(this.targetScreen && this.targetScreen == this.targetScreens.YIELD) {
          this.redirectToYield(data.collection[0]);
        } else if(this.targetScreen && this.targetScreen == this.targetScreens.VERIFIED_YIELD) {
          this.redirectToVerifiedYield(data.collection[0]);
        }

      } else {
        this.hasErrors = true;

        if (data && data.collection.length == 0){
          this.errorMessage = "Policy " + this.policyNumber + " not found. Make sure this is the correct policy number";
        } else if (data && data.collection.length > 1 ){
          this.errorMessage = "Too many results (" + data.collection.length + ") for policy number " + this.policyNumber + ". Make sure this is the correct policy number";
        } else {
          this.errorMessage = "Policy " + this.policyNumber + " not found. Make sure this is the correct policy number";
        }
      }
      
      this.cdr.detectChanges();

    })
  }

  redirectToVerifiedYield(uwContract: UwContract) {

    if (userCanAccessDop(this.securityUtilService, uwContract.links)) {

      let vyLink = "/" + ResourcesRoutes.VERIFIED_YIELD + 
      "/" + uwContract.insurancePlanId + 
      "/" + uwContract.cropYear +
      "/" + uwContract.policyId + 
      "/" + (uwContract.verifiedYieldContractGuid ? uwContract.verifiedYieldContractGuid : " ");

      this.router.navigate([vyLink]) 
  
    } else {
      this.hasErrors = true;
      this.errorMessage = "There is no yield data for policy " + this.policyNumber;
    }
  }

  redirectToYield(uwContract: UwContract) {

    if (userCanAccessDop(this.securityUtilService, uwContract.links)) {

      let resourceRoute = ""

      if ( uwContract.insurancePlanId == INSURANCE_PLAN.GRAIN) {

        resourceRoute = ResourcesRoutes.DOP_GRAIN

      } else if ( uwContract.insurancePlanId == INSURANCE_PLAN.FORAGE) {

          resourceRoute = ResourcesRoutes.DOP_FORAGE
          
      }

      let dopLink = "/" + resourceRoute + 
      "/" + uwContract.insurancePlanId + 
      "/" + uwContract.cropYear +
      "/" + uwContract.policyId + 
      "/" + (uwContract.declaredYieldContractGuid ? uwContract.declaredYieldContractGuid : " ");

      this.router.navigate([dopLink]) 
  
    } else {
      this.hasErrors = true;
      this.errorMessage = "There is no yield data for policy " + this.policyNumber;
    }
  }

  redirectToInventory(uwContract: UwContract) {
    if (userCanAccessInventory(this.securityUtilService, uwContract.links)) {

      let resourcesRoute = ""

      if (uwContract.insurancePlanId == INSURANCE_PLAN.GRAIN) {
        resourcesRoute = ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED
      } else {
        resourcesRoute = ResourcesRoutes.INVENTORY_FORAGE
      }

      let inventoryLink = "/" + resourcesRoute + 
                    "/" + uwContract.insurancePlanId + 
                    "/" + uwContract.cropYear +
                    "/" + uwContract.policyId + 
                    "/" + (uwContract.inventoryContractGuid ? uwContract.inventoryContractGuid : " ")
    
       this.router.navigate([inventoryLink]) 

    } else {
      this.hasErrors = true;
      this.errorMessage = "There is no inventory data for policy " + this.policyNumber;
    }
  }

}
