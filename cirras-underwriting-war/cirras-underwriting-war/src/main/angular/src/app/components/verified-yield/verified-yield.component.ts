import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../common/base/base.component';
import { UwContract } from 'src/app/conversion/models';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { VerifiedYieldContract } from 'src/app/conversion/models-yield';
import { getInsurancePlanName } from 'src/app/utils';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import {ViewEncapsulation } from '@angular/core';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationStateService } from 'src/app/services/application-state.service';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { AppConfigService, TokenService } from '@wf1/core-ui';
import { ConnectionService } from 'ngx-connection-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Overlay } from '@angular/cdk/overlay';
import { HttpClient } from '@angular/common/http';
import { DecimalPipe } from '@angular/common';
import { VerifiedYieldComponentModel } from './verified-yield.component.model';
import { LoadVerifiedYieldContract, RolloverVerifiedYieldContract } from 'src/app/store/verified-yield/verified-yield.actions';
import { VERIFIED_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';

@Component({
  selector: 'verified-yield',
  templateUrl: './verified-yield.component.html',
  styleUrls: ['./verified-yield.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class VerifiedYieldComponent extends BaseComponent {

  @Input() growerContract: UwContract;
  @Input() isUnsaved: boolean;
  @Input() verifiedYieldContract: VerifiedYieldContract;

  policyId: string;
  verifiedYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = VERIFIED_COMPONENT_ID; 


  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    protected store: Store<RootState>,
    protected fb: FormBuilder,
    protected dialog: MatDialog,
    protected applicationStateService: ApplicationStateService,
    public securityUtilService: SecurityUtilService,                
    protected tokenService: TokenService,
    protected connectionService: ConnectionService,
    protected snackbarService: MatSnackBar,
    protected overlay: Overlay,
    protected cdr: ChangeDetectorRef,
    protected appConfigService: AppConfigService,
    protected http: HttpClient,
    protected titleService: Title,
    protected decimalPipe: DecimalPipe) {
    super(router, route, sanitizer, store, fb, dialog, applicationStateService, securityUtilService, tokenService, connectionService, snackbarService, overlay, cdr, appConfigService, http, titleService, decimalPipe);
  }


  initModels() {
    this.viewModel = new VerifiedYieldComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): VerifiedYieldComponentModel  {  
    return <VerifiedYieldComponentModel>this.viewModel;
  }

  loadPage() {

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

          this.policyId = params.get("policyId") ? params.get("policyId") : "";
          this.verifiedYieldContractGuid = params.get("verifiedYieldContractGuid") ? params.get("verifiedYieldContractGuid").trim() : "";
          this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
          this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

          this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId))

          if (this.verifiedYieldContractGuid.length > 0) {
            // get the already existing verified yield contract
            this.store.dispatch(LoadVerifiedYieldContract(this.componentId, this.verifiedYieldContractGuid ))
          } else {
            // prepare the new verified yield contract
            this.store.dispatch(RolloverVerifiedYieldContract(this.componentId, this.policyId))
          }

      }
    );

    this.store.dispatch(setFormStateUnsaved(this.componentId, false ));
  }

  // ngOnChanges(changes: SimpleChanges) {
  //   super.ngOnChanges(changes);

  //   if (this.verifiedYieldContract) {
  //     // any changes that need to be set up
      
  //   }

  // }


  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  onCancel(){
    // TODO
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 146px 12px 155px'
    }
  }

}
