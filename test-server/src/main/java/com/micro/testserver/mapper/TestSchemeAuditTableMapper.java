package com.micro.testserver.mapper;

import com.micro.dto.TestSchemeAuditTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SchemeEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestSchemeAuditTableMapper {
    public Collection<TestSchemeAuditTableDto> toDtos(Collection<SchemeEvaluationTable> objs);
    public Collection<SchemeEvaluationTable> toObjs(Collection<TestSchemeAuditTableDto> dtos);
    public SchemeEvaluationTable toObj(TestSchemeAuditTableDto dto);
    public TestSchemeAuditTableDto toDto(SchemeEvaluationTable obj);
}
