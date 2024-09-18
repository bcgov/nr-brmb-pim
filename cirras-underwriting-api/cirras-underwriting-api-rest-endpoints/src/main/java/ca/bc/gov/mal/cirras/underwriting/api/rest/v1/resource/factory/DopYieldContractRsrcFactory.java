package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.factory;

import java.net.URI;
//import java.sql.Date;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.DopYieldContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.AnnualFieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.DopYieldContractRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededGrainDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.DopYieldContractFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.util.InventoryServiceEnums.InsurancePlans;

public class DopYieldContractRsrcFactory extends BaseResourceFactory implements DopYieldContractFactory { 
	
	@Override
	public DopYieldContract<? extends AnnualField> getDefaultDopYieldContract(
			PolicyDto policyDto,
			String defaultMeasurementUnitCode,
			DeclaredYieldContractDto dycDto,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {
		
		DopYieldContractRsrc resource = new DopYieldContractRsrc();

		populateDefaultResource(resource, policyDto, defaultMeasurementUnitCode);

		// Fields
		if (dycDto.getFields() != null && !dycDto.getFields().isEmpty()) {
			List<AnnualFieldRsrc> fields = new ArrayList<AnnualFieldRsrc>();

			for (ContractedFieldDetailDto cfdDto : dycDto.getFields()) {
				AnnualFieldRsrc afModel = createAnnualField(cfdDto, authentication);
				fields.add(afModel);
			}

			resource.setFields(fields);
		}
		
		// Declared Yield Contract Commodity
		if (!dycDto.getDeclaredYieldContractCommodities().isEmpty()) {
			List<DopYieldContractCommodity> dopContractCommodities = new ArrayList<DopYieldContractCommodity>();

			for (DeclaredYieldContractCommodityDto dyccDto : dycDto.getDeclaredYieldContractCommodities()) {
				DopYieldContractCommodity dyccModel = createDopYieldContractCommodities(dyccDto);
				dopContractCommodities.add(dyccModel);
			}

			resource.setDopYieldContractCommodities(dopContractCommodities);
		}
		
		// Declared Yield Contract Commodity FORAGE
		if (!dycDto.getDeclaredYieldContractCommodityForageList().isEmpty()) {
			List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();

			for (DeclaredYieldContractCommodityForageDto dyccfDto : dycDto.getDeclaredYieldContractCommodityForageList()) {
				DopYieldContractCommodityForage dyccfModel = createDopYieldContractCommodityForage(dyccfDto);
				dopYieldContractCommodityForageList.add(dyccfModel);
			}

			resource.setDopYieldContractCommodityForageList(dopYieldContractCommodityForageList);
		}
		
		// Declared Yield Rollup FORAGE
		if (!dycDto.getDeclaredYieldFieldRollupForageList().isEmpty()) {
			List<DopYieldFieldRollupForage> dopYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();

			for (DeclaredYieldFieldRollupForageDto dyrfDto : dycDto.getDeclaredYieldFieldRollupForageList()) {
				DopYieldFieldRollupForage dyrfModel = createDopYieldFieldRollupForage(dyrfDto);
				dopYieldFieldRollupForageList.add(dyrfModel);
			}

			resource.setDopYieldFieldRollupForageList(dopYieldFieldRollupForageList);
		}


		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}

	private void populateDefaultResource(DopYieldContractRsrc resource, PolicyDto policyDto,
			String defaultMeasurementUnitCode) {
		
		resource.setDeclaredYieldContractGuid(null);
		resource.setContractId(policyDto.getContractId());
		resource.setCropYear(policyDto.getCropYear());
		resource.setDeclarationOfProductionDate(null);
		resource.setDopUpdateTimestamp(null);
		resource.setDopUpdateUser(null);
		resource.setEnteredYieldMeasUnitTypeCode(null);
		resource.setDefaultYieldMeasUnitTypeCode(defaultMeasurementUnitCode);
		resource.setGrainFromOtherSourceInd(null);
		resource.setBalerWagonInfo(null);
		resource.setTotalLivestock(null);
		resource.setInsurancePlanId(policyDto.getInsurancePlanId());
		resource.setGrowerContractYearId(policyDto.getGrowerContractYearId());
	}
   
	@Override
	public DopYieldContract<? extends AnnualField> getDopYieldContract(
			DeclaredYieldContractDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException {

		DopYieldContractRsrc resource = new DopYieldContractRsrc();

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
		
		// contract level uw comments
		if (!dto.getUwComments().isEmpty()) {
			List<UnderwritingComment> uwComments = new ArrayList<UnderwritingComment>();

			for (UnderwritingCommentDto ucDto : dto.getUwComments()) {
				UnderwritingComment ucModel = InventoryContractRsrcFactory.createUnderwritingComment(ucDto, authentication);
				uwComments.add(ucModel);
			}
			
			resource.setUwComments(uwComments);
		}
				
		
		// Declared Yield Field Rollup (Estimated Yield)
		if (!dto.getDeclaredYieldFieldRollupList().isEmpty()) {
			List<DopYieldFieldRollup> dopFieldRollups = new ArrayList<DopYieldFieldRollup>();

			for (DeclaredYieldFieldRollupDto dyfrDto : dto.getDeclaredYieldFieldRollupList()) {
				DopYieldFieldRollup dyfrModel = createDopYieldFieldRollup(dyfrDto);
				dopFieldRollups.add(dyfrModel);
			}

			resource.setDopYieldFieldRollupList(dopFieldRollups);
		}
		
		// Declared Yield Contract Commodity
		if (!dto.getDeclaredYieldContractCommodities().isEmpty()) {
			List<DopYieldContractCommodity> dopContractCommodities = new ArrayList<DopYieldContractCommodity>();

			for (DeclaredYieldContractCommodityDto dyccDto : dto.getDeclaredYieldContractCommodities()) {
				DopYieldContractCommodity dyccModel = createDopYieldContractCommodities(dyccDto);
				dopContractCommodities.add(dyccModel);
			}

			resource.setDopYieldContractCommodities(dopContractCommodities);
		}
		
		// Declared Yield Contract Commodity FORAGE
		if (!dto.getDeclaredYieldContractCommodityForageList().isEmpty()) {
			List<DopYieldContractCommodityForage> dopYieldContractCommodityForageList = new ArrayList<DopYieldContractCommodityForage>();

			for (DeclaredYieldContractCommodityForageDto dyccfDto : dto.getDeclaredYieldContractCommodityForageList()) {
				DopYieldContractCommodityForage dyccfModel = createDopYieldContractCommodityForage(dyccfDto);
				dopYieldContractCommodityForageList.add(dyccfModel);
			}

			resource.setDopYieldContractCommodityForageList(dopYieldContractCommodityForageList);
		}	
		
		// Declared Yield Rollup FORAGE
		if (!dto.getDeclaredYieldFieldRollupForageList().isEmpty()) {
			List<DopYieldFieldRollupForage> dopYieldFieldRollupForageList = new ArrayList<DopYieldFieldRollupForage>();

			for (DeclaredYieldFieldRollupForageDto dyrfDto : dto.getDeclaredYieldFieldRollupForageList()) {
				DopYieldFieldRollupForage dyrfModel = createDopYieldFieldRollupForage(dyrfDto);
				dopYieldFieldRollupForageList.add(dyrfModel);
			}

			resource.setDopYieldFieldRollupForageList(dopYieldFieldRollupForageList);
		}
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getDeclaredYieldContractGuid(), resource, baseUri);
		setLinks(dto.getDeclaredYieldContractGuid(), resource, baseUri, authentication);

		return resource;

	}
	

	@Override
	public List<DeclaredYieldContractCommodityDto> getDopCommoditiesFromInventoryCommodities(
			List<InventoryContractCommodityDto> dtos) throws FactoryException {
		
		List<DeclaredYieldContractCommodityDto> dopCommodities = new ArrayList<DeclaredYieldContractCommodityDto>();
		for (InventoryContractCommodityDto iccDto : dtos) {
			
			DeclaredYieldContractCommodityDto dto = new DeclaredYieldContractCommodityDto();
			dto.setCropCommodityId(iccDto.getCropCommodityId());
			dto.setCropCommodityName(iccDto.getCropCommodityName());
			dto.setIsPedigreeInd(iccDto.getIsPedigreeInd());
			dto.setTotalInsuredAcres(iccDto.getTotalSeededAcres());
			dopCommodities.add(dto);
		}
		return dopCommodities;
	}
	

	@Override
	public List<DeclaredYieldContractCommodityForageDto> getDopForageCommoditiesFromInventorySeeded(
			List<InventorySeededForageDto> dtos) throws FactoryException {
		
		List<DeclaredYieldContractCommodityForageDto> dopForageCommodities = new ArrayList<DeclaredYieldContractCommodityForageDto>();
		for (InventorySeededForageDto isfDto : dtos) {
			
			DeclaredYieldContractCommodityForageDto dto = new DeclaredYieldContractCommodityForageDto();
			dto.setCommodityTypeCode(isfDto.getCommodityTypeCode());
			dto.setCommodityTypeDescription(isfDto.getCommodityTypeDescription());
			dto.setTotalFieldAcres(isfDto.getTotalFieldAcres());
			dopForageCommodities.add(dto);
		}
		return dopForageCommodities;
	}
	
	@Override
	public List<DeclaredYieldFieldRollupForageDto> getDopForageRollupCommoditiesFromInventorySeeded(
			List<InventorySeededForageDto> dtos) throws FactoryException {
		
		List<DeclaredYieldFieldRollupForageDto> dopForageRollups = new ArrayList<DeclaredYieldFieldRollupForageDto>();
		for (InventorySeededForageDto isfDto : dtos) {
			
			DeclaredYieldFieldRollupForageDto dto = new DeclaredYieldFieldRollupForageDto();
			dto.setCommodityTypeCode(isfDto.getCommodityTypeCode());
			dto.setCommodityTypeDescription(isfDto.getCommodityTypeDescription());
			dto.setTotalFieldAcres(isfDto.getTotalFieldAcres());
			dopForageRollups.add(dto);
		}
		return dopForageRollups;
	}
	
	
	@Override  
	public List<DeclaredYieldFieldRollupDto> getDopFieldRollupCommoditiesFromInventoryCommodities(
			List<InventoryContractCommodityDto> dtos) throws FactoryException {
		
		List<DeclaredYieldFieldRollupDto> dopCommodities = new ArrayList<DeclaredYieldFieldRollupDto>();
		for (InventoryContractCommodityDto iccDto : dtos) {
			
			DeclaredYieldFieldRollupDto dto = new DeclaredYieldFieldRollupDto();
			dto.setCropCommodityId(iccDto.getCropCommodityId());
			dto.setCropCommodityName(iccDto.getCropCommodityName());
			dto.setIsPedigreeInd(iccDto.getIsPedigreeInd());
			dopCommodities.add(dto);
		}
		return dopCommodities;
	}
	
	
	private void populateResource(DopYieldContractRsrc resource, DeclaredYieldContractDto dto) {

		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setDeclarationOfProductionDate(dto.getDeclarationOfProductionDate());
		resource.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		resource.setDefaultYieldMeasUnitTypeCode(dto.getDefaultYieldMeasUnitTypeCode());
		resource.setDopUpdateTimestamp(dto.getDopUpdateTimestamp());
		resource.setDopUpdateUser(dto.getDopUpdateUser());
		resource.setEnteredYieldMeasUnitTypeCode(dto.getEnteredYieldMeasUnitTypeCode());
		resource.setGrainFromOtherSourceInd(dto.getGrainFromOtherSourceInd());
		resource.setBalerWagonInfo(dto.getBalerWagonInfo());
		resource.setTotalLivestock(dto.getTotalLivestock());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setGrowerContractYearId(dto.getGrowerContractYearId());
	}
	
	private DopYieldFieldRollup createDopYieldFieldRollup(DeclaredYieldFieldRollupDto dto) {
		DopYieldFieldRollup model = new DopYieldFieldRollup();
		
		model.setDeclaredYieldFieldRollupGuid(dto.getDeclaredYieldFieldRollupGuid());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setEstimatedYieldPerAcreTonnes(dto.getEstimatedYieldPerAcreTonnes());
		model.setEstimatedYieldPerAcreBushels(dto.getEstimatedYieldPerAcreBushels());
		
		return model;
	}

	private DopYieldContractCommodity createDopYieldContractCommodities(DeclaredYieldContractCommodityDto dto) {
		DopYieldContractCommodity model = new DopYieldContractCommodity();
		
		model.setDeclaredYieldContractCommodityGuid(dto.getDeclaredYieldContractCommodityGuid());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setIsPedigreeInd(dto.getIsPedigreeInd());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setStoredYield(dto.getStoredYield());
		model.setStoredYieldDefaultUnit(dto.getStoredYieldDefaultUnit());
		model.setSoldYield(dto.getSoldYield());
		model.setSoldYieldDefaultUnit(dto.getSoldYieldDefaultUnit());
		model.setGradeModifierTypeCode(dto.getGradeModifierTypeCode());

		model.setTotalInsuredAcres(dto.getTotalInsuredAcres());

		return model;
	}
	
	private DopYieldFieldForageCut createDopYieldFieldForageCut(DeclaredYieldFieldForageDto dto) {

		DopYieldFieldForageCut model = new DopYieldFieldForageCut();

		model.setDeclaredYieldFieldForageGuid(dto.getDeclaredYieldFieldForageGuid());
		model.setInventoryFieldGuid(dto.getInventoryFieldGuid());
		model.setCutNumber(dto.getCutNumber());
		model.setTotalBalesLoads(dto.getTotalBalesLoads());
		model.setWeight(dto.getWeight());
		model.setWeightDefaultUnit(dto.getWeightDefaultUnit());
		model.setMoisturePercent(dto.getMoisturePercent());
		model.setDeletedByUserInd(false);
		
		return model;

	}

	private DopYieldContractCommodityForage createDopYieldContractCommodityForage(DeclaredYieldContractCommodityForageDto dto) {
		DopYieldContractCommodityForage model = new DopYieldContractCommodityForage();
		
		model.setDeclaredYieldContractCmdtyForageGuid(dto.getDeclaredYieldContractCmdtyForageGuid());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setTotalFieldAcres(dto.getTotalFieldAcres());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setHarvestedAcresOverride(dto.getHarvestedAcresOverride());
		model.setQuantityHarvestedTons(dto.getQuantityHarvestedTons());
		model.setQuantityHarvestedTonsOverride(dto.getQuantityHarvestedTonsOverride());
		model.setYieldPerAcre(dto.getYieldPerAcre());
		model.setCommodityTypeDescription(dto.getCommodityTypeDescription());

		return model;
	}
	
	private DopYieldFieldRollupForage createDopYieldFieldRollupForage(DeclaredYieldFieldRollupForageDto dto) {
		DopYieldFieldRollupForage model = new DopYieldFieldRollupForage();
		
		model.setDeclaredYieldFieldRollupForageGuid(dto.getDeclaredYieldFieldRollupForageGuid());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setCommodityTypeCode(dto.getCommodityTypeCode());
		model.setTotalFieldAcres(dto.getTotalFieldAcres());
		model.setTotalBalesLoads(dto.getTotalBalesLoads());
		model.setHarvestedAcres(dto.getHarvestedAcres());
		model.setQuantityHarvestedTons(dto.getQuantityHarvestedTons());
		model.setYieldPerAcre(dto.getYieldPerAcre());
		model.setCommodityTypeDescription(dto.getCommodityTypeDescription());

		return model;
	}

	// Creates an AnnualFieldRsrc for a DopYieldContract.
	private AnnualFieldRsrc createAnnualField(ContractedFieldDetailDto dto, WebAdeAuthentication authentication) {
		AnnualFieldRsrc model = new AnnualFieldRsrc();

		AnnualFieldRsrcFactory.populateResource(model, dto);

		
		// DopYieldFields
		if (!dto.getPlantings().isEmpty()) {
			List<DopYieldFieldGrain> dopYieldFieldsGrain = new ArrayList<DopYieldFieldGrain>();
			List<DopYieldFieldForage> dopYieldFieldsForage = new ArrayList<DopYieldFieldForage>();

			for (InventoryFieldDto ifDto : dto.getPlantings()) {
				
				if(InsurancePlans.GRAIN.getInsurancePlanId().equals(ifDto.getInsurancePlanId())) {
					DopYieldFieldGrain dyfModel = createDopYieldFieldGrain(ifDto);
					dopYieldFieldsGrain.add(dyfModel);
				} else if(InsurancePlans.FORAGE.getInsurancePlanId().equals(ifDto.getInsurancePlanId())) {
					DopYieldFieldForage dyffModel = createDopYieldFieldForage(ifDto);
					dopYieldFieldsForage.add(dyffModel);
				}
			}

			model.setDopYieldFieldGrainList(dopYieldFieldsGrain);
			model.setDopYieldFieldForageList(dopYieldFieldsForage);
		}
		
		
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
	
	private DopYieldFieldGrain createDopYieldFieldGrain(InventoryFieldDto ifDto) throws FactoryException {
		
		DopYieldFieldGrain model = new DopYieldFieldGrain();

		// InventoryFieldDto
		model.setCropYear(ifDto.getCropYear());
		model.setFieldId(ifDto.getFieldId());
		model.setInsurancePlanId(ifDto.getInsurancePlanId());
		model.setInventoryFieldGuid(ifDto.getInventoryFieldGuid());

		// InventorySeededGrainDto
		List<InventorySeededGrainDto> isgList = ifDto.getInventorySeededGrains();
		
		if (isgList != null && !isgList.isEmpty()) {
			if (isgList.size() == 1 ) {
				InventorySeededGrainDto isgDto = isgList.get(0);

				model.setCropCommodityId(isgDto.getCropCommodityId());
				model.setCropCommodityName(isgDto.getCropCommodityName());
				model.setCropVarietyId(isgDto.getCropVarietyId());
				model.setCropVarietyName(isgDto.getCropVarietyName());
				model.setInventorySeededGrainGuid(isgDto.getInventorySeededGrainGuid());
				model.setIsPedigreeInd(isgDto.getIsPedigreeInd());
				model.setSeededAcres(isgDto.getSeededAcres());
				model.setSeededDate(isgDto.getSeededDate());
			
			} else {
				// Should never happen. Although the data model supports multiple seeded grain records, the UI does not.
				throw new FactoryException("Cannot populate seeded grain data. There are " + isgList.size() + " records to choose from.");
			}
		}

		// DeclaredYieldFieldDto
		DeclaredYieldFieldDto dyfDto = ifDto.getDeclaredYieldField();
		if ( dyfDto != null ) { 
			model.setDeclaredYieldFieldGuid(dyfDto.getDeclaredYieldFieldGuid());
			model.setEstimatedYieldPerAcre(dyfDto.getEstimatedYieldPerAcre());
			model.setEstimatedYieldPerAcreDefaultUnit(dyfDto.getEstimatedYieldPerAcreDefaultUnit());
			model.setUnharvestedAcresInd(dyfDto.getUnharvestedAcresInd());
		} else {
			// No record, so set defaults.
			model.setDeclaredYieldFieldGuid(null);
			model.setEstimatedYieldPerAcre(null);
			model.setEstimatedYieldPerAcreDefaultUnit(null);
			model.setUnharvestedAcresInd(false);
		}

		return model;
	}
	
	private DopYieldFieldForage createDopYieldFieldForage(InventoryFieldDto ifDto) throws FactoryException {
		
		DopYieldFieldForage model = new DopYieldFieldForage();

		// InventoryFieldDto
		model.setCropYear(ifDto.getCropYear());
		model.setFieldId(ifDto.getFieldId());
		model.setInsurancePlanId(ifDto.getInsurancePlanId());
		model.setInventoryFieldGuid(ifDto.getInventoryFieldGuid());
		model.setIsHiddenOnPrintoutInd(ifDto.getIsHiddenOnPrintoutInd());
		
		// InventorySeededForageDto
		List<InventorySeededForageDto> isfList = ifDto.getInventorySeededForages();
		
		if (isfList != null && !isfList.isEmpty()) {
			if (isfList.size() == 1 ) {
				InventorySeededForageDto isfDto = isfList.get(0);
				
				model.setCommodityTypeCode(isfDto.getCommodityTypeCode());
				model.setCommodityTypeDescription(isfDto.getCommodityTypeDescription());
				model.setCropVarietyName(isfDto.getCropVarietyName());
				model.setCropVarietyId(isfDto.getCropVarietyId());
				model.setCropCommodityId(isfDto.getCropCommodityId());
				model.setIsQuantityInsurableInd(isfDto.getIsQuantityInsurableInd());
				model.setFieldAcres(isfDto.getFieldAcres());
				model.setPlantDurationTypeCode(isfDto.getPlantDurationTypeCode());
				model.setPlantInsurabilityTypeCode(isfDto.getPlantInsurabilityTypeCode());
				model.setSeedingYear(isfDto.getSeedingYear());
				model.setSeedingDate(isfDto.getSeedingDate());
				
			} else {
				// Should never happen. Although the data model supports multiple seeded forage records, the UI does not.
				throw new FactoryException("Cannot populate seeded forage data. There are " + isfList.size() + " records to choose from.");
			}
		}
		
		// Declared Yield Contract Commodity FORAGE
		if (!ifDto.getDeclaredYieldFieldForageList().isEmpty()) {
			List<DopYieldFieldForageCut> dopYieldFieldForageCuts = new ArrayList<DopYieldFieldForageCut>();

			for (DeclaredYieldFieldForageDto dyff : ifDto.getDeclaredYieldFieldForageList()) {
				DopYieldFieldForageCut dyffc = createDopYieldFieldForageCut(dyff);
				dopYieldFieldForageCuts.add(dyffc);
			}

			model.setDopYieldFieldForageCuts(dopYieldFieldForageCuts);
		}
		
		return model;
	}
	
	@Override
	public void updateDto(DeclaredYieldContractDto dto, DopYieldContract<? extends AnnualField> model, String userId) {

		dto.setContractId(model.getContractId());
		dto.setCropYear(model.getCropYear());
		dto.setDeclarationOfProductionDate(model.getDeclarationOfProductionDate());
		dto.setDefaultYieldMeasUnitTypeCode(model.getDefaultYieldMeasUnitTypeCode()); 
		dto.setDopUpdateTimestamp(new Date()); 		
		dto.setDopUpdateUser(userId);
		dto.setEnteredYieldMeasUnitTypeCode(model.getEnteredYieldMeasUnitTypeCode());
		dto.setGrainFromOtherSourceInd(model.getGrainFromOtherSourceInd());
		dto.setBalerWagonInfo(model.getBalerWagonInfo());
		dto.setTotalLivestock(model.getTotalLivestock());
		dto.setInsurancePlanId(model.getInsurancePlanId());
		dto.setGrowerContractYearId(model.getGrowerContractYearId());
	}

	@Override
	public void updateDto(DeclaredYieldFieldForageDto dto, DopYieldFieldForageCut model) {

		dto.setCutNumber(model.getCutNumber());
		dto.setTotalBalesLoads(model.getTotalBalesLoads());
		dto.setWeight(model.getWeight());
		dto.setWeightDefaultUnit(model.getWeightDefaultUnit());
		dto.setMoisturePercent(model.getMoisturePercent());
		
	}

	@Override
	public void updateDto(DeclaredYieldContractCommodityForageDto dto, DopYieldContractCommodityForage model) {

		dto.setDeclaredYieldContractGuid(model.getDeclaredYieldContractGuid());
		dto.setCommodityTypeCode(model.getCommodityTypeCode());
		dto.setTotalFieldAcres(model.getTotalFieldAcres());
		dto.setHarvestedAcres(model.getHarvestedAcres());
		dto.setHarvestedAcresOverride(model.getHarvestedAcresOverride());
		dto.setQuantityHarvestedTons(model.getQuantityHarvestedTons());
		dto.setQuantityHarvestedTonsOverride(model.getQuantityHarvestedTonsOverride());
		dto.setYieldPerAcre(model.getYieldPerAcre());
		
	}
	

	@Override
	public void updateDto(DeclaredYieldFieldRollupForageDto dto, DopYieldFieldRollupForage model) {

		dto.setDeclaredYieldContractGuid(model.getDeclaredYieldContractGuid());
		dto.setCommodityTypeCode(model.getCommodityTypeCode());
		dto.setTotalFieldAcres(model.getTotalFieldAcres());
		dto.setTotalBalesLoads(model.getTotalBalesLoads());
		dto.setHarvestedAcres(model.getHarvestedAcres());
		dto.setQuantityHarvestedTons(model.getQuantityHarvestedTons());
		dto.setYieldPerAcre(model.getYieldPerAcre());

	}
	
	@Override
	public void updateDto(DeclaredYieldFieldDto dto, DopYieldFieldGrain model) {

		dto.setEstimatedYieldPerAcre(model.getEstimatedYieldPerAcre());
		dto.setEstimatedYieldPerAcreDefaultUnit(model.getEstimatedYieldPerAcreDefaultUnit());
		dto.setUnharvestedAcresInd(model.getUnharvestedAcresInd());
	
	}
	
	@Override
	public void updateDto(DeclaredYieldFieldRollupDto dto, DopYieldFieldRollup model) {
		dto.setDeclaredYieldFieldRollupGuid(model.getDeclaredYieldFieldRollupGuid());
		dto.setDeclaredYieldContractGuid(model.getDeclaredYieldContractGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
		dto.setEstimatedYieldPerAcreTonnes(model.getEstimatedYieldPerAcreTonnes());
		dto.setEstimatedYieldPerAcreBushels(model.getEstimatedYieldPerAcreBushels());
	}
	

	@Override
	public void updateDto(DeclaredYieldContractCommodityDto dto, DopYieldContractCommodity model) {
		dto.setDeclaredYieldContractCommodityGuid(model.getDeclaredYieldContractCommodityGuid());
		dto.setDeclaredYieldContractGuid(model.getDeclaredYieldContractGuid());
		dto.setCropCommodityId(model.getCropCommodityId());
		dto.setIsPedigreeInd(model.getIsPedigreeInd());
		dto.setHarvestedAcres(model.getHarvestedAcres());
		dto.setStoredYield(model.getStoredYield());
		dto.setStoredYieldDefaultUnit(model.getStoredYieldDefaultUnit());
		dto.setSoldYield(model.getSoldYield());
		dto.setSoldYieldDefaultUnit(model.getSoldYieldDefaultUnit());
		dto.setGradeModifierTypeCode(model.getGradeModifierTypeCode());
	}
	

	
	static void setSelfLink(String declaredYieldContractGuid, DopYieldContractRsrc resource, URI baseUri) {
		if (declaredYieldContractGuid != null) {
			String selfUri = getDopYieldContractSelfUri(declaredYieldContractGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getDopYieldContractSelfUri(String declaredYieldContractGuid, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(DopYieldContractEndpoint.class).build(declaredYieldContractGuid).toString();
		return result;
	}

	private static void setLinks(String declaredYieldContractGuid, DopYieldContractRsrc resource, URI baseUri, WebAdeAuthentication authentication) {		

		String result = UriBuilder.fromUri(baseUri).path(DopYieldContractEndpoint.class).build(declaredYieldContractGuid).toString();

		if (authentication.hasAuthority(Scopes.UPDATE_DOP_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_DOP_YIELD_CONTRACT, result, "PUT"));
		}
		
		if (authentication.hasAuthority(Scopes.DELETE_DOP_YIELD_CONTRACT)) {
			resource.getLinks().add(new RelLink(ResourceTypes.DELETE_DOP_YIELD_CONTRACT, result, "DELETE"));
		}
		
	}

	public static String getDopYieldContractListSelfUri(URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(DopYieldContractListEndpoint.class).build().toString();
		return result;
	}

	public static String getDopYieldContractReportSelfUri(
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
				.path(DopYieldContractReportEndpoint.class)
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
	
}
