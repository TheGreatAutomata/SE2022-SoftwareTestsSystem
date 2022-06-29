package com.micro.contractserver.mapper;


import com.micro.contractserver.model.NormalResponse;
import com.micro.dto.NormalResponseDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface NormalResponseMapper {

    public Collection<NormalResponseDto> toDtos(Collection<NormalResponse> normalResponses);

    public Collection<NormalResponse> toObjs(Collection<NormalResponseDto> normalResponseDtos);

    public NormalResponse toObj(NormalResponseDto normalResponseDto);

    public NormalResponseDto toDto(NormalResponse normalResponse);
    
}
