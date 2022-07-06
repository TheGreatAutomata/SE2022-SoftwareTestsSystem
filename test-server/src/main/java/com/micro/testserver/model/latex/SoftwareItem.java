package com.micro.testserver.model.latex;

public class SoftwareItem {

    private String category;
    private String name;
    private String version;

    public SoftwareItem() {
    }

    public SoftwareItem(String category, String name, String version) {
        this.category = category;
        this.name = name;
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
