package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.utils.DateUtils;
import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class InventoryContractDto extends BaseDto<InventoryContractDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(InventoryContractDto.class);

	private String inventoryContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private Boolean unseededIntentionsSubmittedInd;
	private Boolean seededCropReportSubmittedInd;
	private Boolean fertilizerInd;
	private Boolean herbicideInd;
	private Boolean tilliageInd;
	private Boolean otherChangesInd;
	private String otherChangesComment;
	private Boolean grainFromPrevYearInd;
	private Date invUpdateTimestamp;
	private String invUpdateUser;


	private List<InventoryContractCommodityDto> commodities = new ArrayList<InventoryContractCommodityDto>();
	private List<InventoryCoverageTotalForageDto> inventoryCoverageTotalForages = new ArrayList<InventoryCoverageTotalForageDto>();
	private List<InventoryContractCommodityBerriesDto> inventoryContractCommodityBerries = new ArrayList<InventoryContractCommodityBerriesDto>();
	private List<ContractedFieldDetailDto> fields = new ArrayList<ContractedFieldDetailDto>();
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	private Integer growerContractYearId;
	private Integer insurancePlanId;
	private String insurancePlanName;
	private String policyNumber;
	private Integer growerNumber;
	private String growerName;

	
	public InventoryContractDto() {
	}
	
	
	public InventoryContractDto(InventoryContractDto dto) {

		this.inventoryContractGuid = dto.inventoryContractGuid;
		this.contractId = dto.contractId;
		this.cropYear = dto.cropYear;
		this.unseededIntentionsSubmittedInd = dto.unseededIntentionsSubmittedInd;
		this.seededCropReportSubmittedInd = dto.seededCropReportSubmittedInd;
		this.fertilizerInd = dto.fertilizerInd;
		this.herbicideInd = dto.herbicideInd;
		this.tilliageInd = dto.tilliageInd;
		this.otherChangesInd = dto.otherChangesInd;
		this.otherChangesComment = dto.otherChangesComment;
		this.grainFromPrevYearInd = dto.grainFromPrevYearInd;
		this.invUpdateTimestamp = dto.invUpdateTimestamp;
		this.invUpdateUser = dto.invUpdateUser;


		if ( dto.commodities != null ) {			
			this.commodities = new ArrayList<>();
			
			for ( InventoryContractCommodityDto iccDto : dto.commodities ) {
				this.commodities.add(iccDto.copy());
			}
		}

		if ( dto.inventoryCoverageTotalForages != null ) {
			this.inventoryCoverageTotalForages = new ArrayList<>();
			
			for ( InventoryCoverageTotalForageDto ictfDto : dto.inventoryCoverageTotalForages ) {
				this.inventoryCoverageTotalForages.add(ictfDto.copy());
			}
		}

		if ( dto.inventoryContractCommodityBerries != null ) {
			this.inventoryContractCommodityBerries = new ArrayList<>();
			
			for ( InventoryContractCommodityBerriesDto iccbDto : dto.inventoryContractCommodityBerries ) {
				this.inventoryContractCommodityBerries.add(iccbDto.copy());
			}
		}

		
		if ( dto.fields != null ) {			
			this.fields = new ArrayList<>();
			
			for ( ContractedFieldDetailDto cfdDto : dto.fields ) {
				this.fields.add(cfdDto.copy());
			}
		}		
				
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.growerContractYearId = dto.growerContractYearId;
		this.insurancePlanId = dto.insurancePlanId;
		this.insurancePlanName = dto.insurancePlanName;
		this.policyNumber = dto.policyNumber;
		this.growerNumber = dto.growerNumber;
		this.growerName = dto.growerName;

	}
	

	@Override
	public boolean equalsBK(InventoryContractDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(InventoryContractDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("inventoryContractGuid", inventoryContractGuid, other.inventoryContractGuid);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("unseededIntentionsSubmittedInd", unseededIntentionsSubmittedInd, other.unseededIntentionsSubmittedInd);
			result = result&&dtoUtils.equals("seededCropReportSubmittedInd", seededCropReportSubmittedInd, other.seededCropReportSubmittedInd);
			result = result&&dtoUtils.equals("fertilizerInd", fertilizerInd, other.fertilizerInd);
			result = result&&dtoUtils.equals("herbicideInd", herbicideInd, other.herbicideInd);
			result = result&&dtoUtils.equals("tilliageInd", tilliageInd, other.tilliageInd);
			result = result&&dtoUtils.equals("otherChangesInd", otherChangesInd, other.otherChangesInd);
			result = result&&dtoUtils.equals("otherChangesComment", otherChangesComment, other.otherChangesComment);
			result = result&&dtoUtils.equals("grainFromPrevYearInd", grainFromPrevYearInd, other.grainFromPrevYearInd);
			result = result&&DateUtils.equalsDate(logger, "invUpdateTimestamp", invUpdateTimestamp, other.invUpdateTimestamp);
			result = result&&dtoUtils.equals("invUpdateUser", invUpdateUser, other.invUpdateUser);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public InventoryContractDto copy() {
		return new InventoryContractDto(this);
	}
	
	public String getInventoryContractGuid() {
		return inventoryContractGuid;
	}
	public void setInventoryContractGuid(String inventoryContractGuid) {
		this.inventoryContractGuid = inventoryContractGuid;
	}

	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}
	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Boolean getUnseededIntentionsSubmittedInd() {
		return unseededIntentionsSubmittedInd;
	}
	public void setUnseededIntentionsSubmittedInd(Boolean unseededIntentionsSubmittedInd) {
		this.unseededIntentionsSubmittedInd = unseededIntentionsSubmittedInd;
	}

	public Boolean getSeededCropReportSubmittedInd() {
		return seededCropReportSubmittedInd;
	}
	public void setSeededCropReportSubmittedInd(Boolean seededCropReportSubmittedInd) {
		this.seededCropReportSubmittedInd = seededCropReportSubmittedInd;
	}

	public Boolean getFertilizerInd() {
		return fertilizerInd;
	}
	public void setFertilizerInd(Boolean fertilizerInd) {
		this.fertilizerInd = fertilizerInd;
	}

	public Boolean getHerbicideInd() {
		return herbicideInd;
	}
	public void setHerbicideInd(Boolean herbicideInd) {
		this.herbicideInd = herbicideInd;
	}

	public Boolean getTilliageInd() {
		return tilliageInd;
	}
	public void setTilliageInd(Boolean tilliageInd) {
		this.tilliageInd = tilliageInd;
	}

	public Boolean getOtherChangesInd() {
		return otherChangesInd;
	}
	public void setOtherChangesInd(Boolean otherChangesInd) {
		this.otherChangesInd = otherChangesInd;
	}

	public String getOtherChangesComment() {
		return otherChangesComment;
	}
	public void setOtherChangesComment(String otherChangesComment) {
		this.otherChangesComment = otherChangesComment;
	}

	public Boolean getGrainFromPrevYearInd() {
		return grainFromPrevYearInd;
	}
	public void setGrainFromPrevYearInd(Boolean grainFromPrevYearInd) {
		this.grainFromPrevYearInd = grainFromPrevYearInd;
	}

	public Date getInvUpdateTimestamp() {
		return invUpdateTimestamp;
	}

	public void setInvUpdateTimestamp(Date invUpdateTimestamp) {
		this.invUpdateTimestamp = invUpdateTimestamp;
	}

	public String getInvUpdateUser() {
		return invUpdateUser;
	}

	public void setInvUpdateUser(String invUpdateUser) {
		this.invUpdateUser = invUpdateUser;
	}

	public List<InventoryContractCommodityDto> getCommodities() {
		return commodities;
	}
	public void setCommodities(List<InventoryContractCommodityDto> commodities) {
		this.commodities = commodities;
	}

	public List<InventoryCoverageTotalForageDto> getInventoryCoverageTotalForages() {
		return inventoryCoverageTotalForages;
	}
	public void setInventoryCoverageTotalForages(List<InventoryCoverageTotalForageDto> inventoryCoverageTotalForages) {
		this.inventoryCoverageTotalForages = inventoryCoverageTotalForages;
	}

	public List<InventoryContractCommodityBerriesDto> getInventoryContractCommodityBerries() {
		return inventoryContractCommodityBerries;
	}
	public void setInventoryContractCommodityBerries(List<InventoryContractCommodityBerriesDto> inventoryContractCommodityBerries) {
		this.inventoryContractCommodityBerries = inventoryContractCommodityBerries;
	}

	public List<ContractedFieldDetailDto> getFields() {
		return fields;
	}
	public void setFields(List<ContractedFieldDetailDto> fields) {
		this.fields = fields;
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

	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
 	public String getInsurancePlanName() {
		return insurancePlanName;
	}

	public void setInsurancePlanName(String insurancePlanName) {
		this.insurancePlanName = insurancePlanName;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	 
	public Integer getGrowerNumber() {
		return growerNumber;
	}
	
	public void setGrowerNumber(Integer growerNumber) {
		this.growerNumber = growerNumber;
	}
	
		public String getGrowerName() {
		return growerName;
	}
	
	public void setGrowerName(String growerName) {
		this.growerName = growerName;
	}

}
