package com.ccbfintech.cim.redisclient.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("CCB")
public class Cstinfo implements Serializable {

    @Id
    private String id;
    private String info;

    public Cstinfo(String id, String info) {
        this.id = id;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Cstinfo{" + "id='" + id + '\'' + ", info='" + info + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
