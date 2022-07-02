package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestRecordDto;
import com.micro.dto.TestRecordEntryDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestRecordEntry;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestRecordEntryMapper {
    public Collection<TestRecordEntryDto> toDtos(Collection<TestRecordEntry> objs);
    public Collection<TestRecordEntry> toObjs(Collection<TestRecordEntryDto> dtos);
    public TestRecordEntry toObj(TestRecordEntryDto dto);
    public TestRecordEntryDto toDto(TestRecordEntry obj);
}
