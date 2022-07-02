package com.micro.testserver.mapper;

import com.micro.dto.TestSchemeDto;
import com.micro.dto.WorkEvaluationTableDto;
import com.micro.testserver.model.SoftwareTestScheme;
import com.micro.testserver.model.SoftwareWorkEvaluationTable;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareWorkEvaluationTableMapper {
    public Collection<WorkEvaluationTableDto> toDtos(Collection<SoftwareWorkEvaluationTable> objs);
    public Collection<SoftwareWorkEvaluationTable> toObjs(Collection<WorkEvaluationTableDto> dtos);
    default public SoftwareWorkEvaluationTable toObj(WorkEvaluationTableDto dto){
        if(dto==null){
            return null;
        }

        SoftwareWorkEvaluationTable obj=new SoftwareWorkEvaluationTable();

        obj.set市场部审核意见(dto.get市场部审核意见());
        obj.set主测人(dto.get主测人());
        obj.set实际完成时间(dto.get实际完成时间());
        obj.set版本号(dto.get版本号());
        obj.set申报单位(dto.get申报单位());
        obj.set起始时间(dto.get起始时间());
        obj.set软件名称(dto.get软件名称());
        obj.set预计完成时间(dto.get预计完成时间());

        //1-7,8,9 11-26
        obj.tableItems[1]=dto.getTableItem1();
        obj.tableItems[2]=dto.getTableItem2();
        obj.tableItems[3]=dto.getTableItem3();
        obj.tableItems[4]=dto.getTableItem4();
        obj.tableItems[5]=dto.getTableItem5();
        obj.tableItems[6]=dto.getTableItem6();
        obj.tableItems[7]=dto.getTableItem7();
        obj.tableItems[9]=dto.getTableItem9();
        obj.tableItems[11]=dto.getTableItem11();
        obj.tableItems[12]=dto.getTableItem12();
        obj.tableItems[13]=dto.getTableItem13();
        obj.tableItems[14]=dto.getTableItem14();
        obj.tableItems[15]=dto.getTableItem15();
        obj.tableItems[16]=dto.getTableItem16();
        obj.tableItems[17]=dto.getTableItem17();
        obj.tableItems[18]=dto.getTableItem18();
        obj.tableItems[19]=dto.getTableItem19();
        obj.tableItems[20]=dto.getTableItem20();
        obj.tableItems[21]=dto.getTableItem21();
        obj.tableItems[22]=dto.getTableItem22();
        obj.tableItems[23]=dto.getTableItem23();
        obj.tableItems[24]=dto.getTableItem24();
        obj.tableItems[25]=dto.getTableItem25();
        obj.tableItems[26]=dto.getTableItem26();

        return obj;
    }
    default public WorkEvaluationTableDto toDto(SoftwareWorkEvaluationTable obj){
        if(obj==null){
            return null;
        }

        WorkEvaluationTableDto dto=new WorkEvaluationTableDto();

        dto.set市场部审核意见(obj.get市场部审核意见());
        dto.set主测人(obj.get主测人());
        dto.set实际完成时间(obj.get实际完成时间());
        dto.set版本号(obj.get版本号());
        dto.set申报单位(obj.get申报单位());
        dto.set起始时间(obj.get起始时间());
        dto.set软件名称(obj.get软件名称());
        dto.set预计完成时间(obj.get预计完成时间());

        //1-7,8,9 11-26
        dto.setTableItem1(obj.tableItems[1]);
        dto.setTableItem1(obj.tableItems[2]);
        dto.setTableItem1(obj.tableItems[3]);
        dto.setTableItem1(obj.tableItems[4]);
        dto.setTableItem1(obj.tableItems[5]);
        dto.setTableItem1(obj.tableItems[6]);
        dto.setTableItem1(obj.tableItems[7]);
        dto.setTableItem1(obj.tableItems[9]);
        dto.setTableItem1(obj.tableItems[11]);
        dto.setTableItem1(obj.tableItems[12]);
        dto.setTableItem1(obj.tableItems[13]);
        dto.setTableItem1(obj.tableItems[14]);
        dto.setTableItem1(obj.tableItems[15]);
        dto.setTableItem1(obj.tableItems[16]);
        dto.setTableItem1(obj.tableItems[17]);
        dto.setTableItem1(obj.tableItems[18]);
        dto.setTableItem1(obj.tableItems[19]);
        dto.setTableItem1(obj.tableItems[20]);
        dto.setTableItem1(obj.tableItems[21]);
        dto.setTableItem1(obj.tableItems[22]);
        dto.setTableItem1(obj.tableItems[23]);
        dto.setTableItem1(obj.tableItems[24]);
        dto.setTableItem1(obj.tableItems[25]);
        dto.setTableItem1(obj.tableItems[26]);

        return dto;
    }
}
