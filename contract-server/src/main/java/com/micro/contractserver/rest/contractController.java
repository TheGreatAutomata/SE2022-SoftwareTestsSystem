package com.micro.contractserver.rest;

import com.micro.api.ContractApi;
import com.micro.contractserver.mapper.*;
import com.micro.contractserver.model.*;
import com.micro.contractserver.repository.MongoDBContractRepository;
import com.micro.contractserver.service.ContractService;
import com.micro.dto.*;
import lombok.SneakyThrows;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class contractController implements ContractApi {

    @Autowired
    public ContractService contractService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    NormalResponseMapper normalResponseMapper;

    @Autowired
    ContractMapper contractMapper;

    @Autowired
    PerformanceTermPartyAMapper performanceTermPartyAMapper;

    @Autowired
    PerformanceTermPartyBMapper performanceTermPartyBMapper;

    @Autowired
    PerformanceTermPartyAResponseMapper performanceTermPartyAResponseMapper;

    @Autowired
    ContractTableMapper contractTableMapper;

    @Autowired
    ContractTablePartyAMapper contractTablePartyAMapper;

    @Autowired
    ContractTablePartyBMapper contractTablePartyBMapper;

    @Autowired
    NondisclosureAgreementTableMapper nondisclosureAgreementTableMapper;

    @Autowired
    ContractFileMapper contractFileMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoDBContractRepository contractRepository;

    @Override
    public ResponseEntity<NormalResponseDto> draftPerformanceTermPartyB(String usrName, String usrId, String usrRole, String delegationId, PerformanceTermPartyBDto performanceTermPartyBDto) {

        System.out.println("Party B draft the performance term...");

        Contract contract = contractService.constructFromPerformanceTermPartyBDto(delegationId, performanceTermPartyBDto);
        contract.setContractState(ContractState.PARTYB_CREATE_CONTRACT_AND_DRAFT_PERFORMANCE_TERM);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("contract", contract);
        variables.put("contractId", contract.getContractId());
        variables.put("delegationId", contract.getDelegationId());
        runtimeService.startProcessInstanceByKey("contract", variables);

        // save contract in delegate/saveContractDelegate
        // contractRepository.save(contract); // TODO: updateservice 判断合同状态

        return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse(contract.contractId)), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PerformanceTermPartyAResponseDto> getPerformanceTermPartyA(String usrName, String usrId, String usrRole, String delegationId) {

        System.out.println("Party A get the performance term...");

        Optional<Contract> contract_op = contractRepository.findByDelegationId(delegationId);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            PerformanceTermPartyAResponse performanceTermPartyAResponse = new PerformanceTermPartyAResponse(contract.getContractId(), new PerformanceTermPartyB(contract.getContractTable().getContractTableExist().getProjectName(), contract.getContractTable().getContractTableExist().getPartyBName1(), contract.getContractTable().getContractTableExist().getPerformanceTerm(), contract.getContractTable().getContractTableExist().getRectificationTimes(), contract.getContractTable().getContractTableExist().getRectificationTerm()));
            PerformanceTermPartyAResponseDto performanceTermPartyAResponseDto = performanceTermPartyAResponseMapper.toDto(performanceTermPartyAResponse);

            return new ResponseEntity<PerformanceTermPartyAResponseDto>(performanceTermPartyAResponseDto, HttpStatus.OK);
        }
        else {
            System.out.println("!!! contract not found !!!");

            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<NormalResponseDto> replyPerformanceTermPartyA(String usrName, String usrId, String usrRole, String id, PerformanceTermPartyADto performanceTermPartyADto) {

        System.out.println("Party A reply the performance term...");

        Task task = taskService.createTaskQuery().taskName("ReplyPerformanceTermPartyA").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            // performance term not found
            System.out.println("!!! performance term " + id + " not found !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("performance not found")), HttpStatus.BAD_REQUEST);
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();

            System.out.println(contract);

            contract.setUsrId(usrId);
            contract.setUsrName(usrName);
            contract.setPerformanceTermState(performanceTermPartyAMapper.toObj(performanceTermPartyADto).get态度());
            contract.setPerformanceTermSuggestion(performanceTermPartyAMapper.toObj(performanceTermPartyADto).get意见());

            String performanceTermState = contract.getPerformanceTermState();
            switch (performanceTermState) {
                case "接受": {
                    contract.setContractState(ContractState.PARTYA_ACCEPT_PERFORMANCE_TERM);
                    break;
                }
                case "申请再议": {
                    contract.setContractState(ContractState.PARTYA_REJECT_PERFORMANCE_TERM_FOR_MODIFICATION);
                    break;
                }
                case "不接受": {
                    contract.setContractState(ContractState.PARTYA_REJECT_PERFORMANCE_TERM_TO_END);
                    break;
                }
            }

            System.out.println(contract);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            // save contract in delegate/saveContractDelegate
            // contractRepository.save(contract); // TODO: updateservice 判断合同状态

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("reply successfully")), HttpStatus.OK);

        } else {
            System.out.println("!!! contract not found !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<PerformanceTermPartyADto> getPerformanceTermReplyPartyB(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Party b get the performance term...");

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();

            PerformanceTermPartyADto performanceTermPartyADto = performanceTermPartyAMapper.toDto(new PerformanceTermPartyA(contract.getPerformanceTermState(), contract.getPerformanceTermSuggestion()));

            return new ResponseEntity<PerformanceTermPartyADto>(performanceTermPartyADto, HttpStatus.OK);
        }
        else {
            System.out.println("!!! contract not found !!!");

            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<NormalResponseDto> updatePerformanceTermPartyB(String usrName, String usrId, String usrRole, String id, PerformanceTermPartyBDto performanceTermPartyBDto) {

        System.out.println("Party B update the performance term...");

        Task task = taskService.createTaskQuery().taskName("UpdatePerformanceTermPartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            // performance term not found
            System.out.println("!!! performance term " + id + " not found !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("performance not found")), HttpStatus.BAD_REQUEST);
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            PerformanceTermPartyB performanceTermPartyB = performanceTermPartyBMapper.toObj(performanceTermPartyBDto);
            contract.getContractTable().getContractTableExist().setPartyBName1(performanceTermPartyB.get受托方乙方());
            contract.getContractTable().getContractTableExist().setPerformanceTerm(performanceTermPartyB.get合同履行期限());
            contract.getContractTable().getContractTableExist().setRectificationTimes(performanceTermPartyB.get整改限制次数());
            contract.getContractTable().getContractTableExist().setRectificationTerm(performanceTermPartyB.get一次整改限制的天数());
            contract.setContractState(ContractState.PARTYB_UPDATE_PERFORMANCE_TERM);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            // save contract in delegate/saveContractDelegate
            // contractRepository.save(contract); // TODO: updateservice 判断合同状态

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("update successfully")), HttpStatus.OK);

        } else {
            System.out.println("!!! contract not found !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<NormalResponseDto> addContractTablePartyB(String usrName, String usrId, String usrRole, String id, ContractTablePartyBDto contractTablePartyBDto) {

        System.out.println("Party B add the contract table...");

        Task task = taskService.createTaskQuery().taskName("AddContractTablePartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            // contract not found
            System.out.println("!!! contract " + id + " not found when add party B's contract table !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            contract.getContractTable().setContractTablePartyB(contractTablePartyBMapper.toObj(contractTablePartyBDto));
            contract.setContractState(ContractState.PARTYB_ADD_CONTRACT_TABLE);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            // save contract in delegate/saveContractDelegate
            // contractRepository.save(contract); // TODO: updateservice 判断合同状态

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("add successfully")), HttpStatus.OK);

        } else {
            System.out.println("!!! contract not found !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ContractTablePartyBDto> getContractTablePartyA(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Party A get the contract table...");

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractTablePartyBDto contractTablePartyBDto = contractTablePartyBMapper.toDto(contract.getContractTable().getContractTablePartyB());

            return new ResponseEntity<ContractTablePartyBDto>(contractTablePartyBDto, HttpStatus.OK);
        }
        else {
            System.out.println("!!! contract not found when get party A's contract table !!!");

            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<NormalResponseDto> addContractTablePartyA(String usrName, String usrId, String usrRole, String id, ContractTablePartyADto contractTablePartyADto) {

        System.out.println("Party A add the contract table...");

        Task task = taskService.createTaskQuery().taskName("AddContractTablePartyA").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("!!! contract " + id + " not found when add party A's contract table !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractTablePartyA contractTablePartyA = contractTablePartyAMapper.toObj(contractTablePartyADto);
            contract.getContractTable().setContractTablePartyA(contractTablePartyA);
            contract.getContractTable().getContractTableExist().setPartyAName1(contractTablePartyA.get单位全称());
            contract.getContractTable().getContractTableExist().setPartyAName2(contractTablePartyA.get单位全称());
            contract.getContractTable().getContractTableExist().setPartyAName3(contractTablePartyA.get单位全称());

            contract.setNondisclosureAgreementTable(new NondisclosureAgreementTable(contract.getContractTable().getContractTableExist().getPartyAName1(), contract.getContractTable().getContractTableExist().getPartyBName1(), contract.getContractTable().getContractTableExist().getProjectName()));

            contract.setContractState(ContractState.PARTYA_ADD_CONTRACT_TABLE);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            // save contract in delegate/saveContractDelegate
            // contractRepository.save(contract); // TODO: updateservice 判断合同状态

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("add successfully")), HttpStatus.OK);

        } else {
            System.out.println("!!! contract not found when add party A's contract table !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<SingleFileDto> downloadUnsignedContractTablePartyB(String usrName, String usrId, String usrRole, String id) {

        System.out.println("download unsigned contract table file...");

        minioFileItem fileItem = contractService.getUnsignedContractTableFile(id);
        if(fileItem == null) {
            System.out.println("!!! unsigned contract file not found !!!");

            return ResponseEntity.status(400).build();
        }
        else {

            return new ResponseEntity<SingleFileDto>(contractFileMapper.toSingleFileDto(fileItem), HttpStatus.OK);

        }

    }

    @Override
    public ResponseEntity<SingleFileDto> downloadUnsignedNondisclosureAgreementTablePartyB(String usrName, String usrId, String usrRole, String id) {

        System.out.println("download unsigned nondisclosure agreement file...");

        minioFileItem fileItem = contractService.getUnsignedNondisclosureAgreementTableFile(id);
        if(fileItem == null) {
            System.out.println("!!! unsigend nondisclosure agreement file not found !!!");

            return ResponseEntity.status(400).build();
        }
        else {

            return new ResponseEntity<SingleFileDto>(contractFileMapper.toSingleFileDto(fileItem), HttpStatus.OK);

        }

    }

    @SneakyThrows
    @Override
    public ResponseEntity<NormalResponseDto> uploadContractPartyB(String usrName, String usrId, String usrRole, String id, MultipartFile contractTableFile, MultipartFile nondisclosureAgreementFile) {

        Task task = taskService.createTaskQuery().taskName("UploadSignedContractPartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            // contract not found
            System.out.println("!!! contract " + id + " not found when upload signed contract !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);

        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            contract.setContractState(ContractState.PARTYB_UPLOAD_SIGNED_CONTRACT);
            contractRepository.save(contract);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());

            if (contractTableFile != null) {
                variables.put("Contract_complete_" + id, contractTableFile.getBytes());
                variables.put("Contract_complete_" + id + "Name", contractTableFile.getOriginalFilename());
            } else {
                variables.put("Contract_complete_" + id, contractTableFile);
                variables.put("Contract_complete_" + id + "Name", "None");
            }
            if (nondisclosureAgreementFile != null) {
                variables.put("NDA_complete_" + id, nondisclosureAgreementFile.getBytes());
                variables.put("NDA_complete_" + id + "Name", nondisclosureAgreementFile.getOriginalFilename());
            } else {
                variables.put("NDA_complete_" + id, nondisclosureAgreementFile);
                variables.put("NDA_complete_" + id + "Name", "None");
            }

            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("upload successfully")), HttpStatus.OK);

        }
        else {
            System.out.println("!!! contract not found when upload sigend contract files !!!");

            return new ResponseEntity<NormalResponseDto>(normalResponseMapper.toDto(new NormalResponse("contract not found")), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<SingleFileDto> downloadSignedContractTable(String usrName, String usrId, String usrRole, String id) {

        System.out.println("download signed contract table file...");

        minioFileItem fileItem = contractService.getSignedContractTableFile(id);
        if(fileItem == null) {
            System.out.println("!!! signed contract file not found !!!");

            return ResponseEntity.status(400).build();
        }
        else {

            return new ResponseEntity<SingleFileDto>(contractFileMapper.toSingleFileDto(fileItem), HttpStatus.OK);

        }

    }

    @Override
    public ResponseEntity<SingleFileDto> downloadSignedNondisclosureAgreementTable(String usrName, String usrId, String usrRole, String id) {

        System.out.println("download signed nondisclosure agreement file...");

        minioFileItem fileItem = contractService.getSignedNondisclosureAgreementTableFile(id);
        if(fileItem == null) {
            System.out.println("!!! signed nondisclosure agreement file not found !!!");

            return ResponseEntity.status(400).build();
        }
        else {

            return new ResponseEntity<SingleFileDto>(contractFileMapper.toSingleFileDto(fileItem), HttpStatus.OK);

        }

    }

    @Override
    public ResponseEntity<ContractDto> getContractByContractId(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Get contract by contract id " + id + " ...");

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractDto contractDto = contractMapper.toDto(contract);

            return new ResponseEntity<ContractDto>(contractDto, HttpStatus.OK);
        }
        else {
            System.out.println("!!! contract not found when get the whole contract by contract id " + id + " !!!");

            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<ContractDto> getContractByDelegationId(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Get contract by delegation id " + id + " ...");

        Optional<Contract> contract_op = contractRepository.findByDelegationId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractDto contractDto = contractMapper.toDto(contract);

            return new ResponseEntity<ContractDto>(contractDto, HttpStatus.OK);
        }
        else {
            System.out.println("!!! contract not found when get the whole contract by delegation id " + id + " !!!");

            return ResponseEntity.status(400).build();
        }

    }

}
