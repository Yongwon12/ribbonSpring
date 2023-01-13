package com.project.ribbon.domain.post;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
@ResponseBody
public class PostController {

    private final PostService postService;

    // 커뮤니티 게시글 작성
    @PostMapping("/post/write")
    public Long savePost(@RequestBody PostRequest params) {
        return postService.savePost(params);

    }

    // 커뮤니티 게시글 조회
    @GetMapping("/post/board1")
    public ResponseEntity<?> boardwrite1(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost1();
        model.addAttribute("posts", posts);
        obj.put("boardwrite1", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board2")
    public ResponseEntity<?> boardwrite2(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost2();
        model.addAttribute("posts", posts);
        obj.put("boardwrite2", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board3")
    public ResponseEntity<?> boardwrite3(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost3();
        model.addAttribute("posts", posts);
        obj.put("boardwrite3", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board4")
    public ResponseEntity<?> boardwrite4(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost4();
        model.addAttribute("posts", posts);
        obj.put("boardwrite4", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board5")
    public ResponseEntity<?> boardwrite5(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost5();
        model.addAttribute("posts", posts);
        obj.put("boardwrite5", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board6")
    public ResponseEntity<?> boardwrite6(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost6();
        model.addAttribute("posts", posts);
        obj.put("boardwrite6", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board7")
    public ResponseEntity<?> boardwrite7(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost7();
        model.addAttribute("posts", posts);
        obj.put("boardwrite7", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board8")
    public ResponseEntity<?> boardwrite8(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost8();
        model.addAttribute("posts", posts);
        obj.put("boardwrite8", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board9")
    public ResponseEntity<?> boardwrite9(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost9();
        model.addAttribute("posts", posts);
        obj.put("boardwrite9", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    @GetMapping("/post/board10")
    public ResponseEntity<?> boardwrite10(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost10();
        model.addAttribute("posts", posts);
        obj.put("boardwrite10", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 기존 게시글 수정
    @PostMapping("/post/update")
    public Long updatePost(@RequestBody PostRequest params) {
        return postService.updatePost(params);
    }

    // 기존 게시글 삭제
    @PostMapping("/post/delete")
    public Long deletePost(@RequestBody PostRequest params) {
        return postService.deletePost(params);
    }

    // 단체 작성글 조회
    @GetMapping("/post/group")
    public ResponseEntity<?> group(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupAllPost();
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 단체 글작성
    @PostMapping("/post/writegroup")
    public Long saveGroupPost(@RequestBody PostGroupRequest params) {
        return postService.saveGroupPost(params);

    }

    // 단체 게시글 수정
    @PostMapping("/post/updategroup")
    public Long updateGroupPost(@RequestBody PostGroupRequest params) {
        return postService.updateGroupPost(params);
    }

    // 단체 게시글 삭제
    @PostMapping("/post/deletegroup")
    public Long deleteGroupPost(@RequestBody PostGroupRequest params) {
        return postService.deleteGroupPost(params);
    }

    // 개인 작성글 조회
    @GetMapping("/post/individual")
    public ResponseEntity<?> individual(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiAllPost();
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 개인 글작성
    @PostMapping("/post/writeindividual")
    public Long saveIndiPost(@RequestBody PostIndiRequest params) {
        return postService.saveIndiPost(params);

    }

    // 개인 게시글 수정
    @PostMapping("/post/updateindividual")
    public Long updateIndiPost(@RequestBody PostIndiRequest params) {
        return postService.updateIndiPost(params);
    }

    // 개인 게시글 삭제
    @PostMapping("/post/deleteindividual")
    public Long deleteIndiPost(@RequestBody PostIndiRequest params) {
        return postService.deleteIndiPost(params);
    }

    // 중고 작성글 조회
    @GetMapping("/post/used")
    public ResponseEntity<?> used(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedAllPost();
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 중고 글작성
    @PostMapping("/post/writeused")
    public Long saveUsedPost(@RequestBody PostUsedRequest params) {
        return postService.saveUsedPost(params);

    }

    // 중고 게시글 수정
    @PostMapping("/post/updateused")
    public Long updateUsedPost(@RequestBody PostUsedRequest params) {
        return postService.updateUsedPost(params);
    }

    // 중고 게시글 삭제
    @PostMapping("/post/deleteused")
    public Long deleteUsedPost(@RequestBody PostUsedRequest params) {
        return postService.deleteUsedPost(params);
    }

    // 유저 정보 조회
    @GetMapping("/post/sign")
    public ResponseEntity<?> user(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostUserResponse> posts = postService.findUserAllPost();
        model.addAttribute("posts", posts);
        obj.put("userinfo", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 유저 정보 등록
    @PostMapping("/post/sign")
    public Long saveUserPost(@RequestBody PostUserRequest params) {
        return postService.saveUserPost(params);

    }

    // 유저 정보 수정
    @PostMapping("/post/updateuser")
    public Long updateUserPost(@RequestBody PostUserRequest params) {
        return postService.updateUserPost(params);
    }

    // 유저 정보 삭제
    @PostMapping("/post/deleteuser")
    public Long deleteUserPost(@RequestBody PostUserRequest params) {
        return postService.deleteUserPost(params);
    }

    // 실시간 인기글
    @GetMapping("/post/realtimeup")
    public ResponseEntity<?> realtimeup(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost11();
        model.addAttribute("posts", posts);
        obj.put("bestwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    // 좋아요 등록
    @PostMapping("/post/liked")
    public Integer saveLikedPost(@RequestBody PostLikedRequest params) {

        return postService.saveLikedPost(params);

    }


    // 좋아요 수정
    @PostMapping("/post/updateliked")
    public Integer updateLikedPost(@RequestBody PostLikedRequest params) {
        return postService.updateLikedPost(params);
    }
    @PostMapping("/post/updatedeleteliked")
    public Integer updateLikedDeletePost(@RequestBody PostLikedRequest params) {
        return postService.updateLikedDeletePost(params);
    }
    // 좋아요 삭제
    @PostMapping("/post/deleteliked")
    public Integer deleteLikedPost(@RequestBody PostLikedRequest params) {
        return postService.deleteLikedPost(params);
    }

    // 댓글 조회
    @GetMapping( "/post/commentsinfo")
    public ResponseEntity<?> comments(Model model) {
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsResponse> posts = postService.findCommentsPost();
        model.addAttribute("posts", posts);
        obj.put("comments", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 댓글 등록 및 아이디 전송
    @RequestMapping(method = RequestMethod.POST,path ="/post/comments")
    public ResponseEntity<?> saveComments(@RequestBody PostCommentsRequest params,Model model) {
        postService.saveCommentsPost(params);
        postService.updateDeleteCommentsCountPost(params);
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsIdResponse> posts = postService.findCommentsIdPost();
        model.addAttribute("posts", posts);
        obj.put("commentsid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }

    // 댓글 수정
    @PostMapping("/post/updatecomments")
    public Long updateCommentsPost(@RequestBody PostCommentsRequest params) {
        return postService.updateCommentsPost(params);
    }
    // 댓글 갯수 카운트


    // 댓글 삭제
    @PostMapping("/post/deletecomments")
    public Long deleteCommentsPost(@RequestBody PostCommentsRequest params) {
        return postService.deleteCommentsPost(params);
    }


    // 특정 유저 아이디 받아서 응답
    @RequestMapping(method = RequestMethod.POST,path ="/post/userprofile")
    public ResponseEntity<?> saveUserInfo(@RequestParam TestRequest params,Model model) {
        postService.saveUserinfo(params);
        Map<String, Object> obj = new HashMap<>();
        List<TestResponse> posts = postService.findUserInfoPost();
        model.addAttribute("posts", posts);
        obj.put("UserInfo", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }


}