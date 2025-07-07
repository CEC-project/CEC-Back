package com.backend.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MockMvcUtil {

    private static final ObjectMapper mapper  = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static ResultMatcher jsonPathEquals(String jsonPath, Object expected) {
        return result -> {
            String responseBody = result.getResponse().getContentAsString();
            Object actualValue = JsonPath.read(responseBody, jsonPath);
            JsonNode actualNode = mapper.valueToTree(actualValue);
            JsonNode expectedNode = mapper.valueToTree(expected);

            System.out.println("expectedNode = " + mapper.writeValueAsString(expectedNode));
            System.out.println("actualNode   = " + mapper.writeValueAsString(actualNode));
            System.out.println("equals?      = " + expectedNode.equals(actualNode));

            if (!expectedNode.toString().equals(actualNode.toString())) {
                throw new AssertionError("Expected: " + expectedNode + "\nActual: " + actualNode);
            }
        };
    }

    public static String convertToJson(Object dto) throws JsonProcessingException {
        return mapper.writeValueAsString(dto);
    }

    public static MultiValueMap<String, String> convertToParams(Object dto) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        JsonNode node = mapper.valueToTree(dto);
        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            if (!value.isNull()) {
                if (value.isArray()) {
                    for (JsonNode item : value)
                        params.add(key, item.asText());
                } else {
                    params.add(key, value.asText());
                }
            }
        });

        return params;
    }
}
