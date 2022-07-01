package com.micro.testserver.mapper;

import com.micro.dto.BugListEntryDto;
import com.micro.dto.TestCaseEntryDto;
import com.micro.testserver.model.BugListEntry;

import com.micro.testserver.model.TestCaseEntry;
import org.aspectj.weaver.ast.Test;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestCaseEntryMapper {
    public Collection<TestCaseEntryDto> toDtos(Collection<TestCaseEntry> objs);
    public Collection<TestCaseEntry> toObjs(Collection<TestCaseEntryDto> dtos);
    public TestCaseEntry toObj(TestCaseEntryDto dto);
    public TestCaseEntryDto toDto(TestCaseEntry obj);
}

