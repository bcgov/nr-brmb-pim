package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifier;
import ca.bc.gov.mal.cirras.underwriting.data.models.GradeModifierList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GradeModifierDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface GradeModifierFactory {

	public GradeModifierList<? extends GradeModifier> getGradeModifierList(
			List<GradeModifierDto> dtos,
			Integer cropYear,
			Integer insurancePlanId,
			Integer cropCommodityId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;

	public void updateDto(GradeModifierDto dto, GradeModifier model) throws FactoryException;

}
