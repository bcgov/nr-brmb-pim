package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.utils.DateUtils;
import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldContractDto extends BaseDto<DeclaredYieldContractDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractDto.class);

	private String declaredYieldContractGuid;
	private Integer contractId;
	private Integer cropYear;
	private Date declarationOfProductionDate;
	private Date dopUpdateTimestamp;
	private String dopUpdateUser;
	private String enteredYieldMeasUnitTypeCode;
	private String defaultYieldMeasUnitTypeCode;
	private Boolean grainFromOtherSourceInd;
	private String balerWagonInfo;
	private Integer totalLivestock;
	
	private List<ContractedFieldDetailDto> fields = new ArrayList<ContractedFieldDetailDto>();
	private List<DeclaredYieldFieldRollupDto> declaredYieldFieldRollupList = new ArrayList<DeclaredYieldFieldRollupDto>();
	private List<DeclaredYieldContractCommodityDto> declaredYieldContractCommodities = new ArrayList<DeclaredYieldContractCommodityDto>();
	private List<UnderwritingCommentDto> uwComments = new ArrayList<UnderwritingCommentDto>();
	private List<DeclaredYieldContractCommodityForageDto> declaredYieldContractCommodityForageList = new ArrayList<DeclaredYieldContractCommodityForageDto>();
	private List<DeclaredYieldFieldRollupForageDto> declaredYieldFieldRollupForageList = new ArrayList<DeclaredYieldFieldRollupForageDto>();
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	//Extended columns
	private Integer insurancePlanId;
	private Integer growerContractYearId;
	
	public DeclaredYieldContractDto() {
	}
	
	
	public DeclaredYieldContractDto(DeclaredYieldContractDto dto) {

		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.contractId = dto.contractId;
		this.cropYear = dto.cropYear;
		this.declarationOfProductionDate = dto.declarationOfProductionDate;
		this.dopUpdateTimestamp = dto.dopUpdateTimestamp;
		this.dopUpdateUser = dto.dopUpdateUser;
		this.enteredYieldMeasUnitTypeCode = dto.enteredYieldMeasUnitTypeCode;
		this.defaultYieldMeasUnitTypeCode = dto.defaultYieldMeasUnitTypeCode;
		this.grainFromOtherSourceInd = dto.grainFromOtherSourceInd;
		this.balerWagonInfo = dto.balerWagonInfo;
		this.totalLivestock = dto.totalLivestock;
		
		if ( dto.fields != null ) {			
			this.fields = new ArrayList<>();
			
			for ( ContractedFieldDetailDto cfdDto : dto.fields ) {
				this.fields.add(cfdDto.copy());
			}
		}
		
		if ( dto.declaredYieldFieldRollupList != null ) {			
			this.declaredYieldFieldRollupList = new ArrayList<>();
			
			for ( DeclaredYieldFieldRollupDto dyfDto : dto.declaredYieldFieldRollupList ) {
				this.declaredYieldFieldRollupList.add(dyfDto.copy());
			}
		}	
				
		if ( dto.declaredYieldContractCommodities != null ) {			
			this.declaredYieldContractCommodities = new ArrayList<>();
			
			for ( DeclaredYieldContractCommodityDto dyfDto : dto.declaredYieldContractCommodities ) {
				this.declaredYieldContractCommodities.add(dyfDto.copy());
			}
		}	
		
		if ( dto.declaredYieldContractCommodityForageList != null ) {			
			this.declaredYieldContractCommodityForageList = new ArrayList<>();
			
			for ( DeclaredYieldContractCommodityForageDto dyccfDto : dto.declaredYieldContractCommodityForageList ) {
				this.declaredYieldContractCommodityForageList.add(dyccfDto.copy());
			}
		}	
		
		if ( dto.declaredYieldFieldRollupForageList != null ) {			
			this.declaredYieldFieldRollupForageList = new ArrayList<>();
			
			for ( DeclaredYieldFieldRollupForageDto dyccfDto : dto.declaredYieldFieldRollupForageList ) {
				this.declaredYieldFieldRollupForageList.add(dyccfDto.copy());
			}
		}	
		
		if ( dto.uwComments != null ) {			
			this.uwComments = new ArrayList<>();
			
			for ( UnderwritingCommentDto uwcDto : dto.uwComments ) {
				this.uwComments.add(uwcDto.copy());
			}
		}
		
				
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

		this.insurancePlanId = dto.insurancePlanId;
		this.growerContractYearId = dto.growerContractYearId;
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldContractDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldContractDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);

			result = result&&DateUtils.equalsDate(logger, "declarationOfProductionDate", declarationOfProductionDate, other.declarationOfProductionDate);
			result = result&&DateUtils.equalsDate(logger, "dopUpdateTimestamp", dopUpdateTimestamp, other.dopUpdateTimestamp);

			result = result&&dtoUtils.equals("dopUpdateUser", dopUpdateUser, other.dopUpdateUser);
			result = result&&dtoUtils.equals("enteredYieldMeasUnitTypeCode", enteredYieldMeasUnitTypeCode, other.enteredYieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("defaultYieldMeasUnitTypeCode", defaultYieldMeasUnitTypeCode, other.defaultYieldMeasUnitTypeCode);
			result = result&&dtoUtils.equals("grainFromOtherSourceInd", grainFromOtherSourceInd, other.grainFromOtherSourceInd);
			result = result&&dtoUtils.equals("balerWagonInfo", balerWagonInfo, other.balerWagonInfo);
			result = result&&dtoUtils.equals("totalLivestock", totalLivestock, other.totalLivestock);

		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldContractDto copy() {
		return new DeclaredYieldContractDto(this);
	}
	
 	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
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
 
 	public Date getDeclarationOfProductionDate() {
		return declarationOfProductionDate;
	}
	public void setDeclarationOfProductionDate(Date declarationOfProductionDate) {
		this.declarationOfProductionDate = declarationOfProductionDate;
	}
 
 	public Date getDopUpdateTimestamp() {
		return dopUpdateTimestamp;
	}
	public void setDopUpdateTimestamp(Date dopUpdateTimestamp) {
		this.dopUpdateTimestamp = dopUpdateTimestamp;
	}
 
 	public String getDopUpdateUser() {
		return dopUpdateUser;
	}
	public void setDopUpdateUser(String dopUpdateUser) {
		this.dopUpdateUser = dopUpdateUser;
	}
 
 	public String getEnteredYieldMeasUnitTypeCode() {
		return enteredYieldMeasUnitTypeCode;
	}
	public void setEnteredYieldMeasUnitTypeCode(String enteredYieldMeasUnitTypeCode) {
		this.enteredYieldMeasUnitTypeCode = enteredYieldMeasUnitTypeCode;
	}
 
 	public String getDefaultYieldMeasUnitTypeCode() {
		return defaultYieldMeasUnitTypeCode;
	}
	public void setDefaultYieldMeasUnitTypeCode(String defaultYieldMeasUnitTypeCode) {
		this.defaultYieldMeasUnitTypeCode = defaultYieldMeasUnitTypeCode;
	}
 
 	public Boolean getGrainFromOtherSourceInd() {
		return grainFromOtherSourceInd;
	}
	public void setGrainFromOtherSourceInd(Boolean grainFromOtherSourceInd) {
		this.grainFromOtherSourceInd = grainFromOtherSourceInd;
	}

	public String getBalerWagonInfo() {
		return balerWagonInfo;
	}

	public void setBalerWagonInfo(String balerWagonInfo) {
		this.balerWagonInfo = balerWagonInfo;
	}

	public Integer getTotalLivestock() {
		return totalLivestock;
	}

	public void setTotalLivestock(Integer totalLivestock) {
		this.totalLivestock = totalLivestock;
	}

	public List<ContractedFieldDetailDto> getFields() {
		return fields;
	}
	public void setFields(List<ContractedFieldDetailDto> fields) {
		this.fields = fields;
	}

	public List<DeclaredYieldFieldRollupDto> getDeclaredYieldFieldRollupList() {
		return declaredYieldFieldRollupList;
	}
	public void setDeclaredYieldFieldRollupList(List<DeclaredYieldFieldRollupDto> declaredYieldFieldRollupList) {
		this.declaredYieldFieldRollupList = declaredYieldFieldRollupList;
	}
	
	public List<UnderwritingCommentDto> getUwComments() {
		return uwComments;
	}
	public void setUwComments(List<UnderwritingCommentDto> uwComments) {
		this.uwComments = uwComments;
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

	public Integer getInsurancePlanId() {
		return insurancePlanId;
	}
	public void setInsurancePlanId(Integer insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}
	
	public List<DeclaredYieldContractCommodityDto> getDeclaredYieldContractCommodities() {
		return declaredYieldContractCommodities;
	}
	public void setDeclaredYieldContractCommodities(List<DeclaredYieldContractCommodityDto> declaredYieldContractCommodities) {
		this.declaredYieldContractCommodities = declaredYieldContractCommodities;
	}
	
	public List<DeclaredYieldContractCommodityForageDto> getDeclaredYieldContractCommodityForageList() {
		return declaredYieldContractCommodityForageList;
	}
	public void setDeclaredYieldContractCommodityForageList(List<DeclaredYieldContractCommodityForageDto> declaredYieldContractCommodityForageList) {
		this.declaredYieldContractCommodityForageList = declaredYieldContractCommodityForageList;
	}	
	
	public List<DeclaredYieldFieldRollupForageDto> getDeclaredYieldFieldRollupForageList() {
		return declaredYieldFieldRollupForageList;
	}
	public void setDeclaredYieldFieldRollupForageList(List<DeclaredYieldFieldRollupForageDto> declaredYieldFieldRollupForageList) {
		this.declaredYieldFieldRollupForageList = declaredYieldFieldRollupForageList;
	}

	public Integer getGrowerContractYearId() {
		return growerContractYearId;
	}
	public void setGrowerContractYearId(Integer growerContractYearId) {
		this.growerContractYearId = growerContractYearId;
	}
	
	
}
