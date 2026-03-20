package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class DeclaredYieldFieldCommodityBerriesDto extends BaseDto<DeclaredYieldFieldCommodityBerriesDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldCommodityBerriesDto.class);

	private String declaredYieldFieldCommodityBerriesGuid;
	private Integer fieldId;
	private Integer cropCommodityId;
	private Integer cropYear;
	private Double totalProduction;
	private Double totalProductionOverride;
	private Double totalPlantedAcres;
	private Double totalMatureEquivalentAcres;

	private List<DeclaredYieldFieldVarietyBerriesDto> declaredYieldFieldVarietyBerriesList = new ArrayList<DeclaredYieldFieldVarietyBerriesDto>();
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private String cropCommodityName;
	
	public DeclaredYieldFieldCommodityBerriesDto() {
	}
	
	
	public DeclaredYieldFieldCommodityBerriesDto(DeclaredYieldFieldCommodityBerriesDto dto) {

		this.declaredYieldFieldCommodityBerriesGuid = dto.declaredYieldFieldCommodityBerriesGuid;
		this.fieldId = dto.fieldId;
		this.cropCommodityId = dto.cropCommodityId;
		this.cropYear = dto.cropYear;
		this.totalProduction = dto.totalProduction;
		this.totalProductionOverride = dto.totalProductionOverride;
		this.totalPlantedAcres = dto.totalPlantedAcres;
		this.totalMatureEquivalentAcres = dto.totalMatureEquivalentAcres;

		if ( dto.declaredYieldFieldVarietyBerriesList != null ) {			
			this.declaredYieldFieldVarietyBerriesList = new ArrayList<>();
			
			for ( DeclaredYieldFieldVarietyBerriesDto dyfvbDto : dto.declaredYieldFieldVarietyBerriesList ) {
				this.declaredYieldFieldVarietyBerriesList.add(dyfvbDto.copy());
			}
		}
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
		
		this.cropCommodityName = dto.cropCommodityName;
		
	}
	

	@Override
	public boolean equalsBK(DeclaredYieldFieldCommodityBerriesDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(DeclaredYieldFieldCommodityBerriesDto other) {
		boolean result = false;
		
		if(other!=null) {
			Integer decimalPrecision = 4;
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("declaredYieldFieldCommodityBerriesGuid", declaredYieldFieldCommodityBerriesGuid, other.declaredYieldFieldCommodityBerriesGuid);
			result = result&&dtoUtils.equals("fieldId", fieldId, other.fieldId);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("cropYear", cropYear, other.cropYear);
			result = result&&dtoUtils.equals("totalProduction", totalProduction, other.totalProduction, decimalPrecision);
			result = result&&dtoUtils.equals("totalProductionOverride", totalProductionOverride, other.totalProductionOverride, decimalPrecision);
			result = result&&dtoUtils.equals("totalPlantedAcres", totalPlantedAcres, other.totalPlantedAcres, decimalPrecision);
			result = result&&dtoUtils.equals("totalMatureEquivalentAcres", totalMatureEquivalentAcres, other.totalMatureEquivalentAcres, decimalPrecision);
		}
		
		return result;
	}

	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public DeclaredYieldFieldCommodityBerriesDto copy() {
		return new DeclaredYieldFieldCommodityBerriesDto(this);
	}
	 

	public String getDeclaredYieldFieldCommodityBerriesGuid() {
		return declaredYieldFieldCommodityBerriesGuid;
	}

	public void setDeclaredYieldFieldCommodityBerriesGuid(String declaredYieldFieldCommodityBerriesGuid) {
		this.declaredYieldFieldCommodityBerriesGuid = declaredYieldFieldCommodityBerriesGuid;
	}

	public Integer getFieldId() {
		return fieldId;
	}

	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}	
	
	public Double getTotalProduction() {
		return totalProduction;
	}

	public void setTotalProduction(Double totalProduction) {
		this.totalProduction = totalProduction;
	}

	public Double getTotalProductionOverride() {
		return totalProductionOverride;
	}

	public void setTotalProductionOverride(Double totalProductionOverride) {
		this.totalProductionOverride = totalProductionOverride;
	}

	public Double getTotalPlantedAcres() {
		return totalPlantedAcres;
	}

	public void setTotalPlantedAcres(Double totalPlantedAcres) {
		this.totalPlantedAcres = totalPlantedAcres;
	}

	public Double getTotalMatureEquivalentAcres() {
		return totalMatureEquivalentAcres;
	}

	public void setTotalMatureEquivalentAcres(Double totalMatureEquivalentAcres) {
		this.totalMatureEquivalentAcres = totalMatureEquivalentAcres;
	}
	
	public List<DeclaredYieldFieldVarietyBerriesDto> getDeclaredYieldFieldVarietyBerriesList() {
		return declaredYieldFieldVarietyBerriesList;
	}

	public void setDeclaredYieldFieldVarietyBerriesList(List<DeclaredYieldFieldVarietyBerriesDto> declaredYieldFieldVarietyBerriesList) {
		this.declaredYieldFieldVarietyBerriesList = declaredYieldFieldVarietyBerriesList;
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
 
	public String getCropCommodityName() {
		return cropCommodityName;
	}

	public void setCropCommodityName(String cropCommodityName) {
		this.cropCommodityName = cropCommodityName;
	}
}
