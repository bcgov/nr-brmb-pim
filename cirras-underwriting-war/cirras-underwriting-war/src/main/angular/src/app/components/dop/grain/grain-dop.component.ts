import { ChangeDetectionStrategy, Component, Input, SimpleChanges, } from '@angular/core';
import { ParamMap } from '@angular/router';
import { UwContract } from 'src/app/conversion/models';
import {  DopYieldContract, DopYieldFieldGrain, GradeModifierList, YieldMeasUnitTypeCodeList } from 'src/app/conversion/models-yield';
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
import { UntypedFormArray, UntypedFormGroup } from '@angular/forms';
import { addUwCommentsObject, areDatesNotEqual, areNotEqual, getInsurancePlanName, makeNumberOnly, replaceNonAlphanumericCharacters, setHttpHeaders } from 'src/app/utils';
import { UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import {ViewEncapsulation } from '@angular/core';
import { GradeModifierOptionsType } from '../dop-common';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
import { roundUpDecimalYield } from '../../inventory/inventory-common';

@Component({
  selector: 'grain-dop',
  templateUrl: './grain-dop.component.html',
  styleUrls: ['./grain-dop.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
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

          this.fetchGradeModifiers()
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

      if(this.dopYieldContract.grainFromOtherSourceInd != null){
        this.viewModel.formGroup.controls.grainFromOtherSourceInd.setValue( this.dopYieldContract.grainFromOtherSourceInd )
      } else {
        this.viewModel.formGroup.controls.grainFromOtherSourceInd.setValue('')
      }

    }

    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.fields ) {

      let flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
      flds.clear()
      this.dopYieldContract.fields.forEach( f => this.addField( f ) )

    }

    // dopYieldContractCommodities
    if ( changes.dopYieldContract && this.dopYieldContract && this.dopYieldContract.dopYieldContractCommodities ) {
      
      let frmCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray
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

      if (field.dopYieldFieldGrainList.length > 0 ) {

        for (let j = 0; j < field.dopYieldFieldGrainList.length; j++) {

          let dopField = field.dopYieldFieldGrainList[j]

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

  roundUpEstimatedYield(dopYieldField, dopYieldFieldIndex){
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    if(formFields) {
      let formField =  formFields.controls.find( f => f.value.fieldId == dopYieldField.value.fieldId )
        if(formField) {
          let formDopYieldField = formField.value.dopYieldFieldGrainList.controls[dopYieldFieldIndex].controls
          if(formDopYieldField) {
            let acres = formDopYieldField.estimatedYieldPerAcre.value
            formDopYieldField.estimatedYieldPerAcre.setValue(roundUpDecimalYield(acres,this.decimalPrecision))
          }
        }
    }
  }

  updateEstimatedYieldRounding(){
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    if(formFields) {

      for (let i = 0; i < formFields.controls.length; i++) {
        let formField = formFields.controls[i]
        if(formField) {

          for (let i = 0; i < formField.value.dopYieldFieldGrainList.controls.length; i++) {
            let formDopYieldField = formField.value.dopYieldFieldGrainList.controls[i].controls
            if(formDopYieldField) {
              let estYield = formDopYieldField.estimatedYieldPerAcre.value
              formDopYieldField.estimatedYieldPerAcre.setValue(roundUpDecimalYield(estYield, this.decimalPrecision))
            }
          }
        }
      }
    }
  }

  roundUpCommodityTotalYield(cmdty, fieldName){
    
    const formCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray
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
      formCommodity['controls'][fieldName].setValue(roundUpDecimalYield(fieldValue, this.decimalPrecision))
    }
  }

  updateCommodityTotalRounding(){
      
    let formCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray
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

    let frmDopYieldFields = new UntypedFormArray ([]) 

    var self = this

    if (field.dopYieldFieldGrainList.length > 0 ) {

      field.dopYieldFieldGrainList.forEach( function(dopField) {

        // add dopYieldField to the form
        frmDopYieldFields.push( self.fb.group( 
          self.addDopYieldFieldGrainObject(dopField) 
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
    

    let fld: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    fld.push( this.fb.group( {  
      annualFieldDetailId:   [ field.annualFieldDetailId ],
      displayOrder:          [ field.displayOrder ],
      fieldId:               [ field.fieldId ],
      fieldLabel:            [ field.fieldLabel ],
      otherLegalDescription: [ field.otherLegalDescription ],
      dopYieldFieldGrainList:        [ frmDopYieldFields ],
      uwComments:            [ fldComments ],
    } ) )


  }

  addDopContractCommodity( dopCommodity ) {

    let frmDopCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray
    
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

  addDopYieldFieldGrainObject(dopYieldField: DopYieldFieldGrain) {

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
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    if(formFields) {
      let formField =  formFields.controls.find( f => f.value.fieldId == dopYieldField.value.fieldId )
        if(formField) {
          let formDopYieldField = formField.value.dopYieldFieldGrainList.controls[dopYieldFieldIndex].controls
          if(formDopYieldField) {
            formDopYieldField.estimatedYieldPerAcre.setValue(null)
            formDopYieldField.unharvestedAcresInd.setValue(false)

            this.isMyFormDirty()
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

    const frmMain = this.viewModel.formGroup as UntypedFormGroup

    if ( areNotEqual (this.dopYieldContract.enteredYieldMeasUnitTypeCode, frmMain.controls.yieldMeasUnitTypeCode.value) || 
         areDatesNotEqual (this.dopYieldContract.declarationOfProductionDate, frmMain.controls.declarationOfProductionDate.value)	||
         areNotEqual (this.dopYieldContract.grainFromOtherSourceInd, frmMain.controls.grainFromOtherSourceInd.value)  ) {

          return true
    }

    // check if estimated yield has changed
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    for (let i = 0; i < formFields.controls.length; i++) {

      let formField = formFields.controls[i] as UntypedFormArray

      let originalField = this.dopYieldContract.fields.find( f => f.fieldId == formField.value.fieldId)

      if (originalField) {

        for (let k = 0; k < formField.value.dopYieldFieldGrainList.controls.length; k++) {
          let formDopYieldField = formField.value.dopYieldFieldGrainList.controls[k] as UntypedFormArray
  
          let originalDopYieldField = originalField.dopYieldFieldGrainList.find( elem => elem.inventorySeededGrainGuid == formDopYieldField.value.inventorySeededGrainGuid) 

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
    const formDopYieldContractCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray
    for (let n = 0; n < formDopYieldContractCommodities.controls.length; n++) {

      let formdDopYieldContractCommodity = formDopYieldContractCommodities.controls[n] as UntypedFormArray

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
      } else if (!originalUwComment) { // new comment and therefore can't find it
        return true
      }

    }

    return false
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
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
    const formFields: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    formFields.controls.forEach( function(formField : UntypedFormArray) {

      // find the corresponding field in updatedDopYieldContract object
      let updField = updatedDopYieldContract.fields.find( f => f.fieldId == formField.value.fieldId)

      if (updField) { 

        updField.uwComments = formField.value.uwComments 

        for (let i = 0; i < formField.value.dopYieldFieldGrainList.length; i++) {

          // this doesn't work when saving for the first time because the declaredYieldFieldGuid is null
          // let updDopYieldField = updField.dopYieldFieldGrainList.find( elem => (elem.declaredYieldFieldGuid == formField.value.dopYieldFieldGrainList.value[i].declaredYieldFieldGuid) )
          let updDopYieldField = updField.dopYieldFieldGrainList.find( elem => (elem.inventoryFieldGuid == formField.value.dopYieldFieldGrainList.value[i].inventoryFieldGuid) )

          if (updDopYieldField){

            updDopYieldField.estimatedYieldPerAcre = formField.value.dopYieldFieldGrainList.value[i].estimatedYieldPerAcre
            updDopYieldField.unharvestedAcresInd = formField.value.dopYieldFieldGrainList.value[i].unharvestedAcresInd

          }
        }
      }
    
    })

    // update dopYieldContractCommodities values 
    const formCommodities: UntypedFormArray = this.viewModel.formGroup.controls.dopYieldContractCommodities as UntypedFormArray

    formCommodities.controls.forEach( function (formCommodity: UntypedFormArray) {

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

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      this.loadPage()
      
      this.hasDataChanged = false   
      this.showEstimatedYieldMessage = false

      this.store.dispatch(setFormStateUnsaved(DOP_COMPONENT_ID, false ));

      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
  }

  onPrint() {
    let reportName = replaceNonAlphanumericCharacters(this.growerContract.growerName) + "-DOP" 
    this.store.dispatch(GetDopReport(reportName, this.policyId, "", this.insurancePlanId, "", "", "", "", ""))

  }


  onInventoryCommentsDone(fieldId: number, uwComments: UnderwritingComment[]) {
    this.updateComments(fieldId, uwComments);

    this.isMyFormDirty();

    this.cdr.detectChanges();
  }

  updateComments(fieldId: number, uwComments: UnderwritingComment[]) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray

    var self = this 

    flds.controls.forEach( function(field : UntypedFormArray) {

      if (field.value.fieldId == fieldId) {

        if (uwComments && uwComments.length > 0 ) {
          
          let fldComments = [] 

          uwComments.forEach ( (comment : UnderwritingComment) => fldComments.push ( 
            addUwCommentsObject( comment )
          ))

          field.controls["uwComments"].setValue( fldComments )

        }

      }
    })
  }

  onDopCommentsDone(uwComments: UnderwritingComment[]) {
    this.viewModel.formGroup.controls.uwComments.setValue(uwComments);

    this.isMyFormDirty();

    this.cdr.detectChanges();
  }

  getInsPlanName(insurancePlanId){

    return getInsurancePlanName(insurancePlanId)
  }

  setFormStyles(){
    return {
      'grid-template-columns':  'auto 148px 120px 186px 146px 12px 155px'
    }
  }

  setPlantingStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr',
      'align-items': 'stretch',
      'width': '830px'
    }

    return styles;
  }

  fetchGradeModifiers(){

    let url = this.appConfigService.getConfig().rest["cirras_underwriting"]
    url = url +"/gradeModifiers?cropYear=" + this.cropYear
    url = url +"&insurancePlanId=" +  this.insurancePlanId.toString()
    
    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())
   
    return this.http.get(url,httpOptions).toPromise().then((data: GradeModifierList) => {

      this.gradeModifierList = []
      // construct the grade modifiers options 
      for (let i=0; i< data.collection.length; i++ ) {
        this.gradeModifierList.push({
            cropCommodityId: data.collection[i].cropCommodityId.toString(),
            gradeModifierTypeCode: data.collection[i].gradeModifierTypeCode,
            description: data.collection[i].description
        })
      }
    })

  }
}
