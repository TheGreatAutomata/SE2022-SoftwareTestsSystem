package com.micro.testserver.repository;

import com.micro.testserver.model.SoftwareTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareTestRepository extends MongoRepository<SoftwareTest,String> {
    @Query("{delegationId: ?0}")
    SoftwareTest findByDelegationId(String delegationId);
}
