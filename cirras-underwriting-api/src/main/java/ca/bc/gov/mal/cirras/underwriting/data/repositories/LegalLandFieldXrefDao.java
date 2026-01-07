package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.LegalLandFieldXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class LegalLandFieldXrefDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandFieldXrefDao.class);

	@Autowired
	private LegalLandFieldXrefMapper mapper;

	
	public LegalLandFieldXrefDto fetch(Integer legalLandId, Integer fieldId) throws DaoException {
		logger.debug("<fetch");

		LegalLandFieldXrefDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			parameters.put("fieldId", fieldId);
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
	
	
	public List<LegalLandFieldXrefDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<LegalLandFieldXrefDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetchAll " + dtos);
		return dtos;
	}


	
	
	public void insert(LegalLandFieldXrefDto dto, String userId) throws DaoException {
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

		logger.debug(">insert legal_land_id: " + dto.getLegalLandId() + " field_id: " + dto.getFieldId());
	}
	

	
	public void delete(Integer legalLandId, Integer fieldId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			parameters.put("fieldId", fieldId);
			this.mapper.delete(parameters);

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
}
