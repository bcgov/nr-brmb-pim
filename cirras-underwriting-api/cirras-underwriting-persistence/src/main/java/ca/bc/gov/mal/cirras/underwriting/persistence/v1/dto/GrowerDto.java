package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class GrowerDto extends BaseDto<GrowerDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GrowerDto.class);

	private Integer growerId;
	private Integer growerNumber;
	private String growerName;
	private String growerAddressLine1;
	private String growerAddressLine2;
	private String growerPostalCode;
	private String growerCity;
	private Integer cityId;
	private String growerProvince;
	private Date dataSyncTransDate;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;

	public GrowerDto() {
	}
	
	
	public GrowerDto(GrowerDto dto) {

		this.growerId = dto.growerId;
		this.growerNumber = dto.growerNumber;
		this.growerName = dto.growerName;
		this.growerAddressLine1 = dto.growerAddressLine1;
		this.growerAddressLine2 = dto.growerAddressLine2;
		this.growerPostalCode = dto.growerPostalCode;
		this.growerCity = dto.growerCity;
		this.cityId = dto.cityId;
		this.growerProvince = dto.growerProvince;
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(GrowerDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(GrowerDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("growerId", growerId, other.growerId);
			result = result&&dtoUtils.equals("growerNumber", growerNumber, other.growerNumber);
			result = result&&dtoUtils.equals("growerName", growerName, other.growerName);
			result = result&&dtoUtils.equals("growerAddressLine1", growerAddressLine1, other.growerAddressLine1);
			result = result&&dtoUtils.equals("growerAddressLine2", growerAddressLine2, other.growerAddressLine2);
			result = result&&dtoUtils.equals("growerPostalCode", growerPostalCode, other.growerPostalCode);
			result = result&&dtoUtils.equals("growerCity", growerCity, other.growerCity);
			result = result&&dtoUtils.equals("cityId", cityId, other.cityId);
			result = result&&dtoUtils.equals("growerProvince", growerProvince, other.growerProvince);
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
	public GrowerDto copy() {
		return new GrowerDto(this);
	}

 	public Integer getGrowerId() {
		return growerId;
	}

	public void setGrowerId(Integer growerId) {
		this.growerId = growerId;
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
 
 	public String getGrowerAddressLine1() {
		return growerAddressLine1;
	}

	public void setGrowerAddressLine1(String growerAddressLine1) {
		this.growerAddressLine1 = growerAddressLine1;
	}
 
 	public String getGrowerAddressLine2() {
		return growerAddressLine2;
	}

	public void setGrowerAddressLine2(String growerAddressLine2) {
		this.growerAddressLine2 = growerAddressLine2;
	}
 
 	public String getGrowerPostalCode() {
		return growerPostalCode;
	}

	public void setGrowerPostalCode(String growerPostalCode) {
		this.growerPostalCode = growerPostalCode;
	}
 
 	public String getGrowerCity() {
		return growerCity;
	}

	public void setGrowerCity(String growerCity) {
		this.growerCity = growerCity;
	}
 
 	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
 
 	public String getGrowerProvince() {
		return growerProvince;
	}

	public void setGrowerProvince(String growerProvince) {
		this.growerProvince = growerProvince;
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
