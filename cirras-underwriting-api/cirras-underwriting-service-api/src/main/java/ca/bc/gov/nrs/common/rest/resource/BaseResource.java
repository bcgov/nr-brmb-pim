package ca.bc.gov.nrs.common.rest.resource;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.rest.resource.types.ResourceTypes;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public abstract class BaseResource extends TypedResource {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseResource.class);

	private static final long serialVersionUID = 1L;

	private String eTag;

	private Long cacheExpiresMillis;

	private List<RelLink> links = new ArrayList<RelLink>();

	@JsonIgnore
	public String getSelfLink() {
		String result = null;
		for(RelLink link:getLinks()) {
			if(ResourceTypes.SELF.equals(link.getRel())) {
				result = link.getHref();
			}
		}
		return result;
	}

	public boolean hasLink(String resourceType) {
		boolean result = false;

		for (RelLink link : getLinks()) {
			if (resourceType.equals(link.getRel())) {
				result = true;
				break;
			}
		}

		return result;
	}

	public RelLink getLink(String resourceType) {
		RelLink result = null;

		for (RelLink link : getLinks()) {
			if (resourceType.equals(link.getRel())) {
				result = link;
				break;
			}
		}

		return result;
	}

	@JsonIgnore
	public String getQuotedETag() {
		return getETag(true);
	}

	@JsonIgnore
	public String getUnquotedETag() {
		return getETag(false);
	}

	private String getETag(boolean quoted) {
		logger.debug("etag="+eTag);
		String result = null;
		if (this.eTag != null) {
			if (quoted) {
				if (eTag.charAt(0) == '"') {
					result = this.eTag;
				} else {
					result = '"' + this.eTag + '"';
				}
			} else {
				if (eTag.charAt(0) == '"') {
					result = this.eTag.substring(1, this.eTag.length() - 1);
				} else {
					result = this.eTag;
				}
			}
		}
		logger.debug("etag="+eTag);
		return result;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	@JsonIgnore
	public Long getCacheExpiresMillis() {
		return cacheExpiresMillis;
	}

	public void setCacheExpiresMillis(Long cacheExpiresMillis) {
		this.cacheExpiresMillis = cacheExpiresMillis;
	}

	public List<RelLink> getLinks() {
		return links;
	}

	public void setLinks(List<RelLink> links) {
		this.links = links;
	}
}
