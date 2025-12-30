package ca.bc.gov.mal.cirras.underwriting.services;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.data.resources.AnnualFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.ContractedFieldDetailRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.FieldRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.GrowerContractYearSyncRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandFieldXrefRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.LegalLandRsrc;
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
	LegalLandRsrc getLegalLand(
		Integer legalLandId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeLegalLand (LegalLandRsrc model, FactoryContext factoryContext,
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
	FieldRsrc getField(
	Integer fieldId,
	FactoryContext factoryContext, 
	WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeField (FieldRsrc model, FactoryContext factoryContext,
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
	LegalLandFieldXrefRsrc getLegalLandFieldXref(
		Integer legalLandId,
		Integer fieldId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeLegalLandFieldXref (LegalLandFieldXrefRsrc model, FactoryContext factoryContext,
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
	AnnualFieldDetailRsrc getAnnualFieldDetail(
		Integer annualFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeAnnualFieldDetail (AnnualFieldDetailRsrc model, FactoryContext factoryContext,
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
	GrowerContractYearSyncRsrc getGrowerContractYear(
		Integer growerContractYearId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeGrowerContractYear (GrowerContractYearSyncRsrc model, FactoryContext factoryContext,
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
	ContractedFieldDetailRsrc getContractedFieldDetail(
		Integer contractedFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeContractedFieldDetail (ContractedFieldDetailRsrc model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteContractedFieldDetail(
		Integer contractedFieldDetailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
		)
		throws ServiceException, NotFoundException, DaoException;
	
}
