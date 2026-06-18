package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.DeclaredYieldContractCommodityBerriesOutboxMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesOutboxDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldContractCommodityBerriesOutboxDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityBerriesOutboxDao.class);

	@Autowired
	private DeclaredYieldContractCommodityBerriesOutboxMapper mapper;

	
	public DeclaredYieldContractCommodityBerriesOutboxDto fetch(Integer declaredYieldContractCommodityBerriesOutboxId) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldContractCommodityBerriesOutboxDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCommodityBerriesOutboxId", declaredYieldContractCommodityBerriesOutboxId);
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

	
	
	public void insert(DeclaredYieldContractCommodityBerriesOutboxDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		Integer declaredYieldContractCommodityBerriesOutboxId = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldContractCommodityBerriesOutboxId = (Integer) parameters.get("declaredYieldContractCommodityBerriesOutboxId");
			dto.setDeclaredYieldContractCommodityBerriesOutboxId(declaredYieldContractCommodityBerriesOutboxId);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldContractCommodityBerriesOutboxId);
	}
	

	
	public void update(DeclaredYieldContractCommodityBerriesOutboxDto dto, String userId) 
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

	
	public void delete(Integer declaredYieldContractCommodityBerriesOutboxId) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCommodityBerriesOutboxId", declaredYieldContractCommodityBerriesOutboxId);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	public List<DeclaredYieldContractCommodityBerriesOutboxDto> select(Integer maxRecords) throws DaoException {

		logger.debug("<select");

		List<DeclaredYieldContractCommodityBerriesOutboxDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("maxRecords", maxRecords);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
			
}
