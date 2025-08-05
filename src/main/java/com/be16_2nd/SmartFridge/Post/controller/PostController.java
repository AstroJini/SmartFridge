package com.be16_2nd.SmartFridge.Post.controller;

import com.be16_2nd.SmartFridge.Post.dto.PostCreateDto;
import com.be16_2nd.SmartFridge.Post.service.PostService;
import com.be16_2nd.SmartFridge.common.dto.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@ModelAttribute PostCreateDto postCreateDto) {
        Long id = postService.createPost(postCreateDto);
        return new  ResponseEntity<>(
                CommonDto.builder()
                        .result(id)
                        .status_code(HttpStatus.CREATED.value())
                        .status_message("게시글등록완료")
                        .build()
                , HttpStatus.CREATED);
    }
}
