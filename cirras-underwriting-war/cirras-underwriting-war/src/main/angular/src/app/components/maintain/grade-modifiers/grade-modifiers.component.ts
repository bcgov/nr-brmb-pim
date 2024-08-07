import { ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { GradeModifiersComponentModel } from './grade-modifiers.component.model';
import { GradeModifierList, GradeModifierTypeList, UnderwritingYearList } from 'src/app/conversion/models-maintenance';
import { loadGradeModifierTypes, loadGradeModifiers, loadUwYears, saveGradeModifiers } from 'src/app/store/maintenance/maintenance.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { makeNumberOnly } from 'src/app/utils';
import { CROP_COMMODITY_TYPE_CONST, CROP_COMMODITY_UNSPECIFIED, INSURANCE_PLAN } from 'src/app/utils/constants';
import { GradeModifierTypesContainer } from 'src/app/containers/maintenance/grade-modifier-types-container.component';
import { CropCommodityList } from 'src/app/conversion/models';
import { ClearCropCommodity, LoadCropCommodityList } from 'src/app/store/crop-commodity/crop-commodity.actions';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationStateService } from 'src/app/services/application-state.service';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { AppConfigService, TokenService } from '@wf1/core-ui';
import { ConnectionService } from 'ngx-connection-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Overlay } from '@angular/cdk/overlay';
import { HttpClient } from '@angular/common/http';
import { DecimalPipe } from '@angular/common';


@Component({
  selector: 'grade-modifiers',
  templateUrl: './grade-modifiers.component.html',
  styleUrls: ['./grade-modifiers.component.scss']
})
export class GradeModifiersComponent extends BaseComponent implements OnChanges  {

  @Input() underwritingYears: UnderwritingYearList
  @Input() gradeModifierList: GradeModifierList   // TODO: Reconcile with yield model.
  @Input() cropCommodityList: CropCommodityList
  @Input() gradeModifierTypeList: GradeModifierTypeList

  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    protected store: Store<RootState>,
    protected fb: FormBuilder,
    protected dialog: MatDialog,
    protected applicationStateService: ApplicationStateService,
    public securityUtilService: SecurityUtilService,                
    protected tokenService: TokenService,
    protected connectionService: ConnectionService,
    protected snackbarService: MatSnackBar,
    protected overlay: Overlay,
    protected cdr: ChangeDetectorRef,
    protected appConfigService: AppConfigService,
    protected http: HttpClient,
    protected titleService: Title,
    protected decimalPipe: DecimalPipe) {
    super(router, route, sanitizer, store, fb, dialog, applicationStateService, securityUtilService, tokenService, connectionService, snackbarService, overlay, cdr, appConfigService, http, titleService, decimalPipe);
  }

  hasDataChanged = false;
  uwYearOptions = [];
  cropCommodityOptions = [];
  gradeModifierTypeOptions = [];

  gradeModifierTypePopupIsOpen = false;

  etagForRollover = ""

  decimalPrecision: number = 3;

  selectedCropYear = new Date().getFullYear()
  selectedCropCommodityId = CROP_COMMODITY_UNSPECIFIED.ID

  initModels() {
    this.viewModel = new GradeModifiersComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): GradeModifiersComponentModel  { //
    return <GradeModifiersComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  loadPage() {
    let defaultCropYear = new Date().getFullYear();
    let defaultCropCommodityId = CROP_COMMODITY_UNSPECIFIED.ID

    this.loadUwCropYear()
    this.loadGrainCommodities(defaultCropYear)
    this.loadGradeModifierTypeList(defaultCropYear)
    this.loadGradeModifiersForCropYear(defaultCropYear, defaultCropCommodityId)
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

    if (changes.gradeModifierList) {

      // pre-fill the form
      let frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray
      frmGradeModifiers.clear()

      let cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

      if (this.gradeModifierList && this.gradeModifierList.cropYear == cropYear) {
        // Clear etag saved for rollover, if any.
        this.etagForRollover = ""
      }

      if ( this.gradeModifierList && this.gradeModifierList.collection && this.gradeModifierList.collection.length > 0) {

        if (this.gradeModifierList.cropYear == cropYear - 1) {

          // Rollover
          this.gradeModifierList.collection.forEach (gm => {

            let doRolloverRecord: boolean = true
            if (self.gradeModifierTypeList && self.gradeModifierTypeList.collection) {

              // If grade modifier type is not found, then it must have expired last year, so do not roll it over.
              let gmtc = self.gradeModifierTypeList.collection.find(el => el.gradeModifierTypeCode == gm.gradeModifierTypeCode)
              if (!gmtc) {
                doRolloverRecord = false
              }
            }

            if ( doRolloverRecord ) {
              frmGradeModifiers.push( this.fb.group( {  
                gradeModifierGuid:              [ null ],
                cropCommodityId:                [ gm.cropCommodityId ],
                commodityName:                  [ "" ],                  // Initially empty. Will be set later from cropCommodityList.
                cropYear:                       [ cropYear ],            // Set to selected crop year
                gradeModifierTypeCode:          [ gm.gradeModifierTypeCode ],
                gradeModifierValue:             [ 1 ],
                deleteAllowedInd:               [ true ],
                deletedByUserInd:               [ false ],
                addedByUserInd:                 [ false ]  // Set to false because only grade modifier value should be editable.
              } ) )
            }
          })

        } else {

          this.gradeModifierList.collection.forEach (gm => {

            frmGradeModifiers.push( this.fb.group( {  
              gradeModifierGuid:              [ gm.gradeModifierGuid ],
              cropCommodityId:                [ gm.cropCommodityId ],
              commodityName:                  [ "" ],                  // Initially empty. Will be set later from cropCommodityList.
              cropYear:                       [ gm.cropYear ],
              gradeModifierTypeCode:          [gm.gradeModifierTypeCode], 
              gradeModifierValue:             [gm.gradeModifierValue], 
              deleteAllowedInd:               [ gm.deleteAllowedInd ],
              deletedByUserInd:               [ false ],
              addedByUserInd:                 [ false ]
            } ) )
          })

        }
        this.setCommodityNames()
      }

    }

    if (changes.cropCommodityList && this.cropCommodityList && this.cropCommodityList.collection) {
      // clear the crop options
      this.cropCommodityOptions = []

      // add an empty crop commodity
      this.cropCommodityOptions.push ({
        commodityName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID
      })

      this.cropCommodityList.collection.forEach( ccm => self.cropCommodityOptions.push 
        ({
          commodityName: ccm.commodityName,
          cropCommodityId: ccm.cropCommodityId
        })
      )

      this.selectedCropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value

      this.setCommodityNames()
    }

    // The Grade Modifier Type pop-up reloads gradeModifierTypeList without the crop year filter. But that can be ignored, as it will be reloaded 
    // again with the crop year filter after the pop-up is closed.
    if ( changes.gradeModifierTypeList && this.gradeModifierTypeList && this.gradeModifierTypeList.collection && !this.gradeModifierTypePopupIsOpen ) {
  
      // update the grade modifier type dropdown
      let updatedGradeModifierTypeOptions = []

      // add an empty grade modifier type
      updatedGradeModifierTypeOptions.push ({
        gradeModifierTypeCode: ""
      })

      this.gradeModifierTypeList.collection.forEach (x => 
          updatedGradeModifierTypeOptions.push ({
              gradeModifierTypeCode: x.gradeModifierTypeCode
          })
      )

      this.gradeModifierTypeOptions = updatedGradeModifierTypeOptions
    }

    this.viewModel.formGroup.valueChanges.subscribe(val => {this.isMyFormDirty()})
  }

  uwYearsChange(event) {

    if (event.value) {

      let cropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value
      let doChangeYear: boolean = false

      if (this.hasDataChanged) {
        if (confirm("There are unsaved changes on the page which will be lost. Do you still wish to proceed? ")) {
          doChangeYear = true
        }
      } else {
        doChangeYear = true
      }

      if ( doChangeYear ) {
        this.selectedCropYear = event.value
        this.loadGrainCommodities(event.value)
        this.loadGradeModifierTypeList(event.value)      
        this.loadGradeModifiersForCropYear(event.value, cropCommodityId)

        this.hasDataChanged = false   
        this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false))
      } else {
        // Set drop-down back to old value.
        this.viewModel.formGroup.controls.selectedCropYear.setValue(this.selectedCropYear)
      }
    }
  }

  selectedCropCommodityIdChange(event) {

    let cropYear = this.viewModel.formGroup.controls.selectedCropYear.value

    if (this.hasDataChanged) {
      if (confirm("There are unsaved changes on the page which will be lost. Do you still wish to proceed? ")) {
        this.selectedCropCommodityId = event.value
        this.loadGradeModifiersForCropYear( cropYear, event.value )
        this.hasDataChanged = false   
        this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false))
    
      } else {
        // Set drop-down back to old value.
        this.viewModel.formGroup.controls.selectedCropCommodityId.setValue(this.selectedCropCommodityId)
      }

    } else {
      this.selectedCropCommodityId = event.value
      this.loadGradeModifiersForCropYear( cropYear, event.value )
      this.hasDataChanged = false   
      this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false))
    }

  }

  loadUwCropYear() {

    if (!this.underwritingYears) {
      this.store.dispatch(loadUwYears(MAINTENANCE_COMPONENT_ID))  
    }

  }

  loadGradeModifiersForCropYear(cropYear, cropCommodityId) {

    let cropCommodityIdStr = null
    if ( cropCommodityId && cropCommodityId != "") {
      cropCommodityIdStr = cropCommodityId.toString()
    }

    this.store.dispatch(loadGradeModifiers(MAINTENANCE_COMPONENT_ID, cropYear.toString(), INSURANCE_PLAN.GRAIN.toString(), cropCommodityIdStr ))

  }

  loadGrainCommodities(cropYear) {

    // Clear existing commodity list first.
    this.store.dispatch(ClearCropCommodity())

    this.store.dispatch(LoadCropCommodityList(MAINTENANCE_COMPONENT_ID,
        INSURANCE_PLAN.GRAIN.toString(),
        cropYear.toString(),
        CROP_COMMODITY_TYPE_CONST.YIELD,
        "N")
      )

  }

  loadGradeModifierTypeList(cropYear) {

    this.store.dispatch(loadGradeModifierTypes(MAINTENANCE_COMPONENT_ID, cropYear))
  }

  setCommodityNames() {

    if ( this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length > 0 ) { 

      const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray

      var self = this
      frmGradeModifiers.controls.forEach( function(frmGM : FormArray) {

        // find the commodity and get its name.
        if ( frmGM.value.cropCommodityId && frmGM.value.cropCommodityId != "" && (!frmGM.value.commodityName || frmGM.value.commodityName == "") ) { 
          let cropCommodity = self.cropCommodityList.collection.find(el => el.cropCommodityId == frmGM.value.cropCommodityId)

          if (cropCommodity) { 
            frmGM.controls['commodityName'].setValue(cropCommodity.commodityName)
          } 
        }
      })
    }
  }

  onSave() {

    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updated grade modifiers
    const newGradeModifiers: GradeModifierList = this.getUpdatedGradeModifiers()

    // save
    let cropYear = this.viewModel.formGroup.controls.selectedCropYear.value
    let cropCommodityId = null

    if (this.viewModel.formGroup.controls.selectedCropCommodityId.value && this.viewModel.formGroup.controls.selectedCropCommodityId.value != "") {
      cropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value
    }

    this.store.dispatch(saveGradeModifiers(MAINTENANCE_COMPONENT_ID, cropYear, INSURANCE_PLAN.GRAIN.toString(), cropCommodityId, newGradeModifiers))
    
    this.hasDataChanged = false   

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }

  isFormValid() {

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray
    let selectedCropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value

    let rowCount = 0
    for (let i = 0; i < frmGradeModifiers.controls.length; i++) {

      let frmGM = frmGradeModifiers.controls[i] as FormArray

      if ( !(frmGM.value.deletedByUserInd == true && (frmGM.value.gradeModifierGuid == null || frmGM.value.gradeModifierGuid == "")) ) {
        rowCount++
      }

      // Validate non-deleted rows.
      if (!(frmGM.value.deletedByUserInd == true)) {

        // commodity should not be empty, and should match selected commodity, if any.
        if (!frmGM.value.cropCommodityId) {
          alert ("Commodity cannot be empty.")
          return false
        } else if (selectedCropCommodityId && selectedCropCommodityId != "" &&  selectedCropCommodityId != frmGM.value.cropCommodityId ) {
          alert ("Commodity does not match selected Commodity for this page")
          return false
        }

        // grade modifier type should not be empty
        if (!frmGM.value.gradeModifierTypeCode) {
          alert ("Grade Modifier Type cannot be empty.")
          return false
        }

        // grade modifier value must be between 0 and 1
        if (frmGM.value.gradeModifierValue == null) {
          alert ("Grade Modifier Value cannot be empty.")
          return false
        } else {
          let acresFloat = parseFloat(frmGM.value.gradeModifierValue)
    
          if (isNaN(acresFloat) || acresFloat < 0 || acresFloat > 1 ) {
            alert("Grade Modifier Value must be a valid number between 0 and 1" )
            return false
          }
        }
        
        // must be unique
        for (let j = i + 1; j < frmGradeModifiers.controls.length; j++) {

          if ( !(frmGradeModifiers.controls[j].value.deletedByUserInd == true) &&
               frmGM.value.cropCommodityId == frmGradeModifiers.controls[j].value.cropCommodityId && 
              frmGM.value.gradeModifierTypeCode == frmGradeModifiers.controls[j].value.gradeModifierTypeCode ) {

                alert ("There is more than one row with commodity " + frmGM.value.commodityName + " and " + frmGM.value.gradeModifierTypeCode)
                return false
          }
        }
      }
    }

    if ( rowCount == 0 ) {
      alert("There are no changes to save.")
      return false
    }

    return true
  }

  getUpdatedGradeModifiers() {

    let cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)

    let updatedGradeModifiers : GradeModifierList = null;
  
    if (this.gradeModifierList.cropYear == cropYear - 1) {
      // Grade Modifiers were rolled-over from previous crop year.
      // So create a new GradeModifierList object for the current crop year.
      updatedGradeModifiers = {
        links: null,
        collection: [],
        etag: this.etagForRollover,
        type: null,
        cropYear: cropYear
      }
    } else {
      //make a deep copy
      updatedGradeModifiers = JSON.parse(JSON.stringify(this.gradeModifierList));
    }

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray

    var self = this
    frmGradeModifiers.controls.forEach( function(frmGM : FormArray) {

      // Ignore rows that were added then deleted without saving.
      if ( !(frmGM.value.deletedByUserInd == true && (frmGM.value.gradeModifierGuid == null || frmGM.value.gradeModifierGuid == "")) ) {

        let origGradeModifier = null

        if (frmGM.value.gradeModifierGuid) {
          origGradeModifier = updatedGradeModifiers.collection.find(el => el.gradeModifierGuid == frmGM.value.gradeModifierGuid)
        }

        if (origGradeModifier) { 
          // update
          origGradeModifier.gradeModifierValue = frmGM.value.gradeModifierValue
          origGradeModifier.deletedByUserInd = frmGM.value.deletedByUserInd
        } else {
          // add new
          updatedGradeModifiers.collection.push({
            gradeModifierGuid:              null,
            cropCommodityId:                frmGM.value.cropCommodityId,
            cropYear:                         self.viewModel.formGroup.controls.selectedCropYear.value,
            gradeModifierTypeCode:         frmGM.value.gradeModifierTypeCode,
            gradeModifierValue:        frmGM.value.gradeModifierValue,
            deletedByUserInd:                 false,
            type: null
          })
        }
      }
    })

    return updatedGradeModifiers
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.gradeModifierList) return false;

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray

    for (let i = 0; i < frmGradeModifiers.controls.length; i++) {

      let frmGM = frmGradeModifiers.controls[i] as FormArray

      if ( frmGM.value.addedByUserInd == true || !frmGM.value.gradeModifierGuid) {
        return true
      }

      let originalGM = this.gradeModifierList.collection.find( gm => gm.gradeModifierGuid == frmGM.value.gradeModifierGuid)

      if (originalGM) {
        
        if ( frmGM.value.deletedByUserInd == true) {
          return true
        }

        if ( originalGM.gradeModifierValue != frmGM.value.gradeModifierValue ) {
          return true
        }
      }
    }


    return false
  }

  onCancel() {

    // reload grade modifiers
    let cropYear = this.viewModel.formGroup.controls.selectedCropYear.value
    let cropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value
    this.loadGradeModifiersForCropYear(cropYear, cropCommodityId)

    this.hasDataChanged = false   
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false));
  }

  onAddGradeModifier() {

    let cropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value
    let commodityName = CROP_COMMODITY_UNSPECIFIED.NAME

    if ( this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length > 0 ) { 

      // find the commodity and get its name.
      if (cropCommodityId && cropCommodityId != "") { 
        let cropCommodity = this.cropCommodityList.collection.find(el => el.cropCommodityId == cropCommodityId)

        if (cropCommodity) { 
          commodityName = cropCommodity.commodityName
        } 
      }
    }

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray

    frmGradeModifiers.push (this.fb.group({
      gradeModifierGuid:              [ null ],
      cropCommodityId:                [ cropCommodityId ],
      commodityName:                  [ commodityName ],
      cropYear:                       [ this.viewModel.formGroup.controls.selectedCropYear.value ],
      gradeModifierTypeCode:          [ "" ],  
      gradeModifierValue:             [ 1 ], 
      deleteAllowedInd:               [ true ],
      deletedByUserInd:               [ false ],
      addedByUserInd:                 [ true ]
    }))

  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  validateGradeModifierValue(rowIndex) {

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray
    const frmGM = frmGradeModifiers.controls[rowIndex] as FormArray
    const gradeModifierValueCtl = frmGM.controls['gradeModifierValue']

    let acres = gradeModifierValueCtl.value

    if (acres == null) {
      alert("Grade Modifier Value must be a valid number between 0 and 1" )
    } else {

      let acresFloat = parseFloat(acres);

      if (isNaN(acresFloat) || acresFloat < 0 || acresFloat > 1 ) {
        alert("Grade Modifier Value must be a valid number between 0 and 1" )
      } else {

        if (acresFloat % 1 == 0 ) {
          // return integer if it's an integer, no zeros after the decimal point
          gradeModifierValueCtl.setValue(parseInt(acres))
        } else {      
          gradeModifierValueCtl.setValue(acresFloat.toFixed(this.decimalPrecision))
        }
      }
    }
  }


  onRollover() {

    this.etagForRollover = ""

    if (this.gradeModifierList && this.gradeModifierList.etag) {
      // the back end requires the original etag when saving the rolled over data
      // this is only required on roll over
      this.etagForRollover = this.gradeModifierList.etag
    }
    
    const cropYear = parseInt(this.viewModel.formGroup.controls.selectedCropYear.value)
    let cropCommodityId = this.viewModel.formGroup.controls.selectedCropCommodityId.value

    this.loadGradeModifiersForCropYear( cropYear - 1, cropCommodityId )

  }

  onDeleteGradeModifier(rowIndex) {

    const frmGradeModifiers: FormArray = this.viewModel.formGroup.controls.gradeModifiers as FormArray
    const frmGM = frmGradeModifiers.controls[rowIndex] as FormArray

    if (frmGM.value.deleteAllowedInd == true) {
      frmGM.controls['deletedByUserInd'].setValue(true)
      this.isMyFormDirty()
    } else {
      alert("Cannot delete Grade Modifier. It is used by one or more DOP")
    }

  }

  onMaintainGradeModifierTypes() {

    let dataToSend = {
      cropYear: this.viewModel.formGroup.controls.selectedCropYear.value
    }

    const dialogRef = this.dialog.open(GradeModifierTypesContainer, {
      width: '650px',
      data: dataToSend
    });

    this.gradeModifierTypePopupIsOpen = true;

    dialogRef.afterClosed().subscribe(result => {

      this.gradeModifierTypePopupIsOpen = false;

      // Always reload because the crop year filter needs to be re-applied.
      this.loadGradeModifierTypeList(this.viewModel.formGroup.controls.selectedCropYear.value)

    });

  }

}
