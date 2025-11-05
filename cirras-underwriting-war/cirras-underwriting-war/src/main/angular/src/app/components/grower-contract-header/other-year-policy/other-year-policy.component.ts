import { ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { OtherYearPolicy } from 'src/app/conversion/models';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { ResourcesRoutes } from 'src/app/utils/constants';

@Component({
    selector: 'other-year-policy',
    templateUrl: './other-year-policy.component.html',
    styleUrl: './other-year-policy.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    standalone: false
})
export class OtherYearPolicyComponent implements OnInit, OnChanges  {

  // Input otherYearPolicies? or growerContract?
  @Input() otherYearPolicies: Array<OtherYearPolicy>
  @Input() currentPolicyCropYear;

  cropYearOptions = [];
  
  otherYearPoliciesForm: UntypedFormGroup;

  constructor( 
    private fb: UntypedFormBuilder,
    protected router: Router,
    public securityUtilService: SecurityUtilService
  ) { }

  ngOnInit(): void {
    
    // initialize the form
    this.otherYearPoliciesForm = this.fb.group({
      cropYear: [this.currentPolicyCropYear],
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.otherYearPolicies && this.otherYearPolicies) {
  
      this.populateCropYears();
    }

    if (changes.currentPolicyCropYear && this.currentPolicyCropYear) {
      if (this.otherYearPoliciesForm) {
        this.otherYearPoliciesForm.controls.cropYear.setValue(this.currentPolicyCropYear)
      }
    }
  }

  populateCropYears(){

    var self = this

    self.cropYearOptions = []
    self.cropYearOptions.push (this.currentPolicyCropYear)

    self.otherYearPolicies.forEach( (el: OtherYearPolicy)  => {
      self.cropYearOptions.push (el.cropYear)
    });

    self.cropYearOptions.sort()
  }

  cropYearChange(event) {

    let otherYearPolicy = this.otherYearPolicies.find( x => x.cropYear == event.value ) 

    if (otherYearPolicy) {

      const resourceRoute = this.getCurrentResourceRoute()

      if (resourceRoute.length > 0 ) {
        this.router.navigate([resourceRoute, 
          otherYearPolicy.insurancePlanId.toString(), 
          otherYearPolicy.cropYear.toString(), 
          otherYearPolicy.policyId.toString(), 
          (otherYearPolicy.screenRecordGuid ) ? otherYearPolicy.screenRecordGuid.toString() : '' 
        ]);
      }
    }
  }

  getCurrentResourceRoute() {

    const routerUrl = this.router.url.toString()
    let newRoute = ""

    if (routerUrl.lastIndexOf(ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1 ) {
      return ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED
    } 

    if (routerUrl.lastIndexOf(ResourcesRoutes.INVENTORY_GRAIN_SEEDED) > -1 ) {
      return ResourcesRoutes.INVENTORY_GRAIN_SEEDED
    } 

    if (routerUrl.lastIndexOf(ResourcesRoutes.INVENTORY_FORAGE) > -1 ) {
      return ResourcesRoutes.INVENTORY_FORAGE
    } 

    if (routerUrl.lastIndexOf(ResourcesRoutes.INVENTORY_BERRIES) > -1 ) {
      return ResourcesRoutes.INVENTORY_BERRIES
    } 

    if (routerUrl.lastIndexOf(ResourcesRoutes.DOP_GRAIN) > -1 ) {
      return ResourcesRoutes.DOP_GRAIN
    } 

    if (routerUrl.lastIndexOf(ResourcesRoutes.DOP_FORAGE) > -1 ) {
      return ResourcesRoutes.DOP_FORAGE
    }

    if (routerUrl.lastIndexOf(ResourcesRoutes.VERIFIED_YIELD) > -1 ) {
      return ResourcesRoutes.VERIFIED_YIELD
    }

    return newRoute
  }

}
