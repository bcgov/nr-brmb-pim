package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierType;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GradeModifierTypeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface GradeModifierTypeFactory {

	public GradeModifierTypeList<? extends GradeModifierType> getGradeModifierTypeList(
			List<GradeModifierTypeCodeDto> dtos,
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;
	
	public void updateDto(GradeModifierTypeCodeDto dto, GradeModifierType model) throws FactoryException;

}
