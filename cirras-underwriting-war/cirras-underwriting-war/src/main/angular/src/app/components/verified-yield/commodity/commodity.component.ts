import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { FormArray, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { VerifiedYieldContractCommodity } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { makeNumberOnly } from "src/app/utils";


@Component({
  selector: 'cirras-commodity',
  templateUrl: './commodity.component.html',
  styleUrls: ['./commodity.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldCommodityComponent implements OnInit {

  @Input() commodity: VerifiedYieldContractCommodity;
  @Input() commoditiesFormArray: FormArray;
  @Input() decimalPrecision: number;

  commodityFormGroup: FormGroup;

  constructor(private fb: FormBuilder,
      private store: Store<RootState>) {
  }

  ngOnInit(): void {
      this.commodityFormGroup = this.fb.group({
        verifiedYieldContractCommodityGuid: [this.commodity.verifiedYieldContractCommodityGuid],
        verifiedYieldContractGuid: [this.commodity.verifiedYieldContractGuid],
        cropCommodityId: [this.commodity.cropCommodityId],
        isPedigreeInd: [this.commodity.isPedigreeInd],
        harvestedAcres: [this.commodity.harvestedAcres],
        harvestedAcresOverride: [this.commodity.harvestedAcresOverride],
        storedYieldDefaultUnit: [this.commodity.storedYieldDefaultUnit],
        soldYieldDefaultUnit: [this.commodity.soldYieldDefaultUnit],
        productionGuarantee: [this.commodity.productionGuarantee],
        harvestedYield: [this.commodity.harvestedYield],
        harvestedYieldOverride: [this.commodity.harvestedYieldOverride],
        yieldPerAcre: [this.commodity.yieldPerAcre],
        cropCommodityName: [this.commodity.cropCommodityName],
        totalInsuredAcres: [this.commodity.totalInsuredAcres]
      });
      this.commoditiesFormArray.push(this.commodityFormGroup);
  }

  numberOnly(event): boolean {
      return makeNumberOnly(event);
  }

}
