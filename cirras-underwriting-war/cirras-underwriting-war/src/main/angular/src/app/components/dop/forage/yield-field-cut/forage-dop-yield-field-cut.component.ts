import { DecimalPipe } from "@angular/common";
import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { DopYieldFieldForage, DopYieldFieldForageCut } from "src/app/conversion/models-yield";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";
import { makeNumberOnly } from "src/app/utils";
import { PLANT_DURATION } from "src/app/utils/constants";

@Component({
    selector: 'forage-dop-yield-field-cut',
    templateUrl: './forage-dop-yield-field-cut.component.html',
    styleUrls: ['./forage-dop-yield-field-cut.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ForageDopYieldFieldCutComponent implements OnInit {
    @Input() yieldField: DopYieldFieldForage;
    @Input() yieldFieldCut: DopYieldFieldForageCut;
    @Input() yieldFieldCutsFormArray: UntypedFormArray;

    yieldFieldCutFormGroup: UntypedFormGroup;

    constructor(private fb: UntypedFormBuilder,
        private store: Store<RootState>,
        private decimalPipe: DecimalPipe) {
    }

    ngOnInit(): void {
        this.yieldFieldCutFormGroup = this.fb.group({
            declaredYieldFieldForageGuid: [this.yieldFieldCut.declaredYieldFieldForageGuid],
            inventoryFieldGuid: [this.yieldFieldCut.inventoryFieldGuid],
            cutNumber: [this.yieldFieldCut.cutNumber],
            totalBalesLoads: [this.yieldFieldCut.totalBalesLoads],
            weight: [this.yieldFieldCut.weight],
            weightDefaultUnit: [this.yieldFieldCut.weightDefaultUnit],
            moisturePercent: [this.yieldFieldCut.moisturePercent],
            deletedByUserInd: [this.yieldFieldCut.deletedByUserInd],
        });
    }

    numberOnly(event): boolean {
        return makeNumberOnly(event);
    }

    isTotalBalesLoadsEditable(): boolean {
        return !!this.yieldField.commodityTypeDescription;
    }

    updateTotalBalesLoads(): void {
        let totalBalesLoads = this.yieldFieldCutFormGroup.value.totalBalesLoads;
        totalBalesLoads = this.decimalPipe.transform(totalBalesLoads, '1.0-0')?.replace(',', '');
        this.yieldFieldCut.totalBalesLoads = parseFloat(totalBalesLoads) || null;

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    isWeightEditable(): boolean {
        return !!this.yieldField.commodityTypeDescription;
    }

    updateWeight(): void {
        let weight = this.yieldFieldCutFormGroup.value.weight;
        weight = this.decimalPipe.transform(weight, '1.0-2')?.replace(',', '');
        this.yieldFieldCut.weight = parseFloat(weight) || null;

        let moisturePercent = this.yieldFieldCut.moisturePercent;
        switch (this.yieldField.plantDurationTypeCode) {
            case PLANT_DURATION.ANNUAL:
                moisturePercent = 0;
                break;
            case PLANT_DURATION.PERENNIAL:
                moisturePercent = 15;
                break;
        }
        this.yieldFieldCut.moisturePercent = moisturePercent;
        this.yieldFieldCutFormGroup.patchValue({ moisturePercent: moisturePercent });

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    isMoisturePercentEditable(): boolean {
        return !!this.yieldField.commodityTypeDescription;
    }

    updateMoisturePercent(): void {
        let moisturePercent = this.yieldFieldCutFormGroup.value.moisturePercent;
        moisturePercent = this.decimalPipe.transform(moisturePercent, '1.0-0')?.replace(',', '');
        this.yieldFieldCut.moisturePercent = isNaN(moisturePercent) ? null : parseFloat(moisturePercent);

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }
}