package com.micro.testserver.model.latex;

public class EfficiencyTestItem {

    private String testFeature;
    private String testDescription;
    private String testResult;

    public EfficiencyTestItem() {
    }

    public EfficiencyTestItem(String testFeature, String testDescription, String testResult) {
        this.testFeature = testFeature;
        this.testDescription = testDescription;
        this.testResult = testResult;
    }

    public String getTestFeature() {
        return testFeature;
    }

    public void setTestFeature(String testFeature) {
        this.testFeature = testFeature;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

}
