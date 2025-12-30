package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.data.models.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.SeedingDeadlineDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface SeedingDeadlineFactory {

	public SeedingDeadlineList<? extends SeedingDeadline> getSeedingDeadlineList(
			List<SeedingDeadlineDto> dtos,
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;
	
	public void updateDto(SeedingDeadlineDto dto, SeedingDeadline model) throws FactoryException;

}
