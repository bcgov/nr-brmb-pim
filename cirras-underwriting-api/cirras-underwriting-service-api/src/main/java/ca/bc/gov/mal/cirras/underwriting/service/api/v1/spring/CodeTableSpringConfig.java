package ca.bc.gov.mal.cirras.underwriting.service.api.v1.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.CachedCodeTables;
import ca.bc.gov.nrs.wfone.common.persistence.code.dao.CodeTableConfig;
import ca.bc.gov.nrs.wfone.common.persistence.code.spring.CodePersistenceSpringConfig;


@Configuration
@Import({
	CodePersistenceSpringConfig.class
})
public class CodeTableSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(CodeTableSpringConfig.class);
	
	public CodeTableSpringConfig() {
		logger.debug("<CodeTableSpringConfig");
		
		logger.debug(">CodeTableSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	
	// Imported Spring Config
	@Autowired CodePersistenceSpringConfig codePersistenceSpringConfig;
	
	@Bean
	public CachedCodeTables cachedCodeTables() {
		CachedCodeTables result;
		
		result = new CachedCodeTables();
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableConfigs(codeTableConfigs());
		
		return result;
	}

	@Bean
	public List<CodeTableConfig> codeTableConfigs() {
		List<CodeTableConfig> result = new ArrayList<>();

//		result.add(claimCalcCreateUserCodeTableConfig());
//		result.add(claimCalcUpdateUserCodeTableConfig());
		result.add(policyStatusCodeTableConfig());
		result.add(plantInsurabilityTypeCodeTableConfig());
		result.add(foragePlantInsurabilityTypeCodeTableConfig());
		result.add(berriesPlantInsurabilityTypeCodeTableConfig());
		result.add(landIdentifierTypeCodeTableConfig());
		result.add(primaryReferenceTypeCodeTableConfig());
		result.add(verifiedYieldAmendmentCodeTableConfig());
		result.add(policyCropYearCodeTableConfig());
		result.add(insurancePlanTableConfig());
		result.add(officeTableConfig());

		return result;
	}	
			
//	@Bean
//	public CodeTableConfig claimCalcCreateUserCodeTableConfig() {
//		String fetchSql = "SELECT DISTINCT ccu.claim_calculation_user_guid AS CODE, " + 
//						  "                CASE WHEN ccu.given_name IS NOT NULL AND ccu.family_name IS NOT NULL THEN ccu.given_name || ' ' || ccu.family_name " + 
//						  "                     ELSE nvl(nvl(ccu.given_name, ccu.family_name), 'Unknown') " + 
//						  "                END AS DESCRIPTION " + 
//						  "FROM CLAIM_CALCULATION cc " + 
//						  "JOIN CLAIM_CALCULATION_USER ccu ON ccu.claim_calculation_user_guid = cc.create_claim_calc_user_guid";
//
//		return createVirtualCodeTableConfig("CLAIM_CALC_CREATE_USER", fetchSql);
//	}
//
//	@Bean
//	public CodeTableConfig claimCalcUpdateUserCodeTableConfig() {
//		String fetchSql = "SELECT DISTINCT ccu.claim_calculation_user_guid AS CODE, " + 
//						  "                CASE WHEN ccu.given_name IS NOT NULL AND ccu.family_name IS NOT NULL THEN ccu.given_name || ' ' || ccu.family_name " + 
//						  "                     ELSE nvl(nvl(ccu.given_name, ccu.family_name), 'Unknown') " + 
//						  "                END AS DESCRIPTION " + 
//						  "FROM CLAIM_CALCULATION cc " + 
//						  "JOIN CLAIM_CALCULATION_USER ccu ON ccu.claim_calculation_user_guid = cc.update_claim_calc_user_guid";
//
//		return createVirtualCodeTableConfig("CLAIM_CALC_UPDATE_USER", fetchSql);
//	}
	
	@Bean
	public CodeTableConfig policyStatusCodeTableConfig() {
		return createCodeTableConfig("policy_status_code", "DESCRIPTION");
	}

	@Bean
	public CodeTableConfig plantInsurabilityTypeCodeTableConfig() {
		return createCodeTableConfig("plant_insurability_type_code", "DESCRIPTION");
	}
	
	@Bean
	public CodeTableConfig foragePlantInsurabilityTypeCodeTableConfig() {
		String fetchSql = "SELECT plant_insurability_type_code AS CODE, DESCRIPTION FROM plant_insurability_type_code WHERE insurance_plan_id = 5";

		return createVirtualCodeTableConfig("forage_plant_insurability_type_code", fetchSql, sortOrderAscending);
	}
	
	@Bean
	public CodeTableConfig berriesPlantInsurabilityTypeCodeTableConfig() {
		String fetchSql = "SELECT plant_insurability_type_code AS CODE, DESCRIPTION FROM plant_insurability_type_code WHERE insurance_plan_id = 3";

		return createVirtualCodeTableConfig("berries_plant_insurability_type_code", fetchSql, sortOrderAscending);
	}
	
	@Bean
	public CodeTableConfig landIdentifierTypeCodeTableConfig() {
		return createCodeTableConfig("land_identifier_type_code", "SORT_ORDER");
	}
	
	@Bean
	public CodeTableConfig primaryReferenceTypeCodeTableConfig() {
		return createCodeTableConfig("primary_reference_type_code", "SORT_ORDER");
	}
	
	@Bean
	public CodeTableConfig verifiedYieldAmendmentCodeTableConfig() {
		return createCodeTableConfig("verified_yield_amendment_code", "DESCRIPTION");
	}	
	
	@Bean
	public CodeTableConfig policyCropYearCodeTableConfig() {
		String fetchSql = "SELECT DISTINCT t.crop_year::text AS CODE, t.crop_year AS DESCRIPTION FROM policy t";

		return createVirtualCodeTableConfig("policy_crop_year", fetchSql, sortOrderDescending);
	}
	
	@Bean
	public CodeTableConfig insurancePlanTableConfig() {
		String fetchSql = "SELECT t.insurance_plan_id::text AS CODE, t.insurance_plan_name AS DESCRIPTION FROM insurance_plan t";

		return createVirtualCodeTableConfig("insurance_plan", fetchSql, sortOrderAscending);
	}	
	
	@Bean
	public CodeTableConfig officeTableConfig() {
		String fetchSql = "SELECT t.office_id::text AS CODE, t.office_name AS DESCRIPTION FROM office t ";

		return createVirtualCodeTableConfig("office", fetchSql, sortOrderAscending);
	}
	
	private final String sortOrderAscending = "ASC";
	private final String sortOrderDescending = "DESC";
	
	
	private CodeTableConfig createCodeTableConfig(String tableName, String sortColumn) {
		CodeTableConfig result = new CodeTableConfig();
		
		String fetchSql = String.format("SELECT T.%s CODE, T.DESCRIPTION, NULL DISPLAY_ORDER, T.EFFECTIVE_DATE, T.EXPIRY_DATE, T.CREATE_USER, T.CREATE_DATE, T.UPDATE_USER, T.UPDATE_DATE FROM %s T ORDER BY T." + sortColumn , tableName, tableName);
				
		result.setUseRevisionCount(Boolean.FALSE);
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableName(tableName);
		result.setFetchSql(fetchSql);
		
		return result;
	}
	
	//Creates a CodeTableConfig, but using a source query that returns CODE and DESCRIPTION that is wrapped into a 
	//format expected by the CodeTablesService, so that it can be accessed as though it were a code table.
	private CodeTableConfig createVirtualCodeTableConfig(String tableName, String innerSql, String sortOrder) {
		CodeTableConfig result = new CodeTableConfig();

		String fetchSql = String.format("SELECT V.CODE, " + 
				                        "       V.DESCRIPTION, " + 
				                        "       NULL DISPLAY_ORDER, " + 
				                        "       to_date('2020-01-01','yyyy-mm-dd') EFFECTIVE_DATE, " + 
				                        "       to_date('9999-12-31','yyyy-mm-dd') EXPIRY_DATE, " + 
				                        "       'DEFAULT_USERID' CREATE_USER, " + 
				                        "       now() CREATE_DATE, " + 
				                        "       'DEFAULT_USERID' UPDATE_USER, " + 
				                        "       now() UPDATE_DATE " + 
				                        "FROM (%1$s) V " + 
				                        "ORDER BY V.DESCRIPTION %2$s", 
				                        innerSql, sortOrder);
		
		result.setUseRevisionCount(Boolean.FALSE);
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableName(tableName);
		result.setFetchSql(fetchSql);
		
		return result;
	}

}
