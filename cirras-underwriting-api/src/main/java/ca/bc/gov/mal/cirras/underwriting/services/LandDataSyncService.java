package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContractedFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.mal.cirras.underwriting.data.models.GrowerContractYear;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandFieldXref;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface LandDataSyncService {

	////////////////////////////////////////////////////////////////////
	// Legal Land
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLand<? extends Field> getLegalLand(
		Integer legalLandId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeLegalLand (LegalLand<? extends Field> model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteLegalLand(
		Integer legalLandId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	
	////////////////////////////////////////////////////////////////////
	// Field
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	Field getField(
	Integer fieldId,
	FactoryContext factoryContext, 
	WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeField (Field model, FactoryContext factoryContext,
	WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteField(
	Integer fieldId,
	FactoryContext factoryContext, 
	WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;


	////////////////////////////////////////////////////////////////////
	// Legal Land - Field Xref
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	LegalLandFieldXref getLegalLandFieldXref(
		Integer legalLandId,
		Integer fieldId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeLegalLandFieldXref (LegalLandFieldXref model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteLegalLandFieldXref(
		Integer legalLandId,
		Integer fieldId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
		
	////////////////////////////////////////////////////////////////////
	// Annual Field Detail
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	AnnualFieldDetail getAnnualFieldDetail(
		Integer annualFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeAnnualFieldDetail (AnnualFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteAnnualFieldDetail(
		Integer annualFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	// Grower Contract Year
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GrowerContractYear getGrowerContractYear(
		Integer growerContractYearId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeGrowerContractYear (GrowerContractYear model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteGrowerContractYear(
		Integer growerContractYearId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	////////////////////////////////////////////////////////////////////
	// Contracted Field Detail
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ContractedFieldDetail getContractedFieldDetail(
		Integer contractedFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeContractedFieldDetail (ContractedFieldDetail model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteContractedFieldDetail(
		Integer contractedFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
}
