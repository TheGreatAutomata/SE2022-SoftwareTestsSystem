package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.OfferConfirmation;
import com.micro.delegationserver.model.OfferTable;
import com.micro.dto.OfferSignItemDto;
import com.micro.dto.OfferTableDto;
import org.mapstruct.Mapper;

@Mapper
public interface OfferConfirmationMapper {
    public OfferConfirmation toOfferConfirmation(OfferSignItemDto offerSignItemDto);

    public OfferSignItemDto toOfferSignItemDto(OfferConfirmation offerConfirmation);
}
