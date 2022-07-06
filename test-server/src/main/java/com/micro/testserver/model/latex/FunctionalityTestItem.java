package com.micro.testserver.model.latex;

public class FunctionalityTestItem {

    private String module;
    private String requirement;
    private String result;

    public FunctionalityTestItem() {
    }

    public FunctionalityTestItem(String module, String requirement, String result) {
        this.module = module;
        this.requirement = requirement;
        this.result = result;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
