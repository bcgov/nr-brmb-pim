import { DecimalPipe } from "@angular/common";
import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { FormArray, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";
import { makeNumberOnly } from "src/app/utils";

@Component({
    selector: 'forage-dop-commodity',
    templateUrl: './forage-dop-commodity.component.html',
    styleUrls: ['./forage-dop-commodity.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopCommodityComponent implements OnInit {
    @Input() commodity: DopYieldContractCommodityForage;
    @Input() commoditiesFormArray: FormArray;
    @Input() decimalPrecision: number;

    commodityFormGroup: FormGroup;

    constructor(private fb: FormBuilder,
        private store: Store<RootState>,
        private decimalPipe: DecimalPipe) {
    }

    ngOnInit(): void {
        this.commodityFormGroup = this.fb.group({
            declaredYieldContractCmdtyForageGuid: [this.commodity.declaredYieldContractCmdtyForageGuid],
            declaredYieldContractGuid: [this.commodity.declaredYieldContractGuid],
            commodityTypeCode: [this.commodity.commodityTypeCode],
            totalFieldAcres: [this.commodity.totalFieldAcres],
            harvestedAcres: [this.commodity.harvestedAcres],
            harvestedAcresOverride: [this.commodity.harvestedAcresOverride],
            quantityHarvestedTons: [this.commodity.quantityHarvestedTons],
            quantityHarvestedTonsOverride: [this.commodity.quantityHarvestedTonsOverride],
            yieldPerAcre: [this.commodity.yieldPerAcre],
            commodityTypeDescription: [this.commodity.commodityTypeDescription]
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

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    updateQuantityHarvestedTonsOverride(): void {
        let quantityHarvestedTonsOverride = this.commodityFormGroup.value.quantityHarvestedTonsOverride;
        quantityHarvestedTonsOverride = this.decimalPipe.transform(quantityHarvestedTonsOverride, `1.0-${this.decimalPrecision}`)?.replace(',', '');
        this.commodity.quantityHarvestedTonsOverride = parseFloat(quantityHarvestedTonsOverride) || null;

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }
}