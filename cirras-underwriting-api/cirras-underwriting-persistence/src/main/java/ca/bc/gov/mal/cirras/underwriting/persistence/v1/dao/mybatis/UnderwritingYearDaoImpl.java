package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.UnderwritingYearDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.UnderwritingYearMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingYearDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class UnderwritingYearDaoImpl extends BaseDao implements UnderwritingYearDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(UnderwritingYearDaoImpl.class);

	@Autowired
	private UnderwritingYearMapper mapper;

	@Override
	public UnderwritingYearDto fetch(String underwritingYearGuid) throws DaoException {
		logger.debug("<fetch");

		UnderwritingYearDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("underwritingYearGuid", underwritingYearGuid);
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
	public List<UnderwritingYearDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<UnderwritingYearDto> dtos = null;

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
	public UnderwritingYearDto selectByCropYear(Integer cropYear) throws DaoException {

		logger.debug("<selectByCropYear");

		UnderwritingYearDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
			result = this.mapper.selectByCropYear(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByCropYear " + result);
		return result;

	}
	
	@Override
	public void insert(UnderwritingYearDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			String underwritingYearGuid = (String) parameters.get("underwritingYearGuid");
			dto.setUnderwritingYearGuid(underwritingYearGuid);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + dto.getUnderwritingYearGuid());
	}

	@Override
	public void delete(String underwritingYearGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("underwritingYearGuid", underwritingYearGuid);
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
	public void deleteByCropYear(Integer cropYear) throws DaoException {
		logger.debug("<deleteByCropYear");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
			int count = this.mapper.deleteByCropYear(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteByCropYear");
	}

}
