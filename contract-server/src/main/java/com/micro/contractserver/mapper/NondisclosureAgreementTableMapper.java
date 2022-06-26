package com.micro.contractserver.mapper;

import com.micro.contractserver.model.NondisclosureAgreementTable;
import com.micro.dto.NondisclosureAgreementTableDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface NondisclosureAgreementTableMapper {

    public Collection<NondisclosureAgreementTableDto> toDtos(Collection<NondisclosureAgreementTable> nondisclosureAgreementTables);

    public Collection<NondisclosureAgreementTable> toObjs(Collection<NondisclosureAgreementTableDto> nondisclosureAgreementTableDtos);

    public NondisclosureAgreementTable toObj(NondisclosureAgreementTableDto nondisclosureAgreementTableDto);

    public NondisclosureAgreementTableDto toDto(NondisclosureAgreementTable nondisclosureAgreementTable);

}