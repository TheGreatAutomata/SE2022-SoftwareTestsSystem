package com.micro.testserver.mapper;

import com.micro.dto.TestReportEvaluationTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.SoftwareReportEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareReportEvaluationMapper {
    public Collection<TestReportEvaluationTableDto> toDtos(Collection<SoftwareReportEvaluationTable> objs);
    public Collection<SoftwareReportEvaluationTable> toObjs(Collection<TestReportEvaluationTableDto> dtos);
    default public SoftwareReportEvaluationTable toObj(TestReportEvaluationTableDto dto){
        if(dto==null){
            return null;
        }
        SoftwareReportEvaluationTable obj=new SoftwareReportEvaluationTable();

        obj.set确认意见(dto.get确认意见());
        obj.set委托单位(dto.get委托单位());
        obj.set日期(dto.get日期());
        obj.set检查人(dto.get检查人());
        obj.set软件名称(dto.get软件名称());

        //0-13
        obj.tableItems[0]=dto.getTableItem0();
        obj.tableItems[1]=dto.getTableItem1();
        obj.tableItems[2]=dto.getTableItem2();
        obj.tableItems[3]=dto.getTableItem3();
        obj.tableItems[4]=dto.getTableItem4();
        obj.tableItems[5]=dto.getTableItem5();
        obj.tableItems[6]=dto.getTableItem6();
        obj.tableItems[7]=dto.getTableItem7();
        obj.tableItems[8]=dto.getTableItem8();
        obj.tableItems[9]=dto.getTableItem9();
        obj.tableItems[10]=dto.getTableItem10();
        obj.tableItems[11]=dto.getTableItem11();
        obj.tableItems[12]=dto.getTableItem12();
        obj.tableItems[13]=dto.getTableItem13();

        return obj;
    }
    default public TestReportEvaluationTableDto toDto(SoftwareReportEvaluationTable obj){
        if(obj==null){
            return null;
        }
        TestReportEvaluationTableDto dto=new TestReportEvaluationTableDto();

        dto.set确认意见(obj.get确认意见());
        dto.set委托单位(obj.get委托单位());
        dto.set日期(obj.get日期());
        dto.set检查人(obj.get检查人());
        dto.set软件名称(obj.get软件名称());

        dto.setTableItem0(obj.tableItems[0]);
        dto.setTableItem1(obj.tableItems[1]);
        dto.setTableItem2(obj.tableItems[2]);
        dto.setTableItem3(obj.tableItems[3]);
        dto.setTableItem4(obj.tableItems[4]);
        dto.setTableItem5(obj.tableItems[5]);
        dto.setTableItem6(obj.tableItems[6]);
        dto.setTableItem7(obj.tableItems[7]);
        dto.setTableItem8(obj.tableItems[8]);
        dto.setTableItem9(obj.tableItems[9]);
        dto.setTableItem10(obj.tableItems[10]);
        dto.setTableItem11(obj.tableItems[11]);
        dto.setTableItem12(obj.tableItems[12]);
        dto.setTableItem13(obj.tableItems[13]);

        return dto;
    }
}
