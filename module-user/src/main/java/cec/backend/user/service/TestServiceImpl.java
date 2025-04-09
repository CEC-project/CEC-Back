package cec.backend.user.service;

import cec.backend.core.model.entity.Test;
import cec.backend.core.model.repository.TestRepository;
import cec.backend.user.dto.test.CreateTestRequest;
import cec.backend.user.dto.test.TestResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    public TestResponse getTest() {
        //DB에 접근하지 않고 기본 값 반환
        return new TestResponse(0L, "test");
    }

    @Override
    public TestResponse getTest(long id) {
        //DB 에서 id가 일치하는 값을 가져옴
        Optional<Test> test = testRepository.findById(id);

        //DB 에서 가져온 값을 TestResponse 로 변환해서 반환함
        //DB 에서 가져온 값이 비어있으면 null 을 반환함
        return test
                .map(TestResponse::new)
                .orElse(null);
    }

    @Override
    public TestResponse createTest(CreateTestRequest request) {
        //DB 에 값을 저장하고, 그 결과를 entity 변수에 반환받음
        Test entity = testRepository.save(request.toEntity());

        //저장된 값을 TestResponse 로 변환해서 반환
        return new TestResponse(entity);
    }
}
