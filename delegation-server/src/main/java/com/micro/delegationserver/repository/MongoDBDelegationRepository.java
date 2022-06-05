package com.micro.delegationserver.repository;


import com.micro.delegationserver.model.Delegation;
import com.micro.delegationserver.model.DelegationState;
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
public interface MongoDBDelegationRepository extends MongoRepository<Delegation,String> {

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