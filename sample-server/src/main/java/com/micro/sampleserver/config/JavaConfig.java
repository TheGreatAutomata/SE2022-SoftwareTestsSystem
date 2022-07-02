package com.micro.sampleserver.config;

import com.micro.sampleserver.delegate.CallDelegationServerDelegate;
import com.micro.sampleserver.delegate.SamplePutServer;
import com.micro.sampleserver.delegate.SaveFileDelegate;
import com.micro.sampleserver.delegate.SaveMassageDelegate;
import com.micro.sampleserver.mapper.SampleMessageMapper;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.micro.sampleserver.mapper.*;
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

        Resource[] resources=new Resource[1];
        resources[0]=new ClassPathResource("processes/sample_application.bpmn20.xml");
        springProcessEngineConfiguration.setDeploymentResources(resources);
        springProcessEngineConfiguration.setDeploymentMode("single-resource");

        HashMap<Object,Object> beans=new HashMap<>();

        beans.put("callDelegationServerDelegate",callDelegationServerDelegate());
        beans.put("saveFileDelegate",saveFileDelegate());
        beans.put("saveMassageDelegate",saveMassageDelegate());
        beans.put("samplePutServer", samplePutServer());
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
    public CallDelegationServerDelegate callDelegationServerDelegate()
    {
        return new CallDelegationServerDelegate();
    }

    @Bean
    public SaveFileDelegate saveFileDelegate()
    {
        return new SaveFileDelegate();
    }

    @Bean
    public SaveMassageDelegate saveMassageDelegate()
    {
        return new SaveMassageDelegate();
    }

    @Bean
    public SampleMessageMapper sampleMessageMapper()
    {
        return new SampleMessageMapperImpl();
    }

    @Bean
    public SamplePutServer samplePutServer()
    {
        return new SamplePutServer();
    }

}
