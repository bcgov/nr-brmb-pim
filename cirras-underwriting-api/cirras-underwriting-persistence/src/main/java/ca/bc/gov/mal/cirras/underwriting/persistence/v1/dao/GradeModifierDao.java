package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface GradeModifierDao extends Serializable {
	
	GradeModifierDto fetch(String gradeModifierGuid) throws DaoException;

    List<GradeModifierDto> fetchAll() throws DaoException;

    void insert(GradeModifierDto dto, String userId) throws DaoException;
    
    void update(GradeModifierDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String gradeModifierGuid) throws DaoException, NotFoundDaoException;

    List<GradeModifierDto> selectByYearPlanCommodity(Integer cropYear, Integer insurancePlanId, Integer cropCommodityId) throws DaoException;
   
}
