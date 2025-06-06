
import { ChangeDetectionStrategy, Component, SimpleChanges} from '@angular/core';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { GrainInventoryComponent } from "../grain-inventory.component";
import { makeTitleCase } from 'src/app/utils'; 
import { CROP_COMMODITY_UNSPECIFIED } from 'src/app/utils/constants';
import { CropVarietyCommodityType } from 'src/app/conversion/models';
import { CropVarietyOptionsType, roundUpDecimalAcres } from '../../inventory-common';
import { AddPlantingPopupData, LinkPlantingComponent } from '../../link-planting/link-planting.component';
import { LoadInventoryContract } from 'src/app/store/inventory/inventory.actions';
import {ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'grain-seeded-inventory',
  templateUrl: './grain-seeded-inventory.component.html',
  styleUrls: ['./grain-seeded-inventory.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
})

export class GrainSeededInventoryComponent extends GrainInventoryComponent { 

  seededCommodityOptions = [];

  seededVarietyOptions = [];
  filteredSeededVarietyOptions: CropVarietyOptionsType[];  

  underSeededVarietyOptions = [];
  filteredUnderSeededVarietyOptions: CropVarietyOptionsType[];  

  isHiddenFieldInTotals = false;

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
  
    this.ngOnChanges3(changes);
  }

  ngOnChanges3(changes: SimpleChanges) {

    // populate seeded commodities and varieties
    if ((changes.cropCommodityList && this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length) || 
      (changes.underSeededCropCommodityList && this.underSeededCropCommodityList && this.underSeededCropCommodityList.collection && this.underSeededCropCommodityList.collection.length )) {

        this.populateSeededCommodityAndVarietyOptions();
    }

    // populate underseeded varieties
    if (changes.underSeededCropCommodityList && 
      this.underSeededCropCommodityList && 
      this.underSeededCropCommodityList.collection && 
      this.underSeededCropCommodityList.collection.length > 0
      ) {

      this.populateUnderseededVarieties();
    }

    // create commodity totals table
    if ( changes.inventoryContract && this.inventoryContract && this.inventoryContract.commodities && 
      this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length &&
      this.underSeededCropCommodityList && this.underSeededCropCommodityList.collection && this.underSeededCropCommodityList.collection.length
    ) {
      this.addAllCommodities()
      this.geInvSeededTotals()
      this.checkForHiddenFieldInTotals()
    }
  }


  getSeededCommodityOptionById(cropCommodityId) {
    
    if ( cropCommodityId && this.seededCommodityOptions && this.seededCommodityOptions.length > 0 ) {
      return this.seededCommodityOptions.find(el => el.cropCommodityId == cropCommodityId)
    }

    return null
  }

  // crop variety search
  searchVariety(value, fieldIndex, plantingIndex, invSeededIndex) {
  
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]
    const selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value

      const varietyName = (( typeof value === 'string') ? value : value?.varietyName)

      if (varietyName) {
        const filterValue = varietyName.toLowerCase()

        if (selectedCropCommodityId != CROP_COMMODITY_UNSPECIFIED.ID) {

          this.filteredSeededVarietyOptions = 
            this.seededVarietyOptions.filter(option => 
                                            ( option.varietyName.toLowerCase().includes(filterValue) 
                                              && option.cropCommodityId == selectedCropCommodityId) )
        } else {
          this.filteredSeededVarietyOptions = this.seededVarietyOptions.filter(option => option.varietyName.toLowerCase().includes(filterValue) )
        }
        
      } else {
        this.filteredSeededVarietyOptions = this.seededVarietyOptions.slice()
      }
  }

  displayVarietyFn(vrty: CropVarietyOptionsType): string {
    return vrty && vrty.varietyName ? makeTitleCase( vrty.varietyName)  : '';
  }

  cropCommodityChange(event, fieldIndex, plantingIndex, invSeededIndex) {

    // clear the previously selected value
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    invSeeded.controls['cropVarietyCtrl'].setValue({      
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
    })

    invSeeded.controls['commodityTypeCode'].setValue(null)
    invSeeded.controls['commodityTypeDesc'].setValue('')
    invSeeded.controls['commodityTypeOptions'].setValue([])
    
    if (event.value) {
      this.filteredSeededVarietyOptions = this.seededVarietyOptions.filter(option => option.cropCommodityId == event.value )

      // check insurable qty 
      let seededCommodityOption = this.getSeededCommodityOptionById(event.value)
      invSeeded.controls['isQuantityInsurableInd'].setValue(seededCommodityOption && seededCommodityOption.isQuantityInsurableInd)
      invSeeded.controls['isSpotLossInsurableInd'].setValue(false)

    } else {

      this.filteredSeededVarietyOptions = this.seededVarietyOptions.slice()

      invSeeded.controls['isPedigreeInd'].setValue(false)
      invSeeded.controls['isQuantityInsurableInd'].setValue(false)
      invSeeded.controls['isSpotLossInsurableInd'].setValue(false)
      invSeeded.controls['isReplacedInd'].setValue(false)

    }

    this.geInvSeededTotals()

    this.isMyFormDirty()
  }

  clearCommoditySelection(fieldIndex, plantingIndex, invSeededIndex){

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    // clear selected commodity type
    invSeeded.controls['commodityTypeOptions'].setValue([])
    invSeeded.controls['commodityTypeCode'].setValue(null)
    invSeeded.controls['commodityTypeDesc'].setValue('')

    invSeeded.controls['isPedigreeInd'].setValue(false)
    invSeeded.controls['isQuantityInsurableInd'].setValue(false)
    invSeeded.controls['isSpotLossInsurableInd'].setValue(false)

    this.isMyFormDirty(); // check for changes
  }

  varietySelected(event, fieldIndex, plantingIndex, invSeededIndex) {
    const tempCropCmdtyId = event.option.value.cropCommodityId
    // set commodity type
    let tempCmodityTypeDesc = event.option.value.cropVarietyCommodityTypes

    this.setCropCommodity(tempCropCmdtyId, tempCmodityTypeDesc, fieldIndex, plantingIndex, invSeededIndex);
  }
  
  setCropCommodity(tempCropCmdtyId, tempCmodityTypeDesc, fieldIndex, plantingIndex, invSeededIndex) {
    var self = this

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    let invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (tempCropCmdtyId) {
      // set crop type
      invSeeded.controls['cropCommodityId'].setValue(tempCropCmdtyId)

      if( tempCmodityTypeDesc ) {

        if (tempCmodityTypeDesc.length > 1) {

          // set options for commodity/vrty types drop down
          let commodityTypeOptions = []

          tempCmodityTypeDesc.forEach(opt => {
              commodityTypeOptions.push ({
              commodityTypeCode: opt.commodityTypeCode ,
              commodityTypeDesc: opt.description
            })
          })

          invSeeded.controls['commodityTypeOptions'].setValue(commodityTypeOptions)

        } else {
          invSeeded.controls['commodityTypeOptions'].setValue([])
          invSeeded.controls['commodityTypeCode'].setValue(tempCmodityTypeDesc[0].commodityTypeCode)
          invSeeded.controls['commodityTypeDesc'].setValue(tempCmodityTypeDesc[0].description)

        }
      }

      // check quantity Insurable checkbox
      let seededCommodityOption = this.getSeededCommodityOptionById(tempCropCmdtyId)
      invSeeded.controls['isQuantityInsurableInd'].setValue(seededCommodityOption && seededCommodityOption.isQuantityInsurableInd)
      invSeeded.controls['isSpotLossInsurableInd'].setValue(false)

    } else {
      // clear selected commodity type
      invSeeded.controls['commodityTypeOptions'].setValue([])
      invSeeded.controls['commodityTypeCode'].setValue(null)
      invSeeded.controls['commodityTypeDesc'].setValue('')

      invSeeded.controls['isPedigreeInd'].setValue(false)
      invSeeded.controls['isQuantityInsurableInd'].setValue(false)
      invSeeded.controls['isSpotLossInsurableInd'].setValue(false)
    }

    this.isMyFormDirty(); // check for changes
  }

  // SEEDED COMMODITIES AND VARIETIES
  populateSeededCommodityAndVarietyOptions() {
 
    var self = this

    this.seededCommodityOptions = []
    this.seededVarietyOptions = [];

    // add empty records
    this.seededCommodityOptions.push ({
      commodityName: CROP_COMMODITY_UNSPECIFIED.NAME,
      cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
      insurancePlanId: null,
      isQuantityInsurableInd: false,
      isSpotLossInsurableInd: false
    })

    this.seededVarietyOptions.push ({
      cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
      cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
      varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
      cropVarietyCommodityTypes: <CropVarietyCommodityType>[]
    })

    // Populate GRAIN Commodities and Varieties
    if ( this.cropCommodityList && this.cropCommodityList.collection && this.cropCommodityList.collection.length ) {
      this.cropCommodityList.collection.forEach( cmdty => {

        this.seededCommodityOptions.push ({
          commodityName: cmdty.commodityName,
          cropCommodityId: cmdty.cropCommodityId,
          insurancePlanId: cmdty.insurancePlanId,
          isQuantityInsurableInd: true,    // Always true for GRAIN
          isSpotLossInsurableInd: true     // Always true for GRAIN
        })
    
        cmdty.cropVariety.forEach( vrty => {
          self.seededVarietyOptions.push ({
            cropCommodityId: vrty.cropCommodityId,
            cropVarietyId: vrty.cropVarietyId,
            varietyName: vrty.varietyName  ,
            cropVarietyCommodityTypes: vrty.cropVarietyCommodityTypes, 
          })
        })
      })
    }

    // Populate FORAGE Commodities and Varieties
    if ( this.underSeededCropCommodityList && this.underSeededCropCommodityList.collection && this.underSeededCropCommodityList.collection.length ) { 
      this.underSeededCropCommodityList.collection.forEach( uCmdty => {

        this.seededCommodityOptions.push ({
          commodityName: uCmdty.commodityName,
          cropCommodityId: uCmdty.cropCommodityId,
          insurancePlanId: uCmdty.insurancePlanId,
          isQuantityInsurableInd: false,                                      // Always false for FORAGE
          isSpotLossInsurableInd: (uCmdty.commodityName == 'FORAGE SEED')     // Only allowed for FORAGE SEED
        })
    
        uCmdty.cropVariety.forEach( uVrty => {
          self.seededVarietyOptions.push ({
            cropCommodityId: uVrty.cropCommodityId,
            cropVarietyId: uVrty.cropVarietyId,
            varietyName: uVrty.varietyName  ,
            cropVarietyCommodityTypes: uVrty.cropVarietyCommodityTypes, 
          })
        })
      })
    }
  }


  // UNDERSEEDED VARIETIES
  populateUnderseededVarieties() {
 
    var self = this

    this.underSeededVarietyOptions = [];

    this.underSeededVarietyOptions.push ({
      cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
      cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
      varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
      cropVarietyCommodityTypes: <CropVarietyCommodityType>[]
    })

    this.underSeededCropCommodityList.collection.forEach( uCmdty => {

      uCmdty.cropVariety.forEach( uVrty => {
        if (uVrty && uVrty.isUnderseedingEligibleInd == true) {
          self.underSeededVarietyOptions.push ({
            cropCommodityId: uVrty.cropCommodityId,
            cropVarietyId: uVrty.cropVarietyId,
            varietyName: uVrty.varietyName  ,
            cropVarietyCommodityTypes: uVrty.cropVarietyCommodityTypes, 
          })
        }

      })
    })
  }

  searchUnderSeededVariety(value) {

    const varietyName = (( typeof value === 'string') ? value : value?.varietyName)

    if (varietyName) {
      const filterValue = varietyName.toLowerCase()

      this.filteredUnderSeededVarietyOptions = this.underSeededVarietyOptions.filter(option => option.varietyName.toLowerCase().includes(filterValue) )

    } else {
      this.filteredUnderSeededVarietyOptions = this.underSeededVarietyOptions.slice()
    }
  }

  varietyFocus(fieldIndex, plantingIndex, invSeededIndex) {

    // prepare the list of varieties based on the selected crop id
    
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]
	
    let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value

    if (selectedCropCommodityId) {

      this.filteredSeededVarietyOptions = 
            this.seededVarietyOptions.filter(option => 
                                            ( option.cropCommodityId == selectedCropCommodityId) )

    } else {
      
      //return all varieties
      this.filteredSeededVarietyOptions = this.seededVarietyOptions.slice()

    }
  }

  underseededVarietyFocus() {

    this.filteredUnderSeededVarietyOptions = this.underSeededVarietyOptions.slice()

  }

   // GET COMMODITIES to prepare the commodity totals table

   addAllCommodities() {
    // first add all commodities
    // then update them based on what's coming from the backend

    let cmdtiesFA: UntypedFormArray = this.viewModel.formGroup.controls.commodities as UntypedFormArray
    cmdtiesFA.clear()

    var self = this
    this.cropCommodityList.collection.forEach( ccm => {

      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [ccm.cropCommodityId],
        cropCommodityName:              [ccm.commodityName],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [false],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     [],
        isVisible:                     [false],
      } ) )

      // add pedigreed commodity for each commodity
      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [ccm.cropCommodityId],
        cropCommodityName:              [ccm.commodityName + " Pedigreed"],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [true],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     [],
        isVisible:                      [false],
      } ) )
    } )

    // Add FORAGE SEED, as the only Forage commodity that could be insurable and appear in the commodity totals.
    let forageSeedCmdty = this.underSeededCropCommodityList.collection.find(el => el.commodityName == "FORAGE SEED")
    if (forageSeedCmdty) {

      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [forageSeedCmdty.cropCommodityId],
        cropCommodityName:              [forageSeedCmdty.commodityName],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [false],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     [],
        isVisible:                     [false],
      } ) )

      // add pedigreed commodity
      cmdtiesFA.push( this.fb.group( {
        cropCommodityId:                [forageSeedCmdty.cropCommodityId],
        cropCommodityName:              [forageSeedCmdty.commodityName + " Pedigreed"],
        inventoryContractCommodityGuid: [ ],
        inventoryContractGuid:          [ ],
        totalSeededAcres:               [0], 
        totalSpotLossAcres:             [0],
        isPedigreeInd:                  [true],
        totalUnseededAcres:             [0],
        totalUnseededAcresOverride:     [],
        isVisible:                      [false],
      } ) )
    }

    this.inventoryContract.commodities.forEach( cmdt => this.updateCommodity( cmdt ) )

  }


  updateCommodity( cmdty ) {

    const cmdtiesFA: UntypedFormArray = this.viewModel.formGroup.controls.commodities as UntypedFormArray

      cmdtiesFA.controls.forEach ( function(cmdtyFC: UntypedFormGroup){

        if (cmdtyFC.value.cropCommodityId == cmdty.cropCommodityId && cmdtyFC.value.isPedigreeInd == cmdty.isPedigreeInd ) {

          cmdtyFC.controls.inventoryContractCommodityGuid.setValue(cmdty.inventoryContractCommodityGuid)
          cmdtyFC.controls.inventoryContractGuid.setValue(cmdty.inventoryContractGuid)
          cmdtyFC.controls.totalSeededAcres.setValue(cmdty.totalSeededAcres)
          cmdtyFC.controls.totalSpotLossAcres.setValue(cmdty.totalSpotLossAcres) 
          cmdtyFC.controls.isPedigreeInd.setValue(cmdty.isPedigreeInd) 
          cmdtyFC.controls.totalUnseededAcres.setValue(cmdty.totalUnseededAcres)
          cmdtyFC.controls.totalUnseededAcresOverride.setValue(cmdty.totalUnseededAcresOverride)
          cmdtyFC.controls.isVisible.setValue(true)
        }

      })    
  }

  
  // for SEEDED crop report
  geInvSeededTotals() {

    var self = this

    // clear total of totals by commodity
    this.sumInsuredQuantityAcres = 0
    this.sumInsuredSpotLossAcres = 0

    this.sumCommodityTotalInsuredQuantityAcres = 0
    this.sumCommodityTotalSpotLossAcres = 0

    // clear commodities form
    let cmdtiesFA: UntypedFormArray = self.viewModel.formGroup.controls.commodities as UntypedFormArray
    cmdtiesFA.controls.forEach ( function( t: UntypedFormGroup) { 
      t.controls.totalSeededAcres.setValue( 0)
      t.controls.totalSpotLossAcres.setValue( 0)
      t.controls.isVisible.setValue(false)
    })

    // populate the commodity form
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    flds.controls.forEach( function(fld : UntypedFormControl) {

      let pltgs : UntypedFormArray = fld.value.plantings as UntypedFormArray
      pltgs.controls.forEach (function (pltg: UntypedFormGroup){

        let invSeededGrains : UntypedFormArray = pltg.value.inventorySeededGrains as UntypedFormArray
        invSeededGrains.controls.forEach( function (invSeeded: UntypedFormGroup){

          let cropCommodityId = invSeeded.value.cropCommodityId
          let isPedigreeInd = invSeeded.value.isPedigreeInd

          let acres = parseFloat(invSeeded.value.seededAcres)
          if (isNaN(acres)) {
            acres = 0
          }

          if (acres > 0 ) {
            
              cmdtiesFA.controls.forEach ( function( t: UntypedFormGroup) { 

              // ins quality cmdty totals
              if (t.value.cropCommodityId == cropCommodityId && 
                t.value.isPedigreeInd == isPedigreeInd && 
                invSeeded.value.isQuantityInsurableInd) {
                            
                t.controls.totalSeededAcres.setValue( t.value.totalSeededAcres + acres)
                t.controls.isVisible.setValue(true)
                self.sumCommodityTotalInsuredQuantityAcres += acres
              }

              // spot loss cmdty totals
              if (t.value.cropCommodityId == cropCommodityId && 
                t.value.isPedigreeInd == isPedigreeInd && 
                invSeeded.value.isSpotLossInsurableInd) {

                  t.controls.totalSpotLossAcres.setValue( t.value.totalSpotLossAcres + acres)
                  t.controls.isVisible.setValue(true)
                  self.sumCommodityTotalSpotLossAcres += acres
              }
            })

            // calculate sumInsuredQuantityAcres
            if (invSeeded.value.isQuantityInsurableInd) {
              self.sumInsuredQuantityAcres += acres
            }      
            
            if (invSeeded.value.isSpotLossInsurableInd) {
              self.sumInsuredSpotLossAcres += acres
            } 
            
          }
        })
      })
    })
  }


  isSeededCommodityQuantityInsurable(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value;
    let seededCommodityOption = this.getSeededCommodityOptionById(selectedCropCommodityId)

    // Only show Quantity Ins checkbox if commodity is quantity insurable
    if ( seededCommodityOption && seededCommodityOption.isQuantityInsurableInd ) {
      return true
    } else {
      invSeeded.controls['isQuantityInsurableInd'].setValue(false)
      return false
    }

  }


  isSeededCommoditySpotLossInsurable(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value;
    let seededCommodityOption = this.getSeededCommodityOptionById(selectedCropCommodityId)

    // Only show Spot Loss Ins checkbox if commodity is spot loss insurable
    if ( seededCommodityOption && seededCommodityOption.isSpotLossInsurableInd ) {
      return true
    } else {
      invSeeded.controls['isSpotLossInsurableInd'].setValue(false)
      return false
    }

  }


  isChecked(event, fieldIndex, plantingIndex, invSeededIndex) {

    if ( event.checked ){
      // check if there is commodity selected

      const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
      const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
      const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

      if (invSeeded.controls['cropCommodityId'].value ) {
        // do nothing
      } else {
        alert ("Crop must be selected")

        invSeeded.controls['isPedigreeInd'].setValue(false)
        invSeeded.controls['isQuantityInsurableInd'].setValue(false)
        invSeeded.controls['isSpotLossInsurableInd'].setValue(false)
        invSeeded.controls['isReplacedInd'].setValue(false)
      }

    }

    this.geInvSeededTotals()
    this.isMyFormDirty(); // check for changes
  }

  isUnderSeededAcresReadOnly(fieldIndex, plantingIndex, invSeededIndex) {
 
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    let selectedUnderSeededVarietyId = invSeeded.controls['underSeededVrtyCtrl'].value.cropVarietyId

    if (selectedUnderSeededVarietyId && selectedUnderSeededVarietyId > 0) {
      return false
    } else {
      invSeeded.controls['underSeededAcres'].setValue('')
      return true
    }
    
  }

  isAddPlantingVisible(planting, invSeededIndex) {

    // if the planting row is empty do not show Add New Planting button
    if ( isNaN( parseFloat(planting.value.inventorySeededGrains.value[invSeededIndex].seededAcres)) 
        || !planting.value.inventorySeededGrains.value[invSeededIndex].cropCommodityId) {

      return false

    }

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const fld: UntypedFormArray  =  flds.controls.find( f => f.value.fieldId == planting.value.fieldId ) as UntypedFormArray 

    //in order to show Add Planting button, find the max planting number for that field that hasn't been deleted 
    let numPlantings = 0
    let maxNotDeletedPlantingNumber = 0

    for (let i = 0; i < fld.value.plantings.length; i++) {

      for ( let k = 0; k < fld.value.plantings.value[i].inventorySeededGrains.length; k++) {

        if (fld.value.plantings.value[i].inventorySeededGrains.value[k].deletedByUserInd != true ) { 

          maxNotDeletedPlantingNumber = fld.value.plantings.value[i].plantingNumber
          numPlantings ++
          break // so we don't double count the planting numbers 
        }
      }
    }

    // if there are no plantings then don't show the button
    if (numPlantings < 1) {
      return false
    }

    // if there are more than 1 planting then show the button at the last planting
    if (numPlantings > 1) {
      if (maxNotDeletedPlantingNumber == planting.value.plantingNumber ) {
        return true
      }else{
        return false
      }
    }

    // most common case: one planting with acres to be seeded 
    return true;
  }

  onAddPlantingLocal(planting, invSeededIndex) {
    if (this.securityUtilService.canEditInventory() && this.isAddPlantingVisible(planting, invSeededIndex)) {
      this.onAddPlanting(planting)
    }
  }

  onDeletePlanting(planting, invSeededIndex) {
    
    // find the field in the form that contains the planting to be deleted
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const field: UntypedFormArray  =  flds.controls.find( f => f.value.fieldId == planting.value.fieldId ) as UntypedFormArray

    // count the number of plantings for that field that have not been deleted
    let numPlantings = 0       

    for (let i = 0; i < field.value.plantings.length; i++) {

      for ( let k = 0; k < field.value.plantings.value[i].inventorySeededGrains.length; k++) {

        if (field.value.plantings.value[i].inventorySeededGrains.value[k].deletedByUserInd != true ) { 

          numPlantings ++
          break // so we don't double count the planting numbers 
        }
      }
    }

    // now find the planting to be deleted from the form and set its values to null 
    for( var i = 0; i < field.value.plantings.length; i++){

      let fieldPlanting = field.value.plantings.value[i]

      // get the count of undeleted inventory seeded records for that planting
      let numInvSeededRecords = 0
      
      for ( let k = 0; k < fieldPlanting.inventorySeededGrains.length; k++) {

        if (fieldPlanting.inventorySeededGrains.value[k].deletedByUserInd != true) {

          numInvSeededRecords ++ 
        }

      }

      // now do the actual delete
      for ( let k = 0; k < fieldPlanting.inventorySeededGrains.length; k++) {

        if (fieldPlanting.inventorySeededGrains.value[k].inventorySeededGrainGuid == 
          planting.value.inventorySeededGrains.value[invSeededIndex].inventorySeededGrainGuid       
          && fieldPlanting.inventorySeededGrains.value[k].deletedByUserInd != true) {

            // if this is NOT the only inveSeeded record for that planting then delete this inv seeded record only 
            if ( numInvSeededRecords > 1 ) {
              field.value.plantings['controls'][i].controls['inventorySeededGrains'].value.controls[k].controls.deletedByUserInd.setValue(true)
            }

            // un-link any FORAGE plantings
            field.value.plantings['controls'][i].controls.underseededInventorySeededForageGuid.setValue(null)

            let invSeededGrainControls = field.value.plantings['controls'][i].controls['inventorySeededGrains'].value.controls[k].controls

            // if this is the ONLY inveSeeded record for that planting  
            if (numInvSeededRecords == 1) {

              // if there is unseeded data for that planting then warn the user and zero out data for that planting
              if ( (fieldPlanting.acresToBeSeeded && fieldPlanting.acresToBeSeeded > 0) || ( fieldPlanting.cropCommodityId && fieldPlanting.cropCommodityId > 0) ) {

                if ( confirm("There is unseeded data for that planting. The seeded data will be deleted but the unseeded data will remain. Proceed with deletion?") ) {
                  
                  this.resetInvSeededGrainForDelete(invSeededGrainControls)

                }

              } else { // no unseeded data 

                // if this is the last planting then reset values
                if ( numPlantings == 1 ) {

                  this.resetInvSeededGrainForDelete(invSeededGrainControls)

                } else {
                  
                  // delete the whole planting
                  this.resetInvSeededGrainForDelete(invSeededGrainControls)
                  field.value.plantings['controls'][i].controls['inventorySeededGrains'].value.controls[k].controls.deletedByUserInd.setValue(true)
                  field.value.plantings['controls'][i].controls.deletedByUserInd.setValue(true)
                }
              }
            }
        }
      }
    }

    this.geInvSeededTotals()

    this.isMyFormDirty()
  }

  resetInvSeededGrainForDelete(invSeededGrainControls) {

    invSeededGrainControls.cropCommodityId.setValue(CROP_COMMODITY_UNSPECIFIED.ID)
    invSeededGrainControls.cropVarietyCtrl.setValue({      
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
      })
      invSeededGrainControls.commodityTypeCode.setValue(null)
      invSeededGrainControls.commodityTypeDesc.setValue('')

      invSeededGrainControls.isQuantityInsurableInd.setValue(false)
      invSeededGrainControls.isReplacedInd.setValue(false)
      invSeededGrainControls.isPedigreeInd.setValue(false)
      invSeededGrainControls.isSpotLossInsurableInd.setValue(false)

      invSeededGrainControls.seededDate.setValue(null)
      invSeededGrainControls.seededAcres.setValue(null)

      invSeededGrainControls.underSeededVrtyCtrl.setValue({      
        cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
        cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
        varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
        cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
      })
      
      invSeededGrainControls.underSeededAcres.setValue(null)

      // invSeededGrainControls.deletedByUserInd.setValue(true)
  }

  isFullCoverageWarningVisible(fieldIndex, plantingIndex, invSeededIndex):boolean {
    // get the varieity and the commodity type

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    // clear selected commodity type
    
    let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value
    let selectedCropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId
    let selectedCommodityTypeCode = invSeeded.controls['commodityTypeCode'].value

    let selectedSeededDate = invSeeded.controls['seededDate'].value

    if (selectedCropCommodityId && selectedCropVarietyId && selectedCommodityTypeCode && selectedSeededDate) {

      let myTempArray = this.seededVarietyOptions.filter(option => 
        ( option.cropCommodityId == selectedCropCommodityId && option.cropVarietyId == selectedCropVarietyId && option.cropVarietyId  ) )

        if (myTempArray && myTempArray.length > 0) {
          let myCmdtyType = myTempArray[0].cropVarietyCommodityTypes.filter( elem => elem.commodityTypeCode == selectedCommodityTypeCode )  //cropVarietyCommodityTypes

          if (myCmdtyType && myCmdtyType.length > 0 && myCmdtyType[0].fullCoverageDeadlineDate && myCmdtyType[0].finalCoverageDeadlineDate) {
            //get the dates 
            let fullCoverageDeadlineDate = new Date(myCmdtyType[0].fullCoverageDeadlineDate)
            let finalCoverageDeadlineDate = new Date(myCmdtyType[0].finalCoverageDeadlineDate)
            let seededDate = new Date(selectedSeededDate)
    
            if (fullCoverageDeadlineDate < seededDate && seededDate <= finalCoverageDeadlineDate) {
              return true
            }
          }
        }
    }

    return false
  }

  isFinalCoverageWarningVisible(fieldIndex, plantingIndex, invSeededIndex):boolean{
    // get the varieity and the commodity type

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    // clear selected commodity type

    let selectedCropCommodityId = invSeeded.controls['cropCommodityId'].value
    let selectedCropVarietyId = invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId
    let selectedCommodityTypeCode = invSeeded.controls['commodityTypeCode'].value

    let selectedSeededDate = invSeeded.controls['seededDate'].value

    if (selectedCropCommodityId && selectedCropVarietyId && selectedCommodityTypeCode && selectedSeededDate) {

      let myTempArray = this.seededVarietyOptions.filter(option => 
        ( option.cropCommodityId == selectedCropCommodityId && option.cropVarietyId == selectedCropVarietyId && option.cropVarietyId  ) )

        if (myTempArray && myTempArray.length > 0) {
          let myCmdtyType = myTempArray[0].cropVarietyCommodityTypes.filter( elem => elem.commodityTypeCode == selectedCommodityTypeCode )  //cropVarietyCommodityTypes

          if (myCmdtyType && myCmdtyType.length > 0 && myCmdtyType[0].finalCoverageDeadlineDate) {
            //get the dates 
            let finalCoverageDeadlineDate = new Date(myCmdtyType[0].finalCoverageDeadlineDate)
            let seededDate = new Date(selectedSeededDate)
    
            if (seededDate > finalCoverageDeadlineDate) {
              return true
            }
          }
        }
    }
    return false
  }

  roundUpAcres(fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    let acres = invSeeded.controls['seededAcres'].value

    invSeeded.controls['seededAcres'].setValue(roundUpDecimalAcres(acres))

    this.isMyFormDirty()
  }

  roundUpUnderSeededAcres(fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    let acres = invSeeded.controls['underSeededAcres'].value

    invSeeded.controls['underSeededAcres'].setValue(roundUpDecimalAcres(acres))

    this.isMyFormDirty()
  }

  validateVariety(option, value, fieldIndex, plantingIndex, invSeededIndex){

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (!option.isOpen){

      //If user typed the variety and didn't select one from the list
      //Search for a variety in the list with the entered name
      this.searchVariety(value, fieldIndex, plantingIndex, invSeededIndex);

      //If only one is found, select it and set the commodity and commodity type
      if(this.filteredSeededVarietyOptions && this.filteredSeededVarietyOptions.length == 1) {

        invSeeded.controls['cropVarietyCtrl'].setValue({      
          cropCommodityId: this.filteredSeededVarietyOptions[0].cropCommodityId,
          cropVarietyId: this.filteredSeededVarietyOptions[0].cropVarietyId,
          varietyName: this.filteredSeededVarietyOptions[0].varietyName,
          cropVarietyCommodityTypes: this.filteredSeededVarietyOptions[0].cropVarietyCommodityTypes     
        })

        this.setCropCommodity(this.filteredSeededVarietyOptions[0].cropCommodityId, this.filteredSeededVarietyOptions[0].cropVarietyCommodityTypes, fieldIndex, plantingIndex, invSeededIndex) 

      }

      if (!invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId){

        alert("Invalid variety. Please check your spelling or select Unspecified variety")
        // if I try to clear the variety then the menu stays opened
        invSeeded.controls['cropVarietyCtrl'].setValue({      
          cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
          cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
          varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
          cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
        })

      } else {

        // check quantity Insurable checkbox
        let seededCommodityOption = this.getSeededCommodityOptionById(invSeeded.controls['cropCommodityId'].value)
        invSeeded.controls['isQuantityInsurableInd'].setValue(seededCommodityOption && seededCommodityOption.isQuantityInsurableInd)
        invSeeded.controls['isSpotLossInsurableInd'].setValue(false)
      }
    }

    this.geInvSeededTotals()
    this.isMyFormDirty()
  }

  validateUnderSeededVariety(option, fieldIndex, plantingIndex, invSeededIndex){

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (!option.isOpen){

      if (!invSeeded.controls['underSeededVrtyCtrl'].value.cropVarietyId){

        alert("Invalid underseeded variety. Please check your spelling or select Unspecified variety")

        // if I try to clear the variety then the menu stays opened
        invSeeded.controls['underSeededVrtyCtrl'].setValue({      
          cropCommodityId: CROP_COMMODITY_UNSPECIFIED.ID,
          cropVarietyId: CROP_COMMODITY_UNSPECIFIED.ID,
          varietyName: CROP_COMMODITY_UNSPECIFIED.NAME,
          cropVarietyCommodityTypes: <CropVarietyCommodityType>[]      
        })

      }

      this.isMyFormDirty()
    }
  }

  validateSeededDate(event, fieldIndex, plantingIndex, invSeededIndex){
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (event.target.value && new Date(event.target.value).toString() == 'Invalid Date') {
      alert("The Seeding Date is invalid and it will not be saved.")
      invSeeded.controls['seededDate'].nativeElement.style.backgroundColor = 'yellow'
    } else {
      invSeeded.controls['seededDate'].nativeElement.style.backgroundColor = 'transparent'
    }
  }

  onDateChange(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    invSeeded.controls['seededDate'].nativeElement.style.backgroundColor = 'transparent'

    this.isMyFormDirty()
    
  }

  // deferred for later
  // isDeletePlantingVisible(fieldIndex, plantingIndex, invSeededIndex) {
  //   // check if the row is empty. If yes - hide the delete button
  //   const flds: FormArray = this.viewModel.formGroup.controls.fields as FormArray
  //   const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
  //   const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

  //   const field: FormArray  =  flds.controls[fieldIndex]['controls']

  //   // count the number of plantings for that field that have not been deleted
  //   let numPlantings = 0        

  //   for (let i = 0; i < field['plantings'].value.length; i++) {

  //     for ( let k = 0; k < field['plantings'].value.controls[i].value.inventorySeededGrains.length; k++) {

  //       if (field['plantings'].value.controls[i].value.inventorySeededGrains.value[k].deletedByUserInd != true ) { 

  //         numPlantings ++
  //         break // so we don't double count the planting numbers 
  //       }
  //     }
  //   }

  //   let hasUnseededData = false

  //   if (!isNaN(parseFloat(pltg.value.acresToBeSeeded)) || !isNaN(parseFloat(pltg.value.cropCommodityId))) {
  //     hasUnseededData = true
  //   } 

  //   if (isNaN(parseFloat(invSeeded.controls['cropCommodityId'].value))&& isNaN(parseFloat(invSeeded.controls['seededAcres'].value)) &&
  //       isNaN(invSeeded.controls['seededDate'].value) && invSeeded.controls['isQuantityInsurableInd'].value == false &&
  //       invSeeded.controls['isReplacedInd'].value == false && invSeeded.controls['isPedigreeInd'].value == false &&
  //       invSeeded.controls['isSpotLossInsurableInd'].value == false) {

  //       if (hasUnseededData == false && numPlantings > 1)
  //         return true
  //       else
  //         return false
  //   }

  //   return true
  // }

  setTableWrapperStyles() {

    let styles = {
      'width': '1485px' 
    }

    if (this.viewModel.formGroup.controls.showMoreColumns.value) {
      styles = {
        'width': '2298px' 
      }
    }

    return styles;
  }

  setStyles(){

    let styles = {
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
      'width': '1466px'
    }

    if (this.viewModel.formGroup.controls.showMoreColumns.value) {
      styles = {
        'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr', 
        'width': '2278px'
      }
    }

    return styles;
  }

  setPlantingStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 6fr',
      'align-items': 'stretch',
      'width': '871px'
    }

    if (this.viewModel.formGroup.controls.showMoreColumns.value) {
      styles = {
        'display': 'grid',
        'grid-template-columns': '1fr 1fr 6fr',
        'align-items': 'stretch',
        'width': '1683px'
      }
    }

    return styles;
  }

  setSeededGrainStyles(){

    let styles = {
      'display': 'grid',
      'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr',
      'align-items': 'stretch',
      'width': '779px'
    }

    if (this.viewModel.formGroup.controls.showMoreColumns.value) {
      styles = {
        'display': 'grid',
        'grid-template-columns': '1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr',
        'align-items': 'stretch',
        'width': '1547px'
      }
    }

    return styles;
  }


  setTotalsLineStyles(){

    let styles = {
      'width': '930px',
      'justify-content': 'right'
    }

    if (this.viewModel.formGroup.controls.showMoreColumns.value) {
      styles = {
        'width': '1208px'  ,
        'justify-content': 'right'
      }
    }

    return styles;
  }

  shouldHighlightVariety(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (invSeeded.controls['cropCommodityId'].value && 
      !invSeeded.controls['cropVarietyCtrl'].value.cropVarietyId) {
      
        return true
    }

    return false
  }

  shouldHighlightUnderseeded(fieldIndex, plantingIndex, invSeededIndex) {

    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]
    
    if (invSeeded.controls['underSeededVrtyCtrl'].value.cropVarietyId && 
      ! (invSeeded.controls['underSeededAcres'].value > 0 ) ) {
      
        return true
    }

    return false
  }

  onLinkPlantings(field, plantingIndex, invSeededIndex) {

    // before opening the dialog, check if the underseeded data is populated
    // and all changes are saved
    const pltg = field.controls['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]
    
    let cropVarietyId = invSeeded.controls['underSeededVrtyCtrl'].value.cropVarietyId

    if ( !cropVarietyId || 
      !(invSeeded.controls['underSeededAcres'].value > 0 ) ) {
      
        alert("Please enter Underseeded variety and acres and save the changes.")
        return 
    }

    if ( this.hasDataChanged ) {      
        alert("There are unsaved changes on the screen, please save them before proceeding.")
        return 
    }

    // searching for the same fieldId in the original data that came from GET Inventory api call
    const apiField = this.inventoryContract.fields.find(f => f.fieldId == field.value.fieldId)
  
    // open the dialog
    const dataToSend : AddPlantingPopupData = {
      fieldId: field.value.fieldId,
      fieldLabel: field.value.fieldLabel,
      legalLandId: field.value.legalLandId,
      otherLegalDescription: field.value.otherLegalDescription,
      cropYear: this.cropYear,
      policyId: this.policyId,
      underSeededCropVarietyId: cropVarietyId,
      underSeededCropCommodityId: this.underSeededVarietyOptions.find(el => 
                                                el.cropVarietyId == cropVarietyId).cropCommodityId,
      underSeededCropCommodityType: this.underSeededVarietyOptions.find(el => 
                                                el.cropVarietyId == cropVarietyId).cropVarietyCommodityTypes[0].commodityTypeCode,
      underSeededAcres: invSeeded.controls['underSeededAcres'].value,
      grainInventoryFieldGuid: pltg.controls['inventoryFieldGuid'].value,
      linkedPolicies: this.growerContract.linkedPolicies,
      isFieldLinked: this.isLinkedField(field.value.fieldId), 
      isPlantingLinked: this.isLinkedPlanting(field.value.fieldId, invSeeded.controls['inventoryFieldGuid'].value),
      policyNumber: ( apiField && apiField.policies && apiField.policies.length > 0 ) ? apiField.policies[0].policyNumber : "" , 
      growerName:  ( apiField && apiField.policies && apiField.policies.length > 0 ) ? apiField.policies[0].growerName : "" ,
      linkedForageInventoryContractGuid : ( apiField && apiField.policies && apiField.policies.length > 0 ) ? apiField.policies[0].inventoryContractGuid : "" , 
      inventorySeededForageGuid: this.getLinkedInventorySeededForageGuid(field.value.fieldId, invSeeded.controls['inventoryFieldGuid'].value)  // inventorySeededForageGuid for linked plantings 
    }

    const dialogRef = this.dialog.open(LinkPlantingComponent, {
      width: '800px',
      data: dataToSend
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result && result.event == 'LinkPlanting'){

        // reload the screen to get the linked plantings and fields 
        this.store.dispatch(LoadInventoryContract(this.componentId, this.inventoryContract.inventoryContractGuid ))
        this.cdr.detectChanges()

      } else if (result && result.event == 'Cancel'){
        // do nothing
      }
    });
  }

  isWarningVisible(fieldId, inventoryFieldGuid, inventorySeededGrains): boolean {

    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return false
    }

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
      // find the planting 
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )

      if (!inventorySeededGrains.value.underSeededAcres) {
        // don't show any warning until a value is entered for underseeded acres
        return false 
      }

      if (planting && planting.linkedPlanting && inventorySeededGrains.value.underSeededAcres && inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId) {
        
        // if the Underseeded Variety and/or Acres do not match the linked Seeded FORAGE Planting
        if (inventorySeededGrains.value.underSeededAcres != planting.linkedPlanting.acres ||
          inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId != planting.linkedPlanting.cropVarietyId) {

            return true
        }
      }

      // if the Grower has a FORAGE Policy that is not linked to this Planting. 
      // Otherwise, do not show a warning for Underseeding Variety or Acres without a linked Policy.
      if ( planting && !planting.linkedPlanting 
        && inventorySeededGrains && inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId 
          && this.growerContract.linkedPolicies && this.growerContract.linkedPolicies.length > 0) {

            return true
      }
    }
    return false
  }

  getWarningTooltip(fieldId, inventoryFieldGuid, inventorySeededGrains): string {
    
    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return ""
    }

    let tooltip = ""

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
      // find the planting 
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )

      if (planting && planting.linkedPlanting && inventorySeededGrains.value.underSeededAcres && inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId) {

        // if the Underseeded Variety and/or Acres do not match the linked Seeded FORAGE Planting
        if (inventorySeededGrains.value.underSeededAcres != planting.linkedPlanting.acres ||
          inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId != planting.linkedPlanting.cropVarietyId) {

            tooltip = "The Underseeded Variety and/or Acres do not match the linked seeded FORAGE planting. "
        }
      }

      // if the Grower has a FORAGE Policy that is not linked to this Planting. 
      // Otherwise, do not show a warning for Underseeding Variety or Acres without a linked Policy.
      if ( planting && !planting.linkedPlanting 
          && inventorySeededGrains && inventorySeededGrains.value.underSeededVrtyCtrl.cropVarietyId 
          && this.growerContract.linkedPolicies && this.growerContract.linkedPolicies.length > 0) {

            tooltip = tooltip + "The underseeded planting is not linked to the Grower's FORAGE Policy. "
      }

    }
    return tooltip
  }

  isInfoVisible(fieldId, inventorySeededGrains): boolean {

    if (!this.inventoryContract || !this.inventoryContract.fields) {
      return false
    }

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (field) {
     
      if (!inventorySeededGrains.value.underSeededAcres) {
        // don't show any warning until a value is entered for underseeded acres
        return false 
      }
 
      // if the FORAGE Policy has a different Grower than the GRAIN Policy
      if (
        field.policies.length > 0 && field.policies[0].growerNumber !== this.growerContract.growerNumber) {
        return true
      }
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    return false
  }

  getLinkedInventorySeededForageGuid(fieldId, inventoryFieldGuid) : string{

    let field = this.inventoryContract.fields.find(f => f.fieldId == fieldId)

    if (fieldId) {
      // find the planting 
      let planting = field.plantings.find(p => p.inventoryFieldGuid == inventoryFieldGuid )

      if (planting && planting.linkedPlanting) {
        return planting.linkedPlanting.underseededInventorySeededForageGuid
      }
    }

    return null
  }

  onToggleColumns() {

    if ( this.viewModel.formGroup.controls.showMoreColumns.value ) {
      this.viewModel.formGroup.controls.showMoreColumns.setValue(false)
    } else {
      this.viewModel.formGroup.controls.showMoreColumns.setValue(true)
    }
  }
  
  setFormSeededStyles(){
    return {
      'grid-template-columns':  'auto 180px 140px 150px 12px 190px' // one extra button
    }
  }

  isVarietyWarningVisible(fieldIndex, plantingIndex, invSeededIndex) {
    const flds: UntypedFormArray = this.viewModel.formGroup.controls.fields as UntypedFormArray
    const pltg = flds.controls[fieldIndex]['controls']['plantings'].value.controls[plantingIndex]
    const invSeeded = pltg.controls['inventorySeededGrains'].value.controls[invSeededIndex]

    if (invSeeded.controls['commodityTypeCode'].value ==  "Polish Canola" ) {
      
        return true
    }

    return false
  }

  checkForHiddenFieldInTotals() {

    // raises a flag if there is an insured field with acres that is marked as hidden 
    const frmMain = this.viewModel.formGroup as UntypedFormGroup
    const formFields: UntypedFormArray = frmMain.controls.fields as UntypedFormArray

    for (let i = 0; i < formFields.controls.length; i++){
      let frmField = formFields.controls[i] as UntypedFormArray
      	  
      for (let k = 0; k < frmField.value.plantings.controls.length; k++){
        let frmPlanting = frmField.value.plantings.controls[k] as UntypedFormArray
        
        // now check inventory seeded grains 
        for (let n = 0; n < frmPlanting.value.inventorySeededGrains.controls.length; n++) {
                  
          let frmInvSeededGrains = frmPlanting.value.inventorySeededGrains.controls[n] as UntypedFormArray
    
          let seededAcres = !isNaN( parseFloat(frmInvSeededGrains.value.seededAcres)) ?  parseFloat(frmInvSeededGrains.value.seededAcres) : 0

          if ( frmPlanting.value.isHiddenOnPrintoutInd && seededAcres > 0 && (frmInvSeededGrains.value.isQuantityInsurableInd || frmInvSeededGrains.value.isSpotLossInsurableInd) ) {
            
            this.isHiddenFieldInTotals = true
            return
          }
        }
      }
	  }

    this.isHiddenFieldInTotals = false // default
  }

}
