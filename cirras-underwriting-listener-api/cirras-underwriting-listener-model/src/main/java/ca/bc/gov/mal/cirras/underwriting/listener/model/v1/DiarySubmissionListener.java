package ca.bc.gov.mal.cirras.underwriting.listener.model.v1;

import java.io.Serializable;
import java.util.Map;

public interface DiarySubmissionListener extends Serializable {

	public SubmissionListenerType getDiarySubmissionListenerType();
	public void setDiarySubmissionListenerType(SubmissionListenerType submissionListenerType);

	public String getDescription();
	public void setDescription(String description);

	public Map<String, String> getProperties();
	public void setProperties(Map<String, String> properties);
}
