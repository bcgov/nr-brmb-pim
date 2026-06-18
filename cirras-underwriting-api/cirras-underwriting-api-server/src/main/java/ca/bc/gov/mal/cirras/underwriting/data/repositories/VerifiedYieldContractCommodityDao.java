package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.VerifiedYieldContractCommodityMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.VerifiedYieldContractCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class VerifiedYieldContractCommodityDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VerifiedYieldContractCommodityDao.class);

	@Autowired
	private VerifiedYieldContractCommodityMapper mapper;

	
	public VerifiedYieldContractCommodityDto fetch(String verifiedYieldContractCommodityGuid) throws DaoException {
		logger.debug("<fetch");

		VerifiedYieldContractCommodityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractCommodityGuid", verifiedYieldContractCommodityGuid);
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

	
	
	public void insert(VerifiedYieldContractCommodityDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String verifiedYieldContractCommodityGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			verifiedYieldContractCommodityGuid = (String) parameters.get("verifiedYieldContractCommodityGuid");
			dto.setVerifiedYieldContractCommodityGuid(verifiedYieldContractCommodityGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + verifiedYieldContractCommodityGuid);
	}
	

	
	public void update(VerifiedYieldContractCommodityDto dto, String userId) 
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

	
	public void delete(String verifiedYieldContractCommodityGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("verifiedYieldContractCommodityGuid", verifiedYieldContractCommodityGuid);
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
			int count = this.mapper.deleteForVerifiedYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForVerifiedYieldContract");
		
	}	
	
	
	public List<VerifiedYieldContractCommodityDto> selectForVerifiedYieldContract(String verifiedYieldContractGuid) throws DaoException {

		logger.debug("<selectForVerifiedYieldContract");

		List<VerifiedYieldContractCommodityDto> dtos = null;

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
