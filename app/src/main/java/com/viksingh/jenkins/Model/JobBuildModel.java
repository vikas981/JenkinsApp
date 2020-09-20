package com.viksingh.jenkins.Model;

import androidx.lifecycle.ViewModel;

public class JobBuildModel extends ViewModel {
    private String consoleOuput;
    private int number;
    private String resultStatus;

    public JobBuildModel(int number, String consoleOuput) {
        this.number =number;
        this.consoleOuput = consoleOuput;
    }

    public JobBuildModel(int number, String consoleOuput, String resultStatus) {
        this.number = number;
        this.consoleOuput = consoleOuput;
        this.resultStatus = resultStatus;
    }

    public String getConsoleOuput() {
        return this.consoleOuput;
    }

    public int getNumber() {
        return this.number;
    }

    public String getResultStatus() {
        return this.resultStatus;
    }
}
