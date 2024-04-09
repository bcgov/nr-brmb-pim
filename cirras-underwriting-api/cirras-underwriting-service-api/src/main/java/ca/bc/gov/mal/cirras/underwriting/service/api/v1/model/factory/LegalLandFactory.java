package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandList;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;

public interface LegalLandFactory {

	LegalLandList<? extends LegalLand<? extends Field>> getLegalLandList(
			PagedDtos<LegalLandDto> dtos,
			String legalLocation, 
			String primaryPropertyIdentifier, 
			String growerInfo,
			String datasetType, 
			Boolean isWildCardSearch, 
			Boolean searchByLegalLocOrLegalDesc, 
			String sortColumn,
			String sortDirection, 
			Integer pageRowCount,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) 
		throws FactoryException, DaoException;

	void createQuickLegalLand(LegalLandDto dto, AnnualField model, String pid);
	void updateLegalLand(LegalLandDto dto, LegalLand<? extends Field> model);

	LegalLand<? extends Field> getLegalLand(
			LegalLandDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication);


}
