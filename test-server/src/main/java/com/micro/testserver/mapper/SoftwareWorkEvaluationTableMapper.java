package com.micro.testserver.mapper;

import com.micro.dto.TestSchemeDto;
import com.micro.dto.WorkEvaluationTableDto;
import com.micro.testserver.model.SoftwareTestScheme;
import com.micro.testserver.model.SoftwareWorkEvaluationTable;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareWorkEvaluationTableMapper {
    public Collection<WorkEvaluationTableDto> toDtos(Collection<SoftwareWorkEvaluationTable> objs);
    public Collection<SoftwareWorkEvaluationTable> toObjs(Collection<WorkEvaluationTableDto> dtos);
    public SoftwareWorkEvaluationTable toObj(WorkEvaluationTableDto dto);
    public WorkEvaluationTableDto toDto(SoftwareWorkEvaluationTable obj);
}
