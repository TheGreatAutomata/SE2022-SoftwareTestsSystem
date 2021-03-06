package com.micro.testserver;

import com.micro.contractserver.mapper.ContractMapper;
import com.micro.contractserver.mapper.ContractMapperImpl;
import com.micro.contractserver.model.Contract;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.delegate.GenerateLatexReportDelegate;
import com.micro.testserver.delegate.GenerateTestReportDelegate;
import com.micro.testserver.mapper.*;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class JavaConfig {
    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(){
        DataSourceTransactionManager transactionManager= new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration(){
        SpringProcessEngineConfiguration springProcessEngineConfiguration=new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource());
        springProcessEngineConfiguration.setTransactionManager(transactionManager());
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        Resource[] resources=new Resource[3];
        resources[0]=new ClassPathResource("processes/test_apply.bpmn20.xml");
        resources[1]=new ClassPathResource("processes/test_audit.bpmn20.xml");
        resources[2]=new ClassPathResource("processes/test_reaudit.bpmn20.xml");


        springProcessEngineConfiguration.setDeploymentResources(resources);
        springProcessEngineConfiguration.setDeploymentMode("single-resource");

        HashMap<Object,Object> beans=new HashMap<>();

        beans.put("generateTestReportDelegate",generateTestReportDelegate());
        beans.put("generateLatexReportDelegate",generateLatexReportDelegate());

        springProcessEngineConfiguration.setBeans(beans);

        springProcessEngineConfiguration.setMailServerHost("smtp.qq.com");
        springProcessEngineConfiguration.setMailServerPort(465);
        springProcessEngineConfiguration.setMailServerDefaultFrom("2379594184@qq.com");
        springProcessEngineConfiguration.setMailServerUsername("2379594184@qq.com");
        springProcessEngineConfiguration.setMailServerPassword("qiijzfyfucxadhha");
        springProcessEngineConfiguration.setMailServerUseSSL(true);

        return springProcessEngineConfiguration;
    }

    @Bean
    public ProcessEngine processEngine() throws Exception{
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean.getObject();
    }

    @Bean
    public RuntimeService runtimeService() throws Exception{
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() throws Exception{
        return processEngine().getTaskService();
    }

    @Bean
    public RepositoryService repositoryService() throws Exception{
        return processEngine().getRepositoryService();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public TestSchemeMapper testSchemeMapper(){
        return new TestSchemeMapperImpl();
    }

    @Bean
    public TestSchemeAuditTableMapper testSchemeAuditTableMapper(){
        return new TestSchemeAuditTableMapperImpl();
    }

    @Bean
    public SoftwareBugListMapper softwareBugListMapper(){
        return new SoftwareBugListMapperImpl();
    }

    @Bean
    public SoftwareDocEvaluationTableMapper softwareDocEvaluationTableMapper(){
        return new SoftwareDocEvaluationTableMapperImpl();
    }

    @Bean
    public SoftwareFormalTestReportMapper softwareFormalTestReportMapper(){
        return new SoftwareFormalTestReportMapperImpl();
    }

    @Bean
    public SoftwareReportEvaluationMapper softwareReportEvaluationMapper(){
        return new SoftwareReportEvaluationMapperImpl();
    }

    @Bean
    public SoftwareTestCaseMapper softwareTestCaseMapper(){
        return new SoftwareTestCaseMapperImpl();
    }

    @Bean
    public SoftwareTestRecordMapper softwareTestRecordMapper(){
        return new SoftwareTestRecordMapperImpl();
    }

    @Bean
    public SoftwareTestReportMapper softwareTestReportMapper(){
        return new SoftwareTestReportMapperImpl();
    }

    @Bean
    public SoftwareWorkEvaluationTableMapper softwareWorkEvaluationTableMapper(){
        return new SoftwareWorkEvaluationTableMapperImpl();
    }

    @Bean
    public TestProjectMapper testProjectMapper(){
        return new TestProjectMapperImpl();
    }

    @Bean
    public GenerateTestReportDelegate generateTestReportDelegate(){
        return new GenerateTestReportDelegate();
    }

    @Bean
    public GenerateLatexReportDelegate generateLatexReportDelegate() {return new GenerateLatexReportDelegate();}

    @Bean
    public ContractMapper contractMapper(){return new ContractMapperImpl();
    }

    @Bean
    public SoftwareReportMinioItemMapper softwareReportMinioItemMapper(){return new SoftwareReportMinioItemMapperImpl();}

}

