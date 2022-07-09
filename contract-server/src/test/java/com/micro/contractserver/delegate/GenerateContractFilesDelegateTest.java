package com.micro.contractserver.delegate;

import com.micro.commonserver.service.MinioService;
import com.micro.contractserver.model.*;
import com.micro.contractserver.service.ContractService;
import net.bytebuddy.asm.Advice;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GenerateContractFilesDelegateTest {

    @Autowired
    private GenerateContractFilesDelegate generateContractFilesDelegate;

    @Autowired
    MinioService minioService;

    @MockBean
    private ContractService contractService;

    @MockBean
    private DelegateExecution delegateExecution;

    private Contract contract;


    @BeforeEach
    void setUp() {

        contract = new Contract();
        contract.setContractId("contractid");
        contract.setUsrId("usrId");
        contract.setUsrName("usrName");
        contract.setDelegationId("delegationId");
        contract.setProjectId("projectId");
        contract.setContractState(ContractState.PARTYB_ADD_CONTRACT_TABLE);
        contract.setPerformanceTermState("同意");
        contract.setPerformanceTermSuggestion("ok");
        contract.setContractTable(new ContractTable());
        contract.setNondisclosureAgreementTable(new NondisclosureAgreementTable());
        contract.setSignedContractTableFile(new minioFileItem());
        contract.setSignedNondisclosureAgreementTableFile(new minioFileItem());

        contract.getContractTable().setContractTableExist(new ContractTableExist());
        contract.getContractTable().setContractTablePartyA(new ContractTablePartyA());
        contract.getContractTable().setContractTablePartyB(new ContractTablePartyB());

        contract.getNondisclosureAgreementTable().setProjectName("projectName");
        contract.getNondisclosureAgreementTable().setPartyAName("partyAName");
        contract.getNondisclosureAgreementTable().setPartyBName("partyBName");

        contract.getContractTable().getContractTableExist().setProjectName("projectName");
        contract.getContractTable().getContractTableExist().setPartyAName1("partyAName");
        contract.getContractTable().getContractTableExist().setPartyBName1("partyBName");
        contract.getContractTable().getContractTableExist().setPartyAName2("partyAName");
        contract.getContractTable().getContractTableExist().setPartyBName2("partyBName");
        contract.getContractTable().getContractTableExist().setSoftwareName("softwareName");
        contract.getContractTable().getContractTableExist().setSoftwareQualityCharacteristic("softwareQualityCharacteristic");
        contract.getContractTable().getContractTableExist().setPaymentInChinese("一百");
        contract.getContractTable().getContractTableExist().setPaymentInArabic("100");
        contract.getContractTable().getContractTableExist().setPerformanceTerm("180");
        contract.getContractTable().getContractTableExist().setRectificationTimes("3");
        contract.getContractTable().getContractTableExist().setRectificationTerm("10");
        contract.getContractTable().getContractTableExist().setPartyAName3("partyAName");
        contract.getContractTable().getContractTableExist().setPartyBName3("partyBName");

        contract.getContractTable().getContractTablePartyA().set单位全称("单位全称");
        contract.getContractTable().getContractTablePartyA().set授权代表("授权代表");
        contract.getContractTable().getContractTablePartyA().set联系人("联系人");
        contract.getContractTable().getContractTablePartyA().set通讯地址("通讯地址");
        contract.getContractTable().getContractTablePartyA().set电话("电话");
        contract.getContractTable().getContractTablePartyA().set传真("传真");
        contract.getContractTable().getContractTablePartyA().set开户银行("开户银行");
        contract.getContractTable().getContractTablePartyA().set账号("账号");
        contract.getContractTable().getContractTablePartyA().set邮编("邮编");

        contract.getContractTable().getContractTablePartyB().set授权代表("授权代表");
        contract.getContractTable().getContractTablePartyB().set联系人("联系人");
        contract.getContractTable().getContractTablePartyB().set通讯地址("通讯地址");
        contract.getContractTable().getContractTablePartyB().set邮编("邮编");
        contract.getContractTable().getContractTablePartyB().set电话("电话");
        contract.getContractTable().getContractTablePartyB().set传真("传真");
        contract.getContractTable().getContractTablePartyB().set开户银行("开户银行");
        contract.getContractTable().getContractTablePartyB().set户名("户名");
        contract.getContractTable().getContractTablePartyB().set账号("账号");

        String str = contract.getContractTable().getContractTablePartyA().get单位全称();
        str = contract.getContractTable().getContractTablePartyA().get授权代表();
        str = contract.getContractTable().getContractTablePartyA().get联系人();
        str = contract.getContractTable().getContractTablePartyA().get通讯地址();
        str = contract.getContractTable().getContractTablePartyA().get电话();
        str = contract.getContractTable().getContractTablePartyA().get传真();
        str = contract.getContractTable().getContractTablePartyA().get开户银行();
        str = contract.getContractTable().getContractTablePartyA().get账号();
        str = contract.getContractTable().getContractTablePartyA().get邮编();
        str = contract.getContractTable().getContractTablePartyB().get授权代表();
        str = contract.getContractTable().getContractTablePartyB().get联系人();
        str = contract.getContractTable().getContractTablePartyB().get通讯地址();
        str = contract.getContractTable().getContractTablePartyB().get邮编();
        str = contract.getContractTable().getContractTablePartyB().get电话();
        str = contract.getContractTable().getContractTablePartyB().get传真();
        str = contract.getContractTable().getContractTablePartyB().get开户银行();
        str = contract.getContractTable().getContractTablePartyB().get户名();
        str = contract.getContractTable().getContractTablePartyB().get账号();
        str = contract.getNondisclosureAgreementTable().getPartyAName();
        str = contract.getNondisclosureAgreementTable().getPartyBName();
        str = contract.getNondisclosureAgreementTable().getProjectName();

    }

    @AfterEach
    void tearDown() throws Exception {

        generateContractFilesDelegate.deleteUnsignedContractFiles("contractid");

        if(minioService.hasBucket("contractid")) {
            minioService.removeBucket("contractid");
        }

    }

    @Test
    void execute() throws IOException {

        File tempFile = new File("");
        String projectPath = tempFile.getCanonicalPath();
        System.out.println(projectPath);

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        generateContractFilesDelegate.execute(delegateExecution);

    }

    @Test
    void generateContractTableFile() {
    }

    @Test
    void generateNondisclosureAgreementTableFile() {
    }

    @Test
    void saveUnsignedContractFiles() {
    }

    @Test
    void deleteUnsignedContractFiles() {
    }
}