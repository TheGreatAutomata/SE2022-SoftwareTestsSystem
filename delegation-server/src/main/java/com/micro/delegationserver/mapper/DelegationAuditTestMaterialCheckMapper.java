package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.DelegationAuditTestMaterialCheck;
import com.micro.delegationserver.model.DelegationAuditTestResult;
import com.micro.dto.DelegationAuditTestMaterialCheckDto;
import com.micro.dto.DelegationAuditTestResultDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface DelegationAuditTestMaterialCheckMapper {
    public Collection<DelegationAuditTestMaterialCheckDto> toDtos(Collection<DelegationAuditTestMaterialCheck> delegationAuditTestMaterialChecks);

    public Collection<DelegationAuditTestMaterialCheck> toObjs(Collection<DelegationAuditTestMaterialCheckDto> delegationAuditTestMaterialCheckDtos);

    public DelegationAuditTestMaterialCheck toObj(DelegationAuditTestMaterialCheckDto delegationAuditTestMaterialCheckDto);

    public DelegationAuditTestMaterialCheckDto toDto(DelegationAuditTestMaterialCheck delegationAuditTestMaterialCheck);
}
