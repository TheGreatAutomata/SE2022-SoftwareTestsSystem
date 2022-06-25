package com.micro.sampleserver.repository;
import com.micro.sampleserver.model.Sample;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface MongoDBDelegationRepository extends MongoRepository<Sample,String> {

    List<Sample> findAllByUsrId(String usrId);
    default Optional<Sample> findByDelegationId(String id){
        List<Sample> delegations=findAll();
        for (Sample d:delegations
        ) {
            if(d.getDelegationId().equals(id)){
                return Optional.of(d);
            }
        }
        return Optional.ofNullable(null);
    }
}
