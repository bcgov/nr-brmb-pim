import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { UserSettingsComponentModel } from './user-settings.component.model';
import { BaseComponent } from '../../common/base/base.component';
import { LoadUserSettings } from 'src/app/store/maintenance/maintenance.actions';
import { UserSetting } from 'src/app/conversion/models-maintenance';

@Component({
  selector: 'user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserSettingsComponent extends BaseComponent implements OnChanges {

  @Input() userSettings: UserSetting;

  cropYearOptions = getCodeOptions("policy_crop_year");
  officeOptions = getCodeOptions("office"); 
  insurancePlanOptions = getCodeOptions("insurance_plan");

  hasDataChanged = false

  
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
      this.viewModel.formGroup.controls.selectedCropYear.setValue(this.userSettings.policySearchCropYear.toString())
      this.viewModel.formGroup.controls.selectedInsurancePlanId.setValue(this.userSettings.policySearchInsurancePlanId.toString())
      this.viewModel.formGroup.controls.selectedOfficeId.setValue(this.userSettings.policySearchOfficeId.toString())

      this.cdr.detectChanges()
    }
  }

  onCancel() {
    this.loadPage()
  }

  dropDownChanged(){
    this.viewModel.formGroup.controls.setDefaultSettings.setValue(false)
    this.hasDataChanged = true
  }

    isChecked(event) {
  
      if ( event.checked ){
        this.viewModel.formGroup.controls.selectedCropYear.setValue("")
        this.viewModel.formGroup.controls.selectedInsurancePlanId.setValue("")
        this.viewModel.formGroup.controls.selectedOfficeId.setValue("")
      }
      this.hasDataChanged = true
    }

  onSave() {
    
    // prepare the updated user settings
    let newUserSettings: UserSetting = this.userSettings

    newUserSettings.policySearchCropYear = this.viewModel.formGroup.controls.selectedCropYear.value
    newUserSettings.policySearchInsurancePlanId = this.viewModel.formGroup.controls.selectedInsurancePlanId.value
    newUserSettings.policySearchOfficeId = this.viewModel.formGroup.controls.selecteOfficeId.value

    // TODO when create/update endpoints are ready
    // if (this.userSettings.userSettingGuid) {
    //   this.store.dispatch(UpdateDopYieldContract(DOP_COMPONENT_ID, newDopYieldContract, this.policyId))
    // } else {
    //   // add new
    //   this.store.dispatch(AddNewDopYieldContract(DOP_COMPONENT_ID, newDopYieldContract, this.policyId))
    // }

    this.hasDataChanged = false   
    // this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }
    
}
