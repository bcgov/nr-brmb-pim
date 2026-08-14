package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.VerifiedYieldGrainBasketMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldGrainBasketDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class VerifiedYieldGrainBasketDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldGrainBasketDao.class);

	@Autowired
	private VerifiedYieldGrainBasketMapper mapper;

	
	public VerifiedYieldGrainBasketDto fetch(String verifiedYieldGrainBasketGuid) throws DaoException {
		logger.debug("<fetch");

		VerifiedYieldGrainBasketDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldGrainBasketGuid", verifiedYieldGrainBasketGuid);
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

	
	
	public void insert(VerifiedYieldGrainBasketDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String verifiedYieldGrainBasketGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			verifiedYieldGrainBasketGuid = (String) parameters.get("verifiedYieldGrainBasketGuid");
			dto.setVerifiedYieldGrainBasketGuid(verifiedYieldGrainBasketGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + verifiedYieldGrainBasketGuid);
	}
	

	
	public void update(VerifiedYieldGrainBasketDto dto, String userId) 
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

	
	public void delete(String verifiedYieldGrainBasketGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldGrainBasketGuid", verifiedYieldGrainBasketGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	

	
	public void deleteForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException, NotFoundDaoException {

		logger.debug("<deleteForVerifiedYieldContract");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
			this.mapper.deleteForVerifiedYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForVerifiedYieldContract");
		
	}	
	
	
	public List<VerifiedYieldGrainBasketDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException {

		logger.debug("<selectForVerifiedYieldContract");

		List<VerifiedYieldGrainBasketDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("verifiedYieldContractGuid", verifiedYieldContractGuid);
						
			dtos = this.mapper.selectForVerifiedYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForVerifiedYieldContract " + dtos);
		return dtos;
	}
}
