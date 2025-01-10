package ca.bc.gov.mal.cirras.underwriting.persistence.v1.spring;

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

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.AnnualFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CommodityTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CommodityTypeVarietyXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContactDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContactEmailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContactPhoneDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ContractedFieldDetailDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsurabilityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldRollupForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GradeModifierDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GradeModifierTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GrowerContactDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GrowerContractYearDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GrowerDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryCoverageTotalForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryFieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventorySeededGrainDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InventoryUnseededDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandRiskAreaXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.OfficeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.InsurancePlanDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.PolicyStatusCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.ProductDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.RiskAreaDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.SeedingDeadlineDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingCommentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingYearDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldAmendmentDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractCommodityDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldContractDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldSummaryDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.VerifiedYieldGrainBasketDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitConversionDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitPlanXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.YieldMeasUnitTypeCodeDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.AnnualFieldDetailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CommodityTypeCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CommodityTypeVarietyXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.ContactDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.ContactEmailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.ContactPhoneDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.ContractedFieldDetailDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CropCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CropVarietyDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CropVarietyInsPlantInsXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.CropVarietyInsurabilityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldContractCommodityForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldFieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldFieldForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldFieldRollupDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.DeclaredYieldFieldRollupForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.FieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.GradeModifierDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.GradeModifierTypeCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.GrowerContactDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.GrowerContractYearDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.GrowerDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventoryContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventoryContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventoryCoverageTotalForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventoryFieldDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventorySeededForageDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventorySeededGrainDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InventoryUnseededDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.LegalLandDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.LegalLandFieldXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.LegalLandRiskAreaXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.OfficeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.InsurancePlanDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.PolicyDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.PolicyStatusCodeDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.ProductDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.RiskAreaDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.SeedingDeadlineDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.UnderwritingCommentDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.UnderwritingYearDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.VerifiedYieldAmendmentDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.VerifiedYieldContractCommodityDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.VerifiedYieldContractDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.VerifiedYieldSummaryDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.VerifiedYieldGrainBasketDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.YieldMeasUnitConversionDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.YieldMeasUnitPlanXrefDaoImpl;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.YieldMeasUnitTypeCodeDaoImpl;
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
		configurer.setBasePackage("ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper");
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
}
