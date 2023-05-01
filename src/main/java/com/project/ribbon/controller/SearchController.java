package com.project.ribbon.controller;

import com.project.ribbon.domain.post.PostWritementorDTO;
import com.project.ribbon.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@ResponseBody
@Slf4j
@RestController
@Validated
@ControllerAdvice
public class SearchController {
    private final PostService postService;
    @GetMapping("/searchwritementor")
    public ResponseEntity<?> searchWritementors(@RequestParam("q") String query, Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostWritementorDTO> posts = postService.findPostByWriteMentorSearch(query);
        model.addAttribute("posts", posts);
        obj.put("search", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

}

