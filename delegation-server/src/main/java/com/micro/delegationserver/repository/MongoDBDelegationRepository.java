package com.micro.delegationserver.repository;


import com.micro.delegationserver.model.Delegation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBDelegationRepository extends MongoRepository<Delegation,String> {

    @Query("{usrBelonged: ?0}")
    List<Delegation> findAllByUsrId(String usrId);

}