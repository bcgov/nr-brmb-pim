import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { SeedingDeadlinesComponentModel } from './seeding-deadlines.component.model';
import { SeedingDeadlineList, UnderwritingYearList } from 'src/app/conversion/models-maintenance';
import { loadSeedingDeadlines, loadUwYears, saveSeedingDeadlines } from 'src/app/store/maintenance/maintenance.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { areDatesNotEqual, setHttpHeaders } from 'src/app/utils';
import {etagFixer} from 'src/app/utils/etagFixer';
import { INSURANCE_PLAN } from 'src/app/utils/constants';
import { CommodityTypeCodeListRsrc } from '@cirras/cirras-underwriting-api';


@Component({
  selector: 'seeding-deadlines',
  templateUrl: './seeding-deadlines.component.html',
  styleUrls: ['./seeding-deadlines.component.scss']
})
export class SeedingDeadlinesComponent extends BaseComponent implements OnChanges  {

  @Input() underwritingYears: UnderwritingYearList
  @Input() seedingDeadlineList: SeedingDeadlineList

  hasDataChanged = false;
  uwYearOptions = [];
  commodityTypeCodeList = [];
  filteredCommodityTypeCodeList = [];

  etag = ""

  selectedCropYear = ""

  initModels() {
    this.viewModel = new SeedingDeadlinesComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): SeedingDeadlinesComponentModel  { //
    return <SeedingDeadlinesComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  loadPage() {
    this.loadUwCropYear()
    this.loadSeedingDeadlinesForCropYear( new Date().getFullYear() )
    this.getAllCommodityTypeCodes()
  }
  

  ngOnChanges(changes: SimpleChanges): void {

    var self = this;
    
    if ( changes.underwritingYears && this.underwritingYears && this.underwritingYears.collection && this.underwritingYears.collection.length > 0 ) {
      // populate the crop year dropdown
      self.uwYearOptions = []

      this.underwritingYears.collection.forEach (x => 
        self.uwYearOptions.push ({
          cropYear: x.cropYear
        }))

        this.selectedCropYear = this.viewModel.formGroup.controls.selectedCropYear.value
    }

    if (changes.seedingDeadlineList) {
      // pre-fill the form
      
      let frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
      frmSeedingDeadlines.clear()

      if ( this.seedingDeadlineList && this.seedingDeadlineList.collection && this.seedingDeadlineList.collection.length > 0) {

        this.seedingDeadlineList.collection.forEach (sd => {

          frmSeedingDeadlines.push( this.fb.group( {  
            seedingDeadlineGuid:              [ sd.seedingDeadlineGuid ],
            commodityTypeCode:                [ sd.commodityTypeCode ],
            cropYear:                         [ sd.cropYear ],
            fullCoverageDeadlineDate:         new FormControl(new Date(sd.fullCoverageDeadlineDate )), 
            finalCoverageDeadlineDate:        new FormControl(new Date(sd.finalCoverageDeadlineDate )),
            fullCoverageDeadlineDateDefault:  new FormControl(new Date(sd.fullCoverageDeadlineDateDefault )),
            finalCoverageDeadlineDateDefault: new FormControl(new Date(sd.finalCoverageDeadlineDateDefault )),
            deletedByUserInd:                 [ sd.deletedByUserInd ],
            addedByUserInd:                    [ false ], 
            fullCoverageDeadlineDateEditedInd: [ false ], // flags to keep track whether the user has edited that date or it needs to be set to the default date on Save
            finalCoverageDeadlineDateEditedInd:[ false ]
          } ) )
        })

      }

      this.checkForRolloverAndUpdateDates()
    }

    this.viewModel.formGroup.valueChanges.subscribe(val => {this.isMyFormDirty()})
  }

  uwYearsChange(event) {

    if (event.value) {

      if (this.hasDataChanged) {
        if (confirm("There are unsaved changes on the page which will be lost. Do you still wish to proceed? ")) {
          this.loadSeedingDeadlinesForCropYear( event.value )
          this.selectedCropYear = event.value
        } else {
          
          //event.preventDefault() -> doesn't work
          // we are setting up the crop year to what it was originally, before the selection was changed
          this.viewModel.formGroup.controls.selectedCropYear.setValue(this.selectedCropYear)
        }
      } else {
        this.loadSeedingDeadlinesForCropYear( event.value )
        this.selectedCropYear = event.value
      }
    }
  }
  

  loadUwCropYear() {

    if (!this.underwritingYears) {
      this.store.dispatch(loadUwYears(MAINTENANCE_COMPONENT_ID))  
    }

  }

  loadSeedingDeadlinesForCropYear(cropYear) {

    if (cropYear) {
      this.store.dispatch(loadSeedingDeadlines(MAINTENANCE_COMPONENT_ID, cropYear.toString() ))
    }

  }

  onSave() {

    this.setCurrentDatesToDefault()   

    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updated seeding deadlines 
    const newSeedingDeadlines: SeedingDeadlineList = this.getUpdatedSeedingDeadlines()

    // save
    let cropYear = this.viewModel.formGroup.controls.selectedCropYear.value
    this.store.dispatch(saveSeedingDeadlines(MAINTENANCE_COMPONENT_ID, cropYear, newSeedingDeadlines))
    
    this.hasDataChanged = false   

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }

  isFormValid() {
    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
    const cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

    for (let i = 0; i < frmSeedingDeadlines.controls.length; i++) {

      let frmSD = frmSeedingDeadlines.controls[i] as FormArray

      // commodity type code should not be empty
      if (!frmSeedingDeadlines.controls[i].value.commodityTypeCode) {
        alert ("Commodity type should not be empty.")
        return false
      }

      //get the dates 
      let fullCoverageDeadlineDate = new Date(frmSD.value.fullCoverageDeadlineDate)
      let finalCoverageDeadlineDate = new Date(frmSD.value.finalCoverageDeadlineDate)
      let fullCoverageDeadlineDateDefault = new Date(frmSD.value.fullCoverageDeadlineDateDefault)
      let finalCoverageDeadlineDateDefault = new Date(frmSD.value.finalCoverageDeadlineDateDefault)

      if (!frmSD.value.fullCoverageDeadlineDateDefault || ! frmSD.value.finalCoverageDeadlineDateDefault) {
        // default values should not be empty
        alert("The Policy Wording Deadlines for commodity type " + frmSD.value.commodityTypeCode + " shoud not be empty.")
        return false
      }

      if (!frmSD.value.fullCoverageDeadlineDate || ! frmSD.value.finalCoverageDeadlineDate) {
        // the user should not delete the current year dates
        alert("The Current Year Deadlines for commodity type " + frmSD.value.commodityTypeCode + " shoud not be empty.")
        return false
      }

      if (fullCoverageDeadlineDate > finalCoverageDeadlineDate ||
          fullCoverageDeadlineDateDefault > finalCoverageDeadlineDateDefault ) {

          alert("The Full Coverage Deadline has to be no later than the Final Coverage Deadline for commodity type " + frmSD.value.commodityTypeCode + ".")
        return false
      }

      // The deadline year has to be consistent with the crop year
      if (fullCoverageDeadlineDate.getFullYear() != cropYear) {
        alert("The year of the Current Year Full Coverage Date for commodity type " + frmSD.value.commodityTypeCode + " has to match the selected crop year.")
        return false
      }

      if (finalCoverageDeadlineDate.getFullYear() != cropYear) {
            alert("The year of the Current Year Final Coverage Date for commodity type " + frmSD.value.commodityTypeCode + " has to match the selected crop year.")
            return false
      }

      if (fullCoverageDeadlineDateDefault.getFullYear() != cropYear) {
        alert("The year of the Policy Wording Full Coverage Date for commodity type " + frmSD.value.commodityTypeCode + " has to match the selected crop year.")
        return false
      }

      if (finalCoverageDeadlineDateDefault.getFullYear() != cropYear) {
        alert("The year of the Policy Wording Final Coverage Date for commodity type " + frmSD.value.commodityTypeCode + " has to match the selected crop year.")
        return false
      }

      // commodity type code must be unique
      for (let j = i + 1; j < frmSeedingDeadlines.controls.length; j++) {

        if ( frmSeedingDeadlines.controls[i].value.commodityTypeCode == frmSeedingDeadlines.controls[j].value.commodityTypeCode) {
          alert ("There is more than one row with commodity type " + frmSeedingDeadlines.controls[j].value.commodityTypeCode)
          return false
        }
      }
    }

    return true
  }

  setCurrentDatesToDefault() {

     // for the new rows set the current dates to the default dates wherever the user hasn't changed them 

    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
    for (let i = 0; i < frmSeedingDeadlines.controls.length; i++) {

      let frmSD = frmSeedingDeadlines.controls[i] as FormArray

      if (!frmSD.value.seedingDeadlineGuid) {
        
        if ( frmSD.value.fullCoverageDeadlineDateEditedInd == false ) {
          frmSD.value.fullCoverageDeadlineDate = frmSD.value.fullCoverageDeadlineDateDefault
        }

        if ( frmSD.value.finalCoverageDeadlineDateEditedInd == false ) {
          frmSD.value.finalCoverageDeadlineDate = frmSD.value.finalCoverageDeadlineDateDefault
        }
      }
    }
  }

  getUpdatedSeedingDeadlines() {

    //make a deep copy
    let updatedSeedingDeadlines : SeedingDeadlineList = JSON.parse(JSON.stringify(this.seedingDeadlineList));

    // required only for rollover -> set the original etag
    if (this.etag !== "") {
      updatedSeedingDeadlines.etag = this.etag
      this.etag = ""
    }

    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray

    var self = this
    frmSeedingDeadlines.controls.forEach( function(frmSD : FormArray) {

      // find the corresponding field in updatedDopYieldContract object
      let origSeedingDeadline = updatedSeedingDeadlines.collection.find( el => el.commodityTypeCode == frmSD.value.commodityTypeCode)
      
      if (origSeedingDeadline) { 
        // update
        origSeedingDeadline.cropYear = frmSD.value.cropYear 
        origSeedingDeadline.seedingDeadlineGuid = frmSD.value.seedingDeadlineGuid // the guid might be set to null if it saves from rollover
        origSeedingDeadline.finalCoverageDeadlineDate = frmSD.value.finalCoverageDeadlineDate
        origSeedingDeadline.finalCoverageDeadlineDateDefault = frmSD.value.finalCoverageDeadlineDateDefault
        origSeedingDeadline.fullCoverageDeadlineDate = frmSD.value.fullCoverageDeadlineDate
        origSeedingDeadline.fullCoverageDeadlineDateDefault = frmSD.value.fullCoverageDeadlineDateDefault
        origSeedingDeadline.deletedByUserInd = frmSD.value.deletedByUserInd
      } else {
        // add new
        updatedSeedingDeadlines.collection.push({
          seedingDeadlineGuid:              null,
          commodityTypeCode:                frmSD.value.commodityTypeCode,
          cropYear:                         self.viewModel.formGroup.controls.selectedCropYear.value,
          fullCoverageDeadlineDate:         frmSD.value.fullCoverageDeadlineDate,
          finalCoverageDeadlineDate:        frmSD.value.finalCoverageDeadlineDate,
          fullCoverageDeadlineDateDefault:  frmSD.value.fullCoverageDeadlineDateDefault,
          finalCoverageDeadlineDateDefault: frmSD.value.finalCoverageDeadlineDateDefault,
          deletedByUserInd:                 frmSD.value.deletedByUserInd
        })
      }
    })

    return updatedSeedingDeadlines
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.seedingDeadlineList) return false;

    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray

    for (let i = 0; i < frmSeedingDeadlines.controls.length; i++) {

      let frmSD = frmSeedingDeadlines.controls[i] as FormArray

      if ( frmSD.value.addedByUserInd == true) {
        return true
      }

      let originalSD = this.seedingDeadlineList.collection.find( sd => sd.seedingDeadlineGuid == frmSD.value.seedingDeadlineGuid)

      if (originalSD) {
        
        if ( frmSD.value.deletedByUserInd == true) {
          return true
        }

        if ( areDatesNotEqual (originalSD.fullCoverageDeadlineDate, frmSD.value.fullCoverageDeadlineDate )	
        || areDatesNotEqual (originalSD.finalCoverageDeadlineDate, frmSD.value.finalCoverageDeadlineDate )	
        || areDatesNotEqual (originalSD.fullCoverageDeadlineDateDefault, frmSD.value.fullCoverageDeadlineDateDefault )
        || areDatesNotEqual (originalSD.finalCoverageDeadlineDateDefault, frmSD.value.finalCoverageDeadlineDateDefault )) {
          return true
        }
      }
    }


    return false
  }

  onCancel() {

    // reload seeding deadlines
    this.loadSeedingDeadlinesForCropYear(this.viewModel.formGroup.controls.selectedCropYear.value )

    this.hasDataChanged = false   
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false));
  }

  onAddSeedingDeadline() {

    // just filter out the existing commodity types from the list
    this.filterUsedCommodityTypeCodes()
  
    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray

    frmSeedingDeadlines.push (this.fb.group({
      seedingDeadlineGuid:              [],
      commodityTypeCode:                [],
      cropYear:                         [ this.viewModel.formGroup.controls.selectedCropYear.value ],
      fullCoverageDeadlineDate:         [],  
      finalCoverageDeadlineDate:        [], 
      fullCoverageDeadlineDateDefault:  [], 
      finalCoverageDeadlineDateDefault: [],
      deletedByUserInd:                 [ false ],
      addedByUserInd:                    [ true ],
      fullCoverageDeadlineDateEditedInd: [ false ], 
      finalCoverageDeadlineDateEditedInd: [ false ]
    }))
  }

  getAllCommodityTypeCodes() {

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url +"/commodityTypeCodes?insurancePlanId=" + INSURANCE_PLAN.GRAIN

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    this.commodityTypeCodeList = []

    return this.http.get(url,httpOptions).toPromise().then((data: CommodityTypeCodeListRsrc) => {
    
      if (data && data.collection && data.collection.length > 0 ) {
        data.collection.forEach(el => {
          
          this.commodityTypeCodeList.push(el.commodityTypeCode)
        })
      }
     })
  }

  filterUsedCommodityTypeCodes() {

    if (this.commodityTypeCodeList && this.commodityTypeCodeList.length > 0 ) {

      this.filteredCommodityTypeCodeList = []

      this.commodityTypeCodeList.forEach(x => {
        this.filteredCommodityTypeCodeList.push(x);
      });

      for (let j = 0; j < this.filteredCommodityTypeCodeList.length; j++) {

        for (let i = 0; i < this.seedingDeadlineList.collection.length; i++) {

          if  (this.filteredCommodityTypeCodeList[j] == this.seedingDeadlineList.collection[i].commodityTypeCode ) {
            this.filteredCommodityTypeCodeList.splice(j, 1)
            j--
          }
        }
      }
    }
  }

  areGridDatesNotEqual(a, b) {

    if (areDatesNotEqual (a,b)) {
      return true
    } else {
      return false
    }

  }

  shouldHighlightDate(rowIndex, datePickerNum) {

    const cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
    const frmSD = frmSeedingDeadlines.controls[rowIndex] as FormArray

    let frmDate = new Date()

    switch (datePickerNum) {
      case 1:
        if ( (new Date(frmSD.value.fullCoverageDeadlineDateDefault)) > (new Date(frmSD.value.finalCoverageDeadlineDateDefault)) ) {
          return true
        }
        frmDate = new Date(frmSD.value.fullCoverageDeadlineDateDefault)
        break

      case 2:
        if ( (new Date(frmSD.value.fullCoverageDeadlineDate)) > (new Date(frmSD.value.finalCoverageDeadlineDate)) ) {
          return true
        }

        frmDate = new Date(frmSD.value.fullCoverageDeadlineDate)
        break

      case 3:
        if ( (new Date(frmSD.value.fullCoverageDeadlineDateDefault)) > (new Date(frmSD.value.finalCoverageDeadlineDateDefault)) ) {
          return true
        }
        frmDate = new Date(frmSD.value.finalCoverageDeadlineDateDefault)
        break

      case 4:
        if ( (new Date(frmSD.value.fullCoverageDeadlineDate)) > (new Date(frmSD.value.finalCoverageDeadlineDate)) ) {
          return true
        }

        frmDate = new Date(frmSD.value.finalCoverageDeadlineDate)
        break

      default:
        return false
    }
    
    if (frmDate.getFullYear() != cropYear) {
      return true
    }
    

    return false
  }

  onCurrentYearDateEdited(rowIndex, datePickerNum) {
    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
    const frmSD = frmSeedingDeadlines.controls[rowIndex] as FormArray

    switch (datePickerNum) {
      case 1:
        frmSD.value.fullCoverageDeadlineDateEditedInd = true
        break
      case 2:
        frmSD.value.finalCoverageDeadlineDateEditedInd = true
        break
    }
  }


  onRollover() {

    if (this.seedingDeadlineList && this.seedingDeadlineList.etag) {
      // the back end requires the original etag when saving the rolled over data
      // this is only required on roll over
      this.etag = this.seedingDeadlineList.etag
    }
    
    const cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

    if (cropYear) {
      this.loadSeedingDeadlinesForCropYear( cropYear - 1 )
    }

  }

  checkForRolloverAndUpdateDates() {

    if (this.seedingDeadlineList && this.seedingDeadlineList.collection && this.seedingDeadlineList.collection.length > 0 ) {

      const cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

      let yr = (new Date(this.seedingDeadlineList.collection[0].fullCoverageDeadlineDateDefault)).getFullYear()

      if (yr == cropYear - 1) {
        // the rollover button was clicked
        // we have to update all dates on the form to show the current year 

        const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray

        for (let i = 0; i < frmSeedingDeadlines.controls.length; i++) {

          let frmSD = frmSeedingDeadlines.controls[i] as FormArray

          let originalSD = this.seedingDeadlineList.collection.find( sd => sd.seedingDeadlineGuid == frmSD.value.seedingDeadlineGuid)

          frmSD.controls['cropYear'].setValue(cropYear)
          frmSD.controls['seedingDeadlineGuid'].setValue(null)
          frmSD.controls['fullCoverageDeadlineDateDefault'].setValue( new Date (new Date(originalSD.fullCoverageDeadlineDateDefault).setFullYear(cropYear) ) )
          frmSD.controls['fullCoverageDeadlineDate'].setValue( frmSD.controls['fullCoverageDeadlineDateDefault'].value ) // same as the default dates
          frmSD.controls['finalCoverageDeadlineDateDefault'].setValue( new Date (new Date(originalSD.finalCoverageDeadlineDateDefault).setFullYear(cropYear) ) )
          frmSD.controls['finalCoverageDeadlineDate'].setValue( frmSD.controls['finalCoverageDeadlineDateDefault'].value ) // same as the default dates
          frmSD.controls['addedByUserInd'].setValue(false)

        }
        this.hasDataChanged = true
      }
    }
  }

  onDeleteSeedingDeadline(rowIndex) {

    const frmSeedingDeadlines: FormArray = this.viewModel.formGroup.controls.seedingDeadlines as FormArray
    const frmSD = frmSeedingDeadlines.controls[rowIndex] as FormArray

    frmSD.controls['deletedByUserInd'].setValue(true)

    this.isMyFormDirty()
  }
}
