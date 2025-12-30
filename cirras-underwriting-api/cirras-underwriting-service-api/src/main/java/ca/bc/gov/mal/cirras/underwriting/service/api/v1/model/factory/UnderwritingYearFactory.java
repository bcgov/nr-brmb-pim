package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingYear;
import ca.bc.gov.mal.cirras.underwriting.data.models.UnderwritingYearList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.UnderwritingYearDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface UnderwritingYearFactory {

	public UnderwritingYearList<? extends UnderwritingYear> getUnderwritingYearList(
			List<UnderwritingYearDto> dtos,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;
	
	public void updateDto(
			UnderwritingYearDto dto, 
			UnderwritingYear model
		) throws FactoryException;

	public UnderwritingYear getUnderwritingYear(
			UnderwritingYearDto dto, 
			FactoryContext context,
			WebAdeAuthentication authentication
	) throws FactoryException;

}
