package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class DopYieldServiceTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldServiceTest.class);

	private Integer insurancePlanIdGrain = 4;
	private Integer insurancePlanIdForage = 5;
	private Integer cropYear = 2023;
	private String defaultMeasurementUnitCodeGrain = "TONNE";
	private String defaultMeasurementUnitCodeForage = "TON";
	private Integer cropCommodityId = 16;
	private double conversionFactor = 45.93; //BUSHEL to TONNE: Make sure this is correct
	private double assertDelta = 0;


	@Test
	public void convertEstimatedYieldTest() throws Exception {
		logger.debug("<convertEstimatedYieldTest");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasDopYieldService dopService = (CirrasDopYieldService)webApplicationContext.getBean("cirrasDopYieldService");
		
		//Create dop Yield Contract
		DopYieldContractRsrc dopYieldContract = createYieldContract(insurancePlanIdGrain, defaultMeasurementUnitCodeGrain);
		dopYieldContract.setEnteredYieldMeasUnitTypeCode("BUSHEL");

		
		String targetUnit = "TONNE";
		double valueToConvert = (double)10;

		//Convert BUSHEL to TONNE
		double convertedValue = dopService.convertEstimatedYieldTest(dopYieldContract, targetUnit, cropCommodityId, valueToConvert);
		
		double expectedValue = valueToConvert * conversionFactor;
		Assert.assertEquals("BUSHEL to TONNE", expectedValue, convertedValue, assertDelta);
		
		
		//Convert TONNE to BUSHEL
		dopYieldContract.setEnteredYieldMeasUnitTypeCode("TONNE");
		targetUnit = "BUSHEL";
		valueToConvert = (double)100;
		convertedValue = dopService.convertEstimatedYieldTest(dopYieldContract, targetUnit, cropCommodityId, valueToConvert);
		
		expectedValue = valueToConvert / conversionFactor;
		Assert.assertEquals("BUSHEL to TONNE", expectedValue, convertedValue, assertDelta);

		//Entered Unit = Target Unit = No conversion
		dopYieldContract.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		targetUnit = "BUSHEL";
		convertedValue = dopService.convertEstimatedYieldTest(dopYieldContract, targetUnit, cropCommodityId, valueToConvert);
		Assert.assertEquals("Entered Unit = Target Unit", valueToConvert, convertedValue, assertDelta);

		//Invalid units
		//Expect an error
		try {
			dopYieldContract.setEnteredYieldMeasUnitTypeCode("XY");
			targetUnit = "BUSHEL";
			convertedValue = dopService.convertEstimatedYieldTest(dopYieldContract, targetUnit, cropCommodityId, valueToConvert);
			Assert.fail("Conversion should have thrown an error XY to BUSHEL");
		} catch (ServiceException e) {
			//Expected
		}

		//Invalid units (entered unit = default units)
		//Expect an error
		try {
			dopYieldContract.setEnteredYieldMeasUnitTypeCode(defaultMeasurementUnitCodeGrain);
			targetUnit = "XY";
			convertedValue = dopService.convertEstimatedYieldTest(dopYieldContract, targetUnit, cropCommodityId, valueToConvert);
			Assert.fail("Conversion should have thrown an error default units to XY");
		} catch (ServiceException e) {
			//Expected
		}
	
		logger.debug(">convertEstimatedYieldTest");
	}

	private static final int barleyId = 16;
	private static final int oatId = 24;
	private static final int wheatId = 26; 
	private double barleyConversionFactor = 45.93;
	private double oatConversionFactor = 64.482;
	
	@Test
	public void testCalculateYieldRollup() throws Exception {
		logger.debug("<testCalculateYieldRollup");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasDopYieldService dopService = (CirrasDopYieldService)webApplicationContext.getBean("cirrasDopYieldService");


		//Create dop Yield Contract
		DopYieldContractRsrc dopYieldContract = createYieldContract(insurancePlanIdGrain, defaultMeasurementUnitCodeGrain);
		
		List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();
		
		double barleyPedigreedTotalYield = 0;
		double barleyNonPedigreedTotalYield = 0;
		double oatTotalYield = 0;
		
		//FIELD A
		AnnualFieldRsrc fieldA = new AnnualFieldRsrc();
		List<DopYieldFieldGrain> dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopFieldGrain(barleyId, true, (double)100, (double)50));
		dopFields.add(createDopFieldGrain(oatId, false, (double)200, (double)50));
		fieldA.setDopYieldFieldGrainList(dopFields);
		barleyPedigreedTotalYield = 100 * 50;
		oatTotalYield = 200 * 50;

		//FIELD B
		AnnualFieldRsrc fieldB = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopFieldGrain(barleyId, true, -1, 0)); //null yield
		dopFields.add(createDopFieldGrain(oatId, false, (double)300, (double)75));
		fieldB.setDopYieldFieldGrainList(dopFields);
		oatTotalYield = (300 * 75) + oatTotalYield;
		
		//FIELD C
		AnnualFieldRsrc fieldC = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopFieldGrain(barleyId, true, (double)200, (double)100));
		dopFields.add(createDopFieldGrain(barleyId, false, (double)250, (double)50));
		dopFields.add(createDopFieldGrain(wheatId, false, -1, 0)); //null yield
		fieldC.setDopYieldFieldGrainList(dopFields);
		barleyPedigreedTotalYield = (200 * 100) + barleyPedigreedTotalYield;
		barleyNonPedigreedTotalYield = 250 * 50;
		
		double barleyPedigreedTotalAcres = 150;
		double barleyNonPedigreedTotalAcres = 50;
		double oatTotalAcres = 125;
		
		fields.add(fieldA);
		fields.add(fieldB);
		fields.add(fieldC);
		
		dopYieldContract.setFields(fields);

		//Convert BUSHEL to TONNE
		dopYieldContract.setEnteredYieldMeasUnitTypeCode("BUSHEL");
		DopYieldContract<? extends AnnualField> convertedDopYieldContract = dopService.calculateYieldRollupTest(dopYieldContract);
		
		double estimatedYieldPerAcre;
		double convertedValue;

		Assert.assertEquals(3, convertedDopYieldContract.getDopYieldFieldRollupList().size());

		for(DopYieldFieldRollup rollup : convertedDopYieldContract.getDopYieldFieldRollupList()) {
			switch (rollup.getCropCommodityId()) {
			case barleyId:
				if(rollup.getIsPedigreeInd()) {
					estimatedYieldPerAcre = barleyPedigreedTotalYield / barleyPedigreedTotalAcres;
					convertedValue = estimatedYieldPerAcre / barleyConversionFactor;
					Assert.assertEquals("Barley Pedigreed Bushel to Tonne - Tonne", convertedValue, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
					Assert.assertEquals("Barley Pedigreed Bushel to Tonne - Bushel", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				} else {
					estimatedYieldPerAcre = barleyNonPedigreedTotalYield / barleyNonPedigreedTotalAcres;
					convertedValue = estimatedYieldPerAcre / barleyConversionFactor;
					Assert.assertEquals("Barley Bushel to Tonne - Tonne", convertedValue, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
					Assert.assertEquals("Barley Bushel to Tonne - Bushel", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				}
				break;
			case oatId:
				estimatedYieldPerAcre = oatTotalYield / oatTotalAcres;
				convertedValue = estimatedYieldPerAcre / oatConversionFactor;
				Assert.assertEquals("Oat Bushel to Tonne - Tonne", convertedValue, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
				Assert.assertEquals("Oat Bushel to Tonne - Bushel", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				break;
			default:
				Assert.fail("Unexpected commodity: " + rollup.getCropCommodityId().toString());
				break;
			}
		}

		//Convert TONNE to BUSHEL
		dopYieldContract.setEnteredYieldMeasUnitTypeCode("TONNE");
		convertedDopYieldContract = dopService.calculateYieldRollupTest(dopYieldContract);
		
		Assert.assertEquals(3, convertedDopYieldContract.getDopYieldFieldRollupList().size());
		
		for(DopYieldFieldRollup rollup : convertedDopYieldContract.getDopYieldFieldRollupList()) {
			switch (rollup.getCropCommodityId()) {
			case barleyId:
				if(rollup.getIsPedigreeInd()) {
					estimatedYieldPerAcre = barleyPedigreedTotalYield / barleyPedigreedTotalAcres;
					convertedValue = estimatedYieldPerAcre * barleyConversionFactor;
					Assert.assertEquals("Barley Pedigreed Tonne to Bushel - Tonne", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
					Assert.assertEquals("Barley Pedigreed Tonne to Bushel - Bushel", convertedValue, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				} else {
					estimatedYieldPerAcre = barleyNonPedigreedTotalYield / barleyNonPedigreedTotalAcres;
					convertedValue = estimatedYieldPerAcre * barleyConversionFactor;
					Assert.assertEquals("Barley Tonne to Bushel - Tonne", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
					Assert.assertEquals("Barley Tonne to Bushel - Bushel", convertedValue, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				}
				break;
			case oatId:
				estimatedYieldPerAcre = oatTotalYield / oatTotalAcres;
				convertedValue = estimatedYieldPerAcre * oatConversionFactor;
				Assert.assertEquals("Oat Tonne to Bushel - Tonne", estimatedYieldPerAcre, rollup.getEstimatedYieldPerAcreTonnes(), assertDelta);
				Assert.assertEquals("Oat Tonne to Bushel - Bushel", convertedValue, rollup.getEstimatedYieldPerAcreBushels(), assertDelta);
				break;
			default:
				Assert.fail("Unexpected commodity: " + rollup.getCropCommodityId().toString());
				break;
			}
		}

		logger.debug(">testCalculateYieldRollup");
	}	
	
	private static final String commodityTypeSilageCorn = "Silage Corn";
	private static final String commodityTypeAlfalfa = "Alfalfa";
	private static final String commodityTypeGrass = "Grass"; 
	private static final String commodityTypeSpringAnnual = "Spring Annual";
	private static final String annual = InventoryServiceEnums.PlantDurationType.ANNUAL.toString();
	private static final String perennial = InventoryServiceEnums.PlantDurationType.PERENNIAL.toString();
	
	@Test
	public void testCalculateYieldContractCommodityForage() throws Exception {
		logger.debug("<testCalculateYieldContractCommodityForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasDopYieldService dopService = (CirrasDopYieldService)webApplicationContext.getBean("cirrasDopYieldService");


		//Create dop Yield Contract
		DopYieldContractRsrc dopYieldContract = createYieldContract(insurancePlanIdForage, defaultMeasurementUnitCodeForage);
		
		List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

		List<DopYieldFieldForage> dopFields = new ArrayList<DopYieldFieldForage>();
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		
		//FIELD A
		AnnualFieldRsrc fieldA = new AnnualFieldRsrc();
		
		//Silage Corn QTY
		DopYieldFieldForage dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		cuts.add(createDopFieldForageCut(120, (double)80, (double)80, (double)85, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		//Alfalfa NOT QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, false, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		cuts.add(createDopFieldForageCut(115, (double)75, (double)75, (double)78, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		fieldA.setDopYieldFieldForageList(dopFields);

		//FIELD B
		AnnualFieldRsrc fieldB = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldForage>();
		
		//Silage Corn QTY
		dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(200, (double)200, (double)200, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(110, (double)80, (double)80, (double)85, false));
		cuts.add(createDopFieldForageCut(130, (double)90, (double)90, (double)80, false));
		cuts.add(createDopFieldForageCut(150, (double)100, (double)100, (double)75, true)); //deleted by user
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		fieldB.setDopYieldFieldForageList(dopFields);

		
		//FIELD C
		AnnualFieldRsrc fieldC = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldForage>();
		
		//Silage Corn QTY => no value in cuts (only one cut)
		dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(null, null, null, null, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa NULL VALUES
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)0, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(null, null, null, null, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Grass QTY
		dyff = createDopFieldForage(commodityTypeGrass, true, (double)100, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(200, (double)100, (double)100, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		
		fieldC.setDopYieldFieldForageList(dopFields);
		
		
		DopYieldContractCommodityForage dyccfSilageCorn = createDopYieldContractCommodityForage(
				commodityTypeSilageCorn, 	// commodityTypeCode
				(double)300, 				// totalFieldAcres
				(double)200, 				// harvestedAcres
				null, 						// harvestedAcresOverride
				null,						// quantityHarvestedTons
				null, 						// quantityHarvestedTonsOverride
				null);		 				// yieldPerAcre
		
		Double quantityHarvestedSilageCorn = (double)11440;
		Double YieldPerAcreSilageCorn = (double)57.2;
		
		DopYieldContractCommodityForage dyccfAlfalfa = createDopYieldContractCommodityForage(
				commodityTypeAlfalfa, 
				(double)400, 				// totalFieldAcres
				(double)400, 				// harvestedAcres
				null, 						// harvestedAcresOverride
				null,						// quantityHarvestedTons
				null, 						// quantityHarvestedTonsOverride
				null); 						// yieldPerAcre

		Double quantityHarvestedAlfalfa = (double)11244.1176;
		Double YieldPerAcreAlfalfa = (double)28.1103;

		DopYieldContractCommodityForage dyccfGrass = createDopYieldContractCommodityForage(
				commodityTypeGrass, 
				(double)100, 				// totalFieldAcres
				(double)100, 				// harvestedAcres
				(double)80, 				// harvestedAcresOverride
				null,						// quantityHarvestedTons
				(double)100000, 			// quantityHarvestedTonsOverride
				null);		 				// yieldPerAcre

		Double quantityHarvestedGrass = (double)4705.8824;
		Double YieldPerAcreGrass = (double)1250;

		//Commodity type that doesn't exist anymore
		DopYieldContractCommodityForage dyccfSpringAnnual = createDopYieldContractCommodityForage(
				commodityTypeSpringAnnual, 
				(double)400, 				// totalFieldAcres
				(double)400, 				// harvestedAcres
				null, 						// harvestedAcresOverride
				(double)1500, 				// quantityHarvestedTons
				null, 						// quantityHarvestedTonsOverride
				(double)145); 				// yieldPerAcre
		
		fields.add(fieldA);
		fields.add(fieldB);
		fields.add(fieldC);
		
		dopYieldContract.setFields(fields);
		
		List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();
		
		//Add commodity totals for grass and silage corn and one that is not in the fields anymore
		dopYieldContractCommodityForageList.add(dyccfSilageCorn);
		dopYieldContractCommodityForageList.add(dyccfGrass);
		dopYieldContractCommodityForageList.add(dyccfSpringAnnual);
		
		dopYieldContract.setDopYieldContractCommodityForageList(dopYieldContractCommodityForageList);

		DopYieldContract<? extends AnnualField> convertedDopYieldContract = dopService.calculateYieldContractCommodityForageTest(dopYieldContract);
		
		Assert.assertNotNull(convertedDopYieldContract);
		
		Assert.assertNotNull(convertedDopYieldContract.getDopYieldContractCommodityForageList());
		Assert.assertEquals(3, convertedDopYieldContract.getDopYieldContractCommodityForageList().size());

		//Set expected calculated values
		dyccfSilageCorn.setQuantityHarvestedTons(quantityHarvestedSilageCorn);
		dyccfSilageCorn.setYieldPerAcre(YieldPerAcreSilageCorn);
		dyccfAlfalfa.setQuantityHarvestedTons(quantityHarvestedAlfalfa);
		dyccfAlfalfa.setYieldPerAcre(YieldPerAcreAlfalfa);
		dyccfGrass.setQuantityHarvestedTons(quantityHarvestedGrass);
		dyccfGrass.setYieldPerAcre(YieldPerAcreGrass);
		
		for(DopYieldContractCommodityForage dyccf : convertedDopYieldContract.getDopYieldContractCommodityForageList()) {
			
			switch (dyccf.getCommodityTypeCode()) {
			case commodityTypeSilageCorn:
				checkDopYieldContractCommodityForage(dyccfSilageCorn, dyccf);
				break;
			case commodityTypeAlfalfa:
				checkDopYieldContractCommodityForage(dyccfAlfalfa, dyccf);
				break;
			case commodityTypeGrass:
				checkDopYieldContractCommodityForage(dyccfGrass, dyccf);
				break;
			default:
				Assert.fail("Unexpected commodity type: " + dyccf.getCommodityTypeCode());
				break;
			}
		}

		logger.debug(">testCalculateYieldContractCommodityForage");
	}
	
	
	@Test
	public void testCalculateYieldRollupForage() throws Exception {
		logger.debug("<testCalculateYieldRollupForage");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasDopYieldService dopService = (CirrasDopYieldService)webApplicationContext.getBean("cirrasDopYieldService");


		//Create dop Yield Contract
		DopYieldContractRsrc dopYieldContract = createYieldContract(insurancePlanIdForage, defaultMeasurementUnitCodeForage);
		
		List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

		List<DopYieldFieldForage> dopFields = new ArrayList<DopYieldFieldForage>();
		List<DopYieldFieldForageCut> cuts = new ArrayList<DopYieldFieldForageCut>();
		
		//FIELD A
		AnnualFieldRsrc fieldA = new AnnualFieldRsrc();
		
		//Silage Corn QTY
		DopYieldFieldForage dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		cuts.add(createDopFieldForageCut(120, (double)80, (double)80, (double)85, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		//Alfalfa NOT QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, false, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		cuts.add(createDopFieldForageCut(115, (double)75, (double)75, (double)78, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		fieldA.setDopYieldFieldForageList(dopFields);

		//FIELD B
		AnnualFieldRsrc fieldB = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldForage>();
		
		//Silage Corn QTY
		dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(200, (double)200, (double)200, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(110, (double)80, (double)80, (double)85, false));
		cuts.add(createDopFieldForageCut(130, (double)90, (double)90, (double)80, false));
		cuts.add(createDopFieldForageCut(150, (double)100, (double)100, (double)75, true)); //deleted by user
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		fieldB.setDopYieldFieldForageList(dopFields);

		
		//FIELD C
		AnnualFieldRsrc fieldC = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldForage>();
		
		//Silage Corn QTY => no value in cuts (only one cut)
		dyff = createDopFieldForage(commodityTypeSilageCorn, true, (double)100, annual);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(null, null, null, null, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa QTY
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)200, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(100, (double)100, (double)100, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Alfalfa NULL VALUES
		dyff = createDopFieldForage(commodityTypeAlfalfa, true, (double)0, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(null, null, null, null, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);
		
		//Grass QTY
		dyff = createDopFieldForage(commodityTypeGrass, true, (double)100, perennial);
		cuts = new ArrayList<DopYieldFieldForageCut>();
		cuts.add(createDopFieldForageCut(200, (double)100, (double)100, (double)80, false));
		dyff.setDopYieldFieldForageCuts(cuts);
		dopFields.add(dyff);

		
		fieldC.setDopYieldFieldForageList(dopFields);
		
		
		DopYieldRollupForage dyrfSilageCorn = createDopYieldRollupForage(
				commodityTypeSilageCorn, 	// commodityTypeCode
				(double)300, 				// totalFieldAcres
				(double)200, 				// harvestedAcres
				null, 						// totalBales
				null,						// quantityHarvestedTons
				null);		 				// yieldPerAcre
		
		Double quantityHarvestedSilageCorn = (double)11440;
		Double YieldPerAcreSilageCorn = (double)57.2;
		Integer totalBalesSilageCorn = 420;
		
		DopYieldRollupForage dyrfAlfalfa = createDopYieldRollupForage(
				commodityTypeAlfalfa, 
				(double)400, 				// totalFieldAcres
				(double)400, 				// harvestedAcres
				null, 						// totalBales
				null,						// quantityHarvestedTons
				null); 						// yieldPerAcre

		Double quantityHarvestedAlfalfa = (double)11244.1176;
		Double YieldPerAcreAlfalfa = (double)28.1103;
		Integer totalBalesAlfalfa = 555;

		DopYieldRollupForage dyrfGrass = createDopYieldRollupForage(
				commodityTypeGrass, 
				(double)100, 				// totalFieldAcres
				(double)100, 				// harvestedAcres
				80, 						// totalBales
				null,						// quantityHarvestedTons
				null);		 				// yieldPerAcre

		Double quantityHarvestedGrass = (double)4705.8824;
		Double YieldPerAcreGrass = (double)47.0588;
		Integer totalBalesGrass = 200;

		//Commodity type that doesn't exist anymore
		DopYieldRollupForage dyrfSpringAnnual = createDopYieldRollupForage(
				commodityTypeSpringAnnual, 
				(double)400, 				// totalFieldAcres
				(double)400, 				// harvestedAcres
				null, 						// totalBales
				(double)1500, 				// quantityHarvestedTons
				(double)145); 				// yieldPerAcre
		
		fields.add(fieldA);
		fields.add(fieldB);
		fields.add(fieldC);
		
		dopYieldContract.setFields(fields);
		
		List<DopYieldRollupForage> dopYieldRollupForageList = new ArrayList<DopYieldRollupForage>();
		
		//Add commodity totals for grass and silage corn and one that is not in the fields anymore
		dopYieldRollupForageList.add(dyrfSilageCorn);
		dopYieldRollupForageList.add(dyrfGrass);
		dopYieldRollupForageList.add(dyrfSpringAnnual);
		
		dopYieldContract.setDopYieldRollupForageList(dopYieldRollupForageList);

		DopYieldContract<? extends AnnualField> convertedDopYieldContract = dopService.calculateYieldRollupForageTest(dopYieldContract);
		
		Assert.assertNotNull(convertedDopYieldContract);
		
		Assert.assertNotNull(convertedDopYieldContract.getDopYieldRollupForageList());
		Assert.assertEquals(3, convertedDopYieldContract.getDopYieldRollupForageList().size());

		//Set expected calculated values
		dyrfSilageCorn.setQuantityHarvestedTons(quantityHarvestedSilageCorn);
		dyrfSilageCorn.setYieldPerAcre(YieldPerAcreSilageCorn);
		dyrfSilageCorn.setTotalBalesLoads(totalBalesSilageCorn);
		dyrfAlfalfa.setQuantityHarvestedTons(quantityHarvestedAlfalfa);
		dyrfAlfalfa.setYieldPerAcre(YieldPerAcreAlfalfa);
		dyrfAlfalfa.setTotalBalesLoads(totalBalesAlfalfa);
		dyrfGrass.setQuantityHarvestedTons(quantityHarvestedGrass);
		dyrfGrass.setYieldPerAcre(YieldPerAcreGrass);
		dyrfGrass.setTotalBalesLoads(totalBalesGrass);
		
		for(DopYieldRollupForage dyrf : convertedDopYieldContract.getDopYieldRollupForageList()) {
			
			switch (dyrf.getCommodityTypeCode()) {
			case commodityTypeSilageCorn:
				checkDopYieldRollupForage(dyrfSilageCorn, dyrf);
				break;
			case commodityTypeAlfalfa:
				checkDopYieldRollupForage(dyrfAlfalfa, dyrf);
				break;
			case commodityTypeGrass:
				checkDopYieldRollupForage(dyrfGrass, dyrf);
				break;
			default:
				Assert.fail("Unexpected commodity type: " + dyrf.getCommodityTypeCode());
				break;
			}
		}

		logger.debug(">testCalculateYieldRollupForage");
	}
	
	
	private void checkDopYieldContractCommodityForage(DopYieldContractCommodityForage expected, DopYieldContractCommodityForage actual) {
		
		Double actualQuantityHarvestedTons = actual.getQuantityHarvestedTons();
		if(actual.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(actual.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Double actualYieldPerAcre = actual.getYieldPerAcre();
		if(actual.getQuantityHarvestedTons() != null) {
			actualYieldPerAcre = BigDecimal.valueOf(actual.getYieldPerAcre())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", expected.getHarvestedAcres(), actual.getHarvestedAcres());
		Assert.assertEquals("HarvestedAcresOverride", expected.getHarvestedAcresOverride(), actual.getHarvestedAcresOverride());
		Assert.assertEquals("QuantityHarvestedTons", expected.getQuantityHarvestedTons(), actualQuantityHarvestedTons);
		Assert.assertEquals("QuantityHarvestedTonsOverride", expected.getQuantityHarvestedTonsOverride(), actual.getQuantityHarvestedTonsOverride());
		Assert.assertEquals("YieldPerAcre", expected.getYieldPerAcre(), actualYieldPerAcre);

	}	
	
	private void checkDopYieldRollupForage(DopYieldRollupForage expected, DopYieldRollupForage actual) {
		
		Double actualQuantityHarvestedTons = actual.getQuantityHarvestedTons();
		if(actual.getQuantityHarvestedTons() != null) {
			actualQuantityHarvestedTons = BigDecimal.valueOf(actual.getQuantityHarvestedTons())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Double actualYieldPerAcre = actual.getYieldPerAcre();
		if(actual.getQuantityHarvestedTons() != null) {
			actualYieldPerAcre = BigDecimal.valueOf(actual.getYieldPerAcre())
	                .setScale(4, BigDecimal.ROUND_HALF_UP)
	                .doubleValue();
		}
		
		Assert.assertEquals("CommodityTypeCode", expected.getCommodityTypeCode(), actual.getCommodityTypeCode());
		Assert.assertEquals("TotalFieldAcres", expected.getTotalFieldAcres(), actual.getTotalFieldAcres());
		Assert.assertEquals("HarvestedAcres", expected.getHarvestedAcres(), actual.getHarvestedAcres());
		Assert.assertEquals("TotalBalesLoads", expected.getTotalBalesLoads(), actual.getTotalBalesLoads());
		Assert.assertEquals("QuantityHarvestedTons", expected.getQuantityHarvestedTons(), actualQuantityHarvestedTons);
		Assert.assertEquals("YieldPerAcre", expected.getYieldPerAcre(), actualYieldPerAcre);

	}

	private DopYieldContractRsrc createYieldContract(Integer insurancePlanId, String defaultMeasurementUnitCode) {
		DopYieldContractRsrc dopYieldContract = new DopYieldContractRsrc();
		dopYieldContract.setInsurancePlanId(insurancePlanId);
		dopYieldContract.setCropYear(cropYear);
		dopYieldContract.setDefaultYieldMeasUnitTypeCode(defaultMeasurementUnitCode);
		return dopYieldContract;
	}

	private DopYieldFieldGrain createDopFieldGrain(
			Integer cropCommodityId,
			Boolean isPedigreeInd,
			double estimatedYieldPerAcre,
			double seededAcres
		) {
	
	DopYieldFieldGrain dopField = new DopYieldFieldGrain();
	dopField.setCropCommodityId(cropCommodityId);
	dopField.setIsPedigreeInd(isPedigreeInd);
	dopField.setSeededAcres(seededAcres);
	if(estimatedYieldPerAcre < 0) {
		dopField.setEstimatedYieldPerAcre(null);
	} else {
		dopField.setEstimatedYieldPerAcre(estimatedYieldPerAcre);
	}

	return dopField;
}

	private DopYieldFieldForage createDopFieldForage(
			String commodityTypeCode, 
			Boolean isQuantityInsurableInd, 
			Double fieldAcres, 
			String plantDurationTypeCode
		) {
	
		DopYieldFieldForage model = new DopYieldFieldForage();

		model.setCommodityTypeCode(commodityTypeCode);
		model.setIsQuantityInsurableInd(isQuantityInsurableInd);
		model.setFieldAcres(fieldAcres);
		model.setPlantDurationTypeCode(plantDurationTypeCode);

		return model;
	}
	
	private DopYieldFieldForageCut createDopFieldForageCut(
			Integer totalBalesLoads, 
			Double weight, 
			Double weightDefaultUnit, 
			Double moisturePercent, 
			Boolean deletedByUserInd
		) {
		
		DopYieldFieldForageCut model = new DopYieldFieldForageCut();

		model.setTotalBalesLoads(totalBalesLoads);
		model.setWeight(weight);
		model.setWeightDefaultUnit(weightDefaultUnit);
		model.setMoisturePercent(moisturePercent);
		model.setDeletedByUserInd(deletedByUserInd);

		return model;
	}
	
	private DopYieldContractCommodityForage createDopYieldContractCommodityForage(
			String commodityTypeCode, 
			Double totalFieldAcres, 
			Double harvestedAcres, 
			Double harvestedAcresOverride, 
			Double quantityHarvestedTons, 
			Double quantityHarvestedTonsOverride, 
			Double yieldPerAcre
			) {
		DopYieldContractCommodityForage model = new DopYieldContractCommodityForage();
		model.setCommodityTypeCode(commodityTypeCode);
		model.setTotalFieldAcres(totalFieldAcres);
		model.setHarvestedAcres(harvestedAcres);
		model.setHarvestedAcresOverride(harvestedAcresOverride);
		model.setQuantityHarvestedTons(quantityHarvestedTons);
		model.setQuantityHarvestedTonsOverride(quantityHarvestedTonsOverride);
		model.setYieldPerAcre(yieldPerAcre);
		
		return model;
	}
	
	private DopYieldRollupForage createDopYieldRollupForage(
			String commodityTypeCode, 
			Double totalFieldAcres, 
			Double harvestedAcres, 
			Integer totalBales, 
			Double quantityHarvestedTons, 
			Double yieldPerAcre
			) {
		DopYieldRollupForage model = new DopYieldRollupForage();
		model.setCommodityTypeCode(commodityTypeCode);
		model.setTotalFieldAcres(totalFieldAcres);
		model.setHarvestedAcres(harvestedAcres);
		model.setTotalBalesLoads(totalBales);
		model.setQuantityHarvestedTons(quantityHarvestedTons);
		model.setYieldPerAcre(yieldPerAcre);
		
		return model;
	}

}
