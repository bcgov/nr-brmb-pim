package ca.bc.gov.mal.cirras.underwriting.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.data.repositories.mapper.CommodityMaturityScaleMapper;
import ca.bc.gov.mal.cirras.underwriting.data.entities.CommodityMaturityScaleDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CommodityMaturityScaleDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CommodityMaturityScaleDao.class);

	@Autowired
	private CommodityMaturityScaleMapper mapper;

	
	public CommodityMaturityScaleDto fetch(String commodityMaturityScaleGuid) throws DaoException {
		logger.debug("<fetch");

		CommodityMaturityScaleDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityMaturityScaleGuid", commodityMaturityScaleGuid);
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

	
	
	public void insert(CommodityMaturityScaleDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String commodityMaturityScaleGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			commodityMaturityScaleGuid = (String) parameters.get("commodityMaturityScaleGuid");
			dto.setCommodityMaturityScaleGuid(commodityMaturityScaleGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + commodityMaturityScaleGuid);
	}
	

	
	public void update(CommodityMaturityScaleDto dto, String userId) 
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

	
	public void delete(String commodityMaturityScaleGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityMaturityScaleGuid", commodityMaturityScaleGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	public List<CommodityMaturityScaleDto> selectByYear(Integer cropYear) throws DaoException {

		logger.debug("<selectByYear");

		List<CommodityMaturityScaleDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
						
			dtos = this.mapper.selectByYear(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByYear " + dtos);
		return dtos;			
		
	}
		
}
