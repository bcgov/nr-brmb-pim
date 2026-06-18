package ca.bc.gov.mal.cirras.underwriting.data.assemblers;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.mal.cirras.underwriting.data.entities.DeclaredYieldContractCommodityBerriesOutboxDto;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerriesOutbox;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

public class OutboxFactory extends BaseResourceFactory { 
	
	
	//======================================================================================================================
	// Dop Yield Contract Commodity Berries Outbox
	//======================================================================================================================

	public List<DopYieldContractCommodityBerriesOutbox> getDopYieldContractCommodityBerriesOutboxList(List<DeclaredYieldContractCommodityBerriesOutboxDto> dtos)
			throws FactoryException {

		List<DopYieldContractCommodityBerriesOutbox> dopYieldContractCommodityBerriesOutboxes = null;
		
		if ( dtos != null && !dtos.isEmpty() ) {

			dopYieldContractCommodityBerriesOutboxes = new ArrayList<DopYieldContractCommodityBerriesOutbox>();
			
			for ( DeclaredYieldContractCommodityBerriesOutboxDto dto : dtos ) { 
				DopYieldContractCommodityBerriesOutbox model = new DopYieldContractCommodityBerriesOutbox();
				populateModel(model, dto);
				dopYieldContractCommodityBerriesOutboxes.add(model);
			}
			
		}
		
		return dopYieldContractCommodityBerriesOutboxes;
	}

	
	private void populateModel(DopYieldContractCommodityBerriesOutbox model, DeclaredYieldContractCommodityBerriesOutboxDto dto) {
		
		model.setDeclaredYieldContractCommodityBerriesOutboxId(dto.getDeclaredYieldContractCommodityBerriesOutboxId());
		model.setDeclaredYieldContractCommodityBerriesGuid(dto.getDeclaredYieldContractCommodityBerriesGuid());
		model.setCreateDate(dto.getCreateDate());
		model.setTransactionType(dto.getAuditTransactionTypeCode());

	}

}
