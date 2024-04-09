package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CropVarietyInsPlantInsXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.CropVarietyInsPlantInsXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CropVarietyInsPlantInsXrefDaoImpl extends BaseDao implements CropVarietyInsPlantInsXrefDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyInsPlantInsXrefDaoImpl.class);

	@Autowired
	private CropVarietyInsPlantInsXrefMapper mapper;

	@Override
	public CropVarietyInsPlantInsXrefDto fetch(String cropVarietyInsurabilityGuid, String plantInsurabilityTypeCode) throws DaoException {
		logger.debug("<fetch");

		CropVarietyInsPlantInsXrefDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyInsurabilityGuid", cropVarietyInsurabilityGuid);
			parameters.put("plantInsurabilityTypeCode", plantInsurabilityTypeCode);
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
	public List<CropVarietyInsPlantInsXrefDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<CropVarietyInsPlantInsXrefDto> dtos = null;

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
	public void insert(CropVarietyInsPlantInsXrefDto dto, String userId) throws DaoException {
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

		logger.debug(">insert " + dto.getCropVarietyId());
	}


	@Override
	public void delete(String cropVarietyInsurabilityGuid, String plantInsurabilityTypeCode) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyInsurabilityGuid", cropVarietyInsurabilityGuid);
			parameters.put("plantInsurabilityTypeCode", plantInsurabilityTypeCode);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	@Override
	public void deleteForVariety(String cropVarietyInsurabilityGuid) throws DaoException {
		logger.debug("<deleteForVariety");

		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyInsurabilityGuid", cropVarietyInsurabilityGuid);
			this.mapper.deleteForVariety(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForVariety");
	}

	@Override
	public List<CropVarietyInsPlantInsXrefDto> selectPlantInsForCropVarieties(Integer cropVarietyId) throws DaoException {
		logger.debug("<selectPlantInsForCropVarieties");

		List<CropVarietyInsPlantInsXrefDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyId", cropVarietyId);
			dtos = this.mapper.selectPlantInsForCropVarieties(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectPlantInsForCropVarieties " + dtos);
		return dtos;	
	}

	@Override
	public List<CropVarietyInsPlantInsXrefDto> selectForInsurancePlan(Integer insurancePlanId) throws DaoException {
		logger.debug("<selectForInsurancePlan");

		List<CropVarietyInsPlantInsXrefDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("insurancePlanId", insurancePlanId);
			dtos = this.mapper.selectForInsurancePlan(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForInsurancePlan " + dtos);
		return dtos;	
	}
	
}
