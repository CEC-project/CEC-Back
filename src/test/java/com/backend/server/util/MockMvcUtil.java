package com.backend.server.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.io.UnsupportedEncodingException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MockMvcUtil {

    private static final ObjectMapper mapper  = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * 이 메소드는 API 응답을 json 으로 파싱하므로, 한번의 API 응답에 대해 여러번 호출하기 부적절합니다.<br>
     * 여러번 호출할거라면, toJsonPathDocument() / toDto 를 사용하세요.
     */
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
        node.forEachEntry((key, value) -> {
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

    public static DocumentContext toJsonPathDocument(ResultActions result) throws UnsupportedEncodingException {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        return JsonPath.parse(responseBody);
    }

    public static <T> T toDto(ResultActions result, TypeReference<T> typeReference) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, typeReference);
    }

    public static MockHttpServletRequestBuilder postJson(String url, Object body) throws JsonProcessingException {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(body));
    }
    public static MockHttpServletRequestBuilder putJson(String url, Object body) throws JsonProcessingException {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(body));
    }
    public static MockHttpServletRequestBuilder patchJson(String url, Object body) throws JsonProcessingException {
        return patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(body));
    }
    public static MockHttpServletRequestBuilder deleteJson(String url, Object body) throws JsonProcessingException {
        return delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJson(body));
    }
}
