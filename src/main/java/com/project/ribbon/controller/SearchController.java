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
    @PostMapping("/searchwritementor")
    public ResponseEntity<?> searchWriteMentor(@RequestBody PostWritementorDTO query, Model model) {
        try {
            Map<String, Object> obj = new HashMap<>();
            List<PostWritementorDTO> posts = postService.findPostByWriteMentorSearch(query.getQuery());

            if (posts.isEmpty()) {
                obj.put("message", "No posts found for the given query");
                return new ResponseEntity<>(obj, HttpStatus.NOT_FOUND);
            }

            model.addAttribute("posts", posts);
            obj.put("search", posts);

            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorObj = new HashMap<>();
            errorObj.put("message", "An error occurred while searching for write mentors");
            return new ResponseEntity<>(errorObj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

