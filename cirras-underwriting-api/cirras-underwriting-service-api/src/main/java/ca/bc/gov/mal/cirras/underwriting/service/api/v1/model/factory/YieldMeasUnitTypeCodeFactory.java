package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

public interface YieldMeasUnitTypeCodeFactory {

	YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> getYieldMeasUnitTypeCodeList(
			List<YieldMeasUnitTypeCodeDto> dtos
			)throws FactoryException, DaoException;
}
