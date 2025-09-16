package ca.bc.gov.nrs.common.rest.resource;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class TypedResource implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@XmlTransient
	public String get_type() {
		return null;
	}
}
