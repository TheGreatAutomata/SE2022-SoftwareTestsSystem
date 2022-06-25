package com.micro.testserver.mapper;

import com.micro.dto.TestReportEvaluationTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareReportEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareReportEvaluationMapper {
    public Collection<TestReportEvaluationTableDto> toDtos(Collection<SoftwareReportEvaluationTable> objs);
    public Collection<SoftwareReportEvaluationTable> toObjs(Collection<TestReportEvaluationTableDto> dtos);
    public SoftwareReportEvaluationTable toObj(TestReportEvaluationTableDto dto);
    public TestReportEvaluationTableDto toDto(SoftwareReportEvaluationTable obj);
}
