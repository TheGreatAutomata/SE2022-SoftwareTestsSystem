package com.micro.testserver.mapper;

import com.micro.dto.BugListDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareBugList;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareBugListMapper {
    public Collection<BugListDto> toDtos(Collection<SoftwareBugList> objs);
    public Collection<SoftwareBugList> toObjs(Collection<BugListDto> dtos);
    public SoftwareBugList toObj(BugListDto dto);
    public BugListDto toDto(SoftwareBugList obj);
}
