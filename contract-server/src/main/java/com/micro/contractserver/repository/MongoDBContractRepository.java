package com.micro.contractserver.repository;

import com.micro.contractserver.model.Contract;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongoDBContractRepository extends MongoRepository<Contract, String> {

    /*default Optional<Contract> findByContractId(String contractId) {
        List<Contract> contracts = findAll();
        for(Contract c : contracts) {
            if(c.getContractId().equals(contractId)) {
                return Optional.of(c);
            }
        }
        return Optional.ofNullable(null);
    }*/
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