package com.micro.contractserver.model;

import lombok.Data;

@Data
public class minioFileItem {
    public String fileType;
    public String fileName;
    public String fileUri;

    public minioFileItem(String type, String name)
    {
        fileType = type;
        fileName = name;
        fileUri = null;
    }

    public minioFileItem(String type, String name, String uri)
    {
        fileType = type;
        fileName = name;
        fileUri = uri;
    }

    public minioFileItem()
    {
        fileType = null;
        fileName = null;
        fileUri = null;
    }
}
