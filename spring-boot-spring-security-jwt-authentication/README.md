**更多详细内容参考`BezCoder`的主页**

**security**: 用的Spring security，目前完全契合我们需求，无须更改

**controllers** 处理Http Request，会正确处理权限以及安全问题

**repository** 与数据库进行交互的部分

**models** 定义了user和role的model，他们是多对多对的关系

**payload** 定义了Request和Response



+ 关于配置数据库和jwt，在application.propertites
+ 关于如何修改数据库表，参考jpa注释注解
+ 关于不同角色的权限认证，参考models.ERole，这里需要DBA手动修改数据库，后续可以视情况在项目启动时即插入。
+ 关于依赖问题，修改了pom文件部分，以适应jdk15，尽量不要更改。

