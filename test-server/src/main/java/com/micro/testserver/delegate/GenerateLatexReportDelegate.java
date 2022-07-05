package com.micro.testserver.delegate;

import com.micro.testserver.model.*;
import com.micro.testserver.repository.SoftwareTestRepository;
import com.micro.commonserver.service.MinioService;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.entity.ContentType;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

/**
 * 生成Latex格式的测试报告
 */
public class GenerateLatexReportDelegate implements JavaDelegate {
    @Autowired
    SoftwareTestRepository softwareTestRepository;

    @Autowired
    MinioService minioService;

    public static void main(String args[]) {

        String str1 = new String("");

        String[] str2 = str1.split("T")[0].split("-");

    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

        SoftwareTest softwareTest = delegateExecution.getVariable("softwareTest", SoftwareTest.class);
        if(softwareTest == null){

            System.out.println("!!! get software test failed !!!");

            return;
        }

        generateReportFile(softwareTest, softwareTest.getProjectId());

        saveReportFile(softwareTest.getProjectId());

        // deleteReportFile(softwareTest.getProjectId());

        softwareTestRepository.save(softwareTest);
        delegateExecution.setVariable("softwareTest",softwareTest);
    }

    @SneakyThrows
    public void generateReportFile(SoftwareTest softwareTest, String projectId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            // 设置输入输出路径
            String filename = "Report_" + projectId;
            String inputPath = "template/";
            String outputPath = "test-server/src/main/resources/generate/";

            // 设置velocity的资源加载器
            Properties prop = new Properties();
            prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            // 初始化velocity引擎
            Velocity.init(prop);

            // 创建velocity容器
            VelocityContext context = new VelocityContext();

            // velocity容器变量数据准备
            SoftwareTestReport report = softwareTest.getTestReport();
            String[] reportTime = report.get报告日期().split("T")[0].split("-");
            String[] sampleTime = report.get来样日期().split("T")[0].split("-");
            String[] testStartTime = report.get测试开始时间().split("T")[0].split("-");
            String[] testEndTime = report.get测试结束时间().split("T")[0].split("-");

            // velocity容器变量数据填充
            context.put("testYear", reportTime[0]);
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1());
            context.put("reportID1", report.get报告编号());
            context.put("reportID2", report.get报告编号());
            context.put("softwareName", report.get软件名称());
            context.put("versionNumber1", report.get版本号());
            context.put("partyAName1", report.get总委托单位());
            context.put("testCategory1", report.get测试类别());
            context.put("reportTimeYear", reportTime[0]);
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", ""));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", ""));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1());
            context.put("partyAName2", report.get委托单位());
            context.put("projectID", projectId);
            context.put("sampleName", report.get样品名称());
            context.put("versionNumber2", report.get版本型号());
            context.put("sampleTimeYear", sampleTime[0]);
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", ""));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", ""));
            context.put("testCategory2", report.get测试类型());
            context.put("testStartYear", testStartTime[0]);
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", ""));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", ""));
            context.put("testEndYear", testEndTime[0]);
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", ""));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", ""));
            context.put("testBasis1", report.get总测试依据());
            context.put("sampleList", report.get样品清单());
            context.put("testConclusion", report.get测试结论());
            context.put("partyAPhoneNumber", report.get电话());
            context.put("partyATaxNumber", report.get传真());
            context.put("partyAAddress", report.get地址());
            context.put("partyAPostCode", report.get邮编());
            context.put("partyALiaison", report.get联系人());
            context.put("partyAEmail", report.getEmail());
            context.put("partyBAddress", report.get测试单位单位地址());
            context.put("partyBPostCode", report.get测试单位邮政编码());
            context.put("partyBPhoneNumber", report.get测试单位电话());
            context.put("partyBTaxNumber", report.get测试单位传真());
            context.put("partyBWebsite", report.get测试单位网址());
            context.put("partyBEmail", report.get测试单位Email());
            context.put("hardwareItems", report.get硬件环境());
            context.put("softwareItems", report.get软件环境());
            context.put("networkEnvironment", report.get网络环境());
            context.put("testBasis2", report.get测试依据());
            context.put("referenceMaterial", report.get参考资料());
            context.put("functionalityTestItems", report.get功能性测试());
            context.put("efficiencyTestItems", report.get效率测试());
            context.put("portabilityTestItems", report.get可移植性测试());
            context.put("accessibilityTestItems", report.get易用性测试());
            context.put("reliabilityTestItems", report.get可靠性测试());
            context.put("maintainabilityTestItems", report.get可维护性测试());
            context.put("testExecutionRecord", report.get测试执行记录());

            // 加载velocity模板文件
            Template template = Velocity.getTemplate(inputPath + "Report.vm", "utf-8");

            // 合并数据到模板
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            template.merge(context, fw);

            // 释放资源
            fw.close();

            // tex文件转为pdf文件
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex");
            File file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                Thread.sleep(100);
            }
            process = runtime.exec("mv " + outputPath + filename + ".pdf " + outputPath + filename + "_old.pdf");
            file = new File(outputPath + filename + "_old.pdf");
            while (!file.exists()) {
                Thread.sleep(100);
            }
            process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex"); // 须执行两次，否则无法获得总页码
            file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                Thread.sleep(100);
            }

            // 删除中间生成文件
            process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux " + outputPath + filename + ".log " + outputPath + "texput.log");

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            // 设置输入输出路径
            String filename = "Report_" + projectId;
            String inputPath = "template/";
            String outputPath = "velocity/generate/";

            // 设置velocity的资源加载器
            Properties properties = new Properties();
            properties.setProperty("resource.loader", "class");
            properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty("jar.resource.loader.path", "jar:file:/root/deployment/contract-server-0.0.1-SNAPSHOT.jar");
            properties.put("input.encoding", "UTF-8");
            properties.put("output.encoding", "UTF-8");

            // 初始化velocity引擎
            VelocityEngine engine = new VelocityEngine();
            engine.init(properties);

            // 创建velocity容器
            VelocityContext context = new VelocityContext();

            // velocity容器变量数据准备
            SoftwareTestReport report = softwareTest.getTestReport();
            String[] reportTime = report.get报告日期().split("T")[0].split("-");
            String[] sampleTime = report.get来样日期().split("T")[0].split("-");
            String[] testStartTime = report.get测试开始时间().split("T")[0].split("-");
            String[] testEndTime = report.get测试结束时间().split("T")[0].split("-");

            // velocity容器变量数据填充
            context.put("testYear", reportTime[0]);
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1());
            context.put("reportID1", report.get报告编号());
            context.put("reportID2", report.get报告编号());
            context.put("softwareName", report.get软件名称());
            context.put("versionNumber1", report.get版本号());
            context.put("partyAName1", report.get总委托单位());
            context.put("testCategory1", report.get测试类别());
            context.put("reportTimeYear", reportTime[0]);
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", ""));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", ""));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1());
            context.put("partyAName2", report.get委托单位());
            context.put("projectID", projectId);
            context.put("sampleName", report.get样品名称());
            context.put("versionNumber2", report.get版本型号());
            context.put("sampleTimeYear", sampleTime[0]);
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", ""));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", ""));
            context.put("testCategory2", report.get测试类型());
            context.put("testStartYear", testStartTime[0]);
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", ""));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", ""));
            context.put("testEndYear", testEndTime[0]);
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", ""));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", ""));
            context.put("sampleStatus", report.get样品状态());
            context.put("testBasis1", report.get总测试依据());
            context.put("sampleList", report.get样品清单());
            context.put("testConclusion", report.get测试结论());
            context.put("partyAPhoneNumber", report.get电话());
            context.put("partyATaxNumber", report.get传真());
            context.put("partyAAddress", report.get地址());
            context.put("partyAPostCode", report.get邮编());
            context.put("partyALiaison", report.get联系人());
            context.put("partyAEmail", report.getEmail());
            context.put("partyBAddress", report.get测试单位单位地址());
            context.put("partyBPostCode", report.get测试单位邮政编码());
            context.put("partyBPhoneNumber", report.get测试单位电话());
            context.put("partyBTaxNumber", report.get测试单位传真());
            context.put("partyBWebsite", report.get测试单位网址());
            context.put("partyBEmail", report.get测试单位Email());
            context.put("hardwareItems", report.get硬件环境());
            context.put("softwareItems", report.get软件环境());
            context.put("networkEnvironment", report.get网络环境());
            context.put("testBasis2", report.get测试依据());
            context.put("referenceMaterial", report.get参考资料());
            context.put("functionalityTestItems", report.get功能性测试());
            context.put("efficiencyTestItems", report.get效率测试());
            context.put("portabilityTestItems", report.get可移植性测试());
            context.put("accessibilityTestItems", report.get易用性测试());
            context.put("reliabilityTestItems", report.get可靠性测试());
            context.put("maintainabilityTestItems", report.get可维护性测试());
            context.put("testExecutionRecord", report.get测试执行记录());

            // 加载velocity模板文件
            // 合并数据到模板
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            engine.mergeTemplate(inputPath + "Report.vm", "utf-8", context, fw);

            // 释放资源
            fw.close();

            // tex文件转为pdf文件
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex");
            File file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                System.out.println("!!! pdf1 not exist !!!");
                Thread.sleep(100);
            }
            process = runtime.exec("mv " + outputPath + filename + ".pdf " + outputPath + filename + "_old.pdf");
            file = new File(outputPath + filename + "_old.pdf");
            while (!file.exists()) {
                System.out.println("!!! pdf1_old not exist !!!");
                Thread.sleep(100);
            }
            process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex"); // 须执行两次，否则无法获得总页码
            file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                System.out.println("!!! pdf2 not exist !!!");
                Thread.sleep(100);
            }

            // 删除中间生成文件
            // process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux " + outputPath + filename + ".log " + outputPath + "texput.log");
            process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux");

        }
        else {
            System.out.println("!!! run not in file or in jar !!!");
        }

    }

    @SneakyThrows
    public boolean creatFile(String projectId, String fileName, String fileType, MultipartFile file) {

        if (!Objects.equals(file, null) && !file.isEmpty()) {

            if(minioService.hasBucket(projectId))
            {
                Iterable<Result<Item>> allFiles = minioService.listObjects(projectId);
                if(allFiles != null)
                {
                    for(Result<Item> f : allFiles)
                    {
                        minioService.removeObject(projectId, f.get().objectName());
                    }
                }
            }
            else {
                minioService.createBucket(projectId);
            }

            StatObjectResponse objectStat = null;
            try {
                objectStat = minioService.getObjectInfo(projectId, fileName);
                return false;
            } catch (Exception e) {
                minioService.putObject(projectId, fileName, file);
                Map<String, String> mp = new HashMap<>();
                mp.put("fileType", fileType);
                minioService.setTag(projectId, fileName, mp);
            }

        } else {
            System.out.println("!!! file is null !!!");
        }

        return true;
    }

    @SneakyThrows
    public void saveReportFile(String projectId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            String outputPath = "test-server/src/main/resources/generate/";

            File reportFile = new File(outputPath + "Report_" + projectId + ".pdf");
            FileInputStream reportFileInputStream = new FileInputStream(reportFile);
            MultipartFile reportMultipartFile = new MockMultipartFile(reportFile.getName(), reportFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), reportFileInputStream);

            creatFile(projectId, reportMultipartFile.getOriginalFilename(), "Report_" + projectId, reportMultipartFile);

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            String outputPath = "velocity/generate/";

            File reportFile = new File(outputPath + "Report_" + projectId + ".pdf");
            FileInputStream reportFileInputStream = new FileInputStream(reportFile);
            MultipartFile reportMultipartFile = new MockMultipartFile(reportFile.getName(), reportFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), reportFileInputStream);

            if(!reportFile.exists()) {
                System.out.println("report file not generate!!!");
            }

            System.out.println(reportMultipartFile.getOriginalFilename());

            creatFile(projectId, reportMultipartFile.getOriginalFilename(), "Report_" + projectId, reportMultipartFile);

        }
        else {
            System.out.println("!!! run not in file or in jar !!!");
        }

    }

    @SneakyThrows
    public void deleteReportFile(String projectId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            String outputPath = "test-server/src/main/resources/generate/";

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("rm " + outputPath + "Report_" + projectId + ".pdf");

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            String outputPath = "velocity/generate/";

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("rm " + outputPath + "Report_" + projectId + ".pdf");

        }
        else {
            System.out.println("!!! run not in file or in jar !!!");
        }
    }

}
