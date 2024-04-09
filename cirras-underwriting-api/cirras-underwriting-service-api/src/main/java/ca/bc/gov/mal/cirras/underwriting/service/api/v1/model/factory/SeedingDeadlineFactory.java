package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadline;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SeedingDeadlineList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.SeedingDeadlineDto;
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
