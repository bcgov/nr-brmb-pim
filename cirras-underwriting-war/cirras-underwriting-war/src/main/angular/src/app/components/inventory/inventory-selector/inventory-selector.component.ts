import { ActivatedRoute, Router} from "@angular/router";
import { Store} from "@ngrx/store";
import { ChangeDetectionStrategy, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ParamMap } from '@angular/router';
import { CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { LoadCropCommodityList, LoadUnderSeededCropCommodityList } from 'src/app/store/crop-commodity/crop-commodity.actions';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { ClearInventoryContract,  LoadInventoryContract, RolloverInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { CROP_COMMODITY_TYPE_CONST, INSURANCE_PLAN, ResourcesRoutes, SCREEN_TYPE } from 'src/app/utils/constants';
import { RootState } from "src/app/store";
import { ErrorState, LoadState } from "src/app/store/application/application.state";


@Component({
  selector: 'cirras-underwriting-inventory-selector',
  templateUrl: './inventory-selector.component.html',
  styleUrls: ['./inventory-selector.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InventorySelectorComponent implements OnInit{ 

  @Input() growerContract: UwContract;
  @Input() inventoryContract: InventoryContract;
  @Input() cropCommodityList: CropCommodityList;
  @Input() underSeededCropCommodityList: CropCommodityList;
  @Input() loadState: LoadState;
  @Input() errorState: ErrorState[];

  policyId: string;
  inventoryContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = INVENTORY_COMPONENT_ID;

  constructor (protected router: Router,
    protected route: ActivatedRoute,
    protected store: Store<RootState>,) {
  }

  ngOnInit() {
    this.loadPage()
  }

  loadPage() {
    this.componentId = INVENTORY_COMPONENT_ID;

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

          this.policyId = params.get("policyId") ? params.get("policyId") : "";
          this.inventoryContractGuid = params.get("inventoryContractGuid") ? params.get("inventoryContractGuid").trim() : "";
          this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
          this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

          this.store.dispatch(ClearInventoryContract());

          this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId, SCREEN_TYPE.INVENTORY ))

          if (this.inventoryContractGuid.length > 0) {
            this.store.dispatch(LoadInventoryContract(this.componentId, this.inventoryContractGuid ))
          } else {
            // prepare the new inventory contract
            this.store.dispatch(RolloverInventoryContract(this.componentId, this.policyId))
          }
          
          this.store.dispatch(LoadCropCommodityList(this.componentId,
                                                    this.insurancePlanId, 
                                                    this.cropYear, 
                                                    CROP_COMMODITY_TYPE_CONST.INVENTORY,
                                                    "Y" ))
 
         
          if (this.router.url.indexOf ( ResourcesRoutes.INVENTORY_GRAIN_SEEDED) > -1 
          || this.router.url.indexOf ( ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1 ) {
             
            this.store.dispatch(LoadUnderSeededCropCommodityList(this.componentId,
              INSURANCE_PLAN.FORAGE.toString(), 
              this.cropYear.toString(), 
              CROP_COMMODITY_TYPE_CONST.INVENTORY,
              "Y" ))

          }
          
      }
    );
  }

  getInventoryType() {
    // console.log("this.router.url: " + this.router.url)

    let currentUrlPath = ""  + this.router.url

    if (currentUrlPath.indexOf ( ResourcesRoutes.INVENTORY_GRAIN_UNSEEDED) > -1 ) {
      return("unseeded" )
    }

    if (currentUrlPath.indexOf ( ResourcesRoutes.INVENTORY_GRAIN_SEEDED) > -1 ) {
      return("seeded" )
    }

    if (currentUrlPath.indexOf ( ResourcesRoutes.INVENTORY_FORAGE) > -1 ) {
      return("forage" )
    }

  }

}
