package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.CropVarietyInsPlantInsXrefDto;

public interface CropVarietyInsPlantInsXrefMapper {
	
	CropVarietyInsPlantInsXrefDto fetch(Map<String, Object> parameters);

	List<CropVarietyInsPlantInsXrefDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int deleteForVariety(Map<String, Object> parameters);
	
	List<CropVarietyInsPlantInsXrefDto> selectPlantInsForCropVarieties(Map<String, Object> parameters);

	List<CropVarietyInsPlantInsXrefDto> selectForInsurancePlan(Map<String, Object> parameters);

}
