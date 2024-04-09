package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.GradeModifierDto;

public interface GradeModifierMapper {
	
	GradeModifierDto fetch(Map<String, Object> parameters);

	List<GradeModifierDto> fetchAll(Map<String, Object> parameters);

	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<GradeModifierDto> selectByYearPlanCommodity(Map<String, Object> parameters);
}
