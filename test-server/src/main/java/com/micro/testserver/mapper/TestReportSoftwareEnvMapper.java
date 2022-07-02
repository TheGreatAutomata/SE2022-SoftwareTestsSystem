package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportSoftwareEnvDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportSoftwareEnv;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportSoftwareEnvMapper {
    public Collection<TestReportSoftwareEnvDto> toDtos(Collection<TestReportSoftwareEnv> objs);
    public Collection<TestReportSoftwareEnv> toObjs(Collection<TestReportSoftwareEnvDto> dtos);
    public TestReportSoftwareEnv toObj(TestReportSoftwareEnvDto dto);
    public TestReportSoftwareEnvDto toDto(TestReportSoftwareEnv obj);
}
