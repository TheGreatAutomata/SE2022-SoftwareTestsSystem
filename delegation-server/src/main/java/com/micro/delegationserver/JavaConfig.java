package com.micro.delegationserver;

import com.micro.delegationserver.delegate.*;
import com.micro.delegationserver.mapper.*;
import com.micro.delegationserver.model.DelegationFunctionTable;
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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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
        Resource[] resources=new Resource[3];
        resources[0]=new ClassPathResource("processes/delegation.bpmn20.xml");
        resources[1]=new ClassPathResource("processes/delegation_apply.bpmn20.xml");
        resources[2]=new ClassPathResource("processes/delegation_modify.bpmn20.xml");
        springProcessEngineConfiguration.setDeploymentResources(resources);
        springProcessEngineConfiguration.setDeploymentMode("single-resource");

        HashMap<Object,Object> beans=new HashMap<>();

        beans.put("saveApplicationDelegate",saveApplicationDelegate());
        beans.put("acceptApplicationDelegate",acceptApplicationDelegate());
        beans.put("updateApplicationDelegate",updateApplicationDelegate());
        beans.put("updateDelegationDelegate",updateDelegationDelegate());

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
//        ProcessEngine processEngine = factoryBean.getObject();
//        Map<String, ProcessEngine> theMap = ProcessEngines.getProcessEngines();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        //这个是删除，注意用的是deployment_id_
//        //repositoryService.deleteDeployment("35001",true);
//
//        //这个是添加
//        repositoryService.createDeployment()
//                .addClasspathResource("processes/delegation.bpmn20.xml")
//                .deploy();

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
    public SaveApplicationDelegate saveApplicationDelegate(){
        return new SaveApplicationDelegate();
    }

    @Bean
    public AcceptApplicationDelegate acceptApplicationDelegate() {return new AcceptApplicationDelegate();}

    @Bean
    public UpdateApplicationDelegate updateApplicationDelegate(){return new UpdateApplicationDelegate();}

    @Bean
    public UpdateDelegationDelegate updateDelegationDelegate(){return new UpdateDelegationDelegate();}

    @Bean
    public DelegationApplicationTableMapper delegationApplicationTableMapper(){
        return new DelegationApplicationTableMapperImpl();
    }

    @Bean
    public DelegationFileListMapper delegationFileListMapper(){
        return new DelegationFileListMapperImpl();
    }

    @Bean
    public DelegationFunctionTableMapper delegationFunctionTableMapper(){
        return new DelegationFunctionTableMapperImpl();
    }
}

