package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.FieldMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class FieldDaoImpl extends BaseDao implements FieldDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(FieldDaoImpl.class);

	@Autowired
	private FieldMapper mapper;

	@Override
	public FieldDto fetch(Integer fieldId) throws DaoException {
		logger.debug("<fetch");

		FieldDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
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
	
	@Override
	public List<FieldDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<FieldDto> dtos = null;

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
	public void insert(FieldDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			Integer fieldId = (Integer) parameters.get("fieldId");
			dto.setFieldId(fieldId);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getFieldId());
	}
	
	@Override
	public void insertDataSync(FieldDto dto, String userId) throws DaoException {
		logger.debug("<insert");

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

		logger.debug(">insert " + dto.getFieldId());
	}	

	@Override
	public void update(FieldDto dto, String userId) 
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
	public void delete(Integer fieldId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("fieldId", fieldId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	@Override
	public List<FieldDto> selectForLegalLandOrField(Integer legalLandId, Integer fieldId, Integer cropYear) throws DaoException {
		List<FieldDto> dtos = null;

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("legalLandId", legalLandId);
			parameters.put("fieldId", fieldId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectForLegalLandOrField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}

	@Override
	public List<FieldDto> selectByLastPolicyForLegalLand(
			Integer legalLandId, 
			Integer cropYear, 
			Integer includeContractId, 
			Integer excludeContractId, 
			Integer excludeFieldId
		) throws DaoException {

		List<FieldDto> dtos = null;

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("legalLandId", legalLandId);
			parameters.put("cropYear", cropYear);
			parameters.put("includeContractId", includeContractId);
			parameters.put("excludeContractId", excludeContractId);
			parameters.put("excludeFieldId", excludeFieldId);
						
			dtos = this.mapper.selectByLastPolicyForLegalLand(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
	
	@Override
	public List<FieldDto> selectOtherFieldsForLegalLand(
    		Integer legalLandId,
    		Integer excludeFieldId,
			Integer cropYear 
		) throws DaoException {

		List<FieldDto> dtos = null;

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("legalLandId", legalLandId);
			parameters.put("excludeFieldId", excludeFieldId);
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectOtherFieldsForLegalLand(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}   

	
	@Override
	public List<FieldDto> selectForLegalLand(
    		Integer legalLandId
		) throws DaoException {

		logger.debug("<selectForLegalLand");

		List<FieldDto> dtos = null;

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("legalLandId", legalLandId);
						
			dtos = this.mapper.selectForLegalLand(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForLegalLand " + dtos);
		return dtos;
	} 
}
