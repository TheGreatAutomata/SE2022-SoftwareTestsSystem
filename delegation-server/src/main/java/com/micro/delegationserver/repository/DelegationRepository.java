package com.micro.delegationserver.repository;


import com.micro.delegationserver.model.Delegation;

<<<<<<< HEAD
import com.micro.delegationserver.model.DelegationState;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
=======
import com.micro.commonserver.model.DelegationState;
>>>>>>> 045d88bebda9adae538f7e2de4388f6c1d8291bd

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DelegationRepository extends MongoRepository<Delegation,String> {

    @Query("{usrBelonged: ?0}")
    List<Delegation> findAllByUsrId(String usrId);
    @Query("{state: ?0}")
    List<Delegation> findAllByState(DelegationState delegationState);

    default Optional<Delegation> findByDelegationId(String id){
        List<Delegation> delegations=findAll();
        for (Delegation d:delegations
             ) {
            if(d.getDelegationId().equals(id)){
                return Optional.of(d);
            }
        }
        return Optional.ofNullable(null);
    }




}