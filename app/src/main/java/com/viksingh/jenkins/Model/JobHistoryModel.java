package com.viksingh.jenkins.Model;

public class JobHistoryModel {

    private String consoleOutputText;
    private String displayName;
    private long duration;
    private String jobName;
    private int number;
    private String resultStatus;

    public JobHistoryModel(String consoleOutputText, String displayName, long duration, String jobName, int number, String resultStatus) {
        this.consoleOutputText = consoleOutputText;
        this.displayName = displayName;
        this.duration = duration;
        this.jobName = jobName;
        this.number = number;
        this.resultStatus = resultStatus;
    }

    public String getConsoleOutputText() {
        return consoleOutputText;
    }

    public void setConsoleOutputText(String consoleOutputText) {
        this.consoleOutputText = consoleOutputText;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
