import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { FormArray } from "@angular/forms";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";

@Component({
  selector: 'verified-yield-commodity-list',
  templateUrl: './verified-yield-commodity-list.component.html',
  styleUrls: ['./verified-yield-commodity-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldCommodityListComponent  {

  @Input() commodities: Array<DopYieldContractCommodityForage>;
  @Input() commoditiesFormArray: FormArray;
  @Input() isUnsaved: boolean;

  yieldCalculatorLink = "ms-excel:ofe|u|https://bcgov.sharepoint.com/sites/AF-BRMB-DATA/Shared%20Documents/PI%20Grain%20Yield%20&%20Claim%20Calculator.xlsm"

  
  goToYieldCalculatorLink(){
    window.open(this.yieldCalculatorLink, "_blank");
  }

}
