package com.mentor.club.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class ExtendableResult {
    private final Map<String, String> properties = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        return properties;
    }
}
