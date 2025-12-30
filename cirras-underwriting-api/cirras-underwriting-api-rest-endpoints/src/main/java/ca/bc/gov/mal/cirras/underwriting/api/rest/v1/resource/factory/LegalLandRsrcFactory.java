package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandListRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandList;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.services.model.factory.LegalLandFactory;

public class LegalLandRsrcFactory extends BaseResourceFactory implements LegalLandFactory { 
	
	//======================================================================================================================
	// Annual Field List (Annual Field Search)
	//======================================================================================================================

	@Override
	public LegalLandList<? extends LegalLand<? extends Field>> getLegalLandList(
			PagedDtos<LegalLandDto> dtos, 
			String legalLocation,
			String primaryPropertyIdentifier, 
			String growerInfo, 
			String datasetType, 
			Boolean isWildCardSearch,
			Boolean searchByLegalLocOrLegalDesc, 
			String sortColumn, 
			String sortDirection, 
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {

		URI baseUri = getBaseURI(context);
		
		LegalLandListRsrc result = null;
		
		List<LegalLandRsrc> resources = new ArrayList<LegalLandRsrc>();

		for (LegalLandDto dto : dtos.getResults()) {
			
			LegalLandRsrc resource = new LegalLandRsrc();
			
			populateResource(resource, dto);
			setSelfLink(resource.getLegalLandId(), resource, baseUri);
			
			resources.add(resource);
		}
		
		int pageNumber = dtos.getPageNumber();
		int totalRowCount = dtos.getTotalRowCount();
		pageRowCount = Integer.valueOf(pageRowCount==null?totalRowCount:pageRowCount.intValue());
		int totalPageCount = (int) Math.ceil(((double)totalRowCount)/((double)pageRowCount.intValue()));
		
		result = new LegalLandListRsrc();
		result.setCollection(resources);
		result.setPageNumber(pageNumber);
		result.setPageRowCount(pageRowCount.intValue());
		result.setTotalRowCount(totalRowCount);
		result.setTotalPageCount(totalPageCount);
		
		String eTag = getEtag(result);
		result.setETag(eTag);		

		
		setSelfLink(result, 
				legalLocation, 
				primaryPropertyIdentifier, 
				growerInfo, 
				datasetType, 
				isWildCardSearch, 
				searchByLegalLocOrLegalDesc, 
				sortColumn, 
				sortDirection, 
				pageNumber, 
				pageRowCount.intValue(), 
				baseUri);

		setLinks(result, 
				legalLocation, 
				primaryPropertyIdentifier, 
				growerInfo, 
				datasetType, 
				isWildCardSearch, 
				searchByLegalLocOrLegalDesc, 
				sortColumn, 
				sortDirection, 
				pageNumber, 
				pageRowCount.intValue(), 
				totalRowCount, 
				baseUri);
		
		return result;
	}
	
	public static String getLegalLandListSelfUri(
		String legalLocation,
		String primaryPropertyIdentifier, 
		String growerInfo, 
		String datasetType, 
		Boolean isWildCardSearch,
		Boolean searchByLegalLocOrLegalDesc, 
		String sortColumn, 
		String sortDirection, 
		Integer pageNumber, 
		Integer pageRowCount, 
		URI baseUri
	) {

		String result = UriBuilder.fromUri(baseUri)
			.path(LegalLandListEndpoint.class)
			.queryParam("legalLocation", nvl(legalLocation, ""))
			.queryParam("primaryPropertyIdentifier", nvl(primaryPropertyIdentifier, ""))
			.queryParam("growerInfo", nvl(growerInfo, ""))
			.queryParam("datasetType", nvl(datasetType, ""))
			.queryParam("isWildCardSearch", nvl(toString(isWildCardSearch), ""))
			.queryParam("searchByLegalLocOrLegalDesc", nvl(toString(searchByLegalLocOrLegalDesc), ""))
			.queryParam("sortColumn", nvl(sortColumn, ""))
			.queryParam("sortDirection", nvl(sortDirection, ""))
			.queryParam("pageNumber", nvl(toString(pageNumber), ""))
			.queryParam("pageRowCount", nvl(toString(pageRowCount), ""))
			.build()
			.toString();

		return result;
	}
	
	
	private static void setSelfLink(
		LegalLandListRsrc resource, 
		String legalLocation,
		String primaryPropertyIdentifier, 
		String growerInfo, 
		String datasetType, 
		Boolean isWildCardSearch,
		Boolean searchByLegalLocOrLegalDesc, 
		String sortColumn, 
		String sortDirection, 
		int pageNumber, 
		int pageRowCount, 
		URI baseUri) {


		String selfUri = getLegalLandListSelfUri(
				legalLocation,
				primaryPropertyIdentifier, 
				growerInfo, 
				datasetType, 
				isWildCardSearch,
				searchByLegalLocOrLegalDesc, 
				sortColumn, 
				sortDirection, 
				Integer.valueOf(pageNumber), 
				Integer.valueOf(pageRowCount), 
				baseUri);
		
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}
	

	private static void setLinks(
		LegalLandListRsrc resource, 
		String legalLocation,
		String primaryPropertyIdentifier, 
		String growerInfo, 
		String datasetType, 
		Boolean isWildCardSearch,
		Boolean searchByLegalLocOrLegalDesc, 
		String sortColumn, 
		String sortDirection, 
		int pageNumber, 
		int pageRowCount, 
		int totalRowCount,
		URI baseUri) {
		
		if(pageNumber > 1) {

			String previousUri = getLegalLandListSelfUri(
					legalLocation,
					primaryPropertyIdentifier, 
					growerInfo, 
					datasetType, 
					isWildCardSearch,
					searchByLegalLocOrLegalDesc, 
					sortColumn, 
					sortDirection, 
					Integer.valueOf(pageNumber-1), 
					Integer.valueOf(pageRowCount), 
					baseUri);
			
			resource.getLinks().add(new RelLink(ResourceTypes.PREV, previousUri, "GET"));
		}
		
		if((pageNumber * pageRowCount) < totalRowCount) {

			String nextUri = getLegalLandListSelfUri(
					legalLocation,
					primaryPropertyIdentifier, 
					growerInfo, 
					datasetType, 
					isWildCardSearch,
					searchByLegalLocOrLegalDesc, 
					sortColumn, 
					sortDirection, 
					Integer.valueOf(pageNumber+1), 
					Integer.valueOf(pageRowCount), 
					baseUri);
			
			resource.getLinks().add(new RelLink(ResourceTypes.NEXT, nextUri, "GET"));
		}
	}
	
	
	@Override
	public LegalLand<? extends Field> getLegalLand(
			LegalLandDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) {
		LegalLandRsrc resource = new LegalLandRsrc();
		
		populateResource(resource, dto);
		
		//Risk Areas
		if (!dto.getRiskAreas().isEmpty()) {
			List<LegalLandRiskArea> riskAreas = new ArrayList<LegalLandRiskArea>();

			for (RiskAreaDto rDto : dto.getRiskAreas()) {
				LegalLandRiskArea raModel = createLegalLandRiskArea(rDto);
				riskAreas.add(raModel);
			}

			resource.setRiskAreas(riskAreas);
		}
		
		//Fields
		if (!dto.getFields().isEmpty()) {
			List<FieldRsrc> fields = new ArrayList<FieldRsrc>();

			for (FieldDto fDto : dto.getFields()) {
				FieldRsrc fResource = createField(fDto);
				fields.add(fResource);
			}

			resource.setFields(fields);
		}

		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getLegalLandId(), resource, baseUri);
		setLinks(dto.getLegalLandId(), resource, baseUri, authentication);
		
		return resource;
	}
	
	private static LegalLandRiskArea createLegalLandRiskArea(RiskAreaDto dto) {
		
		LegalLandRiskArea model = new LegalLandRiskArea();
		
		model.setRiskAreaId(dto.getRiskAreaId());
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setRiskAreaName(dto.getRiskAreaName());
		model.setDescription(dto.getDescription());
		model.setInsurancePlanName(dto.getInsurancePlanName());
		model.setLegalLandId(dto.getLegalLandId());
		model.setDeletedByUserInd(false);

		return model;
	}
	
	private static FieldRsrc createField(FieldDto dto) {
		FieldRsrc resource = new FieldRsrc();
		
		resource.setFieldId(dto.getFieldId());
		resource.setFieldLabel(dto.getFieldLabel());
		resource.setFieldLocation(dto.getLocation());
		resource.setTotalLegalLand(dto.getTotalLegalLand());

		return resource;
	}
	static void populateResource(LegalLandRsrc resource, LegalLandDto dto) {
		
		resource.setLegalLandId(dto.getLegalLandId());
		resource.setPrimaryPropertyIdentifier(dto.getPrimaryPropertyIdentifier());
		resource.setPrimaryLandIdentifierTypeCode(dto.getPrimaryLandIdentifierTypeCode());
		resource.setPrimaryReferenceTypeCode(dto.getPrimaryReferenceTypeCode());
		resource.setLegalDescription(dto.getLegalDescription());
		resource.setLegalShortDescription(dto.getLegalShortDescription());
		resource.setOtherDescription(dto.getOtherDescription());
		resource.setActiveFromCropYear(dto.getActiveFromCropYear());
		resource.setActiveToCropYear(dto.getActiveToCropYear());
		resource.setTotalAcres(dto.getTotalAcres());

	}
	
	static void setSelfLink(Integer legalLandId, LegalLandRsrc resource, URI baseUri) {
		if (legalLandId != null) {
			String selfUri = getLegalLandSelfUri(legalLandId, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	private static String getLegalLandSelfUri(Integer legalLandId, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(LegalLandEndpoint.class).build(legalLandId).toString();
		return result;
	}
	
	public static String getLegalLandListSelfUri(URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(LegalLandListEndpoint.class).build().toString();
		return result;
	}

	private static void setLinks(Integer legalLandId, LegalLandRsrc resource, URI baseUri, WebAdeAuthentication authentication) {

		String result = UriBuilder.fromUri(baseUri).path(LegalLandEndpoint.class).build(legalLandId).toString();

		if (authentication.hasAuthority(Scopes.UPDATE_LEGAL_LAND)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_LEGAL_LAND, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_LEGAL_LAND)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_LEGAL_LAND, result, "DELETE"));
		}
	}
	
	@Override
	public void createQuickLegalLand(LegalLandDto dto, AnnualField model, String primaryReferenceTypeCode, String landIdentifierTypeCode) {

		dto.setLegalLandId(null);
		dto.setPrimaryReferenceTypeCode(primaryReferenceTypeCode);
		dto.setLegalDescription(null);
		dto.setLegalShortDescription(null);
		dto.setOtherDescription(model.getOtherLegalDescription());
		dto.setActiveFromCropYear(model.getCropYear());
		dto.setActiveToCropYear(null);
		dto.setPrimaryPropertyIdentifier(model.getPrimaryPropertyIdentifier());
		dto.setPrimaryLandIdentifierTypeCode(landIdentifierTypeCode);
		dto.setTotalAcres(null);

	}

	@Override
	public void updateLegalLand(LegalLandDto dto, LegalLand<? extends Field> model) {

		dto.setLegalLandId(model.getLegalLandId());
		dto.setPrimaryPropertyIdentifier(model.getPrimaryPropertyIdentifier());
		dto.setPrimaryLandIdentifierTypeCode(model.getPrimaryLandIdentifierTypeCode());
		dto.setPrimaryReferenceTypeCode(model.getPrimaryReferenceTypeCode());
		dto.setLegalDescription(model.getLegalDescription());
		dto.setLegalShortDescription(model.getLegalShortDescription());
		dto.setOtherDescription(model.getOtherDescription());
		dto.setActiveFromCropYear(model.getActiveFromCropYear());
		dto.setActiveToCropYear(model.getActiveToCropYear());
		dto.setTotalAcres(model.getTotalAcres());

	}
}
