package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.model.v1.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.PolicyDto;

public interface DopYieldContractFactory {

	DopYieldContract<? extends AnnualField> getDefaultDopYieldContract(
			PolicyDto policyDto,
			String defaultMeasurementUnitCode,	
			DeclaredYieldContractDto dycDto,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	DopYieldContract<? extends AnnualField> getDopYieldContract(
			DeclaredYieldContractDto dto, 
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	void updateDto(DeclaredYieldContractDto dto, DopYieldContract<? extends AnnualField> model, String userId);

	void updateDto(DeclaredYieldFieldDto dto, DopYieldFieldGrain model);
	
	List<DeclaredYieldContractCommodityDto> getDopCommoditiesFromInventoryCommodities(
			List<InventoryContractCommodityDto> dtos
		) throws FactoryException;
	
	List<DeclaredYieldFieldRollupDto> getDopFieldRollupCommoditiesFromInventoryCommodities(
			List<InventoryContractCommodityDto> dtos
		) throws FactoryException;

	void updateDto(DeclaredYieldFieldRollupDto dto, DopYieldFieldRollup model);

	void updateDto(DeclaredYieldContractCommodityDto dto, DopYieldContractCommodity model);
	
	
	List<DeclaredYieldContractCommodityForageDto> getDopForageCommoditiesFromInventorySeeded(
			List<InventorySeededForageDto> dtos
		) throws FactoryException;
}
