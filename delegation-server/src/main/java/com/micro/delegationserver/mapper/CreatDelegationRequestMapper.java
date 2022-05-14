package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.CreatDelegationRequest;
import com.micro.delegationserver.model.DelegationApplicationTable;
import com.micro.delegationserver.model.DelegationFileList;
import com.micro.delegationserver.model.DelegationFunctionTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import com.micro.dto.*;
import org.springframework.stereotype.Component;

@Component
public class CreatDelegationRequestMapper {
    public Collection<CreatDelegationRequestDto> toCreatDelegationRequestsDto(Collection<CreatDelegationRequest> creatDelegationRequests){
        Collection<CreatDelegationRequestDto> ret=new ArrayList<>();
        for (CreatDelegationRequest r:
             creatDelegationRequests) {
            ret.add(toCreatDelegationRequestDto(r));
        }
        return ret;
    }

    public Collection<CreatDelegationRequest> toCreatDelegationRequests(Collection<CreatDelegationRequestDto> creatDelegationRequestDtos){
        Collection<CreatDelegationRequest> ret=new ArrayList<>();
        for (CreatDelegationRequestDto r:
                creatDelegationRequestDtos) {
            ret.add(toCreatDelegationRequest(r));
        }
        return ret;
    }

    public CreatDelegationRequest toCreatDelegationRequest(CreatDelegationRequestDto creatDelegationRequestDto){
        DelegationApplicationTable delegationApplicationTable=new DelegationApplicationTable();
        delegationApplicationTable.setName(creatDelegationRequestDto.getApplicationTable().getName());

        DelegationFunctionTable delegationFunctionTable=new DelegationFunctionTable();
        delegationFunctionTable.setName(creatDelegationRequestDto.getFunctionTable().getName());

        DelegationFileList delegationFileList=new DelegationFileList();
        delegationFileList.setFileName(creatDelegationRequestDto.getFileList().getFileName());

        CreatDelegationRequest creatDelegationRequest=new CreatDelegationRequest();

        creatDelegationRequest.setApplicationTable(delegationApplicationTable);
        creatDelegationRequest.setFunctionTable(delegationFunctionTable);
        creatDelegationRequest.setFileList(delegationFileList);

        return creatDelegationRequest;
    }

    public CreatDelegationRequestDto toCreatDelegationRequestDto(CreatDelegationRequest creatDelegationRequest){
        DelegationApplicationTableDto delegationApplicationTableDto=new DelegationApplicationTableDto();
        delegationApplicationTableDto.setName(creatDelegationRequest.getApplicationTable().getName());

        DelegationFunctionTableDto delegationFunctionTableDto=new DelegationFunctionTableDto();
        delegationFunctionTableDto.setName(creatDelegationRequest.getFunctionTable().getName());

        DelegationFileListDto delegationFileListDto=new DelegationFileListDto();
        delegationFileListDto.setFileName(creatDelegationRequest.getFileList().getFileName());

        CreatDelegationRequestDto creatDelegationRequestDto=new CreatDelegationRequestDto();

        creatDelegationRequestDto.setApplicationTable(delegationApplicationTableDto);
        creatDelegationRequestDto.setFunctionTable(delegationFunctionTableDto);
        creatDelegationRequestDto.setFileList(delegationFileListDto);

        return creatDelegationRequestDto;
    }

}
