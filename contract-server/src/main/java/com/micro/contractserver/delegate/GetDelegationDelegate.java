package com.micro.contractserver.delegate;

import com.micro.contractserver.model.Contract;
import com.micro.contractserver.model.ContractTableExist;
import com.micro.contractserver.service.NumberService;
import com.micro.dto.DelegationItemDto;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GetDelegationDelegate implements JavaDelegate {

    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String DELEGATION_URI = "http://delegation-server/delegation/";

    @Autowired
    private NumberService numberService;

    @Autowired
    public void setNumberService(NumberService numberService) {
        this.numberService = numberService;
    }

    /**
     * 获取对应委托的相关信息
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {

        // System.out.println("...Getting the existed delegation");

        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "xxx");
        headers.set("usrName", "xxx");
        headers.set("usrId", "xxx");
        headers.set("usrRole", "xxx");
        String delegationId = (String) delegateExecution.getVariable("delegationId");
        // HttpEntity<String> request = new HttpEntity<>("{name:string}", headers);
        // ResponseEntity<DelegationItemDto> result = restTemplate.getForEntity(DELEGATION_URI + delegationId, DelegationItemDto.class);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<DelegationItemDto> result = restTemplate.exchange(DELEGATION_URI + delegationId, HttpMethod.GET, requestEntity, DelegationItemDto.class);
        if(result.getStatusCode() != HttpStatus.OK)
        {
            throw new RuntimeException();
        }
        DelegationItemDto delegationItemDto = result.getBody();
        // System.out.println(delegationItemDto);

        Contract contract = (Contract)delegateExecution.getVariable("contract");
        // 加入已有信息
        contract.setUsrId(delegationItemDto.getUsrBelonged());
        contract.setProjectId(delegationItemDto.getProjectId());
        contract.getContractTable().getContractTableExist().setSoftwareName(delegationItemDto.getFunctionTable().get软件名称());
        //contract.getContractTable().getContractTableExist().setSoftwareName("djsakdjlkajhsfkjaklf");
        String softwareQualityCharacteristic = new String("");
        List<String> s = delegationItemDto.getApplicationTable().get需要测试的技术指标();
        for(int i = 0; i < s.size(); i++) {
            if(i == 0) {
                softwareQualityCharacteristic = softwareQualityCharacteristic + s.get(i);
            }
            else {
                softwareQualityCharacteristic = softwareQualityCharacteristic + "、" + s.get(i);
            }

        }
        contract.getContractTable().getContractTableExist().setSoftwareQualityCharacteristic(softwareQualityCharacteristic);
        contract.getContractTable().getContractTableExist().setPaymentInChinese(numberService.transFormation(delegationItemDto.getOfferTableUnion().get基本信息().get总计().toString()) + "元整");
        contract.getContractTable().getContractTableExist().setPaymentInArabic(delegationItemDto.getOfferTableUnion().get基本信息().get总计().toString());
        //contract.getContractTable().getContractTableExist().setPaymentInChinese(numberService.transFormation("163761822"));
        //contract.getContractTable().getContractTableExist().setPaymentInArabic("163761822");

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());

    }

}
