package ca.bc.gov.mal.cirras.underwriting.spring;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.sql.DataSource;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeVarietyXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactEmailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactPhoneDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsurabilityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContactDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContractYearDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandRiskAreaXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.OfficeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InsurancePlanDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyStatusCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.RiskAreaDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.SeedingDeadlineDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingYearDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UserSettingDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldAmendmentDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldSummaryDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldGrainBasketDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitPlanXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.AnnualFieldDetailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CommodityTypeVarietyXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactEmailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContactPhoneDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ContractedFieldDetailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsPlantInsXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsurabilityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractCommodityForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.DeclaredYieldFieldRollupForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.FieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GradeModifierTypeCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContactDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerContractYearDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.GrowerDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryCoverageTotalForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryFieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventorySeededGrainDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryUnseededDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandFieldXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandRiskAreaXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.OfficeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InsurancePlanDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryBerriesDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.InventoryContractCommodityBerriesDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyStatusCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.ProductDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.RiskAreaDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.SeedingDeadlineDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingCommentDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UnderwritingYearDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.UserSettingDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldAmendmentDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldSummaryDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.VerifiedYieldGrainBasketDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitConversionDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitPlanXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.YieldMeasUnitTypeCodeDaoImpl;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BooleanTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.InstantTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalDateTimeTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalDateTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalTimeTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.ResetDirtyInterceptor;

@Configuration
@EnableTransactionManagement
public class PersistenceSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(PersistenceSpringConfig.class);
	
	public PersistenceSpringConfig() {
		logger.debug("<PersistenceSpringConfig");
		
		logger.debug(">PersistenceSpringConfig");
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource cirrasUnderwritingDataSource) {
		return new DataSourceTransactionManager(cirrasUnderwritingDataSource);
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource cirrasUnderwritingDataSource) {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(cirrasUnderwritingDataSource);
		
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		
		configuration.setCacheEnabled(true);
		configuration.setLazyLoadingEnabled(true);
		configuration.setAggressiveLazyLoading(false);
		configuration.setLocalCacheScope(LocalCacheScope.SESSION);
		
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		typeHandlerRegistry.register(Boolean.class, JdbcType.VARCHAR, BooleanTypeHandler.class);
		typeHandlerRegistry.register(LocalTime.class, JdbcType.VARCHAR, LocalTimeTypeHandler.class);
		typeHandlerRegistry.register(LocalDate.class, JdbcType.DATE, LocalDateTypeHandler.class);
		typeHandlerRegistry.register(LocalDateTime.class, JdbcType.TIMESTAMP, LocalDateTimeTypeHandler.class);
		typeHandlerRegistry.register(Instant.class, JdbcType.TIMESTAMP, InstantTypeHandler.class);
		
		sessionFactory.setConfiguration(configuration);
		
		sessionFactory.setPlugins(new ResetDirtyInterceptor());
		
		return sessionFactory;
	}

	@Bean
	public static MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper");
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return configurer;
	}

	@Bean
	public PolicyDao policyDao() { 
		return new PolicyDaoImpl(); 
	}

	@Bean
	public InventoryUnseededDao inventoryUnseededDao() { 
		return new InventoryUnseededDaoImpl(); 
	}
	
	@Bean
	public InventorySeededGrainDao inventorySeededGrainDao() { 
		return new InventorySeededGrainDaoImpl(); 
	}

	@Bean
	public InventorySeededForageDao inventorySeededForageDao() { 
		return new InventorySeededForageDaoImpl(); 
	}
	
	@Bean
	public InventoryBerriesDao inventoryBerriesDao() { 
		return new InventoryBerriesDaoImpl(); 
	}

	@Bean
	public InventoryFieldDao inventoryFieldDao() { 
		return new InventoryFieldDaoImpl(); 
	}
	
	@Bean
	public ContractedFieldDetailDao contractedFieldDetailDao() { 
		return new ContractedFieldDetailDaoImpl(); 
	}

	@Bean
	public InventoryContractCommodityDao inventoryContractCommodityDao() { 
		return new InventoryContractCommodityDaoImpl(); 
	}

	@Bean
	public InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao() { 
		return new InventoryContractCommodityBerriesDaoImpl(); 
	}
	
	@Bean
	public InventoryContractDao inventoryContractDao() { 
		return new InventoryContractDaoImpl(); 
	}
	
	@Bean
	public CropCommodityDao cropCommodityDao() { 
		return new CropCommodityDaoImpl(); 
	}
	
	@Bean
	public CropVarietyDao cropVarietyDao() { 
		return new CropVarietyDaoImpl(); 
	}

	@Bean
	public CropVarietyInsurabilityDao cropVarietyInsurabilityDao() { 
		return new CropVarietyInsurabilityDaoImpl(); 
	}
	
	@Bean
	public CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao() { 
		return new CropVarietyInsPlantInsXrefDaoImpl(); 
	}

	@Bean
	public PolicyStatusCodeDao policyStatusCodeDao() { 
		return new PolicyStatusCodeDaoImpl(); 
	}

	@Bean
	public GrowerDao growerDao() { 
		return new GrowerDaoImpl(); 
	}

	@Bean
	public UnderwritingCommentDao underwritingCommentDao() { 
		return new UnderwritingCommentDaoImpl(); 
	}

	@Bean
	public LegalLandDao legalLandDao() { 
		return new LegalLandDaoImpl(); 
	}
	
	@Bean
	public FieldDao fieldDao() { 
		return new FieldDaoImpl(); 
	}
	
	@Bean
	public LegalLandFieldXrefDao legalLandFieldXrefDao() { 
		return new LegalLandFieldXrefDaoImpl(); 
	}
	
	@Bean
	public AnnualFieldDetailDao annualFieldDetailDao() { 
		return new AnnualFieldDetailDaoImpl(); 
	}

	@Bean
	public GrowerContractYearDao growerContractYearDao() { 
		return new GrowerContractYearDaoImpl(); 
	}
	
	@Bean
	public ContactDao contactDao() { 
		return new ContactDaoImpl(); 
	}
	
	@Bean
	public GrowerContactDao growerContactDao() { 
		return new GrowerContactDaoImpl(); 
	}
	
	@Bean
	public ContactEmailDao contactEmailDao() { 
		return new ContactEmailDaoImpl(); 
	}
	
	@Bean
	public ContactPhoneDao contactPhoneDao() { 
		return new ContactPhoneDaoImpl(); 
	}
	
	@Bean
	public OfficeDao officeDao() { 
		return new OfficeDaoImpl(); 
	}

	@Bean
	public InsurancePlanDao insurancePlanDao() { 
		return new InsurancePlanDaoImpl(); 
	}
	
	@Bean
	public CommodityTypeCodeDao commodityTypeCodeDao() { 
		return new CommodityTypeCodeDaoImpl(); 
	}
	
	@Bean
	public CommodityTypeVarietyXrefDao commodityTypeVarietyXrefDao() { 
		return new CommodityTypeVarietyXrefDaoImpl(); 
	}

	@Bean
	public SeedingDeadlineDao seedingDeadlineDao() { 
		return new SeedingDeadlineDaoImpl(); 
	}
	
	@Bean
	public YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao() { 
		return new YieldMeasUnitTypeCodeDaoImpl(); 
	}

	@Bean
	public YieldMeasUnitPlanXrefDao yieldMeasUnitPlanXrefDao() { 
		return new YieldMeasUnitPlanXrefDaoImpl(); 
	}

	@Bean
	public YieldMeasUnitConversionDao yieldMeasUnitConversionDao() { 
		return new YieldMeasUnitConversionDaoImpl(); 
	}
	
	@Bean
	public GradeModifierDao gradeModifierDao() { 
		return new GradeModifierDaoImpl(); 
	}
	
	@Bean
	public GradeModifierTypeCodeDao gradeModifierTypeCodeDao() { 
		return new GradeModifierTypeCodeDaoImpl(); 
	}
	
	@Bean
	public DeclaredYieldContractDao declaredYieldContractDao() { 
		return new DeclaredYieldContractDaoImpl();
	}

	@Bean
	public DeclaredYieldFieldDao declaredYieldFieldDao() { 
		return new DeclaredYieldFieldDaoImpl(); 
	}

	@Bean
	public DeclaredYieldFieldForageDao declaredYieldFieldForageDao() { 
		return new DeclaredYieldFieldForageDaoImpl(); 
	}
	
	@Bean
	public DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao() { 
		return new DeclaredYieldFieldRollupDaoImpl(); 
	}

	@Bean
	public DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao() { 
		return new DeclaredYieldContractCommodityDaoImpl(); 
	}
	
	@Bean
	public DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao() { 
		return new DeclaredYieldContractCommodityForageDaoImpl(); 
	}
	
	@Bean
	public DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao() { 
		return new DeclaredYieldFieldRollupForageDaoImpl(); 
	}
	
	@Bean
	public InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao() { 
		return new InventoryCoverageTotalForageDaoImpl();
	}
	
	@Bean
	public RiskAreaDao riskAreaDao() { 
		return new RiskAreaDaoImpl(); 
	}	
	
	@Bean
	public LegalLandRiskAreaXrefDao legalLandRiskAreaXrefDao() { 
		return new LegalLandRiskAreaXrefDaoImpl(); 
	}

	@Bean
	public UnderwritingYearDao underwritingYearDao() { 
		return new UnderwritingYearDaoImpl(); 
	}	

	@Bean
	public VerifiedYieldContractDao verifiedYieldContractDao() { 
		return new VerifiedYieldContractDaoImpl();
	}

	@Bean
	public VerifiedYieldContractCommodityDao verifiedYieldContractCommodityDao() { 
		return new VerifiedYieldContractCommodityDaoImpl(); 
	}	

	@Bean
	public ProductDao productDao() { 
		return new ProductDaoImpl(); 
	}

	@Bean
	public VerifiedYieldAmendmentDao verifiedYieldAmendmentDao() { 
		return new VerifiedYieldAmendmentDaoImpl();
	}
	
	@Bean
	public VerifiedYieldSummaryDao verifiedYieldSummaryDao() { 
		return new VerifiedYieldSummaryDaoImpl();
	}
	
	@Bean
	public VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao() { 
		return new VerifiedYieldGrainBasketDaoImpl();
	}
	@Bean
	public UserSettingDao userSettingDao() { 
		return new UserSettingDaoImpl();
	}
}
