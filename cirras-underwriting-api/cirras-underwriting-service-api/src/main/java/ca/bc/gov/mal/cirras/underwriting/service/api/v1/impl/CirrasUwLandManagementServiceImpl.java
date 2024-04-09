package ca.bc.gov.mal.cirras.underwriting.service.api.v1.impl;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskArea;
import ca.bc.gov.mal.cirras.underwriting.model.v1.RiskAreaList;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandRiskArea;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.FieldDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandFieldXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.LegalLandRiskAreaXrefDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.RiskAreaDao;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.RiskAreaDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandRiskAreaXrefDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasUwLandManagementService;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.LegalLandRiskAreaXrefFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory.RiskAreaFactory;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.validation.ModelValidator;

public class CirrasUwLandManagementServiceImpl implements CirrasUwLandManagementService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasUwLandManagementServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private LegalLandFactory legalLandFactory;
	private RiskAreaFactory riskAreaFactory; 
	private LegalLandRiskAreaXrefFactory legalLandRiskAreaXrefFactory;

	// daos
	private LegalLandDao legalLandDao;
	private RiskAreaDao riskAreaDao;
	private LegalLandRiskAreaXrefDao legalLandRiskAreaXrefDao;
	private FieldDao fieldDao;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setRiskAreaFactory(RiskAreaFactory riskAreaFactory) {
		this.riskAreaFactory = riskAreaFactory;
	}

	public void setLegalLandFactory(LegalLandFactory legalLandFactory) {
		this.legalLandFactory = legalLandFactory;
	}

	public void setLegalLandRiskAreaXrefFactory(LegalLandRiskAreaXrefFactory legalLandRiskAreaXrefFactory) {
		this.legalLandRiskAreaXrefFactory = legalLandRiskAreaXrefFactory;
	}

	public void setLegalLandDao(LegalLandDao legalLandDao) {
		this.legalLandDao = legalLandDao;
	}
	
	public void setRiskAreaDao(RiskAreaDao riskAreaDao) {
		this.riskAreaDao = riskAreaDao;
	}
	
	public void setLegalLandRiskAreaXrefDao(LegalLandRiskAreaXrefDao legalLandRiskAreaXrefDao) {
		this.legalLandRiskAreaXrefDao = legalLandRiskAreaXrefDao;
	}
	
	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}
	
	@Override
	public LegalLand<? extends Field> createLegalLand(LegalLand<? extends Field> legalLand, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ValidationFailureException{
		logger.debug("<createLegalLand");

		LegalLand<? extends Field> result = null;

		try {

			String userId = getUserId(authentication);
			
			LegalLandDto dto = new LegalLandDto();

			legalLandFactory.updateLegalLand(dto, legalLand);
			
			legalLandDao.insert(dto, userId);
			
			legalLand.setLegalLandId(dto.getLegalLandId());
			
			saveLegalLandRiskAreas(legalLand, userId, authentication);

			result = getLegalLand(dto.getLegalLandId(), factoryContext, authentication);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createLegalLand");

		return result;
	}
	
	@Override
	public LegalLand<? extends Field> updateLegalLand(
			Integer legalLandId, 
			String optimisticLock,
			LegalLand<? extends Field> legalLand, 
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException,
			ValidationFailureException {
	
		logger.debug("<updateLegalLand");

		LegalLand<? extends Field> result = null;

		try {

			String userId = getUserId(authentication);
	
			LegalLandDto dto = legalLandDao.fetch(legalLandId);
	
			if (dto == null) {
				throw new NotFoundException("Did not find the legal land: " + legalLandId);
			}
	
			legalLandFactory.updateLegalLand(dto, legalLand);
			legalLandDao.update(dto, userId);
			
			saveLegalLandRiskAreas(legalLand, userId, authentication);

			result = getLegalLand(legalLandId, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">updateLegalLand");
		
		return result;
	}

	private void saveLegalLandRiskAreas(LegalLand<? extends Field> legalLand, String userId, WebAdeAuthentication authentication) throws ServiceException, DaoException {
		logger.debug("<saveLegalLandRiskAreas");

		List<LegalLandRiskArea> riskAreas = legalLand.getRiskAreas();
		if (riskAreas != null && !riskAreas.isEmpty()) {
			for (LegalLandRiskArea riskArea : riskAreas) {
				if (riskArea.getDeletedByUserInd() != null && riskArea.getDeletedByUserInd()) {
					deleteLegalLandRiskArea(riskArea, legalLand.getLegalLandId());
				} else {
					updateLegalLandRiskArea(riskArea, legalLand.getLegalLandId(), userId, authentication);
				}
			}
		}
		logger.debug(">saveLegalLandRiskAreas");
		
	}
	
	private void updateLegalLandRiskArea(LegalLandRiskArea riskArea, Integer legalLandId,
			String userId, WebAdeAuthentication authentication) throws DaoException, ServiceException {

		logger.debug("<updateLegalLandRiskArea");
		
		LegalLandRiskAreaXrefDto dto = null;

		if (riskArea.getRiskAreaId() != null) {
			dto = legalLandRiskAreaXrefDao.fetch(legalLandId, riskArea.getRiskAreaId());
		}
		
		if (dto == null) {
			// Insert if it doesn't exist
			dto = new LegalLandRiskAreaXrefDto();
			legalLandRiskAreaXrefFactory.createLegalLandRiskAreaXref(dto, riskArea);
			
			dto.setLegalLandId(legalLandId);
			
			legalLandRiskAreaXrefDao.insert(dto, userId);
			
		} else {

			legalLandRiskAreaXrefFactory.createLegalLandRiskAreaXref(dto, riskArea);

			legalLandRiskAreaXrefDao.update(dto, userId);
		}
		
		logger.debug(">updateLegalLandRiskArea");

	}
	
	private void deleteLegalLandRiskArea(LegalLandRiskArea riskArea, Integer legalLandId)
			throws NotFoundDaoException, DaoException {
		logger.debug("<deleteLegalLandRiskArea");
		
		LegalLandRiskAreaXrefDto dto = null;

		if (riskArea.getRiskAreaId() != null) {
			dto = legalLandRiskAreaXrefDao.fetch(legalLandId, riskArea.getRiskAreaId());
		}

		if (dto != null) {
			legalLandRiskAreaXrefDao.delete(legalLandId, riskArea.getRiskAreaId());
		}

		logger.debug(">deleteLegalLandRiskArea");
	}

	@Override
	public LegalLand<? extends Field> getLegalLand(
			Integer legalLandId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getLegalLand");

		LegalLand<? extends Field> result = null;

		try {
			
			LegalLandDto dto = legalLandDao.fetch(legalLandId);

			if (dto == null) {
				throw new NotFoundException("Did not find the legal land: " + legalLandId);
			}
			
			//Load Risk Areas
			loadRiskAreas(dto);
			
			//Load Associated Fields
			loadFields(dto);
			
			result = legalLandFactory.getLegalLand(dto, factoryContext, authentication);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getLegalLand");
		return result;
	}

	private void loadFields(LegalLandDto dto) throws DaoException {
		logger.debug("<loadFields");

		List<FieldDto> fields = fieldDao.selectForLegalLand(dto.getLegalLandId());
		dto.setFields(fields);
		
		logger.debug(">loadFields");
		
	}

	private void loadRiskAreas(LegalLandDto dto) throws DaoException {
		logger.debug("<loadRiskAreas");

		List<RiskAreaDto> riskAreas = riskAreaDao.selectByLegalLand(dto.getLegalLandId());
		dto.setRiskAreas(riskAreas);
		
		logger.debug(">loadRiskAreas");
	}

	@Override
	public void deleteLegalLand(Integer legalLandId, String optimisticLock,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		
		logger.debug("<deleteLegalLand");
		
		try {
			
			LegalLandDto dto = legalLandDao.fetch(legalLandId);

			if (dto == null) {
				throw new NotFoundException("Did not find the legal land: " + legalLandId);
			}

			legalLandRiskAreaXrefDao.deleteForLegalLand(legalLandId);
			legalLandDao.delete(legalLandId);
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteLegalLand");
	}

	@Override
	public RiskAreaList<? extends RiskArea> getRiskAreaList(Integer insurancePlanId,
			FactoryContext context, WebAdeAuthentication authentication) throws ServiceException {

		logger.debug("<getRiskAreaList");

		RiskAreaList<? extends RiskArea> result = null;
		
		try {
			List<RiskAreaDto> dtos = riskAreaDao.select(insurancePlanId);
			result = riskAreaFactory.getRiskAreaList(dtos, insurancePlanId, context, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getRiskAreaList");

		return result;
	}
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}
}
