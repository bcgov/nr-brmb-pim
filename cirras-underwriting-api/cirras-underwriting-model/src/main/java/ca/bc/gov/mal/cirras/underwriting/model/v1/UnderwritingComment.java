package ca.bc.gov.mal.cirras.underwriting.model.v1;

import java.io.Serializable;
import java.util.Date;


//
// This is not going to be a resource.
//
public class UnderwritingComment implements Serializable {
	private static final long serialVersionUID = 1L;

	private String underwritingCommentGuid;
	private Integer annualFieldDetailId;
	private String underwritingCommentTypeCode;
	private String underwritingCommentTypeDesc;
	private String underwritingComment;
	private Integer growerContractYearId;
	private String declaredYieldContractGuid;
	private String verifiedYieldSummaryGuid;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	private Boolean deletedByUserInd;
	private Boolean userCanEditInd;
	private Boolean userCanDeleteInd;


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

	public Boolean getDeletedByUserInd() {
		return deletedByUserInd;
	}
	public void setDeletedByUserInd(Boolean deletedByUserInd) {
		this.deletedByUserInd = deletedByUserInd;
	}
	
	public Boolean getUserCanEditInd() {
		return userCanEditInd;
	}
	public void setUserCanEditInd(Boolean userCanEditInd) {
		this.userCanEditInd = userCanEditInd;
	}
	
	public Boolean getUserCanDeleteInd() {
		return userCanDeleteInd;
	}
	public void setUserCanDeleteInd(Boolean userCanDeleteInd) {
		this.userCanDeleteInd = userCanDeleteInd;
	}
	
}
