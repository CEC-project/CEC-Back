package com.backend.server.api.common.s3.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backend.server.config.ControllerTest;
import com.backend.server.util.S3ApiUtil;
import com.jayway.jsonpath.JsonPath;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.EnabledIf;
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
        @EnabledIf(expression = "#{'${spring.profiles.active:default}' == 'integration-test'}", loadContext = true)
        void 파일_업로드가_가능한_URL을_응답한다() throws Exception {
            /* presigned url 을 발급받습니다. */
            //given
            final String fileContent = LocalDateTime.now().toString();
            final String fileName = String.format("%s.txt", fileContent);
            System.out.printf("file name : %s\tfile content : %s\n", fileName, fileContent);

            //when
            ResultActions result = mockMvc.perform(get("/api/s3/presigned-url")
                    .param("fileName", fileName));

            //then
            result.andExpect(status().isOk());

            String responseJson = result.andReturn().getResponse().getContentAsString();
            String presignedUrl = JsonPath.parse(responseJson).read("$.data", String.class);
            /* presigned url 을 발급받습니다. */

            /* presigned url 로 파일이 잘 업로드 되는지 테스트 */
            //given
            s3ApiUtil.upload(presignedUrl, fileContent);

            try {
                //when
                ResponseEntity<String> response = s3ApiUtil.get(fileName);

                //then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody())
                        .as("업로드한 파일 내용과 업로드된 파일 내용이 일치하는지 확인합니다.")
                        .isEqualTo(fileContent);
            } finally {
                // 테스트로 업로드한 파일을 삭제하고, 잘 삭제되었는지 확인합니다.
                s3ApiUtil.delete(fileName);

                assertThat(s3ApiUtil.get(fileName))
                        .as("테스트용 파일이 삭제되었는지 확인합니다.")
                        .isNull();
            }
            /* presigned url 로 파일이 잘 업로드 되는지 테스트 */
        }
    }
}