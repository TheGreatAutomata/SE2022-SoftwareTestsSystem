package com.micro.contractserver.delegate;

import com.micro.contractserver.model.*;
import com.micro.contractserver.service.ContractService;
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
import java.util.Calendar;
import java.util.Properties;

public class GenerateContractFilesDelegate implements JavaDelegate {

    @Autowired
    public ContractService contractService;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        System.out.println("...Generating contract files");

        Contract contract = (Contract)delegateExecution.getVariable("contract");

        generateContractTableFile(contract.getContractTable(), contract.getContractId());
        generateNondisclosureAgreementTableFile(contract.getNondisclosureAgreementTable(), contract.getContractId());

        saveUnsignedContractFiles(contract.getContractId());

        //deleteUnsignedContractFiles(contract.getContractId());

        delegateExecution.setVariable("contract", contract);
        delegateExecution.setVariable("contractId", contract.getContractId());

    }

    @SneakyThrows
    public void generateContractTableFile(ContractTable contractTable, String contractId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            Calendar calendar = Calendar.getInstance();
            ContractTableExist contractTableExist = contractTable.getContractTableExist();
            ContractTablePartyA contractTablePartyA = contractTable.getContractTablePartyA();
            ContractTablePartyB contractTablePartyB = contractTable.getContractTablePartyB();

            // 设置输入输出路径
            File tempFile = new File("");
            String projectPath = tempFile.getCanonicalPath();

            String filename = "Contract_" + contractId;
            String inputPath = "template/";
            String outputPath = "contract-server/src/main/resources/generate/";

            // 设置velocity的资源加载器
            Properties prop = new Properties();
            prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            // 初始化velocity引擎
            Velocity.init(prop);

            // 创建velocity容器
            VelocityContext context = new VelocityContext();

            context.put("testYear", calendar.get(Calendar.YEAR));
            context.put("projectName", contractTableExist.getProjectName());
            context.put("partyAName1", contractTableExist.getPartyAName1());
            context.put("partyBName1", contractTableExist.getPartyBName1());
            context.put("partyAName2", contractTableExist.getPartyAName2());
            context.put("partyBName2", contractTableExist.getPartyBName2());
            context.put("softwareName", contractTableExist.getSoftwareName());
            context.put("softwareQualityCharacteristic", contractTableExist.getSoftwareQualityCharacteristic());
            context.put("paymentInChinese", contractTableExist.getPaymentInChinese());
            context.put("paymentInArabic", contractTableExist.getPaymentInArabic());
            context.put("performanceTerm", contractTableExist.getPerformanceTerm());
            context.put("rectificationTimes", contractTableExist.getRectificationTimes());
            context.put("rectificationTerm", contractTableExist.getRectificationTerm());
            context.put("partyAName3", contractTableExist.getPartyAName3());
            context.put("partyARepresentative", contractTablePartyA.get授权代表());
            context.put("partyALiaison", contractTablePartyA.get联系人());
            context.put("partyAPostalAddress", contractTablePartyA.get通讯地址());
            context.put("partyAPhoneNumber", contractTablePartyA.get电话());
            context.put("partyAFaxNumber", contractTablePartyA.get传真());
            context.put("partyADepositBank", contractTablePartyA.get开户银行());
            context.put("partyABankAccountNumber", contractTablePartyA.get账号());
            context.put("partyAPostalCode", contractTablePartyA.get邮编());
            context.put("partyBName3", contractTableExist.getPartyBName3());
            context.put("partyBRepresentative", contractTablePartyB.get授权代表());
            context.put("partyBLiaison", contractTablePartyB.get联系人());
            context.put("partyBPostalAddress", contractTablePartyB.get通讯地址());
            context.put("partyBPostalCode", contractTablePartyB.get邮编());
            context.put("partyBPhoneNumber", contractTablePartyB.get电话());
            context.put("partyBFaxNumber", contractTablePartyB.get传真());
            context.put("partyBDepositBank", contractTablePartyB.get开户银行());
            context.put("partyBBankAccountName", contractTablePartyB.get户名());
            context.put("partyBBankAccountNumber", contractTablePartyB.get账号());

            // 加载velocity模板文件
            Template template = Velocity.getTemplate(inputPath + "Contract.vm", "utf-8");

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
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            Calendar calendar = Calendar.getInstance();
            ContractTableExist contractTableExist = contractTable.getContractTableExist();
            ContractTablePartyA contractTablePartyA = contractTable.getContractTablePartyA();
            ContractTablePartyB contractTablePartyB = contractTable.getContractTablePartyB();

            // 设置输入输出路径
            File tempFile = new File("");
            String projectPath = tempFile.getCanonicalPath();

            String filename = "Contract_" + contractId;
            String inputPath = "template/";
            // String outputPath = "velocity/generate/";
            String outputPath = "SE2022-SoftwareTestsSystem/contract-server/src/main/resources/generate/";

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

            context.put("testYear", calendar.get(Calendar.YEAR));
            context.put("projectName", contractTableExist.getProjectName());
            context.put("partyAName1", contractTableExist.getPartyAName1());
            context.put("partyBName1", contractTableExist.getPartyBName1());
            context.put("partyAName2", contractTableExist.getPartyAName2());
            context.put("partyBName2", contractTableExist.getPartyBName2());
            context.put("softwareName", contractTableExist.getSoftwareName());
            context.put("softwareQualityCharacteristic", contractTableExist.getSoftwareQualityCharacteristic());
            context.put("paymentInChinese", contractTableExist.getPaymentInChinese());
            context.put("paymentInArabic", contractTableExist.getPaymentInArabic());
            context.put("performanceTerm", contractTableExist.getPerformanceTerm());
            context.put("rectificationTimes", contractTableExist.getRectificationTimes());
            context.put("rectificationTerm", contractTableExist.getRectificationTerm());
            context.put("partyAName3", contractTableExist.getPartyAName3());
            context.put("partyARepresentative", contractTablePartyA.get授权代表());
            context.put("partyALiaison", contractTablePartyA.get联系人());
            context.put("partyAPostalAddress", contractTablePartyA.get通讯地址());
            context.put("partyAPhoneNumber", contractTablePartyA.get电话());
            context.put("partyAFaxNumber", contractTablePartyA.get传真());
            context.put("partyADepositBank", contractTablePartyA.get开户银行());
            context.put("partyABankAccountNumber", contractTablePartyA.get账号());
            context.put("partyAPostalCode", contractTablePartyA.get邮编());
            context.put("partyBName3", contractTableExist.getPartyBName3());
            context.put("partyBRepresentative", contractTablePartyB.get授权代表());
            context.put("partyBLiaison", contractTablePartyB.get联系人());
            context.put("partyBPostalAddress", contractTablePartyB.get通讯地址());
            context.put("partyBPostalCode", contractTablePartyB.get邮编());
            context.put("partyBPhoneNumber", contractTablePartyB.get电话());
            context.put("partyBFaxNumber", contractTablePartyB.get传真());
            context.put("partyBDepositBank", contractTablePartyB.get开户银行());
            context.put("partyBBankAccountName", contractTablePartyB.get户名());
            context.put("partyBBankAccountNumber", contractTablePartyB.get账号());

            // 加载velocity模板文件
            // 合并数据到模板
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            engine.mergeTemplate(inputPath + "Contract.vm", "utf-8", context, fw);

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
            System.out.println("!!! run not in file or jar !!!");
        }

    }


    @SneakyThrows
    public void generateNondisclosureAgreementTableFile(NondisclosureAgreementTable nondisclosureAgreementTable, String contractId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            Calendar calendar = Calendar.getInstance();

            // 设置输入输出路径
            File tempFile = new File("");
            String projectPath = tempFile.getCanonicalPath();

            String filename = "NDA_" + contractId;
            String inputPath = "template/";
            String outputPath = "contract-server/src/main/resources/generate/";

            // 设置velocity的资源加载器
            Properties prop = new Properties();
            prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            // 初始化velocity引擎
            Velocity.init(prop);

            // 创建velocity容器
            VelocityContext context = new VelocityContext();

            // velocity容器变量数据填充
            context.put("testYear", calendar.get(Calendar.YEAR));
            context.put("partyAName", nondisclosureAgreementTable.getPartyAName());
            context.put("partyBName", nondisclosureAgreementTable.getPartyBName());
            context.put("projectName", nondisclosureAgreementTable.getProjectName());

            // 加载velocity模板文件
            Template template = Velocity.getTemplate(inputPath + "NDA.vm", "utf-8");

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

            Calendar calendar = Calendar.getInstance();

            // 设置输入输出路径
            String filename = "NDA_" + contractId;
            String inputPath = "template/";
            // String outputPath = "velocity/generate/";
            String outputPath = "SE2022-SoftwareTestsSystem/contract-server/src/main/resources/generate/";

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

            // velocity容器变量数据填充
            context.put("testYear", calendar.get(Calendar.YEAR));
            context.put("partyAName", nondisclosureAgreementTable.getPartyAName());
            context.put("partyBName", nondisclosureAgreementTable.getPartyBName());
            context.put("projectName", nondisclosureAgreementTable.getProjectName());

            // 加载velocity模板文件
            // 合并数据到模板
            FileWriter fw = new FileWriter(outputPath + filename + ".tex");
            engine.mergeTemplate(inputPath + "NDA.vm", "utf-8", context, fw);

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
        else {
            System.out.println("!!! run not in file or jar !!!");
        }
    }

    @SneakyThrows
    public void saveUnsignedContractFiles(String contractId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            String outputPath = "contract-server/src/main/resources/generate/";

            File contractTableFile = new File(outputPath + "Contract_" + contractId + ".pdf");
            FileInputStream contractTableFileInputStream = new FileInputStream(contractTableFile);
            MultipartFile contractTableMultipartFile = new MockMultipartFile(contractTableFile.getName(), contractTableFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), contractTableFileInputStream);

            File nondisclosureAgreementTableFile = new File(outputPath + "NDA_" + contractId + ".pdf");
            FileInputStream nondisclosureAgreementTableFileInputStream = new FileInputStream(nondisclosureAgreementTableFile);
            MultipartFile nondisclosureAgreementTableMultipartFile = new MockMultipartFile(nondisclosureAgreementTableFile.getName(), nondisclosureAgreementTableFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), nondisclosureAgreementTableFileInputStream);

            contractService.creatFile(contractId, contractTableMultipartFile.getOriginalFilename(), "Contract_" + contractId, contractTableMultipartFile);
            contractService.creatFile(contractId, nondisclosureAgreementTableMultipartFile.getOriginalFilename(), "NDA_" + contractId, nondisclosureAgreementTableMultipartFile);

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            // String outputPath = "velocity/generate/";
            String outputPath = "SE2022-SoftwareTestsSystem/contract-server/src/main/resources/generate/";

            File contractTableFile = new File(outputPath + "Contract_" + contractId + ".pdf");
            FileInputStream contractTableFileInputStream = new FileInputStream(contractTableFile);
            MultipartFile contractTableMultipartFile = new MockMultipartFile(contractTableFile.getName(), contractTableFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), contractTableFileInputStream);

            File nondisclosureAgreementTableFile = new File(outputPath + "NDA_" + contractId + ".pdf");
            FileInputStream nondisclosureAgreementTableFileInputStream = new FileInputStream(nondisclosureAgreementTableFile);
            MultipartFile nondisclosureAgreementTableMultipartFile = new MockMultipartFile(nondisclosureAgreementTableFile.getName(), nondisclosureAgreementTableFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), nondisclosureAgreementTableFileInputStream);

            contractService.creatFile(contractId, contractTableMultipartFile.getOriginalFilename(), "Contract_" + contractId, contractTableMultipartFile);
            contractService.creatFile(contractId, nondisclosureAgreementTableMultipartFile.getOriginalFilename(), "NDA_" + contractId, nondisclosureAgreementTableMultipartFile);

        }
        else {
            System.out.println("!!! run not in file or jar !!!");
        }
    }

    @SneakyThrows
    public void deleteUnsignedContractFiles(String contractId) {

        if(this.getClass().getResource("").getProtocol().equals("file")) {

            String outputPath = "contract-server/src/main/resources/generate/";

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("rm " + outputPath + "Contract_" + contractId + ".pdf");
            process = runtime.exec("rm " + outputPath + "NDA_" + contractId + ".pdf");

        }
        else if(this.getClass().getResource("").getProtocol().equals("jar")) {

            // String outputPath = "velocity/generate/";
            String outputPath = "SE2022-SoftwareTestsSystem/contract-server/src/main/resources/generate/";

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("rm " + outputPath + "Contract_" + contractId + ".pdf");
            process = runtime.exec("rm " + outputPath + "NDA_" + contractId + ".pdf");

        }
        else {
            System.out.println("!!! run not in file or jar !!!");
        }

    }

}
