package com.micro.testserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MinioFileItem implements Serializable {
    public String fileType;
    public String fileName;
    public String fileUri;

    public MinioFileItem(String type, String name)
    {
        fileType = type;
        fileName = name;
        fileUri = null;
    }

    public MinioFileItem(String type, String name, String uri)
    {
        fileType = type;
        fileName = name;
        fileUri = uri;
    }

    public MinioFileItem()
    {
        fileType = null;
        fileName = null;
        fileUri = null;
    }
}

