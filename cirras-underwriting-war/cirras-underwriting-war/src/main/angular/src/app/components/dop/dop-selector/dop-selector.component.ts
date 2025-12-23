import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { UwContract } from 'src/app/conversion/models';
import { DopYieldContract, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { ErrorState, LoadState } from 'src/app/store/application/application.state';
import { ResourcesRoutes } from 'src/app/utils/constants';

@Component({
  selector: 'dop-selector',
  templateUrl: './dop-selector.component.html',
  styleUrl: './dop-selector.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class DopSelectorComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;
  @Input() loadState: LoadState;
  @Input() errorState: ErrorState[];
  @Input() isUnsaved: boolean;

  constructor (protected router: Router) {
  }

  getDopType() {

    let currentUrlPath = ""  + this.router.url
    
    // GRAIN has its own DOP container and it's done slightly differently than Forage and Berries
    
    if (currentUrlPath.indexOf ( ResourcesRoutes.DOP_FORAGE) > -1 ) {
      return("forage" )
    }

    if (currentUrlPath.indexOf ( ResourcesRoutes.DOP_BERRIES) > -1 ) {
      return("berries" )
    }

  }

}
