package com.micro.testserver.mapper;

import com.micro.dto.TestReportDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareTestReport;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareTestReportMapper {
    public Collection<TestReportDto> toDtos(Collection<SoftwareTestReport> objs);
    public Collection<SoftwareTestReport> toObjs(Collection<TestReportDto> dtos);
    public SoftwareTestReport toObj(TestReportDto dto);
    public TestReportDto toDto(SoftwareTestReport obj);
}
