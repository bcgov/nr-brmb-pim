package ca.bc.gov.mal.cirras.underwriting;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.core.Context;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.controllers.impl.CropCommodityListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.CropVarietyInsurabilityListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.DopYieldContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.DopYieldContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.DopYieldContractReportEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.GrowerEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.AnnualFieldDetailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.AnnualFieldListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.AnnualFieldRolloverInvEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.CommodityTypeCodeListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.ContactEmailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.ContactEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.ContactPhoneEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.ContractedFieldDetailEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.InventoryContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.SyncCodeEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.SyncCommodityTypeCodeEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.SyncCommodityTypeVarietyXrefEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.SyncCommodityVarietyEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.InventoryContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.InventoryContractReportEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.LegalLandEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.FieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.GradeModifierListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.GradeModifierTypeListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.GrowerContactEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.GrowerContractYearSyncEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.LegalLandSyncEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.LegalLandFieldXrefEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.LegalLandListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.PolicyEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.ProductEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.RiskAreaListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.SeedingDeadlineListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.TopLevelEndpointsImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UnderwritingYearEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UnderwritingYearListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UserSettingEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UserSettingListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractRolloverDopYieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractRolloverInvEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractRolloverVerifiedYieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractValidateAddFieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractValidateRemoveFieldEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractValidateRenameLegalEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.UwContractValidateReplaceLegalEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.VerifiedYieldContractEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.VerifiedYieldContractListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.VerifiedYieldContractSimpleEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.YieldMeasUnitConversionListEndpointImpl;
import ca.bc.gov.mal.cirras.underwriting.controllers.impl.YieldMeasUnitTypeCodeListEndpointImpl;
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
		register(ProductEndpointImpl.class);
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
		register(VerifiedYieldContractListEndpointImpl.class);
		register(VerifiedYieldContractSimpleEndpointImpl.class);
		register(UserSettingListEndpointImpl.class);
		register(UserSettingEndpointImpl.class);
		

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
