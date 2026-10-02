package ca.bc.gov.mal.cirras.underwriting.listener.service.api.v1.validation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.service.api.validation.BaseValidator;

public class ModelValidator extends BaseValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelValidator.class);
	
	public ModelValidator() {
		logger.debug("<ModelValidator");

		logger.debug(">ModelValidator");
	}

	protected static List<Message> addParentPath(List<Message> messages, String parentPath) {
		
		for(Message message:messages) {
			
			addParentPath(message, parentPath);
		}
		
		return messages;
	}
	
	public static Message addParentPath(Message message, String parentPath) {
		
		String path = message.getPath();
		path = (path == null) ? "" : path;
		path = parentPath + "." + path;
		message.setPath(path);
		
		return message;
	}
}
