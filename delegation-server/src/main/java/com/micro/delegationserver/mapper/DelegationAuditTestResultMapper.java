package com.micro.delegationserver.mapper;

import com.micro.delegationserver.model.ClientRuntimeEnvironment;
import com.micro.delegationserver.model.DelegationAuditTestResult;
import com.micro.dto.ClientRuntimeEnvironmentDto;
import com.micro.dto.DelegationAuditTestResultDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface DelegationAuditTestResultMapper {
    public Collection<DelegationAuditTestResultDto> toDtos(Collection<DelegationAuditTestResult> delegationAuditTestResults);

    public Collection<DelegationAuditTestResult> toObjs(Collection<DelegationAuditTestResultDto> delegationAuditTestResultDtos);

    public DelegationAuditTestResult toObj(DelegationAuditTestResultDto delegationAuditTestResultDto);

    public DelegationAuditTestResultDto toDto(DelegationAuditTestResult delegationAuditTestResult);
}
