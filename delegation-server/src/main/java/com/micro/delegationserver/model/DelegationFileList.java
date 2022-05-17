package com.micro.delegationserver.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DelegationFileList implements Serializable {
    public List<String> fileName;
}
