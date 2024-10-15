package com.bgaebalja.blogbackend.post.controller;

import com.bgaebalja.blogbackend.post.domain.CompletePostRequest;
import com.bgaebalja.blogbackend.post.domain.GetPostResponse;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import com.bgaebalja.blogbackend.post.service.PostService;
import com.bgaebalja.blogbackend.util.FormatValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.bgaebalja.blogbackend.util.ApiConstant.ID_EXAMPLE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시글 관련 API")
public class PostController {
    private final PostService postService;

    private static final String REGISTER_POST = "게시글 임시 등록";
    private static final String REGISTER_POST_DESCRIPTION
            = "회원 이메일 주소와 게시글 내용을 입력해 게시글을 임시 등록할 수 있습니다.";
    private static final String REGISTER_POST_FORM = "게시글 임시 등록 양식";

    private static final String COMPLETE_POST = "게시글 작성 완료";
    private static final String COMPLETE_POST_DESCRIPTION
            = "게시글 ID와 게시글 내용을 입력해 게시글을 작성 완료할 수 있습니다.";
    private static final String COMPLETE_POST_FORM = "게시글 작성 완료 양식";

    private static final String GET_POST = "게시글 조회";
    private static final String GET_POST_DESCRIPTION = "게시글 ID를 입력해 게시글을 조회할 수 있습니다.";
    public static final String POST_ID = "게시글 ID";

    @Operation(summary = REGISTER_POST, description = REGISTER_POST_DESCRIPTION)
    @PostMapping()
    public ResponseEntity<Void> registerPost(
            @Valid @RequestBody @Parameter(description = REGISTER_POST_FORM) RegisterPostRequest registerPostRequest
    ) {
        FormatValidator.validateEmail(registerPostRequest.getEmail());

        return buildResponse(postService.createPost(registerPostRequest));
    }

    private ResponseEntity<Void> buildResponse(Long id) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(CREATED).headers(headers).build();
    }

    @Operation(summary = COMPLETE_POST, description = COMPLETE_POST_DESCRIPTION)
    @PatchMapping()
    public ResponseEntity<Void> completePost(
            @Valid @RequestBody @Parameter(description = COMPLETE_POST_FORM) CompletePostRequest completePostRequest
    ) {
        FormatValidator.validateId(completePostRequest.getId());
        postService.completePost(completePostRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @Operation(summary = GET_POST, description = GET_POST_DESCRIPTION)
    @GetMapping("/{id}")
    public ResponseEntity<GetPostResponse> getPost
            (@PathVariable("id") @Parameter(description = POST_ID, example = ID_EXAMPLE) String id) {
        FormatValidator.validateId(id);
        Post post = postService.getPost(Long.parseLong(id));

        return ResponseEntity.status(HttpStatus.OK).body(GetPostResponse.from(post));
    }
}