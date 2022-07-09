package com.micro.testserver.mapper;

import com.micro.dto.TestSchemeAuditTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SchemeEvaOpinion;
import com.micro.testserver.model.SchemeEvaPass;
import com.micro.testserver.model.SchemeEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface TestSchemeAuditTableMapper {
    public Collection<TestSchemeAuditTableDto> toDtos(Collection<SchemeEvaluationTable> objs);
    public Collection<SchemeEvaluationTable> toObjs(Collection<TestSchemeAuditTableDto> dtos);
    default public SchemeEvaluationTable toObj(TestSchemeAuditTableDto dto){
        if(dto==null){
            return null;
        }
        SchemeEvaluationTable obj=new SchemeEvaluationTable();

        obj.set确认意见(dto.get确认意见());
        obj.set测试类别(dto.get测试类别());
        obj.set软件名称(dto.get软件名称());
        obj.set版本号(dto.get版本号());
        obj.set项目编号(dto.get项目编号());

        //01234567
        obj.evaPasses[0]=new SchemeEvaPass();
        obj.evaPasses[0].pass=dto.getPass0();
        obj.evaPasses[0].failReason=dto.getFailReason0();

        obj.evaPasses[1]=new SchemeEvaPass();
        obj.evaPasses[1].pass=dto.getPass1();
        obj.evaPasses[1].failReason=dto.getFailReason1();

        obj.evaPasses[2]=new SchemeEvaPass();
        obj.evaPasses[2].pass=dto.getPass2();
        obj.evaPasses[2].failReason=dto.getFailReason2();

        obj.evaPasses[3]=new SchemeEvaPass();
        obj.evaPasses[3].pass=dto.getPass3();
        obj.evaPasses[3].failReason=dto.getFailReason3();

        obj.evaPasses[4]=new SchemeEvaPass();
        obj.evaPasses[4].pass=dto.getPass4();
        obj.evaPasses[4].failReason=dto.getFailReason4();

        obj.evaPasses[5]=new SchemeEvaPass();
        obj.evaPasses[5].pass=dto.getPass5();
        obj.evaPasses[5].failReason=dto.getFailReason5();

        obj.evaPasses[6]=new SchemeEvaPass();
        obj.evaPasses[6].pass=dto.getPass6();
        obj.evaPasses[6].failReason=dto.getFailReason6();

        obj.evaPasses[7]=new SchemeEvaPass();
        obj.evaPasses[7].pass=dto.getPass7();
        obj.evaPasses[7].failReason=dto.getFailReason7();
        //01234
        obj.evaOpinions[0]=new SchemeEvaOpinion();
        obj.evaOpinions[0].opinion=dto.getOpinion0();
        obj.evaOpinions[0].date=dto.getDate0();
        obj.evaOpinions[0].sign=dto.getSign0();

        obj.evaOpinions[1]=new SchemeEvaOpinion();
        obj.evaOpinions[1].opinion=dto.getOpinion1();
        obj.evaOpinions[1].date=dto.getDate1();
        obj.evaOpinions[1].sign=dto.getSign1();

        obj.evaOpinions[2]=new SchemeEvaOpinion();
        obj.evaOpinions[2].opinion=dto.getOpinion2();
        obj.evaOpinions[2].date=dto.getDate2();
        obj.evaOpinions[2].sign=dto.getSign2();

        obj.evaOpinions[3]=new SchemeEvaOpinion();
        obj.evaOpinions[3].opinion=dto.getOpinion3();
        obj.evaOpinions[3].date=dto.getDate3();
        obj.evaOpinions[3].sign=dto.getSign3();

        obj.evaOpinions[4]=new SchemeEvaOpinion();
        obj.evaOpinions[4].opinion=dto.getOpinion4();
        obj.evaOpinions[4].date=dto.getDate4();
        obj.evaOpinions[4].sign=dto.getSign4();
        return obj;
    }
    default public TestSchemeAuditTableDto toDto(SchemeEvaluationTable obj){
        if(obj==null){
            return null;
        }
        TestSchemeAuditTableDto dto=new TestSchemeAuditTableDto();

        dto.set确认意见(obj.get确认意见());
        dto.set测试类别(obj.get测试类别());
        dto.set软件名称(obj.get软件名称());
        dto.set版本号(obj.get版本号());
        dto.set项目编号(obj.get项目编号());

        //01234567
        dto.setPass0(obj.evaPasses[0].pass);
        dto.setFailReason0(obj.evaPasses[0].failReason);

        dto.setPass1(obj.evaPasses[1].pass);
        dto.setFailReason1(obj.evaPasses[1].failReason);

        dto.setPass2(obj.evaPasses[2].pass);
        dto.setFailReason2(obj.evaPasses[2].failReason);

        dto.setPass3(obj.evaPasses[3].pass);
        dto.setFailReason3(obj.evaPasses[3].failReason);

        dto.setPass4(obj.evaPasses[4].pass);
        dto.setFailReason4(obj.evaPasses[4].failReason);

        dto.setPass5(obj.evaPasses[5].pass);
        dto.setFailReason5(obj.evaPasses[5].failReason);

        dto.setPass6(obj.evaPasses[6].pass);
        dto.setFailReason6(obj.evaPasses[6].failReason);

        dto.setPass7(obj.evaPasses[7].pass);
        dto.setFailReason7(obj.evaPasses[7].failReason);
        //01234
        dto.setOpinion0(obj.evaOpinions[0].opinion);
        dto.setDate0(obj.evaOpinions[0].date);
        dto.sign0(obj.evaOpinions[0].sign);

        dto.setOpinion1(obj.evaOpinions[1].opinion);
        dto.setDate1(obj.evaOpinions[1].date);
        dto.sign1(obj.evaOpinions[1].sign);

        dto.setOpinion2(obj.evaOpinions[2].opinion);
        dto.setDate2(obj.evaOpinions[2].date);
        dto.sign2(obj.evaOpinions[2].sign);

        dto.setOpinion3(obj.evaOpinions[3].opinion);
        dto.setDate3(obj.evaOpinions[3].date);
        dto.sign3(obj.evaOpinions[3].sign);

        dto.setOpinion4(obj.evaOpinions[4].opinion);
        dto.setDate4(obj.evaOpinions[4].date);
        dto.sign4(obj.evaOpinions[4].sign);
        return dto;
    }
}
