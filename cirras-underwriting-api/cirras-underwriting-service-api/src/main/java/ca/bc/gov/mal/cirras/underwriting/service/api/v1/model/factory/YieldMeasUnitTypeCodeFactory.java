package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitTypeCodeList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitTypeCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

public interface YieldMeasUnitTypeCodeFactory {

	YieldMeasUnitTypeCodeList<? extends YieldMeasUnitTypeCode> getYieldMeasUnitTypeCodeList(
			List<YieldMeasUnitTypeCodeDto> dtos
			)throws FactoryException, DaoException;
}
