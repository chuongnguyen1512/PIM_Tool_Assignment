package com.elca.vn.model;

import java.util.Map;
import java.util.Objects;

public class GUIEventMessage {

    private String messageID;
    private Map<String, Object> params;

    public String getMessageID() {
        return messageID;
    }

    public GUIEventMessage setMessageID(String messageID) {
        this.messageID = messageID;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public GUIEventMessage setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUIEventMessage that = (GUIEventMessage) o;
        return Objects.equals(messageID, that.messageID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageID);
    }
}
