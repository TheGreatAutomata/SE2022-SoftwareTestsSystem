package com.micro.contractserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JsonService {

    ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> convertJSONtoMap(String json) throws Exception {
        Map<String, Object> ret = new HashMap<>();
        ret = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
        return ret;
    }
}