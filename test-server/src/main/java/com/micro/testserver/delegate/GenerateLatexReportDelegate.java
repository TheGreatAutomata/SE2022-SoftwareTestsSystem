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

    void init(){
        accessibilityTestItemList = new ArrayList<>();

        efficiencyTestItemList = new ArrayList<>();

        functionalityTestItemList = new ArrayList<>();

        hardwareItemList = new ArrayList<>();

        maintainabilityTestItemList = new ArrayList<>();

        portabilityTestItemList = new ArrayList<>();

        reliabilityTestItemList = new ArrayList<>();

        softwareItemList = new ArrayList<>();

        referenceMaterialList = new ArrayList<>();

        testBasis2List = new ArrayList<>();

    }

    /**
     * ??????????????????tex??????????????????pdf????????????????????????
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {

        SoftwareTest softwareTest = delegateExecution.getVariable("softwareTest", SoftwareTest.class);
        if(softwareTest == null){

            System.out.println("!!! get software test failed !!!");

            return;
        }

        init();
        System.out.println("???????????????");
        System.out.println(softwareTest);

        transformData(softwareTest.getTestReport());

        generateReportFile(softwareTest, softwareTest.getProjectId());

        saveReportFile(softwareTest.getProjectId());

        deleteReportFile(softwareTest.getProjectId());

        softwareTestRepository.save(softwareTest);
        delegateExecution.setVariable("softwareTest",softwareTest);
    }

    /**
     * ????????????????????????????????????tex???????????????
     * @param report
     */
    public void transformData(SoftwareTestReport report) {

        List<TestReportHardwareEnv> testReportHardwareEnvList = report.get????????????();
        List<TestReportSoftwareEnv> testReportSoftwareEnvList = report.get????????????();
        List<TestReportTestDependency> testReportTestDependencyList = report.get????????????();
        List<TestReportReference> testReportReferenceList = report.get????????????();
        List<TestReportFuncTest> testReportFuncTestList = report.get???????????????();
        List<TestReportEfficiencyTest> testReportEfficiencyTestList = report.get????????????();
        List<TestReportPortabilityTest> testReportPortabilityTestList = report.get??????????????????();
        List<TestReportUsabilityTest> testReportUsabilityTestList = report.get???????????????();
        List<TestReportReliabilityTest> testReportReliabilityTestList = report.get???????????????();
        List<TestReportMaintainabilityTest> testReportMaintainabilityTestList = report.get??????????????????();

        for(TestReportHardwareEnv t : testReportHardwareEnvList) {
            hardwareItemList.add(new HardwareItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get??????().replace("%", "\\%"), Integer.parseInt(t.get??????())));
        }

        for(TestReportSoftwareEnv t : testReportSoftwareEnvList) {
            softwareItemList.add(new SoftwareItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get??????().replace("%", "\\%")));
        }

        for(TestReportTestDependency t : testReportTestDependencyList) {
            testBasis2List.add(t.get??????????????????().replace("%", "\\%"));
        }

        for(TestReportReference t : testReportReferenceList) {
            referenceMaterialList.add(t.get??????????????????().replace("%", "\\%"));
        }

        for(TestReportFuncTest t : testReportFuncTestList) {
            functionalityTestItemList.add(new FunctionalityTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

        for(TestReportEfficiencyTest t : testReportEfficiencyTestList) {
            efficiencyTestItemList.add(new EfficiencyTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

        for(TestReportPortabilityTest t : testReportPortabilityTestList) {
            portabilityTestItemList.add(new PortabilityTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

        for(TestReportUsabilityTest t : testReportUsabilityTestList) {
            accessibilityTestItemList.add(new AccessibilityTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

        for(TestReportReliabilityTest t : testReportReliabilityTestList) {
            reliabilityTestItemList.add(new ReliabilityTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

        for(TestReportMaintainabilityTest t : testReportMaintainabilityTestList) {
            maintainabilityTestItemList.add(new MaintainabilityTestItem(t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%"), t.get????????????().replace("%", "\\%")));
        }

    }

    /**
     * ??????????????????????????????tex??????????????????pdf
     * @param softwareTest
     * @param projectId
     */
    @SneakyThrows
    public void generateReportFile(SoftwareTest softwareTest, String projectId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            // ????????????????????????
            String filename = "Report_" + projectId;
            String inputPath = "template/";
            String outputPath = "test-server/src/main/resources/generate/";

            // ??????velocity??????????????????
            Properties prop = new Properties();
            prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            // ?????????velocity??????
            Velocity.init(prop);

            // ??????velocity??????
            VelocityContext context = new VelocityContext();

            // velocity????????????????????????
            SoftwareTestReport report = softwareTest.getTestReport();
            String[] reportTime = report.get????????????().split("T")[0].split("-");
            String[] sampleTime = report.get????????????().split("T")[0].split("-");
            String[] testStartTime = report.get??????????????????().split("T")[0].split("-");
            String[] testEndTime = report.get??????????????????().split("T")[0].split("-");

            // velocity????????????????????????
            context.put("testYear", reportTime[0].replace("%", "\\%"));
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("reportID1", report.get????????????().replace("%", "\\%"));
            context.put("reportID2", report.get????????????().replace("%", "\\%"));
            context.put("softwareName", report.get????????????().replace("%", "\\%"));
            context.put("versionNumber1", report.get?????????().replace("%", "\\%"));
            context.put("partyAName1", report.get???????????????().replace("%", "\\%"));
            context.put("testCategory1", report.get????????????().replace("%", "\\%"));
            context.put("reportTimeYear", reportTime[0].replace("%", "\\%"));
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("partyAName2", report.get????????????().replace("%", "\\%"));
            context.put("projectID", projectId.replace("%", "\\%"));
            context.put("sampleName", report.get????????????().replace("%", "\\%"));
            context.put("versionNumber2", report.get????????????().replace("%", "\\%"));
            context.put("sampleTimeYear", sampleTime[0].replace("%", "\\%"));
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testCategory2", report.get????????????().replace("%", "\\%"));
            context.put("testStartYear", testStartTime[0].replace("%", "\\%"));
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndYear", testEndTime[0].replace("%", "\\%"));
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleStatus", report.get????????????().replace("%", "\\%"));
            context.put("testBasis1", report.get???????????????().replace("%", "\\%"));
            context.put("sampleList", report.get????????????().replace("%", "\\%"));
            context.put("testConclusion", report.get????????????().replace("%", "\\%"));
            context.put("partyAPhoneNumber", report.get??????().replace("%", "\\%"));
            context.put("partyATaxNumber", report.get??????().replace("%", "\\%"));
            context.put("partyAAddress", report.get??????().replace("%", "\\%"));
            context.put("partyAPostCode", report.get??????().replace("%", "\\%"));
            context.put("partyALiaison", report.get?????????().replace("%", "\\%"));
            context.put("partyAEmail", report.getEmail().replace("%", "\\%"));
            context.put("partyBAddress", report.get????????????????????????().replace("%", "\\%"));
            context.put("partyBPostCode", report.get????????????????????????().replace("%", "\\%"));
            context.put("partyBPhoneNumber", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBTaxNumber", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBWebsite", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBEmail", report.get????????????Email().replace("%", "\\%"));
            context.put("hardwareItems", hardwareItemList);
            context.put("softwareItems", softwareItemList);
            context.put("networkEnvironment", report.get????????????().replace("%", "\\%"));
            context.put("testBasis2", testBasis2List);
            context.put("referenceMaterial", referenceMaterialList);
            context.put("functionalityTestItems", functionalityTestItemList);
            context.put("efficiencyTestItems", efficiencyTestItemList);
            context.put("portabilityTestItems", portabilityTestItemList);
            context.put("accessibilityTestItems", accessibilityTestItemList);
            context.put("reliabilityTestItems", reliabilityTestItemList);
            context.put("maintainabilityTestItems", maintainabilityTestItemList);
            context.put("testExecutionRecord", report.get??????????????????().replace("%", "\\%"));

            // ??????velocity????????????
            Template template = Velocity.getTemplate(inputPath + "Report.vm", "utf-8");

            // ?????????????????????
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            template.merge(context, fw);

            // ????????????
            fw.close();

            // tex????????????pdf??????
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
            process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex"); // ?????????????????????????????????????????????
            file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                Thread.sleep(100);
            }

            // ????????????????????????
            process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux " + outputPath + filename + ".log " + outputPath + "texput.log");

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            // ????????????????????????
            String filename = "Report_" + projectId;
            String inputPath = "template/";
            String outputPath = "velocity/generate/";

            // ??????velocity??????????????????
            Properties properties = new Properties();
            properties.setProperty("resource.loader", "class");
            properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty("jar.resource.loader.path", "jar:file:/root/deployment/test-server-0.0.1-SNAPSHOT.jar");
            properties.put("input.encoding", "UTF-8");
            properties.put("output.encoding", "UTF-8");

            // ?????????velocity??????
            VelocityEngine engine = new VelocityEngine();
            engine.init(properties);

            // ??????velocity??????
            VelocityContext context = new VelocityContext();

            // velocity????????????????????????
            SoftwareTestReport report = softwareTest.getTestReport();

            System.out.println(report.get????????????());
            String[] reportTime = report.get????????????().split("T")[0].split("-");
            String[] sampleTime = report.get????????????().split("T")[0].split("-");
            String[] testStartTime = report.get??????????????????().split("T")[0].split("-");
            String[] testEndTime = report.get??????????????????().split("T")[0].split("-");

            // velocity????????????????????????
            context.put("testYear", reportTime[0].replace("%", "\\%"));
            context.put("partyBName1", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("reportID1", report.get????????????().replace("%", "\\%"));
            context.put("reportID2", report.get????????????().replace("%", "\\%"));
            context.put("softwareName", report.get????????????().replace("%", "\\%"));
            context.put("versionNumber1", report.get?????????().replace("%", "\\%"));
            context.put("partyAName1", report.get???????????????().replace("%", "\\%"));
            context.put("testCategory1", report.get????????????().replace("%", "\\%"));
            context.put("reportTimeYear", reportTime[0].replace("%", "\\%"));
            context.put("reportTimeMonth", reportTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("reportTimeDay", reportTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("partyBName2", softwareTest.getContract().getContractTable().getContractTableExist().getPartyBName1().replace("%", "\\%"));
            context.put("partyAName2", report.get????????????().replace("%", "\\%"));
            context.put("projectID", projectId.replace("%", "\\%"));
            context.put("sampleName", report.get????????????().replace("%", "\\%"));
            context.put("versionNumber2", report.get????????????().replace("%", "\\%"));
            context.put("sampleTimeYear", sampleTime[0].replace("%", "\\%"));
            context.put("sampleTimeMonth", sampleTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleTimeDay", sampleTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testCategory2", report.get????????????().replace("%", "\\%"));
            context.put("testStartYear", testStartTime[0].replace("%", "\\%"));
            context.put("testStartMonth", testStartTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testStartDay", testStartTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndYear", testEndTime[0].replace("%", "\\%"));
            context.put("testEndMonth", testEndTime[1].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("testEndDay", testEndTime[2].replaceFirst("^0*", "").replace("%", "\\%"));
            context.put("sampleStatus", report.get????????????().replace("%", "\\%"));
            context.put("testBasis1", report.get???????????????().replace("%", "\\%"));
            context.put("sampleList", report.get????????????().replace("%", "\\%"));
            context.put("testConclusion", report.get????????????().replace("%", "\\%"));
            context.put("partyAPhoneNumber", report.get??????().replace("%", "\\%"));
            context.put("partyATaxNumber", report.get??????().replace("%", "\\%"));
            context.put("partyAAddress", report.get??????().replace("%", "\\%"));
            context.put("partyAPostCode", report.get??????().replace("%", "\\%"));
            context.put("partyALiaison", report.get?????????().replace("%", "\\%"));
            context.put("partyAEmail", report.getEmail().replace("%", "\\%"));
            context.put("partyBAddress", report.get????????????????????????().replace("%", "\\%"));
            context.put("partyBPostCode", report.get????????????????????????().replace("%", "\\%"));
            context.put("partyBPhoneNumber", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBTaxNumber", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBWebsite", report.get??????????????????().replace("%", "\\%"));
            context.put("partyBEmail", report.get????????????Email().replace("%", "\\%"));
            context.put("hardwareItems", hardwareItemList);
            context.put("softwareItems", softwareItemList);
            context.put("networkEnvironment", report.get????????????().replace("%", "\\%"));
            context.put("testBasis2", testBasis2List);
            context.put("referenceMaterial", referenceMaterialList);
            context.put("functionalityTestItems", functionalityTestItemList);
            context.put("efficiencyTestItems", efficiencyTestItemList);
            context.put("portabilityTestItems", portabilityTestItemList);
            context.put("accessibilityTestItems", accessibilityTestItemList);
            context.put("reliabilityTestItems", reliabilityTestItemList);
            context.put("maintainabilityTestItems", maintainabilityTestItemList);
            context.put("testExecutionRecord", report.get??????????????????().replace("%", "\\%"));

            // ??????velocity????????????
            // ?????????????????????
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            engine.mergeTemplate(inputPath + "Report.vm", "utf-8", context, fw);

            // ????????????
            fw.close();

            // tex????????????pdf??????
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
            process = runtime.exec("xelatex -output-directory=" + outputPath + " " + outputPath + filename + ".tex"); // ?????????????????????????????????????????????
            file = new File(outputPath + filename + ".pdf");
            while (!file.exists()) {
                System.out.println("!!! pdf2 not exist !!!");
                Thread.sleep(100);
            }

            // ????????????????????????
            process = runtime.exec("rm " + outputPath + filename + "_old.pdf " + outputPath + filename + ".tex " + outputPath + filename + ".aux " + outputPath + filename + ".log " + outputPath + "texput.log");

        }
        else {
            System.out.println("!!! run not in file or in jar !!!");
        }

    }

    /**
     * ??????????????????????????????
     * @param projectId
     * @param fileName
     * @param fileType
     * @param file
     * @return boolean
     */
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

    /**
     * ???????????????????????????????????????
     * @param projectId
     */
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

    /**
     * ???????????????????????????????????????
     * @param projectId
     */
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
