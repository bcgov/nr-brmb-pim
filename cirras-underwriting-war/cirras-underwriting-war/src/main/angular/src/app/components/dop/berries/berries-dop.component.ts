import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { AnnualField, UwContract } from 'src/app/conversion/models';
import { DopYieldContract } from 'src/app/conversion/models-yield';
import { BaseComponent } from '../../common/base/base.component';
import { BerriesDopComponentModel } from './berries-dop.component.model';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ParamMap } from '@angular/router';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { BERRY_COMMODITY, SCREEN_TYPE } from 'src/app/utils/constants';
import { AddNewDopYieldContract, DeleteDopYieldContract, GetDopReport, LoadDopYieldContract, RolloverDopYieldContract, UpdateDopYieldContract } from 'src/app/store/dop/dop.actions';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { getInsurancePlanName, replaceNonAlphanumericCharacters } from 'src/app/utils';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';

@Component({
  selector: 'berries-dop',
  templateUrl: './berries-dop.component.html',
  styleUrl: './berries-dop.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class BerriesDopComponent extends BaseComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() isUnsaved: boolean;

  policyId: string;
  declaredYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;

  hasVerifiedYieldData = false

  policyCommoditiesList = []

  initModels() {
    this.viewModel = new BerriesDopComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): BerriesDopComponentModel  {  
    return <BerriesDopComponentModel>this.viewModel;
  }

  loadPage() {
    this.componentId = DOP_COMPONENT_ID;

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

        this.policyId = params.get("policyId") ? params.get("policyId") : "";
        this.declaredYieldContractGuid = params.get("declaredYieldContractGuid") ? params.get("declaredYieldContractGuid").trim() : "";
        this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
        this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

        this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId, SCREEN_TYPE.DOP))

        if (this.declaredYieldContractGuid.length > 0) {
          // get the already existing dop yield contract
          this.store.dispatch(LoadDopYieldContract(this.componentId, this.declaredYieldContractGuid ))
        } else {
          // prepare the new dop yield contract
          this.store.dispatch(RolloverDopYieldContract(this.componentId, this.policyId))
        }
      }
    );

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  ngOnChanges(changes: SimpleChanges) {

    if ( changes.growerContract && this.growerContract ) {

      this.hasVerifiedYieldData = false

      // check for verified yield data
      for (let i = 0; i< this.growerContract.links.length; i++ ) {

        if ( this.growerContract.links[i].href.toLocaleLowerCase().indexOf("verifiedyieldcontracts") > -1  ) {
          this.hasVerifiedYieldData = true
          break
        } 
      }
    }

    if ( changes.dopYieldContract && this.dopYieldContract ) {
      this.setUpCommoditiesList()
    }
  }

  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  setUpCommoditiesList(){
    // commodities on the screen should be ordered as: Blueberry, Raspberry, Strawberry, Cranberry
    this.policyCommoditiesList = []

    if (this.dopYieldContract && this.dopYieldContract.fields) {
      let hasBlueberry = false
      let hasRaspberry = false
      let hasStrawberry = false
      let hasCranberry = false

      this.dopYieldContract.fields.forEach ((field: AnnualField) => {

        if (field.dopYieldFieldCommodityBerriesList.find( x => x.cropCommodityId == BERRY_COMMODITY.Blueberry)) {
          hasBlueberry = true
        }
        
        if (field.dopYieldFieldCommodityBerriesList.find( x => x.cropCommodityId == BERRY_COMMODITY.Raspberry)) {
          hasRaspberry = true
        }

        if (field.dopYieldFieldCommodityBerriesList.find( x => x.cropCommodityId == BERRY_COMMODITY.Strawberry)) {
          hasStrawberry = true
        }

        if (field.dopYieldFieldCommodityBerriesList.find( x => x.cropCommodityId == BERRY_COMMODITY.Cranberry)) {
          hasCranberry = true
        }

      })

      if (hasBlueberry) {
        this.policyCommoditiesList.push({
              cropCommodityId: BERRY_COMMODITY.Blueberry,
              cropCommodityName: "Blueberry"
            })
      }     
      if (hasRaspberry) {
        this.policyCommoditiesList.push({
              cropCommodityId: BERRY_COMMODITY.Raspberry,
              cropCommodityName: "Raspberry"
            })
      }
      if (hasStrawberry) {
        this.policyCommoditiesList.push({
              cropCommodityId: BERRY_COMMODITY.Strawberry,
              cropCommodityName: "Strawberry"
            })
      }
      if (hasCranberry) {
        this.policyCommoditiesList.push({
              cropCommodityId: BERRY_COMMODITY.Cranberry,
              cropCommodityName: "Cranberry"
            })
      }
    }
  }

  getFarmTotalsForCommodity(crpt) {
    return this.dopYieldContract.dopYieldContractCommodityBerriesList.find(x => x.cropCommodityId == crpt)
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 370px 186px 146px 12px 155px'
    }
  }

  onSave() {
    // set up units
    this.dopYieldContract.enteredYieldMeasUnitTypeCode = this.dopYieldContract.defaultYieldMeasUnitTypeCode

    if (this.dopYieldContract.declaredYieldContractGuid) {
      this.store.dispatch(UpdateDopYieldContract(DOP_COMPONENT_ID, this.dopYieldContract, this.policyId, "Yield "))
    } else {
      // add new
      this.store.dispatch(AddNewDopYieldContract(DOP_COMPONENT_ID, this.dopYieldContract, this.policyId))
    }

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  onCancel(){

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
          // reload the page
          this.loadPage()
    
          this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
    
          displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
        }
    }

  onPrint() {
    let reportName = replaceNonAlphanumericCharacters(this.growerContract.growerName) + "-DOP" 
    this.store.dispatch(GetDopReport(reportName, this.policyId, "", this.insurancePlanId, "", "", "", "", ""));
  }


  onDeleteDop() {

    //Ask for confirmation before deleting all DOP data
    if ( confirm("You are about to delete all yield data for the policy. Do you want to continue?") ) {

      if (this.dopYieldContract.declaredYieldContractGuid) {
        //Delete dop contract
        this.store.dispatch(DeleteDopYieldContract(DOP_COMPONENT_ID, this.policyId, this.dopYieldContract, "Yield "))

      } 
    }
  }

  onDopCommentsDone(uwComments: UnderwritingComment[]) {
    this.dopYieldContract.uwComments = uwComments;
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }

}
