package ca.bc.gov.mal.cirras.underwriting.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.CropCommodityList;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

/**************************************************************
  This is used for all commodity related data (Crop Commodity, Crop Variety, Crop Types etc.)
 **************************************************************/
public interface CirrasCommodityService {

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	CropCommodityList<? extends CropCommodity> getCropCommodityList(
			Integer insurancePlanId, 
			Integer cropYear,
	    	String commodityType,
	    	Boolean loadChildren,
			FactoryContext context, 
			WebAdeAuthentication authentication
	) 
	throws ServiceException;	

}
