import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { VerifiedYieldContractCommodity } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { VERIFIED_YIELD_COMPONENT_ID } from "src/app/store/verified-yield/verified-yield.state";
import { makeNumberOnly } from "src/app/utils";
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { roundUpDecimalYield, roundUpDecimalAcres } from "../../inventory/inventory-common";

@Component({
  selector: 'verified-yield-commodity',
  templateUrl: './verified-yield-commodity.component.html',
  styleUrls: ['./verified-yield-commodity.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldCommodityComponent implements OnChanges {

  @Input() commodity: VerifiedYieldContractCommodity;
  @Input() commoditiesFormArray: UntypedFormArray;

  commodityFormGroup: UntypedFormGroup;

  constructor(private fb: UntypedFormBuilder,
      private store: Store<RootState>,
      public securityUtilService: SecurityUtilService, 
      ) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.commodity && this.commodity) {
      this.setupForm()
    }
  }

  setupForm() {
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
    let harvestedAcresOverride = roundUpDecimalAcres(this.commodityFormGroup.value.harvestedAcresOverride)
    this.commodity.harvestedAcresOverride = parseFloat(harvestedAcresOverride.toString()) || null
    this.commodityFormGroup.controls['harvestedAcresOverride'].setValue(harvestedAcresOverride)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true))
  }

  updateHarvestedYieldOverride(): void {
    let harvestedYieldOverride = roundUpDecimalYield(this.commodityFormGroup.value.harvestedYieldOverride, 3)
    this.commodity.harvestedYieldOverride = parseFloat(harvestedYieldOverride.toString()) || null
    this.commodityFormGroup.controls['harvestedYieldOverride'].setValue(harvestedYieldOverride)

    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

  onDeleteCommodity() {
    this.commodityFormGroup.controls['harvestedAcresOverride'].setValue('')
    this.commodityFormGroup.controls['harvestedYieldOverride'].setValue('')
    this.commodity.harvestedAcresOverride = null
    this.commodity.harvestedYieldOverride = null
    
    this.store.dispatch(setFormStateUnsaved(VERIFIED_YIELD_COMPONENT_ID, true));
  }

}
