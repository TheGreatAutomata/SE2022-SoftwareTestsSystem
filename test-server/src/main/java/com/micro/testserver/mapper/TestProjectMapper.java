package com.micro.testserver.mapper;

import com.micro.dto.TestProjectDto;
import com.micro.dto.TestRecordEntryDto;
import com.micro.testserver.model.TestProject;
import com.micro.testserver.model.TestRecordEntry;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestProjectMapper {
    public Collection<TestProjectDto> toDtos(Collection<TestProject> objs);
    public Collection<TestProject> toObjs(Collection<TestProjectDto> dtos);
    public TestProject toObj(TestProjectDto dto);
    public TestProjectDto toDto(TestProject obj);
}
