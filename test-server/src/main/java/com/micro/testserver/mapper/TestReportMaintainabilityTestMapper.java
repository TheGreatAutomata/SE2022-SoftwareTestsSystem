package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportMaintainabilityTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportMaintainabilityTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportMaintainabilityTestMapper {
    public Collection<TestReportMaintainabilityTestDto> toDtos(Collection<TestReportMaintainabilityTest> objs);
    public Collection<TestReportMaintainabilityTest> toObjs(Collection<TestReportMaintainabilityTestDto> dtos);
    public TestReportMaintainabilityTest toObj(TestReportMaintainabilityTestDto dto);
    public TestReportMaintainabilityTestDto toDto(TestReportMaintainabilityTest obj);
}
