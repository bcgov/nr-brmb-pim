package ca.bc.gov.mal.cirras.underwriting.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.GrowerRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.PolicyRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeCode;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CommodityTypeVarietyXref;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Contact;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContactEmail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContactPhone;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Grower;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GrowerContact;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Policy;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Product;
import ca.bc.gov.mal.cirras.underwriting.model.v1.SyncCode;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasDataSyncService {

	////////////////////////////////////////////////////////////////////
	//Generic Code Tables
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncCode getSyncCode(
		String codeTableType, 
		String uniqueKey,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCode (SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncCode(
		String codeTableType, 
		String uniqueKey,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, DaoException;
	
	////////////////////////////////////////////////////////////////////
	//Grower
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	Grower getGrower(
		Integer growerId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeGrower (GrowerRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteGrower(
		Integer growerId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Policy
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	Policy getPolicy(
		Integer policyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizePolicy (PolicyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deletePolicy(
		Integer policyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	
	////////////////////////////////////////////////////////////////////
	//Product
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	Product getProduct(
		Integer productId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeProduct (ProductRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteProduct(
		Integer productId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	
	////////////////////////////////////////////////////////////////////
	//Commodity Variety Sync
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncCommodityVariety getSyncCommodityVariety(
		Integer crptId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCommodityVariety (SyncCommodityVariety model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncCommodityVariety(
		Integer crptId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Contact
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	Contact getContact(
		Integer contactId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeContact (Contact model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteContact(
		Integer contactId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Grower Contact
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	GrowerContact getGrowerContact(
		Integer growerContactId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeGrowerContact (GrowerContact model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteGrowerContact(
		Integer growerContactId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Contact Email
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ContactEmail getContactEmail(
		Integer contactEmailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeContactEmail (ContactEmail model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteContactEmail(
		Integer contactEmailId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Contact Phone
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ContactPhone getContactPhone(
		Integer contactPhoneId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeContactPhone (ContactPhone model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteContactPhone(
		Integer contactPhoneId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Commodity Type Code
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CommodityTypeCode getCommodityTypeCode(
		String commodityTypeCode,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCommodityTypeCode (CommodityTypeCode model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteCommodityTypeCode(
		String commodityTypeCode,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Commodity Type Variety Xref
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CommodityTypeVarietyXref getCommodityTypeVarietyXref(
		String commodityTypeCode,
		Integer cropVarietyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCommodityTypeVarietyXref(CommodityTypeVarietyXref model, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteCommodityTypeVarietyXref(
		String commodityTypeCode,
		Integer cropVarietyId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
}
