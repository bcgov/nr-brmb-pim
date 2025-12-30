package ca.bc.gov.mal.cirras.underwriting.data.repositories;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface GradeModifierTypeCodeDao extends Serializable {
	
	GradeModifierTypeCodeDto fetch(String GradeModifierTypeCodeGuid) throws DaoException;

    List<GradeModifierTypeCodeDto> fetchAll() throws DaoException;

    void insert(GradeModifierTypeCodeDto dto, String userId) throws DaoException;
    
    void update(GradeModifierTypeCodeDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String GradeModifierTypeCodeGuid) throws DaoException, NotFoundDaoException;

    List<GradeModifierTypeCodeDto> select(Integer cropYear) throws DaoException;
   
}
