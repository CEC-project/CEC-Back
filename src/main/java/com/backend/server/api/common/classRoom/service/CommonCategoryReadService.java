package com.backend.server.api.common.classRoom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.backend.server.api.common.dto.CommonCategoryResponse;
import com.backend.server.model.entity.Category;
import com.backend.server.model.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonCategoryReadService{
    private final CategoryRepository categoryRepository;
    
   //이건 무한스크롤로 프론트단에서 보여줄거고 검색이 필요 없으니 굳이 Pagenation이 필요없고
   //그냥 모든 카테고리를 조회해서 보여줄거라 이렇게 해요
    public List<CommonCategoryResponse> getAllCategories() {
        //원래는 이렇게 코드를 작성하는데 
        //자바에서는 아래처럼 표현할수도 있어요요
        // List<Category> categories = categoryRepository.findAll();
        // List<CommonCategoryResponse> responseList = new ArrayList<>();
        
        // for (int i = 0; i < categories.size(); i++) {
        //     Category category = categories.get(i);
        //     responseList.add(new CommonCategoryResponse(category));
        // }
    
        // return responseList;
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(CommonCategoryResponse::new)
            .collect(Collectors.toList());
    }

    //이건 혹시몰라서 만들어놨아요 나중가서도 안쓰면 지울게요 
    public CommonCategoryResponse getCategory(Long id) {
        //Optional은 값이 있을수도 없을수도 있는 객체를 처리할때 사용하는데
        //findById는 값이 없을수도 있으니까 Optional을 씀.
        //iOS처럼 값을 Optional로 감싸는거에요요
        Optional<Category> category = categoryRepository.findById(id);
        //그리고 옵셔널로 감싸진 category가 비어있는지 확인해요
        if (category.isEmpty()) {
            throw new EntityNotFoundException("카테고리를 찾을 수 없음");
        }
        //그리고 Optional로 감싸진 내용물을 .get메서드로 빼내요.
        //.get메서드는 값이 없는 객체에 사용하면 error가 나서 위에서 isEmpty로 확인해줬어요
        return new CommonCategoryResponse(category.get());
    }
}