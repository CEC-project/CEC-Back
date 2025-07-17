package com.backend.server.api.common.s3.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.server.config.ControllerTest;
import com.backend.server.util.S3ApiUtil;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
@DisplayName("CommonPresignedUrlController")
class CommonPresignedUrlControllerTest {

    @Autowired S3ApiUtil s3ApiUtil;
    @Autowired MockMvc mockMvc;

    @Nested
    class Presigned_URL_API_는 {

        @Test
        @IfProfileValue(name = "spring.profiles.active", value = "integration-test")
        void 파일_업로드가_가능한_URL을_응답한다() throws Exception {
            //given
            final String fileName = "asdf.txt";
            final String fileContent = "Hello World!";

            //when
            ResultActions result = mockMvc.perform(get("/api/s3/presigned-url")
                    .param("fileName", fileName));

            //then
            result.andExpect(status().isOk());

            String responseJson = result.andReturn().getResponse().getContentAsString();
            String presignedUrl = JsonPath.parse(responseJson).read("$.data", String.class);

            s3ApiUtil.upload(presignedUrl, fileContent);

            try {
                assertThat(s3ApiUtil.get(fileName))
                        .as("업로드한 파일 내용과 업로드된 파일 내용이 일치하는지 확인합니다.")
                        .isEqualTo(fileContent);
            } finally {
                s3ApiUtil.delete(fileName);
            }
        }
    }
}