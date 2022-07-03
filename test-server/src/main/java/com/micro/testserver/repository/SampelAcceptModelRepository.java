package com.micro.testserver.repository;

import com.micro.commonserver.model.SampleAcceptModel;
import com.micro.contractserver.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SampelAcceptModelRepository extends MongoRepository<SampleAcceptModel,String> {
    @Query("{delegationId: ?0}")
    public void findByDelegationId(String delegationId);
}
