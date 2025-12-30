package ca.bc.gov.mal.cirras.underwriting.controllers.impl;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.BaseEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.CommodityTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.underwriting.data.resources.CommodityTypeCodeListRsrc;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasMaintenanceService;

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
