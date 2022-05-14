package com.micro.delegationserver.repository;

import com.micro.delegationserver.model.dao.CreatDelegationRequestDatabaseObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegationApplicationRepository extends JpaRepository<CreatDelegationRequestDatabaseObj,Long> {

}
