package ca.bc.gov.mal.cirras.underwriting.data.resources;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ca.bc.gov.mal.cirras.underwriting.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.transformers.InstantJacksonDeserializer;
import ca.bc.gov.nrs.common.wfone.rest.resource.transformers.InstantJacksonSerializer;

@JsonSubTypes({ @Type(value = UnderwritingEvent.class, name = ResourceTypes.NAMESPACE+"publishEvent") })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class UnderwritingEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;	

	private String eventType;
	
	@JsonSerialize(using=InstantJacksonSerializer.class)
	@JsonDeserialize(using=InstantJacksonDeserializer.class)
	private Instant eventTimestamp;
	
	private String sourceType;
	
	private String sourceLink;
	
	private Map<String, String> sourceIdentifiers = new HashMap<>();
	
	private BaseResource resourceBeforeUpdate;
	
	private BaseResource resourceAfterUpdate;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Instant getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(Instant eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public Map<String, String> getSourceIdentifiers() {
		return sourceIdentifiers;
	}

	public void setSourceIdentifiers(Map<String, String> sourceIdentifiers) {
		this.sourceIdentifiers = sourceIdentifiers;
	}

	public BaseResource getResourceBeforeUpdate() {
		return resourceBeforeUpdate;
	}

	public BaseResource getResourceAfterUpdate() {
		return resourceAfterUpdate;
	}

	public void setResourceBeforeUpdate(BaseResource resourceBeforeUpdate) {
		this.resourceBeforeUpdate = resourceBeforeUpdate;
	}

	public void setResourceAfterUpdate(BaseResource resourceAfterUpdate) {
		this.resourceAfterUpdate = resourceAfterUpdate;
	}
	
}
