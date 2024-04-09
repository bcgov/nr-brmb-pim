package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CommodityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CommodityTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.ContractedFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CropVarietyInsurabilityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.FieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.GradeModifierRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.GradeModifierTypeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.AnnualFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.AnnualFieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CirrasDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LandDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LegalLandFieldXrefRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LegalLandRiskAreaXrefRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LegalLandRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.RiskAreaRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.SeedingDeadlineRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.UnderwritingYearRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.UwContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.YieldMeasUnitConversionRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.YieldMeasUnitTypeCodeRsrcFactory;

@Configuration
public class ResourceFactorySpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ResourceFactorySpringConfig.class);
	
	public ResourceFactorySpringConfig() {
		logger.info("<ResourceFactorySpringConfig");
		
		logger.info(">ResourceFactorySpringConfig");
	}

	@Bean
	public UwContractRsrcFactory uwContractRsrcFactory() {
		UwContractRsrcFactory result = new UwContractRsrcFactory();
		return result;
	}

	@Bean
	public AnnualFieldRsrcFactory annualFieldRsrcFactory() {
		AnnualFieldRsrcFactory result = new AnnualFieldRsrcFactory();
		return result;
	}
	
	@Bean
	public InventoryContractRsrcFactory inventoryContractRsrcFactory() {
		InventoryContractRsrcFactory result = new InventoryContractRsrcFactory();
		return result;
	}
	
	@Bean
	public CommodityRsrcFactory commodityRsrcFactory() {
		CommodityRsrcFactory result = new CommodityRsrcFactory();
		return result;
	}
	
	@Bean
	public CirrasDataSyncRsrcFactory cirrasDataSyncRsrcFactory() {
		CirrasDataSyncRsrcFactory result = new CirrasDataSyncRsrcFactory();
		return result;
	}	

	@Bean
	public LandDataSyncRsrcFactory landDataSyncRsrcFactory() {
		LandDataSyncRsrcFactory result = new LandDataSyncRsrcFactory();
		return result;
	}	

	@Bean
	public LegalLandRsrcFactory legalLandRsrcFactory() {
		LegalLandRsrcFactory result = new LegalLandRsrcFactory();
		return result;
	}
	
	@Bean
	public FieldRsrcFactory fieldRsrcFactory() {
		FieldRsrcFactory result = new FieldRsrcFactory();
		return result;
	}	
	
	@Bean
	public LegalLandFieldXrefRsrcFactory legalLandFieldXrefRsrcFactory() {
		LegalLandFieldXrefRsrcFactory result = new LegalLandFieldXrefRsrcFactory();
		return result;
	}	
	
	@Bean
	public AnnualFieldDetailRsrcFactory annualFieldDetailRsrcFactory() {
		AnnualFieldDetailRsrcFactory result = new AnnualFieldDetailRsrcFactory();
		return result;
	}	
	
	@Bean
	public ContractedFieldDetailRsrcFactory contractedFieldDetailRsrcFactory() {
		ContractedFieldDetailRsrcFactory result = new ContractedFieldDetailRsrcFactory();
		return result;
	}	
	
	@Bean
	public DopYieldContractRsrcFactory dopYieldContractRsrcFactory() {
		DopYieldContractRsrcFactory result = new DopYieldContractRsrcFactory();
		return result;
	}	
	
	@Bean
	public YieldMeasUnitTypeCodeRsrcFactory yieldMeasUnitTypeCodeRsrcFactory() {
		YieldMeasUnitTypeCodeRsrcFactory result = new YieldMeasUnitTypeCodeRsrcFactory();
		return result;
	}

	@Bean
	public GradeModifierRsrcFactory gradeModifierRsrcFactory() {
		GradeModifierRsrcFactory result = new GradeModifierRsrcFactory();
		return result;
	}

	@Bean
	public GradeModifierTypeRsrcFactory gradeModifierTypeRsrcFactory() {
		GradeModifierTypeRsrcFactory result = new GradeModifierTypeRsrcFactory();
		return result;
	}
	
	@Bean
	public RiskAreaRsrcFactory riskAreaRsrcFactory() {
		RiskAreaRsrcFactory result = new RiskAreaRsrcFactory();
		return result;
	}

	@Bean
	public LegalLandRiskAreaXrefRsrcFactory legalLandRiskAreaXrefRsrcFactory() {
		LegalLandRiskAreaXrefRsrcFactory result = new LegalLandRiskAreaXrefRsrcFactory();
		return result;
	}

	@Bean
	public CommodityTypeCodeRsrcFactory commodityTypeCodeRsrcFactory() {
		CommodityTypeCodeRsrcFactory result = new CommodityTypeCodeRsrcFactory();
		return result;
	}

	@Bean
	public SeedingDeadlineRsrcFactory seedingDeadlineRsrcFactory() {
		SeedingDeadlineRsrcFactory result = new SeedingDeadlineRsrcFactory();
		return result;
	}

	@Bean
	public UnderwritingYearRsrcFactory underwritingYearRsrcFactory() {
		UnderwritingYearRsrcFactory result = new UnderwritingYearRsrcFactory();
		return result;
	}

	@Bean
	public CropVarietyInsurabilityRsrcFactory cropVarietyInsurabilityRsrcFactory() {
		CropVarietyInsurabilityRsrcFactory result = new CropVarietyInsurabilityRsrcFactory();
		return result;
	}

	@Bean
	public YieldMeasUnitConversionRsrcFactory yieldMeasUnitConversionRsrcFactory() {
		YieldMeasUnitConversionRsrcFactory result = new YieldMeasUnitConversionRsrcFactory();
		return result;
	}
	

}
