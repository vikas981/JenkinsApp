package com.viksingh.jenkins.Model;

public class NodeModel {
    int image;

    String name;

    public NodeModel(String paramString) {
        this.name = paramString;
    }

    public NodeModel(String paramString, int paramInt) {
        this.name = paramString;
        this.image = paramInt;
    }

    public int getImage() {
        return this.image;
    }

    public String getName() {
        return this.name;
    }
}