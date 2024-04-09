package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.model.v1.YieldMeasUnitConversionList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.YieldMeasUnitConversionDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface YieldMeasUnitConversionFactory {

	public YieldMeasUnitConversionList<? extends YieldMeasUnitConversion> getYieldMeasUnitConversionList(
			List<YieldMeasUnitConversionDto> dtos,
			Integer insurancePlanId,
			String srcYieldMeasUnitTypeCode,
			String targetYieldMeasUnitTypeCode,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) throws FactoryException, DaoException;
	
	public void updateDto(YieldMeasUnitConversionDto dto, YieldMeasUnitConversion model) throws FactoryException;

}
