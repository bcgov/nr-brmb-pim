package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.CommodityTypeVarietyXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.CommodityTypeVarietyXrefMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeVarietyXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CommodityTypeVarietyXrefDaoImpl extends BaseDao implements CommodityTypeVarietyXrefDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CommodityTypeVarietyXrefDaoImpl.class);

	@Autowired
	private CommodityTypeVarietyXrefMapper mapper;

	@Override
	public CommodityTypeVarietyXrefDto fetch(String commodityTypeCode, Integer cropVarietyId) throws DaoException {
		logger.debug("<fetch");

		CommodityTypeVarietyXrefDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityTypeCode", commodityTypeCode);
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
	public List<CommodityTypeVarietyXrefDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<CommodityTypeVarietyXrefDto> dtos = null;

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
	public void insert(CommodityTypeVarietyXrefDto dto, String userId) throws DaoException {
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

		logger.debug(">insert commodityTypeCode: " + dto.getCommodityTypeCode() + "; cropVarietyId: " + dto.getCropVarietyId());
	}

	@Override
	public void delete(String commodityTypeCode, Integer cropVarietyId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityTypeCode", commodityTypeCode);
			parameters.put("cropVarietyId", cropVarietyId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
