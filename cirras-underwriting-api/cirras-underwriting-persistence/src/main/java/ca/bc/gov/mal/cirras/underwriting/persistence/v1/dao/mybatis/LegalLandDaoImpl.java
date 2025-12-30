package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.LegalLandMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@Repository
public class LegalLandDaoImpl extends BaseDao implements LegalLandDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandDaoImpl.class);

	@Autowired
	private LegalLandMapper mapper;

	@Override
	public LegalLandDto fetch(Integer legalLandId) throws DaoException {
		logger.debug("<fetch");

		LegalLandDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
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
	public List<LegalLandDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<LegalLandDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}


	
	@Override
	public void insert(LegalLandDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			Integer legalLandId = (Integer) parameters.get("legalLandId");
			dto.setLegalLandId(legalLandId);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getLegalLandId());
	}
	
	@Override
	public void insertDataSync(LegalLandDto dto, String userId) throws DaoException {
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

		logger.debug(">insertDataSync " + dto.getLegalLandId());
	}
	
	@Override
	public void update(LegalLandDto dto, String userId) 
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
	public void delete(Integer legalLandId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	@Override
	public PagedDtos<LegalLandDto> select(
			String otherDescription, 
			String primaryPropertyIdentifier,
			String growerInfo,
			String datasetType,
			Boolean wildCardSearch, 
			Boolean searchByOtherDescOrLegalDesc,    // If true, then the otherDescription criteria can match other_description or legal_description.
			String sortColumn,
			String sortDirection,
			int maximumRows,
			Integer pageNumber, 
			Integer pageRowCount
	) throws DaoException, TooManyRecordsException {

		logger.debug("<select");
		
		PagedDtos<LegalLandDto> results = new PagedDtos<>();

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

			if ( otherDescription != null ) {
				otherDescription = otherDescription.toUpperCase();
				
				if ( Boolean.TRUE.equals(wildCardSearch) ) {
					//Add wildcard
					otherDescription = otherDescription + "%";
				}
			}

			if ( primaryPropertyIdentifier != null ) {
				primaryPropertyIdentifier = primaryPropertyIdentifier.toUpperCase();
				
				if ( Boolean.TRUE.equals(wildCardSearch) ) {
					//Add wildcard
					primaryPropertyIdentifier = primaryPropertyIdentifier + "%";
				}
			}
			
			
			String growerPhoneNumber = null;
			if(growerInfo != null) {
				
				//Clean search for phone numer and remove all non numeric characters
				growerPhoneNumber = growerInfo.replaceAll("[^\\d]", "");

				//Don't set the phone number if it's a non numeric search
				if(growerPhoneNumber == null || growerPhoneNumber.isEmpty()) {
					growerPhoneNumber = null;
				} else if ( Boolean.TRUE.equals(wildCardSearch) ) {
					//Add wildcard
					growerPhoneNumber = growerPhoneNumber + "%";
				}

				growerInfo = growerInfo.toUpperCase();
				
				if ( Boolean.TRUE.equals(wildCardSearch) ) {
					//Add wildcard
					growerInfo = growerInfo + "%";
				}
			}

			Boolean datasetLandCleanup = false;
			if ( datasetType != null && datasetType.equals("CLEANUP")) {
				datasetLandCleanup = true;				
			}
			
			String orderBy = getOrderBy(sortColumn, sortDirection);

			parameters.put("otherDescription", otherDescription);
			parameters.put("primaryPropertyIdentifier", primaryPropertyIdentifier);
			parameters.put("growerInfo", growerInfo);
			parameters.put("growerPhoneNumber", growerPhoneNumber);
			parameters.put("datasetLandCleanup", datasetLandCleanup);
			parameters.put("wildCardSearch", wildCardSearch);
			parameters.put("searchByOtherDescOrLegalDesc", searchByOtherDescOrLegalDesc);
			parameters.put("orderBy", orderBy);
			parameters.put("offset", offset);
			parameters.put("pageRowCount", pageRowCount);
			
			int totalRowCount = this.mapper.selectCount(parameters);
			
			boolean pageRowCountExceeds = (pageRowCount==null||pageRowCount.intValue()>maximumRows);
			if(pageRowCountExceeds&&totalRowCount>maximumRows) {
				throw new TooManyRecordsException("Exceeded maximum ("+maximumRows+") results per page.");
			}
			
			List<LegalLandDto> dtos = this.mapper.select(parameters);

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
	
	
	private String getOrderBy(String modelColumnName, String sortDirection) {
		
		//Making sure the direction is a valid value
		sortDirection = getSortDirection(sortDirection);

		String databaseColumn = "other_description";

		if (modelColumnName != null) {
			switch (modelColumnName) {
			case "otherDescription":
				databaseColumn = "other_description";
				break;
			case "primaryPropertyIdentifier":
				databaseColumn = "primary_property_identifier";
				break;
			case "legalDescription":
				databaseColumn = "legal_description";
				break;
			case "totalAcres":
				databaseColumn = "total_acres";
				break;
			case "activeToCropYear":
				databaseColumn = "active_to_crop_year";
				break;
			default:
				databaseColumn = "other_description";
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
	
	@Override
	public List<LegalLandDto> searchOtherLegalLandForField(
	    	Integer fieldId,
	 		Integer legalLandId,
	 		Integer cropYear)
			throws DaoException {

		logger.debug("<searchOtherLegalLandForField");
		
		List<LegalLandDto> dtos = null;

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			parameters.put("legalLandId", legalLandId);
			parameters.put("cropYear", cropYear);
			dtos = this.mapper.searchOtherLegalLandForField(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">searchOtherLegalLandForField " + dtos);
		return dtos;
	}
	
	@Override
	public int getNextPidSequence() throws DaoException {
		logger.debug("<getNextPidSequence");
		
		Integer nextPid = null;

		try {

			nextPid = this.mapper.getNextPidSequence();

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getNextPidSequence " + nextPid);
		return nextPid;
	}

}
