package ca.bc.gov.mal.cirras.underwriting.services.model.factory;

import ca.bc.gov.mal.cirras.underwriting.data.models.AnnualFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.ContractedFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.data.models.Field;
import ca.bc.gov.mal.cirras.underwriting.data.models.GrowerContractYear;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.data.models.LegalLandFieldXref;
import ca.bc.gov.mal.cirras.underwriting.data.entities.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.data.entities.LegalLandFieldXrefDto;

public interface LandDataSyncFactory {

	//Legal Land
	void updateLegalLand(LegalLandDto dto, LegalLand<? extends Field> model);
	LegalLand<? extends Field> getLegalLandSync(LegalLandDto dto);

	//Field
	void updateField(FieldDto dto, Field model);
	Field getField(FieldDto dto);

	//Legal Land - Field xref
	void updateLegalLandFieldXref(LegalLandFieldXrefDto dto, LegalLandFieldXref model);
	LegalLandFieldXref getLegalLandFieldXrefSync(LegalLandFieldXrefDto dto);
	
	//Annual Field Detail
	void updateAnnualFieldDetail(AnnualFieldDetailDto dto, AnnualFieldDetail model);
	AnnualFieldDetail getAnnualFieldDetail(AnnualFieldDetailDto dto);
		
	//Grower Contract Year
	void updateGrowerContractYear(GrowerContractYearDto dto, GrowerContractYear model);
	GrowerContractYear getGrowerContractYear(GrowerContractYearDto dto);
	
	//Contracted Field Detail
	void updateContractedFieldDetail(ContractedFieldDetailDto dto, ContractedFieldDetail model);
	ContractedFieldDetail getContractedFieldDetail(ContractedFieldDetailDto dto);

}
