package ca.bc.gov.mal.cirras.underwriting.spring;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.underwriting.spring.PersistenceSpringConfig;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.services.LandDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasUnderwritingServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.LandDataSyncServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasCommodityService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasDopYieldService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasInventoryService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUwLandManagementService;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasVerifiedYieldService;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasCommodityServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasDataSyncServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasDopYieldServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasInventoryServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasMaintenanceServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasUwLandManagementServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.impl.CirrasVerifiedYieldServiceImpl;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CommodityFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CommodityTypeCodeFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.ContractedFieldDetailFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CropVarietyInsurabilityFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.DopYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.FieldFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.GradeModifierFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.GradeModifierTypeFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.AnnualFieldDetailFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.AnnualFieldFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.CirrasDataSyncFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LandDataSyncFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandFieldXrefFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandRiskAreaXrefFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.RiskAreaFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.SeedingDeadlineFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.UnderwritingYearFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.UserSettingFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.UwContractFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.VerifiedYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.YieldMeasUnitConversionFactory;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.YieldMeasUnitTypeCodeFactory;
import ca.bc.gov.mal.cirras.underwriting.services.reports.JasperReportService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.UnderwritingServiceHelper;
import ca.bc.gov.mal.cirras.underwriting.services.utils.BerriesService;
import ca.bc.gov.mal.cirras.underwriting.services.utils.OutOfSync;

import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;

@Configuration
@Import({
	CodeHierarchySpringConfig.class,  // can't remove this because some wfone stuff depends on it
	CodeTableSpringConfig.class, 
	PersistenceSpringConfig.class,
	JasperReportServiceSpringConfig.class
})
public class ServiceApiSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServiceApiSpringConfig.class);

	public ServiceApiSpringConfig() {
		logger.debug("<ServiceApiSpringConfig");
		
		logger.debug(">ServiceApiSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	@Autowired Properties applicationProperties;
	@Autowired CirrasPolicyService cirrasPolicyService;
	
    // Beans provided by ResourceFactorySpringConfig
	@Autowired AnnualFieldFactory annualFieldFactory;
	@Autowired InventoryContractFactory inventoryContractFactory;
	@Autowired UwContractFactory uwContractFactory;
	@Autowired CommodityFactory commodityFactory;
	@Autowired CirrasDataSyncFactory cirrasDataSyncFactory;
	@Autowired LandDataSyncFactory landDataSyncFactory;
	@Autowired LegalLandFactory legalLandFactory;
	@Autowired FieldFactory fieldFactory;
	@Autowired LegalLandFieldXrefFactory legalLandFieldXrefFactory;
	@Autowired AnnualFieldDetailFactory annualFieldDetailFactory;
	@Autowired ContractedFieldDetailFactory contractedFieldDetailFactory;
	@Autowired DopYieldContractFactory dopYieldContractFactory;
	@Autowired YieldMeasUnitTypeCodeFactory yieldMeasUnitTypeCodeFactory;
	@Autowired GradeModifierFactory gradeModifierFactory;
	@Autowired GradeModifierTypeFactory gradeModifierTypeFactory;
	@Autowired RiskAreaFactory riskAreaFactory;
	@Autowired LegalLandRiskAreaXrefFactory legalLandRiskAreaXrefFactory;
	@Autowired CommodityTypeCodeFactory commodityTypeCodeFactory;
	@Autowired SeedingDeadlineFactory seedingDeadlineFactory;
	@Autowired UnderwritingYearFactory underwritingYearFactory;
	@Autowired CropVarietyInsurabilityFactory cropVarietyInsurabilityFactory;
	@Autowired YieldMeasUnitConversionFactory yieldMeasUnitConversionFactory;
	@Autowired VerifiedYieldContractFactory verifiedYieldContractFactory;
	@Autowired UserSettingFactory userSettingFactory;
	
	// Imported Spring Config
	@Autowired CodeTableSpringConfig codeTableSpringConfig;
	@Autowired CodeHierarchySpringConfig codeHierarchySpringConfig;
	@Autowired PersistenceSpringConfig persistenceSpringConfig;
	@Autowired JasperReportService jasperReportService;
	
//	@Bean
//	public ModelValidator modelValidator() {
//		ModelValidator result;
//		
//		result = new ModelValidator();
//		result.setCachedCodeTables(codeTableSpringConfig.cachedCodeTables());
//		result.setMessageSource(messageSource);
//		
//		return result;
//	}
	
	@Bean
	public UnderwritingServiceHelper underwritingServiceHelper() {
		UnderwritingServiceHelper result = new UnderwritingServiceHelper();
		
		result.setInventoryCoverageTotalForageDao(persistenceSpringConfig.inventoryCoverageTotalForageDao());
		
		return result;
	}
	
	@Bean
	public BerriesService berriesService() {
		BerriesService result = new BerriesService();
		
		result.setInventoryContractCommodityBerriesDao(persistenceSpringConfig.inventoryContractCommodityBerriesDao());
		result.setInventoryBerriesDao(persistenceSpringConfig.inventoryBerriesDao());
		
		result.setInventoryContractFactory(inventoryContractFactory);

		return result;
	}
	
	@Bean
	public OutOfSync outOfSync() {
		OutOfSync result = new OutOfSync();
		return result;
	}
	
	@Bean()
	public CirrasUnderwritingService cirrasUnderwritingService() {
		CirrasUnderwritingServiceImpl result;
		
		result = new CirrasUnderwritingServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setAnnualFieldFactory(annualFieldFactory);
		result.setUwContractFactory(uwContractFactory);
		result.setUserSettingFactory(userSettingFactory);

		result.setFieldDao(persistenceSpringConfig.fieldDao());
		result.setPolicyDao(persistenceSpringConfig.policyDao());
		result.setLegalLandDao(persistenceSpringConfig.legalLandDao());
		result.setUserSettingDao(persistenceSpringConfig.userSettingDao());
		
		result.setUnderwritingServiceHelper(underwritingServiceHelper());
		
		result.setOutOfSync(outOfSync());
		
		return result;
	}

	@Bean()
	public CirrasInventoryService cirrasInventoryService() {
		CirrasInventoryServiceImpl result;
		
		result = new CirrasInventoryServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setInventoryContractFactory(inventoryContractFactory);
		result.setAnnualFieldFactory(annualFieldFactory);
		result.setLegalLandFactory(legalLandFactory);
		result.setFieldFactory(fieldFactory);
		result.setLegalLandFieldXrefFactory(legalLandFieldXrefFactory);
		result.setAnnualFieldDetailFactory(annualFieldDetailFactory);
		result.setContractedFieldDetailFactory(contractedFieldDetailFactory);
		result.setUwContractFactory(uwContractFactory);

		result.setInventoryContractCommodityDao(persistenceSpringConfig.inventoryContractCommodityDao());
		result.setInventoryCoverageTotalForageDao(persistenceSpringConfig.inventoryCoverageTotalForageDao());
		result.setInventoryContractCommodityBerriesDao(persistenceSpringConfig.inventoryContractCommodityBerriesDao());
		result.setInventoryContractDao(persistenceSpringConfig.inventoryContractDao());
		result.setInventoryFieldDao(persistenceSpringConfig.inventoryFieldDao());
		result.setInventorySeededGrainDao(persistenceSpringConfig.inventorySeededGrainDao());
		result.setInventoryUnseededDao(persistenceSpringConfig.inventoryUnseededDao());
		result.setInventorySeededForageDao(persistenceSpringConfig.inventorySeededForageDao());
		result.setInventoryBerriesDao(persistenceSpringConfig.inventoryBerriesDao());
		result.setPolicyDao(persistenceSpringConfig.policyDao());
		result.setUnderwritingCommentDao(persistenceSpringConfig.underwritingCommentDao());
		result.setLegalLandDao(persistenceSpringConfig.legalLandDao());
		result.setFieldDao(persistenceSpringConfig.fieldDao());
		result.setAnnualFieldDetailDao(persistenceSpringConfig.annualFieldDetailDao());
		result.setContractedFieldDetailDao(persistenceSpringConfig.contractedFieldDetailDao());
		result.setLegalLandFieldXrefDao(persistenceSpringConfig.legalLandFieldXrefDao());
		result.setInsurancePlanDao(persistenceSpringConfig.insurancePlanDao());
		result.setDeclaredYieldFieldDao(persistenceSpringConfig.declaredYieldFieldDao());
		result.setDeclaredYieldFieldForageDao(persistenceSpringConfig.declaredYieldFieldForageDao());
		result.setDeclaredYieldContractDao(persistenceSpringConfig.declaredYieldContractDao());
		result.setCropCommodityDao(persistenceSpringConfig.cropCommodityDao());
		result.setGrowerContractYearDao(persistenceSpringConfig.growerContractYearDao());

		result.setCirrasPolicyService(cirrasPolicyService);

		result.setJasperReportService(jasperReportService);
		
		result.setUnderwritingServiceHelper(underwritingServiceHelper());
		result.setBerriesService(berriesService());
		
		result.setOutOfSync(outOfSync());
		
		return result;
	}
	

	@Bean()
	public CirrasCommodityService cirrasCommodityService() {
		CirrasCommodityServiceImpl result;
		
		result = new CirrasCommodityServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setCommodityFactory(commodityFactory);

		result.setCropCommodityDao(persistenceSpringConfig.cropCommodityDao());
		result.setCropVarietyDao(persistenceSpringConfig.cropVarietyDao());
		result.setCommodityTypeCodeDao(persistenceSpringConfig.commodityTypeCodeDao());
		result.setCropVarietyInsPlantInsXrefDao( persistenceSpringConfig.cropVarietyInsPlantInsXrefDao());
		
		return result;
	}
	
	@Bean()
	public CirrasDataSyncService cirrasDataSyncService() {
		CirrasDataSyncServiceImpl result;
		
		result = new CirrasDataSyncServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setCirrasDataSyncFactory(cirrasDataSyncFactory);

		result.setPolicyStatusCodeDao(persistenceSpringConfig.policyStatusCodeDao());
		result.setGrowerDao(persistenceSpringConfig.growerDao());
		result.setPolicyDao(persistenceSpringConfig.policyDao());
		result.setProductDao(persistenceSpringConfig.productDao());
		result.setCropCommodityDao(persistenceSpringConfig.cropCommodityDao());
		result.setCropVarietyDao(persistenceSpringConfig.cropVarietyDao());
		result.setOfficeDao(persistenceSpringConfig.officeDao());
		result.setContactDao(persistenceSpringConfig.contactDao());
		result.setGrowerContactDao(persistenceSpringConfig.growerContactDao());
		result.setContactEmailDao(persistenceSpringConfig.contactEmailDao());
		result.setContactPhoneDao(persistenceSpringConfig.contactPhoneDao());
		result.setCommodityTypeCodeDao(persistenceSpringConfig.commodityTypeCodeDao());
		result.setCommodityTypeVarietyXrefDao(persistenceSpringConfig.commodityTypeVarietyXrefDao());
		
		return result;
	}
	
	@Bean()
	public LandDataSyncService landDataSyncService() {
		LandDataSyncServiceImpl result;
		
		result = new LandDataSyncServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setLandDataSyncFactory(landDataSyncFactory);
		result.setInventoryContractFactory(inventoryContractFactory);
		result.setAnnualFieldDetailFactory(annualFieldDetailFactory);
		result.setContractedFieldDetailFactory(contractedFieldDetailFactory);

		result.setLegalLandDao(persistenceSpringConfig.legalLandDao());
		result.setFieldDao(persistenceSpringConfig.fieldDao());
		result.setLegalLandFieldXrefDao(persistenceSpringConfig.legalLandFieldXrefDao());
		result.setAnnualFieldDetailDao(persistenceSpringConfig.annualFieldDetailDao());
		result.setGrowerContractYearDao(persistenceSpringConfig.growerContractYearDao());
		result.setContractedFieldDetailDao(persistenceSpringConfig.contractedFieldDetailDao());
		result.setInventoryFieldDao(persistenceSpringConfig.inventoryFieldDao());
		result.setInventoryUnseededDao(persistenceSpringConfig.inventoryUnseededDao());
		result.setInventorySeededGrainDao(persistenceSpringConfig.inventorySeededGrainDao());
		result.setInventorySeededForageDao(persistenceSpringConfig.inventorySeededForageDao());
		result.setInventoryBerriesDao(persistenceSpringConfig.inventoryBerriesDao());
		result.setUnderwritingCommentDao(persistenceSpringConfig.underwritingCommentDao());
		result.setInventoryContractDao(persistenceSpringConfig.inventoryContractDao());
		result.setInventoryContractCommodityDao(persistenceSpringConfig.inventoryContractCommodityDao());
		result.setInventoryCoverageTotalForageDao(persistenceSpringConfig.inventoryCoverageTotalForageDao());
		result.setDeclaredYieldContractDao(persistenceSpringConfig.declaredYieldContractDao());
		result.setDeclaredYieldFieldDao(persistenceSpringConfig.declaredYieldFieldDao());
		result.setDeclaredYieldFieldForageDao(persistenceSpringConfig.declaredYieldFieldForageDao());
		result.setDeclaredYieldFieldRollupDao(persistenceSpringConfig.declaredYieldFieldRollupDao());
		result.setDeclaredYieldContractCommodityDao(persistenceSpringConfig.declaredYieldContractCommodityDao());
		result.setDeclaredYieldContractCommodityForageDao(persistenceSpringConfig.declaredYieldContractCommodityForageDao());
		result.setDeclaredYieldFieldRollupForageDao(persistenceSpringConfig.declaredYieldFieldRollupForageDao());

		result.setUnderwritingServiceHelper(underwritingServiceHelper());
		
		return result;
	}

	@Bean()
	public CirrasDopYieldService cirrasDopYieldService() {
		CirrasDopYieldServiceImpl result;
		
		result = new CirrasDopYieldServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setInventoryContractFactory(inventoryContractFactory);
		result.setDopYieldContractFactory(dopYieldContractFactory);
		result.setYieldMeasUnitTypeCodeFactory(yieldMeasUnitTypeCodeFactory);

		result.setPolicyDao(persistenceSpringConfig.policyDao());
		result.setYieldMeasUnitTypeCodeDao(persistenceSpringConfig.yieldMeasUnitTypeCodeDao());
		result.setYieldMeasUnitConversionDao(persistenceSpringConfig.yieldMeasUnitConversionDao());
		result.setDeclaredYieldContractDao(persistenceSpringConfig.declaredYieldContractDao());
		result.setInventoryFieldDao(persistenceSpringConfig.inventoryFieldDao());
		result.setInventorySeededGrainDao(persistenceSpringConfig.inventorySeededGrainDao());
		result.setContractedFieldDetailDao(persistenceSpringConfig.contractedFieldDetailDao());
		result.setDeclaredYieldFieldDao(persistenceSpringConfig.declaredYieldFieldDao());
		result.setDeclaredYieldFieldForageDao(persistenceSpringConfig.declaredYieldFieldForageDao());
		result.setDeclaredYieldFieldRollupDao(persistenceSpringConfig.declaredYieldFieldRollupDao());
		result.setDeclaredYieldContractCommodityDao(persistenceSpringConfig.declaredYieldContractCommodityDao());
		result.setDeclaredYieldContractCommodityForageDao(persistenceSpringConfig.declaredYieldContractCommodityForageDao());
		result.setDeclaredYieldFieldRollupForageDao(persistenceSpringConfig.declaredYieldFieldRollupForageDao());
		result.setInventoryContractCommodityDao(persistenceSpringConfig.inventoryContractCommodityDao());
		result.setInventorySeededForageDao(persistenceSpringConfig.inventorySeededForageDao());
		result.setUnderwritingCommentDao(persistenceSpringConfig.underwritingCommentDao());
		
		result.setJasperReportService(jasperReportService);
		
		return result;
	}
	
	@Bean()
	public CirrasUwLandManagementService cirrasUwLandManagementService() {
		CirrasUwLandManagementServiceImpl result;
		
		result = new CirrasUwLandManagementServiceImpl();
		
		result.setApplicationProperties(applicationProperties);

		result.setLegalLandDao(persistenceSpringConfig.legalLandDao());
		result.setRiskAreaDao(persistenceSpringConfig.riskAreaDao());
		result.setLegalLandRiskAreaXrefDao(persistenceSpringConfig.legalLandRiskAreaXrefDao());
		result.setFieldDao(persistenceSpringConfig.fieldDao());
		
		result.setLegalLandFactory(legalLandFactory);
		result.setRiskAreaFactory(riskAreaFactory);
		result.setLegalLandRiskAreaXrefFactory(legalLandRiskAreaXrefFactory);
		
		return result;
	}
	
	@Bean()
	public CirrasMaintenanceService cirrasMaintenanceService() {
		CirrasMaintenanceServiceImpl result;
		
		result = new CirrasMaintenanceServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setCommodityTypeCodeFactory(commodityTypeCodeFactory);
		result.setSeedingDeadlineFactory(seedingDeadlineFactory);
		result.setUnderwritingYearFactory(underwritingYearFactory);
		result.setGradeModifierFactory(gradeModifierFactory);
		result.setGradeModifierTypeFactory(gradeModifierTypeFactory);
		result.setCropVarietyInsurabilityFactory(cropVarietyInsurabilityFactory);
		result.setCommodityFactory(commodityFactory);
		result.setYieldMeasUnitConversionFactory(yieldMeasUnitConversionFactory);

		result.setCommodityTypeCodeDao(persistenceSpringConfig.commodityTypeCodeDao());
		result.setSeedingDeadlineDao(persistenceSpringConfig.seedingDeadlineDao());
		result.setUnderwritingYearDao(persistenceSpringConfig.underwritingYearDao());
		result.setGradeModifierDao(persistenceSpringConfig.gradeModifierDao());
		result.setGradeModifierTypeCodeDao(persistenceSpringConfig.gradeModifierTypeCodeDao());
		result.setCropVarietyInsurabilityDao(persistenceSpringConfig.cropVarietyInsurabilityDao());
		result.setCropVarietyInsPlantInsXrefDao(persistenceSpringConfig.cropVarietyInsPlantInsXrefDao());
		result.setYieldMeasUnitConversionDao(persistenceSpringConfig.yieldMeasUnitConversionDao());
		result.setDeclaredYieldContractCommodityDao(persistenceSpringConfig.declaredYieldContractCommodityDao());
		result.setDeclaredYieldFieldRollupDao(persistenceSpringConfig.declaredYieldFieldRollupDao());
		result.setDeclaredYieldFieldDao(persistenceSpringConfig.declaredYieldFieldDao());
		result.setDeclaredYieldContractCommodityForageDao(persistenceSpringConfig.declaredYieldContractCommodityForageDao());
		result.setDeclaredYieldFieldRollupForageDao(persistenceSpringConfig.declaredYieldFieldRollupForageDao());
		result.setDeclaredYieldFieldForageDao(persistenceSpringConfig.declaredYieldFieldForageDao());
		
		return result;
	}

	@Bean()
	public CirrasVerifiedYieldService cirrasVerifiedYieldService() {
		CirrasVerifiedYieldServiceImpl result;
		
		result = new CirrasVerifiedYieldServiceImpl();
		result.setApplicationProperties(applicationProperties);

		result.setVerifiedYieldContractFactory(verifiedYieldContractFactory);
		result.setInventoryContractFactory(inventoryContractFactory);

		result.setPolicyDao(persistenceSpringConfig.policyDao());
		result.setInventoryFieldDao(persistenceSpringConfig.inventoryFieldDao());
		result.setInventorySeededGrainDao(persistenceSpringConfig.inventorySeededGrainDao());
		result.setInventorySeededForageDao(persistenceSpringConfig.inventorySeededForageDao());
		result.setContractedFieldDetailDao(persistenceSpringConfig.contractedFieldDetailDao());
		result.setDeclaredYieldContractDao(persistenceSpringConfig.declaredYieldContractDao());
		result.setDeclaredYieldContractCommodityDao(persistenceSpringConfig.declaredYieldContractCommodityDao());
		result.setDeclaredYieldContractCommodityForageDao(persistenceSpringConfig.declaredYieldContractCommodityForageDao());
		result.setDeclaredYieldFieldRollupForageDao(persistenceSpringConfig.declaredYieldFieldRollupForageDao());
		result.setVerifiedYieldContractDao(persistenceSpringConfig.verifiedYieldContractDao());
		result.setVerifiedYieldContractCommodityDao(persistenceSpringConfig.verifiedYieldContractCommodityDao());
		result.setVerifiedYieldAmendmentDao(persistenceSpringConfig.verifiedYieldAmendmentDao());
		result.setVerifiedYieldSummaryDao(persistenceSpringConfig.verifiedYieldSummaryDao());
		result.setVerifiedYieldGrainBasketDao(persistenceSpringConfig.verifiedYieldGrainBasketDao());
		result.setProductDao(persistenceSpringConfig.productDao());
		result.setUnderwritingCommentDao(persistenceSpringConfig.underwritingCommentDao());
		
		return result;
	}	
}
