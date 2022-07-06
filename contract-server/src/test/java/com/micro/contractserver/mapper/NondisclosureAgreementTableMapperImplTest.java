package com.micro.contractserver.mapper;

import com.micro.contractserver.model.NondisclosureAgreementTable;
import com.micro.dto.NondisclosureAgreementTableDto;
import com.mongodb.client.model.Collation;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class NondisclosureAgreementTableMapperImplTest {

    private NondisclosureAgreementTableMapperImpl nondisclosureAgreementTableMapper;

    private Collection<NondisclosureAgreementTableDto> nondisclosureAgreementTableDtos;

    private Collection<NondisclosureAgreementTable> nondisclosureAgreementTables;

    private NondisclosureAgreementTableDto nondisclosureAgreementTableDto = new NondisclosureAgreementTableDto();

    private NondisclosureAgreementTable nondisclosureAgreementTable = new NondisclosureAgreementTable();

    @BeforeEach
    void setUp() {

        nondisclosureAgreementTableMapper = new NondisclosureAgreementTableMapperImpl();

        nondisclosureAgreementTableDtos = new ArrayList<NondisclosureAgreementTableDto>(Arrays.asList(nondisclosureAgreementTableDto));
        nondisclosureAgreementTables = new ArrayList<NondisclosureAgreementTable>(Arrays.asList(nondisclosureAgreementTable));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toDtos() {

        Assert.assertEquals(nondisclosureAgreementTableDtos, nondisclosureAgreementTableMapper.toDtos(nondisclosureAgreementTables));
        Assert.assertEquals(null, nondisclosureAgreementTableMapper.toDtos(null));

    }

    @Test
    void toObjs() {

        Assert.assertEquals(nondisclosureAgreementTables, nondisclosureAgreementTableMapper.toObjs(nondisclosureAgreementTableDtos));
        Assert.assertEquals(null, nondisclosureAgreementTableMapper.toObjs(null));

    }

    @Test
    void toObj() {

        Assert.assertEquals(nondisclosureAgreementTable, nondisclosureAgreementTableMapper.toObj(nondisclosureAgreementTableDto));
        Assert.assertEquals(null, nondisclosureAgreementTableMapper.toObj(null));

    }

    @Test
    void toDto() {

        Assert.assertEquals(nondisclosureAgreementTableDto, nondisclosureAgreementTableMapper.toDto(nondisclosureAgreementTable));
        Assert.assertEquals(null, nondisclosureAgreementTableMapper.toDto(null));

    }
}