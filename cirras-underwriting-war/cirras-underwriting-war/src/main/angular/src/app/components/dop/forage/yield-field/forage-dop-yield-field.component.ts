import { ChangeDetectionStrategy, Component, Input, OnInit, SimpleChanges } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { Store } from "@ngrx/store";
import { DopYieldFieldForage } from "src/app/conversion/models-yield";
import { SecurityUtilService } from "src/app/services/security-util.service";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";
import { getCodeOptions } from "src/app/utils/code-table-utils";

@Component({
    selector: 'forage-dop-yield-field',
    templateUrl: './forage-dop-yield-field.component.html',
    styleUrls: ['./forage-dop-yield-field.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ForageDopYieldFieldComponent implements OnInit {
    @Input() yieldField: DopYieldFieldForage;
    @Input() yieldFieldsFormArray: UntypedFormArray;
    @Input() fieldHiddenOnPrintoutInd: boolean;

    yieldFieldFormGroup: UntypedFormGroup;

    plantInsurabilityOptions = getCodeOptions("plant_insurability_type_code");  

    constructor(private fb: UntypedFormBuilder,
        private store: Store<RootState>,
        public securityUtilService: SecurityUtilService) {
    }

    ngOnInit(): void {
        this.refreshForm()
    }

    ngOnChanges(changes: SimpleChanges) {

        if (changes.yieldField && changes.yieldField.currentValue) {

            this.refreshForm()
        }
      }

    refreshForm() {
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
            plantInsurabilityTypeCode: this.yieldField.plantInsurabilityTypeCode,
            seedingYear: this.yieldField.seedingYear,
            seedingDate: this.yieldField.seedingDate,
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

    // get PlantInsurability description from the code tables
    getPlantInsurabilityDescription(plantInsurabilityTypeCode) {

        if (plantInsurabilityTypeCode) {
        
            return this.plantInsurabilityOptions.find( x => x.code == plantInsurabilityTypeCode).description

        } else {

            return "" 

        }
    }
}