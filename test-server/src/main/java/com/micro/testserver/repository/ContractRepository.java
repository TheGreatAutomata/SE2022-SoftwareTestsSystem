package com.micro.testserver.repository;

import com.micro.contractserver.model.Contract;
import com.micro.delegationserver.model.Delegation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ContractRepository extends MongoRepository<Contract,String> {
    @Query("{delegationId: ?0}")
    public void findByDelegationId(String delegationId);
}
