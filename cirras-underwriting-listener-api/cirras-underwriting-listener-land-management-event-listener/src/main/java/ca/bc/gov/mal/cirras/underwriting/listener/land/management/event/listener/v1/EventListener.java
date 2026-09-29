package ca.bc.gov.mal.cirras.underwriting.listener.land.management.event.listener.v1;

public interface EventListener {
	
	public String getProcessName();

	public void startListening();
	
	public void stopListening();

}
