package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.PolicyDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.PolicyMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@Repository
public class PolicyDaoImpl extends BaseDao implements PolicyDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PolicyDaoImpl.class);

	@Autowired
	private PolicyMapper mapper;

	@Override
	public PolicyDto fetch(Integer policyId) throws DaoException {
		logger.debug("<fetch");

		PolicyDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("policyId", policyId);
			result = this.mapper.fetch(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetch " + result);
		return result;	
	}

	
	@Override
	public void insert(PolicyDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getPolicyId());
	}
	

	@Override
	public void update(PolicyDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.update(parameters);
	
			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	@Override
	public void delete(Integer policyId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("policyId", policyId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	

	@Override
	public List<PolicyDto> selectByFieldAndYear(Integer fieldId, Integer cropYear) throws DaoException {

		logger.debug("<selectByFieldAndYear");

		List<PolicyDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectByFieldAndYear(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByFieldAndYear " + dtos);
		return dtos;
	}

	@Override
	public List<PolicyDto> selectByOtherYearInventory(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException {

		logger.debug("<selectByOtherYearInventory");

		List<PolicyDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractId", contractId);
			parameters.put("currentCropYear", currentCropYear);
			parameters.put("numYears", numYears);
						
			dtos = this.mapper.selectByOtherYearInventory(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByOtherYearInventory " + dtos);
		return dtos;
	}

	@Override
	public List<PolicyDto> selectByOtherYearDop(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException {
		logger.debug("<selectByOtherYearDop");

		List<PolicyDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractId", contractId);
			parameters.put("currentCropYear", currentCropYear);
			parameters.put("numYears", numYears);
						
			dtos = this.mapper.selectByOtherYearDop(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByOtherYearDop " + dtos);
		return dtos;
	}	

	@Override
	public List<PolicyDto> selectByOtherYearVerified(Integer contractId, Integer currentCropYear, Integer numYears) throws DaoException {
		logger.debug("<selectByOtherYearVerified");

		List<PolicyDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractId", contractId);
			parameters.put("currentCropYear", currentCropYear);
			parameters.put("numYears", numYears);
						
			dtos = this.mapper.selectByOtherYearVerified(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByOtherYearVerified " + dtos);
		return dtos;
	}


	@Override
	public PagedDtos<PolicyDto> select(
    	Integer cropYear,
    	Integer insurancePlanId,
		Integer officeId,
		String policyStatusCode,
		String policyNumber,
		String growerInfo,
		String datasetType,
		String sortColumn,
		String sortDirection,
    	int maximumRows,
    	Integer pageNumber, 
    	Integer pageRowCount
	) throws DaoException, TooManyRecordsException {
		PagedDtos<PolicyDto> results = new PagedDtos<>();

		try {
			Integer offset = null;
			//Make sure the page number is 1 or higher
			if(pageNumber == null || pageNumber <= 0) {
				pageNumber = Integer.valueOf(1);
			}
			
			if(pageRowCount != null) { 
				offset = Integer.valueOf((pageNumber.intValue()-1)*pageRowCount.intValue()); 
			}
			
			Map<String, Object> parameters = new HashMap<String, Object>();

			Boolean datasetLinkedGfPolicies = false;
			if ( datasetType != null && datasetType.equals("LINKED_GF_POLICIES")) {
				if ( policyNumber == null ) {
					throw new DaoException("policyNumber is required for datasetType=LINKED_GF_POLICIES");
				}
				
				datasetLinkedGfPolicies = true;				
			}
			
			if(policyNumber != null && !datasetLinkedGfPolicies ) {
				policyNumber += "%";
			}

			String growerPhoneNumber = null;
			if(growerInfo != null) {
				
				growerPhoneNumber = cleanGrowerPhoneNumber(growerInfo);

				//Add wildcard
				growerInfo = growerInfo.toUpperCase() + "%";
			}
			
			//Ignore crop year if the policy number contains the year (i.e. 111111-21)
			if(policyNumber != null && policyNumber.indexOf("-") > -1 && policyNumber.length() > 6) {
				cropYear = null;
			}
			
			
			//Get database column name and default values if none or invalid value is set
			//sets this.databaseColumn and this.isStringColumn
			String orderBy = getOrderBy(sortColumn, sortDirection);

			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("officeId", officeId);
			parameters.put("policyStatusCode", policyStatusCode);
			parameters.put("policyNumber", policyNumber);
			parameters.put("growerInfo", growerInfo);
			parameters.put("growerPhoneNumber", growerPhoneNumber);
			parameters.put("datasetLinkedGfPolicies", datasetLinkedGfPolicies);
			parameters.put("orderBy", orderBy);
			parameters.put("offset", offset);
			parameters.put("pageRowCount", pageRowCount);
			
			int totalRowCount = this.mapper.selectCount(parameters);
			
			boolean pageRowCountExceeds = (pageRowCount==null||pageRowCount.intValue()>maximumRows);
			if(pageRowCountExceeds&&totalRowCount>maximumRows) {
				throw new TooManyRecordsException("Exceeded maximum ("+maximumRows+") results per page.");
			}
			
			List<PolicyDto> dtos = this.mapper.select(parameters);

			results.setResults(dtos);
			results.setPageRowCount(dtos.size());
			results.setTotalRowCount(totalRowCount);
			results.setPageNumber(pageNumber == null ? 0 : pageNumber.intValue());

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + results);
		return results;
	}

	@Override
	public String cleanGrowerPhoneNumber(String growerInfo) {
		String growerPhoneNumber;
		//Clean search for phone numer and remove all non numeric characters
		growerPhoneNumber = growerInfo.replaceAll("[^\\d]", "");

		//Don't set the phone number if it's a non numeric search
		if(growerPhoneNumber == null || growerPhoneNumber.isEmpty()) {
			growerPhoneNumber = null;
		} else {
			//Add wildcard
			growerPhoneNumber = growerPhoneNumber + "%";
		}
		return growerPhoneNumber;
	}

	private String getOrderBy(String modelColumnName, String sortDirection) {
		
		//Making sure the direction is a valid value
		sortDirection = getSortDirection(sortDirection);

		String databaseColumn = "policy_number";

		if (modelColumnName != null) {
			switch (modelColumnName) {
			case "policyNumber":
				databaseColumn = "policy_number";
				break;
			case "policyStatus":
				databaseColumn = "psc.description"; //Policy Status
				break;
			case "insurancePlanName":
				databaseColumn = "insurance_plan_name";
				break;
			case "growerNumber":
				databaseColumn = "grower_number";
				break;
			case "growerName":
				databaseColumn = "grower_name";
				break;
			default:
				databaseColumn = "policy_number";
				break;
			}
		}
		
		String orderBy = java.text.MessageFormat.format("ORDER BY {0} {1}", databaseColumn, sortDirection);
		
		return orderBy;
	}
	
	private String getSortDirection(String sortDirection) {
		
		if (sortDirection != null) {
			String inputSortDirection = sortDirection.toUpperCase();
			switch (inputSortDirection) {
			case "ASC":
				return inputSortDirection;
			case "DESC":
				return inputSortDirection;
			default:
				return "ASC";
			}
		}
		return "ASC";
			
	}
	
}
