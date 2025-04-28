package com.backend.server.api.common.controller;


import com.backend.server.api.common.dto.inquiry.InquiryTypeResponse;
import com.backend.server.api.common.service.InquiryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards/inquiry-types")
@RequiredArgsConstructor
public class InquiryTypeController {

    private final InquiryTypeService inquiryTypeService;

    @GetMapping
    public List<InquiryTypeResponse> getInquiryTypes(){
        return inquiryTypeService.getAllInquiryTypes();
    }
}
