package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationBerriesSyncDto extends BaseDto<ClaimCalculationBerriesSyncDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesSyncDto.class);

	private String claimCalculationBerriesSyncGuid;
	private Integer cropCommodityId;
	private Integer contractId;
	private Integer cropYear;
	private String claimCalculationGuid;
	private String claimCalculationBerriesGuid;
	private Double totalYieldForCalculation;
	private String calculationStatusCode;
	private Integer calculationVersion;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public ClaimCalculationBerriesSyncDto() {
	}
	
	
	public ClaimCalculationBerriesSyncDto(ClaimCalculationBerriesSyncDto dto) {

		this.claimCalculationBerriesSyncGuid = dto.claimCalculationBerriesSyncGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.contractId = dto.contractId;
		this.cropYear = dto.cropYear;
		this.claimCalculationGuid = dto.claimCalculationGuid;
		this.claimCalculationBerriesGuid = dto.claimCalculationBerriesGuid;
		this.totalYieldForCalculation = dto.totalYieldForCalculation;
		this.calculationStatusCode = dto.calculationStatusCode;
		this.calculationVersion = dto.calculationVersion;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ClaimCalculationBerriesSyncDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationBerriesSyncDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("claimCalculationBerriesSyncGuid", claimCalculationBerriesSyncGuid, other.claimCalculationBerriesSyncGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("contractId", contractId, other.contractId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("claimCalculationGuid", claimCalculationGuid, other.claimCalculationGuid);
			result = result&&dtoUtils.equals("claimCalculationBerriesGuid", claimCalculationBerriesGuid, other.claimCalculationBerriesGuid);
			result = result&&dtoUtils.equals("totalYieldForCalculation", totalYieldForCalculation, other.totalYieldForCalculation, 4);
			result = result&&dtoUtils.equals("calculationStatusCode", calculationStatusCode, other.calculationStatusCode);
			result = result&&dtoUtils.equals("calculationVersion", calculationVersion, other.calculationVersion);
			result = result&&dtoUtils.equals("dataSyncTransDate",
					LocalDateTime.ofInstant(dataSyncTransDate.toInstant(), ZoneId.systemDefault()), 
					LocalDateTime.ofInstant(other.dataSyncTransDate.toInstant(), ZoneId.systemDefault()));
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationBerriesSyncDto copy() {
		return new ClaimCalculationBerriesSyncDto(this);
	}

	public String getClaimCalculationBerriesSyncGuid() {
		return claimCalculationBerriesSyncGuid;
	}

	public void setClaimCalculationBerriesSyncGuid(String claimCalculationBerriesSyncGuid) {
		this.claimCalculationBerriesSyncGuid = claimCalculationBerriesSyncGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
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

	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public String getClaimCalculationBerriesGuid() {
		return claimCalculationBerriesGuid;
	}

	public void setClaimCalculationBerriesGuid(String claimCalculationBerriesGuid) {
		this.claimCalculationBerriesGuid = claimCalculationBerriesGuid;
	}

	public Double getTotalYieldForCalculation() {
		return totalYieldForCalculation;
	}

	public void setTotalYieldForCalculation(Double totalYieldForCalculation) {
		this.totalYieldForCalculation = totalYieldForCalculation;
	}

	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	public Integer getCalculationVersion() {
		return calculationVersion;
	}

	public void setCalculationVersion(Integer calculationVersion) {
		this.calculationVersion = calculationVersion;
	}

	public Date getDataSyncTransDate() {
		return dataSyncTransDate;
	}

	public void setDataSyncTransDate(Date dataSyncTransDate) {
		this.dataSyncTransDate = dataSyncTransDate;
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

}
