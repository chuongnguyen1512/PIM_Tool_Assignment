package com.elca.vn.model;

import com.elca.vn.proto.model.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * Model to transform between GUI value and proto object
 */
public enum GUIStatusModel {

    NEW(Status.NEW, "New"),
    PLANNED(Status.PLA, "Planned"),
    INPROGRESS(Status.INO, "In Progress"),
    FINISHED(Status.FIN, "Finished");

    public final Status status;
    public final String statusValue;

    private static final Map<Status, GUIStatusModel> BY_STATUS = new HashMap<>();
    private static final Map<String, GUIStatusModel> BY_STRING = new HashMap<>();

    static {
        for (GUIStatusModel e : values()) {
            BY_STATUS.put(e.status, e);
            BY_STRING.put(e.statusValue, e);
        }
    }

    private GUIStatusModel(Status status, String statusValue) {
        this.status = status;
        this.statusValue = statusValue;
    }

    public static GUIStatusModel valueOfStatus(Status status) {
        return BY_STATUS.get(status);
    }

    public static GUIStatusModel valueOfStatusValue(String statusValue) {
        return BY_STRING.get(statusValue);
    }
}
