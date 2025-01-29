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

  constructor(protected appConfigService: AppConfigService,) { }

  goToYieldCalculatorLink(){

    let yieldCalculatorLink = this.appConfigService.getConfig().rest["grain_verified_yield_calculator_url"]
    window.open(yieldCalculatorLink, "_blank");
  }

}
