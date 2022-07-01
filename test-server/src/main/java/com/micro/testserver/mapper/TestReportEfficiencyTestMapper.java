package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportEfficiencyTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportEfficiencyTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportEfficiencyTestMapper {
    public Collection<TestReportEfficiencyTestDto> toDtos(Collection<TestReportEfficiencyTest> objs);
    public Collection<TestReportEfficiencyTest> toObjs(Collection<TestReportEfficiencyTestDto> dtos);
    public TestReportEfficiencyTest toObj(TestReportEfficiencyTestDto dto);
    public TestReportEfficiencyTestDto toDto(TestReportEfficiencyTest obj);
}
