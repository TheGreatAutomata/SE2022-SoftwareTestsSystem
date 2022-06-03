package com.micro.testserver.mapper;

import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestSchemeMapper {
    public Collection<TestSchemeDto> toDtos(Collection<SoftwareTestScheme> objs);
    public Collection<SoftwareTestScheme> toObjs(Collection<TestSchemeDto> dtos);
    public SoftwareTestScheme toObj(TestSchemeDto dto);
    public TestSchemeDto toDto(SoftwareTestScheme obj);
}
