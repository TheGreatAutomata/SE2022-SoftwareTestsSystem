package com.softwaretest.delegationmicroservice;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

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
        springProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
        return springProcessEngineConfiguration;
    }

    @Bean
    public ProcessEngine processEngine() throws Exception{
        ProcessEngineFactoryBean factoryBean=new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        ProcessEngine processEngine=factoryBean.getObject();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //这个是删除，注意用的是deployment_id_
        repositoryService.deleteDeployment("35001",true);

        //这个是添加
        repositoryService.createDeployment()
                .addClasspathResource("processes/delegation.bpmn20.xml")
                .deploy();

        return factoryBean.getObject();
    }

    @Bean
    public RuntimeService runtimeService() throws Exception{
        return processEngine().getRuntimeService();
    }
}
