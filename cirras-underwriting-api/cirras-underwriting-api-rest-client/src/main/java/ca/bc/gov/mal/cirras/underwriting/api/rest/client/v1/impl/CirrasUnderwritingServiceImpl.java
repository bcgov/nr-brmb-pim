package ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropCommodityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CropVarietyInsurabilityListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GradeModifierTypeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AddFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactEmailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactPhoneRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContactRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RemoveFieldValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RenameLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ReplaceLegalValidationRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.RiskAreaListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SeedingDeadlineListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UwContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitConversionListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.YieldMeasUnitTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityTypeVarietyXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UnderwritingYearRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.UserSettingRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.ValidationException;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.common.wfone.rest.resource.transformers.NullTransformer;
import ca.bc.gov.nrs.wfone.common.rest.client.BadRequestException;
import ca.bc.gov.nrs.wfone.common.rest.client.BaseRestServiceClient;
import ca.bc.gov.nrs.wfone.common.rest.client.GenericRestDAO;
import ca.bc.gov.nrs.wfone.common.rest.client.Response;
import ca.bc.gov.nrs.wfone.common.rest.client.RestDAOException;

public class CirrasUnderwritingServiceImpl extends BaseRestServiceClient implements CirrasUnderwritingService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUnderwritingServiceImpl.class);
	
	public static final String CLIENT_VERSION = "1";

	private static final String Scopes = "WEBADE-REST.*";
	
	/**
	 * Constructor used for making OAuth2 Client Credentials requests
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 * @param webadeOauth2TokenUrl
	 */
	public CirrasUnderwritingServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret, String webadeOauth2TokenUrl, String scopes) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, scopes != null ? scopes : Scopes);
		logger.debug("<CirrasUnderwritingServiceImpl");
		
		logger.debug(">CirrasUnderwritingServiceImpl");
	}

	/**
	 * Constructor used for making requests with basic credentials
	 *
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 */
	public CirrasUnderwritingServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret);
		logger.debug("<CirrasUnderwritingServiceImpl");
		
		logger.debug(">CirrasUnderwritingServiceImpl");
	}

	/**
	 * Constructor used for making requests with basic credentials
	 *
	 * @param headerValue
	 */
	public CirrasUnderwritingServiceImpl(String headerValue) {
		super(headerValue);
		logger.debug("<CirrasUnderwritingServiceImpl");
		
		logger.debug(">CirrasUnderwritingServiceImpl");
	}
	
	/**
	 * Constructor used for making requests using the authorization header of the current HttpServletRequest
	 * 
	 */
	public CirrasUnderwritingServiceImpl() {
		super();
		logger.debug("<CirrasUnderwritingServiceImpl");
		
		logger.debug(">CirrasUnderwritingServiceImpl");
	}

	@Override
	public String getClientVersion() {
		return CLIENT_VERSION;
	}
	
	
	@Override
	public EndpointsRsrc getTopLevelEndpoints() throws CirrasUnderwritingServiceException {
		EndpointsRsrc result = null;

		try {
			String topLevelRestURL = getTopLevelRestURL();
			
			GenericRestDAO<EndpointsRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(EndpointsRsrc.class);
			Response<EndpointsRsrc> response = dao.Process(
				ResourceTypes.ENDPOINTS, this.getTransformer(), new BaseResource() {

					private static final long serialVersionUID = 1L;

					@Override
					public List<RelLink> getLinks() {
						List<RelLink> links = new ArrayList<RelLink>();
						links.add(new RelLink(ResourceTypes.ENDPOINTS, topLevelRestURL, "GET"));
						return links;
					}
				}, getWebClient());

			if (404 == response.getResponseCode()) {
				throw new AuthorizationServiceException("Failed to find toplevel at '" + getTopLevelRestURL() + "'");
			}

			result = response.getResource();

		} catch (RestDAOException e) {
			e.printStackTrace();
			throw new CirrasUnderwritingServiceException(e);
			
		} catch (Throwable t) {
			t.printStackTrace();
			throw new CirrasUnderwritingServiceException(t);
		}

		return result;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// code tables
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public CodeTableListRsrc getCodeTables(
			EndpointsRsrc parent,
			String codeTableName, 
			LocalDate effectiveAsOfDate) throws CirrasUnderwritingServiceException {
		logger.debug("<getCodeTables");

		CodeTableListRsrc result = null;

		try {

			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "codeTableName", codeTableName);
			putQueryParam(queryParams, "effectiveAsOfDate", toQueryParam(effectiveAsOfDate));

			GenericRestDAO<CodeTableListRsrc> dao = this.getRestDAOFactory()
					.getGenericRestDAO(CodeTableListRsrc.class);
			Response<CodeTableListRsrc> response = dao.Process(
					ResourceTypes.CODE_TABLE_LIST, this.getTransformer(), parent, queryParams,
					getWebClient());

			result = response.getResource();

		} catch (RestDAOException e) {
			logger.error(e.getMessage(), e);
			throw new CirrasUnderwritingServiceException(e);
		}

		logger.debug(">getCodeTables");
		return result;
	}

	@Override
	public CodeTableRsrc getCodeTable(CodeTableRsrc codeTable, LocalDate effectiveAsOfDate) throws CirrasUnderwritingServiceException {
		logger.debug("<getCodeTable");

		CodeTableRsrc result = null;

		try {

			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "effectiveAsOfDate", toQueryParam(effectiveAsOfDate));

			GenericRestDAO<CodeTableRsrc> dao = this.getRestDAOFactory()
					.getGenericRestDAO(CodeTableRsrc.class);
			Response<CodeTableRsrc> response = dao.Process(
					ResourceTypes.SELF, this.getTransformer(), codeTable, queryParams, getWebClient());

			result = response.getResource();

		} catch (RestDAOException e) {
			logger.error(e.getMessage(), e);
			throw new CirrasUnderwritingServiceException(e);
		}

		logger.debug(">getCodeTable");
		return result;
	}

	@Override
	public CodeTableRsrc updateCodeTable(CodeTableRsrc codeTable)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<CodeTableRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CodeTableRsrc.class);
		
		try {
			Response<CodeTableRsrc> response = dao.Process(ResourceTypes.UPDATE_CODE_TABLE, this.getTransformer(), codeTable, getWebClient());
			return response.getResource();
			
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			logger.error(rde.getMessage(), rde);
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// uw contracts
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public UwContractListRsrc getUwContractList(
			EndpointsRsrc parent,
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String datasetType,
			String sortColumn,
		    String sortDirection,
			Integer pageNumber, 
			Integer pageRowCount) throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<UwContractListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UwContractListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "officeId",  officeId);
			putQueryParam(queryParams, "policyStatusCode",  policyStatusCode);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "growerInfo",  growerInfo);
			putQueryParam(queryParams, "datasetType",  datasetType);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "sortDirection",  sortDirection);
			putQueryParam(queryParams, "pageNumber", toQueryParam(pageNumber));
			putQueryParam(queryParams, "pageRowCount", toQueryParam(pageRowCount));
			
			Response<UwContractListRsrc> response = dao.Process(ResourceTypes.UWCONTRACT_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	@Override
	public UwContractRsrc getUwContract(UwContractRsrc resource, String loadLinkedPolicies, String loadOtherYearPolicies, String screenType) throws CirrasUnderwritingServiceException {

		GenericRestDAO<UwContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UwContractRsrc.class);
		
		try {
			
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "loadLinkedPolicies",  loadLinkedPolicies);
			putQueryParam(queryParams, "loadOtherYearPolicies",  loadOtherYearPolicies);
			putQueryParam(queryParams, "screenType",  screenType);
			
			Response<UwContractRsrc> response = dao.Process(ResourceTypes.SELF, this.getTransformer(), resource, queryParams, getWebClient());

			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// annual fields
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public AnnualFieldListRsrc getAnnualFieldList(
			EndpointsRsrc parent,
			String legalLandId,
			String fieldId,
			String cropYear
	) throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<AnnualFieldListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AnnualFieldListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "legalLandId",  legalLandId);
			putQueryParam(queryParams, "fieldId",  fieldId);
			putQueryParam(queryParams, "cropYear",  cropYear);
			
			Response<AnnualFieldListRsrc> response = dao.Process(ResourceTypes.ANNUAL_FIELD_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// inventory contracts
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public InventoryContractRsrc getInventoryContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<InventoryContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractRsrc.class);
		
		try {
			Response<InventoryContractRsrc> response = dao.Process(ResourceTypes.INVENTORY_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public AnnualFieldRsrc rolloverAnnualFieldInventory(AnnualFieldRsrc resource, String rolloverToCropYear, String insurancePlanId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<AnnualFieldRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AnnualFieldRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "rolloverToCropYear", rolloverToCropYear);
			putQueryParam(queryParams, "insurancePlanId", insurancePlanId);
			
			Response<AnnualFieldRsrc> response = dao.Process(ResourceTypes.ROLLOVER_ANNUAL_FIELD_INVENTORY, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public InventoryContractRsrc rolloverInventoryContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<InventoryContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractRsrc.class);
		
		try {
			Response<InventoryContractRsrc> response = dao.Process(ResourceTypes.ROLLOVER_INVENTORY_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public InventoryContractRsrc createInventoryContract(EndpointsRsrc parent, InventoryContractRsrc resource)
	throws CirrasUnderwritingServiceException, ValidationException {
			
		GenericRestDAO<InventoryContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractRsrc.class);
		
		try {
			Response<InventoryContractRsrc> response = dao.Process(ResourceTypes.CREATE_INVENTORY_CONTRACT, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public InventoryContractListRsrc getInventoryContractList(
			EndpointsRsrc parent,
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids
		)	
		throws CirrasUnderwritingServiceException {

		GenericRestDAO<InventoryContractListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "officeId",  officeId);
			putQueryParam(queryParams, "policyStatusCode",  policyStatusCode);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "growerInfo",  growerInfo);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "inventoryContractGuids",  inventoryContractGuids);
			
			Response<InventoryContractListRsrc> response = dao.Process(ResourceTypes.INVENTORY_CONTRACT_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
		
	@Override
	public byte[] generateInventoryReport(
			EndpointsRsrc parent, 
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds,
			String reportType) throws CirrasUnderwritingServiceException {

		GenericRestDAO<byte[]> dao = this.getRestDAOFactory().getGenericRestDAO(byte[].class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "officeId",  officeId);
			putQueryParam(queryParams, "policyStatusCode",  policyStatusCode);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "growerInfo",  growerInfo);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "policyIds",  policyIds);
			putQueryParam(queryParams, "reportType",  reportType);
			
			Response<byte[]> response = dao.Process(ResourceTypes.BYTES, new NullTransformer("application/octet-stream", "bytes"), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	@Override
	public InventoryContractRsrc updateInventoryContract(String inventoryContractGuid, InventoryContractRsrc resource)
	throws CirrasUnderwritingServiceException, ValidationException {
	
		GenericRestDAO<InventoryContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractRsrc.class);

		Map<String, String> queryParams = new HashMap<String, String>();
		putQueryParam(queryParams, "inventoryContractGuid", inventoryContractGuid);
		
		try {
			Response<InventoryContractRsrc> response = dao.Process(ResourceTypes.UPDATE_INVENTORY_CONTRACT, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public void deleteInventoryContract(InventoryContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<InventoryContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(InventoryContractRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_INVENTORY_CONTRACT, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public AddFieldValidationRsrc validateAddField(UwContractRsrc resource, String fieldId, String transferFromPolicyId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<AddFieldValidationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AddFieldValidationRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "fieldId", fieldId);
			putQueryParam(queryParams, "transferFromPolicyId", transferFromPolicyId);
			
			Response<AddFieldValidationRsrc> response = dao.Process(ResourceTypes.ADD_FIELD_VALIDATION, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}	

	@Override
	public RemoveFieldValidationRsrc validateRemoveField(UwContractRsrc resource, String fieldId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<RemoveFieldValidationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(RemoveFieldValidationRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "fieldId", fieldId);
			
			Response<RemoveFieldValidationRsrc> response = dao.Process(ResourceTypes.REMOVE_FIELD_VALIDATION, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public RenameLegalValidationRsrc validateRenameLegal(UwContractRsrc resource, String annualFieldDetailId, String newLegalLocation) throws CirrasUnderwritingServiceException {

		GenericRestDAO<RenameLegalValidationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(RenameLegalValidationRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "annualFieldDetailId", annualFieldDetailId);
			putQueryParam(queryParams, "newLegalLocation", newLegalLocation);
			
			Response<RenameLegalValidationRsrc> response = dao.Process(ResourceTypes.RENAME_LEGAL_VALIDATION, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}	

	@Override
	public ReplaceLegalValidationRsrc validateReplaceLegal(UwContractRsrc resource, String annualFieldDetailId, String fieldLabel, String legalLandId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<ReplaceLegalValidationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ReplaceLegalValidationRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "annualFieldDetailId", annualFieldDetailId);
			putQueryParam(queryParams, "fieldLabel", fieldLabel);
			putQueryParam(queryParams, "legalLandId", legalLandId);
			
			Response<ReplaceLegalValidationRsrc> response = dao.Process(ResourceTypes.REPLACE_LEGAL_VALIDATION, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}	
	
	//////////////////////////////////////////////////////
	// DOP Yield Contract
	//////////////////////////////////////////////////////	
	@Override
	public DopYieldContractRsrc rolloverDopYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<DopYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(DopYieldContractRsrc.class);
		
		try {
			Response<DopYieldContractRsrc> response = dao.Process(ResourceTypes.ROLLOVER_DOP_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public DopYieldContractRsrc getDopYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<DopYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(DopYieldContractRsrc.class);
		
		try {
			Response<DopYieldContractRsrc> response = dao.Process(ResourceTypes.DOP_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public byte[] generateDopReport(
			EndpointsRsrc parent, 
			String cropYear,
			String insurancePlanId, 
			String officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds) throws CirrasUnderwritingServiceException {

		GenericRestDAO<byte[]> dao = this.getRestDAOFactory().getGenericRestDAO(byte[].class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "officeId",  officeId);
			putQueryParam(queryParams, "policyStatusCode",  policyStatusCode);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "growerInfo",  growerInfo);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "policyIds",  policyIds);
			
			Response<byte[]> response = dao.Process(ResourceTypes.BYTES, new NullTransformer("application/octet-stream", "bytes"), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	@Override
	public DopYieldContractRsrc createDopYieldContract(EndpointsRsrc parent, DopYieldContractRsrc resource)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<DopYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(DopYieldContractRsrc.class);
		
		try {
			Response<DopYieldContractRsrc> response = dao.Process(ResourceTypes.CREATE_DOP_YIELD_CONTRACT, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public DopYieldContractRsrc updateDopYieldContract(DopYieldContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<DopYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(DopYieldContractRsrc.class);
		
		try {
			Response<DopYieldContractRsrc> response = dao.Process(ResourceTypes.UPDATE_DOP_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void deleteDopYieldContract(DopYieldContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<DopYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(DopYieldContractRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_DOP_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	//////////////////////////////////////////////////////
	// Verified Yield Contract
	//////////////////////////////////////////////////////
	@Override
	public VerifiedYieldContractRsrc rolloverVerifiedYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<VerifiedYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractRsrc.class);
		
		try {
			Response<VerifiedYieldContractRsrc> response = dao.Process(ResourceTypes.ROLLOVER_VERIFIED_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public VerifiedYieldContractRsrc getVerifiedYieldContract(UwContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<VerifiedYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractRsrc.class);
		
		try {
			Response<VerifiedYieldContractRsrc> response = dao.Process(ResourceTypes.VERIFIED_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public VerifiedYieldContractRsrc createVerifiedYieldContract(EndpointsRsrc parent, VerifiedYieldContractRsrc resource)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<VerifiedYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractRsrc.class);
		
		try {
			Response<VerifiedYieldContractRsrc> response = dao.Process(ResourceTypes.CREATE_VERIFIED_YIELD_CONTRACT, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public VerifiedYieldContractRsrc updateVerifiedYieldContract(VerifiedYieldContractRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<VerifiedYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractRsrc.class);
		
		try {
			Response<VerifiedYieldContractRsrc> response = dao.Process(ResourceTypes.UPDATE_VERIFIED_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void deleteVerifiedYieldContract(VerifiedYieldContractRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<VerifiedYieldContractRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_VERIFIED_YIELD_CONTRACT, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}
	
	//////////////////////////////////////////////////////
	// Verified Yield Contract Simple
	//////////////////////////////////////////////////////

	
			
	@Override
	public VerifiedYieldContractSimpleRsrc getVerifiedYieldContractSimple(
			EndpointsRsrc parent,
			String contractId, 
			String cropYear, 
			String commodityId,
			String isPedigreeInd, 
			String loadVerifiedYieldContractCommodities,
			String loadVerifiedYieldAmendments, 
			String loadVerifiedYieldSummaries, 
			String loadVerifiedYieldGrainBasket)
	throws CirrasUnderwritingServiceException {

		GenericRestDAO<VerifiedYieldContractSimpleRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(VerifiedYieldContractSimpleRsrc.class);

		try {
			
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "contractId", contractId);
			putQueryParam(queryParams, "cropYear", cropYear);
			putQueryParam(queryParams, "commodityId", commodityId);
			putQueryParam(queryParams, "isPedigreeInd", isPedigreeInd);
			putQueryParam(queryParams, "loadVerifiedYieldContractCommodities", loadVerifiedYieldContractCommodities);
			putQueryParam(queryParams, "loadVerifiedYieldAmendments", loadVerifiedYieldAmendments);
			putQueryParam(queryParams, "loadVerifiedYieldSummaries", loadVerifiedYieldSummaries);
			putQueryParam(queryParams, "loadVerifiedYieldGrainBasket", loadVerifiedYieldGrainBasket);
			
			Response<VerifiedYieldContractSimpleRsrc> response = dao.Process(ResourceTypes.VERIFIED_YIELD_CONTRACT_SIMPLE, this.getTransformer(), parent, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	//////////////////////////////////////////////////////
	// Yield Measurement Unit Type Code
	//////////////////////////////////////////////////////	
	@Override
	public YieldMeasUnitTypeCodeListRsrc getYieldMeasUnitTypeCodeList(
			EndpointsRsrc parent,
			String insurancePlanId)
			throws CirrasUnderwritingServiceException {

		GenericRestDAO<YieldMeasUnitTypeCodeListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(YieldMeasUnitTypeCodeListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			
			Response<YieldMeasUnitTypeCodeListRsrc> response = dao.Process(ResourceTypes.YIELD_MEAS_UNIT_TYPE_CODE_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	//////////////////////////////////////////////////////
	// Grade Modifier
	//////////////////////////////////////////////////////	
	@Override
	public GradeModifierListRsrc getGradeModifierList(EndpointsRsrc parent, String cropYear, String insurancePlanId, String cropCommodityId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<GradeModifierListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GradeModifierListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "cropCommodityId",  cropCommodityId);
			
			Response<GradeModifierListRsrc> response = dao.Process(ResourceTypes.GRADE_MODIFIER_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	@Override
	public GradeModifierListRsrc saveGradeModifiers(GradeModifierListRsrc resource, String cropYear, String insurancePlanId, String cropCommodityId)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<GradeModifierListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GradeModifierListRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "cropCommodityId",  cropCommodityId);
			
			Response<GradeModifierListRsrc> response = dao.Process(ResourceTypes.SAVE_GRADE_MODIFIER_LIST, this.getTransformer(), resource, queryParams, getWebClient());

			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	

	//////////////////////////////////////////////////////
	// Grade Modifier Type
	//////////////////////////////////////////////////////	
	@Override
	public GradeModifierTypeListRsrc getGradeModifierTypeList(EndpointsRsrc parent, String cropYear)
			throws CirrasUnderwritingServiceException {
		GenericRestDAO<GradeModifierTypeListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GradeModifierTypeListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			
			Response<GradeModifierTypeListRsrc> response = dao.Process(ResourceTypes.GRADE_MODIFIER_TYPE_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	@Override
	public GradeModifierTypeListRsrc saveGradeModifierTypes(GradeModifierTypeListRsrc resource, String cropYear)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GenericRestDAO<GradeModifierTypeListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GradeModifierTypeListRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			
			Response<GradeModifierTypeListRsrc> response = dao.Process(ResourceTypes.SAVE_GRADE_MODIFIER_TYPE_LIST, this.getTransformer(), resource, queryParams, getWebClient());

			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	//////////////////////////////////////////////////////
	// Crop Variety Insurability
	//////////////////////////////////////////////////////	
	@Override
	public CropVarietyInsurabilityListRsrc getCropVarietyInsurabilities(EndpointsRsrc parent, String insurancePlanId, String loadForEdit)
			throws CirrasUnderwritingServiceException {
		GenericRestDAO<CropVarietyInsurabilityListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CropVarietyInsurabilityListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "loadForEdit",  loadForEdit);
			
			Response<CropVarietyInsurabilityListRsrc> response = dao.Process(ResourceTypes.CROP_VARIETY_INSURABILITY_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	@Override
	public CropVarietyInsurabilityListRsrc saveCropVarietyInsurabilities(CropVarietyInsurabilityListRsrc resource, String insurancePlanId, String loadForEdit)
			throws CirrasUnderwritingServiceException, ValidationException {
		
		GenericRestDAO<CropVarietyInsurabilityListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CropVarietyInsurabilityListRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "loadForEdit",  loadForEdit);
			
			Response<CropVarietyInsurabilityListRsrc> response = dao.Process(ResourceTypes.SAVE_CROP_VARIETY_INSURABILITY_LIST, this.getTransformer(), resource, queryParams, getWebClient());

			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	//////////////////////////////////////////////////////
	// Yield Measurement Unit Conversion
	//////////////////////////////////////////////////////	
	@Override
	public YieldMeasUnitConversionListRsrc getYieldMeasUnitConversions(
			EndpointsRsrc parent, 
			String insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode)
			throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<YieldMeasUnitConversionListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(YieldMeasUnitConversionListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "srcYieldMeasUnitTypeCode",  srcYieldMeasUnitTypeCode);
			putQueryParam(queryParams, "targetYieldMeasUnitTypeCode",  targetYieldMeasUnitTypeCode);
			
			Response<YieldMeasUnitConversionListRsrc> response = dao.Process(ResourceTypes.YIELD_MEAS_UNIT_CONVERSION_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	@Override
	public YieldMeasUnitConversionListRsrc saveYieldMeasUnitConversions(
			YieldMeasUnitConversionListRsrc resource, 
			String insurancePlanId, 
			String srcYieldMeasUnitTypeCode, 
			String targetYieldMeasUnitTypeCode
		)			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<YieldMeasUnitConversionListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(YieldMeasUnitConversionListRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "srcYieldMeasUnitTypeCode",  srcYieldMeasUnitTypeCode);
			putQueryParam(queryParams, "targetYieldMeasUnitTypeCode",  targetYieldMeasUnitTypeCode);
			
			Response<YieldMeasUnitConversionListRsrc> response = dao.Process(ResourceTypes.SAVE_YIELD_MEAS_UNIT_CONVERSION_LIST, this.getTransformer(), resource, queryParams, getWebClient());

			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	//////////////////////////////////////////////////////
	// Seeding Deadline
	//////////////////////////////////////////////////////	
	@Override
	public SeedingDeadlineListRsrc getSeedingDeadlines(EndpointsRsrc parent, String cropYear)
			throws CirrasUnderwritingServiceException {
		GenericRestDAO<SeedingDeadlineListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SeedingDeadlineListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			
			Response<SeedingDeadlineListRsrc> response = dao.Process(ResourceTypes.SEEDING_DEADLINE_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	@Override
	public SeedingDeadlineListRsrc saveSeedingDeadlines(EndpointsRsrc parent, SeedingDeadlineListRsrc resource, String cropYear)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<SeedingDeadlineListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SeedingDeadlineListRsrc.class);
		
		try {
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "cropYear",  cropYear);
			
			Response<SeedingDeadlineListRsrc> response = dao.Process(ResourceTypes.SAVE_SEEDING_DEADLINE_LIST, this.getTransformer(), parent, resource, queryParams, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}	
	
	//////////////////////////////////////////////////////
	// Commodity Types
	//////////////////////////////////////////////////////	
	@Override
	public CommodityTypeCodeListRsrc getCommodityTypeCodeList(EndpointsRsrc parent, String insurancePlanId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<CommodityTypeCodeListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CommodityTypeCodeListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			
			Response<CommodityTypeCodeListRsrc> response = dao.Process(ResourceTypes.COMMODITY_TYPE_CODE_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	//////////////////////////////////////////////////////
	// Risk Area
	//////////////////////////////////////////////////////	
	@Override
	public RiskAreaListRsrc getRiskAreaList(EndpointsRsrc parent, String insurancePlanId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<RiskAreaListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(RiskAreaListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			
			Response<RiskAreaListRsrc> response = dao.Process(ResourceTypes.RISK_AREA_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}
	
	//////////////////////////////////////////////////////
	// Crop Commodities
	//////////////////////////////////////////////////////	
	@Override
	public CropCommodityListRsrc getCropCommodityList(
			EndpointsRsrc parent,
			String insurancePlanId, 
			String cropYear, 
			String commodityType,
			String loadChildren)
			throws CirrasUnderwritingServiceException {

		GenericRestDAO<CropCommodityListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CropCommodityListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "commodityType",  commodityType);
			putQueryParam(queryParams, "loadChildren",  loadChildren);
			
			Response<CropCommodityListRsrc> response = dao.Process(ResourceTypes.CROP_COMMODITY_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Legal Land List
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public LegalLandListRsrc getLegalLandList(
			EndpointsRsrc parent,
			String legalLocation, 
			String primaryPropertyIdentifier, 
			String growerInfo,
			String datasetType, 
			String isWildCardSearch, 
			String searchByLegalLocOrLegalDesc, 
			String sortColumn,
			String sortDirection, 
			Integer pageNumber, 
			Integer pageRowCount
	) throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<LegalLandListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "legalLocation",  legalLocation);
			putQueryParam(queryParams, "primaryPropertyIdentifier",  primaryPropertyIdentifier);
			putQueryParam(queryParams, "growerInfo",  growerInfo);
			putQueryParam(queryParams, "datasetType",  datasetType);
			putQueryParam(queryParams, "isWildCardSearch",  isWildCardSearch);
			putQueryParam(queryParams, "searchByLegalLocOrLegalDesc",  searchByLegalLocOrLegalDesc);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "sortDirection",  sortDirection);
			putQueryParam(queryParams, "pageNumber", toQueryParam(pageNumber));
			putQueryParam(queryParams, "pageRowCount", toQueryParam(pageRowCount));
			
			Response<LegalLandListRsrc> response = dao.Process(ResourceTypes.LEGAL_LAND_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Legal Land
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public LegalLandRsrc createLegalLand(EndpointsRsrc parent, LegalLandRsrc resource)
	throws CirrasUnderwritingServiceException, ValidationException {
			
		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);
		
		try {
			Response<LegalLandRsrc> response = dao.Process(ResourceTypes.CREATE_LEGAL_LAND, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public LegalLandRsrc getLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);
		
		try {
			Response<LegalLandRsrc> response = dao.Process(ResourceTypes.SELF, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}


	@Override
	public LegalLandRsrc updateLegalLand(LegalLandRsrc resource)
	throws CirrasUnderwritingServiceException, ValidationException {
	
		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);

		try {
			Response<LegalLandRsrc> response = dao.Process(ResourceTypes.UPDATE_LEGAL_LAND, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	
	@Override
	public void deleteLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_LEGAL_LAND, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Underwriting Year
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public UnderwritingYearRsrc createUnderwritingYear(EndpointsRsrc parent, UnderwritingYearRsrc resource)
	throws CirrasUnderwritingServiceException, ValidationException {
			
		GenericRestDAO<UnderwritingYearRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UnderwritingYearRsrc.class);
		
		try {
			Response<UnderwritingYearRsrc> response = dao.Process(ResourceTypes.CREATE_UNDERWRITING_YEAR, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public UnderwritingYearRsrc getUnderwritingYear(UnderwritingYearRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<UnderwritingYearRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UnderwritingYearRsrc.class);
		
		try {
			Response<UnderwritingYearRsrc> response = dao.Process(ResourceTypes.SELF, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}
	
	@Override
	public void deleteUnderwritingYear(UnderwritingYearRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<UnderwritingYearRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UnderwritingYearRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_UNDERWRITING_YEAR, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}	
	
	@Override
	public UnderwritingYearListRsrc getUnderwritingYearList(
			EndpointsRsrc parent
	) throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<UnderwritingYearListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UnderwritingYearListRsrc.class);
		
		try {
		
			Response<UnderwritingYearListRsrc> response = dao.Process(ResourceTypes.UNDERWRITING_YEAR_LIST, this.getTransformer(), parent, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// User Setting
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public UserSettingRsrc searchUserSetting(EndpointsRsrc parent) throws CirrasUnderwritingServiceException {
		
		GenericRestDAO<UserSettingRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UserSettingRsrc.class);
		
		try {
		
			Response<UserSettingRsrc> response = dao.Process(ResourceTypes.SEARCH_USER_SETTING, this.getTransformer(), parent, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
	}

	@Override
	public UserSettingRsrc getUserSetting(UserSettingRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<UserSettingRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UserSettingRsrc.class);
		
		try {
			Response<UserSettingRsrc> response = dao.Process(ResourceTypes.SELF, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public UserSettingRsrc createUserSetting(EndpointsRsrc parent, UserSettingRsrc resource)
			throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<UserSettingRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UserSettingRsrc.class);
		
		try {
			Response<UserSettingRsrc> response = dao.Process(ResourceTypes.CREATE_USER_SETTING, this.getTransformer(), parent, resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public UserSettingRsrc updateUserSetting(UserSettingRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		GenericRestDAO<UserSettingRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UserSettingRsrc.class);
		
		try {
			Response<UserSettingRsrc> response = dao.Process(ResourceTypes.UPDATE_USER_SETTING, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void deleteUserSetting(UserSettingRsrc resource) throws CirrasUnderwritingServiceException {

		GenericRestDAO<UserSettingRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(UserSettingRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_USER_SETTING, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}	
	
	//////////////////////////////////////////////////////
	//DATA SYNC METHODS
	//////////////////////////////////////////////////////

	//Generic Code sync
	@Override
	public void synchronizeCode(SyncCodeRsrc resource)
			throws CirrasUnderwritingServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCodeRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_CODE, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	//Generic Code sync
	@Override
	public void deleteSyncCode(EndpointsRsrc parent, String codeTableType, String uniqueKey)
			throws CirrasUnderwritingServiceException {

		GenericRestDAO<SyncCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCodeRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "codeTableType",  codeTableType);
			putQueryParam(queryParams, "uniqueKey",  uniqueKey);
			
			dao.Process(ResourceTypes.DELETE_SYNC_CODE, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
		
	}
	
	//GROWER SYNC
	@Override
	public GrowerRsrc getGrower(EndpointsRsrc parent, String growerId)
	throws CirrasUnderwritingServiceException {

		GenericRestDAO<GrowerRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerRsrc.class);

		try {
			
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "growerId",  growerId);
			
			Response<GrowerRsrc> response = dao.Process(ResourceTypes.GROWER, this.getTransformer(), parent, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void synchronizeGrower(GrowerRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<GrowerRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_GROWER, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public void deleteGrower(EndpointsRsrc parent, String growerId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<GrowerRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "growerId",  growerId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_GROWER, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
		
	}

	//POLICY SYNC
	@Override
	public PolicyRsrc getPolicy(EndpointsRsrc parent, String policyId)
	throws CirrasUnderwritingServiceException {

		GenericRestDAO<PolicyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(PolicyRsrc.class);

		try {
			
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "policyId",  policyId);
			
			Response<PolicyRsrc> response = dao.Process(ResourceTypes.POLICY, this.getTransformer(), parent, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void synchronizePolicy(PolicyRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<PolicyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(PolicyRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_POLICY, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public void deletePolicy(EndpointsRsrc parent, String policyId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<PolicyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(PolicyRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "policyId",  policyId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_POLICY, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
		
	}

	//PRODUCT SYNC
	@Override
	public ProductRsrc getProduct(EndpointsRsrc parent, String productId)
	throws CirrasUnderwritingServiceException {

		GenericRestDAO<ProductRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ProductRsrc.class);

		try {
			
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "productId",  productId);
			
			Response<ProductRsrc> response = dao.Process(ResourceTypes.PRODUCT, this.getTransformer(), parent, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
	}

	@Override
	public void synchronizeProduct(ProductRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<ProductRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ProductRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_PRODUCT, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public void deleteProduct(EndpointsRsrc parent, String productId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<ProductRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ProductRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "productId",  productId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_PRODUCT, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
		
	}
	
	@Override
	public void synchronizeLegalLand(LegalLandRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_LEGAL_LAND, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasUnderwritingServiceException(rde);
		}
		
	}

	@Override
	public void deleteLegalLandSync(EndpointsRsrc parent, String legalLandId) throws CirrasUnderwritingServiceException {

		GenericRestDAO<LegalLandRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "legalLandId",  legalLandId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_LEGAL_LAND, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasUnderwritingServiceException(e);
		}
		
	}
	
	
	//FIELD SYNC
	@Override
	public FieldRsrc getField(EndpointsRsrc parent, String fieldId)
	throws CirrasUnderwritingServiceException {

		GenericRestDAO<FieldRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(FieldRsrc.class);

		try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "fieldId",  fieldId);
				
				Response<FieldRsrc> response = dao.Process(ResourceTypes.FIELD, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeField(FieldRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<FieldRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(FieldRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_FIELD, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteField(EndpointsRsrc parent, String fieldId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<FieldRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(FieldRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "fieldId",  fieldId);
				
				dao.Process(ResourceTypes.DELETE_FIELD, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}
		
		
		//LEGAL LAND - FIELD XREF SYNC
		@Override
		public LegalLandFieldXrefRsrc getLegalLandFieldXref(EndpointsRsrc parent, String legalLandId, String fieldId)
				throws CirrasUnderwritingServiceException {

			GenericRestDAO<LegalLandFieldXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandFieldXrefRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "legalLandId",  legalLandId);
				putQueryParam(queryParams, "fieldId",  fieldId);
				
				Response<LegalLandFieldXrefRsrc> response = dao.Process(ResourceTypes.LEGAL_LAND_FIELD_XREF, this.getTransformer(), parent, queryParams, getWebClient());
				
				return response.getResource();
				
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeLegalLandFieldXref(LegalLandFieldXrefRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<LegalLandFieldXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandFieldXrefRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_LEGAL_LAND_FIELD_XREF, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteLegalLandFieldXref(EndpointsRsrc parent, String legalLandId, String fieldId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<LegalLandFieldXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(LegalLandFieldXrefRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "legalLandId",  legalLandId);
				putQueryParam(queryParams, "fieldId",  fieldId);
				
				dao.Process(ResourceTypes.DELETE_LEGAL_LAND_FIELD_XREF, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}
		
		
		//Annual Field Detail SYNC
		@Override
		public AnnualFieldDetailRsrc getAnnualFieldDetail(EndpointsRsrc parent, String annualFieldDetailId)
				throws CirrasUnderwritingServiceException {

		GenericRestDAO<AnnualFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AnnualFieldDetailRsrc.class);

		try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "annualFieldDetailId",  annualFieldDetailId);
				
				Response<AnnualFieldDetailRsrc> response = dao.Process(ResourceTypes.ANNUAL_FIELD_DETAIL, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeAnnualFieldDetail(AnnualFieldDetailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<AnnualFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AnnualFieldDetailRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_ANNUAL_FIELD_DETAIL, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteAnnualFieldDetail(EndpointsRsrc parent, String annualFieldDetailId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<AnnualFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(AnnualFieldDetailRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "annualFieldDetailId",  annualFieldDetailId);
				
				dao.Process(ResourceTypes.DELETE_ANNUAL_FIELD_DETAIL, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}
		
		//GROWER CONTRACT YEAR SYNC
		@Override
		public GrowerContractYearSyncRsrc getGrowerContractYear(EndpointsRsrc parent, String growerContractYearId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<GrowerContractYearSyncRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContractYearSyncRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "growerContractYearId",  growerContractYearId);
				
				Response<GrowerContractYearSyncRsrc> response = dao.Process(ResourceTypes.GROWER_CONTRACT_YEAR, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeGrowerContractYear(GrowerContractYearSyncRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<GrowerContractYearSyncRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContractYearSyncRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_GROWER_CONTRACT_YEAR, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteGrowerContractYear(EndpointsRsrc parent, String growerContractYearId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<GrowerContractYearSyncRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContractYearSyncRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "growerContractYearId",  growerContractYearId);
				
				dao.Process(ResourceTypes.DELETE_GROWER_CONTRACT_YEAR, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}


		//Contracted Field Detail SYNC
		@Override
		public ContractedFieldDetailRsrc getContractedFieldDetail(EndpointsRsrc parent, String contractedFieldDetailId)
				throws CirrasUnderwritingServiceException {

		GenericRestDAO<ContractedFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContractedFieldDetailRsrc.class);

		try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contractedFieldDetailId",  contractedFieldDetailId);
				
				Response<ContractedFieldDetailRsrc> response = dao.Process(ResourceTypes.CONTRACTED_FIELD_DETAIL, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeContractedFieldDetail(ContractedFieldDetailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<ContractedFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContractedFieldDetailRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_CONTRACTED_FIELD_DETAIL, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteContractedFieldDetail(EndpointsRsrc parent, String contractedFieldDetailId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContractedFieldDetailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContractedFieldDetailRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contractedFieldDetailId",  contractedFieldDetailId);
				
				dao.Process(ResourceTypes.DELETE_CONTRACTED_FIELD_DETAIL, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}

		//Commodity Variety SYNC
		@Override
		public SyncCommodityVarietyRsrc getSyncCommodityVariety(EndpointsRsrc parent, String crptId)
				throws CirrasUnderwritingServiceException {

			GenericRestDAO<SyncCommodityVarietyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityVarietyRsrc.class);

			try {
					
					Map<String, String> queryParams = new HashMap<String, String>();
					
					putQueryParam(queryParams, "crptId",  crptId);
					
					Response<SyncCommodityVarietyRsrc> response = dao.Process(ResourceTypes.SYNC_COMMODITY_VARIETY, this.getTransformer(), parent, queryParams, getWebClient());
					return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}
		
		@Override
		public void synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {
			
			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<SyncCommodityVarietyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityVarietyRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_SYNC_COMMODITY_VARIETY, this.getTransformer(), parentEndpoint, resource, getWebClient());			
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}
			
		@Override
		public void deleteSyncCommodityVariety(EndpointsRsrc parent, String crptId) throws CirrasUnderwritingServiceException {
			
			GenericRestDAO<SyncCommodityVarietyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityVarietyRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "crptId",  crptId);
				
				dao.Process(ResourceTypes.DELETE_SYNC_COMMODITY_VARIETY, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}		
		}				

		//CONTACT SYNC
		@Override
		public ContactRsrc getContact(EndpointsRsrc parent, String contactId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactId",  contactId);
				
				Response<ContactRsrc> response = dao.Process(ResourceTypes.CONTACT, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeContact(ContactRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<ContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_CONTACT, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteContact(EndpointsRsrc parent, String contactId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactId",  contactId);
				
				dao.Process(ResourceTypes.DELETE_CONTACT, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}

		//GROWER CONTACT SYNC
		@Override
		public GrowerContactRsrc getGrowerContact(EndpointsRsrc parent, String growerContactId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<GrowerContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContactRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "growerContactId",  growerContactId);
				
				Response<GrowerContactRsrc> response = dao.Process(ResourceTypes.GROWER_CONTACT, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeGrowerContact(GrowerContactRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<GrowerContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContactRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_GROWER_CONTACT, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteGrowerContact(EndpointsRsrc parent, String growerContactId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<GrowerContactRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(GrowerContactRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "growerContactId",  growerContactId);
				
				dao.Process(ResourceTypes.DELETE_GROWER_CONTACT, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}

		//CONTACT EMAIL SYNC
		@Override
		public ContactEmailRsrc getContactEmail(EndpointsRsrc parent, String contactEmailId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactEmailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactEmailRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactEmailId",  contactEmailId);
				
				Response<ContactEmailRsrc> response = dao.Process(ResourceTypes.CONTACT_EMAIL, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeContactEmail(ContactEmailRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<ContactEmailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactEmailRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_CONTACT_EMAIL, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteContactEmail(EndpointsRsrc parent, String contactEmailId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactEmailRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactEmailRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactEmailId",  contactEmailId);
				
				dao.Process(ResourceTypes.DELETE_CONTACT_EMAIL, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}		

		//CONTACT PHONE SYNC
		@Override
		public ContactPhoneRsrc getContactPhone(EndpointsRsrc parent, String contactPhoneId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactPhoneRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactPhoneRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactPhoneId",  contactPhoneId);
				
				Response<ContactPhoneRsrc> response = dao.Process(ResourceTypes.CONTACT_PHONE, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeContactPhone(ContactPhoneRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<ContactPhoneRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactPhoneRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_CONTACT_PHONE, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteContactPhone(EndpointsRsrc parent, String contactPhoneId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<ContactPhoneRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ContactPhoneRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "contactPhoneId",  contactPhoneId);
				
				dao.Process(ResourceTypes.DELETE_CONTACT_PHONE, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}		

		//COMMODITY TYPE CODE SYNC
		@Override
		public SyncCommodityTypeCodeRsrc getCommodityTypeCode(EndpointsRsrc parent, String commodityTypeCode)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<SyncCommodityTypeCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeCodeRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "commodityTypeCode",  commodityTypeCode);
				
				Response<SyncCommodityTypeCodeRsrc> response = dao.Process(ResourceTypes.COMMODITY_TYPE_CODE, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeCommodityTypeCode(SyncCommodityTypeCodeRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<SyncCommodityTypeCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeCodeRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_COMMODITY_TYPE_CODE, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteCommodityTypeCode(EndpointsRsrc parent, String commodityTypeCode) throws CirrasUnderwritingServiceException {

			GenericRestDAO<SyncCommodityTypeCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeCodeRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "commodityTypeCode",  commodityTypeCode);
				
				dao.Process(ResourceTypes.DELETE_COMMODITY_TYPE_CODE, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}
		//COMMODITY TYPE VARIETY XREF SYNC
		@Override
		public SyncCommodityTypeVarietyXrefRsrc getCommodityTypeVarietyXref(EndpointsRsrc parent, String commodityTypeCode, String cropVarietyId)
		throws CirrasUnderwritingServiceException {

			GenericRestDAO<SyncCommodityTypeVarietyXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeVarietyXrefRsrc.class);

			try {
				
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "commodityTypeCode",  commodityTypeCode);
				putQueryParam(queryParams, "cropVarietyId",  cropVarietyId);
				
				Response<SyncCommodityTypeVarietyXrefRsrc> response = dao.Process(ResourceTypes.COMMODITY_TYPE_VARIETY_XREF, this.getTransformer(), parent, queryParams, getWebClient());
				return response.getResource();
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
		}

		@Override
		public void synchronizeCommodityTypeVarietyXref(SyncCommodityTypeVarietyXrefRsrc resource) throws CirrasUnderwritingServiceException, ValidationException {

			EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			GenericRestDAO<SyncCommodityTypeVarietyXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeVarietyXrefRsrc.class);
			
			try {
				dao.Process(ResourceTypes.SYNCHRONIZE_COMMODITY_TYPE_VARIETY_XREF, this.getTransformer(), parentEndpoint, resource, getWebClient());
							
			} catch(BadRequestException e) {
				throw new ValidationException(e.getMessages());			
			} catch (RestDAOException rde) {
				throw new CirrasUnderwritingServiceException(rde);
			}
			
		}

		@Override
		public void deleteCommodityTypeVarietyXref(EndpointsRsrc parent, String commodityTypeCode, String cropVarietyId) throws CirrasUnderwritingServiceException {

			GenericRestDAO<SyncCommodityTypeVarietyXrefRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityTypeVarietyXrefRsrc.class);
			
			try {
			
				Map<String, String> queryParams = new HashMap<String, String>();
				
				putQueryParam(queryParams, "commodityTypeCode",  commodityTypeCode);
				putQueryParam(queryParams, "cropVarietyId",  cropVarietyId);
				
				dao.Process(ResourceTypes.DELETE_COMMODITY_TYPE_VARIETY_XREF, this.getTransformer(), parent, queryParams, getWebClient());
		
			} catch (RestDAOException e) {
				throw new CirrasUnderwritingServiceException(e);
			}
			
		}

}
