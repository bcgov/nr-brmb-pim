import { ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UwContract } from 'src/app/conversion/models';
import { INSURANCE_PLAN, ResourcesRoutes } from 'src/app/utils/constants';
import { goToLinkGlobal, makeTitleCase, userCanAccessDop, userCanAccessInventory, userCanAccessVerifiedYield } from "src/app/utils";
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { Router } from '@angular/router';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';

@Component({
  selector: 'related-policies',
  templateUrl: './related-policies.component.html',
  styleUrls: ['./related-policies.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})

export class RelatedPoliciesComponent implements OnInit, OnChanges {

  @Input() growerContract: UwContract; 

  relatedPoliciesOptions = [];

  relatedPoliciesForm: UntypedFormGroup;

  constructor( 
    private fb: UntypedFormBuilder,
    protected router: Router,
    public securityUtilService: SecurityUtilService
    ) { }

  ngOnInit(): void {
    // initialize the form
    this.relatedPoliciesForm = this.fb.group({
      relatedPolicies: [""],
    })
  }

  ngOnChanges(changes: SimpleChanges) {
  
    // populate related policies dropdown
    if (changes.growerContract && 
      this.growerContract && 
      this.growerContract.linkedPolicies
      ) {
  
      this.populateRelatedPolicies();
    }
  }

  populateRelatedPolicies(){
  
    this.relatedPoliciesOptions = []
  
    let description = ""

    if (this.growerContract.linkedPolicies.length == 1) {
      description = "1 Related Policy" 
    } else {
      description = "" + this.growerContract.linkedPolicies.length + " Related Policies" 
    }

    // add a general info choice
    this.relatedPoliciesOptions.push ({
      description: description,
      value: "" 
    })
    
    // first get the policies with the same grower
    for ( let i = 0; i < this.growerContract.linkedPolicies.length; i++) {

      let linkedPolicy = this.growerContract.linkedPolicies[i]

      if (this.growerContract.growerId == linkedPolicy.growerId) {

        description = "(" + makeTitleCase(linkedPolicy.insurancePlanName) + ") " + 
                        linkedPolicy.contractNumber + "-" + (linkedPolicy.cropYear.toString()).substring(2) 

        this.relatedPoliciesOptions.push ({
          description: description,
          value: linkedPolicy.policyId
        })
      }
    }

    // then get the policies with a different grower
    for ( let i = 0; i < this.growerContract.linkedPolicies.length; i++) {

      let linkedPolicy = this.growerContract.linkedPolicies[i]

      if (this.growerContract.growerId !== linkedPolicy.growerId) {

        description = "(" + makeTitleCase(linkedPolicy.insurancePlanName) + ") " + 
                        linkedPolicy.contractNumber + "-" + (linkedPolicy.cropYear.toString()).substring(2) 

        this.relatedPoliciesOptions.push ({
          description: description + "*",
          value: linkedPolicy.policyId
        })
      }
    }

  }
  
  relatedPoliciesChange(event){
      this.goToPolicy(event.value)
  }

  goToPolicy(policyId){

    let policy = this.growerContract.linkedPolicies.find( x => x.policyId == policyId)

    let routerUrl = this.router.url.toString()
    
    if (policy) {

      if (routerUrl.lastIndexOf(ResourcesRoutes.VERIFIED_YIELD) > -1 ) {
        // if the user is currently on a VERIFIED YIELD screen try to transfer him to the VERIFIED YIELD screen of the related policy 

        if (userCanAccessVerifiedYield(this.securityUtilService, policy.links)) {
          goToLinkGlobal(policy, 'verifiedYield', this.router )
          return
        }
      }

      if (routerUrl.lastIndexOf(ResourcesRoutes.DOP_GRAIN) > -1 || routerUrl.lastIndexOf(ResourcesRoutes.DOP_FORAGE) > -1 ) {
        // if the user is currently on a DOP screen try to transfer him to the DOP screen of the related policy 

        if (userCanAccessDop(this.securityUtilService, policy.links)) {
          // if the related policy has a DOP link and the user can view it  

          goToLinkGlobal(policy, 'dop', this.router )
          return
        }
      } 
      
      // default: transfer the user to the inventory screen
      this.goToInventoryScreen(policy, routerUrl)
    }    
  }

  goToInventoryScreen(policy: UwContract, routerUrl: string ) {

   // go to an inventory screen 
   if ( userCanAccessInventory(this.securityUtilService, policy.links)) {

    if (routerUrl.lastIndexOf(ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1 && policy.insurancePlanId == INSURANCE_PLAN.GRAIN ) {

      goToLinkGlobal(policy, 'unseeded', this.router )

    } else {
      // default
      goToLinkGlobal(policy, 'seeded', this.router )
      
    }
  }
  }


}
