import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { UnderwritingComment } from "@cirras/cirras-underwriting-api";
import { Store } from "@ngrx/store";
import { AnnualField } from "src/app/conversion/models";
import { RootState } from "src/app/store";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { DOP_COMPONENT_ID } from "src/app/store/dop/dop.state";

@Component({
    selector: 'forage-dop-field',
    templateUrl: './forage-dop-field.component.html',
    styleUrls: ['./forage-dop-field.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopFieldComponent implements OnInit {
    @Input() field: AnnualField;
    @Input() fieldsFormArray: UntypedFormArray;

    fieldFormGroup: UntypedFormGroup;

    constructor(private fb: UntypedFormBuilder,
        private dialog: MatDialog,
        private store: Store<RootState>) {
    }

    ngOnInit(): void {
        this.fieldFormGroup = this.fb.group({
            annualFieldDetailId: [this.field.annualFieldDetailId],
            displayOrder: [this.field.displayOrder],
            fieldId: [this.field.fieldId],
            fieldLabel: [this.field.fieldLabel],
            otherLegalDescription: [this.field.otherLegalDescription],
            dopYieldFieldForageList: this.fb.array([]),
            uwComments: [this.field.uwComments],
        });
        this.fieldsFormArray.push(this.fieldFormGroup);
    }

    hasComments(): boolean {
        const notDeletedComments = this.field.uwComments.filter(comment => !comment.deletedByUserInd);
        return notDeletedComments.length > 0;
    }

    onInventoryCommentsDone(uwComments: UnderwritingComment[]) {
        this.field.uwComments = uwComments;
        this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, true));
    }

    get numCuts(): number {
        for (const yieldField of this.field.dopYieldFieldForageList) {
            return yieldField.dopYieldFieldForageCuts.filter(yieldFieldCut => !yieldFieldCut.deletedByUserInd).length;
        }
        return 0;
    }

    setPlantingStyles() {
        return {
            'display': 'grid',
            'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
            'align-items': 'stretch',
            'width': `${320 * this.numCuts + 880}px`
        };
    }

    get fieldHiddenOnPrintoutInd(): boolean {
        let result = true;
        for (let yieldField of this.field.dopYieldFieldForageList) {
            result = result && yieldField.isHiddenOnPrintoutInd;
        }
        return result;
    }
}