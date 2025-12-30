package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversion;
import ca.bc.gov.mal.cirras.underwriting.data.models.YieldMeasUnitConversionList;
import ca.bc.gov.mal.cirras.underwriting.data.entities.YieldMeasUnitConversionDto;
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
