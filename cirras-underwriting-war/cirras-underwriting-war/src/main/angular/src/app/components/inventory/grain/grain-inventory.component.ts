
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Input, OnChanges, SimpleChanges, Directive, ChangeDetectorRef, } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { CropVarietyCommodityType, InventorySeededGrain, InventoryUnseeded, UnderwritingComment } from '@cirras/cirras-underwriting-api';
import { CropCommodityList, InventoryContract, UwContract } from 'src/app/conversion/models';
import { AddNewInventoryContract, DeleteInventoryContract, GetInventoryReport, LoadInventoryContract, RolloverInventoryContract, UpdateInventoryContract } from 'src/app/store/inventory/inventory.actions';
import { INVENTORY_COMPONENT_ID } from 'src/app/store/inventory/inventory.state';
import { addUwCommentsObject, areDatesNotEqual, areNotEqual, areNullableBooleanNotEqual, isBaseCommodity, makeNumberOnly } from 'src/app/utils';
import { CROP_COMMODITY_UNSPECIFIED, INSURANCE_PLAN, UW_COMMENT_TYPE_CODE } from 'src/app/utils/constants';
import { BaseComponent } from '../../common/base/base.component';
import { AddLandPopupData } from '../add-land/add-land.component';
import { GrainInventoryComponentModel } from './grain-inventory.component.model';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { AddNewFormField, addAnnualFieldObject, addPlantingObject, addSeededGrainsObject, deleteFormField, deleteNewFormField, dragField, fieldHasInventory, isLinkedFieldCommon, isLinkedPlantingCommon, isThereAnyCommentForField, linkedFieldTooltipCommon, linkedPlantingTooltipCommon, navigateUpDownTextbox, openAddEditLandPopup, updateComments } from '../inventory-common';
import { RemoveFieldPopupData } from '../remove-field/remove-field.component';
import { displaySuccessSnackbar } from 'src/app/utils/user-feedback-utils';
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

@Directive()
export class GrainInventoryComponent extends BaseComponent implements OnChanges {


  @Input() inventoryContract: InventoryContract;
  @Input() cropCommodityList: CropCommodityList;
  @Input() underSeededCropCommodityList: CropCommodityList;
  @Input() growerContract: UwContract;

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

  cropCommodityOptions = [];
  cropVarietyOptions = [];
  lastYearsCropGrainCommodityOptions = [];
  lastYearsCropForageVarietyOptions = [];
  commodityTotals = [];

  sumCommodityTotalUnseededAcres = 0;
  sumCommodityProjectedAcres = 0;
  sumEligibleUnseededAcres = 0;

  // for seeded crop report 
  sumInsuredQuantityAcres = 0;
  sumInsuredSpotLossAcres = 0;
  sumCommodityTotalInsuredQuantityAcres = 0;
  sumCommodityTotalSpotLossAcres = 0;


  cropYear = 0;
  insurancePlanId = 0;
  policyId = '';

  hasDataChanged = false;
  hasYieldData = false;

initModels() {
  this.viewModel = new GrainInventoryComponentModel(this.sanitizer, this.fb, this.inventoryContract);
}

loadPage() {

    this.componentId = INVENTORY_COMPONENT_ID;
   
    this.route.paramMap.subscribe(
      (params: ParamMap) => {
          this.policyId = params.get("policyId") ? params.get("policyId") : "";
      }
    );
}

getViewModel(): GrainInventoryComponentModel  { //
    return <GrainInventoryComponentModel>this.viewModel;
}

get grainCropForageVrtyOptions() {
  const grainCropForageVrtyOptions = []
  grainCropForageVrtyOptions.push({
    cropCommodityVarietyId: '0_0',
    cropCommodityVarietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
    isUnseededInsurableInd: null
  });
  grainCropForageVrtyOptions.push(...this.lastYearsCropGrainCommodityOptions);
  grainCropForageVrtyOptions.push(...this.lastYearsCropForageVarietyOptions);
  return grainCropForageVrtyOptions;
}

ngAfterViewInit() {
  super.ngAfterViewInit();
}

ngOnChanges(changes: SimpleChanges) {
  super.ngOnChanges(changes);

  if (changes.inventoryContract) {
      this.inventoryContract = changes.inventoryContract.currentValue;
      
      setTimeout(() => {
          this.cdr.detectChanges();
      });
  }
  this.ngOnChanges2(changes);
}

ngOnChanges2(changes: SimpleChanges) {

    if ( changes.growerContract && this.growerContract ) {

      this.hasYieldData = false

      // check for yield data
      for (let i = 0; i< this.growerContract.links.length; i++ ) {

        if ( this.growerContract.links[i].href.toLocaleLowerCase().indexOf("dopyieldcontracts") > -1  ) {
          this.hasYieldData = true
          break
        } 
      }
    }

    if ( changes.inventoryContract && this.inventoryContract ) {
      this.cropYear = this.inventoryContract.cropYear
    }

    if ( changes.inventoryContract && this.inventoryContract ) {

      this.viewModel.formGroup.controls.fertilizerInd.setValue( this.inventoryContract.fertilizerInd )
      this.viewModel.formGroup.controls.herbicideInd.setValue( this.inventoryContract.herbicideInd )
      this.viewModel.formGroup.controls.tilliageInd.setValue( this.inventoryContract.tilliageInd )
      this.viewModel.formGroup.controls.otherChangesInd.setValue(this.inventoryContract.otherChangesInd )       
      this.viewModel.formGroup.controls.otherChangesComment.setValue( 
        (this.inventoryContract && this.inventoryContract.otherChangesComment)? this.inventoryContract.otherChangesComment.toString() : "" )

      this.viewModel.formGroup.controls.grainFromPrevYearInd.setValue( this.inventoryContract.grainFromPrevYearInd )
    }

    if ( changes.inventoryContract && this.inventoryContract && this.inventoryContract.fields ) {

        let flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
        flds.clear()
        this.inventoryContract.fields.forEach( f => this.addField( f ) )

    }

    // populate commodity and variety lists
    if (changes.cropCommodityList && this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length ) {
      // clear the crop options
      this.cropCommodityOptions = []
      this.cropVarietyOptions = []

      // add an empty crop commodity
      this.cropCommodityOptions.push ({
        commodityName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        insurancePlanId: null,
      })

      this.cropVarietyOptions.push ({
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]
      })

      this.lastYearsCropGrainCommodityOptions = []
      this.cropCommodityList.collection.forEach( ccm => this.getCropCommodityCodeOptions(ccm) )

      this.insurancePlanId = this.cropCommodityList.collection[0].insurancePlanId

    }

    // populate commodity and variety lists
    if (changes.underSeededCropCommodityList && this.underSeededCropCommodityList && this.underSeededCropCommodityList.collection && this.underSeededCropCommodityList.collection.length ) {
      this.lastYearsCropForageVarietyOptions = []
      this.underSeededCropCommodityList.collection.forEach( ccm => this.getUnderSeededCropCommodityCodeOptions(ccm) )
    }


    if (changes.inventoryContract && this.inventoryContract && 
      this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length ) {

      this.calculateSumTotals()
    }
}

  addField( field ) {

    let fldPlantings = new FormArray ([]) 

    let pltgInventorySeededGrains = new FormArray ([]) 

    var self = this

    if (field.plantings.length > 0 ) {

      field.plantings.forEach( function(pltg) {

        // add seededGrain if any
        pltgInventorySeededGrains = new FormArray ([])

        if (pltg.inventorySeededGrains && pltg.inventorySeededGrains.length > 0 ) {

          pltg.inventorySeededGrains.forEach( invSeededGrain =>
            pltgInventorySeededGrains.push( self.fb.group( 
              addSeededGrainsObject(pltg.inventoryFieldGuid, pltg.underseededAcres, pltg.underseededCropVarietyId, pltg.underseededCropVarietyName, pltg.isHiddenOnPrintoutInd, invSeededGrain)
            ) )
          )
        } else {

          pltgInventorySeededGrains.push( self.fb.group( 
            addSeededGrainsObject(pltg.inventoryFieldGuid, pltg.underseededAcres, pltg.underseededCropVarietyId, pltg.underseededCropVarietyName, pltg.isHiddenOnPrintoutInd, <InventorySeededGrain>[])
          ) )

        }
        
        // add plantings to the form
        fldPlantings.push( self.fb.group( 
          addPlantingObject(pltg.cropYear, pltg.fieldId, pltg.insurancePlanId, pltg.inventoryFieldGuid, 
            pltg.lastYearCropCommodityId, pltg.lastYearCropCommodityName, pltg.lastYearCropVarietyId, pltg.lastYearCropVarietyName,
            pltg.plantingNumber, pltg.isHiddenOnPrintoutInd, 
            pltg.underseededInventorySeededForageGuid, pltg.inventoryUnseeded, pltgInventorySeededGrains, new FormArray([]) ) ) )
        }

      )

    } else { //empty plantings array
      // console.log("addField -> addEmptyPlantingObject")
      fldPlantings.push( this.fb.group( this.addEmptyPlantingObject( field.fieldId ) ))

    }

    // set underwriting comments
    let fldComments = [] // no need to set them up as a form group
    
    if (field.uwComments.length > 0 ) {

      field.uwComments.forEach ( (comment: UnderwritingComment) => fldComments.push ( 
        addUwCommentsObject( comment )
      ))
    } 
    
    let fld: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    const formGroup = this.fb.group( addAnnualFieldObject(field, fldPlantings, fldComments) )

    // detect when a field is added
    // also detect when other legal description is changed
    formGroup.controls.landUpdateType.valueChanges.subscribe((value) => {
      if (value) {
        self.hasDataChanged = true;
        self.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, self.hasDataChanged));
      }
    });

    // detect when comments are added or deleted
    formGroup.controls.uwComments.valueChanges.subscribe(() => {
      self.hasDataChanged = true;
      self.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, self.hasDataChanged));
    });

    // detect when a field is deleted
    formGroup.controls.deletedByUserInd.valueChanges.subscribe(() => {
      self.hasDataChanged = true;
      self.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, self.hasDataChanged));
    });

    fld.push(formGroup)
  }


  getCropCommodityCodeOptions (opt) {
    this.cropCommodityOptions.push ({
      commodityName: opt.commodityName,
      cropCommodityId: opt.cropCommodityId,
      insurancePlanId: opt.insurancePlanId
    })

    this.lastYearsCropGrainCommodityOptions.push ({
      cropCommodityVarietyId: `${opt.cropCommodityId}_0`,
      cropCommodityVarietyName: opt.commodityName,
      isUnseededInsurableInd: true,
      insurancePlanId: opt.insurancePlanId
    })

    opt.cropVariety.forEach( cv => this.getCropVarietyCodeOptions(cv) )
  }

  getUnderSeededCropCommodityCodeOptions(uCmdty) {
    const self = this;
    uCmdty.cropVariety.forEach(uVrty => {
      self.lastYearsCropForageVarietyOptions.push ({
        cropCommodityVarietyId: `${uCmdty.cropCommodityId}_${uVrty.cropVarietyId}`,
        cropCommodityVarietyName: uVrty.varietyName,
        isUnseededInsurableInd: uVrty.isGrainUnseededDefaultInd,
        insurancePlanId: uVrty.insurancePlanId
      })
    });
  }

  getCropVarietyCodeOptions(opt) {
    this.cropVarietyOptions.push ({
      cropCommodityId: opt.cropCommodityId,
      cropVarietyId: opt.cropVarietyId,
      varietyName: opt.varietyName  ,
      cropVarietyCommodityTypes: opt.cropVarietyCommodityTypes, 
    })
  }
  

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  navigateUpDown(event, jumps) {

    navigateUpDownTextbox(event, jumps)
  }

  

  updatingCalculated = false
  updateCalculated() {

    if ( !this.inventoryContract ) return
    if ( this.updatingCalculated ) return
    this.updatingCalculated = true

    //wait in order to get the new value populated in the form
    // setTimeout(() => {

       this.calculateSumTotals()
      
    // })
    this.updatingCalculated = false
  }
  

  calculateSumTotals() {
    var self = this
    this.clearCommodityTotals()

    this.getPlantingCommodityTotals()

    // update the unseeded acres in the commodity form based on the commodityTotals array
    const cmdtiesFA: FormArray = self.viewModel.formGroup.controls.commodities as FormArray
    cmdtiesFA.controls.forEach ( function(cmdtyFC: FormGroup){

      let cmdtyTot =  self.commodityTotals.find( c => c.cropCommodityId ==  cmdtyFC.controls.cropCommodityId.value)
      if (cmdtyTot) {
        cmdtyFC.controls.totalUnseededAcres.setValue( cmdtyTot.totalUnseededAcres )
      }
      
    })

    this.calculateCommodityTotals()
  }

  clearCommodityTotals() {
    var self = this

    // clear the commodity totals
    this.commodityTotals = []
    this.cropCommodityList.collection.forEach( ccm => {
      self.commodityTotals.push ({
        cropCommodityName: ccm.commodityName,
        cropCommodityId: ccm.cropCommodityId,
        totalUnseededAcres: 0,
      })
    } )  
    // add the unspecified commodity
    self.commodityTotals.push ({
      cropCommodityName: CROP_COMMODITY_UNSPECIFIED.OTHER_NAME,
      cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
      totalUnseededAcres: 0,
    })
  }

  getPlantingCommodityTotals () {
    
    var self = this

    // clear sumEligibleUnseededAcres
    this.sumEligibleUnseededAcres = 0

    // populate the commodityTotals array based on the planting form values
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    flds.controls.forEach( function(fld : FormControl) {

      let pltgs : FormArray = fld.value.plantings as FormArray
      pltgs.controls.forEach (function (pltg: FormGroup){

        const pltgCropCommodityId = pltg.value.cropCommodityId

        let pltgAcres = parseFloat(pltg.value.acresToBeSeeded)
        if (isNaN(pltgAcres)) {
          pltgAcres = 0
        }

        if (pltgAcres > 0 ) {

          if (isBaseCommodity(pltg.value.cropCommodityId, self.cropCommodityList)) {
            // if it is insured (base) commodity save the totals for the corresponding commodity
            let cmdty = self.commodityTotals.find(elem => elem.cropCommodityId == pltgCropCommodityId)
            cmdty.totalUnseededAcres += pltgAcres
          } else {

            const insPlanId = self.cropCommodityOptions.find(el => el.cropCommodityId == pltgCropCommodityId)?.insurancePlanId

            if (!pltgCropCommodityId || (insPlanId && insPlanId == INSURANCE_PLAN.GRAIN)) {
              // if it is not insured cmdty then sum acres towards OTHER cmdty but only for GRAIN commodities
              let otherCmdty = self.commodityTotals.find(elem => elem.cropCommodityId == CROP_COMMODITY_UNSPECIFIED.ID)
              otherCmdty.totalUnseededAcres += pltgAcres
            }
          }

          // calculate sumCommodityTotalUnseededAcres
          if (pltg.value.isUnseededInsurableInd) {
            self.sumEligibleUnseededAcres += pltgAcres
          }        
          
        }
      })
    })
  }

  calculateCommodityTotals() {
    var self = this

    // get ready to calculate sum of commodityTotals and sum of projected acres
    self.sumCommodityTotalUnseededAcres = 0
    self.sumCommodityProjectedAcres = 0

    self.commodityTotals.forEach (cmdtyTot => {
      self.sumCommodityTotalUnseededAcres += cmdtyTot.totalUnseededAcres;
    })
    
    const cmdtiesFA: FormArray = self.viewModel.formGroup.controls.commodities as FormArray
    cmdtiesFA.controls.forEach ( function(cmdtyFC: FormGroup){
      let tmpAcres = parseFloat(cmdtyFC.controls.totalUnseededAcresOverride.value)
      self.sumCommodityProjectedAcres += ( isNaN(tmpAcres) ? 0 : tmpAcres)
    })
  }

  addCmdtyIfNotExist(cropCommodityId, isPedigreeInd) {
    let cmdtiesFA: FormArray = this.viewModel.formGroup.controls.commodities as FormArray
    
    let fcCmdty = cmdtiesFA.controls.find( cmdtyFC => cmdtyFC.value.cropCommodityId == cropCommodityId && cmdtyFC.value.isPedigreeInd == isPedigreeInd ) 

    if (!fcCmdty) {
      // add the commodity
      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [cropCommodityId],
        cropCommodityName:              [this.getCommodityName(cropCommodityId) + (isPedigreeInd ? " Pedigreed" : "")],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [isPedigreeInd],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     []
      } ) )

    }
  }

  getCommodityName(cropCommodityId){

    let commodityName = ""

    if (this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length > 0) {

      let cmdty = this.cropCommodityList.collection.find(x => x.cropCommodityId == cropCommodityId)

      if (cmdty) {
        commodityName = cmdty.commodityName
      } 
    }
    
    return commodityName
  }

  getCommodity(cropCommodityId){

    let cmdty = null

    if (this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length > 0) {

      cmdty = this.cropCommodityList.collection.find(x => x.cropCommodityId == cropCommodityId)

    }
    
    return cmdty
  }

  hasAcres(cAcres) {
    if (!cAcres) {
      return false
    }

    let acres = parseFloat(cAcres)

    if (!isNaN(acres ) ) {
      if (acres > 0 ) {
        return true
      }
    }

    return false
  }


  getUpdatedInventoryContract() {

    //make a deep copy
    let updatedInventoryContract = JSON.parse(JSON.stringify(this.inventoryContract));

    // update InventoryContract values
    updatedInventoryContract.fertilizerInd = this.viewModel.formGroup.controls.fertilizerInd.value
    updatedInventoryContract.herbicideInd = this.viewModel.formGroup.controls.herbicideInd.value
    updatedInventoryContract.tilliageInd = this.viewModel.formGroup.controls.tilliageInd.value
    updatedInventoryContract.otherChangesInd =  this.viewModel.formGroup.controls.otherChangesInd.value
    updatedInventoryContract.otherChangesComment = this.viewModel.formGroup.controls.otherChangesComment.value
    updatedInventoryContract.grainFromPrevYearInd = this.viewModel.formGroup.controls.grainFromPrevYearInd.value

    var self = this;   

    // update unseededInventory values
    const formFields: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    formFields.controls.forEach( function(formField : FormArray) {
      // go through each field and update its planting information
      // TODO: update field and legal land autocomplete info 
      let updField = updatedInventoryContract.fields.find( f => f.fieldId == formField.value.fieldId)

      if (updField) {     
        // the field is updated

        updField.displayOrder = formField.value.displayOrder

        if (! formField.value.fieldLabel || formField.value.fieldLabel.trim() == '') {
          updField.fieldLabel = 'UNSPECIFIED'
        } else {
          updField.fieldLabel = formField.value.fieldLabel
        }

        updField.landUpdateType = formField.value.landUpdateType
        updField.legalLandId = formField.value.legalLandId

        updField.otherLegalDescription = formField.value.otherLegalDescription        
        updField.uwComments = formField.value.uwComments 

        for (let i = 0; i < formField.value.plantings.length; i++) {

          let tmpAcres = parseFloat(formField.value.plantings.value[i].acresToBeSeeded)

          let updPlanting = updField.plantings.find( p => (p.fieldId == formField.value.plantings.value[i].fieldId 
                                                          && p.plantingNumber == formField.value.plantings.value[i].plantingNumber) )
    
          if (updPlanting) {
            // update exisitng unseeded values (also covers delete when there is more than 1 planting)
            updPlanting.lastYearCropCommodityId = parseInt( formField.value.plantings.value[i].lastYearCropCommodityVarietyId.split('_')[0] ) || null
            updPlanting.lastYearCropVarietyId = parseInt( formField.value.plantings.value[i].lastYearCropCommodityVarietyId.split('_')[1] ) || null
            updPlanting.inventoryUnseeded.acresToBeSeeded = ( isNaN( tmpAcres) ? null : tmpAcres)
            updPlanting.inventoryUnseeded.cropCommodityId = formField.value.plantings.value[i].cropCommodityId
            updPlanting.inventoryUnseeded.cropVarietyId = formField.value.plantings.value[i].cropVarietyId
            //Get isCropInsuranceEligibleInd and isInventoryCropInd the commodity list of the selected commodity and set it.
            //This is needed to calculate the commodity totals in the backend.
            let cmdty = self.getCommodity(formField.value.plantings.value[i].cropCommodityId)
            updPlanting.inventoryUnseeded.isCropInsuranceEligibleInd = cmdty && cmdty.isCropInsuranceEligibleInd ? cmdty.isCropInsuranceEligibleInd : false
            updPlanting.inventoryUnseeded.isInventoryCropInd = cmdty && cmdty.isInventoryCropInd ? cmdty.isInventoryCropInd : false
            updPlanting.inventoryUnseeded.isUnseededInsurableInd = formField.value.plantings.value[i].isUnseededInsurableInd
            updPlanting.isHiddenOnPrintoutInd = formField.value.plantings.value[i].isHiddenOnPrintoutInd
            updPlanting.inventoryUnseeded.deletedByUserInd = formField.value.plantings.value[i].deletedByUserInd         

            updPlanting.underseededAcres = formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededAcres
            updPlanting.underseededCropVarietyId = formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededVrtyCtrl.cropVarietyId

            updPlanting.underseededInventorySeededForageGuid = formField.value.plantings.value[i].underseededInventorySeededForageGuid    

            // Update seeded part of the planting
            for (let k = 0; k < formField.value.plantings.value[i].inventorySeededGrains.length; k++){

              let formInvSeededGrains  = formField.value.plantings.value[i].inventorySeededGrains.value[k]

              let updInventorySeededGrain = 
                  updPlanting.inventorySeededGrains.find(elem => elem.inventorySeededGrainGuid ==
                                                                 formField.value.plantings.value[i].inventorySeededGrains.value[k].inventorySeededGrainGuid )

              if (updInventorySeededGrain) {

                updInventorySeededGrain.cropCommodityId = formInvSeededGrains.cropCommodityId 
                updInventorySeededGrain.cropVarietyId = formInvSeededGrains.cropVarietyCtrl.cropVarietyId
                updInventorySeededGrain.commodityTypeCode = formInvSeededGrains.commodityTypeCode
                updInventorySeededGrain.isQuantityInsurableInd = formInvSeededGrains.isQuantityInsurableInd
                updInventorySeededGrain.isReplacedInd = formInvSeededGrains.isReplacedInd
                updInventorySeededGrain.isPedigreeInd = formInvSeededGrains.isPedigreeInd
                updInventorySeededGrain.isSpotLossInsurableInd = formInvSeededGrains.isSpotLossInsurableInd
                updInventorySeededGrain.seededDate = formInvSeededGrains.seededDate
                updInventorySeededGrain.seededAcres = formInvSeededGrains.seededAcres
                updInventorySeededGrain.deletedByUserInd = formInvSeededGrains.deletedByUserInd

              } else {
                
                updPlanting.inventorySeededGrains.push (
                  self.getInventorySeededGrainsObjForSave(formInvSeededGrains.inventorySeededGrainGuid, formInvSeededGrains.inventoryFieldGuid, 
                    formInvSeededGrains.cropCommodityId, formInvSeededGrains.cropVarietyCtrl.cropVarietyId, formInvSeededGrains.commodityTypeCode,
                    formInvSeededGrains.isQuantityInsurableInd, formInvSeededGrains.isReplacedInd, formInvSeededGrains.isPedigreeInd, formInvSeededGrains.isSpotLossInsurableInd, 
                    formInvSeededGrains.seededDate, formInvSeededGrains.seededAcres, formInvSeededGrains.deletedByUserInd)
                )
              }
            }
            
          } else {
            
            // add new planting, only if it hasn't been deleted by the user before the planting was saved in the database
            // updField.plantings.push(formField.value.plantings.value[i])
            
            // first deal with the seeded grains if any 
            if (formField.value.plantings.value[i].deletedByUserInd !== true ) {
              
              // get the seeded grain inventory unless it is deleted by the user

              let inventorySeededGrains = []

              if (formField.value.plantings.value[i].inventorySeededGrains ) {

                  for (let k = 0; k < formField.value.plantings.value[i].inventorySeededGrains.length; k++){

                    let formInvSeededGrains  = formField.value.plantings.value[i].inventorySeededGrains.value[k]

                    if (formInvSeededGrains.deletedByUserInd !== true ) {

                      inventorySeededGrains.push (
                        self.getInventorySeededGrainsObjForSave(formInvSeededGrains.inventorySeededGrainGuid, formInvSeededGrains.inventoryFieldGuid, 
                        formInvSeededGrains.cropCommodityId, formInvSeededGrains.cropVarietyCtrl.cropVarietyId, formInvSeededGrains.commodityTypeCode,
                        formInvSeededGrains.isQuantityInsurableInd, formInvSeededGrains.isReplacedInd, formInvSeededGrains.isPedigreeInd, formInvSeededGrains.isSpotLossInsurableInd, 
                        formInvSeededGrains.seededDate, formInvSeededGrains.seededAcres, formInvSeededGrains.deletedByUserInd)
                      )
                    }
                  }
              }

              updField.plantings.push({
                cropYear: formField.value.plantings.value[i].cropYear,
                fieldId: formField.value.plantings.value[i].fieldId,
                insurancePlanId: formField.value.plantings.value[i].insurancePlanId,
                inventoryFieldGuid: null,
                inventorySeededGrains: inventorySeededGrains, // [],
                inventoryUnseeded: {
                    acresToBeSeeded:        (isNaN( tmpAcres) ? null : tmpAcres),
                    cropCommodityId:        formField.value.plantings.value[i].cropCommodityId,
                    cropCommodityName:      formField.value.plantings.value[i].cropCommodityName,
                    cropVarietyId:        formField.value.plantings.value[i].cropVarietyId,
                    deletedByUserInd:       formField.value.plantings.value[i].deletedByUserInd,
                    inventoryFieldGuid:     null, 
                    inventoryUnseededGuid:  null,
                    isUnseededInsurableInd: formField.value.plantings.value[i].isUnseededInsurableInd,
                  },
                  isHiddenOnPrintoutInd: formField.value.plantings.value[i].isHiddenOnPrintoutInd,
                  lastYearCropCommodityId: formField.value.plantings.value[i].lastYearCropCommodityId,
                  lastYearCropCommodityName: formField.value.plantings.value[i].lastYearCropCommodityName,
                  lastYearCropVarietyId: formField.value.plantings.value[i].lastYearCropVarietyId,
                  lastYearCropVarietyName: formField.value.plantings.value[i].lastYearCropVarietyName,
                  plantingNumber: formField.value.plantings.value[i].plantingNumber,
                  underseededAcres:                 formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededAcres,
                  underseededCropVarietyId:         formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededVrtyCtrl.cropVarietyId,
              })

            }
          }
        }


      } else {
        //the field does not exist in the original inventory, we have to add it   
        // first get all plantings and comments

        let tmpPlantings = []

        for (let i = 0; i < formField.value.plantings.length; i++) {

          // now construct the plantings object
          if (formField.value.plantings.value[i].deletedByUserInd !== true ) {

            let tmpAcres = parseFloat(formField.value.plantings.value[i].acresToBeSeeded)

            // get the seeded grain inventory unless it is deleted by the user
            let inventorySeededGrains = []

            for (let k = 0; k < formField.value.plantings.value[i].inventorySeededGrains.length; k++){

              let formInvSeededGrains  = formField.value.plantings.value[i].inventorySeededGrains.value[k]

              if (formInvSeededGrains.deletedByUserInd !== true ) {

                inventorySeededGrains.push (
                  self.getInventorySeededGrainsObjForSave(formInvSeededGrains.inventorySeededGrainGuid, formInvSeededGrains.inventoryFieldGuid, 
                    formInvSeededGrains.cropCommodityId, formInvSeededGrains.cropVarietyCtrl.cropVarietyId, formInvSeededGrains.commodityTypeCode,
                    formInvSeededGrains.isQuantityInsurableInd, formInvSeededGrains.isReplacedInd, formInvSeededGrains.isPedigreeInd, formInvSeededGrains.isSpotLossInsurableInd, 
                    formInvSeededGrains.seededDate, formInvSeededGrains.seededAcres, formInvSeededGrains.deletedByUserInd)
                )
              }
            }

            tmpPlantings.push({
                      cropYear: formField.value.plantings.value[i].cropYear,
                      fieldId: formField.value.plantings.value[i].fieldId,
                      insurancePlanId: formField.value.plantings.value[i].insurancePlanId,
                      inventoryFieldGuid: (formField.value.plantings.value[i].inventoryFieldGuid && formField.value.plantings.value[i].inventoryFieldGuid.length > 0) ? formField.value.plantings.value[i].inventoryFieldGuid : null,
                      inventorySeededGrains: inventorySeededGrains, // [],
                      inventoryUnseeded: {
                          acresToBeSeeded:        (isNaN( tmpAcres) ? null : tmpAcres),
                          cropCommodityId:        formField.value.plantings.value[i].cropCommodityId,
                          cropCommodityName:      formField.value.plantings.value[i].cropCommodityName,
                          deletedByUserInd:       formField.value.plantings.value[i].deletedByUserInd,
                          inventoryFieldGuid:     (formField.value.plantings.value[i].inventoryFieldGuid && formField.value.plantings.value[i].inventoryFieldGuid.length > 0) ? formField.value.plantings.value[i].inventoryFieldGuid : null,
                          inventoryUnseededGuid:  (formField.value.plantings.value[i].inventoryUnseededGuid && formField.value.plantings.value[i].inventoryUnseededGuid.length > 0) ? formField.value.plantings.value[i].inventoryUnseededGuid : null,
                          isUnseededInsurableInd: formField.value.plantings.value[i].isUnseededInsurableInd,                          
                        },
                        isHiddenOnPrintoutInd: formField.value.plantings.value[i].isHiddenOnPrintoutInd,
                        lastYearCropCommodityId: formField.value.plantings.value[i].lastYearCropCommodityId,
                        lastYearCropCommodityName: formField.value.plantings.value[i].lastYearCropCommodityName,
                        lastYearCropVarietyId: formField.value.plantings.value[i].lastYearCropVarietyId,
                        lastYearCropVarietyName: formField.value.plantings.value[i].lastYearCropVarietyName,
                        plantingNumber: formField.value.plantings.value[i].plantingNumber,
                        underseededAcres:                 formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededAcres,
                        underseededCropVarietyId:         formField.value.plantings.value[i].inventorySeededGrains.value[0].underSeededVrtyCtrl.cropVarietyId,
                        underseededInventorySeededForageGuid: formField.value.plantings.value[i].underseededInventorySeededForageGuid  
                    })
          }
        }

        let fieldLabel = 'UNSPECIFIED'
        if (formField.value.fieldLabel && formField.value.fieldLabel.trim() !== '') {
          fieldLabel = formField.value.fieldLabel
        }

        //then add the field to the original structure 
        updatedInventoryContract.fields.push({
          cropYear:                         self.cropYear,
          displayOrder:                     formField.value.displayOrder,
          fieldId:                          (formField.value.fieldId > 0) ? formField.value.fieldId : null,
          fieldLabel:                       fieldLabel, 
          landUpdateType:                   formField.value.landUpdateType,
          legalLandId:                      (formField.value.legalLandId > 0) ? formField.value.legalLandId : null,
          otherLegalDescription:            formField.value.otherLegalDescription,
          transferFromGrowerContractYearId: (formField.value.transferFromGrowerContractYearId) ? formField.value.transferFromGrowerContractYearId : null,
          plantings:                        tmpPlantings,
          uwComments:                       formField.value.uwComments
        })
      }
   })

    //update InventoryContractCommodity values
    let cmdtiesFA: FormArray = this.viewModel.formGroup.controls.commodities as FormArray
      cmdtiesFA.controls.forEach ( function(cmdtyFC: FormGroup){         

        const tempTotalUnseededAcres = 
          isNaN( parseFloat(cmdtyFC.value.totalUnseededAcres)) ? 0 : parseFloat(cmdtyFC.value.totalUnseededAcres)
        
        const tmpTotalUnseededAcresOverride = 
          isNaN( parseFloat(cmdtyFC.value.totalUnseededAcresOverride)) ? 0 : parseFloat(cmdtyFC.value.totalUnseededAcresOverride)

        const tempTotalSeededAcres = 
          isNaN( parseFloat(cmdtyFC.value.totalSeededAcres)) ? 0 : parseFloat(cmdtyFC.value.totalSeededAcres)
        
        const tempTotalSpotLossAcres = 
          isNaN( parseFloat(cmdtyFC.value.totalSpotLossAcres)) ? 0 : parseFloat(cmdtyFC.value.totalSpotLossAcres)

        let tempUpdContractCmdty = updatedInventoryContract.commodities.find( c => c.cropCommodityId == cmdtyFC.value.cropCommodityId && c.isPedigreeInd == cmdtyFC.value.isPedigreeInd)

        if (tempUpdContractCmdty) { // update the current one 
          
          updatedInventoryContract.commodities.forEach(updCmty => {

            if (updCmty.cropCommodityId == cmdtyFC.value.cropCommodityId && updCmty.isPedigreeInd == cmdtyFC.value.isPedigreeInd) {

              updCmty.totalSeededAcres = cmdtyFC.value.totalSeededAcres
              updCmty.totalSpotLossAcres = cmdtyFC.value.totalSpotLossAcres
              updCmty.isPedigreeInd = cmdtyFC.value.isPedigreeInd
              updCmty.totalUnseededAcres = cmdtyFC.value.totalUnseededAcres
              updCmty.totalUnseededAcresOverride = cmdtyFC.value.totalUnseededAcresOverride

            }
          })

        } else {

          //add new
          if ( tempTotalUnseededAcres > 0 || tmpTotalUnseededAcresOverride > 0 || tempTotalSeededAcres > 0 || tempTotalSpotLossAcres > 0 ) {

            updatedInventoryContract.commodities.push ({
              cropCommodityId: cmdtyFC.value.cropCommodityId,
              cropCommodityName: cmdtyFC.value.cropCommodityName,
              inventoryContractCommodityGuid: cmdtyFC.value.inventoryContractCommodityGuid,
              inventoryContractGuid: cmdtyFC.value.inventoryContractGuid,
              totalSeededAcres: cmdtyFC.value.totalSeededAcres,
              totalSpotLossAcres: cmdtyFC.value.totalSpotLossAcres,
              isPedigreeInd: cmdtyFC.value.isPedigreeInd,
              totalUnseededAcres: cmdtyFC.value.totalUnseededAcres,
              totalUnseededAcresOverride: cmdtyFC.value.totalUnseededAcresOverride
            })
          }            
        }        
      })  

      // remove the empty commodities
      for( var i = 0; i < updatedInventoryContract.commodities.length; i++){ 

        // for unseeded crop report
        let tempUnseededAcres = 
          isNaN( parseFloat(updatedInventoryContract.commodities[i].totalUnseededAcres)) ? 0 : parseFloat(updatedInventoryContract.commodities[i].totalUnseededAcres)
        
        let tmpUnseededAcresOverride = 
          isNaN( parseFloat(updatedInventoryContract.commodities[i].totalUnseededAcresOverride)) ? 0 : parseFloat(updatedInventoryContract.commodities[i].totalUnseededAcresOverride)
        
        // for seeded crop report
        let tempTotalSeededAcres = 
          isNaN( parseFloat(updatedInventoryContract.commodities[i].totalSeededAcres)) ? 0 : parseFloat(updatedInventoryContract.commodities[i].totalSeededAcres)
      
        let tmpTotalSpotLossAcres = 
          isNaN( parseFloat(updatedInventoryContract.commodities[i].totalSpotLossAcres)) ? 0 : parseFloat(updatedInventoryContract.commodities[i].totalSpotLossAcres)

        if (tempUnseededAcres == 0 && tmpUnseededAcresOverride == 0 && tempTotalSeededAcres == 0 && tmpTotalSpotLossAcres == 0 ) {

            updatedInventoryContract.commodities.splice(i,1)
            i--

        }      
      }

    return updatedInventoryContract
  }

  isFormValid() {

    // TODO: add more validations as needed 

    // check for empty legal land / field id for new fields
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    let landHasName = true

    //Other changes comment is required if checkbox is checked
    if(this.viewModel.formGroup.controls.otherChangesInd && this.viewModel.formGroup.controls.otherChangesInd.value){
      if(this.viewModel.formGroup.controls.otherChangesComment && this.viewModel.formGroup.controls.otherChangesComment.value.trim().length == 0 ) {
        alert ("Please enter a valid comment for Other Changes Comment")
        return false
      }
    }

    for (var i = 0;  i < flds.controls.length ; i++) {
  
      // check for duplicates values in field labels and other description
      for (var n = i + 1;  n < flds.controls.length ; n++) {

        if ( flds.controls[n].value.fieldLabel && flds.controls[i].value.fieldLabel && 
             (flds.controls[i].value.fieldLabel).toUpperCase().trim() == (flds.controls[n].value.fieldLabel).toUpperCase().trim() &&
              flds.controls[i].value.fieldLabel.toUpperCase() !== "UNSPECIFIED" &&
              // both legals have the same otherDescription 
              ( 
                ( 
                  flds.controls[i].value.otherLegalDescription && flds.controls[n].value.otherLegalDescription && // they might be empty
                  (flds.controls[i].value.otherLegalDescription).toUpperCase().trim() == (flds.controls[n].value.otherLegalDescription).toUpperCase().trim() 
                )
              || // or both have no values
                ( 
                  !flds.controls[i].value.otherLegalDescription && !flds.controls[n].value.otherLegalDescription
                )
              )
              ) {

          alert("The field name " + flds.controls[i].value.fieldLabel + 
          " and the legal location " + flds.controls[i].value.otherLegalDescription + 
          " exist more than once. The inventory cannot be saved.")

          return false
        }

      }

      // now check for underseeded variety but no underseeded acres
      // go through each planting 
      for (let j = 0; j < flds.controls[i].value.plantings.length; j++) {

        // check that variety is entered for each selected crop  
        for ( let k = 0; k < flds.controls[i].value.plantings.value[j].inventorySeededGrains.length; k++) {

          if (flds.controls[i].value.plantings.value[j].inventorySeededGrains.value[k].cropCommodityId && 
            !flds.controls[i].value.plantings.value[j].inventorySeededGrains.value[k].cropVarietyCtrl.cropVarietyId ) {
  
              alert ("Please enter variety for each selected crop. Field ID: " + flds.controls[i].value.fieldId + 
                " with Field Name: " + flds.controls[i].value.fieldLabel + 
                " has a seeded crop without a variety.  The inventory was not saved.")
              return false
            }
        }

        if (flds.controls[i].value.plantings.value[j].inventorySeededGrains.value[0].underSeededVrtyCtrl.cropVarietyId &&
          !flds.controls[i].value.plantings.value[j].inventorySeededGrains.value[0].underSeededAcres ) {

            alert ("Field ID: " + flds.controls[i].value.fieldId + 
              " with Field Name: " + flds.controls[i].value.fieldLabel + 
              " has Underseeded Variety but no Underseeded Acres. Please enter Underseeded Acres. The inventory was not saved.")
            return false

          }
      }
    }

    return landHasName
  }

  // TODO: currently there is no haetus links on rolloverInventory. 
  // Set permissions for Save button when the haetus links appear in the api point
  onSave() {

    if ( !this.isFormValid() ){
      return
    }
    
    // prepare the updatedInventoryContract 
    const newInventoryContract: InventoryContract = this.getUpdatedInventoryContract()

    if (this.inventoryContract.inventoryContractGuid) {
      this.store.dispatch(UpdateInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, newInventoryContract))
    } else {
      // add new
      this.store.dispatch(AddNewInventoryContract(INVENTORY_COMPONENT_ID, this.policyId, newInventoryContract))
    }

    this.hasDataChanged = false
    this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, this.hasDataChanged ));
  }

  onAddPlanting(planting) {

    var self = this

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    flds.controls.forEach( function(field : FormControl) {

      if (field.value.fieldId == planting.value.fieldId) {

        let pltgInventorySeededGrains = new FormArray ([])

        pltgInventorySeededGrains.push( self.fb.group( 
          addSeededGrainsObject(planting.value.inventoryFieldGuid, null, null, null, false, <InventorySeededGrain>[])
        ) )

        field.value.plantings.push( self.fb.group( 

          addPlantingObject(planting.value.cropYear, planting.value.fieldId, planting.value.insurancePlanId, planting.value.inventoryFieldGuid, 
            planting.value.lastYearCropCommodityId, planting.value.lastYearCropCommodityName, planting.value.lastYearCropVarietyId, planting.value.lastYearCropVarietyName,
            planting.value.plantingNumber + 1, false, null,
            <InventoryUnseeded>{}, pltgInventorySeededGrains, new FormArray ([]) )

         ) )
         
         self.isMyFormDirty()
      }
    })
  }

  onInventoryCommentsDone(fieldId: number, uwComments: UnderwritingComment[]) {

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    updateComments(fieldId, uwComments, flds)

    this.cdr.detectChanges()

    this.isMyFormDirty()

  }

  onCancel() {

    if ( confirm("Are you sure you want to clear all unsaved changes on the screen? There is no way to undo this action.") ) {
      // reload the page
      if (this.inventoryContract && this.inventoryContract.inventoryContractGuid ) {
        // load the existing one from the database
        this.store.dispatch(LoadInventoryContract(this.componentId, this.inventoryContract.inventoryContractGuid ))
      } else {
        // prepare the new inventory contract
        this.store.dispatch(RolloverInventoryContract(this.componentId, this.policyId))
      }

      this.hasDataChanged = false
      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, this.hasDataChanged ));

      displaySuccessSnackbar(this.snackbarService, "Unsaved changes have been cleared successfully.")
    }
    
  }

  onPrint() {

    let reportName = this.growerContract.growerName + "-Inventory" 
    reportName = reportName.replace(".", "")
    this.store.dispatch(GetInventoryReport(reportName, this.policyId, "", INSURANCE_PLAN.GRAIN.toString(), "", "", "", "", ""))

  }

// land management
onDeleteField(field) {
    
  if (field.value.isNewFieldUI == true ) {

    this.deleteNewField(field)

  } else {

    const dataToSend : RemoveFieldPopupData = {
      fieldId: field.value.fieldId,
      fieldLabel: field.value.fieldLabel,
      policyId: this.policyId,
      hasInventory: fieldHasInventory(field),  
      hasComments: isThereAnyCommentForField(field),
      landData: {
        landUpdateType: ""
      }  
    }

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    deleteFormField(field, flds, this.dialog, dataToSend, this.cdr)
    }
  }

  deleteNewField(field) {

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    deleteNewFormField(field, flds)

    this.isMyFormDirty()

  }

  onEditLocation(field) {

    if (field.value.isNewFieldUI) {

      this.deleteNewField(field)
      this.onAddNewField()

    } else {

      const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

      const dataToSend : AddLandPopupData = {
        fieldId: field.value.fieldId,
        fieldLabel: field.value.fieldLabel,
        cropYear: this.cropYear,
        policyId: this.policyId,
        insurancePlanId: this.insurancePlanId,
        annualFieldDetailId: field.value.annualFieldDetailId,
        otherLegalDescription: field.value.otherLegalDescription
      }

      openAddEditLandPopup(this.fb, flds, this.dialog, dataToSend, field.value.maxDisplayOrder, false, this.cdr)
    }

  }

  onAddNewField() {

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray

    AddNewFormField(this.fb, flds, this.dialog, this.cropYear, this.policyId, this.insurancePlanId, this.cdr)

  }
  // end of land management

  
  addEmptyPlantingObject (fieldId) {
    return addPlantingObject( this.cropYear, fieldId, this.insurancePlanId, '', '', '', '', '', 1, false, null, <InventoryUnseeded>{}, new FormArray ([]), new FormArray ([]))
  }

  

  isMyFormDirty() {

    if (!this.hasYieldData) {

      this.hasDataChanged = this.isMyFormReallyDirty()
      this.store.dispatch(setFormStateUnsaved(INVENTORY_COMPONENT_ID, this.hasDataChanged ));
    }

  }

  isMyFormReallyDirty() : boolean {

    // compare the original inventory contract data with the one in the form and set the flag if anything changed
    // watch if the app starts running too slow

    if (!this.inventoryContract) return false

    const frmMain = this.viewModel.formGroup as FormGroup

    if ( areNotEqual (this.inventoryContract.fertilizerInd, frmMain.controls.fertilizerInd.value) || 
         areNotEqual (this.inventoryContract.herbicideInd, frmMain.controls.herbicideInd.value)	||
         areNotEqual (this.inventoryContract.tilliageInd, frmMain.controls.tilliageInd.value) ||
         areNotEqual (this.inventoryContract.otherChangesInd, frmMain.controls.otherChangesInd.value) ||          
         areNotEqual (this.inventoryContract.otherChangesComment, frmMain.controls.otherChangesComment.value) ||          
         areNullableBooleanNotEqual (this.inventoryContract.grainFromPrevYearInd, frmMain.controls.grainFromPrevYearInd.value) ) {
        
        return true
    }

    // check the commodity total projected values
    const formCmdties: FormArray = frmMain.controls.commodities as FormArray
    for (let j = 0; j < formCmdties.controls.length; j++){

      let frmCmdty = formCmdties.controls[j] as FormArray
      let originalCmdty = this.inventoryContract.commodities.find( f => f.cropCommodityId == frmCmdty.value.cropCommodityId && f.isPedigreeInd == frmCmdty.value.isPedigreeInd )

      if (originalCmdty) {
        if (areNotEqual(frmCmdty.value.totalUnseededAcresOverride, originalCmdty.totalUnseededAcresOverride)) {

          return true
        }
      } else {
        // check for totalUnseededAcresOverride that are not saved yet
        if (frmCmdty.value.totalUnseededAcresOverride) {

          return true
        }
      }
    }

    // check if the number of fields has changed
    if ( this.inventoryContract.fields.length !== frmMain.controls.fields.value.length  ) {

      return true
    }

    // start checking if the information for each field and planting was changed from the original one
    const formFields: FormArray = frmMain.controls.fields as FormArray

    for (let i = 0; i < formFields.controls.length; i++){
      let frmField = formFields.controls[i] as FormArray

      if (frmField.value.deletedByUserInd == true ) {
        return true
      }

      let originalField = this.inventoryContract.fields.find( f => f.fieldId == frmField.value.fieldId)

      if (originalField) {

        // check if the field name, legal location or number of plantings for each field have changed 
        if (frmField.value.displayOrder != originalField.displayOrder ||
            frmField.value.fieldLabel != originalField.fieldLabel ||
            frmField.value.legalLandId != originalField.legalLandId ||  
            frmField.value.otherLegalDescription != originalField.otherLegalDescription ) {

            //console.log("5")
            return true
        }

        // check the plantings
        for (let k = 0; k < frmField.value.plantings.controls.length; k++){
          let frmPlanting = frmField.value.plantings.controls[k] as FormArray

          let originalPlanting = originalField.plantings.find( p => p.plantingNumber == frmPlanting.value.plantingNumber)

          if (originalPlanting) {

            const lastYearCropCommodityId = parseInt( frmPlanting.value.lastYearCropCommodityVarietyId.split('_')[0] ) || null
            const lastYearCropVarietyId = parseInt( frmPlanting.value.lastYearCropCommodityVarietyId.split('_')[1] ) || null
            
            // the planting was found in the original resource, check if anything has changed
            if ( frmPlanting.value.deletedByUserInd == true ||
                areNotEqual(lastYearCropCommodityId, originalPlanting.lastYearCropCommodityId) ||
                areNotEqual(lastYearCropVarietyId, originalPlanting.lastYearCropVarietyId) ||
                areNotEqual(frmPlanting.value.isHiddenOnPrintoutInd, originalPlanting.isHiddenOnPrintoutInd)  
              ) {

                  return true
              }

              if ( originalPlanting.inventoryUnseeded && 
                  (frmPlanting.value.deletedByUserInd == true ||
                    areNotEqual(frmPlanting.value.acresToBeSeeded, originalPlanting.inventoryUnseeded.acresToBeSeeded) ||
                    areNotEqual(frmPlanting.value.cropCommodityId, originalPlanting.inventoryUnseeded.cropCommodityId) ||
                    areNotEqual(frmPlanting.value.cropVarietyId, originalPlanting.inventoryUnseeded.cropVarietyId) ||
                    areNotEqual(frmPlanting.value.isUnseededInsurableInd, originalPlanting.inventoryUnseeded.isUnseededInsurableInd)   
                  )
              ) {

                  return true
              }


            // now check inventory seeded grains 
            for (let n = 0; n < frmPlanting.value.inventorySeededGrains.controls.length; n++) {
              
              let frmInvSeededGrains = frmPlanting.value.inventorySeededGrains.controls[n] as FormArray
              let originalInvSeededGrains = originalPlanting.inventorySeededGrains.find( p => p.inventorySeededGrainGuid == frmInvSeededGrains.value.inventorySeededGrainGuid)

              if (originalInvSeededGrains) {

                if ( n == 0 ) {
                  // check underseeded acres and variety
                  if ( areNotEqual(frmInvSeededGrains.value.underSeededAcres, originalPlanting.underseededAcres) ||
                       areNotEqual(frmInvSeededGrains.value.underSeededVrtyCtrl.cropVarietyId, originalPlanting.underseededCropVarietyId)
                  ) {

                    return true
                  }
                }

                if ( frmInvSeededGrains.value.deletedByUserInd == true ||
                      areNotEqual(frmInvSeededGrains.value.cropCommodityId, originalInvSeededGrains.cropCommodityId) ||
                      areNotEqual(frmInvSeededGrains.value.cropVarietyCtrl.cropVarietyId, originalInvSeededGrains.cropVarietyId) ||
                      areNotEqual(frmInvSeededGrains.value.commodityTypeCode, originalInvSeededGrains.commodityTypeCode) ||
                      areNotEqual(frmInvSeededGrains.value.isQuantityInsurableInd, originalInvSeededGrains.isQuantityInsurableInd) ||
                      areNotEqual(frmInvSeededGrains.value.isReplacedInd, originalInvSeededGrains.isReplacedInd) ||
                      areNotEqual(frmInvSeededGrains.value.isPedigreeInd, originalInvSeededGrains.isPedigreeInd)  ||
                      areDatesNotEqual(frmInvSeededGrains.value.seededDate, originalInvSeededGrains.seededDate) || 
                      areNotEqual(frmInvSeededGrains.value.seededAcres, originalInvSeededGrains.seededAcres) ||
                      areNotEqual(frmInvSeededGrains.value.isSpotLossInsurableInd, originalInvSeededGrains.isSpotLossInsurableInd)
                  ) {
                    
                  return true
                }
              } else {
                // new inventorySeededGrains record
                // check if it's not deleted by the user 
                
                if (frmInvSeededGrains.value.deletedByUserInd !== true && 
                    (frmInvSeededGrains.value.cropCommodityId ||
                    frmInvSeededGrains.value.cropVarietyCtrl.cropCommodityId ||  
                    frmInvSeededGrains.value.seededDate.toString() !== 'Invalid Date' ||  
                    frmInvSeededGrains.value.seededAcres || 
                    frmInvSeededGrains.value.isHiddenOnPrintoutInd !== false ||
                    frmInvSeededGrains.value.isPedigreeInd !== false ||
                    frmInvSeededGrains.value.isQuantityInsurableInd !== false ||
                    frmInvSeededGrains.value.isReplacedInd !== false ||
                    frmInvSeededGrains.value.isSpotLossInsurableInd !== false ||
                    frmInvSeededGrains.value.underSeededVrtyCtrl.cropCommodityId ||
                    frmInvSeededGrains.value.underseededAcres)) {

                    return true
                }

              }
            }

          } else {
            // the planting was not found in the original resourse, check if it's an empty or deleted planting
            if (frmPlanting.value.deletedByUserInd !== true && 
                (frmPlanting.value.acresToBeSeeded || frmPlanting.value.cropCommodityId || 
                  frmPlanting.value.inventorySeededGrains.controls[0].value.cropCommodityId || 
                  frmPlanting.value.inventorySeededGrains.controls[0].value.seededDate ||  
                  frmPlanting.value.inventorySeededGrains.controls[0].value.seededAcres )) {
                  // a new planting was added

                  return true
                }
          }
        }

        //check the uwcomments
        for (let k = 0; k < frmField.value.uwComments.length; k++) {

          // now check if anything changed in the comments
          if (frmField.value.uwComments.length != originalField.uwComments.length) {

            return true
          }
          
          // go inside the comments and check for changes there
          for (let n = 0; n < frmField.value.uwComments.length; n++){
            
            let frmUwComments = frmField.value.uwComments[n] 

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

    return false
  }

  getInventorySeededGrainsObjForSave(inventorySeededGrainGuid, inventoryFieldGuid, cropCommodityId, cropVarietyId, commodityTypeCode,
                                    isQuantityInsurableInd, isReplacedInd, isPedigreeInd, isSpotLossInsurableInd, 
                                    seededDate: string, seededAcres, deletedByUserInd) {
    
    if (inventorySeededGrainGuid.indexOf("newGUID_") > -1) {
      inventorySeededGrainGuid = null      
    }

    return {
      inventorySeededGrainGuid:   inventorySeededGrainGuid,
      inventoryFieldGuid:         inventoryFieldGuid,
      cropCommodityId:            cropCommodityId,
      cropCommodityName:          "", 
      cropVarietyId:              cropVarietyId,
      cropVarietyName:            "", 
      commodityTypeCode:          commodityTypeCode, 
      commodityTypeDesc:          "",
      isQuantityInsurableInd:     isQuantityInsurableInd,
      isReplacedInd:              isReplacedInd,
      isPedigreeInd:              isPedigreeInd,
      isSpotLossInsurableInd:     isSpotLossInsurableInd,
      seededDate:                 seededDate, 
      seededAcres:                seededAcres,
      deletedByUserInd:           deletedByUserInd
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    
    let fields = this.getViewModel().formGroup.controls.fields as FormArray
    dragField(event, fields)

    this.isMyFormDirty()
  }

  onDeleteInventory() {

    //Ask for confirmation before deleting all Inventory data
    if ( confirm("You are about to delete all unseeded and seeded inventory data for the policy. Do you want to continue?") ) {

      if (this.inventoryContract && this.policyId) {

        this.store.dispatch(DeleteInventoryContract(INVENTORY_COMPONENT_ID, 
                                this.inventoryContract.inventoryContractGuid, 
                                this.policyId, 
                                this.inventoryContract.etag))
      }
      
    }
  }

  isLinkedField(fieldId): boolean {
    return isLinkedFieldCommon(fieldId, this.inventoryContract)
  }

  linkedFieldTooltip(fieldId): string {
    return linkedFieldTooltipCommon(fieldId, this.inventoryContract)
  }

  isLinkedPlanting(fieldId, inventoryFieldGuid) : boolean{
    return isLinkedPlantingCommon(fieldId, inventoryFieldGuid, this.inventoryContract)
  }

  linkedPlantingTooltip(fieldId, inventoryFieldGuid): string {
    return linkedPlantingTooltipCommon(fieldId, inventoryFieldGuid, this.inventoryContract, this.insurancePlanId)
  }

  toggleHiddenOnPrintout(fieldIndex, plantingIndex) {  

    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    
    // get the current value
    let isHiddenOnPrintoutInd = pltg.controls['isHiddenOnPrintoutInd'].value
    pltg.controls['isHiddenOnPrintoutInd'].setValue(!isHiddenOnPrintoutInd) // isHiddenOnPrintoutInd is the same for seeded and unseeded. Keep the two flags in sync 
    
    this.isMyFormDirty()
  }

  isPlantingHiddenOnPrintout(fieldIndex, plantingIndex) {
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]

    return pltg.controls['isHiddenOnPrintoutInd'].value
  }

  isFieldHiddenOnPrintout(fieldIndex) {

    // return false unless at least one planting is not hidden
    const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
    for (let i = 0; i < flds.controls[fieldIndex]['controls']['plantings'].value.controls.length; i++ ) {

      let pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[i]
      let isHiddenOnPrintoutInd = pltg.controls['isHiddenOnPrintoutInd'].value

      if ( isHiddenOnPrintoutInd == false ) {
        return false 
      }
    }

    return true // all plantings are hidden
  }
  
}
