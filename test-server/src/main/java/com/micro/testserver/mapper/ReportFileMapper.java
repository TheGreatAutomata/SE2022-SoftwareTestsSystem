package com.micro.testserver.mapper;

import com.micro.contractserver.model.minioFileItem;
import com.micro.dto.SingleFileDto;
import com.micro.testserver.model.MinioFileItem;
import org.springframework.stereotype.Component;

@Component
public class ReportFileMapper {

    public SingleFileDto toSingleFileDto(MinioFileItem f) {
        return new SingleFileDto().fileName(f.getFileName()).fileType(f.getFileType()).fileUri(f.getFileUri());
    }

}
