package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.minioFileItem;
import com.micro.dto.AllFilesDto;
import com.micro.dto.SingleFileDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DelegationFilesMapper {
    public SingleFileDto toSingleFileDto(minioFileItem f)
    {
        return new SingleFileDto().fileName(f.getFileName()).fileType(f.getFileType()).fileUri(f.getFileUri());
    }

    public AllFilesDto toAllFilesDto(List<minioFileItem> fileItemList)
    {
        AllFilesDto result = new AllFilesDto();
        for(minioFileItem f : fileItemList)
        {
            result.addFilesItem(toSingleFileDto(f));
        }
        return result;
    }
}
