package cec.backend.user.service;

import cec.backend.user.dto.test.CreateTestRequest;
import cec.backend.user.dto.test.TestResponse;

public interface TestService {
    TestResponse getTest();
    TestResponse getTest(long id);
    TestResponse createTest(CreateTestRequest request);
}
