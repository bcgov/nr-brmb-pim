package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.CropVarietyInsurabilityDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.CropVarietyInsurabilityMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CropVarietyInsurabilityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CropVarietyInsurabilityDaoImpl extends BaseDao implements CropVarietyInsurabilityDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyInsurabilityDaoImpl.class);

	@Autowired
	private CropVarietyInsurabilityMapper mapper;

	@Override
	public CropVarietyInsurabilityDto fetch(Integer cropVarietyId) throws DaoException {
		logger.debug("<fetch");

		CropVarietyInsurabilityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyId", cropVarietyId);
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
	public List<CropVarietyInsurabilityDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<CropVarietyInsurabilityDto> dtos = null;

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
	public List<CropVarietyInsurabilityDto> selectForInsurancePlan(Integer insurancePlanId) throws DaoException {

		logger.debug("<selectByInsurancePlan");
		
		List<CropVarietyInsurabilityDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.selectForInsurancePlan(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByInsurancePlan " + dtos);
		return dtos;
	}
	
	@Override
	public List<CropVarietyInsurabilityDto> selectValidation(Integer insurancePlanId) throws DaoException {

		logger.debug("<selectValidation");
		
		List<CropVarietyInsurabilityDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.selectValidation(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectValidation " + dtos);
		return dtos;
	}
	
	@Override
	public void insert(CropVarietyInsurabilityDto dto, String userId) throws DaoException {
		logger.debug("<insert");
		
		String cropVarietyInsurabilityGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			cropVarietyInsurabilityGuid = (String) parameters.get("cropVarietyInsurabilityGuid");
			dto.setCropVarietyInsurabilityGuid(cropVarietyInsurabilityGuid);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getCropVarietyId());
	}
	

	@Override
	public void update(CropVarietyInsurabilityDto dto, String userId) 
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
	public void delete(Integer cropVarietyId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyId", cropVarietyId);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
