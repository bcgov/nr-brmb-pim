package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.GradeModifierDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper.GradeModifierMapper;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class GradeModifierDaoImpl extends BaseDao implements GradeModifierDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(GradeModifierDaoImpl.class);

	@Autowired
	private GradeModifierMapper mapper;

	@Override
	public GradeModifierDto fetch(String gradeModifierGuid) throws DaoException {
		logger.debug("<fetch");

		GradeModifierDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("gradeModifierGuid", gradeModifierGuid);
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
	public List<GradeModifierDto> fetchAll() throws DaoException {

		logger.debug("<fetchAll");
		
		List<GradeModifierDto> dtos = null;

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
	public void insert(GradeModifierDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String gradeModifierGuid = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			gradeModifierGuid = (String) parameters.get("gradeModifierGuid");
			dto.setGradeModifierGuid(gradeModifierGuid);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + gradeModifierGuid);
	}
	

	@Override
	public void update(GradeModifierDto dto, String userId) throws DaoException, NotFoundDaoException {
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
	public void delete(String gradeModifierGuid) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("gradeModifierGuid", gradeModifierGuid);
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
	public List<GradeModifierDto> selectByYearPlanCommodity(Integer cropYear, Integer insurancePlanId, Integer cropCommodityId) throws DaoException {

		logger.debug("<selectByYearPlanCommodity");

		List<GradeModifierDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropYear", cropYear);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("cropCommodityId", cropCommodityId);
			dtos = this.mapper.selectByYearPlanCommodity(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByYearPlanCommodity " + dtos);
		return dtos;

	}

}
