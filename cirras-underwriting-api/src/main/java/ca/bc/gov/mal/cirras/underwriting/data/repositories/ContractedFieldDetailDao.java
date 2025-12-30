package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.ContractedFieldDetailMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@Repository
public class ContractedFieldDetailDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ContractedFieldDetailDao.class);

	@Autowired
	private ContractedFieldDetailMapper mapper;

	
	public ContractedFieldDetailDto fetch(Integer contractedFieldDetailId) throws DaoException {
		logger.debug("<fetch");

		ContractedFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractedFieldDetailId", contractedFieldDetailId);
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


	
	public ContractedFieldDetailDto fetchSimple(Integer contractedFieldDetailId) throws DaoException {
		logger.debug("<fetchSimple");

		ContractedFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractedFieldDetailId", contractedFieldDetailId);
			result = this.mapper.fetchSimple(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchSimple " + result);
		return result;	
	}

	
	
	public ContractedFieldDetailDto selectByGcyAndField(Integer growerContractYearId, Integer fieldId) throws DaoException {
		logger.debug("<selectByGcyAndField");

		ContractedFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("growerContractYearId", growerContractYearId);
			parameters.put("fieldId", fieldId);
			result = this.mapper.selectByGcyAndField(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByGcyAndField " + result);
		return result;	
	}
	
	
	public ContractedFieldDetailDto selectForFieldRollover(Integer fieldId, Integer cropYear, Integer insurancePlanId) throws DaoException {
		logger.debug("<selectForFieldRollover");

		ContractedFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			result = this.mapper.selectForFieldRollover(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForFieldRollover " + result);
		return result;	
	}	

	
	public void insert(ContractedFieldDetailDto dto, String userId) throws DaoException {
		logger.debug("<insert");
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			Integer contractedFieldDetailId = (Integer) parameters.get("contractedFieldDetailId");
			dto.setContractedFieldDetailId(contractedFieldDetailId);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getContractedFieldDetailId());
	}
	
	
	public void insertDataSync(ContractedFieldDetailDto dto, String userId) throws DaoException {
		logger.debug("<insertDataSync");
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insertDataSync(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insertDataSync " + dto.getContractedFieldDetailId());
	}

	
	public void update(ContractedFieldDetailDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				int count = this.mapper.update(parameters);
	
				if(count==0) {
					throw new DaoException("Record not updated: "+count);
				}
	
			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	
	public void updateSync(ContractedFieldDetailDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<updateSync");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.updateSync(parameters);

			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">updateSync");
	}
	
	
	public void updateDisplayOrder(ContractedFieldDetailDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<updateDisplayOrder");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.updateDisplayOrder(parameters);

			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">updateDisplayOrder");
	}

	
	public void delete(Integer contractedFieldDetailId) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("contractedFieldDetailId", contractedFieldDetailId);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	public void deleteForField(Integer fieldId) throws DaoException, NotFoundDaoException {
		logger.debug("<deleteForField");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			this.mapper.deleteForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForField");
	}
	
	
	public List<ContractedFieldDetailDto> select(Integer contractId, Integer cropYear) throws DaoException {
		List<ContractedFieldDetailDto> dtos = null;

		try {

			String orderBy = getOrderBy("displayOrder", "ASC");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
			parameters.put("orderBy", orderBy);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}

	
	public PagedDtos<ContractedFieldDetailDto> select(
		Integer contractId, 
		Integer cropYear,
		String sortColumn,
		String sortDirection,
    	int maximumRows,
    	Integer pageNumber, 
    	Integer pageRowCount
	) throws DaoException, TooManyRecordsException {
		PagedDtos<ContractedFieldDetailDto> results = new PagedDtos<>();

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
			
			//Get database column name and default values if none or invalid value is set
			String orderBy = getOrderBy(sortColumn, sortDirection);

			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
			parameters.put("orderBy", orderBy);
			parameters.put("offset", offset);
			parameters.put("pageRowCount", pageRowCount);
			
			int totalRowCount = this.mapper.selectCount(parameters);
			
			boolean pageRowCountExceeds = (pageRowCount==null||pageRowCount.intValue()>maximumRows);
			if(pageRowCountExceeds&&totalRowCount>maximumRows) {
				throw new TooManyRecordsException("Exceeded maximum ("+maximumRows+") results per page.");
			}
			
			List<ContractedFieldDetailDto> dtos = this.mapper.select(parameters);

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

	
	public List<ContractedFieldDetailDto> selectForDeclaredYield(Integer contractId, Integer cropYear) throws DaoException {
		logger.debug("<selectForDeclaredYield");

		List<ContractedFieldDetailDto> dtos = null;

		try {

			String orderBy = getOrderBy("displayOrder", "ASC");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
			parameters.put("orderBy", orderBy);
						
			dtos = this.mapper.selectForDeclaredYield(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDeclaredYield " + dtos);
		return dtos;
	}

	
	public List<ContractedFieldDetailDto> selectForVerifiedYield(Integer contractId, Integer cropYear) throws DaoException {
		logger.debug("<selectForVerifiedYield");

		List<ContractedFieldDetailDto> dtos = null;

		try {

			String orderBy = getOrderBy("displayOrder", "ASC");
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("contractId", contractId);
			parameters.put("cropYear", cropYear);
			parameters.put("orderBy", orderBy);
						
			dtos = this.mapper.selectForVerifiedYield(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForVerifiedYield " + dtos);
		return dtos;
	}
	
	
	
	public List<ContractedFieldDetailDto> selectForDisplayOrderUpdate(Integer growerContractYearId) throws DaoException {
		List<ContractedFieldDetailDto> dtos = null;

		logger.debug("<selectForDisplayOrderUpdate ");

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("growerContractYearId", growerContractYearId);
						
			dtos = this.mapper.selectForDisplayOrderUpdate(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDisplayOrderUpdate " + dtos);
		return dtos;
	}

	
	public List<ContractedFieldDetailDto> selectForYearAndField(Integer cropYear, Integer fieldId) throws DaoException {
		List<ContractedFieldDetailDto> dtos = null;

		logger.debug("<selectForYearAndField");

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("cropYear", cropYear);
			parameters.put("fieldId", fieldId);
						
			dtos = this.mapper.selectForYearAndField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForYearAndField " + dtos);
		return dtos;
	}

	
	public List<ContractedFieldDetailDto> selectForField(Integer fieldId) throws DaoException {
		List<ContractedFieldDetailDto> dtos = null;

		logger.debug("<selectForField");

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("fieldId", fieldId);
						
			dtos = this.mapper.selectForField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForField " + dtos);
		return dtos;
	}	
	
	
	public ContractedFieldDetailDto selectForFieldYearAndContract(Integer fieldId, Integer cropYear, Integer contractId) throws DaoException {
		logger.debug("<selectForFieldYearAndContract");

		ContractedFieldDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
			parameters.put("contractId", contractId);
			result = this.mapper.selectForFieldYearAndContract(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForFieldYearAndContract " + result);
		return result;	
	}
	
	private String getOrderBy(String modelColumnName, String sortDirection) {
		
		//Making sure the direction is a valid value
		sortDirection = getSortDirection(sortDirection);

		String databaseColumn = "cfd.display_order";

		if (modelColumnName != null) {
			// Currently only supports sorting by display_order.
			switch (modelColumnName) {
			case "displayOrder":
				databaseColumn = "cfd.display_order";
				break;
			default:
				databaseColumn = "cfd.display_order";
				break;
			}
		}
		
		String orderBy = java.text.MessageFormat.format("ORDER BY {0} {1}, cfd.contracted_field_detail_id", databaseColumn, sortDirection);
		
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
