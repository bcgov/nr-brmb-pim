package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import ca.bc.gov.nrs.common.wfone.rest.resource.MessageRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.VerifiedYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiableCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ProductDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldAmendmentDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldSummaryDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.VerifiedYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.VerifiedYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.CommodityCoverageCode;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;

public class VerifiedYieldContractRsrcFactory extends BaseResourceFactory implements VerifiedYieldContractFactory { 
	
	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> getDefaultVerifiedYieldContract(
			PolicyDto policyDto,
			DeclaredYieldContractDto dycDto,
			List<ProductDto> productDtos,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {
		
		VerifiedYieldContractRsrc resource = new VerifiedYieldContractRsrc();

		populateDefaultResource(resource, policyDto, dycDto);

		// Fields
		if (dycDto.getFields() != null && !dycDto.getFields().isEmpty()) {
			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

			for (ContractedFieldDetailDto cfdDto : dycDto.getFields()) {
				AnnualFieldRsrc afModel = createAnnualField(cfdDto, authentication);
				fields.add(afModel);
			}

			resource.setFields(fields);
		}
		
		// Verified Yield Contract Commodity
		if (!dycDto.getDeclaredYieldContractCommodities().isEmpty()) {
			List<VerifiedYieldContractCommodity> vyContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();

			for (DeclaredYieldContractCommodityDto dyccDto : dycDto.getDeclaredYieldContractCommodities()) {
				VerifiedYieldContractCommodity vyccModel = createDefaultVerifiedYieldContractCommodity(dyccDto, productDtos);
				vyContractCommodities.add(vyccModel);
			}

			resource.setVerifiedYieldContractCommodities(vyContractCommodities);
		}
						
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}

	private void populateDefaultResource(VerifiedYieldContractRsrc resource, PolicyDto policyDto, DeclaredYieldContractDto dycDto) {

		resource.setContractId(policyDto.getContractId());
		resource.setCropYear(policyDto.getCropYear());
		resource.setDeclaredYieldContractGuid(policyDto.getDeclaredYieldContractGuid());
		resource.setDefaultYieldMeasUnitTypeCode(dycDto.getDefaultYieldMeasUnitTypeCode());
		resource.setGrowerContractYearId(policyDto.getGrowerContractYearId());
		resource.setInsurancePlanId(policyDto.getInsurancePlanId());
		resource.setVerifiedYieldContractGuid(null);
		resource.setVerifiedYieldUpdateTimestamp(null);
		resource.setVerifiedYieldUpdateUser(null);
		resource.setProductWarningMessages(new ArrayList<MessageRsrc>());
	}
   	
	
	private VerifiedYieldContractCommodity createDefaultVerifiedYieldContractCommodity(DeclaredYieldContractCommodityDto dto, List<ProductDto> productDtos) {
		VerifiedYieldContractCommodity model = new VerifiedYieldContractCommodity();
		
		//Get production guarantee
		Double productionGuarantee = null;
		ProductDto product = getProductDto(dto.getCropCommodityId(), dto.getIsPedigreeInd(), productDtos);
		if(product != null && product.getProductStatusCode().equals(PRODUCT_STATUS_FINAL)) {
			productionGuarantee = product.getProductionGuarantee();
		}

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setHarvestedAcresOverride(null);
		model.setHarvestedYield(null);   // Calculated later
		model.setHarvestedYieldOverride(null);
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setProductionGuarantee(productionGuarantee);
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());
		model.setVerifiedYieldContractCommodityGuid(null);
		model.setVerifiedYieldContractGuid(null);
		model.setYieldPerAcre(null); // Calculated later

		return model;
	}

	@Override
	public VerifiedYieldContract<? extends AnnualField, ? extends Message> getVerifiedYieldContract(VerifiedYieldContractDto dto, List<ProductDto> productDtos,
			FactoryContext context, WebAdeAuthentication authentication) throws FactoryException {

		VerifiedYieldContractRsrc resource = new VerifiedYieldContractRsrc();

		populateResource(resource, dto);

		// Fields
		if (!dto.getFields().isEmpty()) {
			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

			for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
				AnnualFieldRsrc afModel = createAnnualField(cfdDto, authentication);
				fields.add(afModel);
			}

			resource.setFields(fields);
		}
		
		// Verified Yield Contract Commodity
		if (!dto.getVerifiedYieldContractCommodities().isEmpty()) {
			List<VerifiedYieldContractCommodity> verifiedContractCommodities = new ArrayList<VerifiedYieldContractCommodity>();

			for (VerifiedYieldContractCommodityDto vyccDto : dto.getVerifiedYieldContractCommodities()) {
				VerifiedYieldContractCommodity vyccModel = createVerifiedYieldContractCommodity(vyccDto);
				verifiedContractCommodities.add(vyccModel);
				
//				//Check product guarantee
//				MessageRsrc warning = getProductWarning(vyccDto, productDtos);
//				if(warning != null) {
//					productWarnings.add(warning);
//				}
			}

			resource.setVerifiedYieldContractCommodities(verifiedContractCommodities);
		}

		// Verified Yield Amendment
		if (!dto.getVerifiedYieldAmendments().isEmpty()) {
			List<VerifiedYieldAmendment> verifiedYieldAmendments = new ArrayList<VerifiedYieldAmendment>();

			for (VerifiedYieldAmendmentDto vyaDto : dto.getVerifiedYieldAmendments()) {
				VerifiedYieldAmendment vyaModel = createVerifiedYieldAmendment(vyaDto);
				verifiedYieldAmendments.add(vyaModel);
			}

			resource.setVerifiedYieldAmendments(verifiedYieldAmendments);
		}

		List<MessageRsrc> productWarnings = new ArrayList<MessageRsrc>();
		
		// Verified Yield Summary
		if (!dto.getVerifiedYieldSummaries().isEmpty()) {
			List<VerifiedYieldSummary> verifiedYieldSummaries = new ArrayList<VerifiedYieldSummary>();

			for (VerifiedYieldSummaryDto vysDto : dto.getVerifiedYieldSummaries()) {
				VerifiedYieldSummary vysModel = createVerifiedYieldSummary(vysDto, authentication);
				verifiedYieldSummaries.add(vysModel);
				
				//Check product guarantee
				MessageRsrc warning = getProductWarning(vysDto, productDtos);
				if(warning != null) {
					productWarnings.add(warning);
				}
			}

			resource.setVerifiedYieldSummaries(verifiedYieldSummaries);
		}
		
		resource.setProductWarningMessages(productWarnings);
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getVerifiedYieldContractGuid(), resource, baseUri);
 		setLinks(dto.getVerifiedYieldContractGuid(), resource, baseUri, authentication);

		return resource;		
	}

	private void populateResource(VerifiedYieldContractRsrc resource, VerifiedYieldContractDto dto) {
		
		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		resource.setDefaultYieldMeasUnitTypeCode(dto.getDefaultYieldMeasUnitTypeCode());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		resource.setVerifiedYieldUpdateTimestamp(dto.getVerifiedYieldUpdateTimestamp());
		resource.setVerifiedYieldUpdateUser(dto.getVerifiedYieldUpdateUser());
		resource.setUpdateProductValuesInd(false);
		
	}
	
	public static final String PRODUCT_STATUS_FINAL = "FINAL";
	public static final String PRODUCTION_GUARANTEE_DIFFERENCE_MSG = "Production guarantee for %s is different in the product: %.2f";
	public static final String PRODUCTION_GUARANTEE_NONE_MSG = "There is no production guarantee for %s";
	public static final String PRODUCTION_GUARANTEE_NO_PRODUCT_MSG = "There is no product for %s in CIRRAS. The shown production guarantee is not valid anymore.";
	public static final String PROBABLE_YIELD_DIFFERENCE_MSG = "Probable yield for %s is different in the product: %.2f";
	public static final String PROBABLE_YIELD_NONE_MSG = "There is no probable yield for %s";
	public static final String PROBABLE_YIELD_NO_PRODUCT_MSG = "There is no product for %s in CIRRAS. The shown probable yield is not valid anymore.";

	private MessageRsrc getProductWarning(VerifiedYieldSummaryDto vysDto, List<ProductDto> productDtos) {
		
		MessageRsrc messageRsrc = null;
		
		String commodity = vysDto.getCropCommodityName();
		if(vysDto.getIsPedigreeInd()) {
			commodity = commodity + " Pedigreed";
		}
		
		//Find product
		ProductDto product = getProductDto(vysDto.getCropCommodityId(), vysDto.getIsPedigreeInd(), productDtos);
		
		if(product != null) {
			if(product.getProductStatusCode().equals(PRODUCT_STATUS_FINAL)) {
				if (Double.compare(notNull(product.getProductionGuarantee(), (double)-1), notNull(vysDto.getProductionGuarantee(), (double)-1)) != 0) {
					//Add warning if values are different -> Only if product is in status FINAL
					String msg = "";
					if(product.getProductionGuarantee() == null) {
						msg = String.format(PRODUCTION_GUARANTEE_NONE_MSG, commodity, product.getProductionGuarantee());
					} else {
						msg = String.format(PRODUCTION_GUARANTEE_DIFFERENCE_MSG, commodity, product.getProductionGuarantee());
					}
					
					messageRsrc = new MessageRsrc(msg);
				}
				if (Double.compare(notNull(product.getProbableYield(), (double)-1), notNull(vysDto.getProbableYield(), (double)-1)) != 0) {
					//Add warning if values are different -> Only if product is in status FINAL
					String msg = "";
					if(product.getProbableYield() == null) {
						msg = String.format(PROBABLE_YIELD_NONE_MSG, commodity, product.getProbableYield());
					} else {
						msg = String.format(PROBABLE_YIELD_DIFFERENCE_MSG, commodity, product.getProbableYield());
					}
					
					messageRsrc = new MessageRsrc(msg);
				}			}
		} else {
			//No product: Check if the production guarantee in the summary is null
			if(vysDto.getProductionGuarantee() != null) {
				//Add warning if there is no product but a saved production guarantee
				String msg = String.format(PRODUCTION_GUARANTEE_NO_PRODUCT_MSG, commodity);
				messageRsrc = new MessageRsrc(msg);
			}
			//No product: Check if the probable yield in the summary is null
			if(vysDto.getProbableYield() != null) {
				//Add warning if there is no product but a saved probable yield
				String msg = String.format(PROBABLE_YIELD_NO_PRODUCT_MSG, commodity);
				messageRsrc = new MessageRsrc(msg);
			}
		}
		
		
		return messageRsrc;
	}
	
	@Override
	public Set<String> getForageGrainCoverageCodes(){
		return coverageCodesQuantityForageGrain;
	}
	
	private static final Set<String> coverageCodesQuantityForageGrain = new HashSet<String>(Arrays.asList(
											     new String[] {
											    		 	CommodityCoverageCode.QUANTITY_GRAIN, 
											    		 	CommodityCoverageCode.QUANTITY_FORAGE, 
											    		 	CommodityCoverageCode.QUANTITY_SILAGE_CORN
											    		 }));
	
	private ProductDto getProductDto(Integer cropCommodityId, Boolean isPedigree, List<ProductDto> productDtos) {
		
		ProductDto product = null;
		
		if(productDtos != null && productDtos.size() > 0) {
			//Products in CIRRAS use a different commodity id for pedigreed than in this app. A table maps the correct commodity ids and
			//are returned to the NonPedigreeCropCommodityId property
			List<ProductDto> products = productDtos.stream()
					.filter(x -> x.getNonPedigreeCropCommodityId().equals(cropCommodityId) 
							&& x.getIsPedigreeProduct().equals(isPedigree)
							&& coverageCodesQuantityForageGrain.contains(x.getCommodityCoverageCode()))
					.collect(Collectors.toList());
			
			if (products != null && products.size() > 0) {
				product = products.get(0);
			}
		}

		
		return product;
	}

	private VerifiedYieldContractCommodity createVerifiedYieldContractCommodity(VerifiedYieldContractCommodityDto dto) {
		VerifiedYieldContractCommodity model = new VerifiedYieldContractCommodity();

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setHarvestedAcresOverride(dto.getHarvestedAcresOverride());
		model.setHarvestedYield(dto.getHarvestedYield());
		model.setHarvestedYieldOverride(dto.getHarvestedYieldOverride());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setProductionGuarantee(dto.getProductionGuarantee());
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());
		model.setVerifiedYieldContractCommodityGuid(dto.getVerifiedYieldContractCommodityGuid());
		model.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		model.setYieldPerAcre(dto.getYieldPerAcre());

		return model;
	}

	private VerifiedYieldAmendment createVerifiedYieldAmendment(VerifiedYieldAmendmentDto dto) {
		VerifiedYieldAmendment model = new VerifiedYieldAmendment();

		model.setAcres(dto.getAcres());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setFieldId(dto.getFieldId());
		model.setFieldLabel(dto.getFieldLabel());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setRationale(dto.getRationale());
		model.setVerifiedYieldAmendmentCode(dto.getVerifiedYieldAmendmentCode());
		model.setVerifiedYieldAmendmentGuid(dto.getVerifiedYieldAmendmentGuid());
		model.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		model.setYieldPerAcre(dto.getYieldPerAcre());

		return model;
	}

	private VerifiedYieldSummary createVerifiedYieldSummary(VerifiedYieldSummaryDto dto, WebAdeAuthentication authentication) {
		VerifiedYieldSummary model = new VerifiedYieldSummary();

		model.setVerifiedYieldSummaryGuid(dto.getVerifiedYieldSummaryGuid());
		model.setVerifiedYieldContractGuid(dto.getVerifiedYieldContractGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setHarvestedYield(dto.getHarvestedYield());
		model.setHarvestedYieldPerAcre(dto.getHarvestedYieldPerAcre());
		model.setAppraisedYield(dto.getAppraisedYield());
		model.setAssessedYield(dto.getAssessedYield());
		model.setYieldToCount(dto.getYieldToCount());
		model.setYieldPercentPy(dto.getYieldPercentPy());
		model.setProductionGuarantee(dto.getProductionGuarantee());
		model.setProbableYield(dto.getProbableYield());
		
		// UnderwritingComment
		if (!dto.getUwComments().isEmpty()) {
			List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

			for (UnderwritingCommentDto ucDto : dto.getUwComments()) {
				UnderwritingComment ucModel = InventoryContractRsrcFactory.createUnderwritingComment(ucDto, authentication);
				uwComments.add(ucModel);
			}

			model.setUwComments(uwComments);
		}

		return model;
	}
	
	// Creates an AnnualFieldRsrc and populates its verifiableCommodities.
	private AnnualFieldRsrc createAnnualField(ContractedFieldDetailDto dto, WebAdeAuthentication authentication) {
		AnnualFieldRsrc model = new AnnualFieldRsrc();

		AnnualFieldRsrcFactory.populateResource(model, dto);

		// VerifiableCommodities
		if (!dto.getPlantings().isEmpty()) {
			List<VerifiableCommodity> verifiableCommodities = new ArrayList<VerifiableCommodity>();
			Map<String, VerifiableCommodity> vcMap = new HashMap<String, VerifiableCommodity>();

			for (InventoryFieldDto ifDto : dto.getPlantings()) {
				
				if (InsurancePlans.GRAIN.getInsurancePlanId().equals(ifDto.getInsurancePlanId())) {
					for ( InventorySeededGrainDto isgDto : ifDto.getInventorySeededGrains() ) {
						if ( isgDto.getCropCommodityId() != null && isgDto.getIsPedigreeInd() != null ) {						
							VerifiableCommodity vc = createVerifiableCommodity(isgDto);
							String vcKey = vc.getCropCommodityId() + "_" + vc.getIsPedigreeInd();
							vcMap.put(vcKey, vc);
						}
					}
					
				} else if (InsurancePlans.FORAGE.getInsurancePlanId().equals(ifDto.getInsurancePlanId())) {
					
				}
			}

			verifiableCommodities.addAll(vcMap.values());
			verifiableCommodities.sort(new Comparator<VerifiableCommodity>() {
				
				@Override
				public int compare(VerifiableCommodity vc1, VerifiableCommodity vc2) {
					
					int cmpResult = vc1.getCropCommodityName().compareTo(vc2.getCropCommodityName());
					if ( cmpResult == 0 ) {
						cmpResult = vc1.getIsPedigreeInd().compareTo(vc2.getIsPedigreeInd());
					}
					return cmpResult;
				}
				
			} );
			
			model.setVerifiableCommodities(verifiableCommodities);
		}

		return model;
	}

	private VerifiableCommodity createVerifiableCommodity(InventorySeededGrainDto isgDto) {

		VerifiableCommodity vc = new VerifiableCommodity();
		vc.setCropCommodityId(isgDto.getCropCommodityId());
		vc.setCropCommodityName(isgDto.getCropCommodityName());
		vc.setIsPedigreeInd(isgDto.getIsPedigreeInd());
		
		return vc;		
	}
	
	static void setSelfLink(String verifiedYieldContractGuid, VerifiedYieldContractRsrc resource, URI baseUri) {
		if (verifiedYieldContractGuid != null) {
			String selfUri = getVerifiedYieldContractSelfUri(verifiedYieldContractGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getVerifiedYieldContractSelfUri(String verifiedYieldContractGuid, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractEndpoint.class).build(verifiedYieldContractGuid).toString();
		return result;
	}
	
	public static String getVerifiedYieldContractListSelfUri(URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractListEndpoint.class).build().toString();
		return result;
	}

	private static void setLinks(String verifiedYieldContractGuid, VerifiedYieldContractRsrc resource, URI baseUri, WebAdeAuthentication authentication) {		

		String result = UriBuilder.fromUri(baseUri).path(VerifiedYieldContractEndpoint.class).build(verifiedYieldContractGuid).toString();

		if (authentication.hasAuthority(Scopes.UPDATE_VERIFIED_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_VERIFIED_YIELD_CONTRACT, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_VERIFIED_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_VERIFIED_YIELD_CONTRACT, result, "DELETE"));
		}
		
	}

	@Override
	public void updateDto(VerifiedYieldContractDto dto, VerifiedYieldContract<? extends AnnualField, ? extends Message> model, String userId) {
		 
		dto.setVerifiedYieldContractGuid(model.getVerifiedYieldContractGuid());
		dto.setContractId(model.getContractId());
		dto.setCropYear(model.getCropYear());
		dto.setDeclaredYieldContractGuid(model.getDeclaredYieldContractGuid());
		dto.setDefaultYieldMeasUnitTypeCode(model.getDefaultYieldMeasUnitTypeCode());
		dto.setVerifiedYieldUpdateTimestamp(model.getVerifiedYieldUpdateTimestamp());
		dto.setVerifiedYieldUpdateUser(model.getVerifiedYieldUpdateUser());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setGrowerContractYearId(model.getGrowerContractYearId());
		
	}

	@Override
	public void updateDto(
			VerifiedYieldContractCommodityDto dto, 
			VerifiedYieldContractCommodity model, 
			List<ProductDto> productDtos,
			Boolean updateProductValues) {
		
		Double productionGuarantee = model.getProductionGuarantee();
		//Get production guarantee if user wants to update the values
		if(Boolean.TRUE.equals(updateProductValues)) {
			ProductDto product = getProductDto(dto.getCropCommodityId(), dto.getIsPedigreeInd(), productDtos);
			if(product != null && product.getProductStatusCode().equals(PRODUCT_STATUS_FINAL)) {
				productionGuarantee = product.getProductionGuarantee();
			} else {
				productionGuarantee = null;
			}
		}		

		dto.setVerifiedYieldContractCommodityGuid(model.getVerifiedYieldContractCommodityGuid());
		dto.setVerifiedYieldContractGuid(model.getVerifiedYieldContractGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
		dto.setHarvestedAcres(model.getHarvestedAcres());
		dto.setHarvestedAcresOverride(model.getHarvestedAcresOverride());
		dto.setStoredYieldDefaultUnit(model.getStoredYieldDefaultUnit());
		dto.setSoldYieldDefaultUnit(model.getSoldYieldDefaultUnit());
		dto.setProductionGuarantee(productionGuarantee);
		dto.setHarvestedYield(model.getHarvestedYield());
		dto.setHarvestedYieldOverride(model.getHarvestedYieldOverride());
		dto.setYieldPerAcre(model.getYieldPerAcre());
		
	}

	@Override
	public void updateDto(VerifiedYieldAmendmentDto dto, VerifiedYieldAmendment model) {
		dto.setAcres(model.getAcres());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setCropCommodityName(model.getCropCommodityName());
		dto.setFieldId(model.getFieldId());
		dto.setFieldLabel(model.getFieldLabel());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
		dto.setRationale(model.getRationale());
		dto.setVerifiedYieldAmendmentCode(model.getVerifiedYieldAmendmentCode());
		dto.setVerifiedYieldAmendmentGuid(model.getVerifiedYieldAmendmentGuid());
		dto.setVerifiedYieldContractGuid(model.getVerifiedYieldContractGuid());
		dto.setYieldPerAcre(model.getYieldPerAcre());
	}

	@Override
	public void updateDto(VerifiedYieldSummaryDto dto, VerifiedYieldSummary model) {
		dto.setVerifiedYieldSummaryGuid(model.getVerifiedYieldSummaryGuid());
		dto.setVerifiedYieldContractGuid(model.getVerifiedYieldContractGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
		dto.setHarvestedYield(model.getHarvestedYield());
		dto.setHarvestedYieldPerAcre(model.getHarvestedYieldPerAcre());
		dto.setAppraisedYield(model.getAppraisedYield());
		dto.setAssessedYield(model.getAssessedYield());
		dto.setYieldToCount(model.getYieldToCount());
		dto.setYieldPercentPy(model.getYieldPercentPy());
		dto.setProductionGuarantee(model.getProductionGuarantee());
		dto.setProbableYield(model.getProbableYield());
	}
	
	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}
