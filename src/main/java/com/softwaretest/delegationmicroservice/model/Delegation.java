package com.softwaretest.delegationmicroservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Delegation {
    @Id
    String id;

}
