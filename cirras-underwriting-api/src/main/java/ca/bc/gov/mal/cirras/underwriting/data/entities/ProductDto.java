package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class ProductDto extends BaseDto<ProductDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ProductDto.class);

	private Integer productId;
	private Integer policyId;
	private Integer cropCommodityId;
	private String commodityCoverageCode;
	private String productStatusCode;
	private Integer deductibleLevel;
	private Double productionGuarantee;
	private Double probableYield;
	private String insuredByMeasType;
	private Double insurableValueHundredPercent;
	private Double coverageDollars;
	
	private Date dataSyncTransDate;
	private Integer nonPedigreeCropCommodityId;
	private Boolean isPedigreeProduct;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public ProductDto() {
	}
	
	
	public ProductDto(ProductDto dto) {

		this.productId = dto.productId;
		this.policyId = dto.policyId;
		this.cropCommodityId = dto.cropCommodityId;
		this.commodityCoverageCode = dto.commodityCoverageCode;
		this.productStatusCode = dto.productStatusCode;
		this.deductibleLevel = dto.deductibleLevel;
		this.productionGuarantee = dto.productionGuarantee;
		this.probableYield = dto.probableYield;
		this.insuredByMeasType = dto.insuredByMeasType;
		this.insurableValueHundredPercent = dto.insurableValueHundredPercent;
		this.coverageDollars = dto.coverageDollars;
		
		this.dataSyncTransDate = dto.dataSyncTransDate;
		this.nonPedigreeCropCommodityId = dto.nonPedigreeCropCommodityId;
		this.isPedigreeProduct = dto.isPedigreeProduct;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;

	}
	

	@Override
	public boolean equalsBK(ProductDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ProductDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("productId", productId, other.productId);
			result = result&&dtoUtils.equals("policyId", policyId, other.policyId);
			result = result&&dtoUtils.equals("cropCommodityId", cropCommodityId, other.cropCommodityId);
			result = result&&dtoUtils.equals("commodityCoverageCode", commodityCoverageCode, other.commodityCoverageCode);
			result = result&&dtoUtils.equals("productStatusCode", productStatusCode, other.productStatusCode);
			result = result&&dtoUtils.equals("deductibleLevel", deductibleLevel, other.deductibleLevel);
			result = result&&dtoUtils.equals("productionGuarantee", productionGuarantee, other.productionGuarantee, 4);
			result = result&&dtoUtils.equals("probableYield", probableYield, other.probableYield, 4);
			result = result&&dtoUtils.equals("insuredByMeasType", insuredByMeasType, other.insuredByMeasType);
			result = result&&dtoUtils.equals("insurableValueHundredPercent", insurableValueHundredPercent, other.insurableValueHundredPercent, 4);
			result = result&&dtoUtils.equals("coverageDollars", coverageDollars, other.coverageDollars, 4);
						
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
	public ProductDto copy() {
		return new ProductDto(this);
	}
	
 	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}
	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public String getCommodityCoverageCode() {
		return commodityCoverageCode;
	}
	public void setCommodityCoverageCode(String commodityCoverageCode) {
		this.commodityCoverageCode = commodityCoverageCode;
	}

	public String getProductStatusCode() {
		return productStatusCode;
	}
	public void setProductStatusCode(String productStatusCode) {
		this.productStatusCode = productStatusCode;
	}

	public Integer getDeductibleLevel() {
		return deductibleLevel;
	}
	public void setDeductibleLevel(Integer deductibleLevel) {
		this.deductibleLevel = deductibleLevel;
	}

	public Double getProductionGuarantee() {
		return productionGuarantee;
	}
	public void setProductionGuarantee(Double productionGuarantee) {
		this.productionGuarantee = productionGuarantee;
	}

	public Double getProbableYield() {
		return probableYield;
	}
	public void setProbableYield(Double probableYield) {
		this.probableYield = probableYield;
	}

	public String getInsuredByMeasType() {
		return insuredByMeasType;
	}
	public void setInsuredByMeasType(String insuredByMeasType) {
		this.insuredByMeasType = insuredByMeasType;
	}

	public Double getInsurableValueHundredPercent() {
		return insurableValueHundredPercent;
	}

	public void setInsurableValueHundredPercent(Double insurableValueHundredPercent) {
		this.insurableValueHundredPercent = insurableValueHundredPercent;
	}

	public Double getCoverageDollars() {
		return coverageDollars;
	}

	public void setCoverageDollars(Double coverageDollars) {
		this.coverageDollars = coverageDollars;
	}
	
	public Integer getNonPedigreeCropCommodityId() {
		return nonPedigreeCropCommodityId;
	}

	public void setNonPedigreeCropCommodityId(Integer nonPedigreeCropCommodityId) {
		this.nonPedigreeCropCommodityId = nonPedigreeCropCommodityId;
	}

	public Boolean getIsPedigreeProduct() {
		return isPedigreeProduct;
	}

	public void setIsPedigreeProduct(Boolean isPedigreeProduct) {
		this.isPedigreeProduct = isPedigreeProduct;
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
