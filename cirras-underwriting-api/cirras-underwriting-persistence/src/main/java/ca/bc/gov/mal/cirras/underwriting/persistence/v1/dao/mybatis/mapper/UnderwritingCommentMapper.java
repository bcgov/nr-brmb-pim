package ca.bc.gov.mal.cirras.underwriting.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.underwriting.persistence.v1.dto.UnderwritingCommentDto;

public interface UnderwritingCommentMapper {

	UnderwritingCommentDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);

	int deleteForAnnualField(Map<String, Object> parameters);
	
	int deleteForDeclaredYieldContractGuid(Map<String, Object> parameters);
	
	int deleteForField(Map<String, Object> parameters);
	
	void deleteForVerifiedYieldSummaryGuid(Map<String, Object> parameters);

	void deleteForVerifiedYieldContract(Map<String, Object> parameters);

	List<UnderwritingCommentDto> select(Map<String, Object> parameters);
	
	List<UnderwritingCommentDto> selectForDopContract(Map<String, Object> parameters);

	List<UnderwritingCommentDto> selectForField(Map<String, Object> parameters);
	
	List<UnderwritingCommentDto> selectForVerifiedYieldSummary(Map<String, Object> parameters);

}
