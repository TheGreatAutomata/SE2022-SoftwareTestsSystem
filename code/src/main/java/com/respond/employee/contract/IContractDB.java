package com.respond.employee.contract;
import java.util.List;

public interface IContractDB {
    public List<Contract> getContractsList(String usrName);
    public boolean addContract(String name, String createTime, String usrName, String employeeName, String body);
}
