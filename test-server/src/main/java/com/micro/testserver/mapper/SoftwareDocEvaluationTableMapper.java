package com.micro.testserver.mapper;

import com.micro.dto.DocEvaluationTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareDocEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareDocEvaluationTableMapper {
    public Collection<DocEvaluationTableDto> toDtos(Collection<SoftwareDocEvaluationTable> objs);
    public Collection<SoftwareDocEvaluationTable> toObjs(Collection<DocEvaluationTableDto> dtos);
    public SoftwareDocEvaluationTable toObj(DocEvaluationTableDto dto);
    public DocEvaluationTableDto toDto(SoftwareDocEvaluationTable obj);
}
