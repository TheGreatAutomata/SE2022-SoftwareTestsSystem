package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestReportReferenceDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestReportReference;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestReportReferenceMapper {
    public Collection<TestReportReferenceDto> toDtos(Collection<TestReportReference> objs);
    public Collection<TestReportReference> toObjs(Collection<TestReportReferenceDto> dtos);
    public TestReportReference toObj(TestReportReferenceDto dto);
    public TestReportReferenceDto toDto(TestReportReference obj);
}
