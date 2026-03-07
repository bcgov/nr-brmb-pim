package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class CommodityMaturityScaleDto extends BaseDto<CommodityMaturityScaleDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CommodityMaturityScaleDto.class);

	private String commodityMaturityScaleGuid;
	private Integer cropCommodityId;
	private Integer plantAge;
	private Double scale;
	private Integer versionNumber;
	private Integer effectiveCropYear;
	private Integer expiryCropYear;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	
	public CommodityMaturityScaleDto() {
	}
	
	
	public CommodityMaturityScaleDto(CommodityMaturityScaleDto dto) {

		this.commodityMaturityScaleGuid = dto.commodityMaturityScaleGuid;
		this.cropCommodityId = dto.cropCommodityId;
		this.plantAge = dto.plantAge;
		this.scale = dto.scale;
		this.versionNumber = dto.versionNumber;
		this.effectiveCropYear = dto.effectiveCropYear;
		this.expiryCropYear = dto.expiryCropYear;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;	
	}
	

	@Override
	public boolean equalsBK(CommodityMaturityScaleDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CommodityMaturityScaleDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("commodityMaturityScaleGuid", commodityMaturityScaleGuid, other.commodityMaturityScaleGuid);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("plantAge", plantAge, other.plantAge);
			result = result&&dtoUtils.equals("scale", scale, other.scale, 4);
			result = result&&dtoUtils.equals("versionNumber", versionNumber, other.versionNumber);
			result = result&&dtoUtils.equals("effectiveCropYear", effectiveCropYear, other.effectiveCropYear);
			result = result&&dtoUtils.equals("expiryCropYear", expiryCropYear, other.expiryCropYear);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public CommodityMaturityScaleDto copy() {
		return new CommodityMaturityScaleDto(this);
	}
	public String getCommodityMaturityScaleGuid() {
		return commodityMaturityScaleGuid;
	}

	public void setCommodityMaturityScaleGuid(String commodityMaturityScaleGuid) {
		this.commodityMaturityScaleGuid = commodityMaturityScaleGuid;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getPlantAge() {
		return plantAge;
	}

	public void setPlantAge(Integer plantAge) {
		this.plantAge = plantAge;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getEffectiveCropYear() {
		return effectiveCropYear;
	}

	public void setEffectiveCropYear(Integer effectiveCropYear) {
		this.effectiveCropYear = effectiveCropYear;
	}

	public Integer getExpiryCropYear() {
		return expiryCropYear;
	}

	public void setExpiryCropYear(Integer expiryCropYear) {
		this.expiryCropYear = expiryCropYear;
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
