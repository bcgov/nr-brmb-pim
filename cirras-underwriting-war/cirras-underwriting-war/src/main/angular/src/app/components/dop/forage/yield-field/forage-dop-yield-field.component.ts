import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { FormArray, FormBuilder, FormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { DopYieldFieldForage } from "src/app/conversion/models-yield";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";

@Component({
    selector: 'forage-dop-yield-field',
    templateUrl: './forage-dop-yield-field.component.html',
    styleUrls: ['./forage-dop-yield-field.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopYieldFieldComponent implements OnInit {
    @Input() yieldField: DopYieldFieldForage;
    @Input() yieldFieldsFormArray: FormArray;
    @Input() fieldHiddenOnPrintoutInd: boolean;

    yieldFieldFormGroup: FormGroup;

    constructor(private fb: FormBuilder,
        private store: Store<RootState>,
        public securityUtilService: SecurityUtilService) {
    }

    ngOnInit(): void {
        this.yieldFieldFormGroup = this.fb.group({
            inventoryFieldGuid: [this.yieldField.inventoryFieldGuid],
            commodityTypeCode: [this.yieldField.commodityTypeCode],
            commodityTypeDescription: [this.yieldField.commodityTypeDescription],
            cropVarietyName: [this.yieldField.cropVarietyName],
            cropVarietyId: [this.yieldField.cropVarietyId],
            plantDurationTypeCode: [this.yieldField.plantDurationTypeCode],
            isQuantityInsurableInd: {
                value: this.yieldField.isQuantityInsurableInd,
                disabled: true
            },
            fieldAcres: [this.yieldField.fieldAcres],
            insurancePlanId: [this.yieldField.insurancePlanId],
            fieldId: [this.yieldField.fieldId],
            cropYear: [this.yieldField.cropYear],
            isHiddenOnPrintoutInd: {
                value: this.yieldField.isHiddenOnPrintoutInd,
                disabled: true
            },
            dopYieldFieldForageCuts: this.fb.array([]),
        });
        this.yieldFieldsFormArray.push(this.yieldFieldFormGroup);
    }

    clearCut(): void {
        this.yieldField.dopYieldFieldForageCuts = this.yieldField.dopYieldFieldForageCuts.map(yieldFieldCut => {
            return {...yieldFieldCut, totalBalesLoads: null, weight: null, moisturePercent: null};
        });

        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    get numCuts(): number {
        return this.yieldField.dopYieldFieldForageCuts.filter(yieldFieldCut => !yieldFieldCut.deletedByUserInd).length;
    }

    setCutStyles() {
        let gridTemplateColumns = '';
        for (let i = 0; i < this.numCuts; i++) {
            gridTemplateColumns += ' 1fr 1fr 1fr';
        }
        return {
            'display': 'grid',
            'grid-template-columns': gridTemplateColumns,
            'align-items': 'stretch',
            'width': `${320 * this.numCuts}px`
        };
    }
}