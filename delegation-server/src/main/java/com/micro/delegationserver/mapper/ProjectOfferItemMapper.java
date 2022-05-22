package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ProjectOfferItem;
import com.micro.dto.ProjectItemDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ProjectOfferItemMapper {
    public Collection<ProjectItemDto> toProjectItemDtos(Collection<ProjectOfferItem> projectOfferItem);

    public Collection<ProjectOfferItem> toProjectOfferItems(Collection<ProjectItemDto> projectItemDto);

    public ProjectOfferItem toProjectOfferItem(ProjectItemDto projectItemDto);

    public ProjectItemDto toProjectItemDto(ProjectOfferItem projectOfferItem);
}
