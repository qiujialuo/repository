package com.newcoder.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiujl on 2017/6/21.
 */
public class EventModel {
    private int actorId;
    private int entityOwnerId;
    private EventType eventType;
    private int entityType;
    private int EntityId;

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }

    private Map<String, String> exts = new HashMap<String, String>();
    public EventModel() {

    }
    public EventModel(EventType type) {
        this.eventType = type;
    }


    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return EntityId;
    }

    public EventModel setEntityId(int entityId) {
        EntityId = entityId;
        return this;
    }

    public String getExts(String key) {
        return exts.get(key);
    }

    public EventModel setExts(String name, String value) {
        exts.put(name, value);
        return this;
    }
}
