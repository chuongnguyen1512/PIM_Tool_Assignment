package com.elca.vn.model;

import com.elca.vn.proto.model.Status;

/**
 * Model to transform between GUI value and proto object
 */
public enum GUIStatusModel {

    NEW(Status.NEW, "New"),
    PLANNED(Status.PLA, "Planned"),
    INPROGRESS(Status.INO, "In Progress"),
    FINISHED(Status.FIN, "Finished");

    private Status status;
    private String statusValue;

    public Status getStatus() {
        return status;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public boolean isEqual(Status status) {
        if (this.status.equals(status)) {
            return true;
        }
        return false;
    }

    public boolean isEqual(String statusValue) {
        if (this.statusValue.equals(statusValue)) {
            return true;
        }
        return false;
    }

    private GUIStatusModel(Status status, String statusValue) {
        this.status = status;
        this.statusValue = statusValue;
    }
}
