package com.micro.delegationserver.repository;

import com.micro.delegationserver.model.Delegation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation,String> {
}
