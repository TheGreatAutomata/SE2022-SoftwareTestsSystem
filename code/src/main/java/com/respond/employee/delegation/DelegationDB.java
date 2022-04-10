package com.respond.employee.delegation;

import java.util.ArrayList;
import java.util.List;

public class DelegationDB implements IDelegationDB {
    private final List<Delegation> _delegations = new ArrayList<>();

    public DelegationDB() {
        _delegations.add(new Delegation("SnakeWilder", "2020.2.2", DelegationState.Queuing));
        _delegations.add(new Delegation("Mouth", "BC 2020.2.2", DelegationState.Accepted));
        _delegations.add(new Delegation("XiuWater", "2023.2.2", DelegationState.Queuing));
        _delegations.add(new Delegation("Byte and Tight", "2009.2.2", DelegationState.Queuing));
        _delegations.add(new Delegation("Life Cycle", "200.2.2", DelegationState.Queuing));
        _delegations.add(new Delegation("N", "2077.2.2", DelegationState.Queuing));

    }

    public List<Delegation> getQueuingDelegations() {
        List<Delegation> ret = new ArrayList<>();
        for (Delegation d : _delegations
        ) {
            if (d.state == DelegationState.Queuing) {
                ret.add(d);
            }
        }
        return ret;
    }
}
