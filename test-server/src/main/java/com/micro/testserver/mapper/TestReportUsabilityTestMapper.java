package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportUsabilityTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportUsabilityTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportUsabilityTestMapper {
    public Collection<TestReportUsabilityTestDto> toDtos(Collection<TestReportUsabilityTest> objs);
    public Collection<TestReportUsabilityTest> toObjs(Collection<TestReportUsabilityTestDto> dtos);
    public TestReportUsabilityTest toObj(TestReportUsabilityTestDto dto);
    public TestReportUsabilityTestDto toDto(TestReportUsabilityTest obj);
}
