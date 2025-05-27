import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { UserSettingsComponentModel } from './user-settings.component.model';
import { BaseComponent } from '../../common/base/base.component';
import { AddNewUserSettings, LoadUserSettings, UpdateUserSettings } from 'src/app/store/maintenance/maintenance.actions';
import { UserSetting } from 'src/app/conversion/models-maintenance';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { USER_SETTINGS_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';

@Component({
  selector: 'user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserSettingsComponent extends BaseComponent implements OnChanges {

  @Input() userSettings: UserSetting;
  @Input() isUnsaved: boolean;

  cropYearOptions = getCodeOptions("policy_crop_year");
  officeOptions = getCodeOptions("office"); 
  insurancePlanOptions = getCodeOptions("insurance_plan");

  initModels() {
    this.model = new UserSettingsComponentModel(this.sanitizer, this.fb);
    this.viewModel = new UserSettingsComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): UserSettingsComponentModel {
    return <UserSettingsComponentModel>this.viewModel;
  }

  loadPage() {
    // get the current user settings from the backend
    this.store.dispatch(LoadUserSettings())
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);

    if ( changes.userSettings && this.userSettings) {
      this.viewModel.formGroup.controls.selectedCropYear.setValue( this.userSettings.policySearchCropYear ? this.userSettings.policySearchCropYear.toString() : "" ) 
      this.viewModel.formGroup.controls.selectedInsurancePlanId.setValue( this.userSettings.policySearchInsurancePlanId ? this.userSettings.policySearchInsurancePlanId.toString() : "" )
      this.viewModel.formGroup.controls.selectedOfficeId.setValue( this.userSettings.policySearchOfficeId ? this.userSettings.policySearchOfficeId.toString() : "" )

      this.cdr.detectChanges()
    }
  }

  onCancel() {
    this.loadPage()
  }

  dropDownChanged(){
    this.viewModel.formGroup.controls.setDefaultSettings.setValue(false)
    this.store.dispatch(setFormStateUnsaved(USER_SETTINGS_COMPONENT_ID, true ));
  }

  onRestoreDefaultSettings() {

      this.viewModel.formGroup.controls.selectedCropYear.setValue("")
      this.viewModel.formGroup.controls.selectedInsurancePlanId.setValue("")
      this.viewModel.formGroup.controls.selectedOfficeId.setValue("")
    
      this.store.dispatch(setFormStateUnsaved(USER_SETTINGS_COMPONENT_ID, true ));
    }

  onSave() {
    
    // prepare the updated user settings
    let newUserSettings: UserSetting = JSON.parse(JSON.stringify(this.userSettings));

    newUserSettings.policySearchCropYear = (this.viewModel.formGroup.controls.selectedCropYear.value ? parseInt(this.viewModel.formGroup.controls.selectedCropYear.value) : null)
    newUserSettings.policySearchInsurancePlanId = parseInt(this.viewModel.formGroup.controls.selectedInsurancePlanId.value)
    newUserSettings.policySearchOfficeId = parseInt(this.viewModel.formGroup.controls.selectedOfficeId.value)

    if (this.userSettings.userSettingGuid) {
      this.store.dispatch(UpdateUserSettings( newUserSettings ))
    } else {
      // add new
      this.store.dispatch(AddNewUserSettings( newUserSettings ))
    }

    this.store.dispatch(setFormStateUnsaved(USER_SETTINGS_COMPONENT_ID, false ));
  }
    
}
