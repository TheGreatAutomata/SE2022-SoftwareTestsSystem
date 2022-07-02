package com.micro.testserver.model;

import com.micro.contractserver.model.Contract;
import com.micro.delegationserver.model.User;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Document("software_test")
public class SoftwareTest implements Serializable {
    @MongoId
    String id;

    String delegation_id;

    String projectId;

    String usrId;

    String usrName;

    SoftwareTestScheme scheme;

    SchemeEvaluationTable schemeEvaluationTable;
    SoftwareTestState state;

    Contract contract;

    //测试文档
    SoftwareTestCase testCase;
    SoftwareTestRecord testRecord;
    SoftwareBugList bugList;
    SoftwareDocEvaluationTable docEvaluationTable;
    SoftwareTestReport testReport;
    SoftwareReportEvaluationTable reportEvaluationTable;
    SoftwareWorkEvaluationTable workEvaluationTable;
    SoftwareFormalTestReport formalTestReport;

    public SoftwareTest(){
        id=new ObjectId().toString();
        state=SoftwareTestState.TEST_SCHEME;
    }
}
