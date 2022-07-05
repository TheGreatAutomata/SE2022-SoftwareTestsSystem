package com.micro.testserver.model;

import com.micro.dto.TestCaseEntryDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SoftwareTestCase implements Serializable {
    public String name;
    public List<TestCaseEntry> 测试用例 = new ArrayList<>();
}
