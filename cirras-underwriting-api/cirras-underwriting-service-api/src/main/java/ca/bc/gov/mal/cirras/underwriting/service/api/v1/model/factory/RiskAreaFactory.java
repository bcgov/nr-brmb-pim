package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskArea;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskAreaList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface RiskAreaFactory {

	public RiskAreaList<? extends RiskArea> getRiskAreaList(
			List<RiskAreaDto> dtos,
			Integer insurancePlanId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;

}
