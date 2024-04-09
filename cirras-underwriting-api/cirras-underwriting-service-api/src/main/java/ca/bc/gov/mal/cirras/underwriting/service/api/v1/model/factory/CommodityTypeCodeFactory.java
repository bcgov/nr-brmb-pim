package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CommodityTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CommodityTypeCodeFactory {

	public CommodityTypeCodeList<? extends CommodityTypeCode> getCommodityTypeCodeList(
			List<CommodityTypeCodeDto> dtos,
			Integer insurancePlanId,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;

}
