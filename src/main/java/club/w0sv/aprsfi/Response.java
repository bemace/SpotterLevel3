package club.w0sv.aprsfi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Response {
    
    private String command;
    @JsonDeserialize(using = ResultJsonDeserializer.class)
    private Result result;
    /** if <tt>result == {@link Result#FAIL}</tt>,
     * this will contain a description of the error that occured.
     */
    @JsonProperty("description")
    private String description;
    @JsonDeserialize(using = WhatJsonDeserializer.class)
    private What what;
    @JsonProperty("found")
    private Integer found;
    
    @JsonProperty("entries")
    private List<AprsEntry> entries;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public What getWhat() {
        return what;
    }

    public void setWhat(What what) {
        this.what = what;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }

    public List<AprsEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<AprsEntry> entries) {
        this.entries = entries;
    }
}
