package com.micro.testserver.mapper;

import com.micro.dto.TestRecordDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareTestRecord;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareTestRecordMapper {
    public Collection<TestRecordDto> toDtos(Collection<SoftwareTestRecord> objs);
    public Collection<SoftwareTestRecord> toObjs(Collection<TestRecordDto> dtos);
    public SoftwareTestRecord toObj(TestRecordDto dto);
    public TestRecordDto toDto(SoftwareTestRecord obj);
}
