package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PoliciesSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.LandManagementEventTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class YieldMeasUnitConversionListEndpointForageTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(YieldMeasUnitConversionListEndpointForageTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL,
		Scopes.GET_YIELD_MEAS_UNIT_CONVERSIONS,
		Scopes.SAVE_YIELD_MEAS_UNIT_CONVERSIONS,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT,
		Scopes.PRINT_DOP_YIELD_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND
	};
	
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException, ValidationException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();

	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException, ValidationException {
		delete();
	}
	
	private void delete() throws NotFoundDaoException, DaoException, CirrasUnderwritingServiceException, ValidationException{

		deleteInventoryContract(policyNumber1);
		
		service.deleteContractedFieldDetail(topLevelEndpoints, contractedFieldDetailId1.toString());
		
		service.deleteAnnualFieldDetail(topLevelEndpoints, annualFieldDetailId1.toString());

		service.deleteField(topLevelEndpoints, fieldId1.toString());

		service.deleteLegalLandSync(topLevelEndpoints, legalLandId1.toString());
		
		service.deleteGrowerContractYear(topLevelEndpoints, gcyId1.toString());
		service.deletePolicy(topLevelEndpoints, policyId1.toString());

		service.deleteGrower(topLevelEndpoints, growerId1.toString());
		
		deleteYieldMeasUnit(insurancePlanId, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode);
		
		// delete commodity type code
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCodePerennial);
		service.deleteCommodityTypeCode(topLevelEndpoints, commodityTypeCodeAnnual);

		// delete variety
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropVarietyIdPerennial.toString());
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropVarietyIdAnnual.toString());
		
		// delete commodity
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityIdPerennial.toString());
		service.deleteSyncCommodityVariety(topLevelEndpoints, cropCommodityIdAnnual.toString());

	}

	protected void deleteYieldMeasUnit(Integer insurancePlanId, String srcYieldMeasUnitTypeCode, String targetYieldMeasUnitTypeCode) throws CirrasUnderwritingServiceException, ValidationException {
		YieldMeasUnitConversionListRsrc searchResults = service.getYieldMeasUnitConversions(
				topLevelEndpoints,
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
		);
		
		if(searchResults != null && searchResults.getCollection().size() > 0) {
			
			YieldMeasUnitConversion yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityIdPerennial);
			
			if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
				yieldMeasUnitConversion.setDeletedByUserInd(true);

				searchResults = service.saveYieldMeasUnitConversions(
						searchResults, 
						insurancePlanId.toString(), 
						srcYieldMeasUnitTypeCode, 
						targetYieldMeasUnitTypeCode
					);

				//Make sure both versions are deleted
				yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityIdPerennial);
				if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
					yieldMeasUnitConversion.setDeletedByUserInd(true);

					service.saveYieldMeasUnitConversions(
							searchResults, 
							insurancePlanId.toString(), 
							srcYieldMeasUnitTypeCode, 
							targetYieldMeasUnitTypeCode
						);
				}
			}
			
			yieldMeasUnitConversion = getYieldMeasUnitConversion(searchResults, cropCommodityIdAnnual);
			if(yieldMeasUnitConversion != null && yieldMeasUnitConversion.getYieldMeasUnitConversionGuid() != null) {
				yieldMeasUnitConversion.setDeletedByUserInd(true);

				searchResults = service.saveYieldMeasUnitConversions(
						searchResults, 
						insurancePlanId.toString(), 
						srcYieldMeasUnitTypeCode, 
						targetYieldMeasUnitTypeCode
					);
			}
		}
	}
	
	public void deleteInventoryContract(String policyNumber)
			throws CirrasUnderwritingServiceException {
		
		UwContractListRsrc searchResults = getUwContracts();
		
		if ( searchResults.getCollection() != null && searchResults.getCollection().size() == 1 ) {
			UwContractRsrc referrer = searchResults.getCollection().get(0);
			
			if ( referrer.getDeclaredYieldContractGuid() != null ) {
				DopYieldContractRsrc dyc = service.getDopYieldContract(referrer);
				if ( dyc != null ) {
					service.deleteDopYieldContract(dyc);
				}
			}			
			
			if ( referrer.getInventoryContractGuid() != null ) { 
				InventoryContractRsrc invContract = service.getInventoryContract(referrer);
				service.deleteInventoryContract(invContract);
			}
		}
	}

	private Integer insurancePlanId = 5;
	
	private String commodityNamePerennial = "Perennial Cmdty";
	private Integer cropCommodityIdPerennial = 88558888;
	private String commodityTypeCodePerennial = "Perennial Type";
	private Integer cropVarietyIdPerennial = 99911999;

	private String commodityNameAnnual = "Annual Cmdty";
	private Integer cropCommodityIdAnnual = 88559999;
	private String commodityTypeCodeAnnual = "Annual Type";
	private Integer cropVarietyIdAnnual = 99922999;

	private String srcYieldMeasUnitTypeCode ="TON";
	private String targetYieldMeasUnitTypeCode ="LB";
	
	String annual = InventoryServiceEnums.PlantDurationType.ANNUAL.toString();
	String perennial = InventoryServiceEnums.PlantDurationType.PERENNIAL.toString();

	private Integer growerId1 = 90000011;
	private Integer policyId1 = 90000012;
	private Integer gcyId1 = 90000013;
	private Integer contractId1 = 90000014;
	private String policyNumber1 = "998865-21";
	private String contractNumber1 = "998865";
	private Integer cropYear1 = 2014;

	private Integer legalLandId1 = 90000015;
	private Integer fieldId1 = 90000016;
	private Integer annualFieldDetailId1 = 90000017;
	private Integer contractedFieldDetailId1 = 90000018;
	
	private double weightInLbs = 200000.0;

	private double commodityPerennialConversionFactor1 = 2000.0;
	private double commodityPerennialConversionFactor2 = 1000.0;
	private double commodityAnnualConversionFactor1 = 1500.0;
	private double commodityAnnualConversionFactor2 = 500.0;
	
	private double quantityHarvestedTonsPerennial1 = 2352.9412;
	private double quantityHarvestedTonsPerennial2 = 4705.8824;
	private double quantityHarvestedTonsAnnual1 = 2666.6667;
	private double quantityHarvestedTonsAnnual2 = 8000.0;

	private Double fieldAcres = 100.0;

	double delta = 0.0002; //Allow rounding differences

	private void checkConversions(
			double conversionFactorPerennial, 
			double conversionFactorAnnual,
			double quantityHarvestedTonsPerennial,
			double quantityHarvestedTonsAnnual) throws CirrasUnderwritingServiceException {
		//fetch dop data and compare calculations
		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		DopYieldContractRsrc dopYieldContractRsrc = service.getDopYieldContract(referrer);

		AnnualFieldRsrc field1 = getField(fieldId1, dopYieldContractRsrc.getFields());

		List<DopYieldFieldForage> dopFields = field1.getDopYieldFieldForageList();

		//Default Weight -----------------------------------
		//Perennial
		DopYieldFieldForage dyff = getDopYieldFieldForage(commodityTypeCodePerennial, dopFields);
		DopYieldFieldForageCut cut = dyff.getDopYieldFieldForageCuts().get(0);
		Double expectedWeightDefaultUnit = cut.getWeight() / conversionFactorPerennial;
		expectedWeightDefaultUnit = BigDecimal.valueOf(expectedWeightDefaultUnit)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
		Assert.assertEquals("WeightDefaultUnit Perennial", expectedWeightDefaultUnit, cut.getWeightDefaultUnit());
		
		//Annual
		dyff = getDopYieldFieldForage(commodityTypeCodeAnnual, dopFields);
		cut = dyff.getDopYieldFieldForageCuts().get(0);
		expectedWeightDefaultUnit = cut.getWeight() / conversionFactorAnnual;
		expectedWeightDefaultUnit = BigDecimal.valueOf(expectedWeightDefaultUnit)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
		Assert.assertEquals("WeightDefaultUnit Annual", expectedWeightDefaultUnit, cut.getWeightDefaultUnit());

		checkContractCommodityForage(quantityHarvestedTonsPerennial, quantityHarvestedTonsAnnual, dopYieldContractRsrc);
		checkRollupForage(quantityHarvestedTonsPerennial, quantityHarvestedTonsAnnual, dopYieldContractRsrc);

	}

	private void checkContractCommodityForage(double quantityHarvestedTonsPerennial, double quantityHarvestedTonsAnnual,
			DopYieldContractRsrc dopYieldContractRsrc) {
		//Quantity Harvested ------------------------------
		//Perennial
		DopYieldContractCommodityForage dycc = getDopYieldContractCommodityForage(commodityTypeCodePerennial, dopYieldContractRsrc.getDopYieldContractCommodityForageList());
		Double actualQuantityHarvestedTons = dycc.getQuantityHarvestedTons();
		if(dycc.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(dycc.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
			Assert.assertEquals("QuantityHarvestedTons Perennial CC", quantityHarvestedTonsPerennial, actualQuantityHarvestedTons.doubleValue(), delta);
			
			double yieldPerAcre = dycc.getQuantityHarvestedTons() / fieldAcres;
			Assert.assertEquals("YieldPerAcre Perennial CC", yieldPerAcre, dycc.getYieldPerAcre().doubleValue(), delta);
			
		}
		
		//Annual
		dycc = getDopYieldContractCommodityForage(commodityTypeCodeAnnual, dopYieldContractRsrc.getDopYieldContractCommodityForageList());
		actualQuantityHarvestedTons = dycc.getQuantityHarvestedTons();
		if(dycc.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(dycc.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
			Assert.assertEquals("QuantityHarvestedTons Annual CC", quantityHarvestedTonsAnnual, actualQuantityHarvestedTons.doubleValue(), delta);

			double yieldPerAcre = dycc.getQuantityHarvestedTons() / fieldAcres;
			Assert.assertEquals("YieldPerAcre Annual CC", yieldPerAcre, dycc.getYieldPerAcre().doubleValue(), delta);
		
		}
	}
	
	private void checkRollupForage(double quantityHarvestedTonsPerennial, double quantityHarvestedTonsAnnual,
			DopYieldContractRsrc dopYieldContractRsrc) {
		//Quantity Harvested ------------------------------
		//Perennial
		DopYieldFieldRollupForage dyrf = getDopYieldFieldRollupForage(commodityTypeCodePerennial, dopYieldContractRsrc.getDopYieldFieldRollupForageList());
		Double actualQuantityHarvestedTons = dyrf.getQuantityHarvestedTons();
		if(dyrf.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(dyrf.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
			Assert.assertEquals("QuantityHarvestedTons Perennial Rollup", quantityHarvestedTonsPerennial, actualQuantityHarvestedTons.doubleValue(), delta);
			
			double yieldPerAcre = dyrf.getQuantityHarvestedTons() / fieldAcres;
			Assert.assertEquals("YieldPerAcre Perennial Rollup", yieldPerAcre, dyrf.getYieldPerAcre().doubleValue(), delta);
			
		}
		
		//Annual
		dyrf = getDopYieldFieldRollupForage(commodityTypeCodeAnnual, dopYieldContractRsrc.getDopYieldFieldRollupForageList());
		actualQuantityHarvestedTons = dyrf.getQuantityHarvestedTons();
		if(dyrf.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(dyrf.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
			Assert.assertEquals("QuantityHarvestedTons Annual Rollup", quantityHarvestedTonsAnnual, actualQuantityHarvestedTons.doubleValue(), delta);

			double yieldPerAcre = dyrf.getQuantityHarvestedTons() / fieldAcres;
			Assert.assertEquals("YieldPerAcre Annual Rollup", yieldPerAcre, dyrf.getYieldPerAcre().doubleValue(), delta);
		
		}
	}
	
	private DopYieldFieldRollupForage getDopYieldFieldRollupForage(String commodityTypeCode, List<DopYieldFieldRollupForage> dyccfList) {
		
		List<DopYieldFieldRollupForage> dyrfs = dyccfList.stream().filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, dyrfs.size());
		
		return dyrfs.get(0);
	}

	@Test
	public void testRecalculationForage() throws CirrasUnderwritingServiceException, Oauth2ClientException, ValidationException, NotFoundDaoException, DaoException {
		//Test insert/update and calculate yield field rollup
		logger.debug("<testRecalculationForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//Create commodity
		createCommodity(cropCommodityIdPerennial, commodityNamePerennial, insurancePlanId, perennial, srcYieldMeasUnitTypeCode);
		createCommodity(cropCommodityIdAnnual, commodityNameAnnual, insurancePlanId, annual, srcYieldMeasUnitTypeCode);
		
		//Create Variety
		createVariety(cropVarietyIdPerennial, cropCommodityIdPerennial);
		createVariety(cropVarietyIdAnnual, cropCommodityIdAnnual);
		
		//Create CommodityType
		createCommodityTypeCode(commodityTypeCodePerennial, cropCommodityIdPerennial);
		createCommodityTypeCode(commodityTypeCodeAnnual, cropCommodityIdAnnual);
		
		//Add new conversion factors for barley and canola conversion factors
		YieldMeasUnitConversionListRsrc list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());

		YieldMeasUnitConversion yieldMeasUnitConversionPerennial = getYieldMeasUnitConversion(list, cropCommodityIdPerennial);
		Assert.assertNotNull(yieldMeasUnitConversionPerennial);
		
		//Add unit conversion
		yieldMeasUnitConversionPerennial = setYieldMeasUnitConversion(yieldMeasUnitConversionPerennial, 1, commodityPerennialConversionFactor1, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, 2000, 9999);

		YieldMeasUnitConversion yieldMeasUnitConversionAnnual = getYieldMeasUnitConversion(list, cropCommodityIdAnnual);
		Assert.assertNotNull(yieldMeasUnitConversionAnnual);
		
		//Add unit conversion
		yieldMeasUnitConversionAnnual = setYieldMeasUnitConversion(yieldMeasUnitConversionAnnual, 1, commodityAnnualConversionFactor1, srcYieldMeasUnitTypeCode, targetYieldMeasUnitTypeCode, 2000, 9999);
		
		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		
		createGrower();
		createPolicy(insurancePlanId);
		createGrowerContractYear(insurancePlanId);

		createLegalLand();
		createField();
		createAnnualFieldDetail();
		createContractedFieldDetail();
		
		createForageInventoryContract();
		
		createDopData();
		
		//Check conversions
		checkConversions(commodityPerennialConversionFactor1, commodityAnnualConversionFactor1, quantityHarvestedTonsPerennial1, quantityHarvestedTonsAnnual1);
		
		//Update conversion factors for commodity 1 and 2
		list = service.getYieldMeasUnitConversions(
				topLevelEndpoints, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
				);
				
		Assert.assertNotNull(list);
		Assert.assertNotNull(list.getCollection());

		yieldMeasUnitConversionPerennial = getYieldMeasUnitConversion(list, cropCommodityIdPerennial);
		Assert.assertNotNull(yieldMeasUnitConversionPerennial);
		yieldMeasUnitConversionPerennial.setConversionFactor(commodityPerennialConversionFactor2);

		yieldMeasUnitConversionAnnual = getYieldMeasUnitConversion(list, cropCommodityIdAnnual);
		Assert.assertNotNull(yieldMeasUnitConversionAnnual);
		yieldMeasUnitConversionAnnual.setConversionFactor(commodityAnnualConversionFactor2);
		
		//Save and Fetch
		list = service.saveYieldMeasUnitConversions(
				list, 
				insurancePlanId.toString(), 
				srcYieldMeasUnitTypeCode, 
				targetYieldMeasUnitTypeCode
			);
		 
		Assert.assertNotNull(list);
		
		//fetch dop data and compare calculations
		checkConversions(commodityPerennialConversionFactor2, commodityAnnualConversionFactor2, quantityHarvestedTonsPerennial2, quantityHarvestedTonsAnnual2);

		delete();
		
		logger.debug(">testRecalculationForage");

	}
	
	private void createContractCommodities(DopYieldContractRsrc dopContract) {
		
		DopYieldContractCommodityForage dyccPerennial = getDopYieldContractCommodityForage(commodityTypeCodePerennial, dopContract.getDopYieldContractCommodityForageList());
		dyccPerennial.setTotalBalesLoads(100);
		dyccPerennial.setWeight(weightInLbs);
		dyccPerennial.setWeightDefaultUnit((double)100);
		dyccPerennial.setMoisturePercent((double)80);
		
		DopYieldContractCommodityForage dyccAnnual = getDopYieldContractCommodityForage(commodityTypeCodeAnnual, dopContract.getDopYieldContractCommodityForageList());
		dyccAnnual.setTotalBalesLoads(100);
		dyccAnnual.setWeight(weightInLbs);
		dyccAnnual.setWeightDefaultUnit((double)100);
		dyccAnnual.setMoisturePercent((double)80);

	}
	
	private void createCuts(DopYieldContractRsrc dopContract) {
		
		//Field 1 *********************
		AnnualFieldRsrc field1 = getField(fieldId1, dopContract.getFields());

		List<DopYieldFieldForage> dopFields = field1.getDopYieldFieldForageList();

		//Perennial
		DopYieldFieldForage dyff = getDopYieldFieldForage(commodityTypeCodePerennial, dopFields);
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, weightInLbs, (double)80, false)); //100 ton
		dyff.setDopYieldFieldForageCuts(cuts);
		
		//Annual
		dyff = getDopYieldFieldForage(commodityTypeCodeAnnual, dopFields);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(dyff.getInventoryFieldGuid(), 1, 100, weightInLbs, (double)80, false)); //100 ton
		dyff.setDopYieldFieldForageCuts(cuts);

	}

	private DopYieldFieldForage getDopYieldFieldForage(String commodityTypeCode, List<DopYieldFieldForage> dyffList) {
		
		List<DopYieldFieldForage> dyffs = dyffList.stream().filter(x -> x.getCommodityTypeCode().equalsIgnoreCase(commodityTypeCode)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, dyffs.size());
		
		return dyffs.get(0);
	}
	
	private DopYieldFieldForageCut createDopFieldForageCut(
			String inventoryFieldGuid,
			Integer cutNumber,
			Integer totalBalesLoads, 
			Double weight,
			Double moisturePercent, 
			Boolean deletedByUserInd 
		) {
		
		DopYieldFieldForageCut model = new DopYieldFieldForageCut();

		model.setInventoryFieldGuid(inventoryFieldGuid);
		model.setCutNumber(cutNumber);
		model.setTotalBalesLoads(totalBalesLoads);
		model.setWeight(weight);
		model.setWeightDefaultUnit(null);
		model.setMoisturePercent(moisturePercent);
		model.setDeletedByUserInd(deletedByUserInd);

		return model;
	}

	protected void createDopData() throws CirrasUnderwritingServiceException, ValidationException {

		// Dop Date
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2022, Calendar.JANUARY, 15);
		Date dopDate = cal.getTime();

		//Get uw contract
		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				1, 20);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);

		//Rollover DOP ********* 
		Assert.assertNull(referrer.getDeclaredYieldContractGuid());
		DopYieldContractRsrc newDyc = service.rolloverDopYieldContract(referrer);
		
		Assert.assertNotNull(newDyc);
		Assert.assertNull(newDyc.getDeclaredYieldContractGuid());
		Assert.assertEquals(contractId1, newDyc.getContractId());
		Assert.assertEquals(cropYear1, newDyc.getCropYear());
		
		//Field 1 checks
		AnnualFieldRsrc fetchedField1 = getField(fieldId1, newDyc.getFields());
		Assert.assertEquals(2, fetchedField1.getDopYieldFieldForageList().size());

		//Commodity totals list
		Assert.assertNotNull(newDyc.getDopYieldContractCommodityForageList());
		Assert.assertEquals(2, newDyc.getDopYieldContractCommodityForageList().size());

		newDyc.setDeclarationOfProductionDate(dopDate);
		newDyc.setDefaultYieldMeasUnitTypeCode("TON");
		newDyc.setEnteredYieldMeasUnitTypeCode("LB");
		newDyc.setGrainFromOtherSourceInd(false);
		newDyc.setBalerWagonInfo("Test Baler");
		newDyc.setTotalLivestock(100);
		newDyc.setInsurancePlanId(insurancePlanId);		
		
		//Add forage dop cuts
		createCuts(newDyc);
		
		//Add Contract commodities
		createContractCommodities(newDyc);
		
		//CREATE DOP *********************************************************************************************
		service.createDopYieldContract(topLevelEndpoints, newDyc);

	}

	protected UwContractListRsrc getUwContracts() throws CirrasUnderwritingServiceException {
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber1,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);
		
		Assert.assertNotNull(searchResults);

		return searchResults;
	}
	
	private void createGrower() throws ValidationException, CirrasUnderwritingServiceException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		//Date createTransactionDate = addSeconds(transactionDate, -1);

		GrowerRsrc resource = new GrowerRsrc();
		
		resource.setGrowerId(growerId1);
		resource.setGrowerNumber(999888);
		resource.setGrowerName("grower test name");
		resource.setGrowerAddressLine1("address line 1");
		resource.setGrowerAddressLine2("address line 2");
		resource.setGrowerPostalCode("V8P 4N8");
		resource.setGrowerCity("Victoria");
		resource.setCityId(1);
		resource.setGrowerProvince("BC");
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerCreated);

		service.synchronizeGrower(resource);
		
	}
	
	private void createPolicy(Integer insurancePlanId) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();

		PolicyRsrc resource = new PolicyRsrc();
		
		resource.setPolicyId(policyId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setPolicyStatusCode("ACTIVE");
		resource.setOfficeId(1);
		resource.setPolicyNumber(policyNumber1);
		resource.setContractNumber(contractNumber1);
		resource.setContractId(contractId1);
		resource.setCropYear(cropYear1);
		
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.PolicyCreated);

		service.synchronizePolicy(resource);
	}
	
	private void createGrowerContractYear(Integer insurancePlanId) throws ValidationException, CirrasUnderwritingServiceException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		GrowerContractYearSyncRsrc resource = new GrowerContractYearSyncRsrc();
		
		resource.setGrowerContractYearId(gcyId1);
		resource.setContractId(contractId1);
		resource.setGrowerId(growerId1);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setCropYear(cropYear1);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.GrowerContractYearCreated);

		service.synchronizeGrowerContractYear(resource);
		
	}

	private void createLegalLand() throws CirrasUnderwritingServiceException, ValidationException {

		//CREATE LegalLand
		LegalLandRsrc resource = new LegalLandRsrc();
		
		resource.setLegalLandId(legalLandId1);
		resource.setPrimaryPropertyIdentifier("GF0099999");
		resource.setPrimaryReferenceTypeCode("OTHER");
		resource.setLegalDescription(null);
		resource.setLegalShortDescription(null);
		resource.setOtherDescription("TEST LEGAL LOC 123");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.LegalLandCreated);
		
		service.synchronizeLegalLand(resource);

	}
	
	private void createField() throws CirrasUnderwritingServiceException, ValidationException {

		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(fieldId1);
		resource.setFieldLabel("Field Label");
		resource.setActiveFromCropYear(2011);
		resource.setActiveToCropYear(2022);
		resource.setTransactionType(LandManagementEventTypes.FieldCreated);
		
		service.synchronizeField(resource);
	}
	
	private void createAnnualFieldDetail() throws CirrasUnderwritingServiceException, ValidationException {

		AnnualFieldDetailRsrc resource = new AnnualFieldDetailRsrc();
		
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setLegalLandId(legalLandId1);
		resource.setFieldId(fieldId1);
		resource.setCropYear(cropYear1);
		resource.setTransactionType(LandManagementEventTypes.AnnualFieldDetailCreated);
		
		service.synchronizeAnnualFieldDetail(resource);
	}

	private void createContractedFieldDetail() throws CirrasUnderwritingServiceException, ValidationException {

		ContractedFieldDetailRsrc resource = new ContractedFieldDetailRsrc();
		
		resource.setContractedFieldDetailId(contractedFieldDetailId1);
		resource.setAnnualFieldDetailId(annualFieldDetailId1);
		resource.setGrowerContractYearId(gcyId1);
		resource.setDisplayOrder(1);
		resource.setTransactionType(LandManagementEventTypes.ContractedFieldDetailCreated);
	
		service.synchronizeContractedFieldDetail(resource);

	}
	
	private void createForageInventoryContract() throws ValidationException, CirrasUnderwritingServiceException {

		UwContractListRsrc searchResults = getUwContracts();
		
		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNull(referrer.getInventoryContractGuid());

		InventoryContractRsrc invContract = service.rolloverInventoryContract(referrer);

		Assert.assertNotNull(invContract);
		Assert.assertNotNull(invContract.getFields());
		Assert.assertEquals(1, invContract.getFields().size());
		
		//Field 1 ****		
		AnnualFieldRsrc field1 = getField(fieldId1, invContract.getFields());

		// Remove default planting.
		field1.getPlantings().remove(0);
		
		// Planting 1, Perennial (i.e. Alfalfa)
		InventoryField planting = createPlanting(field1, 1, cropYear1, false);
		createInventorySeededForage(planting, cropCommodityIdPerennial, cropVarietyIdPerennial, commodityTypeCodePerennial, true, fieldAcres );

		// Planting 2, Annual (silage Corn)
		planting = createPlanting(field1, 2, cropYear1, false);
		createInventorySeededForage(planting, cropCommodityIdAnnual, cropVarietyIdAnnual, commodityTypeCodeAnnual, true, fieldAcres);

		InventoryContractRsrc fetchedInvContract = service.createInventoryContract(topLevelEndpoints, invContract);

		Assert.assertNotNull(fetchedInvContract);
		Assert.assertNotNull(fetchedInvContract.getFields());
		Assert.assertEquals(1, fetchedInvContract.getFields().size());
		
		AnnualFieldRsrc fetchedField1 = getField(fieldId1, fetchedInvContract.getFields());
		
		Assert.assertNotNull(fetchedField1.getPlantings());
		Assert.assertEquals(2, fetchedField1.getPlantings().size());

	}	

	private InventoryField createPlanting(AnnualFieldRsrc field, Integer plantingNumber, Integer cropYear, Boolean isHiddenOnPrintoutInd) {
		InventoryField planting = new InventoryField();

		planting.setCropYear(cropYear);
		planting.setFieldId(field.getFieldId());
		planting.setInsurancePlanId(5);
		planting.setInventoryFieldGuid(null);
		planting.setLastYearCropCommodityId(null);
		planting.setLastYearCropCommodityName(null);
		planting.setLastYearCropVarietyId(null);
		planting.setLastYearCropVarietyName(null);
		planting.setIsHiddenOnPrintoutInd(isHiddenOnPrintoutInd);
		planting.setPlantingNumber(plantingNumber);
		planting.setUnderseededAcres(null);
		planting.setUnderseededCropVarietyId(null);
		planting.setUnderseededCropVarietyName(null);
		
		field.getPlantings().add(planting);

		return planting;
	}

	private InventorySeededForage createInventorySeededForage(
			InventoryField planting, 
            Integer cropCommodityId,
            Integer cropVarietyId,
            String commodityTypeCode,
			Boolean isQuantityInsurableInd,
			Double fieldAcres) {
		
		InventorySeededForage isf = new InventorySeededForage();


		isf.setCommodityTypeCode(commodityTypeCode);
		isf.setCropCommodityId(cropCommodityId);
		isf.setCropVarietyId(cropVarietyId);
		isf.setCropVarietyName(null);
		isf.setFieldAcres(fieldAcres);
		isf.setInventoryFieldGuid(planting.getInventoryFieldGuid());
		isf.setIsAwpEligibleInd(false);
		isf.setIsIrrigatedInd(false);
		isf.setIsQuantityInsurableInd(isQuantityInsurableInd);
		isf.setPlantInsurabilityTypeCode(null);
		isf.setSeedingYear(planting.getCropYear() - 3);
		
		planting.getInventorySeededForages().add(isf);

		return isf;
	}	
	
	private AnnualFieldRsrc getField(Integer fieldId, List<AnnualFieldRsrc> contractFields) {
		
		List<AnnualFieldRsrc> fields = contractFields.stream().filter(x -> x.getFieldId().equals(fieldId)) 
				.collect(Collectors.toList());
		
		Assert.assertEquals(1, fields.size());
		
		return fields.get(0);
	}
	
	private YieldMeasUnitConversion setYieldMeasUnitConversion(
			YieldMeasUnitConversion model,
			Integer versionNumber, 
			Double conversionFactor,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			Integer effectiveCropYear,
			Integer expiryCropYear) throws DaoException {
		
		model.setSrcYieldMeasUnitTypeCode(srcYieldMeasUnitTypeCode);
		model.setTargetYieldMeasUnitTypeCode(targetYieldMeasUnitTypeCode);
		model.setVersionNumber(versionNumber);
		model.setConversionFactor(conversionFactor);
		model.setEffectiveCropYear(effectiveCropYear);
		model.setExpiryCropYear(expiryCropYear);

		return model;
	}
	
	private YieldMeasUnitConversion getYieldMeasUnitConversion(YieldMeasUnitConversionListRsrc results, Integer cropCommodityId) {
		
		if(results != null && results.getCollection().size() > 0) {
			List<YieldMeasUnitConversion> resources = results.getCollection().stream()
					.filter(x -> x.getCropCommodityId().equals(cropCommodityId))
					.collect(Collectors.toList());
			
			if(resources != null && resources.size() > 0) {
				return resources.get(0);
			}
		}
		
		return null;
	}	
	
	private DopYieldContractCommodityForage getDopYieldContractCommodityForage(String commodityTypeCode, List<DopYieldContractCommodityForage> dyccList) {
		
		DopYieldContractCommodityForage dycc = null;
		
		List<DopYieldContractCommodityForage> dyccFiltered = dyccList.stream()
				.filter(x -> x.getCommodityTypeCode().equals(commodityTypeCode))
				.collect(Collectors.toList());
		
		if (dyccFiltered != null) {
			dycc = dyccFiltered.get(0);
		}
		return dycc;
	}	
	
	private void createCommodity(Integer cropCommodityId, String commodityName, Integer insurancePlanId, 
			String plantDurationTypeCode, String yieldMeasUnitTypeCode)
			throws CirrasUnderwritingServiceException, ValidationException {

		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		//CREATE PARENT COMMODITY
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropCommodityId);
		resource.setCropName(commodityName);
		resource.setInsurancePlanId(insurancePlanId);
		resource.setShortLabel("SHRT");
		resource.setPlantDurationTypeCode(plantDurationTypeCode);
		resource.setIsInventoryCrop(true);
		resource.setIsYieldCrop(true);
		resource.setIsUnderwritingCrop(true);
		resource.setIsProductInsurableInd(true);
		resource.setIsCropInsuranceEligibleInd(true);
		resource.setIsPlantInsuranceEligibleInd(true);
		resource.setIsOtherInsuranceEligibleInd(true);
		resource.setYieldMeasUnitTypeCode(yieldMeasUnitTypeCode);
		resource.setYieldDecimalPrecision(2);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);
	}
	

	private void createCommodityTypeCode(String commodityTypeCode, Integer cropCommodityId)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
				
		SyncCommodityTypeCodeRsrc resource = new SyncCommodityTypeCodeRsrc();

		resource.setCommodityTypeCode(commodityTypeCode);
		resource.setCropCommodityId(cropCommodityId);
		resource.setDescription("Test Code");
		resource.setIsActive(true);
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityTypeCodeCreated);

		service.synchronizeCommodityTypeCode(resource);

	}

	private void createVariety(Integer cropVarietyId, Integer cropCommodityId)
			throws CirrasUnderwritingServiceException, ValidationException {
		//Date and Time without millisecond
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); //Set milliseconds to 0 becauce they are not set in the database
		Date transactionDate = cal.getTime();
		
		SyncCommodityVarietyRsrc resource = new SyncCommodityVarietyRsrc();

		resource.setCropId(cropVarietyId);
		resource.setParentCropId(cropCommodityId);
		resource.setCropName("TEST VARIETY");
		resource.setDataSyncTransDate(transactionDate);
		resource.setTransactionType(PoliciesSyncEventTypes.CommodityVarietyCreated);

		service.synchronizeCommodityVariety(resource);

	}	
}
