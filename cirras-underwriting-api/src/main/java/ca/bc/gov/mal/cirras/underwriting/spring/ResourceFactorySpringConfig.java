package ca.bc.gov.mal.cirras.underwriting.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CommodityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CommodityTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.ContractedFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CropVarietyInsurabilityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.FieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.GradeModifierRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.GradeModifierTypeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldDetailRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.AnnualFieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.CirrasDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LandDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandFieldXrefRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandRiskAreaXrefRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.LegalLandRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.RiskAreaRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.SeedingDeadlineRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UnderwritingYearRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UserSettingRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.UwContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.YieldMeasUnitConversionRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.data.assemblers.YieldMeasUnitTypeCodeRsrcFactory;

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
	
	@Bean
	public VerifiedYieldContractRsrcFactory verifiedYieldContractRsrcFactory() {
		VerifiedYieldContractRsrcFactory result = new VerifiedYieldContractRsrcFactory();
		return result;
	}	

	@Bean
	public UserSettingRsrcFactory userSettingRsrcFactory() {
		UserSettingRsrcFactory result = new UserSettingRsrcFactory();
		return result;
	}
	
}
