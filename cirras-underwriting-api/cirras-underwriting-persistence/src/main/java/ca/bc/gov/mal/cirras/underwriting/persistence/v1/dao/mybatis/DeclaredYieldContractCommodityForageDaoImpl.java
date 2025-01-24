package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldContractCommodityForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldContractCommodityForageMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldContractCommodityForageDaoImpl extends BaseDao implements DeclaredYieldContractCommodityForageDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityForageDaoImpl.class);

	@Autowired
	private DeclaredYieldContractCommodityForageMapper mapper;

	@Override
	public DeclaredYieldContractCommodityForageDto fetch(String declaredYieldContractCmdtyForageGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldContractCommodityForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCmdtyForageGuid", declaredYieldContractCmdtyForageGuid);
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
	public void insert(DeclaredYieldContractCommodityForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldContractCmdtyForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldContractCmdtyForageGuid = (String) parameters.get("declaredYieldContractCmdtyForageGuid");
			dto.setDeclaredYieldContractCmdtyForageGuid(declaredYieldContractCmdtyForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldContractCmdtyForageGuid);
	}
	

	@Override
	public void update(DeclaredYieldContractCommodityForageDto dto, String userId) 
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

	@Override
	public void delete(String declaredYieldContractCmdtyForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCmdtyForageGuid", declaredYieldContractCmdtyForageGuid);
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
	public void deleteForDeclaredYieldContract(String declaredYieldContractGuid) throws DaoException, NotFoundDaoException {

		logger.debug("<deleteForDeclaredYieldContract");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
			this.mapper.deleteForDeclaredYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForDeclaredYieldContract");
		
	}	
	
	
	@Override
	public List<DeclaredYieldContractCommodityForageDto> selectForDeclaredYieldContract(String declaredYieldContractGuid, sortOrder order) throws DaoException {

		logger.debug("<selectForDeclaredYieldContract");

		List<DeclaredYieldContractCommodityForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			String orderBy = "ctc.description"; //default
			if(order.equals(sortOrder.CommodityType)) {
				orderBy = "ctc.description";
			} else if(order.equals(sortOrder.CommodityNameCommodityType)) {
				orderBy = "c.commodity_name, ctc.description";
			}
			
			parameters.put("declaredYieldContractGuid", declaredYieldContractGuid);
			parameters.put("orderBy", orderBy);
						
			dtos = this.mapper.selectForDeclaredYieldContract(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectForDeclaredYieldContract " + dtos);
		return dtos;
	}
	
	@Override
	public  List<DeclaredYieldContractCommodityForageDto> selectToRecalculate(
	    		Integer cropCommodityId,
	    		String enteredYieldMeasUnitTypeCode,
	    		Integer effectiveCropYear,
	    		Integer expiryCropYear
				) throws DaoException {

		logger.debug("<selectToRecalculate");

		List<DeclaredYieldContractCommodityForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("cropCommodityId", cropCommodityId);
			parameters.put("enteredYieldMeasUnitTypeCode", enteredYieldMeasUnitTypeCode);
			parameters.put("effectiveCropYear", effectiveCropYear);
			parameters.put("expiryCropYear", expiryCropYear);
						
			dtos = this.mapper.selectToRecalculate(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectToRecalculate " + dtos);
		return dtos;
	}			
}
