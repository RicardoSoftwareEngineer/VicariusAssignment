package io.vicarius.assignment.quota;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Quota")
public class Quota {
    private String id;
    private int totalRequestsMade;

    public Quota() {
    }

    public Quota(String id, int totalRequestsMade) {
        this.id = id;
        this.totalRequestsMade = totalRequestsMade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalRequestsMade() {
        return totalRequestsMade;
    }

    public void setTotalRequestsMade(int totalRequestsMade) {
        this.totalRequestsMade = totalRequestsMade;
    }
}
