import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { UwContract } from 'src/app/conversion/models';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { 
  AddForageDopYieldFieldCut,
  AddNewDopYieldContract,
  ClearDopYieldContract, 
  DelForageDopYieldFieldCut, 
  DeleteDopYieldContract,
  GetDopReport,
  LoadDopYieldContract, 
  LoadYieldMeasUnitList, 
  RolloverDopYieldContract,
  UpdateDopYieldContract
} from 'src/app/store/dop/dop.actions';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { DopYieldContract, GradeModifierList, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { getInsurancePlanName, makeNumberOnly, setHttpHeaders } from 'src/app/utils';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import {ViewEncapsulation } from '@angular/core';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
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
import { GrainVerifiedYieldComponentModel } from './grain-verified-yield.component.model';



@Component({
  selector: 'grain-verified-yield',
  templateUrl: './grain-verified-yield.component.html',
  styleUrls: ['./grain-verified-yield.component.scss']
})
export class GrainVerifiedYieldComponent extends BaseComponent {

  @Input() growerContract: UwContract;
  @Input() isUnsaved: boolean;

  policyId: string;
  verifiedYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = DOP_COMPONENT_ID; //TODO replace with VERIFIED_YIELD_COMPONENT_ID once the store is ready


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
    this.viewModel = new GrainVerifiedYieldComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): GrainVerifiedYieldComponentModel  {  
    return <GrainVerifiedYieldComponentModel>this.viewModel;
  }

  loadPage() {

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

          this.policyId = params.get("policyId") ? params.get("policyId") : "";
          this.verifiedYieldContractGuid = params.get("verifiedYieldContractGuid") ? params.get("verifiedYieldContractGuid").trim() : "";
          this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
          this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

          // this.store.dispatch(ClearDopYieldContract());

          this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId))

          // TODO: replace with GET/ROLLOVER links for verified yield
          // if (this.declaredYieldContractGuid.length > 0) {
          //   // get the already existing dop yield contract
          //   this.store.dispatch(LoadDopYieldContract(this.componentId, this.declaredYieldContractGuid ))
          // } else {
          //   // prepare the new dop yield contract
          //   this.store.dispatch(RolloverDopYieldContract(this.componentId, this.policyId))
          // }

      }
    );

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  onCancel(){
    // TODO
  }

  setFormStyles(){
    return {
      // 'grid-template-columns':  'auto 148px 110px 100px 128px 186px 146px 12px 155px'
      'grid-template-columns':  'auto 146px 12px 155px'
    }
  }

}
