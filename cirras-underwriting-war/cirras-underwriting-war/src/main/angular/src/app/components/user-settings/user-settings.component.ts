import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { UserSettingsComponentModel } from './user-settings.component.model';
import { BaseComponent } from '../common/base/base.component';

@Component({
  selector: 'cirras-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent extends BaseComponent implements OnChanges {

  cropYearOptions = getCodeOptions("policy_crop_year");
  officeOptions = getCodeOptions("office"); 
  insurancePlans = getCodeOptions("insurance_plan");

  selectedCropYear = ""
  
  initModels() {
    this.model = new UserSettingsComponentModel(this.sanitizer, this.fb);
    this.viewModel = new UserSettingsComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): UserSettingsComponentModel {
    return <UserSettingsComponentModel>this.viewModel;
  }

  loadPage() {
    // TODO: get the current user settings from the backend
  }

   ngOnChanges(changes: SimpleChanges) {
      super.ngOnChanges(changes);
   }

}
