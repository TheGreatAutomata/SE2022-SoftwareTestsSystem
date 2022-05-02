package com.bezkoder.springjwt.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;

@Repository
@Mapper
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
