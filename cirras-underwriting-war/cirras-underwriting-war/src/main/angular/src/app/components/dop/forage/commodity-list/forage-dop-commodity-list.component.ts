import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { FormArray } from "@angular/forms";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";

@Component({
    selector: 'forage-dop-commodity-list',
    templateUrl: './forage-dop-commodity-list.component.html',
    styleUrls: ['./forage-dop-commodity-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ForageDopCommodityListComponent {
    @Input() commodities: Array<DopYieldContractCommodityForage>;
    @Input() commoditiesFormArray: FormArray;
    @Input() decimalPrecision: number;
    @Input() isUnsaved: boolean;

    get totalFieldAcresTotal(): number {
        return this.commodities.reduce((acc, curr) => acc + curr.totalFieldAcres || 0, 0);
    }

    get harvestedAcresTotal(): number {
        return this.commodities.reduce((acc, curr) => acc + curr.harvestedAcres || 0, 0);
    }
}