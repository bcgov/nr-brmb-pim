import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { UntypedFormArray } from '@angular/forms';
import { VerifiedYieldAmendment } from 'src/app/conversion/models-yield';
import { CropCommodityList } from 'src/app/conversion/models';

@Component({
  selector: 'verified-yield-amendment-list',
  templateUrl: './verified-yield-amendment-list.component.html',
  styleUrl: './verified-yield-amendment-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VerifiedYieldAmendmentListComponent {
  @Input() amendments: Array<VerifiedYieldAmendment>;
  @Input() amendmentsFormArray: UntypedFormArray;
  @Input() isUnsaved: boolean;
  @Input() cropCommodityList: CropCommodityList

  addAmendment() {
    // TODO
  }
}
