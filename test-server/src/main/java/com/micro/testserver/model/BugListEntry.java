package com.micro.testserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BugListEntry {
    public String 问题缺陷简要描述;

    public String 对应需求条目;

    public String 发现缺陷的初始条件;

    public String 发现缺陷用例及具体操作路径要具体;

    public String 关联用例;

    public String 时间;

    public String 责任人;

    public String 修改建议;
}
