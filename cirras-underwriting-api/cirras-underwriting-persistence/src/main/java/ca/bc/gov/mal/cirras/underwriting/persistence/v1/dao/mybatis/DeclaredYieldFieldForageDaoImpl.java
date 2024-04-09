package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.DeclaredYieldFieldForageDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.DeclaredYieldFieldForageMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldForageDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class DeclaredYieldFieldForageDaoImpl extends BaseDao implements DeclaredYieldFieldForageDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldFieldForageDaoImpl.class);

	@Autowired
	private DeclaredYieldFieldForageMapper mapper;

	@Override
	public DeclaredYieldFieldForageDto fetch(String declaredYieldFieldForageGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldFieldForageDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldForageGuid", declaredYieldFieldForageGuid);
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
	public void insert(DeclaredYieldFieldForageDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldFieldForageGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			declaredYieldFieldForageGuid = (String) parameters.get("declaredYieldFieldForageGuid");
			dto.setDeclaredYieldFieldForageGuid(declaredYieldFieldForageGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldFieldForageGuid);
	}
	

	@Override
	public void update(DeclaredYieldFieldForageDto dto, String userId) 
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
	public void delete(String declaredYieldFieldForageGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldFieldForageGuid", declaredYieldFieldForageGuid);
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
	public List<DeclaredYieldFieldForageDto> getByInventoryField(String inventoryFieldGuid) throws DaoException {

		logger.debug("<getByInventoryField");

		List<DeclaredYieldFieldForageDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("inventoryFieldGuid", inventoryFieldGuid);
						
			dtos = this.mapper.getByInventoryField(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByInventoryField " + dtos);
		return dtos;
	}
}
