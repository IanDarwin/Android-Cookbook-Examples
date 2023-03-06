package com.darwinsys.testingwithmockito;

public class WorkerBee {
    WorkerHelper helper;

    void setHelper(WorkerHelper helper) {
        this.helper = helper;
    }

    void process() {
        helper.invoke();
    }
}
