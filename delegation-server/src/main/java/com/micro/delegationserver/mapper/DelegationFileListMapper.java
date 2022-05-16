package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.DelegationFileList;
import com.micro.dto.DelegationApplicationTableDto;
import com.micro.dto.DelegationFileListDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface DelegationFileListMapper {

    public Collection<DelegationFileListDto> toDtos(Collection<DelegationFileList> fileLists);

    public Collection<DelegationFileList> toObjs(Collection<DelegationFileListDto> fileListDtos);

    public DelegationFileList toObj(DelegationFileListDto fileListDto);

    public DelegationFileListDto toDto(DelegationFileList fileList);

}
