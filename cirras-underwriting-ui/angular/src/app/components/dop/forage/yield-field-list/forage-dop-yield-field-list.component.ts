import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { DopYieldFieldForage } from "src/app/conversion/models-yield";
import { NgFor } from "@angular/common";
import { ForageDopYieldFieldComponent } from "../yield-field/forage-dop-yield-field.component";

@Component({
    selector: 'forage-dop-yield-field-list',
    templateUrl: './forage-dop-yield-field-list.component.html',
    styleUrls: ['./forage-dop-yield-field-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgFor, ForageDopYieldFieldComponent]
})
export class ForageDopYieldFieldListComponent {
    @Input() yieldFields: Array<DopYieldFieldForage>;
    @Input() yieldFieldsFormArray: UntypedFormArray;
    @Input() fieldHiddenOnPrintoutInd: boolean;
}