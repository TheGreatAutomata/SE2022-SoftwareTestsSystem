package com.micro.contractserver.mapper;

import com.micro.contractserver.model.minioFileItem;
import com.micro.dto.SingleFileDto;
import org.springframework.stereotype.Component;

@Component
public class ContractFileMapper {
    
    public SingleFileDto toSingleFileDto(minioFileItem f) {
        return new SingleFileDto().fileName(f.getFileName()).fileType(f.getFileType()).fileUri(f.getFileUri());
    }
    
}
