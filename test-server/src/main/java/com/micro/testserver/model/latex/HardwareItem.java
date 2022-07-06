package com.micro.testserver.model.latex;

public class HardwareItem {

    private String category;
    private String name;
    private String configuration;
    private int amount;

    public HardwareItem() {
    }

    public HardwareItem(String category, String name, String configuration, int amount) {
        this.category = category;
        this.name = name;
        this.configuration = configuration;
        this.amount = amount;
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

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
