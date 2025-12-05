import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray, FormBuilder, FormGroup } from "@angular/forms";
import { AnnualField } from "src/app/conversion/models";

@Component({
    selector: 'forage-dop-field-list',
    templateUrl: './forage-dop-field-list.component.html',
    styleUrls: ['./forage-dop-field-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class ForageDopFieldListComponent {
    @Input() fields: Array<AnnualField>;
    @Input() fieldsFormArray: UntypedFormArray;

    get numCuts(): number {
        for (const field of this.fields) {
            for (const yieldField of field.dopYieldFieldForageList) {
                return yieldField.dopYieldFieldForageCuts.filter(yieldFieldCut => !yieldFieldCut.deletedByUserInd).length;
            }
        }
        return 0;
    }

    getArrayOfCutNumbers(): Array<number> {
        return Array(this.numCuts).fill(0).map((x, i) => i + 1);
    }

    setFieldHeaderStyles() {
        return {
            width: `${320 * this.numCuts + 1470}px`
        };
    }

    setWrapperStyles() {
        return {
            width: `${320 * this.numCuts + 1430}px`
        };
    }

    setFieldStyles() {
        return {
            width: `${320 * this.numCuts + 1410}px`
        };
    }
}