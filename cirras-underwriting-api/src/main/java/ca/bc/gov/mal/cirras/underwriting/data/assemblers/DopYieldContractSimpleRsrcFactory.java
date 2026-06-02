package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesDto;

public class DopYieldContractSimpleRsrcFactory extends BaseResourceFactory { 
	
	public DopYieldContractSimpleRsrc getDopYieldContractSimple(DeclaredYieldContractCommodityBerriesDto dto) throws FactoryException {

		DopYieldContractSimpleRsrc resource = new DopYieldContractSimpleRsrc();

		resource.setContractId(dto.getContractId());
		resource.setCropYear(dto.getCropYear());
		resource.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());

		// Declared Yield Contract Commodity Berries
		DopYieldContractCommodityBerries dyccbModel = createDopYieldContractCommodityBerries(dto);
		resource.setDopYieldContractCommodityBerries(dyccbModel);
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;

	}
	
	private DopYieldContractCommodityBerries createDopYieldContractCommodityBerries(DeclaredYieldContractCommodityBerriesDto dto) {
		DopYieldContractCommodityBerries model = new DopYieldContractCommodityBerries();

		model.setDeclaredYieldContractCommodityBerriesGuid(dto.getDeclaredYieldContractCommodityBerriesGuid());
		model.setCropCommodityId(dto.getCropCommodityId());
		model.setCropCommodityName(dto.getCropCommodityName());
		model.setDeclaredYieldContractGuid(dto.getDeclaredYieldContractGuid());
		model.setTotalProduction(dto.getTotalProduction());
		model.setTotalProductionOverride(dto.getTotalProductionOverride());
		model.setTotalPlantedAcres(dto.getTotalPlantedAcres());
		model.setTotalMatureEquivalentAcres(dto.getTotalMatureEquivalentAcres());
		model.setTotalSoldShippedYield(dto.getTotalSoldShippedYield());
		model.setTotalSalesYield(dto.getTotalSalesYield());
		model.setTotalAbandonmentYield(dto.getTotalAbandonmentYield());

		return model;
	}
	
}
