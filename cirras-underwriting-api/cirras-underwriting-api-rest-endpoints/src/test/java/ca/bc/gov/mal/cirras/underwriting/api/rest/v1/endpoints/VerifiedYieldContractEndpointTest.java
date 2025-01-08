package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class VerifiedYieldContractEndpointTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractEndpointTest.class);


	private static final String[] SCOPES = {
		Scopes.GET_TOP_LEVEL, 
		Scopes.SEARCH_UWCONTRACTS,
		Scopes.SEARCH_ANNUAL_FIELDS,
		Scopes.CREATE_INVENTORY_CONTRACT,
		Scopes.DELETE_INVENTORY_CONTRACT,
		Scopes.GET_INVENTORY_CONTRACT,
		Scopes.UPDATE_INVENTORY_CONTRACT,
		Scopes.CREATE_DOP_YIELD_CONTRACT,
		Scopes.GET_DOP_YIELD_CONTRACT,
		Scopes.UPDATE_DOP_YIELD_CONTRACT,
		Scopes.DELETE_DOP_YIELD_CONTRACT,
		Scopes.PRINT_DOP_YIELD_CONTRACT,
		Scopes.CREATE_SYNC_UNDERWRITING,
		Scopes.UPDATE_SYNC_UNDERWRITING,
		Scopes.DELETE_SYNC_UNDERWRITING,
		Scopes.GET_GROWER,
		Scopes.GET_POLICY,
		Scopes.GET_LAND,
		Scopes.GET_LEGAL_LAND,
		Scopes.DELETE_COMMENTS,
		Scopes.CREATE_VERIFIED_YIELD_CONTRACT,
		Scopes.GET_VERIFIED_YIELD_CONTRACT,
		Scopes.UPDATE_VERIFIED_YIELD_CONTRACT,
		Scopes.DELETE_VERIFIED_YIELD_CONTRACT
	};
			
	private CirrasUnderwritingService service;
	private EndpointsRsrc topLevelEndpoints;

	@Before
	public void prepareTests() throws CirrasUnderwritingServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();
	}

	@After 
	public void cleanUp() throws CirrasUnderwritingServiceException, NotFoundDaoException, DaoException {
		delete();
	}

	
	private void delete() throws CirrasUnderwritingServiceException {

	}
	
	@Test
	public void testGetVerifiedYieldContract() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetVerifiedYieldContract");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE YIELD BY RUNNING THE INSERT SCRIPTS BELOW.
		//*****************************************************************
		
		String policyNumber = "999999-23";   // Set to valid policy number.

		VerifiedYieldContractRsrc expectedVyc = new VerifiedYieldContractRsrc();
		
		expectedVyc.setContractId(-1); // Set to a valid contract id.
		expectedVyc.setCropYear(2023);
		expectedVyc.setDeclaredYieldContractGuid("dyc-guid");  // Set to a valid dop guid.
		expectedVyc.setDefaultYieldMeasUnitTypeCode("TONNE");
		expectedVyc.setGrowerContractYearId(-1);  // Set to a valid gcy id.
		expectedVyc.setInsurancePlanId(4);
		expectedVyc.setVerifiedYieldContractGuid("bd4c96bf89d94e46a7b81e89fa5eba56");
		
		Integer pageNumber = new Integer(1);
		Integer pageRowCount = new Integer(20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNotNull(referrer.getVerifiedYieldContractGuid());

		VerifiedYieldContractRsrc verifiedYldContract = service.getVerifiedYieldContract(referrer);
		Assert.assertNotNull(verifiedYldContract);

		checkVerifiedYieldContract(expectedVyc, verifiedYldContract);
		

		//*****************************************************************
		//AFTER TESTS: DELETE LAND, INVENTORY AND YIELD BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************


		logger.debug(">testGetVerifiedYieldContract");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************

		insert into verified_yield_contract(
			verified_yield_contract_guid, 
			contract_id, 
			crop_year, 
			declared_yield_contract_guid, 
			default_yield_meas_unit_type_code, 
			verified_yield_update_timestamp, 
			verified_yield_update_user, 
			create_user, 
			create_date, 
			update_user, 
			update_date
		) values (
			'bd4c96bf89d94e46a7b81e89fa5eba56', 
			-1,
			2023,
			'dyc-guid',
			'TONNE',
			now(),
			'admin',
			'admin',
			now(),
			'admin',
			now()
		)


	*****************
	DELETE STATEMENTS
	*****************
	delete from verified_yield_contract where verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';

	 
	*****************
	SELECT STATEMENTS
	*****************
	select * from verified_yield_contract vyc where vyc.verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
	
 
	 */
	}

	@Test
	public void testGetVerifiedYieldContractCommodities() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetVerifiedYieldContractCommodities");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE YIELD BY RUNNING THE INSERT SCRIPTS BELOW.
		//*****************************************************************

		String policyNumber = "301848-23";   // Set to valid policy number.
		
		List<VerifiedYieldContractCommodity> expectedVycc = new ArrayList<VerifiedYieldContractCommodity>();
		VerifiedYieldContractCommodity vycc = new VerifiedYieldContractCommodity();
		vycc.setCropCommodityId(16);
		vycc.setCropCommodityName("BARLEY");
		vycc.setHarvestedAcres(11.22);
		vycc.setHarvestedAcresOverride(33.44);
		vycc.setHarvestedYield(66.77);
		vycc.setHarvestedYieldOverride(88.99);
		vycc.setIsPedigreeInd(false);
		vycc.setProductionGuarantee(13.57);
		vycc.setSoldYieldDefaultUnit(99.88);
		vycc.setStoredYieldDefaultUnit(77.66);
		vycc.setTotalInsuredAcres(null);  // Set based on existing inventory for selected policy.
		vycc.setVerifiedYieldContractCommodityGuid("dfd5b5523b8740039b057cfdf5b58d32");
		vycc.setVerifiedYieldContractGuid("bd4c96bf89d94e46a7b81e89fa5eba56");
		vycc.setYieldPerAcre(55.44);

		expectedVycc.add(vycc);
		
		vycc = new VerifiedYieldContractCommodity();
		vycc.setCropCommodityId(18);
		vycc.setCropCommodityName("CANOLA");
		vycc.setHarvestedAcres(null);
		vycc.setHarvestedAcresOverride(null);
		vycc.setHarvestedYield(null);
		vycc.setHarvestedYieldOverride(null);
		vycc.setIsPedigreeInd(true);
		vycc.setProductionGuarantee(null);
		vycc.setSoldYieldDefaultUnit(null);
		vycc.setStoredYieldDefaultUnit(null);
		vycc.setTotalInsuredAcres(null);
		vycc.setVerifiedYieldContractCommodityGuid("03d7d25a22b74c9b8d5a40d938386453");
		vycc.setVerifiedYieldContractGuid("bd4c96bf89d94e46a7b81e89fa5eba56");
		vycc.setYieldPerAcre(null);
		
		expectedVycc.add(vycc);		
		
		List<VerifiedYieldAmendment> expectedVya = new ArrayList<VerifiedYieldAmendment>();
		VerifiedYieldAmendment vya = new VerifiedYieldAmendment();
		
		vya.setVerifiedYieldAmendmentGuid("abdcdgfgrsaged2025-1");
		vya.setVerifiedYieldContractGuid("bd4c96bf89d94e46a7b81e89fa5eba56");
		vya.setVerifiedYieldAmendmentCode("Appraisal");
		vya.setCropCommodityId(16);
		vya.setIsPedigreeInd(false);
		vya.setFieldId(7929);
		vya.setYieldPerAcre(15.5);
		vya.setAcres(50.0);
		vya.setRationale("test");
		vya.setCropCommodityName("BARLEY");
		vya.setFieldLabel("LOT A");
		vya.setDeletedByUserInd(null);

		expectedVya.add(vya);
		
		vya = new VerifiedYieldAmendment();
		
		vya.setVerifiedYieldAmendmentGuid("abdcdgfgrsaged2025-2");
		vya.setVerifiedYieldContractGuid("bd4c96bf89d94e46a7b81e89fa5eba56");
		vya.setVerifiedYieldAmendmentCode("Assessment");
		vya.setCropCommodityId(16);
		vya.setIsPedigreeInd(true);
		vya.setFieldId(null);
		vya.setYieldPerAcre(7.7);
		vya.setAcres(77.0);
		vya.setRationale("test");
		vya.setCropCommodityName("BARLEY");
		vya.setFieldLabel(null);
		vya.setDeletedByUserInd(null);

		expectedVya.add(vya);

		Integer pageNumber = (1);
		Integer pageRowCount = (20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		//Assert.assertNotNull(referrer.getInventoryContractGuid());
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNotNull(referrer.getVerifiedYieldContractGuid());

		VerifiedYieldContractRsrc verifiedYldContract = service.getVerifiedYieldContract(referrer);
		Assert.assertNotNull(verifiedYldContract);

		checkVerifiedYieldContractCommodities(expectedVycc, verifiedYldContract.getVerifiedYieldContractCommodities());
		
		checkVerifiedYieldAmendments(expectedVya, verifiedYldContract.getVerifiedYieldAmendments());
		
		//*****************************************************************
		//AFTER TESTS: DELETE LAND, INVENTORY AND YIELD BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************

		logger.debug(">testGetVerifiedYieldContractCommodities");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************

		-- Declared Yield Contract
		INSERT INTO cuws.declared_yield_contract(
		declared_yield_contract_guid, 
			contract_id, crop_year, 
			declaration_of_production_date, 
			dop_update_timestamp, 
			dop_update_user, 
			entered_yield_meas_unit_type_code, 
			default_yield_meas_unit_type_code, 
			grain_from_other_source_ind, 
			create_user, 
			create_date, 
			update_user, 
			update_date, 
			baler_wagon_info, 
			total_livestock)
		VALUES ('dyc-guid',
				3939,
				2023,
				now(),
				now(),
				'admin',
				'BUSHEL',
				'TONNE',
				'N',
				'admin',
				now(),
				'admin',
				now(),
				null,
				null);
		
		-- Verified Yield Contract
		insert into verified_yield_contract(
			verified_yield_contract_guid, 
			contract_id, 
			crop_year, 
			declared_yield_contract_guid, 
			default_yield_meas_unit_type_code, 
			verified_yield_update_timestamp, 
			verified_yield_update_user, 
			create_user, 
			create_date, 
			update_user, 
			update_date
		) values (
			'bd4c96bf89d94e46a7b81e89fa5eba56', 
			3939,
			2023,
			'dyc-guid',
			'TONNE',
			now(),
			'admin',
			'admin',
			now(),
			'admin',
			now()
		);


		-- Verified Yield Contract Commodity
		insert into verified_yield_contract_commodity(
			verified_yield_contract_commodity_guid, 
			verified_yield_contract_guid, 
			crop_commodity_id, 
			is_pedigree_ind, 
			harvested_acres, 
			harvested_acres_override, 
			stored_yield_default_unit, 
			sold_yield_default_unit, 
			production_guarantee, 
			harvested_yield, 
			harvested_yield_override, 
			yield_per_acre, 
			create_user, 
			create_date, 
			update_user, 
			update_date
		) values (
			'dfd5b5523b8740039b057cfdf5b58d32',
			'bd4c96bf89d94e46a7b81e89fa5eba56',
			16,
			'N',
			11.22,
			33.44,
			77.66,
			99.88,
			13.57,
			66.77,
			88.99,
			55.44,
			'admin',
			now(),
			'admin',
			now()
		);

		insert into verified_yield_contract_commodity(
			verified_yield_contract_commodity_guid, 
			verified_yield_contract_guid, 
			crop_commodity_id, 
			is_pedigree_ind, 
			harvested_acres, 
			harvested_acres_override, 
			stored_yield_default_unit, 
			sold_yield_default_unit, 
			production_guarantee, 
			harvested_yield, 
			harvested_yield_override, 
			yield_per_acre, 
			create_user, 
			create_date, 
			update_user, 
			update_date
		) values (
			'03d7d25a22b74c9b8d5a40d938386453',
			'bd4c96bf89d94e46a7b81e89fa5eba56',
			18,
			'Y',
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			'admin',
			now(),
			'admin',
			now()
		);

	--Verified Yield Amendment
	INSERT INTO cuws.verified_yield_amendment(verified_yield_amendment_guid, verified_yield_amendment_code, verified_yield_contract_guid, crop_commodity_id, field_id, yield_per_acre, acres, rationale, is_pedigree_ind, create_user, create_date, update_user, update_date)
		VALUES ('abdcdgfgrsaged2025-1', 'Appraisal', 'bd4c96bf89d94e46a7b81e89fa5eba56', 16, 7929, 15.5, 50, 'test', 'N', 'admin', now(), 'admin', now());
		
	INSERT INTO cuws.verified_yield_amendment(verified_yield_amendment_guid, verified_yield_amendment_code, verified_yield_contract_guid, crop_commodity_id, field_id, yield_per_acre, acres, rationale, is_pedigree_ind, create_user, create_date, update_user, update_date)
		VALUES ('abdcdgfgrsaged2025-2', 'Assessment', 'bd4c96bf89d94e46a7b81e89fa5eba56', 16, null, 7.7, 77, 'test', 'Y', 'admin', now(), 'admin', now());
	


	delete from verified_yield_amendment where verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
	delete from verified_yield_contract_commodity where verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
	delete from verified_yield_contract where verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
	delete from declared_yield_contract where declared_yield_contract_guid = 'dyc-guid';

	 
	*****************
	SELECT STATEMENTS
	*****************
	select * from declared_yield_contract where declared_yield_contract_guid = 'dyc-guid';
	select * from verified_yield_contract vyc where vyc.verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
	select * from verified_yield_contract_commodity vycc where vycc.verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';	
	select * from verified_yield_amendment where verified_yield_contract_guid = 'bd4c96bf89d94e46a7b81e89fa5eba56';
*/ 
	}
	
	@Test
	public void testGetVerifiedYieldSummaries() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testGetVerifiedYieldSummaries");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		//*****************************************************************
		//BEFORE TESTS: CREATE YIELD BY RUNNING THE INSERT SCRIPTS BELOW.
		//*****************************************************************

		String policyNumber = "301848-23";   // Set to valid policy number.
		
		List<VerifiedYieldSummary> expectedVys = new ArrayList<VerifiedYieldSummary>();
		VerifiedYieldSummary vys = new VerifiedYieldSummary();
		
		vys.setVerifiedYieldSummaryGuid("vys-guid-1");
		vys.setVerifiedYieldContractGuid("vyc-guid");
		vys.setCropCommodityId(16);
		vys.setCropCommodityName("BARLEY");
		vys.setIsPedigreeInd(true);
		vys.setHarvestedYield(100.5);
		vys.setHarvestedYieldPerAcre(11.5);
		vys.setAppraisedYield(1.3);
		vys.setAssessedYield(2.0);
		vys.setYieldToCount(111.5);
		vys.setYieldPercentPy(75.5);
		vys.setProductionGuarantee(55.5);
		vys.setProbableYield(17.5);
		vys.setInsurableValueHundredPercent(32.14);
		vys.setTotalInsuredAcres(null);  // Set based on existing inventory for selected policy.

		
		List<UnderwritingComment> expectedComments = new ArrayList<UnderwritingComment>();
		UnderwritingComment uw = new UnderwritingComment();
		uw.setUnderwritingCommentGuid("uw-guid-1");
		uw.setUnderwritingCommentTypeCode("VY");
		uw.setUnderwritingComment("comment 1");
		uw.setVerifiedYieldSummaryGuid(vys.getVerifiedYieldSummaryGuid());

		expectedComments.add(uw);
		vys.setUwComments(expectedComments);

		expectedVys.add(vys);

		vys = new VerifiedYieldSummary();
		vys.setVerifiedYieldSummaryGuid("vys-guid-2");
		vys.setVerifiedYieldContractGuid("vyc-guid");
		vys.setCropCommodityId(18);
		vys.setCropCommodityName("CANOLA");
		vys.setIsPedigreeInd(false);
		vys.setHarvestedYield(101.5);
		vys.setHarvestedYieldPerAcre(12.5);
		vys.setAppraisedYield(2.3);
		vys.setAssessedYield(3.0);
		vys.setYieldToCount(112.5);
		vys.setYieldPercentPy(76.5);
		vys.setProductionGuarantee(56.5);
		vys.setProbableYield(18.5);
		vys.setTotalInsuredAcres(null);  // Set based on existing inventory for selected policy.
		
		expectedVys.add(vys);	
		

		Integer pageNumber = (1);
		Integer pageRowCount = (20);

		UwContractListRsrc searchResults = service.getUwContractList(
				topLevelEndpoints, 
				null, 
				null, 
				null,
				null,
				policyNumber,
				null,
				null, 
				null, 
				null, 
				pageNumber, pageRowCount);

		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.getCollection().size());

		UwContractRsrc referrer = searchResults.getCollection().get(0);
		Assert.assertNotNull(referrer.getDeclaredYieldContractGuid());
		Assert.assertNotNull(referrer.getVerifiedYieldContractGuid());

		VerifiedYieldContractRsrc verifiedYldContract = service.getVerifiedYieldContract(referrer);
		Assert.assertNotNull(verifiedYldContract);

		checkVerifiedYieldSummaries(expectedVys, verifiedYldContract.getVerifiedYieldSummaries());
		
		//*****************************************************************
		//AFTER TESTS: DELETE LAND, INVENTORY AND YIELD BY RUNNING THE DELETE SCRIPTS BELOW
		//*****************************************************************

		logger.debug(">testGetVerifiedYieldSummaries");
	
	/* 
	 * 
	*****************
	INSERT STATEMENTS
	*****************
		-- Declared Yield Contract
		INSERT INTO cuws.declared_yield_contract(
		declared_yield_contract_guid, 
			contract_id, crop_year, 
			declaration_of_production_date, 
			dop_update_timestamp, 
			dop_update_user, 
			entered_yield_meas_unit_type_code, 
			default_yield_meas_unit_type_code, 
			grain_from_other_source_ind, 
			create_user, 
			create_date, 
			update_user, 
			update_date, 
			baler_wagon_info, 
			total_livestock)
		VALUES ('dyc-guid',
				3939,
				2023,
				now(),
				now(),
				'admin',
				'BUSHEL',
				'TONNE',
				'N',
				'admin',
				now(),
				'admin',
				now(),
				null,
				null);
		
		-- Verified Yield Contract
		insert into verified_yield_contract(
			verified_yield_contract_guid, 
			contract_id, 
			crop_year, 
			declared_yield_contract_guid, 
			default_yield_meas_unit_type_code, 
			verified_yield_update_timestamp, 
			verified_yield_update_user, 
			create_user, 
			create_date, 
			update_user, 
			update_date
		) values (
			'vyc-guid', 
			3939,
			2023,
			'dyc-guid',
			'TONNE',
			now(),
			'admin',
			'admin',
			now(),
			'admin',
			now()
		);
		
	--Verified Yield Summary
	INSERT INTO cuws.verified_yield_summary(
	verified_yield_summary_guid, verified_yield_contract_guid, crop_commodity_id, is_pedigree_ind, harvested_yield, harvested_yield_per_acre, appraised_yield, assessed_yield, yield_to_count, yield_percent_py, production_guarantee, probable_yield, create_user, create_date, update_user, update_date)
	VALUES ('vys-guid-1', 'vyc-guid', 16, 'Y', 100.5, 11.5, 1.3, 2.0, 111.5, 75.5, 55.5, 17.5, 'admin', now(), 'admin', now());
		
	INSERT INTO cuws.verified_yield_summary(
	verified_yield_summary_guid, verified_yield_contract_guid, crop_commodity_id, is_pedigree_ind, harvested_yield, harvested_yield_per_acre, appraised_yield, assessed_yield, yield_to_count, yield_percent_py, production_guarantee, probable_yield, create_user, create_date, update_user, update_date)
	VALUES ('vys-guid-2', 'vyc-guid', 18, 'N', 101.5, 12.5, 2.3, 3.0, 112.5, 76.5, 56.5, 18.5, 'admin', now(), 'admin', now());
		
	--Comment
	INSERT INTO cuws.underwriting_comment(underwriting_comment_guid, underwriting_comment_type_code, underwriting_comment, verified_yield_summary_guid, create_user, create_date, update_user, update_date)
	VALUES ('uw-guid-1', 'VY', 'comment 1', 'vys-guid-1', 'admin', now(), 'admin', now());

	delete from underwriting_comment where verified_yield_summary_guid = 'vys-guid-1';
	delete from verified_yield_summary where verified_yield_contract_guid = 'vyc-guid';
	delete from verified_yield_contract where verified_yield_contract_guid = 'vyc-guid';
	delete from declared_yield_contract where declared_yield_contract_guid = 'dyc-guid';

	 
	*****************
	SELECT STATEMENTS
	*****************
	select * from declared_yield_contract where declared_yield_contract_guid = 'dyc-guid';
	select * from verified_yield_contract vyc where vyc.verified_yield_contract_guid = 'vyc-guid';
	select * from verified_yield_summary vycc where vycc.verified_yield_contract_guid = 'vyc-guid';	
	select * from underwriting_comment where verified_yield_summary_guid = 'vys-guid-1';


*/ 
	}	
	
	
	
	private void checkVerifiedYieldContract(VerifiedYieldContractRsrc expected, VerifiedYieldContractRsrc actual) {
		Assert.assertEquals(expected.getContractId(), actual.getContractId());
		Assert.assertEquals(expected.getCropYear(), actual.getCropYear());
		Assert.assertEquals(expected.getDeclaredYieldContractGuid(), actual.getDeclaredYieldContractGuid());
		Assert.assertEquals(expected.getDefaultYieldMeasUnitTypeCode(), actual.getDefaultYieldMeasUnitTypeCode());
		Assert.assertEquals(expected.getGrowerContractYearId(), actual.getGrowerContractYearId());
		Assert.assertEquals(expected.getInsurancePlanId(), actual.getInsurancePlanId());
		Assert.assertEquals(expected.getVerifiedYieldContractGuid(), actual.getVerifiedYieldContractGuid());
		Assert.assertNotNull(actual.getVerifiedYieldUpdateUser());
		Assert.assertNotNull(actual.getVerifiedYieldUpdateTimestamp());
	}
	
	private void checkVerifiedYieldSummaries(List<VerifiedYieldSummary> expected, List<VerifiedYieldSummary> actual) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());
			for (VerifiedYieldSummary expectedVys : expected) {
				VerifiedYieldSummary actualVys = getVerifiedYieldSummary(expectedVys.getCropCommodityId(), expectedVys.getIsPedigreeInd(), actual);
				Assert.assertNotNull(actualVys);
				checkVerifiedYieldSummary(expectedVys, actualVys);
			}
		}
		
	}
	
	private VerifiedYieldSummary getVerifiedYieldSummary(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldSummary> vysList) {
		
		VerifiedYieldSummary vys = null;
		
		List<VerifiedYieldSummary> vysFiltered = vysList.stream()
				.filter(x -> x.getCropCommodityId().equals(cropCommodityId) && x.getIsPedigreeInd().equals(isPedigree) )
				.collect(Collectors.toList());
		
		if (vysFiltered != null && vysFiltered.size() > 0) {
			vys = vysFiltered.get(0);
		}
		return vys;
	}

	private void checkVerifiedYieldSummary(VerifiedYieldSummary expected, VerifiedYieldSummary actual) {
		Assert.assertEquals(expected.getVerifiedYieldSummaryGuid(), actual.getVerifiedYieldSummaryGuid());
		Assert.assertEquals(expected.getVerifiedYieldContractGuid(), actual.getVerifiedYieldContractGuid());
		Assert.assertEquals(expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals(expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals(expected.getHarvestedYield(), actual.getHarvestedYield());
		Assert.assertEquals(expected.getHarvestedYieldPerAcre(), actual.getHarvestedYieldPerAcre());
		Assert.assertEquals(expected.getAppraisedYield(), actual.getAppraisedYield());
		Assert.assertEquals(expected.getAssessedYield(), actual.getAssessedYield());
		Assert.assertEquals(expected.getYieldToCount(), actual.getYieldToCount());
		Assert.assertEquals(expected.getYieldPercentPy(), actual.getYieldPercentPy());
		Assert.assertEquals(expected.getProductionGuarantee(), actual.getProductionGuarantee());
		Assert.assertEquals(expected.getProbableYield(), actual.getProbableYield());
		Assert.assertEquals(expected.getInsurableValueHundredPercent(), actual.getInsurableValueHundredPercent());
		Assert.assertEquals(expected.getTotalInsuredAcres(), actual.getTotalInsuredAcres());

		
		if (expected.getUwComments() != null && expected.getUwComments().size() > 0) {
			Assert.assertNotNull(actual.getUwComments());
			Assert.assertEquals(1, actual.getUwComments().size());
			UnderwritingComment expectedComment = expected.getUwComments().get(0);
			UnderwritingComment actualComment = actual.getUwComments().get(0);
			
			Assert.assertEquals(expectedComment.getUnderwritingCommentGuid(), actualComment.getUnderwritingCommentGuid());
			Assert.assertEquals(expectedComment.getUnderwritingCommentTypeCode(), actualComment.getUnderwritingCommentTypeCode());
			Assert.assertEquals(expectedComment.getUnderwritingComment(), actualComment.getUnderwritingComment());
			Assert.assertEquals(expectedComment.getVerifiedYieldSummaryGuid(), actualComment.getVerifiedYieldSummaryGuid());
		}
		
	}


	private void checkVerifiedYieldAmendments(List<VerifiedYieldAmendment> expected, List<VerifiedYieldAmendment> actual) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());
			for (VerifiedYieldAmendment expectedVya : expected) {
				VerifiedYieldAmendment actualVya = getVerifiedYieldAmendment(expectedVya.getCropCommodityId(), expectedVya.getIsPedigreeInd(), actual);
				checkVerifiedYieldAmendment(expectedVya, actualVya);
			}
		}
		
	}
	
	private VerifiedYieldAmendment getVerifiedYieldAmendment(Integer cropCommodityId, Boolean isPedigree, List<VerifiedYieldAmendment> vyaList) {
		
		VerifiedYieldAmendment vya = null;
		
		List<VerifiedYieldAmendment> vyaFiltered = vyaList.stream()
				.filter(x -> x.getCropCommodityId() == cropCommodityId && x.getIsPedigreeInd() == isPedigree )
				.collect(Collectors.toList());
		
		if (vyaFiltered != null) {
			vya = vyaFiltered.get(0);
		}
		return vya;
	}
	
	private void checkVerifiedYieldAmendment(VerifiedYieldAmendment expected, VerifiedYieldAmendment actual) {
		Assert.assertEquals("VerifiedYieldAmendmentGuid", expected.getVerifiedYieldAmendmentGuid(), actual.getVerifiedYieldAmendmentGuid());
		Assert.assertEquals("VerifiedYieldContractGuid", expected.getVerifiedYieldContractGuid(), actual.getVerifiedYieldContractGuid());
		Assert.assertEquals("VerifiedYieldAmendmentCode", expected.getVerifiedYieldAmendmentCode(), actual.getVerifiedYieldAmendmentCode());
		Assert.assertEquals("CropCommodityId", expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals("IsPedigreeInd", expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals("FieldId", expected.getFieldId(), actual.getFieldId());
		Assert.assertEquals("YieldPerAcre", expected.getYieldPerAcre(), actual.getYieldPerAcre());
		Assert.assertEquals("Acres", expected.getAcres(), actual.getAcres());
		Assert.assertEquals("Rationale", expected.getRationale(), actual.getRationale());
		Assert.assertEquals("CropCommodityName", expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals("FieldLabel", expected.getFieldLabel(), actual.getFieldLabel());
		Assert.assertEquals("DeletedByUserInd", expected.getDeletedByUserInd(), actual.getDeletedByUserInd());
	}
	
	private void checkVerifiedYieldContractCommodities(List<VerifiedYieldContractCommodity> expected, List<VerifiedYieldContractCommodity> actual) {
		if ( expected == null && actual != null || expected != null && actual == null ) {
			Assert.fail();
		} else if ( expected != null && actual != null ) {
			Assert.assertEquals(expected.size(), actual.size());
			for ( int i = 0; i < expected.size(); i++ ) {
				checkVerifiedYieldContractCommodity(expected.get(i), actual.get(i));
			}
		}
	}
	
	private void checkVerifiedYieldContractCommodity(VerifiedYieldContractCommodity expected, VerifiedYieldContractCommodity actual) {

		Assert.assertEquals(expected.getCropCommodityId(), actual.getCropCommodityId());
		Assert.assertEquals(expected.getCropCommodityName(), actual.getCropCommodityName());
		Assert.assertEquals(expected.getHarvestedAcres(), actual.getHarvestedAcres());
		Assert.assertEquals(expected.getHarvestedAcresOverride(), actual.getHarvestedAcresOverride());
		Assert.assertEquals(expected.getHarvestedYield(), actual.getHarvestedYield());
		Assert.assertEquals(expected.getHarvestedYieldOverride(), actual.getHarvestedYieldOverride());
		Assert.assertEquals(expected.getIsPedigreeInd(), actual.getIsPedigreeInd());
		Assert.assertEquals(expected.getProductionGuarantee(), actual.getProductionGuarantee());
		Assert.assertEquals(expected.getSoldYieldDefaultUnit(), actual.getSoldYieldDefaultUnit());
		Assert.assertEquals(expected.getStoredYieldDefaultUnit(), actual.getStoredYieldDefaultUnit());
		Assert.assertEquals(expected.getTotalInsuredAcres(), actual.getTotalInsuredAcres());
		Assert.assertEquals(expected.getVerifiedYieldContractCommodityGuid(), actual.getVerifiedYieldContractCommodityGuid());
		Assert.assertEquals(expected.getVerifiedYieldContractGuid(), actual.getVerifiedYieldContractGuid());
		Assert.assertEquals(expected.getYieldPerAcre(), actual.getYieldPerAcre());
	}
	
}
