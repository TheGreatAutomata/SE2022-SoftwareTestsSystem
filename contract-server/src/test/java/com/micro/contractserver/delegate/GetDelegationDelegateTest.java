package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractTable;
import com.micro.contractserver.model.ContractTableExist;
import com.micro.contractserver.service.NumberService;
import com.micro.dto.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetDelegationDelegateTest {

    @Autowired
    private GetDelegationDelegate getDelegationDelegate;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private NumberService numberService;

    @MockBean
    private DelegateExecution delegateExecution;

    private HttpEntity<Void> requestEntity;

    private String DELEGATION_URI = "http://delegation-server/delegation/";

    @BeforeEach
    void setUp() {

        when(delegateExecution.getVariable("delegationId"))
                .thenReturn("delegationId");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "xxx");
        headers.set("usrName", "xxx");
        headers.set("usrId", "xxx");
        headers.set("usrRole", "xxx");
        requestEntity = new HttpEntity<>(headers);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setRestTemplate() {
    }

    @Test
    void setNumberService() {
    }

    @Test
    void execute() {

        Contract contract = new Contract();
        contract.setContractTable(new ContractTable());
        contract.getContractTable().setContractTableExist(new ContractTableExist());

        when(delegateExecution.getVariable("contract"))
                .thenReturn(contract);

        DelegationItemDto delegationItemDto = new DelegationItemDto();
        delegationItemDto.setUsrBelonged("usrId");
        delegationItemDto.setProjectId("projectId");
        DelegationFunctionTableDto delegationFunctionTableDto = new DelegationFunctionTableDto();
        delegationFunctionTableDto.set软件名称("软件名称");
        delegationItemDto.setFunctionTable(delegationFunctionTableDto);
        DelegationApplicationTableDto delegationApplicationTableDto = new DelegationApplicationTableDto();
        delegationItemDto.setApplicationTable(delegationApplicationTableDto);
        delegationApplicationTableDto.set需要测试的技术指标(new ArrayList<>(Arrays.asList("需要测试的技术指标1", "需要测试的技术指标2")));
        OfferTableDto offerTableDto = new OfferTableDto();
        offerTableDto.set总计(100);
        OfferTableUnionDto offerTableUnionDto = new OfferTableUnionDto();
        offerTableUnionDto.set基本信息(offerTableDto);
        delegationItemDto.setOfferTableUnion(offerTableUnionDto);

        when(numberService.transFormation("100"))
                .thenReturn("一百");

        when(restTemplate.exchange(DELEGATION_URI + "delegationId", HttpMethod.GET, requestEntity, DelegationItemDto.class))
                .thenReturn(ResponseEntity.status(200).body(delegationItemDto));

        getDelegationDelegate.execute(delegateExecution);

        verify(restTemplate, times(1)).exchange(DELEGATION_URI + "delegationId", HttpMethod.GET, requestEntity, DelegationItemDto.class);

    }
}