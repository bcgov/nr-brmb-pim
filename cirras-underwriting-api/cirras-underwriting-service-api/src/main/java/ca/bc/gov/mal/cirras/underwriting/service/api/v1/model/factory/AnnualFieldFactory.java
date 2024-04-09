package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;

public interface AnnualFieldFactory {

	
	AnnualFieldList<? extends AnnualField> getAnnualFieldList(
			List<FieldDto> dtos,
			Integer legalLandId, 
			Integer cropYear,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) 
		throws FactoryException, DaoException;

	void updateDto(ContractedFieldDetailDto dto, AnnualField model);

}
