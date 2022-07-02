package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestProgressEntryDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestProgressEntry;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestProgressEntryMapper {
    public Collection<TestProgressEntryDto> toDtos(Collection<TestProgressEntry> objs);
    public Collection<TestProgressEntry> toObjs(Collection<TestProgressEntryDto> dtos);
    public TestProgressEntry toObj(TestProgressEntryDto dto);
    public TestProgressEntryDto toDto(TestProgressEntry obj);
}
