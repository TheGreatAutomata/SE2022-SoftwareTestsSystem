package com.micro.delegationserver.repository;


import com.micro.delegationserver.model.Delegation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoDBDelegationRepository extends MongoRepository<Delegation,String> {

}