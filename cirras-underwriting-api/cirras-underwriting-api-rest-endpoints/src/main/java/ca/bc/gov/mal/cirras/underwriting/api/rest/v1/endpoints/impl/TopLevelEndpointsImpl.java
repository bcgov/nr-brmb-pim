package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CirrasDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.TopLevelEndpoints;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CommodityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CommodityTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.CropVarietyInsurabilityRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.DopYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.GradeModifierRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.GradeModifierTypeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.AnnualFieldRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.InventoryContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LandDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.LegalLandRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.RiskAreaRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.SeedingDeadlineRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.UnderwritingYearRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.UserSettingRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.UwContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.VerifiedYieldContractRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.YieldMeasUnitConversionRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory.YieldMeasUnitTypeCodeRsrcFactory;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.api.rest.code.resource.factory.CodeTableResourceFactory;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;

public class TopLevelEndpointsImpl extends BaseEndpointsImpl implements TopLevelEndpoints {
	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(TopLevelEndpointsImpl.class);
	
	@Override
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getTopLevel() {
		Response response = null;

		URI baseUri = this.getBaseUri();
		
		EndpointsRsrc result = new EndpointsRsrc();

		result.setReleaseVersion(this.getApplicationProperties().getProperty("application.version"));

		try {
			
			String selfURI = CodeTableResourceFactory.getCodeTablesSelfUri(null, baseUri);
			result.getLinks().add(new RelLink(ResourceTypes.CODE_TABLE_LIST, selfURI, HttpMethod.GET));

			if (hasAuthority(Scopes.SEARCH_UWCONTRACTS)) {
				selfURI = UwContractRsrcFactory.getUwContractListSelfUri(null, null, null, null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.UWCONTRACT_LIST, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.PRINT_INVENTORY_CONTRACT)) {
				selfURI = InventoryContractRsrcFactory.getInventoryContractListSelfUri(null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.INVENTORY_CONTRACT_LIST, selfURI, HttpMethod.GET));

				selfURI = InventoryContractRsrcFactory.getInventoryContractReportSelfUri(null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.BYTES, selfURI, HttpMethod.GET));			
			}

			if (hasAuthority(Scopes.PRINT_DOP_YIELD_CONTRACT)) {
				selfURI = DopYieldContractRsrcFactory.getDopYieldContractReportSelfUri(null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.BYTES, selfURI, HttpMethod.GET));
			}
			
			//Get crop commodity list
			selfURI = CommodityRsrcFactory.getCropCommodityListSelfUri(baseUri);
			result.getLinks().add(new RelLink(ResourceTypes.CROP_COMMODITY_LIST, selfURI, HttpMethod.GET));
			
			if (hasAuthority(Scopes.GET_YIELD_MEAS_UNIT_TYPE_CODES)) {
				selfURI = YieldMeasUnitTypeCodeRsrcFactory.getYieldMeasUnitTypeCodeListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.YIELD_MEAS_UNIT_TYPE_CODE_LIST, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_GRADE_MODIFIERS)) {
				selfURI = GradeModifierRsrcFactory.getGradeModifierListSelfUri(null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.GRADE_MODIFIER_LIST, selfURI, HttpMethod.GET));
				
				selfURI = GradeModifierTypeRsrcFactory.getGradeModifierTypeListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.GRADE_MODIFIER_TYPE_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_YIELD_MEAS_UNIT_CONVERSIONS)) {
				selfURI = YieldMeasUnitConversionRsrcFactory.getYieldMeasUnitConversionListSelfUri(null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.YIELD_MEAS_UNIT_CONVERSION_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_CROP_VARIETY_INSURABILITIES)) {
				selfURI = CropVarietyInsurabilityRsrcFactory.getCropVarietyInsurabilityListSelfUri(null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CROP_VARIETY_INSURABILITY_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_SEEDING_DEADLINES)) {
				selfURI = SeedingDeadlineRsrcFactory.getSeedingDeadlineListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SEEDING_DEADLINE_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.SAVE_SEEDING_DEADLINES)) {
				selfURI = SeedingDeadlineRsrcFactory.getSeedingDeadlineListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SAVE_SEEDING_DEADLINE_LIST, selfURI, HttpMethod.PUT));
			}
			
			if (hasAuthority(Scopes.GET_UNDERWRITING_YEAR)) {
				selfURI = UnderwritingYearRsrcFactory.getUnderwritingYearListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.UNDERWRITING_YEAR_LIST, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.CREATE_UNDERWRITING_YEAR)) {
				selfURI = UnderwritingYearRsrcFactory.getUnderwritingYearListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_UNDERWRITING_YEAR, selfURI, HttpMethod.POST));
			}
			
			if (hasAuthority(Scopes.GET_CODE_TABLES)) {
				selfURI = RiskAreaRsrcFactory.getRiskAreaListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.RISK_AREA_LIST, selfURI, HttpMethod.GET));

				//Get commodity type list
				selfURI = CommodityTypeCodeRsrcFactory.getCommodityTypeCodeListSelfUri(null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.COMMODITY_TYPE_CODE_LIST, selfURI, HttpMethod.GET));
				
			}
			
			if (hasAuthority(Scopes.SEARCH_ANNUAL_FIELDS)) {
				selfURI = AnnualFieldRsrcFactory.getAnnualFieldListSelfUri(null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.ANNUAL_FIELD_LIST, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.CREATE_INVENTORY_CONTRACT)) {
				selfURI = InventoryContractRsrcFactory.getInventoryContractListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_INVENTORY_CONTRACT, selfURI, HttpMethod.POST));
			}

			if (hasAuthority(Scopes.CREATE_DOP_YIELD_CONTRACT)) {
				selfURI = DopYieldContractRsrcFactory.getDopYieldContractListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_DOP_YIELD_CONTRACT, selfURI, HttpMethod.POST));
			}
			
			if (hasAuthority(Scopes.CREATE_VERIFIED_YIELD_CONTRACT)) {
				selfURI = VerifiedYieldContractRsrcFactory.getVerifiedYieldContractListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_VERIFIED_YIELD_CONTRACT, selfURI, HttpMethod.POST));
			}
			
			if (hasAuthority(Scopes.CREATE_LEGAL_LAND)) {
				selfURI = LegalLandRsrcFactory.getLegalLandListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CREATE_LEGAL_LAND, selfURI, HttpMethod.POST));
			}
			
			if (hasAuthority(Scopes.GET_GROWER)) {
				selfURI = CirrasDataSyncRsrcFactory.getGrowerSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.GROWER, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_POLICY)) {
				selfURI = CirrasDataSyncRsrcFactory.getPolicySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.POLICY, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_PRODUCT)) {
				selfURI = CirrasDataSyncRsrcFactory.getProductSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.PRODUCT, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_CONTACT)) {
				selfURI = CirrasDataSyncRsrcFactory.getContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CONTACT, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_GROWER_CONTACT)) {
				selfURI = CirrasDataSyncRsrcFactory.getGrowerContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.GROWER_CONTACT, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_CONTACT_EMAIL)) {
				selfURI = CirrasDataSyncRsrcFactory.getContactEmailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CONTACT_EMAIL, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_CONTACT_PHONE)) {
				selfURI = CirrasDataSyncRsrcFactory.getContactPhoneSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CONTACT_PHONE, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_COMMODITY_TYPE_CODE)) {
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeCodeSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.COMMODITY_TYPE_CODE, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_COMMODITY_TYPE_VARIETY_XREF)) {
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeVarietyXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.COMMODITY_TYPE_VARIETY_XREF, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.GET_LAND)) {
				selfURI = LandDataSyncRsrcFactory.getFieldSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.FIELD, selfURI, HttpMethod.GET));
		
				selfURI = LandDataSyncRsrcFactory.getLegalLandFieldXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.LEGAL_LAND_FIELD_XREF, selfURI, HttpMethod.GET));
				
				selfURI = LandDataSyncRsrcFactory.getAnnualFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.ANNUAL_FIELD_DETAIL, selfURI, HttpMethod.GET));

				selfURI = LandDataSyncRsrcFactory.getGrowerContractYearSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.GROWER_CONTRACT_YEAR, selfURI, HttpMethod.GET));
				
				selfURI = LandDataSyncRsrcFactory.getContractedFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.CONTRACTED_FIELD_DETAIL, selfURI, HttpMethod.GET));
			}
			
			if (hasAuthority(Scopes.GET_COMMODITY_VARIETY)) {
				selfURI = CirrasDataSyncRsrcFactory.getSyncCommodityVarietySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNC_COMMODITY_VARIETY, selfURI, HttpMethod.GET));
			}	
			
			if (hasAuthority(Scopes.GET_LEGAL_LAND)) {
				selfURI = LegalLandRsrcFactory.getLegalLandListSelfUri(null, null, null, null, null, null, null, null, null, null, baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.LEGAL_LAND_LIST, selfURI, HttpMethod.GET));
			}

			if (hasAuthority(Scopes.SEARCH_USER_SETTING)) {
				selfURI = UserSettingRsrcFactory.getUserSettingListSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SEARCH_USER_SETTING, selfURI, HttpMethod.GET));
			}
			
			//**********************************************************
			//Data Sync
			//**********************************************************
			//CREATE/UPDATE
			if (hasAuthority(Scopes.UPDATE_SYNC_UNDERWRITING)) {
				
				//CODE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCodeSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CODE, selfURI, HttpMethod.POST));
				
				//GROWER SYNC
				selfURI = CirrasDataSyncRsrcFactory.getGrowerSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_GROWER, selfURI, HttpMethod.POST));

				//POLICY SYNC
				selfURI = CirrasDataSyncRsrcFactory.getPolicySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_POLICY, selfURI, HttpMethod.POST));

				//PRODUCT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getProductSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_PRODUCT, selfURI, HttpMethod.POST));
				
				//LEGAL LAND SYNC
				selfURI = LandDataSyncRsrcFactory.getLegalLandSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_LEGAL_LAND, selfURI, HttpMethod.POST));
				
				//FIELD SYNC
				selfURI = LandDataSyncRsrcFactory.getFieldSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_FIELD, selfURI, HttpMethod.POST));
				
				//LEGAL LAND - FIELD XREF SYNC
				selfURI = LandDataSyncRsrcFactory.getLegalLandFieldXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_LEGAL_LAND_FIELD_XREF, selfURI, HttpMethod.POST));
				
				//Annual Field Detail SYNC
				selfURI = LandDataSyncRsrcFactory.getAnnualFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_ANNUAL_FIELD_DETAIL, selfURI, HttpMethod.POST));
				
				//GROWER CONTRACT YEAR SYNC
				selfURI = LandDataSyncRsrcFactory.getGrowerContractYearSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_GROWER_CONTRACT_YEAR, selfURI, HttpMethod.POST));
				
				//CONTRACTED FIELD DETAIL SYNC
				selfURI = LandDataSyncRsrcFactory.getContractedFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CONTRACTED_FIELD_DETAIL, selfURI, HttpMethod.POST));

				//COMMODITY/VARIETY SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCommodityVarietySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_SYNC_COMMODITY_VARIETY, selfURI, HttpMethod.POST));

				//CONTACT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CONTACT, selfURI, HttpMethod.POST));

				//GROWER CONTACT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getGrowerContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_GROWER_CONTACT, selfURI, HttpMethod.POST));

				//CONTACT EMAIL SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactEmailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CONTACT_EMAIL, selfURI, HttpMethod.POST));

				//CONTACT PHONE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactPhoneSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_CONTACT_PHONE, selfURI, HttpMethod.POST));

				//COMMODITY TYPE CODE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeCodeSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_COMMODITY_TYPE_CODE, selfURI, HttpMethod.POST));

				//COMMODITY TYPE VARIETY XREF SYNC
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeVarietyXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.SYNCHRONIZE_COMMODITY_TYPE_VARIETY_XREF, selfURI, HttpMethod.POST));

			}

			//DELETE
			if (hasAuthority(Scopes.DELETE_SYNC_UNDERWRITING)) {
				
				//CODE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCodeSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_CODE, selfURI, HttpMethod.DELETE));

				//GROWER SYNC
				selfURI = CirrasDataSyncRsrcFactory.getGrowerSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_GROWER, selfURI, HttpMethod.DELETE));

				//POLICY SYNC
				selfURI = CirrasDataSyncRsrcFactory.getPolicySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_POLICY, selfURI, HttpMethod.DELETE));

				//PRODUCT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getProductSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_PRODUCT, selfURI, HttpMethod.DELETE));
				
				//LEGAL LAND SYNC
				selfURI = LandDataSyncRsrcFactory.getLegalLandSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_LEGAL_LAND, selfURI, HttpMethod.DELETE));

				//FIELD SYNC
				selfURI = LandDataSyncRsrcFactory.getFieldSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_FIELD, selfURI, HttpMethod.DELETE));
				
				//LEGAL LAND - FIELD XREF SYNC
				selfURI = LandDataSyncRsrcFactory.getLegalLandFieldXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_LEGAL_LAND_FIELD_XREF, selfURI, HttpMethod.DELETE));
				
				//ANNUAL FIELD DETAIL SYNC
				selfURI = LandDataSyncRsrcFactory.getAnnualFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_ANNUAL_FIELD_DETAIL, selfURI, HttpMethod.DELETE));

				//GROWER CONTRACT YEAR SYNC
				selfURI = LandDataSyncRsrcFactory.getGrowerContractYearSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_GROWER_CONTRACT_YEAR, selfURI, HttpMethod.DELETE));
				
				//CONTRACTED FIELD DETAIL SYNC
				selfURI = LandDataSyncRsrcFactory.getContractedFieldDetailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_CONTRACTED_FIELD_DETAIL, selfURI, HttpMethod.DELETE));
				
				//COMMODITY/VARIETY SYNC
				selfURI = CirrasDataSyncRsrcFactory.getSyncCommodityVarietySelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_SYNC_COMMODITY_VARIETY, selfURI, HttpMethod.DELETE));

				//CONTACT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_CONTACT, selfURI, HttpMethod.DELETE));

				//GROWER CONTACT SYNC
				selfURI = CirrasDataSyncRsrcFactory.getGrowerContactSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_GROWER_CONTACT, selfURI, HttpMethod.DELETE));

				//CONTACT EMAIL SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactEmailSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_CONTACT_EMAIL, selfURI, HttpMethod.DELETE));

				//CONTACT PHONE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getContactPhoneSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_CONTACT_PHONE, selfURI, HttpMethod.DELETE));

				//COMMODITY TYPE CODE SYNC
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeCodeSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_COMMODITY_TYPE_CODE, selfURI, HttpMethod.DELETE));

				//COMMODITY TYPE VARIETY XREF SYNC
				selfURI = CirrasDataSyncRsrcFactory.getCommodityTypeVarietyXrefSelfUri(baseUri);
				result.getLinks().add(new RelLink(ResourceTypes.DELETE_COMMODITY_TYPE_VARIETY_XREF, selfURI, HttpMethod.DELETE));

			}

			result.setETag(getEtag(result));

			GenericEntity<EndpointsRsrc> entity = new GenericEntity<EndpointsRsrc>(result) {
				/* do nothing */
			};

			response = Response.ok(entity).tag(result.getUnquotedETag()).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getTopLevel=" + response);
		}
		
		return response;
	}
}
