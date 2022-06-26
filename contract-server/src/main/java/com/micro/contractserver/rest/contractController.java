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
    public ResponseEntity<String> draftPerformanceTermPartyB(String usrName, String usrId, String usrRole, String delegationId, PerformanceTermPartyBDto performanceTermPartyBDto) {

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

        return ResponseEntity.status(200).body(contract.contractId);

    }

    @Override
    public ResponseEntity<PerformanceTermPartyAResponseDto> getPerformanceTermPartyA(String usrName, String usrId, String usrRole, String delegationId) {

        System.out.println("Party A get the performance term...");

        Task task = taskService.createTaskQuery().taskName("GetPerformanceTermPartyA").processVariableValueEquals("delegationId", delegationId).singleResult();
        if(task == null) {
            //performance term not found
            System.out.println("performance term not found...");
            return ResponseEntity.status(400).build();
        }

        Optional<Contract> contract_op = contractRepository.findByDelegationId(delegationId);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            PerformanceTermPartyAResponse performanceTermPartyAResponse = new PerformanceTermPartyAResponse(contract.getContractId(), new PerformanceTermPartyB(contract.getContractTable().getContractTableExist().getProjectName(), contract.getContractTable().getContractTableExist().getPartyBName1(), contract.getContractTable().getContractTableExist().getPerformanceTerm(), contract.getContractTable().getContractTableExist().getRectificationTimes(), contract.getContractTable().getContractTableExist().getRectificationTerm()));
            PerformanceTermPartyAResponseDto performanceTermPartyAResponseDto = performanceTermPartyAResponseMapper.toDto(performanceTermPartyAResponse);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            return new ResponseEntity<PerformanceTermPartyAResponseDto>(performanceTermPartyAResponseDto, HttpStatus.OK);
        }
        else {
            System.out.println("contract not found...");
            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<String> replyPerformanceTermPartyA(String usrName, String usrId, String usrRole, String id, PerformanceTermPartyADto performanceTermPartyADto) {

        System.out.println("Party A reply the performance term...");

        Task task = taskService.createTaskQuery().taskName("ReplyPerformanceTermPartyA").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //performance term not found
            System.out.println("performance term not found...");
            return ResponseEntity.status(400).body("performance term not found");
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

            return new ResponseEntity<String>("reply successfully" ,HttpStatus.OK);

        } else {
            System.out.println("contract not found...");
            return ResponseEntity.status(400).body("contract not found");
        }
    }

    @Override
    public ResponseEntity<PerformanceTermPartyADto> getPerformanceTermReplyPartyB(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Party b get the performance term...");

        Task task = taskService.createTaskQuery().taskName("GetPerformanceTermReplyPartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //performance term not found
            System.out.println("performance term not found...");
            return ResponseEntity.status(400).build();
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();

            PerformanceTermPartyADto performanceTermPartyADto = performanceTermPartyAMapper.toDto(new PerformanceTermPartyA(contract.getPerformanceTermState(), contract.getPerformanceTermSuggestion()));

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());

            String performanceTermState = contract.getPerformanceTermState();

            if(performanceTermState != null) {
                switch (performanceTermState) {
                    case "接受": {
                        variables.put("state", 1);
                        break;
                    }
                    case "申请再议": {
                        variables.put("state", 2);
                        break;
                    }
                    case "不接受": {
                        variables.put("state", 3);
                        break;
                    }
                    default: {
                        System.out.println("State is illegal...");
                        break;
                    }
                }
            }

            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            return new ResponseEntity<PerformanceTermPartyADto>(performanceTermPartyADto, HttpStatus.OK);
        }
        else {
            System.out.println("contract not found...");
            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<String> updatePerformanceTermPartyB(String usrName, String usrId, String usrRole, String id, PerformanceTermPartyBDto performanceTermPartyBDto) {

        System.out.println("Party B update the performance term...");

        Task task = taskService.createTaskQuery().taskName("UpdatePerformanceTermPartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //performance term not found
            System.out.println("performance term not found...");
            return ResponseEntity.status(400).body("performance term not found");
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

            return new ResponseEntity<String>("update successfully" ,HttpStatus.OK);

        } else {
            System.out.println("contract not found...");
            return ResponseEntity.status(400).body("contract not found");
        }
    }

    @Override
    public ResponseEntity<String> addContractTablePartyB(String usrName, String usrId, String usrRole, String id, ContractTablePartyBDto contractTablePartyBDto) {

        System.out.println("Party B add the contract table...");

        Task task = taskService.createTaskQuery().taskName("AddContractTablePartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found1...");
            return ResponseEntity.status(400).body("contract not found");
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

            return new ResponseEntity<String>("add successfully", HttpStatus.OK);

        } else {
            System.out.println("contract not found2...");
            return ResponseEntity.status(400).body("contract not found");
        }
    }

    @Override
    public ResponseEntity<ContractTablePartyBDto> getContractTablePartyA(String usrName, String usrId, String usrRole, String id) {

        System.out.println("Party A get the contract table...");

        Task task = taskService.createTaskQuery().taskName("GetContractTablePartyA").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found1...");
            return ResponseEntity.status(400).build();
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractTablePartyBDto contractTablePartyBDto = contractTablePartyBMapper.toDto(contract.getContractTable().getContractTablePartyB());

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            return new ResponseEntity<ContractTablePartyBDto>(contractTablePartyBDto, HttpStatus.OK);
        }
        else {
            System.out.println("contract not found2...");
            return ResponseEntity.status(400).build();
        }

    }

    @Override
    public ResponseEntity<String> addContractTablePartyA(String usrName, String usrId, String usrRole, String id, ContractTablePartyADto contractTablePartyADto) {

        System.out.println("Party A add the contract table...");

        Task task = taskService.createTaskQuery().taskName("AddContractTablePartyA").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found1...");
            return ResponseEntity.status(400).body("contract not found");
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            ContractTablePartyA contractTablePartyA = contractTablePartyAMapper.toObj(contractTablePartyADto);
            contract.getContractTable().setContractTablePartyA(contractTablePartyA);
            contract.getContractTable().getContractTableExist().setPartyAName1(contractTablePartyA.get单位全称());
            contract.getContractTable().getContractTableExist().setPartyAName2(contractTablePartyA.get单位全称());
            contract.getContractTable().getContractTableExist().setPartyAName3(contractTablePartyA.get单位全称());
            contract.setContractState(ContractState.PARTYA_ADD_CONTRACT_TABLE);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());

            // save contract in delegate/saveContractDelegate
            // contractRepository.save(contract); // TODO: updateservice 判断合同状态

            return new ResponseEntity<String>("add successfully" ,HttpStatus.OK);

        } else {
            System.out.println("contract not found2...");
            return ResponseEntity.status(400).body("contract not found");
        }
    }


    @Override
    public ResponseEntity<SingleFileDto> downloadContractTablePartyB(String usrName, String usrId, String usrRole, String id) {

        Task task = taskService.createTaskQuery().taskName("DownloadUnsignedContractTablePartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found...");
            return ResponseEntity.status(400).build();
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            contract.setContractState(ContractState.PARTYB_DOWNLOAD_UNSIGNED_CONTRACT_TABLE);
            contractRepository.save(contract);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());
        }

        minioFileItem fileItem = contractService.getContractTableFile(id);
        if(fileItem == null) {
            System.out.println("contract file not found...");
            return ResponseEntity.status(400).build();
        }
        else {
            return ResponseEntity.ok(contractFileMapper.toSingleFileDto(fileItem));
        }

    }

    @Override
    public ResponseEntity<SingleFileDto> downloadNondisclosureAgreementTablePartyB(String usrName, String usrId, String usrRole, String id) {

        Task task = taskService.createTaskQuery().taskName("DownloadUnsignedNondisclosureAgreementTablePartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found...");
            return ResponseEntity.status(400).build();
        }

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()) {
            Contract contract = contract_op.get();
            contract.setContractState(ContractState.PARTYB_DOWNLOAD_UNSIGNED_NONDISCLOSURE_AGREEMENT_TABLE);
            contractRepository.save(contract);

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("contract", contract);
            variables.put("contractId", contract.getContractId());
            runtimeService.setVariables(task.getExecutionId(), variables);
            taskService.complete(task.getId());
        }

        minioFileItem fileItem = contractService.getNondisclosureAgreementTableFile(id);
        if(fileItem == null) {
            System.out.println("nondisclosure agreement file not found...");
            return ResponseEntity.status(400).build();
        }
        else {
            return ResponseEntity.ok(contractFileMapper.toSingleFileDto(fileItem));
        }

    }

    @SneakyThrows
    @Override
    public ResponseEntity<String> uploadContractPartyB(String usrName, String usrId, String usrRole, String id, MultipartFile contractTableFile, MultipartFile nondisclosureAgreementFile) {

        Task task = taskService.createTaskQuery().taskName("UploadSignedContractPartyB").processVariableValueEquals("contractId", id).singleResult();
        if(task == null) {
            //contract not found
            System.out.println("contract not found...");
            return ResponseEntity.status(400).body("contract not found");
        }

        Map<String, Object> variables = new HashMap<String, Object>();
        //String delegationId = "delega"+id;
        variables.put("contractId", id);
        //List<MultipartFile> filesList = Lists.newArrayList(file1, file2, file3, file4);

        if(contractTableFile != null)
        {
            variables.put("Contract_complete_" + id, contractTableFile.getBytes());
            variables.put("Contract_complete_" + id + "Name", contractTableFile.getOriginalFilename());
        }
        else
        {
            variables.put("Contract_complete_" + id, contractTableFile);
            variables.put("Contract_complete_" + id + "Name", "None");
        }
        if(nondisclosureAgreementFile != null)
        {
            variables.put("NDA_complete_" + id, nondisclosureAgreementFile.getBytes());
            variables.put("NDA_complete_" + id + "Name", nondisclosureAgreementFile.getOriginalFilename());
        }
        else
        {
            variables.put("NDA_complete_" + id, nondisclosureAgreementFile);
            variables.put("NDA_complete_" + id + "Name", "None");
        }
        //variables.put("installationManual", installationManual.getBytes());
        //variables.put("operationManual", operationManual.getBytes());
        //variables.put("maintenanceManual", maintenanceManual.getBytes());
        //variables.put("file2", file2);
        //variables.put("file3", file3);
        //variables.put("file4", file4);

        Optional<Contract> contract_op = contractRepository.findByContractId(id);
        if(contract_op.isPresent()){
            Contract contract = contract_op.get();
            contract.setContractState(ContractState.PARTYB_UPLOAD_SIGNED_CONTRACT);
            contractRepository.save(contract);
        }

        taskService.complete(task.getId(), variables);

//        if(delegationService.creatFile(id, "file1", file1) && delegationService.creatFile(id, "file2", file2) && delegationService.creatFile(id, "file3", file3) && delegationService.creatFile(id, "file4", file4))
//        {
        return ResponseEntity.status(200).body("upload successfully");
//        }

    }

}
