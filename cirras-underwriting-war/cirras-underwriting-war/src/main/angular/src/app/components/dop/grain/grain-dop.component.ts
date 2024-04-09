import { ChangeDetectionStrategy, Component, Input, SimpleChanges, } from '@angular/core';
import { ParamMap } from '@angular/router';
import { UwContract } from 'src/app/conversion/models';
import {  DopYieldContract, DopYieldField, GradeModifierList, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
import { 
  AddNewDopYieldContract, 
  ClearDopYieldContract, 
  DeleteDopYieldContract, 
  LoadDopYieldContract, 
  LoadYieldMeasUnitList, 
  RolloverDopYieldContract, 
  UpdateDopYieldContract, 
  GetDopReport
 } from 'src/app/store/dop/dop.actions';
import { DOP_COMPONENT_ID } from 'src/app/store/dop/dop.state';
import { LoadGrowerContract } from 'src/app/store/grower-contract/grower-contract.actions';
import { BaseComponent } from '../../common/base/base.component';
import { GrainDopComponentModel } from './grain-dop.component.model';
import { FormArray, FormGroup } from '@angular/forms';
import { addUwCommentsObject, areDatesNotEqual, areNotEqual, getInsurancePlanName, makeNumberOnly, setHttpHeaders } from 'src/app/utils';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { FieldUwComment, UnderwritingCommentsComponent } from '../../underwriting-comments/underwriting-comments.component';
import { UW_COMMENT_TYPE_CODE } from 'src/app/utils/constants';

export interface GradeModifierOptionsType {
  cropCommodityId: string;
  gradeModifierTypeCode: string;
  description: string;
}

@Component({
  selector: 'grain-dop',
  templateUrl: './grain-dop.component.html',
  styleUrls: ['./grain-dop.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class GrainDopComponent extends BaseComponent{
  @Input() growerContract: UwContract;
  @Input() dopYieldContract: DopYieldContract;
  @Input() yieldMeasUnitList: YieldMeasUnitTypeCodeList;

  acceptableRangeCommodityPercentage = 0.1 // 10% difference between declared and estimated commodity totals 

  policyId: string;
  declaredYieldContractGuid: string;
  cropYear: string;
  insurancePlanId: string;
  componentId = DOP_COMPONENT_ID;
  decimalPrecision: number;

  hasDataChanged = false;
  showEstimatedYieldMessage = false;

  yieldMeasUnitOptions = [];

  // gradeModifierList : GradeModifierList = <GradeModifierList>{};
  gradeModifierList : GradeModifierOptionsType[] = [];

  filteredGradeModifierOptions: GradeModifierOptionsType[]; 

  flaggedTotalEstimatedYield = [];

  initModels() {
    this.viewModel = new GrainDopComponentModel(this.sanitizer, this.fb);
  }

  getViewModel(): GrainDopComponentModel  {  
    return <GrainDopComponentModel>this.viewModel;
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

          this.getGradeModifiers()
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

        this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.setValue(this.dopYieldContract.enteredYieldMeasUnitTypeCode )

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

      if(this.dopYieldContract.grainFromOtherSourceInd != null){
        this.viewModel.formGroup.controls.grainFromOtherSourceInd.setValue( this.dopYieldContract.grainFromOtherSourceInd )
      } else {
        this.viewModel.formGroup.controls.grainFromOtherSourceInd.setValue('')
      }

    }

    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.fields ) {

      let flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
      flds.clear()
      this.dopYieldContract.fields.forEach( f => this.addField( f ) )

    }

    // dopYieldContractCommodities
    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.dopYieldContractCommodities ) {
      
      let frmCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray
      frmCommodities.clear()
      this.dopYieldContract.dopYieldContractCommodities.forEach( cmdty => this.addDopContractCommodity( cmdty ) )

      // subscribe to searchGradeModifiers
      for (let i = 0; i < frmCommodities.controls.length; i++) {
        let selectedCropCommodityId = frmCommodities.controls[i]['controls']['cropCommodityId'].value
        frmCommodities.controls[i]['controls']['gradeModifierCtrl'].valueChanges.subscribe(value => this.searchGradeModifier (value, selectedCropCommodityId))    
      }
    }

    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.uwComments ) {
      this.viewModel.formGroup.controls["uwComments"].setValue( this.dopYieldContract.uwComments )
    }

    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.dopYieldContractCommodities && this.dopYieldContract.dopYieldFieldRollupList ) {
      this.calculateFlaggedTotalEstimatedYield()
    }

  }

  calculateFlaggedTotalEstimatedYield () {
    // calculates in tonne/acre 

    if (this.dopYieldContract.dopYieldContractCommodities && this.dopYieldContract.dopYieldFieldRollupList && 
      this.dopYieldContract.dopYieldContractCommodities.length > 0 && this.dopYieldContract.dopYieldFieldRollupList.length > 0) {

        this.flaggedTotalEstimatedYield = []

        for (let i = 0; i < this.dopYieldContract.dopYieldFieldRollupList.length; i++ ) {

          let cmdtyId = this.dopYieldContract.dopYieldFieldRollupList[i].cropCommodityId
          let isPdgree = this.dopYieldContract.dopYieldFieldRollupList[i].isPedigreeInd
          let estimatedYieldTonnesPerAcre = this.dopYieldContract.dopYieldFieldRollupList[i].estimatedYieldPerAcreTonnes * this.getTotalInsuredAcresFromCommodityTotals(cmdtyId, isPdgree)
          let farmYield = this.getFarmYieldFromCommodityTotals(cmdtyId, isPdgree)
          let isFlagged = false;

          //Only flag the total if there are values in the commodity totals: 0 or greater
          if(farmYield != null){
            let maxAcceptableRange = farmYield * this.acceptableRangeCommodityPercentage //10% of farmYield
            if(Math.abs(estimatedYieldTonnesPerAcre - farmYield) > maxAcceptableRange) {
              isFlagged = true
            }
          }

          this.flaggedTotalEstimatedYield.push ({
            cropCommodityId: cmdtyId,
            isPedigreeInd: isPdgree,
            totalYield: estimatedYieldTonnesPerAcre,  
            farmYield: farmYield,
            isFlagged: (isFlagged)
          })
        }
    }
  }

  getTotalInsuredAcresFromCommodityTotals(cropCommodityId, isPedigree) {
    
    let totalHarvestedAcres = 0

    for (let i = 0; i < this.dopYieldContract.fields.length; i++) {

      let field = this.dopYieldContract.fields[i]

      if (field.dopYieldFields.length > 0 ) {

        for (let j = 0; j < field.dopYieldFields.length; j++) {

          let dopField = field.dopYieldFields[j]

          if(dopField.cropCommodityId == cropCommodityId && dopField.isPedigreeInd == isPedigree && 
            dopField.estimatedYieldPerAcre != null && dopField.seededAcres){
            totalHarvestedAcres = totalHarvestedAcres + dopField.seededAcres
          }
        }
      }
    }

    return totalHarvestedAcres

  }

  getFarmYieldFromCommodityTotals(cropCommodityId, isPedigree) {
    // it should always return the yield in tonnes - default unit

    let el = this.dopYieldContract.dopYieldContractCommodities.find( x => x.cropCommodityId == cropCommodityId && x.isPedigreeInd == isPedigree)
    let sum = 0
    let hasValues = false

    if (el) {
  
      if (el.storedYieldDefaultUnit != null && !isNaN(el.storedYieldDefaultUnit) ) {
        sum = sum + el.storedYieldDefaultUnit
        hasValues = true
      }
      
      if (el.soldYieldDefaultUnit != null && !isNaN(el.soldYieldDefaultUnit) ) {
        sum = sum + el.soldYieldDefaultUnit
        hasValues = true
      }
    } 

    if(hasValues){
      return sum
    } else {
      return null
    }
  }

  getFlaggedTotalEstimatedYield(cmdtyId, isPedigree) {

    let el = this.flaggedTotalEstimatedYield.find(x => x.cropCommodityId == cmdtyId && x.isPedigreeInd == isPedigree )
    
    if (el) {
      return el.totalYield
    } else {
      return ""
    }
  }

  isFlaggedTotalEstimatedYield(cmdtyId, isPedigree) {

    let el = this.flaggedTotalEstimatedYield.find(x => x.cropCommodityId == cmdtyId && x.isPedigreeInd == isPedigree )

    if (el) {
      return el.isFlagged
    } else {
      return false
    }

  }

  yieldMeasUnitTypeSelectionChanged(){
    this.setDecimalPrecision();

    this.updateEstimatedYieldRounding();
    this.updateCommodityTotalRounding();

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

  roundUpDecimals(numberToRound) {
    if (!numberToRound) {
      return ""
    }

    if (isNaN(parseFloat(numberToRound))) {
      alert ("Yield must be a valid number" )
    } else {

      if (parseFloat(numberToRound) % 1 == 0 ) {
        // return integer if it's an integer, no zeros after the decimal point
        return parseInt(numberToRound)
      }
      
      return parseFloat(numberToRound).toFixed(this.decimalPrecision)
    }
  }

  roundUpEstimatedYield(dopYieldField, dopYieldFieldIndex){
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    if(formFields) {
      let formField =  formFields.controls.find( f => f.value.fieldId == dopYieldField.value.fieldId )
        if(formField) {
          let formDopYieldField = formField.value.dopYieldFields.controls[dopYieldFieldIndex].controls
          if(formDopYieldField) {
            let acres = formDopYieldField.estimatedYieldPerAcre.value
            formDopYieldField.estimatedYieldPerAcre.setValue(this.roundUpDecimals(acres))
          }
        }
    }
  }

  updateEstimatedYieldRounding(){
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    if(formFields) {

      for (let i = 0; i < formFields.controls.length; i++) {
        let formField = formFields.controls[i]
        if(formField) {

          for (let i = 0; i < formField.value.dopYieldFields.controls.length; i++) {
            let formDopYieldField = formField.value.dopYieldFields.controls[i].controls
            if(formDopYieldField) {
              let estYield = formDopYieldField.estimatedYieldPerAcre.value
              formDopYieldField.estimatedYieldPerAcre.setValue(this.roundUpDecimals(estYield))
            }
          }
        }
      }
    }
  }

  roundUpCommodityTotalYield(cmdty, fieldName){
    
    const formCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray
    if(formCommodities) {
      let formCommodity =  formCommodities.controls.find( f => f.value.cropCommodityId == cmdty.value.cropCommodityId 
                                                            && f.value.isPedigreeInd == cmdty.value.isPedigreeInd)
        this.updateCommodityTotalField(formCommodity, fieldName);
    }

    this.calculateFlaggedTotalEstimatedYield()
  }

  updateCommodityTotalField(formCommodity, fieldName){
    if(formCommodity) {
      let fieldValue = formCommodity['controls'][fieldName].value
      formCommodity['controls'][fieldName].setValue(this.roundUpDecimals(fieldValue))
    }
  }

  updateCommodityTotalRounding(){
      
    let formCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray
    if(formCommodities) {
      for (let i = 0; i < formCommodities.controls.length; i++) {
        this.updateCommodityTotalField(formCommodities.controls[i], 'storedYield');
        this.updateCommodityTotalField(formCommodities.controls[i], 'soldYield');
      }
    }

  }

  getYieldMeasUnitOptions (opt) {
    this.yieldMeasUnitOptions.push ({
      yieldMeasUnitTypeCode: opt.yieldMeasUnitTypeCode,
      description: opt.description
    })
  }


  addField( field ) {

    let frmDopYieldFields = new FormArray ([]) 

    var self = this

    if (field.dopYieldFields.length > 0 ) {

      field.dopYieldFields.forEach( function(dopField) {

        // add dopYieldField to the form
        frmDopYieldFields.push( self.fb.group( 
          self.addDopYieldFieldObject(dopField) 
        ) )
      }

      )

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
      dopYieldFields:        [ frmDopYieldFields ],
      uwComments:            [ fldComments ],
    } ) )


  }


  addDopComment( dopComment ) {

    let frmDopComments: FormArray = this.viewModel.formGroup.controls.uwComments as FormArray
    
    // add dopYieldContractCommodities to the form
    frmDopComments.push( this.fb.group(  {
      underwritingCommentGuid:     [ dopComment.underwritingCommentGuid],
      annualFieldDetailId:         [ dopComment.annualFieldDetailId ],
      growerContractYearId:        [ dopComment.growerContractYearId ],
      declaredYieldContractGuid:   [ dopComment.declaredYieldContractGuid ],
      underwritingCommentTypeCode: [ dopComment.underwritingCommentTypeCode ],
      underwritingCommentTypeDesc: [ dopComment.underwritingCommentTypeDesc ],
      underwritingComment:         [ dopComment.underwritingComment ],
      createUser:                  [ dopComment.createUser ],
      createDate:                  [ dopComment.createDate.toString()] ,
      updateUser:                  [ dopComment.updateUser ],
      updateDate:                  [ dopComment.updateDate.toString() ] ,
      deletedByUserInd:            [ dopComment.deletedByUserInd ],
      userCanEditInd:              [ dopComment.userCanEditInd ],
      userCanDeleteInd:            [ dopComment.userCanDeleteInd ]
      }
    ) )
  }

  addDopContractCommodity( dopCommodity ) {

    let frmDopCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray
    
    // add dopYieldContractCommodities to the form
    frmDopCommodities.push( this.fb.group(  {
      declaredYieldContractCommodityGuid:   [ dopCommodity.declaredYieldContractCommodityGuid ],
      declaredYieldContractGuid:            [ dopCommodity.declaredYieldContractGuid ],
      cropCommodityId:                      [ dopCommodity.cropCommodityId ], 
      isPedigreeInd:                        [ dopCommodity.isPedigreeInd ],
      harvestedAcres:                       [ dopCommodity.harvestedAcres ], 
      storedYield:                          [ dopCommodity.storedYield ],
      storedYieldDefaultUnit:               [ dopCommodity.storedYieldDefaultUnit ],
      soldYield:                            [ dopCommodity.soldYield ],
      soldYieldDefaultUnit:                 [ dopCommodity.soldYieldDefaultUnit ],
      gradeModifierCtrl:          [ { 
        gradeModifierTypeCode: dopCommodity.gradeModifierTypeCode, 
        description: this.getGradeModifierDescription(dopCommodity.gradeModifierTypeCode)
      } ],

      cropCommodityName:                    [ dopCommodity.cropCommodityName ],         
      totalInsuredAcres:                    [ dopCommodity.totalInsuredAcres ],
      }
    ) )
  }

  addDopYieldFieldObject(dopYieldField: DopYieldField) {

    return {
        declaredYieldFieldGuid:           [ dopYieldField.declaredYieldFieldGuid ],
        inventoryFieldGuid:               [ dopYieldField.inventoryFieldGuid ],
        estimatedYieldPerAcre:            [ dopYieldField.estimatedYieldPerAcre ], 
        estimatedYieldPerAcreDefaultUnit: [ dopYieldField.estimatedYieldPerAcreDefaultUnit ], 
        unharvestedAcresInd:              [ dopYieldField.unharvestedAcresInd ],
        inventorySeededGrainGuid:         [ dopYieldField.inventorySeededGrainGuid ],
        cropCommodityId:                  [ dopYieldField.cropCommodityId ],
        cropCommodityName:                [ dopYieldField.cropCommodityName ],
        cropVarietyId:                    [ dopYieldField.cropVarietyId ], 
        cropVarietyName:                  [ dopYieldField.cropVarietyName ], 
        isPedigreeInd:                    [ dopYieldField.isPedigreeInd ],
        seededDate:                       [ dopYieldField.seededDate ],
        seededAcres:                      [ dopYieldField.seededAcres ],
        insurancePlanId:                  [ dopYieldField.insurancePlanId],  
        fieldId:                          [ dopYieldField.fieldId ],
        cropYear:                         [ dopYieldField.cropYear ],
      }
  }

  onDeleteFieldYield(dopYieldField, dopYieldFieldIndex){

    //Reset a dop field record
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    if(formFields) {
      let formField =  formFields.controls.find( f => f.value.fieldId == dopYieldField.value.fieldId )
        if(formField) {
          let formDopYieldField = formField.value.dopYieldFields.controls[dopYieldFieldIndex].controls
          if(formDopYieldField) {
            formDopYieldField.estimatedYieldPerAcre.setValue(null)
            formDopYieldField.unharvestedAcresInd.setValue(false)
          }
        }
    }
  }

  isMyFormDirty() {

    this.hasDataChanged = this.isMyFormReallyDirty()
    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, this.hasDataChanged ));
  }

  isMyFormReallyDirty() : boolean {
    
    if (!this.dopYieldContract) return false;

    this.showEstimatedYieldMessage = false

    const frmMain = this.viewModel.formGroup as FormGroup

    if ( areNotEqual (this.dopYieldContract.enteredYieldMeasUnitTypeCode, frmMain.controls.yieldMeasUnitTypeCode.value) || 
         areDatesNotEqual (this.dopYieldContract.declarationOfProductionDate, frmMain.controls.declarationOfProductionDate.value)	||
         areNotEqual (this.dopYieldContract.grainFromOtherSourceInd, frmMain.controls.grainFromOtherSourceInd.value)  ) {

          return true
    }

    // check if estimated yield has changed
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    for (let i = 0; i < formFields.controls.length; i++) {

      let formField = formFields.controls[i] as FormArray

      let originalField = this.dopYieldContract.fields.find( f => f.fieldId == formField.value.fieldId)

      if (originalField) {

        for (let k = 0; k < formField.value.dopYieldFields.controls.length; k++) {
          let formDopYieldField = formField.value.dopYieldFields.controls[k] as FormArray
  
          let originalDopYieldField = originalField.dopYieldFields.find( elem => elem.inventorySeededGrainGuid == formDopYieldField.value.inventorySeededGrainGuid) 

          if (originalDopYieldField) {

            if (areNotEqual(formDopYieldField.value.estimatedYieldPerAcre,  originalDopYieldField.estimatedYieldPerAcre ) ) {

                  this.showEstimatedYieldMessage = true

            }

            if (areNotEqual(formDopYieldField.value.estimatedYieldPerAcre,  originalDopYieldField.estimatedYieldPerAcre ) ||
                areNotEqual(formDopYieldField.value.unharvestedAcresInd,  originalDopYieldField.unharvestedAcresInd) ) {
                
                return true
              }
          }
        }

        //check the uwComments
        for (let k = 0; k < formField.value.uwComments.length; k++) {

          // now check if anything changed in the comments
          if (formField.value.uwComments.length != originalField.uwComments.length) {

            return true
          }
          
          // go inside the comments and check for changes there
          for (let n = 0; n < formField.value.uwComments.length; n++){
            
            let frmUwComments = formField.value.uwComments[n] 

            let originalUwComment = originalField.uwComments.find( p => p.underwritingCommentGuid == frmUwComments.underwritingCommentGuid)

            if (originalUwComment && 
                (areNotEqual(frmUwComments.underwritingComment, originalUwComment.underwritingComment) || frmUwComments.deletedByUserInd == true )
                ) {

              return true
            }

          }

        }
      }
    }


    // check if commodity totals have changed
    const formDopYieldContractCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray
    for (let n = 0; n < formDopYieldContractCommodities.controls.length; n++) {

      let formdDopYieldContractCommodity = formDopYieldContractCommodities.controls[n] as FormArray

      let originalDopYieldContractCommodity = 
        this.dopYieldContract.dopYieldContractCommodities
        .find( elem => (elem.cropCommodityId == formdDopYieldContractCommodity.value.cropCommodityId 
                        && elem.isPedigreeInd == formdDopYieldContractCommodity.value.isPedigreeInd  ) )

      if (originalDopYieldContractCommodity) {

        if (areNotEqual( originalDopYieldContractCommodity.harvestedAcres , formdDopYieldContractCommodity.value.harvestedAcres ) 
            || areNotEqual( originalDopYieldContractCommodity.storedYield , formdDopYieldContractCommodity.value.storedYield ) 
            || areNotEqual( originalDopYieldContractCommodity.soldYield , formdDopYieldContractCommodity.value.soldYield ) 
            || areNotEqual( originalDopYieldContractCommodity.gradeModifierTypeCode , formdDopYieldContractCommodity.value.gradeModifierCtrl.gradeModifierTypeCode ) ) {

          return true
        }
      }
    }

    //check DOP comments
    if (frmMain.controls["uwComments"].value.length != this.dopYieldContract.uwComments.length) {
      return true
    }
    
    // go inside the comments and check for changes there
    for (let n = 0; n < frmMain.controls["uwComments"].value.length; n++){
      
      let frmUwComments = frmMain.controls["uwComments"].value[n] 

      let originalUwComment = this.dopYieldContract.uwComments.find( p => p.underwritingCommentGuid == frmUwComments.underwritingCommentGuid)

      if (originalUwComment && 
          (areNotEqual(frmUwComments.underwritingComment, originalUwComment.underwritingComment) || frmUwComments.deletedByUserInd == true )
          ) {

        return true
      }

    }

    return false
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  getGradeModifiers(){

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url +"/gradeModifiers?cropYear=" + this.cropYear
    url = url +"&insurancePlanId=" +  this.insurancePlanId.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    var self = this
    return this.http.get(url,httpOptions).toPromise().then((data: GradeModifierList) => {

      self.gradeModifierList = []
      // construct the grade modifiers options 
      for (let i=0; i< data.collection.length; i++ ) {
        self.gradeModifierList.push({
            cropCommodityId: data.collection[i].cropCommodityId.toString(),
            gradeModifierTypeCode: data.collection[i].gradeModifierTypeCode,
            description: data.collection[i].description
        })
      }
    })

  }

  // grade modifier search
  searchGradeModifier(value, selectedCropCommodityId) {
  
    const gmDescription = (( typeof value === 'string') ? value : value?.description)

    if (gmDescription) {
      const filterValue = gmDescription.toLowerCase()

      this.filteredGradeModifierOptions = 
        this.gradeModifierList.filter(option => 
                                        ( option.description.toLowerCase().includes(filterValue) 
                                          && option.cropCommodityId == selectedCropCommodityId) )
    
    } else {
      this.filteredGradeModifierOptions = 
          this.gradeModifierList.filter(option => 
                                          ( option.cropCommodityId == selectedCropCommodityId) )

    }
  }

  displayGradeModifierFn(gm: GradeModifierOptionsType): string {
    return gm && gm.description ? gm.description  : '';
  }

  gradeModifierFocus(cropCommodityId) {

    // prepare the list of grade modifiers based on the crop id
    this.filteredGradeModifierOptions = 
          this.gradeModifierList.filter(option => 
                                          ( option.cropCommodityId == cropCommodityId) )

  }

  getGradeModifierDescription(gradeModifierTypeCode) : String {

    let gmtc = this.gradeModifierList.find(el => el.gradeModifierTypeCode == gradeModifierTypeCode)

    if (gmtc) {
      return gmtc.description
    } else {
      return ""
    }
  }

  onSave() {
 
    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updatedDopYieldContract 
    const newDopYieldContract: DopYieldContract = this.getUpdatedDopYieldContract()

    if (this.dopYieldContract.declaredYieldContractGuid) {
      this.store.dispatch(UpdateDopYieldContract(DOP_COMPONENT_ID, newDopYieldContract))
    } else {
      // add new
      this.store.dispatch(AddNewDopYieldContract(DOP_COMPONENT_ID, newDopYieldContract))
    }

    this.hasDataChanged = false   
    this.showEstimatedYieldMessage = false

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));

  }

  
  onDeleteDop() {

    //Ask for confirmation before deleting all DOP data
    if ( confirm("You are about to delete all DOP data for the policy. Do you want to continue?") ) {

      const newDopYieldContract: DopYieldContract = this.getUpdatedDopYieldContract()

      if (this.dopYieldContract.declaredYieldContractGuid) {
        //Delete dop contract
        this.store.dispatch(DeleteDopYieldContract(DOP_COMPONENT_ID, this.policyId, newDopYieldContract))

      } 
    }
  }

  getUpdatedDopYieldContract() {

    //make a deep copy
    let updatedDopYieldContract : DopYieldContract = JSON.parse(JSON.stringify(this.dopYieldContract));

    // update InventoryContract values
    updatedDopYieldContract.declarationOfProductionDate = this.viewModel.formGroup.controls.declarationOfProductionDate.value
    updatedDopYieldContract.enteredYieldMeasUnitTypeCode = this.viewModel.formGroup.controls.yieldMeasUnitTypeCode.value
    updatedDopYieldContract.grainFromOtherSourceInd = this.viewModel.formGroup.controls.grainFromOtherSourceInd.value

    var self = this;   

    // update fields Estimated Yield values
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    formFields.controls.forEach( function(formField : FormArray) {

      // find the corresponding field in updatedDopYieldContract object
      let updField = updatedDopYieldContract.fields.find( f => f.fieldId == formField.value.fieldId)

      if (updField) { 

        updField.uwComments = formField.value.uwComments 

        for (let i = 0; i < formField.value.dopYieldFields.length; i++) {

          // this doesn't work when saving for the first time because the declaredYieldFieldGuid is null
          // let updDopYieldField = updField.dopYieldFields.find( elem => (elem.declaredYieldFieldGuid == formField.value.dopYieldFields.value[i].declaredYieldFieldGuid) )
          let updDopYieldField = updField.dopYieldFields.find( elem => (elem.inventoryFieldGuid == formField.value.dopYieldFields.value[i].inventoryFieldGuid) )

          if (updDopYieldField){

            updDopYieldField.estimatedYieldPerAcre = formField.value.dopYieldFields.value[i].estimatedYieldPerAcre
            updDopYieldField.unharvestedAcresInd = formField.value.dopYieldFields.value[i].unharvestedAcresInd

          }
        }
      }
    
    })

    // update dopYieldContractCommodities values 
    const formCommodities: FormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as FormArray

    formCommodities.controls.forEach( function (formCommodity: FormArray) {

      // find the corresponding commodity in updDopYieldField object 
      let updCommodity = updatedDopYieldContract.dopYieldContractCommodities
                .find( elem => elem.cropCommodityId == formCommodity.value.cropCommodityId && 
                        elem.isPedigreeInd == formCommodity.value.isPedigreeInd)
      
      if (updCommodity) {

        updCommodity.harvestedAcres = formCommodity.value.harvestedAcres
        updCommodity.storedYield = formCommodity.value.storedYield
        updCommodity.soldYield = formCommodity.value.soldYield
        updCommodity.gradeModifierTypeCode = formCommodity.value.gradeModifierCtrl.gradeModifierTypeCode
        
      }
    })

    // update DOP comments values 
    if(this.viewModel.formGroup.controls.uwComments) {
      updatedDopYieldContract.uwComments = this.viewModel.formGroup.controls.uwComments.value
    }

    return updatedDopYieldContract
  }

  isFormValid() {

    // DOP date should be a valid date
    let declarationOfProductionDate = this.viewModel.formGroup.controls.declarationOfProductionDate.value

    if ( !declarationOfProductionDate || new Date(declarationOfProductionDate).toString() == 'Invalid Date') {
      alert("Empty or invalid Declaration of Production Date" )
      return false 
    }

    // grain from other sources cannot be null
    const grainFromOtherSourceInd  = this.viewModel.formGroup.controls.grainFromOtherSourceInd.value

    if (grainFromOtherSourceInd === "") {
      alert("Please answer the grain from other sources question")
      return false
    }

    // TODO: add more validations as needed 
    return true
  }

  onCancel() {

    // reload the page
    this.loadPage()

    this.hasDataChanged = false   
    this.showEstimatedYieldMessage = false

    this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));
  }

  onPrint() {

    let reportName = this.growerContract.growerName + "-DOP" 
    reportName = reportName.replace(".", "")
    this.store.dispatch(GetDopReport(reportName, this.policyId, "", "", "", "", "", "", ""))
    
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

  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }
}
