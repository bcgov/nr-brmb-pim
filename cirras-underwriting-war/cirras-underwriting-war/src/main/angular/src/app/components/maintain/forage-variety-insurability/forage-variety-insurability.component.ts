import { ChangeDetectorRef, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { ForageVarietyInsurabilityComponentModel } from './forage-variety-insurability.component.model';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { MAINTENANCE_COMPONENT_ID } from 'src/app/store/maintenance/maintenance.state';
import { loadVarietyInsurability, saveVarietyInsurability } from 'src/app/store/maintenance/maintenance.actions';
import { INSURANCE_PLAN } from 'src/app/utils/constants';
import { CropVarietyInsurabilityList } from 'src/app/conversion/models-maintenance';
import { UntypedFormArray, UntypedFormBuilder, FormControl, Validators } from '@angular/forms';
import { PlantInsurabilityComponent } from './plant-insurability/plant-insurability.component';
import { areNotEqual } from 'src/app/utils';
import { ActivatedRoute, Router } from '@angular/router';
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
  selector: 'forage-variety-insurability',
  templateUrl: './forage-variety-insurability.component.html',
  styleUrls: ['./forage-variety-insurability.component.scss']
})
export class ForageVarietyInsurabilityComponent  extends BaseComponent implements OnChanges  {


  @Input() cropVarietyInsurabilityList: CropVarietyInsurabilityList
  // @Input() isUnsaved: boolean;

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

  hasDataChanged = false;
  isInEditMode = false;
  etag = ""

  initModels() {
    this.viewModel = new ForageVarietyInsurabilityComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): ForageVarietyInsurabilityComponentModel  { //
    return <ForageVarietyInsurabilityComponentModel>this.viewModel;
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  loadPage() {
    this.store.dispatch(loadVarietyInsurability(MAINTENANCE_COMPONENT_ID, INSURANCE_PLAN.FORAGE.toString(), "N" ))

    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }
  

  ngOnChanges(changes: SimpleChanges): void {

    if (changes.cropVarietyInsurabilityList) {
      // pre-fill the form
      
      let frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray
      frmVarietyInsurability.clear()

      if ( this.cropVarietyInsurabilityList && this.cropVarietyInsurabilityList.collection && this.cropVarietyInsurabilityList.collection.length > 0) {

        this.cropVarietyInsurabilityList.collection.forEach (el => {

          frmVarietyInsurability.push( this.fb.group( {  
            cropVarietyId:                      [ el.cropVarietyId ],
            varietyName:                        [ el.varietyName ],
            plantDurationTypeCode:              [ el.plantDurationTypeCode ],
            cropVarietyInsurabilityGuid:        [ el.cropVarietyInsurabilityGuid], 
            isQuantityInsurableInd:             [ el.isQuantityInsurableInd ],
            isUnseededInsurableInd:             [ el.isUnseededInsurableInd],
            isPlantInsurableInd:                [ el.isPlantInsurableInd ],
            isAwpEligibleInd:                   [ el.isAwpEligibleInd ],
            isUnderseedingEligibleInd:          [ el.isUnderseedingEligibleInd ],
            isGrainUnseededDefaultInd:          [ el.isGrainUnseededDefaultInd ],
            deletedByUserInd:                   [ el.deletedByUserInd ],
            isQuantityInsurableEditableInd:     [el.isQuantityInsurableEditableInd] , //[ (el.isQuantityInsurableEditableInd ) ? el.isQuantityInsurableEditableInd : false ], 
            isUnseededInsurableEditableInd:     [ el.isUnseededInsurableEditableInd ],
            isPlantInsurableEditableInd:        [ el.isPlantInsurableEditableInd ],
            isAwpEligibleEditableInd:           [ el.isAwpEligibleEditableInd ],
            isUnderseedingEligibleEditableInd:  [ el.isUnderseedingEligibleEditableInd ],
            cropVarietyPlantInsurabilities:     [ (el.cropVarietyPlantInsurabilities && el.cropVarietyPlantInsurabilities.length > 0 ) ? 
                                                    el.cropVarietyPlantInsurabilities : "" ] 
          } ) )
        })
      }
    }

    this.viewModel.formGroup.valueChanges.subscribe(val => {this.isMyFormDirty()})
  }

  onSave() {
    
    // prepare the updated variaty insurabilities 
    const newCropVarietyInsurabilityList: CropVarietyInsurabilityList = this.getUpdatedCropVarietyInsurabilityLists()

    // save 
    this.store.dispatch(saveVarietyInsurability(MAINTENANCE_COMPONENT_ID, INSURANCE_PLAN.FORAGE.toString(), "N", this.etag, newCropVarietyInsurabilityList))
    
    this.hasDataChanged = false   
    this.isInEditMode = false 

    // this is supposed to let the user know that they are going to loose their changes, 
    // if they click on the side menu links
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false ));
  }

  getUpdatedCropVarietyInsurabilityLists() {

    //make a deep copy
    let updatedVarietyInsurability : CropVarietyInsurabilityList = JSON.parse(JSON.stringify(this.cropVarietyInsurabilityList));

    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    frmVarietyInsurability.controls.forEach( function(frmVI : UntypedFormArray) {

      // find the corresponding field in updatedVarietyInsurability object
      let origVarietyInsurability = updatedVarietyInsurability.collection.find( el => el.cropVarietyId == frmVI.value.cropVarietyId)
      
      if (origVarietyInsurability) { 
        // update only
        origVarietyInsurability.isQuantityInsurableInd = frmVI.value.isQuantityInsurableInd ? frmVI.value.isQuantityInsurableInd : false
        origVarietyInsurability.isUnseededInsurableInd = frmVI.value.isUnseededInsurableInd ? frmVI.value.isUnseededInsurableInd : false
        origVarietyInsurability.isPlantInsurableInd = frmVI.value.isPlantInsurableInd ? frmVI.value.isPlantInsurableInd : false
        origVarietyInsurability.isAwpEligibleInd = frmVI.value.isAwpEligibleInd ? frmVI.value.isAwpEligibleInd : false
        origVarietyInsurability.isUnderseedingEligibleInd = frmVI.value.isUnderseedingEligibleInd ? frmVI.value.isUnderseedingEligibleInd : false
        origVarietyInsurability.isGrainUnseededDefaultInd = frmVI.value.isGrainUnseededDefaultInd ? frmVI.value.isGrainUnseededDefaultInd : false
        origVarietyInsurability.cropVarietyPlantInsurabilities = (frmVI.value.cropVarietyPlantInsurabilities && frmVI.value.cropVarietyPlantInsurabilities.length > 0) ? 
                                                                      frmVI.value.cropVarietyPlantInsurabilities : [] 
        origVarietyInsurability.deletedByUserInd = frmVI.value.deletedByUserInd ? frmVI.value.deletedByUserInd : false 
      } 
    })

    return updatedVarietyInsurability
 }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, this.hasDataChanged ));

  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.cropVarietyInsurabilityList) return false;

    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    for (let i = 0; i < frmVarietyInsurability.controls.length; i++) {

      let frmVI = frmVarietyInsurability.controls[i] as UntypedFormArray

      let originalVI = this.cropVarietyInsurabilityList.collection.find( el => el.cropVarietyId == frmVI.value.cropVarietyId)

      if (originalVI) {
        
        if ( frmVI.value.deletedByUserInd == true) {
          return true
        }

        if ( areNotEqual (originalVI.isQuantityInsurableInd, frmVI.value.isQuantityInsurableInd )	
        || areNotEqual (originalVI.isUnseededInsurableInd, frmVI.value.isUnseededInsurableInd )	
        || areNotEqual (originalVI.isPlantInsurableInd, frmVI.value.isPlantInsurableInd )
        || areNotEqual (originalVI.isAwpEligibleInd, frmVI.value.isAwpEligibleInd )
        || areNotEqual (originalVI.isUnderseedingEligibleInd, frmVI.value.isUnderseedingEligibleInd)
        || areNotEqual (originalVI.isGrainUnseededDefaultInd, frmVI.value.isGrainUnseededDefaultInd)) {
          return true
        }

        // check cropVarietyPlantInsurabilities
        if ( originalVI.cropVarietyPlantInsurabilities.length !== frmVI.value.cropVarietyPlantInsurabilities.length ) {
          return true
        }

        for ( let k = 0; k < frmVI.value.cropVarietyPlantInsurabilities.length; k++) {

          let frmPI = frmVI.value.cropVarietyPlantInsurabilities[k]

          if (frmPI.deletedByUserInd == true ) {
            return true
          }

          let origPI = originalVI.cropVarietyPlantInsurabilities.find ( x => x.plantInsurabilityTypeCode == frmPI.plantInsurabilityTypeCode)

          if ( !origPI ) {
            // added
            return true
          }
        }
      }
    }
    
    return false
  }

  onCancel() {

    if (this.isUnsaved) {

      this.isInEditMode = !confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.");

      if (!this.isInEditMode) {
        // reload the page
        this.store.dispatch(loadVarietyInsurability(MAINTENANCE_COMPONENT_ID, INSURANCE_PLAN.FORAGE.toString(), "N"))

        this.hasDataChanged = false
        this.store.dispatch(setFormStateUnsaved(MAINTENANCE_COMPONENT_ID, false));
      }
    } else {
      this.isInEditMode = false
    }
  }

  getPlantInsurability(cropVarietyPlantInsurabilities) {

    let res = ""
    
    if (cropVarietyPlantInsurabilities) {

      for(let i=0; i < cropVarietyPlantInsurabilities.length; i++ ) {

        if ( !(cropVarietyPlantInsurabilities[i].deletedByUserInd == true) ) {

          if ( res.length > 0 ) {

            res = res + ";\n" + cropVarietyPlantInsurabilities[i].plantInsurabilityTypeCode // description

          } else {

            res = cropVarietyPlantInsurabilities[i].plantInsurabilityTypeCode // description

          }
        }
      }
    }
    return res
  }

  onEdit() {
    this.etag = this.cropVarietyInsurabilityList.etag

    this.store.dispatch(loadVarietyInsurability(MAINTENANCE_COMPONENT_ID, INSURANCE_PLAN.FORAGE.toString(), "Y" ))
    this.isInEditMode = true
  }

  onEditPlantInsurability(rowIndex) {
  
    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    // open up the popup
    const dataToSend  = {
      cropVarietyId:                  frmVarietyInsurability.controls[rowIndex].value.cropVarietyId,
      cropVarietyInsurabilityGuid:    ( (frmVarietyInsurability.controls[rowIndex].value.cropVarietyPlantInsurabilities && 
                                          frmVarietyInsurability.controls[rowIndex].value.cropVarietyPlantInsurabilities.length > 0) ? 
                                          frmVarietyInsurability.controls[rowIndex].value.cropVarietyPlantInsurabilities[0].cropVarietyInsurabilityGuid : null ),
      varietyName:                    frmVarietyInsurability.controls[rowIndex].value.varietyName,
      cropVarietyPlantInsurabilities: frmVarietyInsurability.controls[rowIndex].value.cropVarietyPlantInsurabilities
    }

    const dialogRef = this.dialog.open(PlantInsurabilityComponent, {
      width: '560px',
      data: dataToSend
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'Update'){

        this.setPlantInsurability(result.data)

      // } else if (result && result.event == 'Cancel'){
      //   do nothing
      }
    });
  }

  setPlantInsurability(data) {
    // gets the changes from the popup and transfers them to the form
    let frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    for ( let i = 0; i < frmVarietyInsurability.controls.length; i++) {

      if (frmVarietyInsurability.controls[i]['controls'].cropVarietyId.value == data.cropVarietyId) {

        frmVarietyInsurability.controls[i]['controls'].cropVarietyPlantInsurabilities.setValue(data.cropVarietyPlantInsurabilities)
        this.cdr.detectChanges()
        return
      }

    }
  }

  isDeletePossible(rowIndex){
    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    if (frmVarietyInsurability.controls[rowIndex].value.isQuantityInsurableEditableInd == true &&
      frmVarietyInsurability.controls[rowIndex].value.isUnseededInsurableEditableInd == true &&
      frmVarietyInsurability.controls[rowIndex].value.isPlantInsurableEditableInd == true &&
      frmVarietyInsurability.controls[rowIndex].value.isAwpEligibleEditableInd == true &&
      frmVarietyInsurability.controls[rowIndex].value.isUnderseedingEligibleEditableInd == true) {

        return true

      } else {
        return false
      }
  }

  onDeleteVarietyInsurability(rowIndex) {
    
    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    frmVarietyInsurability.controls[rowIndex]['controls'].deletedByUserInd.setValue(false)

    frmVarietyInsurability.controls[rowIndex]['controls'].isQuantityInsurableInd.setValue(false)
    frmVarietyInsurability.controls[rowIndex]['controls'].isUnseededInsurableInd.setValue(false)
    frmVarietyInsurability.controls[rowIndex]['controls'].isPlantInsurableInd.setValue(false)
    frmVarietyInsurability.controls[rowIndex]['controls'].isAwpEligibleInd.setValue(false)
    frmVarietyInsurability.controls[rowIndex]['controls'].isUnderseedingEligibleInd.setValue(false)
    frmVarietyInsurability.controls[rowIndex]['controls'].isGrainUnseededDefaultInd.setValue(false)

    this.removePlantInsurabilities(rowIndex)
  }

  removePlantInsurabilities(rowIndex) {
    const frmVarietyInsurability: UntypedFormArray = this.viewModel.formGroup.controls.varietyInsurability as UntypedFormArray

    let cvpi = frmVarietyInsurability.controls[rowIndex]['controls'].cropVarietyPlantInsurabilities.value

    // for some reason I can't just change deletedByUserInd, I have to re-construct the whole object
    let newCVPI = []

    for(let i = 0; i < cvpi.length; i++ ){
      newCVPI.push({
        cropVarietyId:                cvpi[i].cropVarietyId,
        cropVarietyInsurabilityGuid:  cvpi[i].cropVarietyInsurabilityGuid,
        deletedByUserInd:             true,
        description:                  cvpi[i].description,
        isUsedInd:                    cvpi[i].isUsedInd,
        plantInsurabilityTypeCode:    cvpi[i].plantInsurabilityTypeCode
      })
    }

    frmVarietyInsurability.controls[rowIndex]['controls'].cropVarietyPlantInsurabilities.setValue(newCVPI)
  }

  changeIsPlantInsurableInd(event, rowIndex) {
    
    if ( !event.checked ){ // if the checkbox is unchecked
      this.removePlantInsurabilities(rowIndex)
    }
  }

}
