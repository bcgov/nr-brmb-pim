package ca.bc.gov.mal.cirras.underwriting.api.rest.v1.jersey;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.CropCommodityListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.CropVarietyInsurabilityListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.DopYieldContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.DopYieldContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.DopYieldContractReportEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.GrowerEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.AnnualFieldDetailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.AnnualFieldListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.AnnualFieldRolloverInvEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.CommodityTypeCodeListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.ContactEmailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.ContactEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.ContactPhoneEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.ContractedFieldDetailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.InventoryContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.SyncCodeEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.SyncCommodityTypeCodeEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.SyncCommodityTypeVarietyXrefEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.SyncCommodityVarietyEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.InventoryContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.InventoryContractReportEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.LegalLandEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.FieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.GradeModifierListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.GradeModifierTypeListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.GrowerContactEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.GrowerContractYearSyncEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.LegalLandSyncEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.LegalLandFieldXrefEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.LegalLandListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.PolicyEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.RiskAreaListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.SeedingDeadlineListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.TopLevelEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UnderwritingYearEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UnderwritingYearListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractRolloverDopYieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractRolloverInvEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractRolloverVerifiedYieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractValidateAddFieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractValidateRemoveFieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractValidateRenameLegalEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.UwContractValidateReplaceLegalEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.VerifiedYieldContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.YieldMeasUnitConversionListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints.impl.YieldMeasUnitTypeCodeListEndpointImpl;
import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableListEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.jersey.JerseyResourceConfig;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

public class JerseyApplication extends JerseyResourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

	/**
	 * Register JAX-RS application components.
	 */
	public JerseyApplication(@Context ServletConfig servletConfig) {
		super();

		logger.debug("<JerseyApplication");
		
		register(MultiPartFeature.class);
		
		register(TopLevelEndpointsImpl.class);
		register(CodeTableEndpointsImpl.class);
		register(CodeTableListEndpointsImpl.class);

		register(UwContractListEndpointImpl.class);
		register(UwContractEndpointImpl.class);
		register(UwContractRolloverInvEndpointImpl.class);
		register(UwContractValidateAddFieldEndpointImpl.class);
		register(UwContractValidateRemoveFieldEndpointImpl.class);
		register(UwContractValidateRenameLegalEndpointImpl.class);
		register(UwContractValidateReplaceLegalEndpointImpl.class);
		register(AnnualFieldListEndpointImpl.class);
		register(InventoryContractEndpointImpl.class);
		register(InventoryContractListEndpointImpl.class);
		register(InventoryContractReportEndpointImpl.class);
		register(CropCommodityListEndpointImpl.class);
		register(SyncCodeEndpointImpl.class);
		register(GrowerEndpointImpl.class);
		register(PolicyEndpointImpl.class);
		register(LegalLandSyncEndpointImpl.class);
		register(FieldEndpointImpl.class);
		register(LegalLandFieldXrefEndpointImpl.class);
		register(AnnualFieldDetailEndpointImpl.class);
		register(GrowerContractYearSyncEndpointImpl.class);
		register(ContractedFieldDetailEndpointImpl.class);
		register(SyncCommodityVarietyEndpointImpl.class);
		register(ContactEndpointImpl.class);
		register(GrowerContactEndpointImpl.class);
		register(ContactEmailEndpointImpl.class);
		register(ContactPhoneEndpointImpl.class);
		register(LegalLandListEndpointImpl.class);
		register(LegalLandEndpointImpl.class);
		register(AnnualFieldRolloverInvEndpointImpl.class);
		register(SyncCommodityTypeCodeEndpointImpl.class);
		register(SyncCommodityTypeVarietyXrefEndpointImpl.class);
		register(UwContractRolloverDopYieldEndpointImpl.class);
		register(YieldMeasUnitTypeCodeListEndpointImpl.class);
		register(GradeModifierListEndpointImpl.class);
		register(GradeModifierTypeListEndpointImpl.class);
		register(DopYieldContractEndpointImpl.class);
		register(DopYieldContractListEndpointImpl.class);
		register(DopYieldContractReportEndpointImpl.class);
		register(RiskAreaListEndpointImpl.class);
		register(CommodityTypeCodeListEndpointImpl.class);
		register(SeedingDeadlineListEndpointImpl.class);
		register(UnderwritingYearEndpointImpl.class);
		register(UnderwritingYearListEndpointImpl.class);
		register(CropVarietyInsurabilityListEndpointImpl.class);
		register(YieldMeasUnitConversionListEndpointImpl.class);
		register(UwContractRolloverVerifiedYieldEndpointImpl.class);
		register(VerifiedYieldContractEndpointImpl.class);
		

		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);

		SwaggerConfiguration oasConfig = new SwaggerConfiguration()
			.prettyPrint(Boolean.TRUE)
			.resourcePackages(
				Stream.of(
					"ca.bc.gov.mal.cirras.underwriting.api.rest.v1.endpoints",
					"ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints",
					"ca.bc.gov.nrs.wfone.common.rest.endpoints"
				).collect(Collectors.toSet()));


        try {
            new JaxrsOpenApiContextBuilder<JaxrsOpenApiContextBuilder<?>>()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

		logger.debug(">JerseyApplication");
	}
}
