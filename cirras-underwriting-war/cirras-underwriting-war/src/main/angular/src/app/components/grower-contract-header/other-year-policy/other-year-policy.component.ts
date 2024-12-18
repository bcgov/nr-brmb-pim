import { ChangeDetectionStrategy, Component, OnChanges, OnInit, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { SecurityUtilService } from 'src/app/services/security-util.service';

@Component({
  selector: 'other-year-policy',
  templateUrl: './other-year-policy.component.html',
  styleUrl: './other-year-policy.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class OtherYearPolicyComponent implements OnInit, OnChanges  {

  // Input() OtherYearPolicy? or growerContract?
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
        cropYear: [""],
      })
    }
  
    ngOnChanges(changes: SimpleChanges) {
    
      // // populate crop year dropdown
      // if (changes.growerContract && 
      //   this.growerContract && 
      //   this.growerContract.linkedPolicies
      //   ) {
    
      //   this.populateCropYears();
      // }
    }

    populateCropYears(){

      // add a general info choice
      this.cropYearOptions.push ({
        description: "2024",
        value: "2024" 
      })

    }

}
