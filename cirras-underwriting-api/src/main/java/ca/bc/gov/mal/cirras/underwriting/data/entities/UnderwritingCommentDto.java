package ca.bc.gov.mal.cirras.underwriting.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;



public class UnderwritingCommentDto extends BaseDto<UnderwritingCommentDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingCommentDto.class);

	private String underwritingCommentGuid;
	private Integer annualFieldDetailId;
	private String underwritingCommentTypeCode;
	private String underwritingCommentTypeDesc;
	private String underwritingComment;

	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	private Integer growerContractYearId;
	private String declaredYieldContractGuid;
	private String verifiedYieldSummaryGuid;
	
	public UnderwritingCommentDto() {
	}
	
	
	public UnderwritingCommentDto(UnderwritingCommentDto dto) {
		this.underwritingCommentGuid = dto.underwritingCommentGuid;
		this.annualFieldDetailId = dto.annualFieldDetailId;
		this.underwritingCommentTypeCode = dto.underwritingCommentTypeCode;
		this.underwritingCommentTypeDesc = dto.underwritingCommentTypeDesc;
		this.underwritingComment = dto.underwritingComment;
		
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;	
		
		this.growerContractYearId = dto.growerContractYearId;
		this.declaredYieldContractGuid = dto.declaredYieldContractGuid;
		this.verifiedYieldSummaryGuid = dto.verifiedYieldSummaryGuid;

	}
	

	@Override
	public boolean equalsBK(UnderwritingCommentDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(UnderwritingCommentDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("underwritingCommentGuid", underwritingCommentGuid, other.underwritingCommentGuid);
			result = result&&dtoUtils.equals("annualFieldDetailId", annualFieldDetailId, other.annualFieldDetailId);
			result = result&&dtoUtils.equals("underwritingCommentTypeCode", underwritingCommentTypeCode, other.underwritingCommentTypeCode);
			result = result&&dtoUtils.equals("underwritingComment", underwritingComment, other.underwritingComment);
			result = result&&dtoUtils.equals("growerContractYearId", growerContractYearId, other.growerContractYearId);
			result = result&&dtoUtils.equals("declaredYieldContractGuid", declaredYieldContractGuid, other.declaredYieldContractGuid);
			result = result&&dtoUtils.equals("verifiedYieldSummaryGuid", verifiedYieldSummaryGuid, other.verifiedYieldSummaryGuid);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public UnderwritingCommentDto copy() {
		return new UnderwritingCommentDto(this);
	}
	
	public String getUnderwritingCommentGuid() {
		return underwritingCommentGuid;
	}
	public void setUnderwritingCommentGuid(String underwritingCommentGuid) {
		this.underwritingCommentGuid = underwritingCommentGuid;
	}
	
	public Integer getAnnualFieldDetailId() {
		return annualFieldDetailId;
	}

	public void setAnnualFieldDetailId(Integer annualFieldDetailId) {
		this.annualFieldDetailId = annualFieldDetailId;
	}
 
	public String getUnderwritingCommentTypeCode() {
		return underwritingCommentTypeCode;
	}
	public void setUnderwritingCommentTypeCode(String underwritingCommentTypeCode) {
		this.underwritingCommentTypeCode = underwritingCommentTypeCode;
	}
	
	public String getUnderwritingCommentTypeDesc() {
		return underwritingCommentTypeDesc;
	}
	public void setUnderwritingCommentTypeDesc(String underwritingCommentTypeDesc) {
		this.underwritingCommentTypeDesc = underwritingCommentTypeDesc;
	}
	
	public String getUnderwritingComment() {
		return underwritingComment;
	}
	public void setUnderwritingComment(String underwritingComment) {
		this.underwritingComment = underwritingComment;
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
		
	public String getDeclaredYieldContractGuid() {
		return declaredYieldContractGuid;
	}
	public void setDeclaredYieldContractGuid(String declaredYieldContractGuid) {
		this.declaredYieldContractGuid = declaredYieldContractGuid;
	}

	public String getVerifiedYieldSummaryGuid() {
		return verifiedYieldSummaryGuid;
	}

	public void setVerifiedYieldSummaryGuid(String verifiedYieldSummaryGuid) {
		this.verifiedYieldSummaryGuid = verifiedYieldSummaryGuid;
	}
 
}
