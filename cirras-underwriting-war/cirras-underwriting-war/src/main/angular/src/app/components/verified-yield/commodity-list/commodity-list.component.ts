import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { FormArray } from "@angular/forms";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";

@Component({
  selector: 'verified-yield-commodity-list',
  templateUrl: './commodity-list.component.html',
  styleUrls: ['./commodity-list.component.scss']
})
export class VerifiedYieldCommodityListComponent  {

  @Input() commodities: Array<DopYieldContractCommodityForage>;
  @Input() commoditiesFormArray: FormArray;
  @Input() decimalPrecision: number;
  @Input() isUnsaved: boolean;

  get harvestedYieldOverride(): number {
    // TODO
      return this.commodities.reduce((acc, curr) => acc + curr.totalFieldAcres || 0, 0);
  }

  get harvestedAcresOverride(): number {
    // TODO
      return this.commodities.reduce((acc, curr) => acc + curr.harvestedAcres || 0, 0);
  }
}
