package com.respond.employee.contract;
import java.util.ArrayList;
import java.util.List;

public class ContractDB implements IContractDB {
    private final List<Contract> _contracts=new ArrayList<>();
    public ContractDB(){
        _contracts.add(new Contract("Contract1", "2020.2.2", "usr1", "employee1", "ContractBody1"));
    }

    public List<Contract> getContractsList(String usrName)
    {
        List<Contract> ret=new ArrayList<>();
        for (Contract c:_contracts) 
        {
            if(c.usrName==usrName){
                ret.add(c);
            }
        }
        return ret;
   }

   public boolean addContract(String name, String createTime, String usrName, String employeeName, String body)
   {
        _contracts.add(new Contract(name, createTime, usrName, employeeName, body));
        return true;
   }
}