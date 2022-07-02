package com.micro.testserver.repository;

import com.micro.testserver.model.SoftwareTest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftwareTestRepository extends MongoRepository<SoftwareTest,String> {
    @Query("{delegation_id: ?0}")
    SoftwareTest findByDelegationId(String delegationId);

    @Query("{usrId: ?0}")
    List<SoftwareTest> findByUsrId(String usrId);
}
