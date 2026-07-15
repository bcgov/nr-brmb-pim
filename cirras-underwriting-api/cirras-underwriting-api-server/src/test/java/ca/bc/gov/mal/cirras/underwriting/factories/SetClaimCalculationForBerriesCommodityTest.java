package ca.bc.gov.mal.cirras.underwriting.factories;

import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ClaimCalculationBerriesSyncDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;

public class SetClaimCalculationForBerriesCommodityTest {

	private static final Logger logger = LoggerFactory.getLogger(SetClaimCalculationForBerriesCommodityTest.class);

	private DopYieldContractRsrcFactory service;
    private DopYieldContractCommodityBerries model;
	
    @Before
    public void setUp() {
        service = new DopYieldContractRsrcFactory();
        model = new DopYieldContractCommodityBerries();
        model.setCropCommodityId(101); // Target commodity ID for the tests
    }
    
    @Test
    public void testSetClaimCalculation_EmptyList_ResetsToNull() {
        // Arrange
        // Set pre-existing values to verify they get reset to null
        model.setTotalYieldForCalculation(100.0);
        model.setCalculationStatusCode("DRAFT");
        List<ClaimCalculationBerriesSyncDto> emptyList = Collections.emptyList();

        // Act
        service.setClaimCalculationForBerriesCommodity(model, emptyList);

        // Assert
        Assert.assertNull("Yield should be reset to null when list is empty", model.getTotalYieldForCalculation());
        Assert.assertNull("Status code should be reset to null when list is empty", model.getCalculationStatusCode());
    }

    @Test
    public void testSetClaimCalculation_NoMatchingCommodityId_ResetsToNull() {
        // Arrange
        model.setTotalYieldForCalculation(100.0);
        model.setCalculationStatusCode("DRAFT");

        // List has items, but none with cropCommodityId = 101
        ClaimCalculationBerriesSyncDto nonMatchingDto = new ClaimCalculationBerriesSyncDto();
        nonMatchingDto.setCropCommodityId(999); // Different ID
        nonMatchingDto.setCalculationVersion(1);
        nonMatchingDto.setTotalYieldForCalculation(140.0);
        nonMatchingDto.setCalculationStatusCode("APPROVED");

        List<ClaimCalculationBerriesSyncDto> dtoList = List.of(nonMatchingDto);

        // Act
        service.setClaimCalculationForBerriesCommodity(model, dtoList);

        // Assert
        Assert.assertNull("Yield should be reset to null when no matches exist", model.getTotalYieldForCalculation());
        Assert.assertNull("Status should be reset to null when no matches exist", model.getCalculationStatusCode());
    }

    @Test
    public void testSetClaimCalculation_OneMatch_MapsValuesCorrectly() {
        // Matching object
        ClaimCalculationBerriesSyncDto matchingDto = new ClaimCalculationBerriesSyncDto();
        matchingDto.setCropCommodityId(101); // Matches model
        matchingDto.setCalculationVersion(1);
        matchingDto.setTotalYieldForCalculation(100.0);
        matchingDto.setCalculationStatusCode("APPROVED");
        
        //Non matching object
        ClaimCalculationBerriesSyncDto nonMatchingDto = new ClaimCalculationBerriesSyncDto();
        nonMatchingDto.setCropCommodityId(999); // Different ID
        nonMatchingDto.setCalculationVersion(2);
        nonMatchingDto.setTotalYieldForCalculation(140.0);
        nonMatchingDto.setCalculationStatusCode("DRAFT");

        List<ClaimCalculationBerriesSyncDto> dtoList = List.of(matchingDto, nonMatchingDto);

        // Act
        service.setClaimCalculationForBerriesCommodity(model, dtoList);

        // Assert
        Assert.assertEquals(matchingDto.getTotalYieldForCalculation(), model.getTotalYieldForCalculation(), 0.005);
        Assert.assertEquals(matchingDto.getCalculationStatusCode(), model.getCalculationStatusCode());
    }

    @Test
    public void testSetClaimCalculation_MultipleMatches_SelectsHighestVersion() {

        // V1: Lower version
        ClaimCalculationBerriesSyncDto v1Dto = new ClaimCalculationBerriesSyncDto();
        v1Dto.setCropCommodityId(101);
        v1Dto.setCalculationVersion(1);
        v1Dto.setTotalYieldForCalculation(100.0);
        v1Dto.setCalculationStatusCode("ARCHIVED");

        // V3: Highest version (Target)
        ClaimCalculationBerriesSyncDto v3Dto = new ClaimCalculationBerriesSyncDto();
        v3Dto.setCropCommodityId(101);
        v3Dto.setCalculationVersion(3); // Highest
        v3Dto.setTotalYieldForCalculation(300.0);
        v3Dto.setCalculationStatusCode("DRAFT");

        // V2: Middle version
        ClaimCalculationBerriesSyncDto v2Dto = new ClaimCalculationBerriesSyncDto();
        v2Dto.setCropCommodityId(101);
        v2Dto.setCalculationVersion(2);
        v2Dto.setTotalYieldForCalculation(200.0);
        v2Dto.setCalculationStatusCode("ARCHIVED");

        // Unrelated commodity to ensure it doesn't interfere
        ClaimCalculationBerriesSyncDto unrelatedDto = new ClaimCalculationBerriesSyncDto();
        unrelatedDto.setCropCommodityId(999);
        unrelatedDto.setCalculationVersion(5); // Higher version, but wrong ID

        // Add them out of order to ensure stream max logic is properly challenged
        List<ClaimCalculationBerriesSyncDto> dtoList = List.of(v1Dto, unrelatedDto, v3Dto, v2Dto);

        // Act
        service.setClaimCalculationForBerriesCommodity(model, dtoList);

        // Assert
        Assert.assertEquals("Should map yield from the highest calculation version (v3)", 
                v3Dto.getTotalYieldForCalculation(), model.getTotalYieldForCalculation());
        Assert.assertEquals("Should map status from the highest calculation version (v3)", 
        		v3Dto.getCalculationStatusCode(), model.getCalculationStatusCode());
    }    
}