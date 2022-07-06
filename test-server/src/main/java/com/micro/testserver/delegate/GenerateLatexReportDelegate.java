package com.micro.testserver.delegate;

import com.micro.testserver.model.*;
import com.micro.testserver.model.latex.*;
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

    private List<AccessibilityTestItem> accessibilityTestItemList = new ArrayList<>();

    private List<EfficiencyTestItem> efficiencyTestItemList = new ArrayList<>();

    private List<FunctionalityTestItem> functionalityTestItemList = new ArrayList<>();

    private List<HardwareItem> hardwareItemList = new ArrayList<>();

    private List<MaintainabilityTestItem> maintainabilityTestItemList = new ArrayList<>();

    private List<PortabilityTestItem> portabilityTestItemList = new ArrayList<>();

    private List<ReliabilityTestItem> reliabilityTestItemList = new ArrayList<>();

    private List<SoftwareItem> softwareItemList = new ArrayList<>();

    private List<String> referenceMaterialList = new ArrayList<>();

    private List<String> testBasis2List = new ArrayList<>();

    @Override
    public void execute(DelegateExecution delegateExecution) {

        SoftwareTest softwareTest = delegateExecution.getVariable("softwareTest", SoftwareTest.class);
        if(softwareTest == null){

            System.out.println("!!! get software test failed !!!");

            return;
        }

        transformData(softwareTest.getTestReport());

        generateReportFile(softwareTest, softwareTest.getProjectId());

        saveReportFile(softwareTest.getProjectId());

        deleteReportFile(softwareTest.getProjectId());

        softwareTestRepository.save(softwareTest);
        delegateExecution.setVariable("softwareTest",softwareTest);
    }

    public void transformData(SoftwareTestReport report) {

        List<TestReportHardwareEnv> testReportHardwareEnvList = report.get硬件环境();
        List<TestReportSoftwareEnv> testReportSoftwareEnvList = report.get软件环境();
        List<TestReportTestDependency> testReportTestDependencyList = report.get测试依据();
        List<TestReportReference> testReportReferenceList = report.get参考资料();
        List<TestReportFuncTest> testReportFuncTestList = report.get功能性测试();
        List<TestReportEfficiencyTest> testReportEfficiencyTestList = report.get效率测试();
        List<TestReportPortabilityTest> testReportPortabilityTestList = report.get可移植性测试();
        List<TestReportUsabilityTest> testReportUsabilityTestList = report.get易用性测试();
        List<TestReportReliabilityTest> testReportReliabilityTestList = report.get可靠性测试();
        List<TestReportMaintainabilityTest> testReportMaintainabilityTestList = report.get可维护性测试();

        for(TestReportHardwareEnv t : testReportHardwareEnvList) {
            hardwareItemList.add(new HardwareItem(t.get硬件类别().replace("%", "\\%"), t.get硬件名称().replace("%", "\\%"), t.get配置().replace("%", "\\%"), Integer.parseInt(t.get数量())));
        }

        for(TestReportSoftwareEnv t : testReportSoftwareEnvList) {
            softwareItemList.add(new SoftwareItem(t.get软件类别().replace("%", "\\%"), t.get软件名称().replace("%", "\\%"), t.get版本().replace("%", "\\%")));
        }

        for(TestReportTestDependency t : testReportTestDependencyList) {
            testBasis2List.add(t.get测试依据分项().replace("%", "\\%"));
        }

        for(TestReportReference t : testReportReferenceList) {
            referenceMaterialList.add(t.get参考资料分项().replace("%", "\\%"));
        }

        for(TestReportFuncTest t : testReportFuncTestList) {
            functionalityTestItemList.add(new FunctionalityTestItem(t.get功能模块().replace("%", "\\%"), t.get功能要求().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

        for(TestReportEfficiencyTest t : testReportEfficiencyTestList) {
            efficiencyTestItemList.add(new EfficiencyTestItem(t.get测试特性().replace("%", "\\%"), t.get测试说明().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

        for(TestReportPortabilityTest t : testReportPortabilityTestList) {
            portabilityTestItemList.add(new PortabilityTestItem(t.get测试特性().replace("%", "\\%"), t.get测试说明().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

        for(TestReportUsabilityTest t : testReportUsabilityTestList) {
            accessibilityTestItemList.add(new AccessibilityTestItem(t.get测试特性().replace("%", "\\%"), t.get测试说明().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

        for(TestReportReliabilityTest t : testReportReliabilityTestList) {
            reliabilityTestItemList.add(new ReliabilityTestItem(t.get测试特性().replace("%", "\\%"), t.get测试说明().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

        for(TestReportMaintainabilityTest t : testReportMaintainabilityTestList) {
            maintainabilityTestItemList.add(new MaintainabilityTestItem(t.get测试特性().replace("%", "\\%"), t.get测试说明().replace("%", "\\%"), t.get测试结果().replace("%", "\\%")));
        }

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
            context.put("testYear", reportTime[0].replace("%", "\\%"));
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("reportID1", report.get报告编号().replace("%", "\\%"));
            context.put("reportID2", report.get报告编号().replace("%", "\\%"));
            context.put("softwareName", report.get软件名称().replace("%", "\\%"));
            context.put("versionNumber1", report.get版本号().replace("%", "\\%"));
            context.put("partyAName1", report.get总委托单位().replace("%", "\\%"));
            context.put("testCategory1", report.get测试类别().replace("%", "\\%"));
            context.put("reportTimeYear", reportTime[0].replace("%", "\\%"));
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("partyAName2", report.get委托单位().replace("%", "\\%"));
            context.put("projectID", projectId.replace("%", "\\%"));
            context.put("sampleName", report.get样品名称().replace("%", "\\%"));
            context.put("versionNumber2", report.get版本型号().replace("%", "\\%"));
            context.put("sampleTimeYear", sampleTime[0].replace("%", "\\%"));
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testCategory2", report.get测试类型().replace("%", "\\%"));
            context.put("testStartYear", testStartTime[0].replace("%", "\\%"));
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndYear", testEndTime[0].replace("%", "\\%"));
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleStatus", report.get样品状态().replace("%", "\\%"));
            context.put("testBasis1", report.get总测试依据().replace("%", "\\%"));
            context.put("sampleList", report.get样品清单().replace("%", "\\%"));
            context.put("testConclusion", report.get测试结论().replace("%", "\\%"));
            context.put("partyAPhoneNumber", report.get电话().replace("%", "\\%"));
            context.put("partyATaxNumber", report.get传真().replace("%", "\\%"));
            context.put("partyAAddress", report.get地址().replace("%", "\\%"));
            context.put("partyAPostCode", report.get邮编().replace("%", "\\%"));
            context.put("partyALiaison", report.get联系人().replace("%", "\\%"));
            context.put("partyAEmail", report.getEmail().replace("%", "\\%"));
            context.put("partyBAddress", report.get测试单位单位地址().replace("%", "\\%"));
            context.put("partyBPostCode", report.get测试单位邮政编码().replace("%", "\\%"));
            context.put("partyBPhoneNumber", report.get测试单位电话().replace("%", "\\%"));
            context.put("partyBTaxNumber", report.get测试单位传真().replace("%", "\\%"));
            context.put("partyBWebsite", report.get测试单位网址().replace("%", "\\%"));
            context.put("partyBEmail", report.get测试单位Email().replace("%", "\\%"));
            context.put("hardwareItems", hardwareItemList);
            context.put("softwareItems", softwareItemList);
            context.put("networkEnvironment", report.get网络环境().replace("%", "\\%"));
            context.put("testBasis2", testBasis2List);
            context.put("referenceMaterial", referenceMaterialList);
            context.put("functionalityTestItems", functionalityTestItemList);
            context.put("efficiencyTestItems", efficiencyTestItemList);
            context.put("portabilityTestItems", portabilityTestItemList);
            context.put("accessibilityTestItems", accessibilityTestItemList);
            context.put("reliabilityTestItems", reliabilityTestItemList);
            context.put("maintainabilityTestItems", maintainabilityTestItemList);
            context.put("testExecutionRecord", report.get测试执行记录().replace("%", "\\%"));

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
            properties.setProperty("jar.resource.loader.path", "jar:file:/root/deployment/test-server-0.0.1-SNAPSHOT.jar");
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
            context.put("testYear", reportTime[0].replace("%", "\\%"));
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("reportID1", report.get报告编号().replace("%", "\\%"));
            context.put("reportID2", report.get报告编号().replace("%", "\\%"));
            context.put("softwareName", report.get软件名称().replace("%", "\\%"));
            context.put("versionNumber1", report.get版本号().replace("%", "\\%"));
            context.put("partyAName1", report.get总委托单位().replace("%", "\\%"));
            context.put("testCategory1", report.get测试类别().replace("%", "\\%"));
            context.put("reportTimeYear", reportTime[0].replace("%", "\\%"));
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("partyAName2", report.get委托单位().replace("%", "\\%"));
            context.put("projectID", projectId.replace("%", "\\%"));
            context.put("sampleName", report.get样品名称().replace("%", "\\%"));
            context.put("versionNumber2", report.get版本型号().replace("%", "\\%"));
            context.put("sampleTimeYear", sampleTime[0].replace("%", "\\%"));
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testCategory2", report.get测试类型().replace("%", "\\%"));
            context.put("testStartYear", testStartTime[0].replace("%", "\\%"));
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndYear", testEndTime[0].replace("%", "\\%"));
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleStatus", report.get样品状态().replace("%", "\\%"));
            context.put("testBasis1", report.get总测试依据().replace("%", "\\%"));
            context.put("sampleList", report.get样品清单().replace("%", "\\%"));
            context.put("testConclusion", report.get测试结论().replace("%", "\\%"));
            context.put("partyAPhoneNumber", report.get电话().replace("%", "\\%"));
            context.put("partyATaxNumber", report.get传真().replace("%", "\\%"));
            context.put("partyAAddress", report.get地址().replace("%", "\\%"));
            context.put("partyAPostCode", report.get邮编().replace("%", "\\%"));
            context.put("partyALiaison", report.get联系人().replace("%", "\\%"));
            context.put("partyAEmail", report.getEmail().replace("%", "\\%"));
            context.put("partyBAddress", report.get测试单位单位地址().replace("%", "\\%"));
            context.put("partyBPostCode", report.get测试单位邮政编码().replace("%", "\\%"));
            context.put("partyBPhoneNumber", report.get测试单位电话().replace("%", "\\%"));
            context.put("partyBTaxNumber", report.get测试单位传真().replace("%", "\\%"));
            context.put("partyBWebsite", report.get测试单位网址().replace("%", "\\%"));
            context.put("partyBEmail", report.get测试单位Email().replace("%", "\\%"));
            context.put("hardwareItems", hardwareItemList);
            context.put("softwareItems", softwareItemList);
            context.put("networkEnvironment", report.get网络环境().replace("%", "\\%"));
            context.put("testBasis2", testBasis2List);
            context.put("referenceMaterial", referenceMaterialList);
            context.put("functionalityTestItems", functionalityTestItemList);
            context.put("efficiencyTestItems", efficiencyTestItemList);
            context.put("portabilityTestItems", portabilityTestItemList);
            context.put("accessibilityTestItems", accessibilityTestItemList);
            context.put("reliabilityTestItems", reliabilityTestItemList);
            context.put("maintainabilityTestItems", maintainabilityTestItemList);
            context.put("testExecutionRecord", report.get测试执行记录().replace("%", "\\%"));

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
            process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux " + outputPath + filename + ".log " + outputPath + "texput.log");

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
