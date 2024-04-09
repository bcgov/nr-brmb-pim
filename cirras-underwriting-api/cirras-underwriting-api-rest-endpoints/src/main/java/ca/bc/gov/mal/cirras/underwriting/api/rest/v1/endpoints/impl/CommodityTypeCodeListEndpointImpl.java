package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.CommodityTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.security.Scopes;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.service.api.v1.CirrasMaintenanceService;

public class CommodityTypeCodeListEndpointImpl extends BaseEndpointsImpl implements CommodityTypeCodeListEndpoint {
		
	@Autowired
	private CirrasMaintenanceService cirrasMaintenanceService;

	@Override
	public Response getCommodityTypeCodeList(
			String insurancePlanId) {
		
		Response response = null;
		
		logRequest();
		
		if(!hasAuthority(Scopes.GET_CODE_TABLES)) {
			return Response.status(Status.FORBIDDEN).build();
		}

		try {

			CommodityTypeCodeListRsrc results = (CommodityTypeCodeListRsrc) cirrasMaintenanceService.getCommodityTypeCodeList(
					toInteger(insurancePlanId),
					getFactoryContext(), 
					getWebAdeAuthentication());

				GenericEntity<CommodityTypeCodeListRsrc> entity = new GenericEntity<CommodityTypeCodeListRsrc>(results) {
					/* do nothing */
				};

				response = Response.ok(entity).tag(results.getUnquotedETag()).build();
			
		} catch (Throwable t) {
			response = getInternalServerErrorResponse(t);
		}
		
		logResponse(response);

		return response;
	}

}
