package com.micro.testserver.mapper;

import com.micro.dto.DocEvaluationTableDto;
import com.micro.dto.TestSchemeDto;
import com.micro.testserver.model.DocEvaResult;
import com.micro.testserver.model.SoftwareDocEvaluationTable;
import com.micro.testserver.model.SoftwareTestScheme;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface SoftwareDocEvaluationTableMapper {
    public Collection<DocEvaluationTableDto> toDtos(Collection<SoftwareDocEvaluationTable> objs);
    public Collection<SoftwareDocEvaluationTable> toObjs(Collection<DocEvaluationTableDto> dtos);
    default public SoftwareDocEvaluationTable toObj(DocEvaluationTableDto dto){
        if(dto==null){
            return null;
        }

        //1-14 16-35
        SoftwareDocEvaluationTable obj=new SoftwareDocEvaluationTable();

        obj.set检查人(dto.get检查人());
        obj.set版本号(dto.get版本号());
        obj.set评审人(dto.get评审人());
        obj.set评审完成时间(dto.get评审完成时间());
        obj.set软件名称(dto.get软件名称());

        obj.docEvaResults[1]=new DocEvaResult();
        obj.docEvaResults[1].result=dto.getResult1();
        obj.docEvaResults[1].description=dto.getDescription1();

        obj.docEvaResults[2]=new DocEvaResult();
        obj.docEvaResults[2].result=dto.getResult2();
        obj.docEvaResults[2].description=dto.getDescription2();

        obj.docEvaResults[3]=new DocEvaResult();
        obj.docEvaResults[3].result=dto.getResult3();
        obj.docEvaResults[3].description=dto.getDescription3();

        obj.docEvaResults[4]=new DocEvaResult();
        obj.docEvaResults[4].result=dto.getResult4();
        obj.docEvaResults[4].description=dto.getDescription4();

        obj.docEvaResults[5]=new DocEvaResult();
        obj.docEvaResults[5].result=dto.getResult5();
        obj.docEvaResults[5].description=dto.getDescription5();

        obj.docEvaResults[6]=new DocEvaResult();
        obj.docEvaResults[6].result=dto.getResult6();
        obj.docEvaResults[6].description=dto.getDescription6();

        obj.docEvaResults[7]=new DocEvaResult();
        obj.docEvaResults[7].result=dto.getResult7();
        obj.docEvaResults[7].description=dto.getDescription7();

        obj.docEvaResults[8]=new DocEvaResult();
        obj.docEvaResults[8].result=dto.getResult8();
        obj.docEvaResults[8].description=dto.getDescription8();

        obj.docEvaResults[9]=new DocEvaResult();
        obj.docEvaResults[9].result=dto.getResult9();
        obj.docEvaResults[9].description=dto.getDescription9();

        obj.docEvaResults[10]=new DocEvaResult();
        obj.docEvaResults[10].result=dto.getResult10();
        obj.docEvaResults[10].description=dto.getDescription10();

        obj.docEvaResults[11]=new DocEvaResult();
        obj.docEvaResults[11].result=dto.getResult11();
        obj.docEvaResults[11].description=dto.getDescription11();

        obj.docEvaResults[12]=new DocEvaResult();
        obj.docEvaResults[12].result=dto.getResult12();
        obj.docEvaResults[12].description=dto.getDescription12();

        obj.docEvaResults[13]=new DocEvaResult();
        obj.docEvaResults[13].result=dto.getResult13();
        obj.docEvaResults[13].description=dto.getDescription13();

        obj.docEvaResults[14]=new DocEvaResult();
        obj.docEvaResults[14].result=dto.getResult14();
        obj.docEvaResults[14].description=dto.getDescription14();

        obj.docEvaResults[16]=new DocEvaResult();
        obj.docEvaResults[16].result=dto.getResult16();
        obj.docEvaResults[16].description=dto.getDescription16();

        obj.docEvaResults[17]=new DocEvaResult();
        obj.docEvaResults[17].result=dto.getResult17();
        obj.docEvaResults[17].description=dto.getDescription17();

        obj.docEvaResults[18]=new DocEvaResult();
        obj.docEvaResults[18].result=dto.getResult18();
        obj.docEvaResults[18].description=dto.getDescription18();

        obj.docEvaResults[19]=new DocEvaResult();
        obj.docEvaResults[19].result=dto.getResult19();
        obj.docEvaResults[19].description=dto.getDescription19();

        obj.docEvaResults[20]=new DocEvaResult();
        obj.docEvaResults[20].result=dto.getResult20();
        obj.docEvaResults[20].description=dto.getDescription20();

        obj.docEvaResults[21]=new DocEvaResult();
        obj.docEvaResults[21].result=dto.getResult21();
        obj.docEvaResults[21].description=dto.getDescription21();

        obj.docEvaResults[22]=new DocEvaResult();
        obj.docEvaResults[22].result=dto.getResult22();
        obj.docEvaResults[22].description=dto.getDescription22();

        obj.docEvaResults[23]=new DocEvaResult();
        obj.docEvaResults[23].result=dto.getResult23();
        obj.docEvaResults[23].description=dto.getDescription23();

        obj.docEvaResults[24]=new DocEvaResult();
        obj.docEvaResults[24].result=dto.getResult24();
        obj.docEvaResults[24].description=dto.getDescription24();

        obj.docEvaResults[25]=new DocEvaResult();
        obj.docEvaResults[25].result=dto.getResult25();
        obj.docEvaResults[25].description=dto.getDescription25();

        obj.docEvaResults[26]=new DocEvaResult();
        obj.docEvaResults[26].result=dto.getResult26();
        obj.docEvaResults[26].description=dto.getDescription26();

        obj.docEvaResults[27]=new DocEvaResult();
        obj.docEvaResults[27].result=dto.getResult27();
        obj.docEvaResults[27].description=dto.getDescription27();

        obj.docEvaResults[28]=new DocEvaResult();
        obj.docEvaResults[28].result=dto.getResult28();
        obj.docEvaResults[28].description=dto.getDescription28();

        obj.docEvaResults[29]=new DocEvaResult();
        obj.docEvaResults[29].result=dto.getResult29();
        obj.docEvaResults[29].description=dto.getDescription29();

        obj.docEvaResults[30]=new DocEvaResult();
        obj.docEvaResults[30].result=dto.getResult30();
        obj.docEvaResults[30].description=dto.getDescription30();

        obj.docEvaResults[31]=new DocEvaResult();
        obj.docEvaResults[31].result=dto.getResult31();
        obj.docEvaResults[31].description=dto.getDescription31();

        obj.docEvaResults[32]=new DocEvaResult();
        obj.docEvaResults[32].result=dto.getResult32();
        obj.docEvaResults[32].description=dto.getDescription32();

        obj.docEvaResults[33]=new DocEvaResult();
        obj.docEvaResults[33].result=dto.getResult33();
        obj.docEvaResults[33].description=dto.getDescription33();

        obj.docEvaResults[34]=new DocEvaResult();
        obj.docEvaResults[34].result=dto.getResult34();
        obj.docEvaResults[34].description=dto.getDescription34();

        obj.docEvaResults[35]=new DocEvaResult();
        obj.docEvaResults[35].result=dto.getResult35();
        obj.docEvaResults[35].description=dto.getDescription35();

        return obj;
    }
    default public DocEvaluationTableDto toDto(SoftwareDocEvaluationTable obj){
        if(obj==null){
            return null;
        }
        DocEvaluationTableDto dto=new DocEvaluationTableDto();

        dto.set检查人(obj.get检查人());
        dto.set版本号(obj.get版本号());
        dto.set评审人(obj.get评审人());
        dto.set评审完成时间(obj.get评审完成时间());
        dto.set软件名称(obj.get软件名称());


        dto.setResult1(obj.docEvaResults[1].getResult());
        dto.setDescription1(obj.docEvaResults[1].getDescription());

        dto.setResult2(obj.docEvaResults[2].getResult());
        dto.setDescription2(obj.docEvaResults[2].getDescription());

        dto.setResult3(obj.docEvaResults[3].getResult());
        dto.setDescription3(obj.docEvaResults[3].getDescription());

        dto.setResult4(obj.docEvaResults[4].getResult());
        dto.setDescription4(obj.docEvaResults[4].getDescription());

        dto.setResult5(obj.docEvaResults[5].getResult());
        dto.setDescription5(obj.docEvaResults[5].getDescription());

        dto.setResult6(obj.docEvaResults[6].getResult());
        dto.setDescription6(obj.docEvaResults[6].getDescription());

        dto.setResult7(obj.docEvaResults[7].getResult());
        dto.setDescription7(obj.docEvaResults[7].getDescription());

        dto.setResult8(obj.docEvaResults[8].getResult());
        dto.setDescription8(obj.docEvaResults[8].getDescription());

        dto.setResult9(obj.docEvaResults[9].getResult());
        dto.setDescription9(obj.docEvaResults[9].getDescription());

        dto.setResult10(obj.docEvaResults[10].getResult());
        dto.setDescription10(obj.docEvaResults[10].getDescription());

        dto.setResult11(obj.docEvaResults[11].getResult());
        dto.setDescription11(obj.docEvaResults[11].getDescription());

        dto.setResult12(obj.docEvaResults[12].getResult());
        dto.setDescription12(obj.docEvaResults[12].getDescription());

        dto.setResult13(obj.docEvaResults[13].getResult());
        dto.setDescription13(obj.docEvaResults[13].getDescription());

        dto.setResult14(obj.docEvaResults[14].getResult());
        dto.setDescription14(obj.docEvaResults[14].getDescription());

        dto.setResult16(obj.docEvaResults[16].getResult());
        dto.setDescription16(obj.docEvaResults[16].getDescription());

        dto.setResult17(obj.docEvaResults[17].getResult());
        dto.setDescription17(obj.docEvaResults[17].getDescription());

        dto.setResult18(obj.docEvaResults[18].getResult());
        dto.setDescription18(obj.docEvaResults[18].getDescription());

        dto.setResult19(obj.docEvaResults[19].getResult());
        dto.setDescription19(obj.docEvaResults[19].getDescription());

        dto.setResult20(obj.docEvaResults[20].getResult());
        dto.setDescription20(obj.docEvaResults[20].getDescription());

        dto.setResult21(obj.docEvaResults[21].getResult());
        dto.setDescription21(obj.docEvaResults[21].getDescription());

        dto.setResult22(obj.docEvaResults[22].getResult());
        dto.setDescription22(obj.docEvaResults[22].getDescription());

        dto.setResult23(obj.docEvaResults[23].getResult());
        dto.setDescription23(obj.docEvaResults[23].getDescription());

        dto.setResult24(obj.docEvaResults[24].getResult());
        dto.setDescription24(obj.docEvaResults[24].getDescription());

        dto.setResult25(obj.docEvaResults[25].getResult());
        dto.setDescription25(obj.docEvaResults[25].getDescription());

        dto.setResult26(obj.docEvaResults[26].getResult());
        dto.setDescription26(obj.docEvaResults[26].getDescription());

        dto.setResult27(obj.docEvaResults[27].getResult());
        dto.setDescription27(obj.docEvaResults[27].getDescription());

        dto.setResult28(obj.docEvaResults[28].getResult());
        dto.setDescription28(obj.docEvaResults[28].getDescription());

        dto.setResult29(obj.docEvaResults[29].getResult());
        dto.setDescription29(obj.docEvaResults[29].getDescription());

        dto.setResult30(obj.docEvaResults[30].getResult());
        dto.setDescription30(obj.docEvaResults[30].getDescription());

        dto.setResult31(obj.docEvaResults[31].getResult());
        dto.setDescription31(obj.docEvaResults[31].getDescription());

        dto.setResult32(obj.docEvaResults[32].getResult());
        dto.setDescription32(obj.docEvaResults[32].getDescription());

        dto.setResult33(obj.docEvaResults[33].getResult());
        dto.setDescription33(obj.docEvaResults[33].getDescription());

        dto.setResult34(obj.docEvaResults[34].getResult());
        dto.setDescription34(obj.docEvaResults[34].getDescription());

        dto.setResult35(obj.docEvaResults[35].getResult());
        dto.setDescription35(obj.docEvaResults[35].getDescription());

        return dto;
    }
}
