package com.micro.testserver.delegate;

import com.micro.testserver.model.SoftwareTest;
import com.micro.testserver.model.SoftwareTestReport;
import com.micro.testserver.model.SoftwareTestState;
import com.micro.testserver.repository.SoftwareTestRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 生成Latex格式的测试报告
 */
public class GenerateLatexReportDelegate implements JavaDelegate {
    @Autowired
    SoftwareTestRepository softwareTestRepository;


    @Override
    public void execute(DelegateExecution delegateExecution) {
        SoftwareTest softwareTest = delegateExecution.getVariable("softwareTest", SoftwareTest.class);
        if(softwareTest==null){
            //？！
            return;
        }




        System.out.println(softwareTestRepository);
        softwareTest.setState(SoftwareTestState.TEST_DOC_TEST_REPORT_EVALUATION_TABLE);
        softwareTestRepository.save(softwareTest);
        delegateExecution.setVariable("softwareTest",softwareTest);
    }
}
