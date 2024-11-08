import { DecimalPipe } from "@angular/common";
import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { VerifiedYieldContractCommodity } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { VERIFIED_YIELD_COMPONENT_ID } from "src/app/store/verified-yield/verified-yield.state";
import { makeNumberOnly } from "src/app/utils";
import { SecurityUtilService } from 'src/app/services/security-util.service';

@Component({
  selector: 'verified-yield-commodity',
  templateUrl: './verified-yield-commodity.component.html',
  styleUrls: ['./verified-yield-commodity.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldCommodityComponent implements OnInit {

  @Input() commodity: VerifiedYieldContractCommodity;
  @Input() commoditiesFormArray: UntypedFormArray;
  @Input() decimalPrecision: number;

  commodityFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder,
      private store: Store<RootState>,
      public securityUtilService: SecurityUtilService, 
      private decimalPipe: DecimalPipe) {
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

  updateHarvestedAcresOverride(): void {
    let harvestedAcresOverride = this.commodityFormGroup.value.harvestedAcresOverride;
    
    harvestedAcresOverride = this.decimalPipe.transform(harvestedAcresOverride, '1.0-1')?.replace(',', '');
    this.commodity.harvestedAcresOverride = parseFloat(harvestedAcresOverride) || null;

    this.commodityFormGroup.controls['harvestedAcresOverride'].setValue(harvestedAcresOverride)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

  updateHarvestedYieldOverride(): void {
    let harvestedYieldOverride = this.commodityFormGroup.value.harvestedYieldOverride;
    harvestedYieldOverride = this.decimalPipe.transform(harvestedYieldOverride, '1.0-3')?.replace(',', '');
    this.commodity.harvestedYieldOverride = parseFloat(harvestedYieldOverride) || null;

    this.commodityFormGroup.controls['harvestedYieldOverride'].setValue(harvestedYieldOverride)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

  onDeleteCommodity() {
    this.commodityFormGroup.controls['harvestedAcresOverride'].setValue('')
    this.commodityFormGroup.controls['harvestedYieldOverride'].setValue('')

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

}
