package ca.bc.gov.mal.cirras.underwriting.listener.api.rest.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeHierarchyListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeHierarchyRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;

import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;

public class WildfireHumanResourcesPayrollServiceStub {

	private static final Logger logger = LoggerFactory.getLogger(WildfireHumanResourcesPayrollServiceStub.class);

//	@Override
//	public EndpointsRsrc getTopLevelEndpoints() throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeTableListRsrc getCodeTables(EndpointsRsrc parent, String codeTableName, LocalDate effectiveAsOfDate)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeTableRsrc getCodeTable(CodeTableRsrc codeTable, LocalDate effectiveAsOfDate)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeTableRsrc updateCodeTable(CodeTableRsrc codeTable)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeHierarchyListRsrc getCodeHierarchys(EndpointsRsrc parent, String codeHierarchyName,
//			LocalDate effectiveAsOfDate) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeHierarchyRsrc getCodeHierarchy(CodeHierarchyRsrc codeHierarchy, LocalDate effectiveAsOfDate)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CodeHierarchyRsrc updateCodeHierarchy(CodeHierarchyRsrc codeHierarchy)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeListRsrc getChipsEmployees(EndpointsRsrc parent, LocalDate effectiveAsOfDate,
//			String[] payListIdentifier, String[] employeeNumber, String[] firstName, String[] lastName,
//			String typeAheadName, Boolean chipsUpdateReviewPendingInd, String employeeTypeCodeForTypeAhead,
//			String supervisorOrEAForEmployeeGuid, String[] benefitStatusCode, String[] employeeStatus,
//			String[] location, String[] jobClassificationCode, String[] jobClassificationStepCode,
//			String[] nonStandardHoursTypeCode, LocalDate expenseAuthorityAsOfDate, String[] webadeUserGuid,
//			Integer pageNumber, Integer pageRowCount, String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc createChipsEmployee(BaseResource parent, ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc getChipsEmployee(String employeeGuid)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc getChipsEmployee(ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc getChipsEmployeeByEmployeeNumber(String employeeNumber)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc updateChipsEmployee(ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteChipsEmployee(ChipsEmployeeRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc updateChipsEmployeePeriod(BaseResource parent, ChipsEmployeePeriodRsrc chipsEmployeePeriod)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc deleteChipsEmployeePeriod(BaseResource parent, LocalDate validStartDate,
//			LocalDate validEndDate) throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeeScheduleRsrc getEmployeeSchedule(ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeeScheduleRsrc updateEmployeeSchedule(EmployeeScheduleRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeeScheduleRsrc deleteEmployeeSchedulePeriod(BaseResource parent, LocalDate validStartDate,
//			LocalDate validEndDate) throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeePayrollDetailRsrc getEmployeePayrollDetail(ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeePayrollDetailRsrc updateEmployeePayrollDetail(EmployeePayrollDetailRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public AccountRegistrationRequestListRsrc getAccountRegistrationRequests(EndpointsRsrc parent,
//			String[] registrationEmailAddress, Instant effectiveAsOfTimestamp, Integer pageNumber, Integer pageRowCount,
//			String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public AccountRegistrationRequestRsrc createAccountRegistrationRequest(BaseResource parent,
//			AccountRegistrationRequestRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public AccountRegistrationRequestRsrc getAccountRegistrationRequest(AccountRegistrationRequestRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteAccountRegistrationRequest(AccountRegistrationRequestRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc registerAccount(AccountRegistrationRequestRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc unregisterAccount(ChipsEmployeeRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc getCurrentEmployee(EndpointsRsrc parent)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public ChipsEmployeeRsrc getCurrentEmployee(EndpointsRsrc parent, boolean calculateStatisticsInd)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayPeriodListRsrc getPayPeriods(EndpointsRsrc parent, LocalDate effectiveAsOfDate, String employeeGuid,
//			LocalDate employeeStartRangeDate, LocalDate employeeEndRangeDate, Boolean existsForEmployeeInd,
//			Integer pageNumber, Integer pageRowCount, String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayPeriodRsrc getPayPeriod(PayPeriodRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public WildFireProjectRsrc getWildFireProject(WildFireProjectRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public WildFireProjectListRsrc getWildFireProjects(EndpointsRsrc parent, LocalDate effectiveAsOfDate,
//			Long[] wildFireProjectId, String[] projectTypeCode, String[] ministryCode,
//			Long[] responsibilityCentreNumber, String[] searchText, Integer maximumRows, Integer pageNumber,
//			Integer pageRowCount, String[] orderBy)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryCommentListRsrc getDiaryComments(DiaryRsrc parent, String[] diaryCommentTypeCode,
//			Instant commentTimestamp, String[] userGuid, String[] userUserId, String[] searchText, Integer pageNumber,
//			Integer pageRowCount, String[] orderBy)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryCommentRsrc getDiaryComment(DiaryCommentRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryCommentRsrc createDiaryComment(DiaryRsrc parent, DiaryCommentRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryCommentRsrc updateDiaryComment(DiaryCommentRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteDiaryComment(DiaryCommentRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayCodeRsrc getPayCode(PayCodeRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayCodeListRsrc getPayCodes(EndpointsRsrc parent, LocalDate effectiveAsOfDate, String[] payCodeTypeCode,
//			String[] payCode, String[] orderBy)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryListRsrc getDiarysForEmployee(EndpointsRsrc parent, String ownerEmployeeGuid, Integer pageNumber,
//			Integer pageRowCount, String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc createDiary(BaseResource parent, DiaryRsrc resource, boolean standardHoursInd)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc getDiary(DiaryRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc updateDiary(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteDiary(DiaryRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiarySubmissionRsrc getDairySubmission(DiaryRsrc diary)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiarySubmissionRsrc getDairySubmission(DiarySubmissionRsrc dairySubmission)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc supervisorApproveDiaryEntires(DiaryRsrc resource, DiaryApprovalRsrc diaryApproval)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc supervisorRejectDiaryEntires(DiaryRsrc resource, DiaryApprovalRsrc diaryApproval)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc expenseAuthorityApproveDiaryEntries(DiaryRsrc resource, DiaryApprovalRsrc diaryApproval)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc expenseAuthorityRejectDiaryEntires(DiaryRsrc resource, DiaryApprovalRsrc diaryApproval,
//			Boolean rejectDiaryInd) throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc payrollCompleteDiaryEntires(DiaryRsrc resource, DiaryApprovalRsrc diaryApproval)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryValidationRsrc getDiaryValidation(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc submitDiary(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc recallDiary(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc retryAutomatedProcessingDiary(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc manuallyProcessDiary(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc createDiaryAmendment(DiaryRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc addDiaryDay(DiaryRsrc parent, DiaryDayRsrc resource, boolean standardHoursInd)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryValidationRsrc getDiaryDayValidation(DiaryDayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc getDiaryDay(DiaryDayRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc updateDiaryDay(DiaryDayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteDiaryDay(DiaryDayRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc createDiaryDayAmendment(DiaryDayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc submitDiaryDay(DiaryDayRsrc resourceDay)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryDayRsrc recallDiaryDay(DiaryDayRsrc resourceDay)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryEntryListRsrc updateDiaryEntrys(DiaryEntryListRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryEntryTemplateListRsrc getDiaryEntryTemplates(DiaryDayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public CalculationResultsRsrc calculateDiaryEntrys(DiaryDayRsrc parent, DiaryEntryTemplateListRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayListListRsrc getPayLists(EndpointsRsrc parent, Integer pageNumber, Integer pageRowCount, String[] orderBy,
//			Integer expand) throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayListRsrc createPayList(BaseResource parent, PayListRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayListRsrc getPayList(PayListRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayListRsrc updatePayList(PayListRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deletePayList(PayListRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public SubPayListRsrc getSubPayList(EndpointsRsrc parent, String[] searchText, String[] employeeGuid,
//			String[] employeeNumber, String[] substitutionTypeCode, String[] positionSubCode, String[] positionCode,
//			LocalDate validPeriodStartDate, LocalDate validPeriodEndDate, Integer pageNumber, Integer pageRowCount,
//			String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public SubPayRsrc createSubPay(BaseResource parent, SubPayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public SubPayRsrc getSubPay(SubPayRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public SubPayRsrc updateSubPay(SubPayRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public void deleteSubPay(SubPayRsrc resource) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeeDayEntryOptionsRsrc getEmployeeDayEntryOptions(BaseResource diaryDay)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public DiaryRsrc submitWpsDiary(WpsDiarySubmissionResultsRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public EmployeeScheduleRsrc updateEmployeeSchedulePeriod(BaseResource parent, EmployeeSchedulePeriodRsrc resource)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	private static Map<String, PayrollProcessingStatusRsrc> ppsRsrcByGuid = new HashMap<>();
//	
//	@Override
//	public PayrollProcessingStatusListRsrc getDiaryProcessingStatusList(String diaryGuid, Integer versionNumber)
//			throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public PayrollProcessingStatusRsrc getCurrentDiaryProcessingStatus(
//			String diaryGuid, Integer versionNumber) throws WildfireHumanResourcesPayrollServiceException {
//		logger.debug("<getCurrentDiaryProcessingStatus " + diaryGuid + "/" + versionNumber);
//
//		PayrollProcessingStatusRsrc ppsRsrc = ppsRsrcByGuid.get(diaryGuid + "/" + versionNumber);
//
//		logger.debug(">getCurrentDiaryProcessingStatus");
//		
//		return ppsRsrc;
//	}
//
//	@Override
//	public PayrollProcessingStatusRsrc readyForProcessing(
//			String diaryGuid, Integer versionNumber)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		logger.debug("<readyForProcessing " + diaryGuid + "/" + versionNumber);
//		
//		PayrollProcessingStatusRsrc result;
//		
//		result = new PayrollProcessingStatusRsrc();
//		result.setPayrollProcessingStatusCode(PayrollProcessingStatusCodes.ReadyForProcessing);
//		result.setDiaryGuid(diaryGuid);
//		result.setVersionNumber(versionNumber);
//		
//		ppsRsrcByGuid.put(diaryGuid + "/" + versionNumber, result);
//		
//		logger.debug(">readyForProcessing");
//
//		return result;
//	}
//
//	@Override
//	public <T> T getPreviousPage(T pagedResource, Class<T> clazz) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public <T> T getNextPage(T pagedResource, Class<T> clazz) throws WildfireHumanResourcesPayrollServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	@Override
//	public String getSwaggerString() throws RestClientServiceException {
//		throw new UnsupportedOperationException("Not Implemented");
//		
//	}
//
//	public DiaryListRsrc getDiarys(EndpointsRsrc parent, String[] diaryVersionNumber, String[] searchText,
//			String[] ownerEmployeeGuid, String[] employeeNumber, String[] approverEmployeeGuid,
//			Boolean approverPendingInd, Boolean approverApprovedInd, String[] diaryStatusCode,
//			String[] diaryDayStatusCode, String[] payPeriodNumber, LocalDate payPeriodStartDate,
//			LocalDate payPeriodEndDate, Boolean manuallyProcessedInd, Boolean amendmentInd, Boolean latestVersionInd,
//			String[] processingStatusUserId, String[] processingStatusUserGuid, LocalDateTime processingStatusTimestamp,
//			String[] processingStatusCode, LocalDateTime processingDateRecieved, LocalDateTime processingDateVerified,
//			String[] processingAssignedToUserType, String[] processingAssignedToUserGuid,
//			String[] processingAssignedToUserId, LocalDateTime processingAssignedToTimestamp, Integer pageNumber,
//			Integer pageRowCount, String[] orderBy, Integer expand)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public DiaryListRsrc getDiarys(EndpointsRsrc arg0, String[] arg1, String[] arg2, String[] arg3, String[] arg4,
//			String[] arg5, Boolean arg6, Boolean arg7, String[] arg8, String[] arg9, String[] arg10, LocalDate arg11,
//			LocalDate arg12, Boolean arg13, Boolean arg14, Boolean arg15, String[] arg16, String[] arg17,
//			LocalDateTime arg18, String[] arg19, LocalDateTime arg20, String[] arg21, String[] arg22, String[] arg23,
//			LocalDateTime arg24, Integer arg25, Integer arg26, String[] arg27, Integer arg28)
//			throws WildfireHumanResourcesPayrollServiceException, ValidationException {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
