import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { AppConfigService } from "@wf1/wfcc-core-lib";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";
import { INSURANCE_PLAN } from "src/app/utils/constants";

@Component({
    selector: 'verified-yield-commodity-list',
    templateUrl: './verified-yield-commodity-list.component.html',
    styleUrls: ['./verified-yield-commodity-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class VerifiedYieldCommodityListComponent  {

  @Input() commodities: Array<DopYieldContractCommodityForage>;
  @Input() commoditiesFormArray: UntypedFormArray;
  @Input() insurancePlanId: number;
  @Input() isUnsaved: boolean;

  constructor(protected appConfigService: AppConfigService,) { }

  goToYieldCalculatorLink(){

    let yieldCalculatorLink = ""

    switch (this.insurancePlanId) { 

      case INSURANCE_PLAN.GRAIN:
        yieldCalculatorLink = this.appConfigService.getConfig().rest["grain_verified_yield_calculator_url"]
        break

      case INSURANCE_PLAN.FORAGE:
        yieldCalculatorLink = this.appConfigService.getConfig().rest["forage_verified_yield_calculator_url"]
        break

      default :
        yieldCalculatorLink = ""
    }
      
    if (yieldCalculatorLink.length > 0) {
      window.open(yieldCalculatorLink, "_blank");
    }
    
  }

    isGrainPolicy() {
      if (this.insurancePlanId == INSURANCE_PLAN.GRAIN ) {
        return true
      } else {
        return false
      }
    }

}
