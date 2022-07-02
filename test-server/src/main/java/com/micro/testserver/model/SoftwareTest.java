package com.micro.testserver.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Data
@Document("software_test")
public class SoftwareTest implements Serializable {
    @MongoId
    String id;

    String delegation_id;

    SoftwareTestScheme scheme;

    SchemeEvaluationTable schemeEvaluationTable;

    SoftwareTestState state;

    //测试文档

    //available now
    SoftwareTestCase testCase;
    SoftwareTestRecord testRecord;
    SoftwareBugList bugList;
    SoftwareDocEvaluationTable docEvaluationTable;

    //todo: fill it
    SoftwareTestReport testReport;

    SoftwareReportEvaluationTable reportEvaluationTable;
    SoftwareWorkEvaluationTable workEvaluationTable;
    SoftwareFormalTestReport formalTestReport;

    public SoftwareTest() {
        id = new ObjectId().toString();
        state = SoftwareTestState.TEST_SCHEME;
    }


    public static void main(String[] args) {
        Set<String> reportEntries = new HashSet<>();
        SoftwareTest softwareTest = new SoftwareTest();
        SoftwareTestReport softwareTestReport = softwareTest.getTestReport();

        Field[] SoftwareTestReportfields = SoftwareTestReport.class.getFields();
        for (var f : SoftwareTestReportfields) {
            reportEntries.add(f.getName());
        }
        for (var f : SoftwareTestCase.class.getFields()) {
            if(reportEntries.contains(f.getName()))
                System.out.println("SoftwareTestCase" + f.getName());
        }

        for(var f : SoftwareTestRecord.class.getFields()){
            if(reportEntries.contains(f.getName()))
                System.out.println("SoftwareTestRecord" + f.getName());
        }

        for(var f : SoftwareBugList.class.getFields()){
            if(reportEntries.contains(f.getName()))
                System.out.println("SoftwareBugList" + f.getName());
        }

        for(var f : SoftwareDocEvaluationTable.class.getFields()){
            if(reportEntries.contains(f.getName()))
                System.out.println("SoftwareDocEvaluationTable" + f.getName());
        }
    }
}
