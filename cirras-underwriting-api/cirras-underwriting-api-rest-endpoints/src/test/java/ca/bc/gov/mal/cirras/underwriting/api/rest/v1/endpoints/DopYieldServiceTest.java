package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

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
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;

public class DopYieldServiceTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(DopYieldServiceTest.class);

	private Integer insurancePlanId = 4;
	private Integer cropYear = 2023;
	private String defaultMeasurementUnitCode = "TONNE";
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
		DopYieldContractRsrc dopYieldContract = createYieldContract();
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
			dopYieldContract.setEnteredYieldMeasUnitTypeCode(defaultMeasurementUnitCode);
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
		DopYieldContractRsrc dopYieldContract = createYieldContract();
		
		List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();
		
		double barleyPedigreedTotalYield = 0;
		double barleyNonPedigreedTotalYield = 0;
		double oatTotalYield = 0;
		
		//FIELD A
		AnnualFieldRsrc fieldA = new AnnualFieldRsrc();
		List<DopYieldFieldGrain> dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopField(barleyId, true, (double)100, (double)50));
		dopFields.add(createDopField(oatId, false, (double)200, (double)50));
		fieldA.setDopYieldFieldGrainList(dopFields);
		barleyPedigreedTotalYield = 100 * 50;
		oatTotalYield = 200 * 50;

		//FIELD B
		AnnualFieldRsrc fieldB = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopField(barleyId, true, -1, 0)); //null yield
		dopFields.add(createDopField(oatId, false, (double)300, (double)75));
		fieldB.setDopYieldFieldGrainList(dopFields);
		oatTotalYield = (300 * 75) + oatTotalYield;
		
		//FIELD C
		AnnualFieldRsrc fieldC = new AnnualFieldRsrc();
		dopFields = new ArrayList<DopYieldFieldGrain>();
		dopFields.add(createDopField(barleyId, true, (double)200, (double)100));
		dopFields.add(createDopField(barleyId, false, (double)250, (double)50));
		dopFields.add(createDopField(wheatId, false, -1, 0)); //null yield
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

	private DopYieldContractRsrc createYieldContract() {
		DopYieldContractRsrc dopYieldContract = new DopYieldContractRsrc();
		dopYieldContract.setInsurancePlanId(insurancePlanId);
		dopYieldContract.setCropYear(cropYear);
		dopYieldContract.setDefaultYieldMeasUnitTypeCode(defaultMeasurementUnitCode);
		return dopYieldContract;
	}

	private DopYieldFieldGrain createDopField(
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

}
