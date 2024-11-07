import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UwContract } from 'src/app/conversion/models';
import { INSURANCE_PLAN, ResourcesRoutes } from 'src/app/utils/constants';
import { goToLinkGlobal, userCanAccessDop, userCanAccessInventory, userCanAccessVerifiedYield } from "src/app/utils";
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { Router } from '@angular/router';

@Component({
  selector: 'cirras-grower-contract-header',
  templateUrl: './grower-contract-header.component.html',
  styleUrls: ['./grower-contract-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GrowerContractHeaderComponent {

  @Input() growerContract: UwContract;  

  constructor(
    protected router: Router,
    public securityUtilService: SecurityUtilService,                
    ) { }

  isUnseededVisible(){
    if (this.growerContract && this.growerContract.insurancePlanId == INSURANCE_PLAN.GRAIN) {
      return true
    } else {
      return false
    }
  }

  userCanAccessInventory(){
    // if growerContract is not loaded don't show the links
    if (this.growerContract) {
      return userCanAccessInventory (this.securityUtilService, this.growerContract.links)  
    } else {
      return false
    }    
  }

  userCanAccessDop(){
    if (this.growerContract) {
      return userCanAccessDop(this.securityUtilService, this.growerContract.links)  
    } else {
      return false
    }
  }

  userCanAccessVerifiedYield(){
    if (this.growerContract) {
      return userCanAccessVerifiedYield(this.securityUtilService, this.growerContract.links)  
    } else {
      return false
    }
  }

  goToLink (linkType) {

    goToLinkGlobal(this.growerContract, linkType, this.router)

  }
  
  isLinkActive(linkType) {

    let routerUrl = this.router.url.toString()

    if ( linkType == 'unseeded' && routerUrl.indexOf(ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1) {
      return true
    } 

    if ( linkType == 'seeded' && 
        ( routerUrl.indexOf(ResourcesRoutes.INVENTORY_GRAIN_SEEDED) > -1 || 
          routerUrl.indexOf(ResourcesRoutes.INVENTORY_FORAGE) > -1  
        )) {
        return true
    } 

    if ( linkType == 'dop' && 
        (routerUrl.indexOf(ResourcesRoutes.DOP_GRAIN) > -1 || 
        routerUrl.indexOf(ResourcesRoutes.DOP_FORAGE) > -1  )) {
      return true
    } 

    if ( linkType == 'verifiedYield' && 
      routerUrl.indexOf(ResourcesRoutes.VERIFIED_YIELD_GRAIN) > -1 ) { 
      return true
    } 

    return false
  }

  isForageScreen(){

    let routerUrl = this.router.url.toString()

    if (routerUrl.indexOf(ResourcesRoutes.INVENTORY_FORAGE) > -1 ||
        routerUrl.indexOf(ResourcesRoutes.DOP_FORAGE) > -1 ) {

      return true

    } else {

      return false
      
    }
    
  }

  setStyles(){

    let styles = { // default - for Grain
      'grid-template-columns':  '26px 154px 35px 26px 134px 35px 26px 190px 35px 26px 110px auto'
    }

    if (this.isForageScreen()) {
      styles = {
        'grid-template-columns':  '26px 134px 35px 26px 200px 35px 26px 110px auto'
      }
    }

    return styles;
  }


  setGrowerStyles(){

    let routerUrl = this.router.url.toString()

    let styles = { // default - no styling
      'border': '',
      'border-radius': '',
      'background': ''
    }

    if (routerUrl.indexOf(ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1 ) {
      styles = {  
        'border': '1px solid #BECFE3',
        'border-radius': '5px',
        'background': '#EFF5FA'
      }
    }

    // seeded inventory for Grain and Forage background colors
    if (routerUrl.indexOf(ResourcesRoutes.INVENTORY_GRAIN_SEEDED) > -1 ||
        routerUrl.indexOf(ResourcesRoutes.INVENTORY_FORAGE) > -1) {

      styles = {  
        'border': '1px solid #B8CFB8',
        'border-radius': '5px',
        'background': '#F1F7F1'
      }
    }

    if (routerUrl.indexOf(ResourcesRoutes.DOP_GRAIN) > -1 || 
        routerUrl.indexOf(ResourcesRoutes.DOP_FORAGE) > -1 ) {
      styles = {  
        'border': '1px solid #E1CF7B',
        'border-radius': '5px',
        'background': '#FEFAEC'
      }
    }

    if (routerUrl.indexOf(ResourcesRoutes.VERIFIED_YIELD_GRAIN) > -1 ) {
      styles = {  
        'border': '1px solid #DEC7DE',
        'border-radius': '5px',
        'background': '#FFF5FF'
      }
    }

    return styles;
  }

}
