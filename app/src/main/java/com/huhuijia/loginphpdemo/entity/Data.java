package com.huhuijia.loginphpdemo.entity;

public class Data {
    private String sno;
    private String username;
    private String subject;
    private String type;
    private String source;
    private String guidance;
    private String technology;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGuidance() {
        return guidance;
    }

    public void setGuidance(String guidance) {
        this.guidance = guidance;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
    @Override
    public String toString() {
        return "Data [subject=" + subject + ", type=" + type + ", source="
                + source + ", guidance=" + guidance + ", technology=" + technology
                + ", sno=" + sno + ", sno=" + sno +"]";
    }
}
