package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryFieldDto extends BaseDto<InventoryFieldDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryFieldDto.class);

	private String inventoryFieldGuid;
	private Integer insurancePlanId;
	private Integer fieldId;
	private Integer lastYearCropCommodityId;
	private String lastYearCropCommodityName;
	private Integer lastYearCropVarietyId;
	private String lastYearCropVarietyName;
	private Integer cropYear;
	private Integer plantingNumber;
	private Boolean isHiddenOnPrintoutInd;
	private Integer underseededCropVarietyId;
	private String underseededCropVarietyName;
	private Double underseededAcres;
	private String underseededInventorySeededForageGuid;

	//Used in rollover
	private Double acresToBeSeeded;
	private Boolean isGrainUnseededDefaultInd;

	private InventoryUnseededDto inventoryUnseeded;
	private InventoryBerriesDto inventoryBerries;

	// All but one InventorySeededGrainDto should have isReplacedInd=true, so that only one is current.
	private List<InventorySeededGrainDto> inventorySeededGrains = new ArrayList<InventorySeededGrainDto>();
	private List<InventorySeededForageDto> inventorySeededForages = new ArrayList<InventorySeededForageDto>();

	private DeclaredYieldFieldDto declaredYieldField;
	private List<DeclaredYieldFieldForageDto> declaredYieldFieldForageList = new ArrayList<DeclaredYieldFieldForageDto>();
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Contains linked planting information
	private Integer linkedCropVarietyId;
	private String linkedVarietyName;
	private Double linkedFieldAcres;

	
	public InventoryFieldDto() {
	}
	
	
	public InventoryFieldDto(InventoryFieldDto dto) {

		this.inventoryFieldGuid = dto.inventoryFieldGuid;
		this.insurancePlanId = dto.insurancePlanId;
		this.fieldId = dto.fieldId;
		this.lastYearCropCommodityId = dto.lastYearCropCommodityId;
		this.lastYearCropCommodityName = dto.lastYearCropCommodityName;
		this.lastYearCropVarietyId = dto.lastYearCropVarietyId;
		this.lastYearCropVarietyName = dto.lastYearCropVarietyName;
		this.cropYear = dto.cropYear;
		this.plantingNumber = dto.plantingNumber;
		this.acresToBeSeeded = dto.acresToBeSeeded;
		this.isGrainUnseededDefaultInd = dto.isGrainUnseededDefaultInd;
		this.isHiddenOnPrintoutInd = dto.isHiddenOnPrintoutInd;
		this.underseededCropVarietyId = dto.underseededCropVarietyId;
		this.underseededCropVarietyName = dto.underseededCropVarietyName;
		this.underseededAcres = dto.underseededAcres;
		this.underseededInventorySeededForageGuid = dto.underseededInventorySeededForageGuid;


		if ( dto.inventoryUnseeded != null) {
			this.inventoryUnseeded = dto.inventoryUnseeded.copy();
		}

		if ( dto.inventorySeededGrains != null ) {			
			this.inventorySeededGrains = new ArrayList<>();
			
			for ( InventorySeededGrainDto isgDto : dto.inventorySeededGrains ) {
				this.inventorySeededGrains.add(isgDto.copy());
			}
		}
		
		if ( dto.inventorySeededForages != null ) {			
			this.inventorySeededForages = new ArrayList<>();
			
			for ( InventorySeededForageDto isfDto : dto.inventorySeededForages ) {
				this.inventorySeededForages.add(isfDto.copy());
			}
		}
		
		if ( dto.inventoryBerries != null) {
			this.inventoryBerries = dto.inventoryBerries.copy();
		}

		if ( dto.declaredYieldField != null) {
			this.declaredYieldField = dto.declaredYieldField.copy();
		}		
		
		if ( dto.declaredYieldFieldForageList != null ) {			
			this.declaredYieldFieldForageList = new ArrayList<>();
			
			for ( DeclaredYieldFieldForageDto dyffDto : dto.declaredYieldFieldForageList ) {
				this.declaredYieldFieldForageList.add(dyffDto.copy());
			}
		}		
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.linkedCropVarietyId = dto.linkedCropVarietyId;
		this.linkedVarietyName = dto.linkedVarietyName;
		this.linkedFieldAcres = dto.linkedFieldAcres;
	
	}
	

	@Override
	public boolean equalsBK(InventoryFieldDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryFieldDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryFieldGuid", inventoryFieldGuid, other.inventoryFieldGuid);
			result = result&&dtoUtils.equals("insurancePlanId", insurancePlanId, other.insurancePlanId);
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
			result = result&&dtoUtils.equals("lastYearCropCommodityId", lastYearCropCommodityId, other.lastYearCropCommodityId);
			result = result&&dtoUtils.equals("lastYearCropVarietyId", lastYearCropVarietyId, other.lastYearCropVarietyId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("plantingNumber", plantingNumber, other.plantingNumber);
			result = result&&dtoUtils.equals("isHiddenOnPrintoutInd", isHiddenOnPrintoutInd, other.isHiddenOnPrintoutInd);
			result = result&&dtoUtils.equals("underseededCropVarietyId", underseededCropVarietyId, other.underseededCropVarietyId);
			result = result&&dtoUtils.equals("underseededAcres", underseededAcres, other.underseededAcres, 4);
			result = result&&dtoUtils.equals("underseededInventorySeededForageGuid", underseededInventorySeededForageGuid, other.underseededInventorySeededForageGuid);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryFieldDto copy() {
		return new InventoryFieldDto(this);
	}
	
	public String getInventoryFieldGuid() {
		return inventoryFieldGuid;
	}
	public void setInventoryFieldGuid(String inventoryFieldGuid) {
		this.inventoryFieldGuid = inventoryFieldGuid;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getLastYearCropCommodityId() {
		return lastYearCropCommodityId;
	}
	public void setLastYearCropCommodityId(Integer lastYearCropCommodityId) {
		this.lastYearCropCommodityId = lastYearCropCommodityId;
	}
	
	public String getLastYearCropCommodityName() {
		return lastYearCropCommodityName;
	}
	public void setLastYearCropCommodityName(String lastYearCropCommodityName) {
		this.lastYearCropCommodityName = lastYearCropCommodityName;
	}
	
	public Integer getLastYearCropVarietyId() {
		return lastYearCropVarietyId;
	}
	public void setLastYearCropVarietyId(Integer lastYearCropVarietyId) {
		this.lastYearCropVarietyId = lastYearCropVarietyId;
	}

	public String getLastYearCropVarietyName() {
		return lastYearCropVarietyName;
	}
	public void setLastYearCropVarietyName(String lastYearCropVarietyName) {
		this.lastYearCropVarietyName = lastYearCropVarietyName;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}
	
	public Integer getPlantingNumber() {
		return plantingNumber;
	}
	public void setPlantingNumber(Integer plantingNumber) {
		this.plantingNumber = plantingNumber;
	}
	
	public Boolean getIsHiddenOnPrintoutInd() {
		return isHiddenOnPrintoutInd;
	}

	public void setIsHiddenOnPrintoutInd(Boolean isHiddenOnPrintoutInd) {
		this.isHiddenOnPrintoutInd = isHiddenOnPrintoutInd;
	}

	public InventoryUnseededDto getInventoryUnseeded() {
		return inventoryUnseeded;
	}
	public void setInventoryUnseeded(InventoryUnseededDto inventoryUnseeded) {
		this.inventoryUnseeded = inventoryUnseeded;
	}

	public List<InventorySeededGrainDto> getInventorySeededGrains() {
		return inventorySeededGrains;
	}
	public void setInventorySeededGrains(List<InventorySeededGrainDto> inventorySeededGrains) {
		this.inventorySeededGrains = inventorySeededGrains;
	}

	public List<InventorySeededForageDto> getInventorySeededForages() {
		return inventorySeededForages;
	}
	public void setInventorySeededForages(List<InventorySeededForageDto> inventorySeededForages) {
		this.inventorySeededForages = inventorySeededForages;
	}

	public InventoryBerriesDto getInventoryBerries() {
		return inventoryBerries;
	}
	public void setInventoryBerries(InventoryBerriesDto inventoryBerries) {
		this.inventoryBerries = inventoryBerries;
	}

	public DeclaredYieldFieldDto getDeclaredYieldField() {
		return declaredYieldField;
	}
	public void setDeclaredYieldField(DeclaredYieldFieldDto declaredYieldField) {
		this.declaredYieldField = declaredYieldField;
	}
	
 	public List<DeclaredYieldFieldForageDto> getDeclaredYieldFieldForageList() {
		return declaredYieldFieldForageList;
	}
	public void setDeclaredYieldFieldForageList(List<DeclaredYieldFieldForageDto> declaredYieldFieldForageList) {
		this.declaredYieldFieldForageList = declaredYieldFieldForageList;
	}

	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
 
 	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
 
 	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
 
 	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Double getAcresToBeSeeded() {
		return acresToBeSeeded;
	}
	public void setAcresToBeSeeded(Double acresToBeSeeded) {
		this.acresToBeSeeded = acresToBeSeeded;
	}

	public Boolean getIsGrainUnseededDefaultInd() {
		return isGrainUnseededDefaultInd;
	}

	public void setIsGrainUnseededDefaultInd(Boolean isGrainUnseededDefaultInd) {
		this.isGrainUnseededDefaultInd = isGrainUnseededDefaultInd;
	}

 	public Integer getUnderseededCropVarietyId() {
		return underseededCropVarietyId;
	}

	public void setUnderseededCropVarietyId(Integer underseededCropVarietyId) {
		this.underseededCropVarietyId = underseededCropVarietyId;
	}
	
	public String getUnderseededCropVarietyName() {
		return underseededCropVarietyName;
	}
	public void setUnderseededCropVarietyName(String underseededCropVarietyName) {
		this.underseededCropVarietyName = underseededCropVarietyName;
	}
 
 	public Double getUnderseededAcres() {
		return underseededAcres;
	}

	public void setUnderseededAcres(Double underseededAcres) {
		this.underseededAcres = underseededAcres;
	}
	
	public String getUnderseededInventorySeededForageGuid() {
		return underseededInventorySeededForageGuid;
	}
	
	public void setUnderseededInventorySeededForageGuid(String underseededInventorySeededForageGuid) {
		this.underseededInventorySeededForageGuid = underseededInventorySeededForageGuid;
	}

	public Integer getLinkedCropVarietyId() {
		return linkedCropVarietyId;
	}

	public void setLinkedCropVarietyId(Integer linkedCropVarietyId) {
		this.linkedCropVarietyId = linkedCropVarietyId;
	}

	public String getLinkedVarietyName() {
		return linkedVarietyName;
	}

	public void setLinkedVarietyName(String linkedVarietyName) {
		this.linkedVarietyName = linkedVarietyName;
	}

	public Double getLinkedFieldAcres() {
		return linkedFieldAcres;
	}

	public void setLinkedFieldAcres(Double linkedFieldAcres) {
		this.linkedFieldAcres = linkedFieldAcres;
	}

}
