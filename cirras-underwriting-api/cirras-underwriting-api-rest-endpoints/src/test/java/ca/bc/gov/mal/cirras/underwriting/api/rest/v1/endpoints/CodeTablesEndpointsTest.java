package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.test.EndpointsTest;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
//import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimService;
//import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.CirrasClaimServiceException;
//import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.security.Scopes;
//import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
//import ca.bc.gov.mal.cirras.claims.api.rest.test.EndpointsTest;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.wfone.common.model.Code;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;

public class CodeTablesEndpointsTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(CodeTablesEndpointsTest.class);

	private static final String[] SCOPES = {Scopes.GET_TOP_LEVEL, Scopes.GET_CODE_TABLES};

	
	@Test
	public void testCodeTables() throws CirrasUnderwritingServiceException, Oauth2ClientException {
		logger.debug("<testCodeTables");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}
		
		CirrasUnderwritingService service = getService(SCOPES);
		
		EndpointsRsrc topLevelEndpoints = service.getTopLevelEndpoints();

		String codeTableName = null;
		LocalDate effectiveAsOfDate = LocalDate.now();

		CodeTableListRsrc codeTables = service.getCodeTables(topLevelEndpoints, codeTableName, effectiveAsOfDate);

		Assert.assertNotNull(codeTables);
		List<CodeTableRsrc> codeTableList = codeTables.getCodeTableList();
		Assert.assertNotNull(codeTableList);
		//Assert.assertEquals(8, codeTableList.size());

		for (CodeTableRsrc codeTable : codeTableList) {
			codeTable = service.getCodeTable(codeTable, effectiveAsOfDate);
			Assert.assertNotNull(codeTable);
			
			List<CodeRsrc> codes = codeTable.getCodes();
			Assert.assertNotNull(codes);
			
			for(Code code:codes) {
				Assert.assertNotNull(code.getCode());
				Assert.assertNotNull(code.getDescription());
			}
		}

		logger.debug(">testCodeTables");
	}

	
}
