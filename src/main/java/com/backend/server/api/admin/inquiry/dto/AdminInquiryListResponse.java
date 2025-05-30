package com.backend.server.api.admin.inquiry.dto;

import com.backend.server.api.common.dto.PageableInfo;
import com.backend.server.model.entity.Inquiry;
import com.backend.server.model.entity.InquiryAnswer;
import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.User;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AdminInquiryListResponse {
    private List<AdminInquiryResponse> content;
    private PageableInfo pageable;

    public AdminInquiryListResponse(
            Page<Inquiry> inquiries,
            List<List<InquiryAnswer>> answers,
            List<List<User>> responders,
            List<User> authors,
            List<Professor> professors) {
        pageable = new PageableInfo(inquiries);
        content = IntStream.range(0, inquiries.getNumberOfElements())
                .mapToObj(i -> new AdminInquiryResponse(
                        inquiries.getContent().get(i),
                        answers.get(i),
                        responders.get(i),
                        authors.get(i),
                        professors.get(i)))
                .toList();
    }
}
