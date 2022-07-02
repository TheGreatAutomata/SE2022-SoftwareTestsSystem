package com.micro.testserver.mapper;

import com.micro.dto.TestCaseEntryDto;
import com.micro.dto.TestProgressTableDto;
import com.micro.testserver.model.TestCaseEntry;
import com.micro.testserver.model.TestProgressTable;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestProgressTableMapper {
    public Collection<TestProgressTableDto> toDtos(Collection<TestProgressTable> objs);
    public Collection<TestProgressTable> toObjs(Collection<TestProgressTableDto> dtos);
    public TestProgressTable toObj(TestProgressTableDto dto);
    public TestProgressTableDto toDto(TestProgressTable obj);
}
