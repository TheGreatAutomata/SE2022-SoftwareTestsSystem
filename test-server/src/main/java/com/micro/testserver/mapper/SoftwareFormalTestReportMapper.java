package com.micro.testserver.mapper;

import com.micro.dto.FormalTestReportDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareFormalTestReport;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareFormalTestReportMapper {
    public Collection<FormalTestReportDto> toDtos(Collection<SoftwareFormalTestReport> objs);
    public Collection<SoftwareFormalTestReport> toObjs(Collection<FormalTestReportDto> dtos);
    public SoftwareFormalTestReport toObj(FormalTestReportDto dto);
    public FormalTestReportDto toDto(SoftwareFormalTestReport obj);
}
