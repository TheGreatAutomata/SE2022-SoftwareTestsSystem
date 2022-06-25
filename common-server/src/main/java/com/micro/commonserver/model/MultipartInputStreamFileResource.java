package com.micro.commonserver.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class MultipartInputStreamFileResource implements Serializable {

    private final String filename;
    private final Long size;

    private final byte[] inputStream;

    public MultipartInputStreamFileResource(byte[] inputStream, String filename, Long size) {
        this.filename = filename;
        this.size = size;
        this.inputStream = inputStream;
    }


    public String getFilename() {
        return this.filename;
    }

    public Long getSize()
    {
        return this.size;
    }

    public byte[] getInputStream()
    {
        return this.inputStream;
    }
}
