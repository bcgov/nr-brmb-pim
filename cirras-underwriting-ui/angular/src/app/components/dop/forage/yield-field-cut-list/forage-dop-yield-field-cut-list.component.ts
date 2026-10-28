import { ChangeDetectionStrategy, Component, Input } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { DopYieldFieldForage, DopYieldFieldForageCut } from "src/app/conversion/models-yield";
import { NgFor, NgIf } from "@angular/common";
import { ForageDopYieldFieldCutComponent } from "../yield-field-cut/forage-dop-yield-field-cut.component";

@Component({
    selector: 'forage-dop-yield-field-cut-list',
    templateUrl: './forage-dop-yield-field-cut-list.component.html',
    styleUrls: ['./forage-dop-yield-field-cut-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgFor, NgIf, ForageDopYieldFieldCutComponent]
})
export class ForageDopYieldFieldCutListComponent {
    @Input() yieldField: DopYieldFieldForage;
    @Input() yieldFieldCuts: Array<DopYieldFieldForageCut>;
    @Input() yieldFieldCutsFormArray: UntypedFormArray;
}