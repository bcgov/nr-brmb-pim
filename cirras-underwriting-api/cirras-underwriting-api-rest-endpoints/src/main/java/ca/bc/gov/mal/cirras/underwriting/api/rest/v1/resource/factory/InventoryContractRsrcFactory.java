package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractListRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.InventoryContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryContractList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryCoverageTotalForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventorySeededGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.InventoryUnseeded;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LinkedPlanting;
import ca.bc.gov.mal.cirras.underwriting.model.v1.PolicySimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityBerriesDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryCoverageTotalForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryUnseededDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.InventoryContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.InventoryContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;

public class InventoryContractRsrcFactory extends BaseResourceFactory implements InventoryContractFactory {

	@Override
	public InventoryContract<? extends AnnualField> getInventoryContract(
			InventoryContractDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {

		InventoryContractRsrc resource = new InventoryContractRsrc();

		populateResource(resource, dto);

		// Commodities
		if (!dto.getCommodities().isEmpty()) {
			List<InventoryContractCommodity> commodities = new ArrayList<InventoryContractCommodity>();

			for (InventoryContractCommodityDto iccDto : dto.getCommodities()) {
				InventoryContractCommodity iccModel = createInventoryContractCommodity(iccDto);
				commodities.add(iccModel);
			}

			resource.setCommodities(commodities);
		}

		// Inventory Coverage Total Forage
		if (!dto.getInventoryCoverageTotalForages().isEmpty()) {
			List<InventoryCoverageTotalForage> inventoryCoverageTotalForages = new ArrayList<InventoryCoverageTotalForage>();

			for (InventoryCoverageTotalForageDto ictfDto : dto.getInventoryCoverageTotalForages()) {
				InventoryCoverageTotalForage ictfModel = createInventoryCoverageTotalForage(ictfDto);
				inventoryCoverageTotalForages.add(ictfModel);
			}

			resource.setInventoryCoverageTotalForages(inventoryCoverageTotalForages);
		}
		
		// Berries Commodities
		if (!dto.getInventoryContractCommodityBerries().isEmpty()) {
			List<InventoryContractCommodityBerries> invContractCommodityBerries = new ArrayList<InventoryContractCommodityBerries>();

			for (InventoryContractCommodityBerriesDto iccbDto : dto.getInventoryContractCommodityBerries()) {
				InventoryContractCommodityBerries iccbModel = createInventoryContractCommodityBerries(iccbDto);
				invContractCommodityBerries.add(iccbModel);
			}

			resource.setInventoryContractCommodityBerries(invContractCommodityBerries);
		}
		
		// Fields
		if (!dto.getFields().isEmpty()) {
			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

			for (ContractedFieldDetailDto cfdDto : dto.getFields()) {
				AnnualFieldRsrc afModel = createAnnualField(cfdDto, authentication);
				fields.add(afModel);
			}

			resource.setFields(fields);
		}
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getInventoryContractGuid(), resource, baseUri);
		setLinks(dto.getInventoryContractGuid(), resource, baseUri, authentication);

		return resource;

	}

	
	private void populateResource(InventoryContractRsrc resource, InventoryContractDto dto) {

		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setFertilizerInd(dto.getFertilizerInd());
		resource.setGrainFromPrevYearInd(dto.getGrainFromPrevYearInd());
		resource.setInvUpdateTimestamp(dto.getInvUpdateTimestamp());
		resource.setInvUpdateUser(dto.getInvUpdateUser());
		resource.setHerbicideInd(dto.getHerbicideInd());
		resource.setInventoryContractGuid(dto.getInventoryContractGuid());
		resource.setOtherChangesComment(dto.getOtherChangesComment());
		resource.setOtherChangesInd(dto.getOtherChangesInd());
		resource.setSeededCropReportSubmittedInd(dto.getSeededCropReportSubmittedInd());
		resource.setTilliageInd(dto.getTilliageInd());
		resource.setUnseededIntentionsSubmittedInd(dto.getUnseededIntentionsSubmittedInd());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setInsurancePlanName(dto.getInsurancePlanName());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setGrowerNumber(dto.getGrowerNumber());
		resource.setGrowerName(dto.getGrowerName());

	}
	
	@Override
	public InventoryContractList<? extends InventoryContract<? extends AnnualField>> getInventoryContractList(
			List<InventoryContractDto> inventoryContractDtos, 
			Integer cropYear, 
			Integer insurancePlanId,
			Integer officeId, 
			String policyStatusCode, 
			String policyNumber, 
			String growerInfo, 
			String sortColumn,
			String inventoryContractGuids,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException {
		
		URI baseUri = getBaseURI(context);
		
		InventoryContractListRsrc result = null;
		
		List<InventoryContractRsrc> resources = new ArrayList<InventoryContractRsrc>();

		for (InventoryContractDto dto : inventoryContractDtos) {
			//InventoryContractRsrc resource = new InventoryContractRsrc();
			//populateResource(resource, dto);

			InventoryContractRsrc resource = (InventoryContractRsrc)getInventoryContract(dto, context, authentication);

			resources.add(resource);
		}

		int pageNumber = 1;
		int totalRowCount = inventoryContractDtos.size();
		int pageRowCount = totalRowCount;
		int totalPageCount = 1;
		
		result = new InventoryContractListRsrc();
		result.setCollection(resources);
		result.setPageNumber(pageNumber);
		result.setPageRowCount(pageRowCount);
		result.setTotalRowCount(totalRowCount);
		result.setTotalPageCount(totalPageCount);

		setInventoryContractListSelfLink(
				result,
				cropYear,
				insurancePlanId,
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				sortColumn,
				inventoryContractGuids,
				baseUri);
		
		return result;
	}	

	private InventoryContractCommodity createInventoryContractCommodity(InventoryContractCommodityDto dto) {
		
		InventoryContractCommodity model = new InventoryContractCommodity();

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setInventoryContractCommodityGuid(dto.getInventoryContractCommodityGuid());
		model.setInventoryContractGuid(dto.getInventoryContractGuid());
		model.setTotalSeededAcres(dto.getTotalSeededAcres());
		model.setTotalSpotLossAcres(dto.getTotalSpotLossAcres());
		model.setTotalUnseededAcres(dto.getTotalUnseededAcres());
		model.setTotalUnseededAcresOverride(dto.getTotalUnseededAcresOverride());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		
		return model;
	}

	private InventoryContractCommodityBerries createInventoryContractCommodityBerries(InventoryContractCommodityBerriesDto dto) {
		
		InventoryContractCommodityBerries model = new InventoryContractCommodityBerries();
		
		model.setInventoryContractCommodityBerriesGuid(dto.getInventoryContractCommodityBerriesGuid());
		model.setInventoryContractGuid(dto.getInventoryContractGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setTotalInsuredPlants(dto.getTotalInsuredPlants());
		model.setTotalUninsuredPlants(dto.getTotalUninsuredPlants());
		model.setTotalQuantityInsuredAcres(dto.getTotalQuantityInsuredAcres());
		model.setTotalQuantityUninsuredAcres(dto.getTotalQuantityUninsuredAcres());
		model.setTotalPlantInsuredAcres(dto.getTotalPlantInsuredAcres());
		model.setTotalPlantUninsuredAcres(dto.getTotalPlantUninsuredAcres());

		return model;
	}

	private InventoryCoverageTotalForage createInventoryCoverageTotalForage(InventoryCoverageTotalForageDto dto) {
		
		InventoryCoverageTotalForage model = new InventoryCoverageTotalForage();
		
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setInventoryContractGuid(dto.getInventoryContractGuid());
		model.setInventoryCoverageTotalForageGuid(dto.getInventoryCoverageTotalForageGuid());
		model.setPlantInsurabilityTypeCode(dto.getPlantInsurabilityTypeCode());
		model.setPlantInsurabilityTypeDesc(dto.getPlantInsurabilityTypeDesc());
		model.setTotalFieldAcres(dto.getTotalFieldAcres());
		model.setIsUnseededInsurableInd(dto.getIsUnseededInsurableInd());

		return model;
	}
	
	@Override
	public AnnualFieldRsrc createAnnualField(ContractedFieldDetailDto dto, WebAdeAuthentication authentication) {
		AnnualFieldRsrc model = new AnnualFieldRsrc();

		AnnualFieldRsrcFactory.populateResource(model, dto);
		
		// Plantings
		if (!dto.getPlantings().isEmpty()) {
			List<InventoryField> plantings = new ArrayList<InventoryField>();

			for (InventoryFieldDto ifDto : dto.getPlantings()) {
				InventoryField ifModel = createInventoryField(ifDto);
				plantings.add(ifModel);
			}

			model.setPlantings(plantings);
		}
		
		// Underwriting Comments
		loadComments(dto, authentication, model);
		
		// Associated/Linked Policies
		addAssociatedPolicies(dto, model);

		return model;
	}


	public void addAssociatedPolicies(ContractedFieldDetailDto dto, AnnualFieldRsrc model) {
		
		// Associated/Linked Policies
		if(dto.getPolicies() != null && !dto.getPolicies().isEmpty()) {
			List<PolicySimple> policies = new ArrayList<PolicySimple>();

			for (PolicyDto pDto : dto.getPolicies()) {
				PolicySimple policyModel = AnnualFieldRsrcFactory.createPolicy(pDto, dto.getCropYear());
				policies.add(policyModel);
			}

			model.setPolicies(policies);
		}
	}

	public void loadComments(ContractedFieldDetailDto dto, WebAdeAuthentication authentication, AnnualFieldRsrc model) {
		if (dto.getUwComments() != null && !dto.getUwComments().isEmpty()) {
			List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

			for (UnderwritingCommentDto ucDto : dto.getUwComments()) {
				UnderwritingComment ucModel = createUnderwritingComment(ucDto, authentication);
				uwComments.add(ucModel);
			}

			model.setUwComments(uwComments);
		}
	}

	private InventoryField createInventoryField(InventoryFieldDto dto) {
		InventoryField model = new InventoryField();

		model.setCropYear(dto.getCropYear());
		model.setFieldId(dto.getFieldId());
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setLastYearCropCommodityId(dto.getLastYearCropCommodityId());
		model.setLastYearCropCommodityName(dto.getLastYearCropCommodityName());
		model.setLastYearCropVarietyId(dto.getLastYearCropVarietyId());
		model.setLastYearCropVarietyName(dto.getLastYearCropVarietyName());
		model.setPlantingNumber(dto.getPlantingNumber());
		model.setIsHiddenOnPrintoutInd(dto.getIsHiddenOnPrintoutInd());
		model.setUnderseededCropVarietyId(dto.getUnderseededCropVarietyId());
		model.setUnderseededCropVarietyName(dto.getUnderseededCropVarietyName());
		model.setUnderseededAcres(dto.getUnderseededAcres());
		model.setUnderseededInventorySeededForageGuid(dto.getUnderseededInventorySeededForageGuid());

		// InventoryUnseeded
		if (dto.getInventoryUnseeded() != null) {
			model.setInventoryUnseeded(createInventoryUnseeded(dto.getInventoryUnseeded()));
		}

		// InventorySeededGrains
		if (!dto.getInventorySeededGrains().isEmpty()) {
			List<InventorySeededGrain> inventorySeededGrains = new ArrayList<InventorySeededGrain>();

			for (InventorySeededGrainDto isgDto : dto.getInventorySeededGrains()) {
				InventorySeededGrain isgModel = createInventorySeededGrain(isgDto);
				inventorySeededGrains.add(isgModel);
			}

			model.setInventorySeededGrains(inventorySeededGrains);
		}
		
		// InventorySeededForages
		if (!dto.getInventorySeededForages().isEmpty()) {
			List<InventorySeededForage> inventorySeededForages = new ArrayList<InventorySeededForage>();

			for (InventorySeededForageDto isfDto : dto.getInventorySeededForages()) {
				InventorySeededForage isfModel = createInventorySeededForage(isfDto);
				inventorySeededForages.add(isfModel);
			}

			model.setInventorySeededForages(inventorySeededForages);
		}
		
		// Inventory Berries
		if (dto.getInventoryBerries() != null) {
			model.setInventoryBerries(createInventoryBerries(dto.getInventoryBerries()));
		}
		
		if(dto.getUnderseededInventorySeededForageGuid() != null) {
			LinkedPlanting linkedPlanting = new LinkedPlanting();
			linkedPlanting.setUnderseededInventorySeededForageGuid(dto.getUnderseededInventorySeededForageGuid());
			linkedPlanting.setCropVarietyId(dto.getLinkedCropVarietyId());
			linkedPlanting.setVarietyName(dto.getLinkedVarietyName());
			linkedPlanting.setAcres(dto.getLinkedFieldAcres());
			model.setLinkedPlanting(linkedPlanting);
		}
		
		return model;
	}

	private InventoryUnseeded createInventoryUnseeded(InventoryUnseededDto dto) {
		InventoryUnseeded model = new InventoryUnseeded();

		model.setAcresToBeSeeded(dto.getAcresToBeSeeded());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setCropVarietyName(dto.getCropVarietyName());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setInventoryUnseededGuid(dto.getInventoryUnseededGuid());
		model.setIsUnseededInsurableInd(dto.getIsUnseededInsurableInd());
		model.setIsCropInsuranceEligibleInd(dto.getIsCropInsuranceEligibleInd());
		model.setIsInventoryCropInd(dto.getIsInventoryCropInd());

		return model;
	}
	
	private InventorySeededGrain createInventorySeededGrain(InventorySeededGrainDto dto) {
		
		InventorySeededGrain model = new InventorySeededGrain();

		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setCommodityTypeDesc(dto.getCommodityTypeDesc());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setCropVarietyName(dto.getCropVarietyName());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setInventorySeededGrainGuid(dto.getInventorySeededGrainGuid());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setIsSpotLossInsurableInd(dto.getIsSpotLossInsurableInd());
		model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		model.setIsReplacedInd(dto.getIsReplacedInd());
		model.setSeededAcres(dto.getSeededAcres());
		model.setSeededDate(dto.getSeededDate());

		return model;
	}
	
	private InventorySeededForage createInventorySeededForage(InventorySeededForageDto dto) {
		
		InventorySeededForage model = new InventorySeededForage();
		
		model.setInventorySeededForageGuid(dto.getInventorySeededForageGuid());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setCropVarietyName(dto.getCropVarietyName());
		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setFieldAcres(dto.getFieldAcres());
		model.setSeedingYear(dto.getSeedingYear());
		model.setSeedingDate(dto.getSeedingDate());
		model.setIsIrrigatedInd(dto.getIsIrrigatedInd());
		model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		model.setPlantInsurabilityTypeCode(dto.getPlantInsurabilityTypeCode());
		model.setIsAwpEligibleInd(dto.getIsAwpEligibleInd());
		
		if(dto.getLinkedInventoryFieldGuid() != null) {
			LinkedPlanting linkedPlanting = new LinkedPlanting();
			linkedPlanting.setUnderseededInventorySeededForageGuid(null);
			linkedPlanting.setInventoryFieldGuid(dto.getLinkedInventoryFieldGuid());
			linkedPlanting.setCropVarietyId(dto.getLinkedUnderseededCropVarietyId());
			linkedPlanting.setVarietyName(dto.getLinkedVarietyName());
			linkedPlanting.setAcres(dto.getLinkedUnderseededAcres());
			model.setLinkedPlanting(linkedPlanting);
		}

		return model;
	}
	
	private InventoryBerries createInventoryBerries(InventoryBerriesDto dto) {
		InventoryBerries model = new InventoryBerries();

		model.setInventoryBerriesGuid(dto.getInventoryBerriesGuid());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setCropVarietyName(dto.getCropVarietyName());
		model.setPlantInsurabilityTypeCode(dto.getPlantInsurabilityTypeCode());
		model.setPlantedYear(dto.getPlantedYear());
		model.setPlantedAcres(dto.getPlantedAcres());
		model.setRowSpacing(dto.getRowSpacing());
		model.setPlantSpacing(dto.getPlantSpacing());
		model.setTotalPlants(dto.getTotalPlants());
		model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		model.setIsPlantInsurableInd(dto.getIsPlantInsurableInd());
		model.setBogId(dto.getBogId());
		model.setBogMowedDate(dto.getBogMowedDate());
		model.setBogRenovatedDate(dto.getBogRenovatedDate());
		model.setIsHarvestedInd(dto.getIsHarvestedInd());

		return model;
	}
	
	static UnderwritingComment createUnderwritingComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication) {
		
		Boolean userCanEdit = userCanEditComment(dto, authentication);
		Boolean userCanDelete = userCanEdit || userCanDeleteAllComments(authentication);
				
		UnderwritingComment model = new UnderwritingComment();

		model.setCreateDate(dto.getCreateDate());
		model.setCreateUser(dto.getCreateUser());
		model.setAnnualFieldDetailId(dto.getAnnualFieldDetailId());
		model.setUnderwritingComment(dto.getUnderwritingComment());
		model.setUnderwritingCommentGuid(dto.getUnderwritingCommentGuid());
		model.setUnderwritingCommentTypeCode(dto.getUnderwritingCommentTypeCode());
		model.setUnderwritingCommentTypeDesc(dto.getUnderwritingCommentTypeDesc());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setVerifiedYieldSummaryGuid(dto.getVerifiedYieldSummaryGuid());
		model.setGrowerContractYearId(dto.getGrowerContractYearId());
		model.setUpdateDate(dto.getUpdateDate());
		model.setUpdateUser(dto.getUpdateUser());
		model.setUserCanEditInd(userCanEdit);
		model.setUserCanDeleteInd(userCanDelete);
		
		return model;
	}
	
	@Override
	public InventoryContract<? extends AnnualField> createRolloverInventoryContract(
			PolicyDto policyDto,
			List<AnnualFieldRsrc> fields,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {

		InventoryContractRsrc resource = new InventoryContractRsrc();

		populateDefaultResource(resource, policyDto);
		resource.setFields(fields);

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}
	
//	@Override
//	public InventoryContract<? extends AnnualField> createDefaultInventoryContract(
//			PolicyDto policyDto,
//			List<ContractedFieldDetailDto> fieldDtos, 
//			FactoryContext context, 
//			WebAdeAuthentication authentication
//		) throws FactoryException {
//
//		InventoryContractRsrc resource = new InventoryContractRsrc();
//
//		populateDefaultResource(resource, policyDto);
//
//		// Fields
//		if ( fieldDtos != null && !fieldDtos.isEmpty()) {
//			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();
//
//			for (ContractedFieldDetailDto cfdDto : fieldDtos) {
//				AnnualFieldRsrc afModel = createDefaultAnnualField(cfdDto);
//				fields.add(afModel);
//			}
//
//			resource.setFields(fields);
//		}
//		
//		String eTag = getEtag(resource);
//		resource.setETag(eTag);
//
//		return resource;
//	}

	@Override
	public AnnualFieldRsrc addRolloverAnnualField(Integer insurancePlanId, ContractedFieldDetailDto dto, WebAdeAuthentication authentication) {
		
		AnnualFieldRsrc model = new AnnualFieldRsrc();

		AnnualFieldRsrcFactory.populateResource(model, dto);
		
		List<InventoryField> plantings = new ArrayList<InventoryField>();
		InventoryField ifModel = null;
		
		// Rollover plantings
		if ( dto.getPlantings() != null && !dto.getPlantings().isEmpty()) {
			for (InventoryFieldDto ifDto : dto.getPlantings()) {
				ifModel = createRolloverInventoryField(insurancePlanId, dto, ifDto);
				plantings.add(ifModel);
			}
		}
		model.setPlantings(plantings);
		
		// Underwriting Comments
		loadComments(dto, authentication, model);
		
		// Associated/Linked Policies
		addAssociatedPolicies(dto, model);
		
		return model;
	}	

//
//	@Override
//	public void addRolloverAnnualFieldDto(ContractedFieldDetailDto dto) {
//		
//		//AnnualFieldRsrc model = new AnnualFieldRsrc();
//
//		//AnnualFieldRsrcFactory.populateResource(model, dto);
//		
//		List<InventoryField> plantings = new ArrayList<InventoryField>();
//		InventoryField ifModel = null;
//		
//		// Create plantings
//		if ( dto.getPlantings() != null && !dto.getPlantings().isEmpty()) {
//			for (InventoryFieldDto ifDto : dto.getPlantings()) {
//				for (InventorySeededGrainDto isgDto : ifDto.getInventorySeededGrains())
//				ifModel = createRolloverInventoryField(dto, ifDto, isgDto);
//				plantings.add(ifModel);
//			}
//		}
//		model.setPlantings(plantings);
//		
//		//return model;
//	}		

	private void populateDefaultResource(InventoryContractRsrc resource, PolicyDto dto) {

		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setInsurancePlanName(dto.getInsurancePlanName());
		resource.setFertilizerInd(false);
		resource.setGrainFromPrevYearInd(null);
		resource.setHerbicideInd(false);
		resource.setInventoryContractGuid(null);
		resource.setOtherChangesComment(null);
		resource.setOtherChangesInd(false);
		resource.setSeededCropReportSubmittedInd(false);
		resource.setTilliageInd(false);
		resource.setUnseededIntentionsSubmittedInd(false);

	}

	@Override
	public AnnualFieldRsrc createDefaultAnnualField(Integer insurancePlanId, ContractedFieldDetailDto dto, WebAdeAuthentication authentication) {
		AnnualFieldRsrc model = new AnnualFieldRsrc();

		AnnualFieldRsrcFactory.populateResource(model, dto);
		
		// Create one default planting.
		List<InventoryField> plantings = new ArrayList<InventoryField>();

		InventoryField ifModel = createDefaultInventoryField(insurancePlanId, dto);
		plantings.add(ifModel);

		model.setPlantings(plantings);
		
		// Underwriting Comments
		loadComments(dto, authentication, model);
		
		// Associated/Linked Policies
		addAssociatedPolicies(dto, model);
		
		return model;
	}
	
	private InventoryField createRolloverInventoryField(Integer insurancePlanId, ContractedFieldDetailDto cfdDto, InventoryFieldDto ifDto) {
		InventoryField model = new InventoryField();

		model.setCropYear(cfdDto.getCropYear());
		model.setFieldId(cfdDto.getFieldId());
		model.setInsurancePlanId(cfdDto.getInsurancePlanId());
		model.setInventoryFieldGuid(null);
		model.setLastYearCropCommodityId(ifDto.getLastYearCropCommodityId());
		model.setLastYearCropCommodityName(ifDto.getLastYearCropCommodityName());
		model.setLastYearCropVarietyId(ifDto.getLastYearCropVarietyId());
		model.setLastYearCropVarietyName(ifDto.getLastYearCropVarietyName());
		model.setPlantingNumber(ifDto.getPlantingNumber());
		model.setIsHiddenOnPrintoutInd(ifDto.getIsHiddenOnPrintoutInd());
		model.setUnderseededCropVarietyId(null);
		model.setUnderseededCropVarietyName(null);
		model.setUnderseededAcres(null);
		model.setUnderseededInventorySeededForageGuid(null);
		

		// InventoryUnseeded
		// InventoryUnseeded Grain
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
			model.setInventoryUnseeded(createRolloverInventoryUnseeded(ifDto.getAcresToBeSeeded(), ifDto.getLastYearCropVarietyId(), ifDto.getIsGrainUnseededDefaultInd()));
		}

		//Inventory seeded Forage
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
			
			List<InventorySeededForage> inventorySeededForageList = new ArrayList<InventorySeededForage>();
			if ( ifDto.getInventorySeededForages() != null && ifDto.getInventorySeededForages().size() > 0 ) {
				InventorySeededForageDto isfDto = ifDto.getInventorySeededForages().get(0);
				inventorySeededForageList.add(createRolloverInventorySeededForage(isfDto));
			} else {
				inventorySeededForageList.add(createDefaultInventorySeededForage());
			}
			
			model.setInventorySeededForages(inventorySeededForageList);
		}

		// Inventory Berries
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
			if ( ifDto.getInventoryBerries() != null) {
				model.setInventoryBerries(createRolloverInventoryBerries(ifDto.getInventoryBerries(), cfdDto.getCropYear()));
			} else {
				model.setInventoryBerries(createDefaultInventoryBerries());
			}
		}

		return model;
	}

	private InventoryBerries createRolloverInventoryBerries(InventoryBerriesDto dto, Integer cropYear) {
		
		InventoryBerries model = new InventoryBerries();
		
		//Rollover insurability for STRAWBERRY works different than other commodities
		if(dto.getCropCommodityId() != null && dto.getCropCommodityId().equals(13) && dto.getPlantInsurabilityTypeCode() != null) {
			if (dto.getPlantInsurabilityTypeCode().equalsIgnoreCase("ST1")) {
				//Strawberries that were previously insured with ST1 (Strawberry Year 1) will now be ST2 (Strawberry Year 2)
				dto.setPlantInsurabilityTypeCode("ST2");
				dto.setIsPlantInsurableInd(true); //Should already be set to true
			} else if (dto.getPlantInsurabilityTypeCode().equalsIgnoreCase("ST2")) {
				//Strawberries that were previously insured with ST2 (Strawberry Year 2) will become uninsurable and set to null
				dto.setPlantInsurabilityTypeCode(null);
				dto.setIsPlantInsurableInd(false);
			}
		}

		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setCropVarietyName(dto.getCropVarietyName());
		model.setPlantInsurabilityTypeCode(dto.getPlantInsurabilityTypeCode());
		model.setPlantedYear(dto.getPlantedYear());
		model.setPlantedAcres(dto.getPlantedAcres());
		model.setRowSpacing(dto.getRowSpacing());
		model.setPlantSpacing(dto.getPlantSpacing());
		model.setTotalPlants(dto.getTotalPlants());
		model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());
		model.setIsPlantInsurableInd(dto.getIsPlantInsurableInd());
		model.setBogId(dto.getBogId());
		model.setBogMowedDate(dto.getBogMowedDate());
		model.setBogRenovatedDate(dto.getBogRenovatedDate());
		model.setIsHarvestedInd(dto.getIsHarvestedInd());

		return model;
	}


	private InventoryField createDefaultInventoryField(Integer insurancePlanId, ContractedFieldDetailDto dto) {
		InventoryField model = new InventoryField();

		model.setCropYear(dto.getCropYear());
		model.setFieldId(dto.getFieldId());
		model.setInsurancePlanId(dto.getInsurancePlanId());
		model.setInventoryFieldGuid(null);
		model.setLastYearCropCommodityId(null);
		model.setLastYearCropCommodityName(null);
		model.setLastYearCropVarietyId(null);
		model.setLastYearCropVarietyName(null);
		model.setPlantingNumber(1);
		model.setIsHiddenOnPrintoutInd(false);
		model.setUnderseededCropVarietyId(null);
		model.setUnderseededCropVarietyName(null);
		model.setUnderseededAcres(null);
		model.setUnderseededInventorySeededForageGuid(null);

		// InventoryUnseeded Grain
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.GRAIN.getInsurancePlanId())) {
			model.setInventoryUnseeded(createRolloverInventoryUnseeded(null, null, null));
		}
		
		//Inventory seeded Forage
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.FORAGE.getInsurancePlanId())) {
			List<InventorySeededForage> inventorySeededForageList = new ArrayList<InventorySeededForage>();
			inventorySeededForageList.add(createDefaultInventorySeededForage());
			model.setInventorySeededForages(inventorySeededForageList);
		}
		
		// InventoryBerries
		if (insurancePlanId.equals(InventoryServiceEnums.InsurancePlans.BERRIES.getInsurancePlanId())) {
			model.setInventoryBerries(createDefaultInventoryBerries());
		}
		
		return model;
	}

	private InventorySeededForage createRolloverInventorySeededForage(InventorySeededForageDto dto) {
		InventorySeededForage model = new InventorySeededForage();

		if ( "PERENNIAL".equals(dto.getCommodityTypeCode()) || "Pasture".equals(dto.getCommodityTypeCode()) || "Native Wetland".equals(dto.getCommodityTypeCode()) || "Forage Seed".equals(dto.getCommodityTypeCode())) {
			model.setCropCommodityId(dto.getCropCommodityId());
			model.setCropVarietyId(dto.getCropVarietyId());
			model.setCropVarietyName(dto.getCropVarietyName());
			model.setCommodityTypeCode(dto.getCommodityTypeCode());
			model.setSeedingYear(dto.getSeedingYear());
			model.setIsQuantityInsurableInd(dto.getIsQuantityInsurableInd());

			// Calculate new Plant Insurability
			String prevPlanIns = dto.getPlantInsurabilityTypeCode();
			String rolloverPlantIns = null;
			
			if ("E1".equals(prevPlanIns) ) {
				if ( Boolean.TRUE.equals(dto.getIsIrrigatedInd()) ) {
					rolloverPlantIns = "W1";
				} else {
					rolloverPlantIns = "E2";					
				}
			}
			else if ("E2".equals(prevPlanIns) ) {
				rolloverPlantIns = null;
			}
			else if ("W1".equals(prevPlanIns) ) {
				rolloverPlantIns = "W2";
			}
			else if ("W2".equals(prevPlanIns) ) {
				rolloverPlantIns = "W3";
			}
			else if ("W3".equals(prevPlanIns) ) {
				rolloverPlantIns = null;
			}

			model.setPlantInsurabilityTypeCode(rolloverPlantIns);		
		
		} else {
			model.setCropCommodityId(null);
			model.setCropVarietyId(null);
			model.setCropVarietyName(null);
			model.setCommodityTypeCode(null);
			model.setSeedingYear(null);
			model.setIsQuantityInsurableInd(false);
			model.setPlantInsurabilityTypeCode(null);
		}

		model.setFieldAcres(dto.getFieldAcres());
		model.setSeedingDate(null);                                   // Does not rollover.
		model.setIsIrrigatedInd(dto.getIsIrrigatedInd());
		model.setIsAwpEligibleInd(dto.getIsAwpEligibleInd());
		
		return model;
	}

	private InventorySeededForage createDefaultInventorySeededForage() {
		InventorySeededForage model = new InventorySeededForage();

		model.setCropCommodityId(null);
		model.setCropVarietyId(null);
		model.setCropVarietyName(null);
		model.setCommodityTypeCode(null);
		model.setFieldAcres(null);
		model.setSeedingYear(null);
		model.setSeedingDate(null);
		model.setIsIrrigatedInd(null);
		model.setIsQuantityInsurableInd(null);
		model.setPlantInsurabilityTypeCode(null);
		model.setIsAwpEligibleInd(true);             // Default true.

		return model;
	}
	
	
	private InventoryUnseeded createRolloverInventoryUnseeded(Double acresToBeSeeded, Integer lastYearVarietyId, Boolean isGrainUnseededDefault) {
		InventoryUnseeded model = new InventoryUnseeded();

		model.setAcresToBeSeeded(acresToBeSeeded);
		model.setCropCommodityId(null);
		model.setCropCommodityName(null);
		model.setInventoryFieldGuid(null);
		model.setInventoryUnseededGuid(null);
		if(lastYearVarietyId == null || isGrainUnseededDefault == null) {
			model.setIsUnseededInsurableInd(true); //Default
		} else {
			model.setIsUnseededInsurableInd(isGrainUnseededDefault);
		}

		return model;
	}
	
	private InventoryBerries createDefaultInventoryBerries() {
		InventoryBerries model = new InventoryBerries();

		model.setInventoryBerriesGuid(null);
		model.setInventoryFieldGuid(null);
		model.setCropCommodityId(null);
		model.setCropVarietyId(null);
		model.setPlantInsurabilityTypeCode(null);
		model.setPlantedYear(null);
		model.setPlantedAcres(null);
		model.setRowSpacing(null);
		model.setPlantSpacing(null);
		model.setTotalPlants(null);
		model.setIsQuantityInsurableInd(null);
		model.setIsPlantInsurableInd(null);
		model.setBogId(null);
		model.setBogMowedDate(null);
		model.setBogRenovatedDate(null);
		model.setIsHarvestedInd(false);

		return model;
	}

	@Override
	public void updateDto(InventoryContractDto dto, InventoryContract<? extends AnnualField> model, String userId) {
		dto.setContractId(model.getContractId());
		dto.setCropYear(model.getCropYear());
		dto.setFertilizerInd(model.getFertilizerInd());
		dto.setGrainFromPrevYearInd(model.getGrainFromPrevYearInd());
		dto.setInvUpdateTimestamp(new Date()); //Need to set the date to make sure the record is identified as dirty and is updated
		dto.setInvUpdateUser(userId);
		dto.setHerbicideInd(model.getHerbicideInd());
		dto.setOtherChangesComment(model.getOtherChangesComment());
		dto.setOtherChangesInd(model.getOtherChangesInd());
		dto.setSeededCropReportSubmittedInd(model.getSeededCropReportSubmittedInd());
		dto.setTilliageInd(model.getTilliageInd());
		dto.setUnseededIntentionsSubmittedInd(model.getUnseededIntentionsSubmittedInd());
	}

	@Override
	public void updateDto(InventoryContractCommodityDto dto, InventoryContractCommodity model) {
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setCropCommodityName(model.getCropCommodityName());
		dto.setTotalSeededAcres(model.getTotalSeededAcres());
		dto.setTotalSpotLossAcres(model.getTotalSpotLossAcres());
		dto.setTotalUnseededAcres(model.getTotalUnseededAcres());
		dto.setTotalUnseededAcresOverride(model.getTotalUnseededAcresOverride());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
	}

	@Override
	public void updateDto(InventoryContractCommodityBerriesDto dto, InventoryContractCommodityBerries model) {
		dto.setInventoryContractGuid(model.getInventoryContractGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setTotalInsuredPlants(model.getTotalInsuredPlants());
		dto.setTotalUninsuredPlants(model.getTotalUninsuredPlants());
		dto.setTotalQuantityInsuredAcres(model.getTotalQuantityInsuredAcres());
		dto.setTotalQuantityUninsuredAcres(model.getTotalQuantityUninsuredAcres());
		dto.setTotalPlantInsuredAcres(model.getTotalPlantInsuredAcres());
		dto.setTotalPlantUninsuredAcres(model.getTotalPlantUninsuredAcres());
	}

	@Override
	public void updateDto(InventoryCoverageTotalForageDto dto, InventoryCoverageTotalForage model) {
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setCropCommodityName(model.getCropCommodityName());
		dto.setPlantInsurabilityTypeCode(model.getPlantInsurabilityTypeCode());
		dto.setPlantInsurabilityTypeDesc(model.getPlantInsurabilityTypeDesc());
		dto.setTotalFieldAcres(model.getTotalFieldAcres());
		dto.setIsUnseededInsurableInd(model.getIsUnseededInsurableInd());
	}
	
	@Override
	public void updateDto(InventoryFieldDto dto, InventoryField model) {
		dto.setCropYear(model.getCropYear());
		dto.setFieldId(model.getFieldId());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setLastYearCropCommodityId(model.getLastYearCropCommodityId());
		dto.setLastYearCropCommodityName(model.getLastYearCropCommodityName());
		dto.setLastYearCropVarietyId(model.getLastYearCropVarietyId());
		dto.setLastYearCropVarietyName(model.getLastYearCropVarietyName());
		dto.setPlantingNumber(model.getPlantingNumber());
		dto.setIsHiddenOnPrintoutInd(model.getIsHiddenOnPrintoutInd());
		dto.setUnderseededCropVarietyId(model.getUnderseededCropVarietyId());
		dto.setUnderseededCropVarietyName(model.getUnderseededCropVarietyName());
		dto.setUnderseededAcres(model.getUnderseededAcres());
		dto.setUnderseededInventorySeededForageGuid(model.getUnderseededInventorySeededForageGuid());
	}

	@Override
	public void updateDto(InventoryUnseededDto dto, InventoryUnseeded model) {

		if (Boolean.TRUE.equals(model.getDeletedByUserInd())) {

			// Flagged for deletion, but record could not be deleted, so clear the user-entered data.
			dto.setAcresToBeSeeded(null);
			dto.setCropCommodityId(null);
			dto.setCropCommodityName(null);
			dto.setCropVarietyId(null);
			dto.setCropVarietyName(null);
			dto.setIsUnseededInsurableInd(true); // Defaults to true.

		} else {		
			dto.setAcresToBeSeeded(model.getAcresToBeSeeded());
			dto.setCropCommodityId(model.getCropCommodityId());
			dto.setCropCommodityName(model.getCropCommodityName());
			dto.setCropVarietyId(model.getCropVarietyId());
			dto.setCropVarietyName(model.getCropVarietyName());
			dto.setIsUnseededInsurableInd(model.getIsUnseededInsurableInd());
		}	
	}
	
	@Override
	public void updateDto(InventorySeededGrainDto dto, InventorySeededGrain model) {

		if (Boolean.TRUE.equals(model.getDeletedByUserInd())) {
			
			// Flagged for deletion, but record could not be deleted, so clear the user-entered data.
			dto.setCommodityTypeCode(null);
			dto.setCommodityTypeDesc(null);
			dto.setCropCommodityId(null);
			dto.setCropCommodityName(null);
			dto.setCropVarietyId(null);
			dto.setCropVarietyName(null);
			dto.setIsPedigreeInd(false);           // Default
			dto.setIsQuantityInsurableInd(false);  // Default
			dto.setIsReplacedInd(false);           // Default
			dto.setIsSpotLossInsurableInd(false);  // Default
			dto.setSeededAcres(null);
			dto.setSeededDate(null);
			
		} else {
		
			dto.setCommodityTypeCode(model.getCommodityTypeCode());
			dto.setCommodityTypeDesc(model.getCommodityTypeDesc());
			dto.setCropCommodityId(model.getCropCommodityId());
			dto.setCropCommodityName(model.getCropCommodityName());
			dto.setCropVarietyId(model.getCropVarietyId());
			dto.setCropVarietyName(model.getCropVarietyName());
			dto.setIsPedigreeInd(model.getIsPedigreeInd());
			dto.setIsSpotLossInsurableInd(model.getIsSpotLossInsurableInd());
			dto.setIsQuantityInsurableInd(model.getIsQuantityInsurableInd());
			dto.setIsReplacedInd(model.getIsReplacedInd());
			dto.setSeededAcres(model.getSeededAcres());
			dto.setSeededDate(model.getSeededDate());
		}
	}
	
	@Override
	public void updateDto(InventorySeededForageDto dto, InventorySeededForage model) {

		if (Boolean.TRUE.equals(model.getDeletedByUserInd())) {
			
			// Flagged for deletion, but record could not be deleted, so clear the user-entered data.
			
			dto.setCropCommodityId(null);
			dto.setCropVarietyId(null);
			dto.setCommodityTypeCode(null);
			dto.setFieldAcres(null);
			dto.setSeedingYear(null);
			dto.setSeedingDate(null);
			dto.setIsIrrigatedInd(false);
			dto.setIsQuantityInsurableInd(false);
			dto.setPlantInsurabilityTypeCode(null);
			dto.setIsAwpEligibleInd(true); //Default

			
		} else {
		
			dto.setCropCommodityId(model.getCropCommodityId());
			dto.setCropVarietyId(model.getCropVarietyId());
			dto.setCropVarietyName(model.getCropVarietyName());
			dto.setCommodityTypeCode(model.getCommodityTypeCode());
			dto.setFieldAcres(model.getFieldAcres());
			dto.setSeedingYear(model.getSeedingYear());
			dto.setSeedingDate(model.getSeedingDate());
			dto.setIsIrrigatedInd(model.getIsIrrigatedInd());
			dto.setIsQuantityInsurableInd(model.getIsQuantityInsurableInd());
			dto.setPlantInsurabilityTypeCode(model.getPlantInsurabilityTypeCode());
			dto.setIsAwpEligibleInd(model.getIsAwpEligibleInd());
		}
	}

	@Override
	public void updateDto(InventoryBerriesDto dto, InventoryBerries model) {

		if (Boolean.TRUE.equals(model.getDeletedByUserInd())) {

			// Flagged for deletion, but record could not be deleted, so clear the user-entered data.
			//The commodity stays on the record to be able to show it in the correct table in the ui
			dto.setCropCommodityId(model.getCropCommodityId());
			dto.setCropCommodityName(model.getCropCommodityName());
			dto.setCropVarietyId(null);
			dto.setCropVarietyName(null);
			dto.setPlantInsurabilityTypeCode(null);
			dto.setPlantedYear(null);
			dto.setPlantedAcres(null);
			dto.setRowSpacing(null);
			dto.setPlantSpacing(null);
			dto.setTotalPlants(null);
			dto.setIsQuantityInsurableInd(false);
			dto.setIsPlantInsurableInd(false);
			dto.setBogId(null);
			dto.setBogMowedDate(null);
			dto.setBogRenovatedDate(null);
			dto.setIsHarvestedInd(false);

		} else {		
			dto.setCropCommodityId(model.getCropCommodityId());
			dto.setCropCommodityName(model.getCropCommodityName());
			dto.setCropVarietyId(model.getCropVarietyId());
			dto.setCropVarietyName(model.getCropVarietyName());
			dto.setPlantInsurabilityTypeCode(model.getPlantInsurabilityTypeCode());
			dto.setPlantedYear(model.getPlantedYear());
			dto.setPlantedAcres(model.getPlantedAcres());
			dto.setRowSpacing(model.getRowSpacing());
			dto.setPlantSpacing(model.getPlantSpacing());
			dto.setTotalPlants(model.getTotalPlants());
			dto.setIsQuantityInsurableInd(model.getIsQuantityInsurableInd());
			dto.setIsPlantInsurableInd(model.getIsPlantInsurableInd());
			dto.setBogId(model.getBogId());
			dto.setBogMowedDate(model.getBogMowedDate());
			dto.setBogRenovatedDate(model.getBogRenovatedDate());
			///Not all commodities use the flag and it might be not set
			dto.setIsHarvestedInd(notNull(model.getIsHarvestedInd(), false));

		}	
	}
	
	@Override
	public void updateDto(UnderwritingCommentDto dto, UnderwritingComment model) {
		dto.setUnderwritingComment(model.getUnderwritingComment());
		dto.setUnderwritingCommentTypeCode(model.getUnderwritingCommentTypeCode());
		dto.setUnderwritingCommentTypeDesc(model.getUnderwritingCommentTypeDesc());
	}

	@Override
	public boolean checkEmptyInventoryUnseeded(InventoryUnseeded inventoryUnseeded) {
		return inventoryUnseeded.getAcresToBeSeeded() == null && inventoryUnseeded.getCropCommodityId() == null;
	}

	@Override
	public boolean checkEmptyInventoryUnseeded(InventoryUnseededDto inventoryUnseeded) {
		return inventoryUnseeded.getAcresToBeSeeded() == null && inventoryUnseeded.getCropCommodityId() == null;
	}
	
	@Override
	public boolean checkEmptyInventorySeededGrain(InventorySeededGrain inventorySeededGrain) {
		return inventorySeededGrain.getCommodityTypeCode() == null && 
				inventorySeededGrain.getCropCommodityId() == null && 
				inventorySeededGrain.getCropVarietyId() == null && 
				inventorySeededGrain.getSeededAcres() == null && 
				inventorySeededGrain.getSeededDate() == null;
	}

	@Override
	public boolean checkEmptyInventorySeededGrain(InventorySeededGrainDto inventorySeededGrain) {
		return inventorySeededGrain.getCommodityTypeCode() == null && 
				inventorySeededGrain.getCropCommodityId() == null && 
				inventorySeededGrain.getCropVarietyId() == null && 
				inventorySeededGrain.getSeededAcres() == null && 
				inventorySeededGrain.getSeededDate() == null;
	}
	
	
	@Override
	public Boolean checkUserCanEditComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication) { 
		
		return userCanEditComment(dto, authentication);
	}
	
	static Boolean userCanEditComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication) {

		// If permission cannot be determined, then this returns false.
		Boolean userCanEdit = false;
		
		if (authentication != null ) {
			String currUserId = authentication.getUserId();
			String createUserId = dto.getCreateUser();
			
			if (currUserId != null && !currUserId.isEmpty() && createUserId != null && !createUserId.isEmpty()) {
				userCanEdit = currUserId.equals(createUserId);
			}
		}
		
		return userCanEdit;
	}
	
	@Override
	public Boolean checkUserCanDeleteComment(UnderwritingCommentDto dto, WebAdeAuthentication authentication) { 
		
		// If permission cannot be determined, then this returns false.
		Boolean userCanDelete = false;
		
		if (authentication != null ) {
			if (userCanDeleteAllComments(authentication)) {
				userCanDelete = true;
			} else {
				String currUserId = authentication.getUserId();
				String createUserId = dto.getCreateUser();
				
				if (currUserId != null && !currUserId.isEmpty() && createUserId != null && !createUserId.isEmpty()) {
					userCanDelete = currUserId.equals(createUserId);
				}
			}
		}
		
		return userCanDelete;
	}

	static Boolean userCanDeleteAllComments(WebAdeAuthentication authentication) {

		// If permission cannot be determined, then this returns false.
		Boolean userCanDelete = false;
		
		if (authentication != null && authentication.hasAuthority(Scopes.DELETE_COMMENTS)) {
			userCanDelete = true;
		}
		
		return userCanDelete;
	}

	static void setSelfLink(String inventoryContractGuid, InventoryContractRsrc resource, URI baseUri) {
		if (inventoryContractGuid != null) {
			String selfUri = getInventoryContractSelfUri(inventoryContractGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getInventoryContractSelfUri(String inventoryContractGuid, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(InventoryContractEndpoint.class).build(inventoryContractGuid).toString();
		return result;
	}

	private static void setLinks(String inventoryContractGuid, InventoryContractRsrc resource, URI baseUri, WebAdeAuthentication authentication) {

		String result = UriBuilder.fromUri(baseUri).path(InventoryContractEndpoint.class).build(inventoryContractGuid).toString();

		if (authentication.hasAuthority(Scopes.UPDATE_INVENTORY_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_INVENTORY_CONTRACT, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_INVENTORY_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_INVENTORY_CONTRACT, result, "DELETE"));
		}
	}

	public static String getInventoryContractListSelfUri(URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(InventoryContractListEndpoint.class).build().toString();
		return result;
	}
	
	private static void setInventoryContractListSelfLink(
			InventoryContractListRsrc resource,
			Integer cropYear,
			Integer insurancePlanId,
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids,
			URI baseUri) {
		
		String selfUri = getInventoryContractListSelfUri(
				cropYear,
				insurancePlanId,
				officeId,
				policyStatusCode,
				policyNumber,
				growerInfo,
				sortColumn,
				inventoryContractGuids,
				baseUri);
		
		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));		
	}
	
	public static String getInventoryContractListSelfUri(
			Integer cropYear,
			Integer insurancePlanId,
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String inventoryContractGuids,
			URI baseUri) {

			String result = UriBuilder.fromUri(baseUri)
				.path(InventoryContractListEndpoint.class)
				.queryParam("cropYear", nvl(toString(cropYear), ""))
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.queryParam("officeId", nvl(toString(officeId), ""))
				.queryParam("policyStatusCode", nvl(policyStatusCode, ""))
				.queryParam("policyNumber", nvl(policyNumber, ""))
				.queryParam("growerInfo", nvl(growerInfo, ""))
				.queryParam("sortColumn", nvl(sortColumn, ""))
				.queryParam("inventoryContractGuids", nvl(inventoryContractGuids, ""))
				.build()
				.toString();

			return result;
		}	

	public static String getInventoryContractReportSelfUri(
			Integer cropYear,
			Integer insurancePlanId,
			Integer officeId,
			String policyStatusCode,
			String policyNumber,
			String growerInfo,
			String sortColumn,
			String policyIds, 
			URI baseUri) {
		String result = UriBuilder.fromUri(baseUri)
				.path(InventoryContractReportEndpoint.class)
				.queryParam("cropYear", nvl(toString(cropYear), ""))
				.queryParam("insurancePlanId", nvl(toString(insurancePlanId), ""))
				.queryParam("officeId", nvl(toString(officeId), ""))
				.queryParam("policyStatusCode", nvl(policyStatusCode, ""))
				.queryParam("policyNumber", nvl(policyNumber, ""))
				.queryParam("growerInfo", nvl(growerInfo, ""))
				.queryParam("sortColumn", nvl(sortColumn, ""))
				.queryParam("policyIds", nvl(policyIds, ""))
				.build()
				.toString();

		return result;
	}

	private Boolean notNull(Boolean value, Boolean defaultValue) {
		return (value == null) ? defaultValue : value;
	}

}
