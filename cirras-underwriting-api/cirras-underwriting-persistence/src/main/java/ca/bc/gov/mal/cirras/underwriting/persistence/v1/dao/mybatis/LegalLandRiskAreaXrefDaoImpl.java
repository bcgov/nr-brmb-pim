package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.LegalLandRiskAreaXrefDao;
import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.LegalLandRiskAreaXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandRiskAreaXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class LegalLandRiskAreaXrefDaoImpl extends BaseDao implements LegalLandRiskAreaXrefDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(LegalLandRiskAreaXrefDaoImpl.class);

	@Autowired
	private LegalLandRiskAreaXrefMapper mapper;

	@Override
	public LegalLandRiskAreaXrefDto fetch(Integer legalLandId, Integer riskAreaId) throws DaoException {
		logger.debug("<fetch");

		LegalLandRiskAreaXrefDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			parameters.put("riskAreaId", riskAreaId);
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
	public List<LegalLandRiskAreaXrefDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<LegalLandRiskAreaXrefDto> dtos = null;

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
	public void insert(LegalLandRiskAreaXrefDto dto, String userId) throws DaoException {
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

		logger.debug(">insert legal_land_id: " + dto.getLegalLandId() + " riskArea_id: " + dto.getRiskAreaId());
	}
	
	@Override
	public void update(LegalLandRiskAreaXrefDto dto, String userId) 
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
	public void delete(Integer legalLandId, Integer riskAreaId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			parameters.put("riskAreaId", riskAreaId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	@Override
	public void deleteForLegalLand(Integer legalLandId) throws DaoException {
		logger.debug("<deleteForLegalLand");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("legalLandId", legalLandId);
			this.mapper.deleteForLegalLand(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForLegalLand");
	}
	
	

}
