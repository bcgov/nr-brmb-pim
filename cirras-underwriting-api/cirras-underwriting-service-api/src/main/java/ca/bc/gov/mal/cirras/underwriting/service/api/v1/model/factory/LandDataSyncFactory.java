package ca.bc.gov.mal.cirras.underwriting.service.api.v1.model.factory;

import ca.bc.gov.mal.cirras.underwriting.model.v1.AnnualFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.ContractedFieldDetail;
import ca.bc.gov.mal.cirras.underwriting.model.v1.Field;
import ca.bc.gov.mal.cirras.underwriting.model.v1.GrowerContractYear;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLand;
import ca.bc.gov.mal.cirras.underwriting.model.v1.LegalLandFieldXref;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.AnnualFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.ContractedFieldDetailDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.FieldDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GrowerContractYearDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandDto;
import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.LegalLandFieldXrefDto;

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
