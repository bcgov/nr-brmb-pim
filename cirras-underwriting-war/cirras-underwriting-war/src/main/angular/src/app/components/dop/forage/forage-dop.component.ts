import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { UwContract } from 'src/app/conversion/models';
import { ForageDopComponentModel } from './forage-dop.component.model';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ParamMap } from '@angular/router';
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
import { getInsurancePlanName, makeNumberOnly, replaceNonAlphanumericCharacters, setHttpHeaders } from 'src/app/utils';
import { GradeModifierOptionsType } from '../dop-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import {ViewEncapsulation } from '@angular/core';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';

@Component({
  selector: 'forage-dop',
  templateUrl: './forage-dop.component.html',
  styleUrls: ['./forage-dop.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class ForageDopComponent extends BaseComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;
  @Input() isUnsaved: boolean;

  // max number of the allowed cuts per field
  maxNumOfCutsAllowed = 7

  // variables
  policyId: string;
  declaredYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = DOP_COMPONENT_ID;

  gradeModifierList : GradeModifierOptionsType[] = [];

  initModels() {
    this.viewModel = new ForageDopComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): ForageDopComponentModel  {  
    return <ForageDopComponentModel>this.viewModel;
  }

  loadPage() {
    this.componentId = DOP_COMPONENT_ID;

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

          this.policyId = params.get("policyId") ? params.get("policyId") : "";
          this.declaredYieldContractGuid = params.get("declaredYieldContractGuid") ? params.get("declaredYieldContractGuid").trim() : "";
          this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
          this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

          this.store.dispatch(ClearDopYieldContract());

          this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId))

          this.store.dispatch( LoadYieldMeasUnitList(this.componentId, this.insurancePlanId) )

          if (this.declaredYieldContractGuid.length > 0) {
            // get the already existing dop yield contract
            this.store.dispatch(LoadDopYieldContract(this.componentId, this.declaredYieldContractGuid ))
          } else {
            // prepare the new dop yield contract
            this.store.dispatch(RolloverDopYieldContract(this.componentId, this.policyId))
          }

          this.getGradeModifiers(this.gradeModifierList)
      }
    );

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);

    this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.setValue(this.dopYieldContract?.enteredYieldMeasUnitTypeCode);
    if (this.dopYieldContract && this.dopYieldContract.declarationOfProductionDate) {
      this.viewModel.formGroup.controls.declarationOfProductionDate.setValue(new Date(this.dopYieldContract.declarationOfProductionDate));
    } else {
      this.viewModel.formGroup.controls.declarationOfProductionDate.setValue(null);
    }
    this.viewModel.formGroup.controls.balerWagonInfo.setValue(this.dopYieldContract?.balerWagonInfo);
    this.viewModel.formGroup.controls.totalLivestock.setValue(this.dopYieldContract?.totalLivestock);
  }


  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  get decimalPrecision() {
    const yieldMeasUnitTypeCode = this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.value;

    if (this.yieldMeasUnitList && this.yieldMeasUnitList.collection) {
      let selectedYieldMeasUnit = this.yieldMeasUnitList.collection.find( f => f.yieldMeasUnitTypeCode == yieldMeasUnitTypeCode )
      if (selectedYieldMeasUnit) {
        return selectedYieldMeasUnit.decimalPrecision;
      }
    }

    return 0;
  }


  onPrint() {
    let reportName = replaceNonAlphanumericCharacters(this.growerContract.growerName) + "-DOP" 
    this.store.dispatch(GetDopReport(reportName, this.policyId, "", this.insurancePlanId, "", "", "", "", ""));
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  updateBalerWagonInfo() {
    this.dopYieldContract.balerWagonInfo = this.getViewModel().formGroup.value.balerWagonInfo;

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }

  updateTotalLivestock() {
    let totalLivestock = this.getViewModel().formGroup.value.totalLivestock;
    totalLivestock = this.decimalPipe.transform(totalLivestock, '1.0-0')?.replace(',', '');
    this.dopYieldContract.totalLivestock = parseFloat(totalLivestock) || null;

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }

  updateDeclarationOfProductionDate() {
    let declarationOfProductionDate = this.getViewModel().formGroup.value.declarationOfProductionDate;
    this.dopYieldContract.declarationOfProductionDate = declarationOfProductionDate?.valueOf();

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }

  updateEnteredYieldMeasUnitTypeCode() {
    this.dopYieldContract.enteredYieldMeasUnitTypeCode = this.getViewModel().formGroup.value.yieldMeasUnitTypeCode;

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }


  getGradeModifiers(gradeModifierList: GradeModifierOptionsType[]){

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url +"/gradeModifiers?cropYear=" + this.cropYear
    url = url +"&insurancePlanId=" +  this.insurancePlanId.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())
   
    return this.http.get(url,httpOptions).toPromise().then((data: GradeModifierList) => {

      gradeModifierList = []
      // construct the grade modifiers options 
      for (let i=0; i< data.collection.length; i++ ) {
        gradeModifierList.push({
            cropCommodityId: data.collection[i].cropCommodityId.toString(),
            gradeModifierTypeCode: data.collection[i].gradeModifierTypeCode,
            description: data.collection[i].description
        })
      }
    })

  }

  onDopCommentsDone(uwComments: UnderwritingComment[]) {
    this.dopYieldContract.uwComments = uwComments;
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
  }

  onDeleteDop() {

    //Ask for confirmation before deleting all DOP data
    if ( confirm("You are about to delete all DOP data for the policy. Do you want to continue?") ) {

      if (this.dopYieldContract.declaredYieldContractGuid) {
        //Delete dop contract
        this.store.dispatch(DeleteDopYieldContract(DOP_COMPONENT_ID, this.policyId, this.dopYieldContract))

      } 
    }
  }

  onCancel() {

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      this.loadPage()

      this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));

      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 148px 120px 100px 128px 186px 146px 12px 155px'
    }
  }

  isCutEmpty(cutNumber) {
    for (const field of this.dopYieldContract.fields) {
      for (const yieldField of field.dopYieldFieldForageList) {
        for (const yieldFieldCut of yieldField.dopYieldFieldForageCuts) {
          if (yieldFieldCut.cutNumber == cutNumber && 
              (yieldFieldCut.totalBalesLoads || yieldFieldCut.weight || yieldFieldCut.moisturePercent)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  onAddNewCut() {
    
    if (this.securityUtilService.canEditInventory() && this.isNewCutBtnEnabled()) {

      // check if the last added cut is empty
      if (this.isCutEmpty(this.numCuts)) {

        alert("Cut " + this.numCuts + " is empty. Please enter bales/load and weight for at least one field before creating a new cut.")
        return
      }

      this.store.dispatch(AddForageDopYieldFieldCut(this.dopYieldContract));
      this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));

    }
  }

  onRemoveLastCut() {

    // Mark the cut for deletion 

    if (this.securityUtilService.canEditInventory() && this.isRemoveCutBtnEnabled()) {

      if ( confirm("You are about to delete the last cut for all fields. Do you want to continue?") ) {

        this.store.dispatch(DelForageDopYieldFieldCut(this.dopYieldContract));
        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));

      }
    }

  }

  get numCuts(): number {
    if (!this.dopYieldContract || !this.dopYieldContract.fields) { return 0; }
    for (const field of this.dopYieldContract.fields) {
      for (const yieldField of field.dopYieldFieldForageList) {
        return yieldField.dopYieldFieldForageCuts.filter(yieldFieldCut => !yieldFieldCut.deletedByUserInd).length;
      }
    }
    return 0;
  }

  isNewCutBtnEnabled() {
    if (this.numCuts < this.maxNumOfCutsAllowed) {
      return true;
    } else {
      return false;
    }
  } 

  isRemoveCutBtnEnabled() {
    if (this.numCuts > 1) {
      return true;
    } else {
      return false;
    }
  } 

  // Function for Save
  onSave() {
    

    if (!this.isFormValid()) {
      return
    }

    if (this.dopYieldContract.declaredYieldContractGuid) {
      this.store.dispatch(UpdateDopYieldContract(DOP_COMPONENT_ID, this.dopYieldContract))
    } else {
      // add new
      this.store.dispatch(AddNewDopYieldContract(DOP_COMPONENT_ID, this.dopYieldContract))
    }

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));


  }

  isFormValid() {

    // DOP date should be a valid date
    let declarationOfProductionDate = this.dopYieldContract.declarationOfProductionDate;

    if ( !declarationOfProductionDate || new Date(declarationOfProductionDate).toString() == 'Invalid Date' ) {
      alert("Empty or invalid Declaration of Production Date")
      return false
    }

    // Baler/Wagon Make & Model cannot be empty
    const balerWagonInfo = this.dopYieldContract.balerWagonInfo;

    if (balerWagonInfo === "") {
      alert("Baler/Wagon Make & Model cannot be empty")
      return false
    }

    // validate cuts - testable when Save is ready

    //At least one field must have data in a Cut.
    for(let i = 1; i <= this.numCuts; i++) {

      if (this.isCutEmpty(i)) {
        alert("Cut Number " + i + " is empty. Save is not possible.")        
        return false
      }

    }

    for (const field of this.dopYieldContract.fields) {
      for (const yieldField of field.dopYieldFieldForageList) {
        for (const yieldFieldCut of yieldField.dopYieldFieldForageCuts) {
          // If either totalBalesLoads or weight is entered then all three values are mandatory. 
          // However since the moisture percent has a default value, it might happen that only the moisture percent is saved for a cut. 
          // TODO: if that's the case then onSave() clear the moisture percent if totalBalesLoads and weight are empty
          const totalBalesLoads = yieldFieldCut.totalBalesLoads;
          const weight = yieldFieldCut.weight;
          const moisturePercent = yieldFieldCut.moisturePercent;
          if ((totalBalesLoads || weight) && (!totalBalesLoads || !weight || moisturePercent == null)) {
            alert("Saving partial information for a Cut is not possible. Please enter all 3 values: Bales/Load, Weight and Percent Moisture.")
            return false
          }

          // Percent Moisture: validate range 0 - 100
          if (moisturePercent < 0 || moisturePercent > 100) {
            alert("Percent Moisture for Cuts should be between 0 and 100")
            return false
          }
        }
      }
    }

    // Units have to be selected
    if (!this.dopYieldContract.enteredYieldMeasUnitTypeCode) {
      alert("Please select a unit for Yield")
      return false
    }

    // For commodity totals: if either totalBalesLoads or weight is entered then all three values are mandatory.  
    for (const cmdty of this.dopYieldContract.dopYieldContractCommodityForageList) {
      // If either totalBalesLoads or weight is entered then all three values are mandatory. 
      // However since the moisture percent has a default value, it might happen that only the moisture percent is saved for a cut. 
      const totalBalesLoads = cmdty.totalBalesLoads;
      const weight = cmdty.weight;
      const moisturePercent = cmdty.moisturePercent;
      if ((totalBalesLoads || weight) && (!totalBalesLoads || !weight || moisturePercent == null)) {
        alert("Saving partial information for a commodity in Commodity Totals table is not possible. Please enter all 3 values: Bales/Load, Weight and Percent Moisture.")
        return false
      }

      // Percent Moisture: validate range 0 - 100
      if (moisturePercent < 0 || moisturePercent > 100) {
        alert("Percent Moisture in in Commodity Totals table  should be between 0 and 100")
        return false
      }
    }

    return true
  }

}
