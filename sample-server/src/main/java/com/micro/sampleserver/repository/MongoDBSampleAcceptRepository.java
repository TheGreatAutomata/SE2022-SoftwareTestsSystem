package com.micro.sampleserver.repository;

import com.micro.commonserver.model.SampleAcceptModel;
import com.micro.sampleserver.model.Sample;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoDBSampleAcceptRepository extends MongoRepository<SampleAcceptModel,String> {

    List<Sample> findAllByDelegationId(String usrId);
}
