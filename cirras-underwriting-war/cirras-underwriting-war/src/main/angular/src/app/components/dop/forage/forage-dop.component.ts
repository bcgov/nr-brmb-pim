import { Component, Input, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { UwContract } from 'src/app/conversion/models';
import { ForageDopComponentModel } from './forage-dop.component.model';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ParamMap } from '@angular/router';
import { ClearDopYieldContract, LoadDopYieldContract, LoadYieldMeasUnitList, RolloverDopYieldContract } from 'src/app/store/dop/dop.actions';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { DopYieldContract, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { getInsurancePlanName } from 'src/app/utils';

@Component({
  selector: 'forage-dop',
  templateUrl: './forage-dop.component.html',
  styleUrls: ['./forage-dop.component.scss']
})
export class ForageDopComponent extends BaseComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;


  policyId: string;
  declaredYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = DOP_COMPONENT_ID;
  decimalPrecision: number;

  hasDataChanged= false;

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

          // if (this.declaredYieldContractGuid.length > 0) {
          //   // get the already existing dop yield contract
          //   this.store.dispatch(LoadDopYieldContract(this.componentId, this.declaredYieldContractGuid ))
          // } else {
          //   // prepare the new dop yield contract
          //   this.store.dispatch(RolloverDopYieldContract(this.componentId, this.policyId))
          // }

          //  this.getGradeModifiers()
      }
    );

    //this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  // ngOnChanges(changes: SimpleChanges) {
  //   super.ngOnChanges(changes);
  
  //   this.ngOnChanges2(changes);
  // }

  
  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  onPrint() {
    // TODO
  }

}
