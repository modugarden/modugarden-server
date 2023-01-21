package com.modugarden.domain.comment.controller;

import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {
    private CommentRepository commentRepository;
    @PostMapping("/boards/{board_id}/comments")
    public @ResponseBody BaseResponseDto comment(@){

    }
}
