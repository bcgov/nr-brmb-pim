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
		return new PolicyDao(); 
	}

	@Bean
	public InventoryUnseededDao inventoryUnseededDao() { 
		return new InventoryUnseededDao(); 
	}
	
	@Bean
	public InventorySeededGrainDao inventorySeededGrainDao() { 
		return new InventorySeededGrainDao(); 
	}

	@Bean
	public InventorySeededForageDao inventorySeededForageDao() { 
		return new InventorySeededForageDao(); 
	}
	
	@Bean
	public InventoryBerriesDao inventoryBerriesDao() { 
		return new InventoryBerriesDao(); 
	}

	@Bean
	public InventoryFieldDao inventoryFieldDao() { 
		return new InventoryFieldDao(); 
	}
	
	@Bean
	public ContractedFieldDetailDao contractedFieldDetailDao() { 
		return new ContractedFieldDetailDao(); 
	}

	@Bean
	public InventoryContractCommodityDao inventoryContractCommodityDao() { 
		return new InventoryContractCommodityDao(); 
	}

	@Bean
	public InventoryContractCommodityBerriesDao inventoryContractCommodityBerriesDao() { 
		return new InventoryContractCommodityBerriesDao(); 
	}
	
	@Bean
	public InventoryContractDao inventoryContractDao() { 
		return new InventoryContractDao(); 
	}
	
	@Bean
	public CropCommodityDao cropCommodityDao() { 
		return new CropCommodityDao(); 
	}
	
	@Bean
	public CropVarietyDao cropVarietyDao() { 
		return new CropVarietyDao(); 
	}

	@Bean
	public CropVarietyInsurabilityDao cropVarietyInsurabilityDao() { 
		return new CropVarietyInsurabilityDao(); 
	}
	
	@Bean
	public CropVarietyInsPlantInsXrefDao cropVarietyInsPlantInsXrefDao() { 
		return new CropVarietyInsPlantInsXrefDao(); 
	}

	@Bean
	public PolicyStatusCodeDao policyStatusCodeDao() { 
		return new PolicyStatusCodeDao(); 
	}

	@Bean
	public GrowerDao growerDao() { 
		return new GrowerDao(); 
	}

	@Bean
	public UnderwritingCommentDao underwritingCommentDao() { 
		return new UnderwritingCommentDao(); 
	}

	@Bean
	public LegalLandDao legalLandDao() { 
		return new LegalLandDao(); 
	}
	
	@Bean
	public FieldDao fieldDao() { 
		return new FieldDao(); 
	}
	
	@Bean
	public LegalLandFieldXrefDao legalLandFieldXrefDao() { 
		return new LegalLandFieldXrefDao(); 
	}
	
	@Bean
	public AnnualFieldDetailDao annualFieldDetailDao() { 
		return new AnnualFieldDetailDao(); 
	}

	@Bean
	public GrowerContractYearDao growerContractYearDao() { 
		return new GrowerContractYearDao(); 
	}
	
	@Bean
	public ContactDao contactDao() { 
		return new ContactDao(); 
	}
	
	@Bean
	public GrowerContactDao growerContactDao() { 
		return new GrowerContactDao(); 
	}
	
	@Bean
	public ContactEmailDao contactEmailDao() { 
		return new ContactEmailDao(); 
	}
	
	@Bean
	public ContactPhoneDao contactPhoneDao() { 
		return new ContactPhoneDao(); 
	}
	
	@Bean
	public OfficeDao officeDao() { 
		return new OfficeDao(); 
	}

	@Bean
	public InsurancePlanDao insurancePlanDao() { 
		return new InsurancePlanDao(); 
	}
	
	@Bean
	public CommodityTypeCodeDao commodityTypeCodeDao() { 
		return new CommodityTypeCodeDao(); 
	}
	
	@Bean
	public CommodityTypeVarietyXrefDao commodityTypeVarietyXrefDao() { 
		return new CommodityTypeVarietyXrefDao(); 
	}

	@Bean
	public SeedingDeadlineDao seedingDeadlineDao() { 
		return new SeedingDeadlineDao(); 
	}
	
	@Bean
	public YieldMeasUnitTypeCodeDao yieldMeasUnitTypeCodeDao() { 
		return new YieldMeasUnitTypeCodeDao(); 
	}

	@Bean
	public YieldMeasUnitPlanXrefDao yieldMeasUnitPlanXrefDao() { 
		return new YieldMeasUnitPlanXrefDao(); 
	}

	@Bean
	public YieldMeasUnitConversionDao yieldMeasUnitConversionDao() { 
		return new YieldMeasUnitConversionDao(); 
	}
	
	@Bean
	public GradeModifierDao gradeModifierDao() { 
		return new GradeModifierDao(); 
	}
	
	@Bean
	public GradeModifierTypeCodeDao gradeModifierTypeCodeDao() { 
		return new GradeModifierTypeCodeDao(); 
	}
	
	@Bean
	public DeclaredYieldContractDao declaredYieldContractDao() { 
		return new DeclaredYieldContractDao();
	}

	@Bean
	public DeclaredYieldFieldDao declaredYieldFieldDao() { 
		return new DeclaredYieldFieldDao(); 
	}

	@Bean
	public DeclaredYieldFieldForageDao declaredYieldFieldForageDao() { 
		return new DeclaredYieldFieldForageDao(); 
	}
	
	@Bean
	public DeclaredYieldFieldRollupDao declaredYieldFieldRollupDao() { 
		return new DeclaredYieldFieldRollupDao(); 
	}

	@Bean
	public DeclaredYieldContractCommodityDao declaredYieldContractCommodityDao() { 
		return new DeclaredYieldContractCommodityDao(); 
	}
	
	@Bean
	public DeclaredYieldContractCommodityForageDao declaredYieldContractCommodityForageDao() { 
		return new DeclaredYieldContractCommodityForageDao(); 
	}
	
	@Bean
	public DeclaredYieldFieldRollupForageDao declaredYieldFieldRollupForageDao() { 
		return new DeclaredYieldFieldRollupForageDao(); 
	}
	
	@Bean
	public InventoryCoverageTotalForageDao inventoryCoverageTotalForageDao() { 
		return new InventoryCoverageTotalForageDao();
	}
	
	@Bean
	public RiskAreaDao riskAreaDao() { 
		return new RiskAreaDao(); 
	}	
	
	@Bean
	public LegalLandRiskAreaXrefDao legalLandRiskAreaXrefDao() { 
		return new LegalLandRiskAreaXrefDao(); 
	}

	@Bean
	public UnderwritingYearDao underwritingYearDao() { 
		return new UnderwritingYearDao(); 
	}	

	@Bean
	public VerifiedYieldContractDao verifiedYieldContractDao() { 
		return new VerifiedYieldContractDao();
	}

	@Bean
	public VerifiedYieldContractCommodityDao verifiedYieldContractCommodityDao() { 
		return new VerifiedYieldContractCommodityDao(); 
	}	

	@Bean
	public ProductDao productDao() { 
		return new ProductDao(); 
	}

	@Bean
	public VerifiedYieldAmendmentDao verifiedYieldAmendmentDao() { 
		return new VerifiedYieldAmendmentDao();
	}
	
	@Bean
	public VerifiedYieldSummaryDao verifiedYieldSummaryDao() { 
		return new VerifiedYieldSummaryDao();
	}
	
	@Bean
	public VerifiedYieldGrainBasketDao verifiedYieldGrainBasketDao() { 
		return new VerifiedYieldGrainBasketDao();
	}
	@Bean
	public UserSettingDao userSettingDao() { 
		return new UserSettingDao();
	}
}
