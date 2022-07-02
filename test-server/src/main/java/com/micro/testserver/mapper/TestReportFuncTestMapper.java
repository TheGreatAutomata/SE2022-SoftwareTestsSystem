package com.micro.testserver.mapper;


import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportFuncTestDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportFuncTest;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportFuncTestMapper {
    public Collection<TestReportFuncTestDto> toDtos(Collection<TestReportFuncTest> objs);
    public Collection<TestReportFuncTest> toObjs(Collection<TestReportFuncTestDto> dtos);
    public TestReportFuncTest toObj(TestReportFuncTestDto dto);
    public TestReportFuncTestDto toDto(TestReportFuncTest obj);
}
