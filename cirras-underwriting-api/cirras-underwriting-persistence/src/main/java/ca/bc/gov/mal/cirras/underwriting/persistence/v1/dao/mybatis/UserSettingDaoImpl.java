package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UserSettingDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.UserSettingMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UserSettingDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class UserSettingDaoImpl extends BaseDao implements UserSettingDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UserSettingDaoImpl.class);

	@Autowired
	private UserSettingMapper mapper;

	@Override
	public UserSettingDto fetch(String userSettingGuid) throws DaoException {
		logger.debug("<fetch");

		UserSettingDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userSettingGuid", userSettingGuid);
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
	public void insert(UserSettingDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String userSettingGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			userSettingGuid = (String) parameters.get("userSettingGuid");
			dto.setUserSettingGuid(userSettingGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + userSettingGuid);
	}
	

	@Override
	public void update(UserSettingDto dto, String userId) 
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
	public void delete(String userSettingGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userSettingGuid", userSettingGuid);
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
	public UserSettingDto getByLoginUserGuid(String loginUserGuid) throws DaoException {

		logger.debug("<getByLoginUserGuid");

		UserSettingDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("loginUserGuid", loginUserGuid);
			result = this.mapper.getByLoginUserGuid(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByLoginUserGuid " + result);
		return result;	
	}	
}
