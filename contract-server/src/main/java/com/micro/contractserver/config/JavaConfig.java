package com.micro.contractserver.config;

import com.micro.contractserver.delegate.*;
import com.micro.contractserver.mapper.*;

import com.micro.contractserver.service.NumberService;
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
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration(){
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource());
        springProcessEngineConfiguration.setTransactionManager(transactionManager());
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        Resource[] resources = new Resource[1];
        resources[0] = new ClassPathResource("processes/contract.bpmn20.xml");
        springProcessEngineConfiguration.setDeploymentResources(resources);
        springProcessEngineConfiguration.setDeploymentMode("single-resource");

        HashMap<Object,Object> beans=new HashMap<>();

        beans.put("setDelegationContractIdDelegate", setDelegationContractIdDelegate());
        beans.put("setDelegationStateDelegate", setDelegationStateDelegate());
        beans.put("getDelegationDelegate", getDelegationDelegate());
        beans.put("saveContractDelegate", saveContractDelegate());
        beans.put("generateContractFilesDelegate", generateContractFilesDelegate());
        beans.put("saveContractFilesDelegate", saveContractFilesDelegate());
        beans.put("deleteContractDelegate", deleteContractDelegate());
        beans.put("setTestPreparationDelegate", setTestPreparationDelegate());

        springProcessEngineConfiguration.setBeans(beans);

        //springProcessEngineConfiguration.setMailServerHost("smtp.qq.com");
        //springProcessEngineConfiguration.setMailServerPort(465);
        //springProcessEngineConfiguration.setMailServerDefaultFrom("2379594184@qq.com");
        //springProcessEngineConfiguration.setMailServerUsername("2379594184@qq.com");
        //springProcessEngineConfiguration.setMailServerPassword("qiijzfyfucxadhha");
        //springProcessEngineConfiguration.setMailServerUseSSL(true);

        return springProcessEngineConfiguration;
    }

    @Bean
    public ProcessEngine processEngine() throws Exception {
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
    public RuntimeService runtimeService() throws Exception {
        return processEngine().getRuntimeService();
    }

    @Bean
    public TaskService taskService() throws Exception {
        return processEngine().getTaskService();
    }

    @Bean
    public RepositoryService repositoryService() throws Exception {
        return processEngine().getRepositoryService();
    }

    @Bean
    public NumberService NumberService() {
        return new NumberService();
    }

    @Bean
    public SetDelegationContractIdDelegate setDelegationContractIdDelegate() {
        return new SetDelegationContractIdDelegate();
    }

    @Bean
    public GetDelegationDelegate getDelegationDelegate() {
        return new GetDelegationDelegate();
    }

    @Bean
    public SetDelegationStateDelegate setDelegationStateDelegate() {
        return new SetDelegationStateDelegate();
    }

    @Bean
    public GenerateContractFilesDelegate generateContractFilesDelegate() {
        return new GenerateContractFilesDelegate();
    }

    @Bean
    public SaveContractDelegate saveContractDelegate() {
        return new SaveContractDelegate();
    }

    @Bean
    public DeleteContractDelegate deleteContractDelegate() {
        return new DeleteContractDelegate();
    }

    @Bean
    public SaveContractFilesDelegate saveContractFilesDelegate() {
        return new SaveContractFilesDelegate();
    }

    @Bean
    public SetTestPreparationDelegate setTestPreparationDelegate() {
        return new SetTestPreparationDelegate();
    }

    @Bean
    public NormalResponseMapper normalResponseMapper() {
        return new NormalResponseMapperImpl();
    }

    @Bean
    public ContractMapper contractMapper() {
        return new ContractMapperImpl();
    }

    @Bean
    public PerformanceTermPartyAMapper performanceTermPartyAMapper() {
        return new PerformanceTermPartyAMapperImpl();
    }

    @Bean
    public PerformanceTermPartyBMapper performanceTermPartyBMapper() {
        return new PerformanceTermPartyBMapperImpl();
    }

    @Bean
    public PerformanceTermPartyAResponseMapper performanceTermPartyAResponseMapper() {
        return new PerformanceTermPartyAResponseMapperImpl();
    }

    @Bean
    public ContractTableMapper contractTableMapper() {
        return new ContractTableMapperImpl();
    }

    @Bean
    public ContractTablePartyAMapper contractTablePartyAMapper() {
        return new ContractTablePartyAMapperImpl();
    }

    @Bean
    public ContractTablePartyBMapper contractTablePartyBMapper() {
        return new ContractTablePartyBMapperImpl();
    }

    @Bean
    public NondisclosureAgreementTableMapper nondisclosureAgreementTableMapper() {
        return new NondisclosureAgreementTableMapperImpl();
    }

}
