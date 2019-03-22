package com.swift.consumer.model;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwiftData {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private String read_timestamp;
    private String id;
    private String incoming_timestamp;
    private Integer your_name;

    public SwiftData () {
        this.id = UUID.randomUUID().toString();
    }

    @JsonProperty("read_timestamp")
    public String getRead_timestamp() {
        return read_timestamp;
    }

    public void setRead_timestamp(String read_timestamp) {
        this.read_timestamp = read_timestamp;
    }
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    //    public void setId(String id) {
//        this.id = id;
//    }
    @JsonProperty("incoming_timestamp")
    public String getIncoming_timestamp() {
        return incoming_timestamp;
    }

    public void setIncoming_timestamp(String incoming_timestamp) {
        this.incoming_timestamp = incoming_timestamp;
    }
    @JsonProperty("your_name")
    public Integer getYour_name() {
        return your_name;
    }

    public void setYour_name(Integer your_name) {
        this.your_name = your_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SwiftData SwiftData = (SwiftData) o;

        return Objects.equals(read_timestamp, SwiftData.read_timestamp) &&
                Objects.equals(id, SwiftData.id) &&
                Objects.equals(incoming_timestamp, SwiftData.incoming_timestamp) &&

                Objects.equals(your_name, SwiftData.your_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(read_timestamp, id, incoming_timestamp,  your_name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SwiftData {\n");
        sb.append("    read_timestamp: ").append(toIndentedString(read_timestamp)).append("\n");        sb.append("    id: ").append(toIndentedString(id)).append("\n");        sb.append("    incoming_timestamp: ").append(toIndentedString(incoming_timestamp)).append("\n");        sb.append("    your_name: ").append(toIndentedString(your_name)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}