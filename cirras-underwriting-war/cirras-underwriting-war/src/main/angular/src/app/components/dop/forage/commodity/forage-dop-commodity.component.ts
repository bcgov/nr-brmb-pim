import { DecimalPipe } from "@angular/common";
import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";
import { makeNumberOnly } from "src/app/utils";
import { PLANT_DURATION } from "src/app/utils/constants";

@Component({
    selector: 'forage-dop-commodity',
    templateUrl: './forage-dop-commodity.component.html',
    styleUrls: ['./forage-dop-commodity.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopCommodityComponent implements OnInit {
    @Input() commodity: DopYieldContractCommodityForage;
    @Input() commoditiesFormArray: UntypedFormArray;
    @Input() decimalPrecision: number;

    commodityFormGroup: UntypedFormGroup;

    constructor(private fb: UntypedFormBuilder,
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
            totalBalesLoads: [this.commodity.totalBalesLoads],
            weight: [this.commodity.weight],
            weightDefaultUnit: [this.commodity.weightDefaultUnit],
            moisturePercent: [this.commodity.moisturePercent],
            quantityHarvestedTons: [this.commodity.quantityHarvestedTons],
            yieldPerAcre: [this.commodity.yieldPerAcre],
            commodityTypeDescription: [this.commodity.commodityTypeDescription],
            cropCommodityId: [this.commodity.cropCommodityId],
            plantDurationTypeCode: [this.commodity.plantDurationTypeCode]
        });
        this.commoditiesFormArray.push(this.commodityFormGroup);
    }

    numberOnly(event): boolean {
        return makeNumberOnly(event);
    }

    updateHarvestedAcres(): void {
        let harvestedAcres = this.commodityFormGroup.value.harvestedAcres;
        harvestedAcres = this.decimalPipe.transform(harvestedAcres, '1.0-1')?.replace(',', '');
        this.commodity.harvestedAcres = parseFloat(harvestedAcres) || null;

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    updateTotalBalesLoads(): void {
        let totalBalesLoads = this.commodityFormGroup.value.totalBalesLoads;
        totalBalesLoads = this.decimalPipe.transform(totalBalesLoads, '1.0-0')?.replace(',', '');
        this.commodity.totalBalesLoads = parseFloat(totalBalesLoads) || null;

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    updateWeight(): void {
        let weight = this.commodityFormGroup.value.weight;
        weight = this.decimalPipe.transform(weight, '1.0-2')?.replace(',', '');
        this.commodity.weight = parseFloat(weight) || null;

        let moisturePercent = this.commodity.moisturePercent;
        switch (this.commodity.plantDurationTypeCode) {
            case PLANT_DURATION.ANNUAL:
                moisturePercent = 0;
                break;
            case PLANT_DURATION.PERENNIAL:
                moisturePercent = 15;
                break;
        }
        this.commodity.moisturePercent = moisturePercent;
        this.commodityFormGroup.patchValue({ moisturePercent: moisturePercent });

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }


    updateMoisturePercent(): void {
        let moisturePercent = this.commodityFormGroup.value.moisturePercent;
        moisturePercent = this.decimalPipe.transform(moisturePercent, '1.0-0')?.replace(',', '');
        this.commodity.moisturePercent = isNaN(moisturePercent) ? null : parseFloat(moisturePercent);

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

}