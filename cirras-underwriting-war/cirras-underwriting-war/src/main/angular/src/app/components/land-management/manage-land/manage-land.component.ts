import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { BaseComponent } from '../../common/base/base.component';
import { ManageLandComponentModel } from './manage-land.component.model';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { LegalLand, RiskAreaList } from 'src/app/conversion/models';
import { MANAGE_LEGAL_LAND_COMPONENT_ID } from 'src/app/store/land-management/land-management.state';
import { addNewLegalLand, deleteLegalLand, getLegalLand, getRiskAreaList, updateLegalLand } from 'src/app/store/land-management/land-management.actions';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { areNotEqual, makeNumberOnly } from 'src/app/utils';
import { PRIMARY_LAND_IDENTIFIER_TYPE_CODE, PRIMARY_REFERENCE_TYPE_CODE } from 'src/app/utils/constants';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { LegalLandRiskArea } from '@cirras/cirras-underwriting-api';
import { DomSanitizer, Title } from '@angular/platform-browser';
import { Store } from '@ngrx/store';
import { RootState } from 'src/app/store';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationStateService } from 'src/app/services/application-state.service';
import { SecurityUtilService } from 'src/app/services/security-util.service';
import { AppConfigService, TokenService } from '@wf1/wfcc-core-lib';
import { ConnectionService } from 'ngx-connection-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Overlay } from '@angular/cdk/overlay';
import { HttpClient } from '@angular/common/http';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'manage-land',
  templateUrl: './manage-land.component.html',
  styleUrls: ['./manage-land.component.scss']
})
export class ManageLandComponent extends BaseComponent implements OnChanges {

  @Input() legalLand: LegalLand
  @Input() riskAreaList: RiskAreaList

  constructor(protected router: Router,
    protected route: ActivatedRoute,
    protected sanitizer: DomSanitizer,
    protected store: Store<RootState>,
    protected fb: UntypedFormBuilder,
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

  legalLandId: string
  
  landIdentifierTypeOptions = getCodeOptions("land_identifier_type_code");  
  primaryReferenceTypeOptions = getCodeOptions("primary_reference_type_code");  
  insurancePlansOptions = getCodeOptions("insurance_plan");
  riskAreaOptions = []; // array that contains all risk area options for each choice of plan 

  hasDataChanged = false
  validateIdentifier = true

  pidMask = "000-000-000"
  
  allPidHints = {
    PID:  "PID expects 9 digits (XXX-XXX-XXX)",
    PIN:  "PIN expects 7 digits (no separator)",
    LINC: "LINC expects 10 digits (XXXX-XXX-XXX)",
    IR:    "IR expects 9 digits (no separator)",
    OTHER: "OTHER expects up to 13 digits/characters (no separator)"
  }

  pidMaskHint = this.allPidHints.PID

  initModels() {
    this.viewModel = new ManageLandComponentModel(this.sanitizer, this.fb);
  }

  loadPage() {

    // add SHORT option in primaryReferenceTypeOptions 
    // TODO: ideally this should be fixed in the uw-api
    // SHORT option is not used for grain and forage 
    // and is a calculated value and mainly used for treefruits and grapes. 
    // The option exists in the database but it is inactive.

    this.primaryReferenceTypeOptions.push({
      code: "SHORT",
      description: "Short"
    })

    this.componentId = MANAGE_LEGAL_LAND_COMPONENT_ID;
    var self = this
    
    this.route.paramMap.subscribe(
      (params: ParamMap) => {

        self.legalLandId = params.get("legalLandId") ? params.get("legalLandId") : "";

          if (self.legalLandId) {
            self.store.dispatch(getLegalLand(self.componentId, self.legalLandId));
          }
        }
    );

    if (!this.riskAreaList) {
      // load the risk areas 
      this.store.dispatch(getRiskAreaList(this.componentId))
    }

  }

  ngOnChanges(changes: SimpleChanges) {
    // super.ngOnChanges(changes);

    if (changes.legalLand) {
        this.legalLand = changes.legalLand.currentValue;

        if ( this.legalLand && this.legalLand.legalLandId ) {
          this.legalLandId = this.legalLand.legalLandId.toString();
        } else {
          this.legalLandId = "";
        }

        this.viewModel.formGroup.controls.primaryReferenceTypeCode.setValue( ( this.legalLand && this.legalLand.primaryReferenceTypeCode) ? this.legalLand.primaryReferenceTypeCode : PRIMARY_REFERENCE_TYPE_CODE.DEFAULT )
        this.viewModel.formGroup.controls.primaryLandIdentifierTypeCode.setValue( ( this.legalLand && this.legalLand.primaryLandIdentifierTypeCode) ? this.legalLand.primaryLandIdentifierTypeCode : PRIMARY_LAND_IDENTIFIER_TYPE_CODE.DEFAULT )
        this.viewModel.formGroup.controls.primaryPropertyIdentifier.setValue( ( this.legalLand && this.legalLand.primaryPropertyIdentifier) ? this.legalLand.primaryPropertyIdentifier : ""  )
        this.viewModel.formGroup.controls.legalDescription.setValue( ( this.legalLand && this.legalLand.legalDescription) ? this.legalLand.legalDescription : "" )
        this.viewModel.formGroup.controls.otherDescription.setValue( ( this.legalLand && this.legalLand.otherDescription) ? this.legalLand.otherDescription : "" )
        this.viewModel.formGroup.controls.activeFromCropYear.setValue( ( this.legalLand && this.legalLand.activeFromCropYear) ? this.legalLand.activeFromCropYear : "" )
        this.viewModel.formGroup.controls.activeToCropYear.setValue ( ( this.legalLand && this.legalLand.activeToCropYear) ? this.legalLand.activeToCropYear : "")
    
        this.selectLandIdentifierType(this.viewModel.formGroup.controls.primaryLandIdentifierTypeCode.value)

        // populate the risk areas in the form
        let riskAreas: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray
        riskAreas.clear()

        if (this.legalLand && this.legalLand.riskAreas && this.legalLand.riskAreas.length > 0 ){
          this.legalLand.riskAreas.forEach( ra => this.addRiskArea( ra ) )
        }
      }

    if (changes.riskAreaList) {
      this.riskAreaList = changes.riskAreaList.currentValue
    }

    setTimeout(() => {
      this.cdr.detectChanges();
    });

    this.viewModel.formGroup.valueChanges.subscribe(val => {this.isMyFormDirty()})
  }

  addRiskArea(ra: LegalLandRiskArea) {
    
    let raForm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray

    raForm.push( this.fb.group( {
      riskAreaId:       [ ra.riskAreaId ],
      insurancePlanId:  [ ra.insurancePlanId ],
      riskAreaName:     [ ra.riskAreaName ],
      description:      [ ra.description ],
      legalLandId:      [ ra.legalLandId ],
      insurancePlanName:[ ra.insurancePlanName ],
      deletedByUserInd: [ ra.deletedByUserInd ],
      isNewRiskArea:    [ false ]
    } ) )
  }

  getViewModel(): ManageLandComponentModel  { //
      return <ManageLandComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  onCancel() {
    // reload the page
    if (this.legalLandId) {
      this.store.dispatch(getLegalLand(this.componentId, this.legalLandId));
    } else {
      const frmMain = this.viewModel.formGroup as UntypedFormGroup

      frmMain.controls.primaryReferenceTypeCode.setValue(PRIMARY_REFERENCE_TYPE_CODE.DEFAULT)
      frmMain.controls.primaryLandIdentifierTypeCode.setValue(PRIMARY_LAND_IDENTIFIER_TYPE_CODE.DEFAULT)

      frmMain.controls.primaryPropertyIdentifier.setValue("") 
      frmMain.controls.legalDescription.setValue("")
      frmMain.controls.otherDescription.setValue("") 
      frmMain.controls.activeFromCropYear.setValue("") 
      frmMain.controls.activeToCropYear.setValue("") 
      
      let riskAreas: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray
      riskAreas.clear()
      
    }
  }

  onSave() {
    
    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updatedInventoryContract 
    const newLegalLand: LegalLand = this.getUpdatedLegalLand()
  
    if (this.legalLand && this.legalLand.legalLandId) {
      this.store.dispatch(updateLegalLand(MANAGE_LEGAL_LAND_COMPONENT_ID, newLegalLand))
    } else {
      // add new
      this.store.dispatch(addNewLegalLand(MANAGE_LEGAL_LAND_COMPONENT_ID, newLegalLand))
    }
  
    this.hasDataChanged = false
  }

  getUpdatedLegalLand() {

    let updatedLegalLand : LegalLand 

    if (!this.legalLand) {

      updatedLegalLand = {
        links: [],
        legalLandId: null,
        primaryPropertyIdentifier: this.viewModel.formGroup.controls.primaryPropertyIdentifier.value,
        primaryLandIdentifierTypeCode: this.viewModel.formGroup.controls.primaryLandIdentifierTypeCode.value,
        primaryReferenceTypeCode: this.viewModel.formGroup.controls.primaryReferenceTypeCode.value,
        legalDescription: this.viewModel.formGroup.controls.legalDescription.value,
        legalShortDescription: "",
        otherDescription: this.viewModel.formGroup.controls.otherDescription.value,
        activeFromCropYear: this.viewModel.formGroup.controls.activeFromCropYear.value,
        activeToCropYear: this.viewModel.formGroup.controls.activeToCropYear.value,
        totalAcres: null,
        transactionType: null,
        riskAreas: [],
        fields: [],
        etag: "",
        type: ""
      }

    } else {

      //make a deep copy
      updatedLegalLand = JSON.parse(JSON.stringify(this.legalLand));

      // update the legal land values
      updatedLegalLand.primaryReferenceTypeCode = this.viewModel.formGroup.controls.primaryReferenceTypeCode.value
      updatedLegalLand.primaryLandIdentifierTypeCode = this.viewModel.formGroup.controls.primaryLandIdentifierTypeCode.value
      updatedLegalLand.primaryPropertyIdentifier = this.viewModel.formGroup.controls.primaryPropertyIdentifier.value
      updatedLegalLand.legalDescription =  this.viewModel.formGroup.controls.legalDescription.value
      updatedLegalLand.otherDescription = this.viewModel.formGroup.controls.otherDescription.value
      updatedLegalLand.activeFromCropYear = this.viewModel.formGroup.controls.activeFromCropYear.value
      updatedLegalLand.activeToCropYear = this.viewModel.formGroup.controls.activeToCropYear.value

    }
        
    // risk areas
    updatedLegalLand.riskAreas = []
    const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray

    for (let i = 0; i < riskAreasFrm.controls.length; i++ ) {

      if ( riskAreasFrm.controls[i]['controls']['isNewRiskArea'].value == true
          && riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value == true ) {
            // skip; added and deleted before saving, no need to send it to the api
      } else {
        // just add it to risk arrea object 
        updatedLegalLand.riskAreas.push(
          {
            riskAreaId:       riskAreasFrm.controls[i]['controls']['riskAreaId'].value,
            insurancePlanId:  riskAreasFrm.controls[i]['controls']['insurancePlanId'].value,
            riskAreaName:     "",
            description:      "",
            legalLandId:      parseInt(this.legalLandId),
            insurancePlanName:"",
            deletedByUserInd: riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value
          }
        )
      }
    }

    return updatedLegalLand
  }

  isFormValid() {

    const frmMain = this.viewModel.formGroup as UntypedFormGroup

    if ( !frmMain.controls.primaryPropertyIdentifier.value ) {
      alert ("Identifier is mandatory field")
      return false 
    }

    if (!this.validatePid(frmMain.controls.primaryPropertyIdentifier.value, frmMain.controls.primaryLandIdentifierTypeCode.value)) {
      return false
    }

    if ( !frmMain.controls.otherDescription.value ) {
      alert ("Other Description / Legal Location is mandatory field")
      return false 
    }

    if ( !frmMain.controls.activeFromCropYear.value ) {
      alert ("Active From Year is mandatory field")
      return false 
    }

    if ( isNaN (frmMain.controls.activeFromCropYear.value) || (frmMain.controls.activeToCropYear.value && isNaN(frmMain.controls.activeToCropYear.value)) ) {
      alert ("Active From Year should be a number between 1980 and the current year. Active To Year could be empty or a number between 1980 and the current year.")
      return false 
    }

    if ( frmMain.controls.activeFromCropYear.value < 1980 || frmMain.controls.activeFromCropYear.value > (new Date()).getFullYear() ) {
      alert ("Active From Year should be no earlier than 1980 and no later than the current year.")
      return false 
    }

    if ( frmMain.controls.activeToCropYear.value &&  frmMain.controls.activeFromCropYear.value > frmMain.controls.activeToCropYear.value ) {
      alert ("Active From Year should be less than Active To Year")
      return false 
    }

    // validate the risk areas
    const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray

    for (let i = 0; i < riskAreasFrm.controls.length; i++ ) {

      if ( riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value == false ) {

        let insurancePlanId = riskAreasFrm.controls[i]['controls']['insurancePlanId'].value
        let riskAreaId = riskAreasFrm.controls[i]['controls']['riskAreaId'].value

        if (!insurancePlanId || !riskAreaId ) {

          alert("Empty plan or risk area is not allowed")
          return false

        }

        for (let n = i + 1; n < riskAreasFrm.controls.length; n++ ) {

          if ( riskAreasFrm.controls[n]['controls']['deletedByUserInd'].value == false 
            && riskAreasFrm.controls[n]['controls']['insurancePlanId'].value == insurancePlanId ) {

              alert("Only one risk area per plan is allowed")
              return false 

          }
        }       
      }
    }

    return true
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MANAGE_LEGAL_LAND_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {

    const frmMain = this.viewModel.formGroup as UntypedFormGroup

    if (!this.legalLand) {

      if (areNotEqual(frmMain.controls.primaryReferenceTypeCode.value, PRIMARY_REFERENCE_TYPE_CODE.DEFAULT )
        || areNotEqual(frmMain.controls.primaryLandIdentifierTypeCode.value, PRIMARY_LAND_IDENTIFIER_TYPE_CODE.DEFAULT ) 
        || frmMain.controls.primaryPropertyIdentifier.value 
        || frmMain.controls.legalDescription.value 
        || frmMain.controls.otherDescription.value 
        || frmMain.controls.activeFromCropYear.value 
        || frmMain.controls.activeToCropYear.value ) {
          return true
        }

    } else {

      const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray

      // compare the original inventory contract data with the one in the form and set the flag if anything changed
      if ( areNotEqual(frmMain.controls.primaryReferenceTypeCode.value, this.legalLand.primaryReferenceTypeCode )
        || areNotEqual(frmMain.controls.primaryLandIdentifierTypeCode.value, this.legalLand.primaryLandIdentifierTypeCode ) 
        || areNotEqual(frmMain.controls.primaryPropertyIdentifier.value, this.legalLand.primaryPropertyIdentifier )
        || areNotEqual(frmMain.controls.legalDescription.value, this.legalLand.legalDescription )
        || areNotEqual(frmMain.controls.otherDescription.value, this.legalLand.otherDescription )
        || areNotEqual(frmMain.controls.activeFromCropYear.value, this.legalLand.activeFromCropYear )
        || areNotEqual(frmMain.controls.activeToCropYear.value, this.legalLand.activeToCropYear )  
        )  {

          return true
      } 

      for (let i =0; i < riskAreasFrm.controls.length; i++ ) {
        
        if ( riskAreasFrm.controls[i]['controls']['isNewRiskArea'].value == false 
          && riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value == true) {
            
          return true
        } 

        if ( riskAreasFrm.controls[i]['controls']['isNewRiskArea'].value == true 
          && riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value == false) {
            
          return true
        } 

        if ( riskAreasFrm.controls[i]['controls']['deletedByUserInd'].value == false 
          && ( areNotEqual( riskAreasFrm.controls[i]['controls']['insurancePlanId'].value, this.legalLand.riskAreas[i].insurancePlanId)
          || areNotEqual( riskAreasFrm.controls[i]['controls']['riskAreaId'].value, this.legalLand.riskAreas[i].riskAreaId))) {
         
            return true 
        }
      }
    }

    return false
  }

  onDeleteLegalLand() {

    if ( confirm("You are about to delete this Legal Land and associated Risk Areas. Do you want to continue?") ) { 
      
      if (this.legalLand && this.legalLand.legalLandId) {

        if ( !this.legalLand.fields || this.legalLand.fields.length === 0) {
          this.store.dispatch(deleteLegalLand(MANAGE_LEGAL_LAND_COMPONENT_ID, this.legalLand));
        } else {
          alert('Cannot Delete Legal Land. Please remove all associated Fields first.');
        }
      } else {
        alert('Cannot Delete Legal Land. It has never been saved.');
      }
    }
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  onAddRiskArea() {
    let raForm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray

    raForm.push( this.fb.group( {
      riskAreaId:       [ null ],
      insurancePlanId:  [ null ],
      riskAreaName:     [  ],
      description:      [  ],
      legalLandId:      [ null ],
      insurancePlanName:[ ],
      deletedByUserInd: [ false ],
      isNewRiskArea:    [ true ]
    } ) )

    this.riskAreaOptions.push([])

  }

  populateRiskAreaOptions(raIndex) {
    const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray
    const selectedInsurancePlanId = riskAreasFrm.controls[raIndex]['controls']['insurancePlanId'].value
    
    this.riskAreaOptions[raIndex] = []

    let filteredRAs = this.riskAreaList.collection.filter(x => x.insurancePlanId == selectedInsurancePlanId)

    var self = this

    filteredRAs.forEach(el => {
      self.riskAreaOptions[raIndex].push({
        code: el.riskAreaId,
        description: el.description
      })
    });
  }

  clearRiskAreaOptions(raIndex) {
    const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray
    riskAreasFrm.controls[raIndex]['controls']['riskAreaId'].setValue(null)
  }

  onDeleteRiskArea(raIndex) {
    const riskAreasFrm: UntypedFormArray = this.viewModel.formGroup.controls.riskAreas as UntypedFormArray
    riskAreasFrm.controls[raIndex]['controls']['deletedByUserInd'].setValue(true)
  }

  selectLandIdentifierType(pidType) {

    this.validateIdentifier = true

    switch (pidType) {
      case "PID":
        this.pidMask = "000-000-000" // xxx-xxx-xxx
        this.pidMaskHint = this.allPidHints.PID
        break
      case "PIN":
        this.pidMask = "0000000" // 7 digits, no separator
        this.pidMaskHint = this.allPidHints.PIN
        break
      case "LINC":
        this.pidMask = "0000-000-000" // 10 digits (XXXX-XXX-XXX) - This Is the Alberta version of a PID
        this.pidMaskHint = this.allPidHints.LINC
        break
      case "IR":
        this.pidMask = "000000000" //  9 digits, no separator
        this.pidMaskHint = this.allPidHints.IR
        break
      case "OTHER":
        this.pidMask = "AAAAAAAAAAAAA" //  up to 13 digits/characters, no separator
        this.pidMaskHint = this.allPidHints.OTHER
        this.validateIdentifier = false
        break
    }
  }

  validatePid(pid, pidType): boolean {
    
    switch (pidType) {
      case "PID":
        if (pid.length !== 11) {
          alert(this.allPidHints.PID)
          return false
        } else {
          return true
        }
      case "PIN":
        if (pid.length !== 7) {
          alert(this.allPidHints.PIN)
          return false
        } else {
          return true
        }
      case "LINC":
        if (pid.length < 12) {
          alert(this.allPidHints.LINC)
          return false
        } else {
          return true
        }
      case "IR":
        if (pid.length !== 9) {
          alert(this.allPidHints.IR)
          return false
        } else {
          return true
        }
      case "OTHER":
        // no min length requirements
        return true
    }
    return true
  }
  
}
