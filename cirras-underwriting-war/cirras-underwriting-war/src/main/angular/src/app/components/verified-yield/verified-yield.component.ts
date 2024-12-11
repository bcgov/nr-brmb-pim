import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { BaseComponent } from '../common/base/base.component';
import { CropCommodityList, UwContract } from 'src/app/conversion/models';
import { ParamMap } from '@angular/router';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { VerifiedYieldContract } from 'src/app/conversion/models-yield';
import { getInsurancePlanName } from 'src/app/utils';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { ViewEncapsulation } from '@angular/core';
import { VerifiedYieldComponentModel } from './verified-yield.component.model';
import { AddNewVerifiedYieldContract, DeleteVerifiedYieldContract, LoadVerifiedYieldContract, RolloverVerifiedYieldContract, UpdateVerifiedYieldContract } from 'src/app/store/verified-yield/verified-yield.actions';
import { VERIFIED_YIELD_COMPONENT_ID } from 'src/app/store/verified-yield/verified-yield.state';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { ClearCropCommodity, LoadCropCommodityList } from 'src/app/store/crop-commodity/crop-commodity.actions';
import { CROP_COMMODITY_TYPE_CONST, INSURANCE_PLAN } from 'src/app/utils/constants';

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
  @Input() cropCommodityList: CropCommodityList

  policyId: string;
  verifiedYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = VERIFIED_YIELD_COMPONENT_ID; 

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

          this.loadCropCommodities()
      }
    );

    this.store.dispatch(setFormStateUnsaved(this.componentId, false ));
  }
 
  loadCropCommodities() {
    // Clear existing commodity list first.
    this.store.dispatch(ClearCropCommodity())

    this.store.dispatch(LoadCropCommodityList(VERIFIED_YIELD_COMPONENT_ID,
        INSURANCE_PLAN.GRAIN.toString(),
        this.cropYear.toString(),
        CROP_COMMODITY_TYPE_CONST.INVENTORY,
        "N")
      )
  }

  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  onCancel() {

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      this.loadPage()

      this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, false ));

      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }

  onDeleteVerifiedYield() {
        //Ask for confirmation before deleting all Verified Yield data
        if ( confirm("You are about to delete all Verified Yield data for the policy. Do you want to continue?") ) {

          if (this.verifiedYieldContract.verifiedYieldContractGuid) {
            //Delete verified yield contract
            this.store.dispatch(DeleteVerifiedYieldContract(VERIFIED_YIELD_COMPONENT_ID, this.policyId, this.verifiedYieldContract))
    
          } 
        }
  }

  // Function for Save
  onSave() {
    
    if (!this.isFormValid()) {
      return
    }

    // remove rows with empty verifiedYieldAmendmentGuid and deletedByUserInd = true from verifiedYieldContract
    this.cleanUpAmendments()

    if (this.verifiedYieldContract.verifiedYieldContractGuid) {
      this.store.dispatch(UpdateVerifiedYieldContract(VERIFIED_YIELD_COMPONENT_ID, this.verifiedYieldContract))
    } else {
      // add new
      this.store.dispatch(AddNewVerifiedYieldContract(VERIFIED_YIELD_COMPONENT_ID, this.verifiedYieldContract))
    }

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, false ));
  }

  isFormValid() {
    // amendments: all inputs but fields are required
    for (let i=0; i < this.verifiedYieldContract.verifiedYieldAmendments.length; i++) {

      if (this.verifiedYieldContract.verifiedYieldAmendments[i].deletedByUserInd !== true ) {
        if (!this.verifiedYieldContract.verifiedYieldAmendments[i].verifiedYieldAmendmentCode ||
            !this.verifiedYieldContract.verifiedYieldAmendments[i].cropCommodityId ||
            !this.verifiedYieldContract.verifiedYieldAmendments[i].yieldPerAcre ||
            !this.verifiedYieldContract.verifiedYieldAmendments[i].acres ||
            !this.verifiedYieldContract.verifiedYieldAmendments[i].rationale) {

              alert("Ammendment Type, Commodity, Yield/ac, Acres and Rationale are mandatory!")
              return false
            }
      }
    }

    return true
  }

  cleanUpAmendments(){
    // removes rows with empty verifiedYieldAmendmentGuid and deletedByUserInd = true from verifiedYieldContract
  
    for (let i=0; i < this.verifiedYieldContract.verifiedYieldAmendments.length; i++) {

      if (this.verifiedYieldContract.verifiedYieldAmendments[i].deletedByUserInd == true && 
          !this.verifiedYieldContract.verifiedYieldAmendments[i].verifiedYieldAmendmentGuid) {

            // remove amendment
            this.verifiedYieldContract.verifiedYieldAmendments.splice(i)
            i--
          }
    }
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 146px 12px 200px'
    }
  }

}
