package com.micro.testserver.repository;

import com.micro.contractserver.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestContractRepo extends MongoRepository<Contract, String> {

    Optional<Contract> findByContractId(String contractId);

    default Optional<Contract> findByDelegationId(String delegationId) {
        List<Contract> contracts = findAll();
        for(Contract c : contracts) {
            if(c.getDelegationId().equals(delegationId)) {
                return Optional.of(c);
            }
        }
        return Optional.ofNullable(null);
    }

}