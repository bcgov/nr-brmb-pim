import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { AppConfigService } from "@wf1/wfcc-core-lib";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";

@Component({
  selector: 'verified-yield-commodity-list',
  templateUrl: './verified-yield-commodity-list.component.html',
  styleUrls: ['./verified-yield-commodity-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldCommodityListComponent  {

  @Input() commodities: Array<DopYieldContractCommodityForage>;
  @Input() commoditiesFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;

  // yieldCalculatorLink = "ms-excel:ofe|u|https://bcgov.sharepoint.com/sites/AF-BRMB-DATA/Shared%20Documents/Yield%20Calculators/PI%20Grain%20Yield%20&%20Claim%20Calculator.xlsm"

  constructor(protected appConfigService: AppConfigService,) { }

  goToYieldCalculatorLink(){

    let yieldCalculatorLink = this.appConfigService.getConfig().rest["grain_verified_yield_calculator_url"]

    // TODO remove console.log

    console.log("yieldCalculatorLink: " + yieldCalculatorLink )
    window.open(yieldCalculatorLink, "_blank");
  }

}
