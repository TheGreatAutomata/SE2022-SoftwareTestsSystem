package com.softwaretest.delegationmicroservice.repository;

import com.softwaretest.delegationmicroservice.model.Delegation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation,String> {

}
