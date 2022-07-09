package com.micro.testserver.mapper;

import com.micro.dto.SingleFileDto;
import com.micro.dto.SoftwareSingleFileDto;
import com.micro.testserver.model.BugListEntry;
import com.micro.testserver.model.MinioFileItem;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareReportMinioItemMapper {
    public Collection<SoftwareSingleFileDto> toDtos(Collection<MinioFileItem> objs);
    public Collection<MinioFileItem> toObjs(Collection<SoftwareSingleFileDto> dtos);
    public MinioFileItem toObj(SoftwareSingleFileDto dto);
    public SoftwareSingleFileDto toDto(MinioFileItem obj);
}
