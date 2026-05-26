package ca.bc.gov.mal.cirras.underwriting.controllers.async;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.underwriting.data.models.BaseOutbox;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerriesOutbox;
import ca.bc.gov.mal.cirras.underwriting.services.CirrasUnderwritingOutboxService;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class DopYieldContractCommodityBerriesOutboxProcessor extends OutboxProcessor{

	protected DopYieldContractCommodityBerriesOutboxProcessor(Properties applicationProperties) {
		super(applicationProperties);
	}

	private static final Logger logger = LoggerFactory.getLogger(DopYieldContractCommodityBerriesOutboxProcessor.class);

	@Override
	protected String getOutboxClassName() {
		return DopYieldContractCommodityBerriesOutbox.class.getName();
	}

	@Override
	protected List<? extends BaseOutbox> getNextOutboxes(int maxRecords, WebAdeAuthentication authentication,
			CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) throws ServiceException {
 		return cirrasUnderwritingOutboxService.getNextDopYieldContractCommodityBerriesOutboxes(maxRecords, authentication);
	}

	@Override
	protected void processOutbox(BaseOutbox outbox, boolean doPublishEvent, WebAdeAuthentication authentication,
			CirrasUnderwritingOutboxService cirrasUnderwritingOutboxService) throws ServiceException {
		DopYieldContractCommodityBerriesOutbox dopYieldContractCommodityBerriesOutbox = (DopYieldContractCommodityBerriesOutbox)outbox;
		cirrasUnderwritingOutboxService.processDopYieldContractCommodityBerriesOutbox(dopYieldContractCommodityBerriesOutbox, doPublishEvent, authentication);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
