package com.movienights.api.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Rating {

    private String Source;
    private String Value;
    @JsonSetter("Source")
    public void setSource(String source) {
        Source = source;
    }
    @JsonSetter("Value")
    public void setValue(String value) {
        Value = value;
    }
    @JsonGetter("Source")
    public String getSource() {
        return Source;
    }
    @JsonGetter("Value")
    public String getValue() {
        return Value;
    }

    public Rating(){}

}
