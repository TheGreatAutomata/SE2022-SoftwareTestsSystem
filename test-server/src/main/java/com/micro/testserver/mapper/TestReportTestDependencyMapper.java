package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportTestDependencyDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportTestDependency;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportTestDependencyMapper {
    public Collection<TestReportTestDependencyDto> toDtos(Collection<TestReportTestDependency> objs);
    public Collection<TestReportTestDependency> toObjs(Collection<TestReportTestDependencyDto> dtos);
    public TestReportTestDependency toObj(TestReportTestDependencyDto dto);
    public TestReportTestDependencyDto toDto(TestReportTestDependency obj);
}
