import { ChangeDetectionStrategy, Component, Input, OnInit } from "@angular/core";
import { UntypedFormArray } from "@angular/forms";
import { DopYieldContractCommodityForage } from "src/app/conversion/models-yield";
import { NgIf, NgStyle, NgFor, DecimalPipe } from "@angular/common";
import { MatTooltip } from "@angular/material/tooltip";
import { MatIcon } from "@angular/material/icon";
import { ForageDopCommodityComponent } from "../commodity/forage-dop-commodity.component";

@Component({
    selector: 'forage-dop-commodity-list',
    templateUrl: './forage-dop-commodity-list.component.html',
    styleUrls: ['./forage-dop-commodity-list.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [NgIf, MatTooltip, MatIcon, NgStyle, NgFor, ForageDopCommodityComponent, DecimalPipe]
})
export class ForageDopCommodityListComponent {
    @Input() commodities: Array<DopYieldContractCommodityForage>;
    @Input() commoditiesFormArray: UntypedFormArray;
    @Input() decimalPrecision: number;
    @Input() isUnsaved: boolean;

    get totalFieldAcresTotal(): number {
        return this.commodities.reduce((acc, curr) => acc + curr.totalFieldAcres || 0, 0);
    }

    get harvestedAcresTotal(): number {
        return this.commodities.reduce((acc, curr) => acc + curr.harvestedAcres || 0, 0);
    }
}