package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.OfferTable;
import com.micro.dto.OfferTableDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface OfferTableMapper {
    public Collection<OfferTableDto> toOfferTableDtos(Collection<OfferTable> offerTable);

    public Collection<OfferTable> toOfferTables(Collection<OfferTableDto> offerTableDto);

    public OfferTable toOfferTable(OfferTableDto offerTableDto);

    public OfferTableDto toOfferTableDto(OfferTable offerTable);
}
