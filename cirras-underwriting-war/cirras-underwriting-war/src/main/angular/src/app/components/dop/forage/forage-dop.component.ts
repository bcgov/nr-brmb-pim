import { ChangeDetectionStrategy, Component, Input, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { UwContract } from 'src/app/conversion/models';
import { ForageDopComponentModel } from './forage-dop.component.model';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { ParamMap } from '@angular/router';
import { ClearDopYieldContract, LoadDopYieldContract, LoadYieldMeasUnitList, RolloverDopYieldContract } from 'src/app/store/dop/dop.actions';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { DopYieldContract, DopYieldFieldForage, DopYieldFieldForageCut, GradeModifierList, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { addUwCommentsObject, getInsurancePlanName, makeNumberOnly, setHttpHeaders } from 'src/app/utils';
import { GradeModifierOptionsType, roundUpDecimals } from '../dop-common';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import {ViewEncapsulation } from '@angular/core';
import { FieldUwComment, UnderwritingCommentsComponent } from '../../underwriting-comments/underwriting-comments.component';
import { UW_COMMENT_TYPE_CODE } from 'src/app/utils/constants';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';

@Component({
  selector: 'forage-dop',
  templateUrl: './forage-dop.component.html',
  styleUrls: ['./forage-dop.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})
export class ForageDopComponent extends BaseComponent {
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;


  policyId: string;
  declaredYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = DOP_COMPONENT_ID;
  decimalPrecision: number;

  hasDataChanged= false;

  gradeModifierList : GradeModifierOptionsType[] = [];

  yieldMeasUnitOptions = [];

  // the combined column width in pixels of totalBalesLoads + weight + moisturePercent for each cut
  baseColWidth = 320; // 440; 

  // the maxumum number of visible cuts that is going to be displayed on the screen
  maxNumOfVisibleCuts = 1;

  initModels() {
    this.viewModel = new ForageDopComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): ForageDopComponentModel  {  
    return <ForageDopComponentModel>this.viewModel;
  }

  loadPage() {
    this.componentId = DOP_COMPONENT_ID;

    this.route.paramMap.subscribe(
      (params: ParamMap) => {

          this.policyId = params.get("policyId") ? params.get("policyId") : "";
          this.declaredYieldContractGuid = params.get("declaredYieldContractGuid") ? params.get("declaredYieldContractGuid").trim() : "";
          this.cropYear = params.get("cropYear") ? params.get("cropYear") : "";
          this.insurancePlanId = params.get("insurancePlanId") ? params.get("insurancePlanId") : "";

          this.store.dispatch(ClearDopYieldContract());

          this.store.dispatch(LoadGrowerContract(this.componentId, this.policyId))

          this.store.dispatch( LoadYieldMeasUnitList(this.componentId, this.insurancePlanId) )

          if (this.declaredYieldContractGuid.length > 0) {
            // get the already existing dop yield contract
            this.store.dispatch(LoadDopYieldContract(this.componentId, this.declaredYieldContractGuid ))
          } else {
            // prepare the new dop yield contract
            this.store.dispatch(RolloverDopYieldContract(this.componentId, this.policyId))
          }

          this.getGradeModifiers(this.gradeModifierList)
      }
    );

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
  
    this.ngOnChanges2(changes);
  }

  ngOnChanges2(changes: SimpleChanges) {

    // populate the yield measurement unit dropdown
    if (changes.yieldMeasUnitList && this.yieldMeasUnitList && this.yieldMeasUnitList.collection && this.yieldMeasUnitList.collection.length ) {

      // clear the unit options
      this.yieldMeasUnitOptions = []
      
      this.yieldMeasUnitList.collection.forEach( ymu => this.getYieldMeasUnitOptions(ymu) )
    }

    if ( changes.dopYieldContract && this.dopYieldContract ) {

      if (this.dopYieldContract.enteredYieldMeasUnitTypeCode) {

        this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.setValue( this.dopYieldContract.enteredYieldMeasUnitTypeCode )

      } else {

        // get the default value 
        let defaultYieldMeasUnitTypeCode = ""

        for ( let k = 0;  k < this.yieldMeasUnitList.collection.length; k++) {

          if (this.yieldMeasUnitList.collection[k].isDefaultYieldUnitInd ) {

            defaultYieldMeasUnitTypeCode = this.yieldMeasUnitList.collection[k].yieldMeasUnitTypeCode
            break
          }

        }

        this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.setValue(defaultYieldMeasUnitTypeCode)
      }

      this.setDecimalPrecision();
        
      if (this.dopYieldContract.declarationOfProductionDate) {
        this.viewModel.formGroup.controls.declarationOfProductionDate.setValue(new Date(this.dopYieldContract.declarationOfProductionDate) )
      } else {
        this.viewModel.formGroup.controls.declarationOfProductionDate.setValue('')
      }

      if(this.dopYieldContract.balerWagonInfo != null){
        this.viewModel.formGroup.controls.balerWagonInfo.setValue( this.dopYieldContract.balerWagonInfo )
      } else {
        this.viewModel.formGroup.controls.balerWagonInfo.setValue('')
      }

      if(this.dopYieldContract.totalLivestock != null){
        this.viewModel.formGroup.controls.totalLivestock.setValue( this.dopYieldContract.totalLivestock )
      } else {
        this.viewModel.formGroup.controls.totalLivestock.setValue('')
      }
    }

    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.fields ) {

      let flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
      flds.clear()
      this.dopYieldContract.fields.forEach( f => this.addField( f ) )

      this.maxNumOfVisibleCuts = this.getMaxVisibleCutNumber()
    }

    // TODO load commodity totals
 
  }
  
  getYieldMeasUnitOptions (opt) {
    this.yieldMeasUnitOptions.push ({
      yieldMeasUnitTypeCode: opt.yieldMeasUnitTypeCode,
      description: opt.description
    })
  }

  addField( field ) {

    let frmDopYieldFields = new FormArray ([]) 
    let frmCuts : FormArray = new FormArray ([]) 

    var self = this

    if (field.dopYieldFieldForageList.length > 0 ) {

      frmCuts = new FormArray ([]) 

      field.dopYieldFieldForageList.forEach( function(dopField) {

        if (dopField.dopYieldFieldForageCuts && dopField.dopYieldFieldForageCuts.length > 0 ) {
          dopField.dopYieldFieldForageCuts.forEach( function(cut) {
            frmCuts.push( self.fb.group (
              self.addDopYieldFieldForageCutObject(cut)
            ))
          })
        } else { 

          frmCuts.push( self.fb.group (
            self.addEmptyDopYieldFieldForageCutObject( field.inventoryFieldGuid, 1)
          ))
        }
        

        // add dopYieldField to the form
        frmDopYieldFields.push( self.fb.group( 
          self.addDopYieldFieldForageObject(dopField, frmCuts) 
        ) )
      })
    }
    
    // set underwriting comments
    let fldComments = [] // no need to set them up as a form group
    
    if (field.uwComments.length > 0 ) {

      field.uwComments.forEach ( (comment: UnderwritingComment) => fldComments.push ( 
        addUwCommentsObject( comment )
      ))
    } 
    
    let fld: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    fld.push( this.fb.group( {  
      annualFieldDetailId:   [ field.annualFieldDetailId ],
      displayOrder:          [ field.displayOrder ],
      fieldId:               [ field.fieldId ],
      fieldLabel:            [ field.fieldLabel ],
      otherLegalDescription: [ field.otherLegalDescription ],
      dopYieldFieldForageList:        [ frmDopYieldFields ],
      uwComments:            [ fldComments ],
    } ) )

  }

  addDopYieldFieldForageObject(dopYieldField: DopYieldFieldForage, frmCuts: FormArray) {
    return {
        inventoryFieldGuid:               [ dopYieldField.inventoryFieldGuid ],
        commodityTypeCode:                [ dopYieldField.commodityTypeCode ], 
        commodityTypeDescription:         [ dopYieldField.commodityTypeDescription ], 
        cropVarietyName:                  [ dopYieldField.cropVarietyName ],
        isQuantityInsurableInd:           [ dopYieldField.isQuantityInsurableInd ],
        fieldAcres:                       [ dopYieldField.fieldAcres ],
        insurancePlanId:                  [ dopYieldField.insurancePlanId],  
        fieldId:                          [ dopYieldField.fieldId ],
        cropYear:                         [ dopYieldField.cropYear ],
        dopYieldFieldForageCuts:          [ frmCuts ] 
      }
  }

  addDopYieldFieldForageCutObject(dopYieldFieldForageCut: DopYieldFieldForageCut) {
    return {
      declaredYieldFieldForageGuid: [ dopYieldFieldForageCut.declaredYieldFieldForageGuid ],
      inventoryFieldGuid:           [ dopYieldFieldForageCut.inventoryFieldGuid ],
      cutNumber:                    [ dopYieldFieldForageCut.cutNumber ],
      totalBalesLoads:              [ dopYieldFieldForageCut.totalBalesLoads ],
      weight:                       [ dopYieldFieldForageCut.weight ],
      weightDefaultUnit:            [ dopYieldFieldForageCut.weightDefaultUnit ],
      moisturePercent:              [ dopYieldFieldForageCut.moisturePercent ],
    }
  }

  addEmptyDopYieldFieldForageCutObject(inventoryFieldGuid, cutNumber) {
    return {
      declaredYieldFieldForageGuid: [ null ], //TODO: I might need to generate a guid for the purposes of the ui but submit null to the backend
      inventoryFieldGuid:           [ inventoryFieldGuid ],
      cutNumber:                    [ cutNumber ],
      totalBalesLoads:              [''],
      weight:                       [''],
      weightDefaultUnit:            [''],
      moisturePercent:              [''],
    }
  }


  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  yieldMeasUnitTypeSelectionChanged(){
    this.setDecimalPrecision();

    this.isMyFormDirty();
  }

  setDecimalPrecision() {
    if (this.yieldMeasUnitList && this.yieldMeasUnitList.collection && this.yieldMeasUnitList.collection.length ) {

      const selectedCode = this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.value;

      let selectedYieldMeasUnit = this.yieldMeasUnitList.collection.find( f => f.yieldMeasUnitTypeCode == selectedCode )

      if (selectedYieldMeasUnit) {
        this.decimalPrecision = selectedYieldMeasUnit.decimalPrecision;
      } 
    }

  }

  getMaxVisibleCutNumber() {

    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    let maxNumCuts = 1 // there is always at least one cut for each field displayed on the screen

    for (let i = 0; i < formFields.controls.length; i++) {

      let formField = formFields.controls[i] as FormArray

      for (let k = 0; k < formField.value.dopYieldFieldForageList.controls.length; k++) {
        let formDopYieldField = formField.value.dopYieldFieldForageList.controls[k] as FormArray

        for (let n = 0; n < formDopYieldField.value.dopYieldFieldForageCuts.controls.length; n++) {
          let formDopYieldFieldForageCut = formDopYieldField.value.dopYieldFieldForageCuts.controls[n] as FormArray

          if (maxNumCuts < formDopYieldFieldForageCut.value.cutNumber ) {
            // TODO: check whether the cut is visible or it's marked for deletion
            maxNumCuts = formDopYieldFieldForageCut.value.cutNumber
          }
        }
      }
	  }
    
    return maxNumCuts
  }

  onPrint() {
    // TODO
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  roundUpCutBales(dopYieldFieldForageCut){

    let val = dopYieldFieldForageCut.value.totalBalesLoads

    if (!val) {
      return
    }

    let totalBalesLoads = roundUpDecimals(val, 0)

    if ( totalBalesLoads != "") {
      dopYieldFieldForageCut.controls['totalBalesLoads'].setValue(totalBalesLoads)
    }
  }

  roundUpCutWeight(dopYieldFieldForageCut){
    // TODO: if the moisturePercent is empty, set defaults:
    // moisturePercent = 15%  // for Perennials
    // moisturePercent = 0% // for Silage Corn (Annual)

    let val = dopYieldFieldForageCut.value.weight

    if (!val) {
      return
    }

    let weight = roundUpDecimals(val, 2)

    if ( weight != "") {
      dopYieldFieldForageCut.controls['weight'].setValue(weight)
    }
  }

  roundUpCutMoisturePercent(dopYieldFieldForageCut){

    let val = dopYieldFieldForageCut.value.moisturePercent

    if (!val) {
      return
    }

    let moisturePercent = roundUpDecimals(val, 0)

    if ( moisturePercent != "") {
      dopYieldFieldForageCut.controls['moisturePercent'].setValue(moisturePercent)
    }
  }

  roundUptotalLivestock(){

    let val = this.getViewModel().formGroup.controls.totalLivestock.value

    if (!val) {
      return
    }

    let roundUpVal = roundUpDecimals(val, 0)

    if ( roundUpVal != "") {
      this.getViewModel().formGroup.controls.totalLivestock.setValue(roundUpVal)
    }
  }


  getGradeModifiers(gradeModifierList: GradeModifierOptionsType[]){

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url +"/gradeModifiers?cropYear=" + this.cropYear
    url = url +"&insurancePlanId=" +  this.insurancePlanId.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())
   
    return this.http.get(url,httpOptions).toPromise().then((data: GradeModifierList) => {

      gradeModifierList = []
      // construct the grade modifiers options 
      for (let i=0; i< data.collection.length; i++ ) {
        gradeModifierList.push({
            cropCommodityId: data.collection[i].cropCommodityId.toString(),
            gradeModifierTypeCode: data.collection[i].gradeModifierTypeCode,
            description: data.collection[i].description
        })
      }
    })

  }

  isThereAnyDopComment() {
    // Checks if there are un-deleted comments
    var self = this

    if (self.viewModel.formGroup.controls["uwComments"] && self.viewModel.formGroup.controls["uwComments"].value && self.viewModel.formGroup.controls["uwComments"].value.length > 0) {

      for (let i = 0; i < self.viewModel.formGroup.controls["uwComments"].value.length; i++) {
        if (self.viewModel.formGroup.controls["uwComments"].value[i].deletedByUserInd == true) {

        } else {
          return true
        }
      }
      
    }

    return false
  }

  isThereAnyComment(field) {
    
    // Checks if there are un-deleted comments
    if (field.value.uwComments && field.value.uwComments.length > 0) {

      for (let i = 0; i < field.value.uwComments.length; i++) {
        if (field.value.uwComments[i].deletedByUserInd == true) {

        } else {
          return true
        }
      }
      
    }
    return false
  }

  onLoadComments(field) {
  
    const dataToSend : FieldUwComment = {
      fieldId: field.value.fieldId,
      annualFieldDetailId: field.value.annualFieldDetailId,
      uwCommentTypeCode: UW_COMMENT_TYPE_CODE.INVENTORY_GENERAL,
      uwComments: field.value.uwComments 
    }

    const dialogRef = this.dialog.open(UnderwritingCommentsComponent, {
      width: '800px',
      data: dataToSend
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'Update'){
        this.updateComments(result.data);

        this.isMyFormDirty();

        this.cdr.detectChanges()
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });

  }

  updateComments(data: FieldUwComment) {

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    var self = this 

    flds.controls.forEach( function(field : FormArray) {

      if (field.value.fieldId == data.fieldId) {

        if (data.uwComments && data.uwComments.length > 0 ) {
          
          let fldComments = [] 

          data.uwComments.forEach ( (comment : UnderwritingComment) => fldComments.push ( 
            addUwCommentsObject( comment )
          ))

          field.controls["uwComments"].setValue( fldComments )

        }

      }
    })
  }

  onLoadDopComments(){

    const dataToSend : FieldUwComment = {
      growerContractYearId: this.dopYieldContract.growerContractYearId,
      declaredYieldContractGuid: this.declaredYieldContractGuid,
      uwCommentTypeCode: UW_COMMENT_TYPE_CODE.DOP_GENERAL,
      uwComments: this.viewModel.formGroup.controls.uwComments.value
    }

    const dialogRef = this.dialog.open(UnderwritingCommentsComponent, {
      width: '800px',
      data: dataToSend
    });

    var self = this

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'Update'){

        self.viewModel.formGroup.controls["uwComments"].setValue( result.data.uwComments )

        self.isMyFormDirty();

        self.cdr.detectChanges()
      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });
  }

  onDeleteFieldYield(dopYieldField, dopYieldFieldIndex) {
    //TODO
  }

  onDeleteDop() {
    // TODO: 
    // //Ask for confirmation before deleting all DOP data
    // if ( confirm("You are about to delete all DOP data for the policy. Do you want to continue?") ) {

    //   const newDopYieldContract: DopYieldContract = this.getUpdatedDopYieldContract()

    //   if (this.dopYieldContract.declaredYieldContractGuid) {
    //     //Delete dop contract
    //     this.store.dispatch(DeleteDopYieldContract(DOP_COMPONENT_ID, this.policyId, newDopYieldContract))

    //   } 
    // }
  }

  onCancel() {

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      this.loadPage()

      this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));

      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }


  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, this.hasDataChanged ));
  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.dopYieldContract) return false;
    // TODO

    return false

  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 148px 110px 186px 146px 12px 155px'
    }
  }

  setPlantingStyles(){
    
    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr',
      'align-items': 'stretch',
      'width':   this.baseColWidth * this.maxNumOfVisibleCuts + 540 +'px' 
    }

    return styles;
  }
  
  setCutStyles(){

    let gridTemplateColumns = '' 

    for (let i = 0; i < this.maxNumOfVisibleCuts; i++ ) {
      gridTemplateColumns = gridTemplateColumns + ' 1fr 1fr 1fr'
    }

    let styles = {
      'display': 'grid',
      'grid-template-columns': gridTemplateColumns,
      'align-items': 'stretch',
      'width': this.baseColWidth * this.maxNumOfVisibleCuts +'px'
    }

    return styles;
  }

}
