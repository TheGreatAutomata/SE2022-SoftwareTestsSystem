package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportPortabilityTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportPortabilityTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportPortabilityTestMapper {
    public Collection<TestReportPortabilityTestDto> toDtos(Collection<TestReportPortabilityTest> objs);
    public Collection<TestReportPortabilityTest> toObjs(Collection<TestReportPortabilityTestDto> dtos);
    public TestReportPortabilityTest toObj(TestReportPortabilityTestDto dto);
    public TestReportPortabilityTestDto toDto(TestReportPortabilityTest obj);
}
