package com.micro.testserver.model;

import com.micro.dto.BugListEntryDto;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SoftwareBugList implements Serializable {
    public String name;
    public List<BugListEntry> 项目列表 = new ArrayList<>();
}
