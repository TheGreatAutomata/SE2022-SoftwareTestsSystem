package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportReliabilityTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportReference;
import com.micro.testserver.model.TestReportReliabilityTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportReliabilityTestMapper {
    public Collection<TestReportReliabilityTestDto> toDtos(Collection<TestReportReliabilityTest> objs);
    public Collection<TestReportReliabilityTest> toObjs(Collection<TestReportReliabilityTestDto> dtos);
    public TestReportReliabilityTest toObj(TestReportReliabilityTestDto dto);
    public TestReportReliabilityTestDto toDto(TestReportReliabilityTest obj);
}
