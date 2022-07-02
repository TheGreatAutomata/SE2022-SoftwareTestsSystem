package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportHardwareEnvDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportHardwareEnv;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportHardwareEnvMapper {
    public Collection<TestReportHardwareEnvDto> toDtos(Collection<TestReportHardwareEnv> objs);
    public Collection<TestReportHardwareEnv> toObjs(Collection<TestReportHardwareEnvDto> dtos);
    public TestReportHardwareEnv toObj(TestReportHardwareEnvDto dto);
    public TestReportHardwareEnvDto toDto(TestReportHardwareEnv obj);
}
