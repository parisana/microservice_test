package com.swift.consumer.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Parisana
 */
public class MongoConfig {

    @JsonIgnore
    private String description;
    private String host;
    private String name;

    public MongoConfig() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
