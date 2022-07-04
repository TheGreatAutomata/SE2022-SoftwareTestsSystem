package com.micro.testserver.model;

import com.micro.dto.TestRecordEntryDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SoftwareTestRecord implements Serializable {
    public String name;
    public List<TestRecordEntry> 软件测试记录 = new ArrayList<>();
}
