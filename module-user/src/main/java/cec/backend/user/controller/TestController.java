package cec.backend.user.controller;

import cec.backend.user.dto.test.CreateTestRequest;
import cec.backend.user.dto.test.TestResponse;
import cec.backend.user.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("")
    public TestResponse getTest() {
        return testService.getTest();
    }

    @GetMapping("/{id}")
    public TestResponse getTest(@PathVariable long id) {
        TestResponse test = testService.getTest(id);
        if (test == null) {
            //나중에 throw 를 이용한 예외처리 코드가 들어갈 자리
            return null;
        }
        return test;
    }

    @PostMapping("")
    public TestResponse createTest(@RequestBody CreateTestRequest request) {
        return testService.createTest(request);
    }
}
