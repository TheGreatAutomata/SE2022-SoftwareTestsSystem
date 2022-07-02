package com.micro.testserver.mapper;

import com.micro.dto.BugListDto;
import com.micro.dto.BugListEntryDto;
import com.micro.testserver.model.BugListEntry;
import com.micro.testserver.model.SoftwareBugList;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface BugListEntryMapper {
    public Collection<BugListEntryDto> toDtos(Collection<BugListEntry> objs);
    public Collection<BugListEntry> toObjs(Collection<BugListEntryDto> dtos);
    public BugListEntry toObj(BugListEntryDto dto);
    public BugListEntryDto toDto(BugListEntry obj);
}
