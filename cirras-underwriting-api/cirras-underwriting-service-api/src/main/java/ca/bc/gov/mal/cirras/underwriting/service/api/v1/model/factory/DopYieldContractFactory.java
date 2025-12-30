package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualField;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContract;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodity;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityForage;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldForageCut;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldGrain;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollup;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldFieldRollupForage;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldFieldRollupForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventoryContractCommodityDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.InventorySeededForageDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.PolicyDto;

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

	List<DeclaredYieldFieldRollupForageDto> getDopForageRollupCommoditiesFromInventorySeeded(
			List<InventorySeededForageDto> dtos
		) throws FactoryException;
	
	void updateDto(DeclaredYieldFieldForageDto dto, DopYieldFieldForageCut model);

	void updateDto(DeclaredYieldContractCommodityForageDto dto, DopYieldContractCommodityForage model);

	void updateDto(DeclaredYieldFieldRollupForageDto dto, DopYieldFieldRollupForage model);
}
