import { ChangeDetectionStrategy, Component, Input, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { AnnualField, UwContract } from 'src/app/conversion/models';
import { DopYieldContract } from 'src/app/conversion/models-yield';
import { BaseComponent } from '../../common/base/base.component';
import { BerriesDopComponentModel } from './berries-dop.component.model';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ParamMap } from '@angular/router';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { BERRY_COMMODITY, SCREEN_TYPE } from 'src/app/utils/constants';
import { LoadDopYieldContract, RolloverDopYieldContract } from 'src/app/store/dop/dop.actions';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { getInsurancePlanName } from 'src/app/utils';

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
    this.policyCommoditiesList = []

    var self = this
    if (this.dopYieldContract && this.dopYieldContract.fields) {

      this.dopYieldContract.fields.forEach ((field: AnnualField) => {
        // commodities on the screen should be ordered as: Blueberry, Raspberry, Strawberry, Cranberry
        self.addCommodityToList(field, BERRY_COMMODITY.Blueberry)
        self.addCommodityToList(field, BERRY_COMMODITY.Raspberry)
        self.addCommodityToList(field, BERRY_COMMODITY.Strawberry)
        self.addCommodityToList(field, BERRY_COMMODITY.Cranberry)

      })
    }
  }

  addCommodityToList(field : AnnualField, crpt){

    let el = field.dopYieldFieldCommodityBerriesList.find( x => x.cropCommodityId == crpt)

    // if there is a field which has the desired commodity 
    // then add it to the list of commodities to display on the screen,
    // assuming it was not added before
    if (el && !this.policyCommoditiesList.find( a => a.cropCommodityId == crpt )) {

       this.policyCommoditiesList.push({
              cropCommodityId: el.cropCommodityId,
              cropCommodityName: el.cropCommodityName
            })
    }
  }

  getFarmTotalsForCommodity(crpt) {
    return this.dopYieldContract.dopYieldContractCommodityBerriesList.find(x => x.cropCommodityId == crpt)
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 186px 146px 12px 155px'
    }
  }

}
