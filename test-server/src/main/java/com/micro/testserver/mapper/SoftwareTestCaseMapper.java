package com.micro.testserver.mapper;

import com.micro.dto.TestCaseDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareTestCase;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareTestCaseMapper {
    public Collection<TestCaseDto> toDtos(Collection<SoftwareTestCase> objs);
    public Collection<SoftwareTestCase> toObjs(Collection<TestCaseDto> dtos);
    public SoftwareTestCase toObj(TestCaseDto dto);
    public TestCaseDto toDto(SoftwareTestCase obj);
}
