package ca.bc.gov.mal.cirras.underwriting;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.AnnualFieldRolloverInvEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.CommodityTypeCodeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContactEmailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContactEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContactPhoneEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ContractedFieldDetailEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.CropCommodityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.CropVarietyInsurabilityListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.DopYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.DopYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.DopYieldContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.FieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GradeModifierListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GradeModifierTypeListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GrowerContactEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GrowerContractYearSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.GrowerEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.InventoryContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.InventoryContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.InventoryContractReportEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandFieldXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.LegalLandSyncEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.PolicyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.ProductEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.RiskAreaListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SeedingDeadlineListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncClaimCalculationSimpleEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncCommodityTypeCodeEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncCommodityTypeVarietyXrefEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.SyncCommodityVarietyEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.TopLevelEndpoints;
import ca.bc.gov.mal.cirras.underwriting.controllers.UnderwritingYearEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UnderwritingYearListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UserSettingEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UserSettingListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractRolloverDopYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractRolloverInvEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractRolloverVerifiedYieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateAddFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateRemoveFieldEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateRenameLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.UwContractValidateReplaceLegalEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.VerifiedYieldContractEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.VerifiedYieldContractListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.VerifiedYieldContractSimpleEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.YieldMeasUnitConversionListEndpoint;
import ca.bc.gov.mal.cirras.underwriting.controllers.YieldMeasUnitTypeCodeListEndpoint;
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
		
		register(TopLevelEndpoints.class);
		register(CodeTableEndpointsImpl.class);
		register(CodeTableListEndpointsImpl.class);

		register(UwContractListEndpoint.class);
		register(UwContractEndpoint.class);
		register(UwContractRolloverInvEndpoint.class);
		register(UwContractValidateAddFieldEndpoint.class);
		register(UwContractValidateRemoveFieldEndpoint.class);
		register(UwContractValidateRenameLegalEndpoint.class);
		register(UwContractValidateReplaceLegalEndpoint.class);
		register(AnnualFieldListEndpoint.class);
		register(InventoryContractEndpoint.class);
		register(InventoryContractListEndpoint.class);
		register(InventoryContractReportEndpoint.class);
		register(CropCommodityListEndpoint.class);
		register(SyncCodeEndpoint.class);
		register(GrowerEndpoint.class);
		register(PolicyEndpoint.class);
		register(ProductEndpoint.class);
		register(LegalLandSyncEndpoint.class);
		register(FieldEndpoint.class);
		register(LegalLandFieldXrefEndpoint.class);
		register(AnnualFieldDetailEndpoint.class);
		register(GrowerContractYearSyncEndpoint.class);
		register(ContractedFieldDetailEndpoint.class);
		register(SyncCommodityVarietyEndpoint.class);
		register(ContactEndpoint.class);
		register(GrowerContactEndpoint.class);
		register(ContactEmailEndpoint.class);
		register(ContactPhoneEndpoint.class);
		register(LegalLandListEndpoint.class);
		register(LegalLandEndpoint.class);
		register(AnnualFieldRolloverInvEndpoint.class);
		register(SyncCommodityTypeCodeEndpoint.class);
		register(SyncCommodityTypeVarietyXrefEndpoint.class);
		register(UwContractRolloverDopYieldEndpoint.class);
		register(YieldMeasUnitTypeCodeListEndpoint.class);
		register(GradeModifierListEndpoint.class);
		register(GradeModifierTypeListEndpoint.class);
		register(DopYieldContractEndpoint.class);
		register(DopYieldContractListEndpoint.class);
		register(DopYieldContractReportEndpoint.class);
		register(RiskAreaListEndpoint.class);
		register(CommodityTypeCodeListEndpoint.class);
		register(SeedingDeadlineListEndpoint.class);
		register(UnderwritingYearEndpoint.class);
		register(UnderwritingYearListEndpoint.class);
		register(CropVarietyInsurabilityListEndpoint.class);
		register(YieldMeasUnitConversionListEndpoint.class);
		register(UwContractRolloverVerifiedYieldEndpoint.class);
		register(VerifiedYieldContractEndpoint.class);
		register(VerifiedYieldContractListEndpoint.class);
		register(VerifiedYieldContractSimpleEndpoint.class);
		register(UserSettingListEndpoint.class);
		register(UserSettingEndpoint.class);
		register(SyncClaimCalculationSimpleEndpoint.class);
		

		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);

		SwaggerConfiguration oasConfig = new SwaggerConfiguration()
			.prettyPrint(Boolean.TRUE)
			.resourcePackages(
				Stream.of(
					"ca.bc.gov.mal.cirras.underwriting.controllers",
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
