package ca.bc.gov.mal.cirras.underwriting.listener.policies.event.listener.v1;

public interface EventListener {
	
	public String getProcessName();

	public void startListening();
	
	public void stopListening();

}
