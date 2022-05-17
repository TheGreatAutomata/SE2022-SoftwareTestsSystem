package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientEnterpriseInfo;
import com.micro.delegationserver.model.DelegationFileList;
import com.micro.dto.ClientEnterpriseInfoDto;
import com.micro.dto.DelegationFileListDto;
import com.mysql.cj.xdevapi.Client;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface ClientEnterpriseInfoMapper {
    public Collection<ClientEnterpriseInfoDto> toDtos(Collection<ClientEnterpriseInfo> clientEnterpriseInfos);

    public Collection<ClientEnterpriseInfo> toObjs(Collection<ClientEnterpriseInfoDto> clientEnterpriseInfoDtos);

    public ClientEnterpriseInfo toObj(ClientEnterpriseInfoDto clientEnterpriseInfoDto);

    public ClientEnterpriseInfoDto toDto(ClientEnterpriseInfo clientEnterpriseInfo);
}
