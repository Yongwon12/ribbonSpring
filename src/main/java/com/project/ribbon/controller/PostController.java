package com.project.ribbon.controller;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.enums.ExceptionEnum;

import com.project.ribbon.response.ApiException;
import com.project.ribbon.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("api")
@ResponseBody
@Slf4j
@RestController

public class PostController {

    private final PostService postService;
    private final FirebaseCloudMessageLikedService firebaseCloudMessageLikedService;
    private final FirebaseCloudMessageCommentsService firebaseCloudMessageCommentsService;
    private final FirebaseCloudChatMessageService firebaseCloudChatMessageService;





    // 커뮤니티 게시글 작성
    @PostMapping("/post/boardwrite")
    public ResponseEntity<?> savePost(@RequestBody @Valid PostRequest params) throws ApiException{
        try {
            Long postId = postService.savePost(params);
            return new ResponseEntity<>(postId, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 커뮤니티 게시글 조회
    @GetMapping("/board")
    public ResponseEntity<?> boardwrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost();
        model.addAttribute("posts", posts);
        obj.put("boardwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 기존 게시글 수정
    @PostMapping("/post/update")
    public Long updatePost(@RequestBody PostRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updatePost(params);
    }

    // 기존 게시글 삭제
    @RequestMapping("/post/delete")
    public Long deletePost(@RequestBody PostRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.deletePost(params);
    }

    // 단체 작성글 조회
    @GetMapping("/group")
    public ResponseEntity<?> groupWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupResponse> posts = postService.findGroupAllPost();
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 단체 글작성
    @PostMapping("/post/writegroup")
    public ResponseEntity<?> saveGroupPost(@RequestBody @Valid PostGroupRequest params) {
        try {
            Long id = postService.saveGroupPost(params);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 단체 게시글 수정
    @PostMapping("/post/updategroup")
    public Long updateGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateGroupPost(params);
    }

    // 단체 게시글 삭제
    @RequestMapping("/post/deletegroup")
    public Long deleteGroupPost(@RequestBody PostGroupRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.deleteGroupPost(params);
    }

    // 개인 작성글 조회
    @GetMapping("/individual")
    public ResponseEntity<?> indiWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiResponse> posts = postService.findIndiAllPost();
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 개인 글작성
    @PostMapping("/post/writeindividual")
    public ResponseEntity<?> saveIndiPost(@RequestBody @Valid PostIndiRequest params) throws ApiException {
        try {
            Long id = postService.saveIndiPost(params);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 게시글 수정
    @PostMapping("/post/updateindividual")
    public Long updateIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateIndiPost(params);
    }

    // 개인 게시글 삭제
    @RequestMapping("/post/deleteindividual")
    public Long deleteIndiPost(@RequestBody PostIndiRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.deleteIndiPost(params);
    }

    // 중고 작성글 조회
    @GetMapping("/used")
    public ResponseEntity<?> usedWrite(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedResponse> posts = postService.findUsedAllPost();
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 중고 글작성
    @PostMapping("/post/writeused")
    public ResponseEntity<?> saveUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        try {
            Long id = postService.saveUsedPost(params);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 중고 게시글 수정
    @PostMapping("/post/updateused")
    public Long updateUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateUsedPost(params);
    }

    // 중고 게시글 삭제
    @RequestMapping("/post/deleteused")
    public Long deleteUsedPost(@RequestBody PostUsedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.deleteUsedPost(params);
    }

    // 유저 정보 조회
    @GetMapping("/sign")
    public ResponseEntity<?> user(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostUserResponse> posts = postService.findUserAllPost();
        model.addAttribute("posts", posts);
        obj.put("userinfo", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 유저 정보 등록
    @PostMapping("/post/sign")
    public ResponseEntity<?> saveUserPost(@RequestBody @Valid PostUserRequest params, Model model) throws ApiException {
        try {
            postService.saveUserPost(params);
            Map<String, Object> obj = new HashMap<>();
            List<PostUserResponse> posts = postService.findUserAllPost();
            model.addAttribute("posts", posts);
            obj.put("userid", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    // 유저 정보 수정
    @PostMapping("/post/updateuser")
    public ResponseEntity<?> updateUserPost(@RequestBody @Valid PostUserUpdateRequest params) {
        try {
            List<PostUserResponse> posts = postService.findUserAllPost();
            return new ResponseEntity<>(postService.updateUserPost(params), HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 유저 정보 삭제
    @PostMapping("/post/deleteuser")
    public ResponseEntity<?> deleteUserPost(@RequestBody PostUserRequest params) {
        try {
            return new ResponseEntity<>(postService.deleteUserPost(params), HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 실시간 인기글
    @GetMapping("/realtimeup")
    public ResponseEntity<?> realTimeUp(Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        Map<String, Object> obj = new HashMap<>();
        List<PostResponse> posts = postService.findAllPost11();
        model.addAttribute("posts", posts);
        obj.put("bestwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    // 좋아요 등록
    @PostMapping("/post/liked")
    public Integer saveLikedPost(@RequestBody PostLikedRequest params) throws ApiException,IOException{
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateLikedPost(params);
        postService.saveLikedPost(params);
        firebaseCloudMessageLikedService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        return ResponseEntity.ok().build().getStatusCodeValue();

    }


    // 좋아요 삭제
    @RequestMapping("/post/deleteliked")
    public Integer deleteLikedPost(@RequestBody PostLikedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteLikedPost(params);
        return postService.deleteLikedPost(params);
    }

    // 개인 좋아요 등록
    @PostMapping("/post/individualliked")
    public Integer saveIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException,IOException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateIndividualLikedPost(params);
        postService.saveIndividualLikedPost(params);
        firebaseCloudMessageLikedService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        return ResponseEntity.ok().build().getStatusCodeValue();

    }


    // 개인 좋아요 삭제
    @RequestMapping("/post/deleteindividualliked")
    public Integer deleteIndiLikedPost(@RequestBody PostIndividualLikedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteIndividualLikedPost(params);
        return postService.deleteIndividualLikedPost(params);
    }

    // 중고 좋아요 등록
    @PostMapping("/post/usedliked")
    public Integer saveUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException ,IOException{
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateUsedLikedPost(params);
        postService.saveUsedLikedPost(params);
        firebaseCloudMessageLikedService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        return ResponseEntity.ok().build().getStatusCodeValue();

    }


    // 중고 좋아요 삭제
    @RequestMapping("/post/deleteusedliked")
    public Integer deleteUsedLikedPost(@RequestBody PostUsedLikedRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteUsedLikedPost(params);
        return postService.deleteUsedLikedPost(params);
    }

    // 댓글 조회
    @RequestMapping("/post/commentsinfo")
    public ResponseEntity<?> commentsInfo(@RequestBody PostCommentsResponse inherentid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByInherentId(inherentid.getInherentid());
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsResponse> posts = postService.findPostByInherentId(inherentid.getInherentid());
        model.addAttribute("posts", posts);
        obj.put("comment", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 댓글 등록 및 아이디 전송
    @RequestMapping(method = RequestMethod.POST,path ="/post/comments")
    public ResponseEntity<?> saveComments(@RequestBody PostCommentsRequest params,Model model) throws ApiException ,IOException{
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.saveCommentsPost(params);
        postService.updateCommentsCountPost(params);
        firebaseCloudMessageCommentsService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        ResponseEntity.ok().build().getStatusCodeValue();
        Map<String, Object> obj = new HashMap<>();
        List<PostCommentsIdResponse> posts = postService.findCommentsIdPost();
        model.addAttribute("posts", posts);
        obj.put("commentsid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }

    // 댓글 수정
    @PostMapping("/post/updatecomments")
    public Long updateCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateCommentsPost(params);
    }

    // 댓글 삭제
    @RequestMapping("/post/deletecomments")
    public Long deleteCommentsPost(@RequestBody PostCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteCommentsCountPost(params);
        return postService.deleteCommentsPost(params);
    }

    // 개인 댓글 조회
    @RequestMapping("/post/indicommentsinfo")
    public ResponseEntity<?> indiCommentsInfo(@RequestBody PostIndiCommentsResponse inherentid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByIndiCommentsInherentId(inherentid.getInherentid());
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiCommentsResponse> posts = postService.findPostByIndiCommentsInherentId(inherentid.getInherentid());
        model.addAttribute("posts", posts);
        obj.put("comment", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 개인 댓글 등록 및 아이디 전송
    @RequestMapping(method = RequestMethod.POST,path ="/post/indicomments")
    public ResponseEntity<?> saveIndiComments(@RequestBody PostIndiCommentsRequest params,Model model) throws ApiException,IOException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.saveIndiCommentsPost(params);
        postService.updateIndiCommentsCountPost(params);
        firebaseCloudMessageCommentsService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        ResponseEntity.ok().build().getStatusCodeValue();
        Map<String, Object> obj = new HashMap<>();
        List<PostIndiCommentsIdResponse> posts = postService.findIndiCommentsIdPost();
        model.addAttribute("posts", posts);
        obj.put("commentsid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }

    // 개인 댓글 수정
    @PostMapping("/post/updateindicomments")
    public Long updateIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateIndiCommentsPost(params);
    }

    // 개인 댓글 삭제
    @RequestMapping("/post/deleteindicomments")
    public Long deleteIndiCommentsPost(@RequestBody PostIndiCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteIndiCommentsCountPost(params);
        return postService.deleteIndiCommentsPost(params);
    }

    // 단체 댓글 조회
    @RequestMapping("/post/groupcommentsinfo")
    public ResponseEntity<?> groupCommentsInfo(@RequestBody PostGroupCommentsResponse inherentid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByGroupCommentsInherentId(inherentid.getInherentid());
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupCommentsResponse> posts = postService.findPostByGroupCommentsInherentId(inherentid.getInherentid());
        model.addAttribute("posts", posts);
        obj.put("comment", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 단체 댓글 등록 및 아이디 전송
    @RequestMapping(method = RequestMethod.POST,path ="/post/groupcomments")
    public ResponseEntity<?> saveGroupComments(@RequestBody PostGroupCommentsRequest params,Model model) throws ApiException,IOException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.saveGroupCommentsPost(params);
        postService.updateGroupCommentsCountPost(params);
        firebaseCloudMessageCommentsService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        ResponseEntity.ok().build().getStatusCodeValue();
        Map<String, Object> obj = new HashMap<>();
        List<PostGroupCommentsIdResponse> posts = postService.findGroupCommentsIdPost();
        model.addAttribute("posts", posts);
        obj.put("commentsid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }

    // 단체 댓글 수정
    @PostMapping("/post/updategroupcomments")
    public Long updateGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateGroupCommentsPost(params);
    }

    // 단체 댓글 삭제
    @RequestMapping("/post/deletegroupcomments")
    public Long deleteGroupCommentsPost(@RequestBody PostGroupCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteGroupCommentsCountPost(params);
        return postService.deleteGroupCommentsPost(params);
    }


    // 중고 댓글 조회
    @RequestMapping("/post/usedcommentsinfo")
    public ResponseEntity<?> usedcommentsinfo(@RequestBody PostUsedCommentsResponse inherentid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedCommentsResponse> posts = postService.findPostByUsedCommentsInherentId(inherentid.getInherentid());
        model.addAttribute("posts", posts);
        obj.put("comment", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 중고 댓글 등록 및 아이디 전송
    @RequestMapping(method = RequestMethod.POST,path ="/post/usedcomments")
    public ResponseEntity<?> saveUsedComments(@RequestBody PostUsedCommentsRequest params,Model model) throws ApiException,IOException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.saveUsedCommentsPost(params);
        postService.updateUsedCommentsCountPost(params);
        firebaseCloudMessageCommentsService.sendMessageTo(
                params.getToken(),
                params.getNickname());
        ResponseEntity.ok().build().getStatusCodeValue();
        Map<String, Object> obj = new HashMap<>();
        List<PostUsedCommentsIdResponse> posts = postService.findUsedCommentsIdPost();
        model.addAttribute("posts", posts);
        obj.put("commentsid", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);

    }

    // 중고 댓글 수정
    @PostMapping("/post/updateusedcomments")
    public Long updateUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.updateUsedCommentsPost(params);
    }

    // 중고 댓글 삭제
    @RequestMapping("/post/deleteusedcomments")
    public Long deleteUsedCommentsPost(@RequestBody PostUsedCommentsRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.updateDeleteUsedCommentsCountPost(params);
        return postService.deleteUsedCommentsPost(params);
    }




//    // 답글 조회
//    @RequestMapping("/post/replyinfo")
//    public ResponseEntity<?> replyInfo(@RequestBody PostReplyResponse inherentid, Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.findPostByReplyInherentId(inherentid.getInherentid());
//        Map<String, Object> obj = new HashMap<>();
//        List<PostReplyResponse> posts = postService.findPostByReplyInherentId(inherentid.getInherentid());
//        model.addAttribute("posts", posts);
//        obj.put("reply", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//    }
//
//
//    // 답글 등록 및 아이디 전송
//    @RequestMapping(method = RequestMethod.POST,path ="/post/reply")
//    public ResponseEntity<?> saveReply(@RequestBody PostReplyRequest params,Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.saveReplyPost(params);
//        postService.updateReplyCountPost(params);
//        Map<String, Object> obj = new HashMap<>();
//        List<PostReplyIdResponse> posts = postService.findReplyIdPost();
//        model.addAttribute("posts", posts);
//        obj.put("replyid", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//
//    }
//
//    // 답글 수정
//    @PostMapping("/post/updatereply")
//    public Long updateReplyPost(@RequestBody PostReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        return postService.updateReplyPost(params);
//    }
//
//    // 답글 삭제
//    @RequestMapping("/post/deletereply")
//    public Long deleteReplyPost(@RequestBody PostReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.updateDeleteReplyCountPost(params);
//        return postService.deleteReplyPost(params);
//    }
//
//    // 개인 답글 조회
//    @RequestMapping("/post/indireplyinfo")
//    public ResponseEntity<?> indiReplyInfo(@RequestBody PostIndiReplyResponse inherentid, Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.findPostByIndiReplyInherentId(inherentid.getInherentid());
//        Map<String, Object> obj = new HashMap<>();
//        List<PostIndiReplyResponse> posts = postService.findPostByIndiReplyInherentId(inherentid.getInherentid());
//        model.addAttribute("posts", posts);
//        obj.put("reply", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//    }
//
//
//    // 개인 답글 등록 및 아이디 전송
//    @RequestMapping(method = RequestMethod.POST,path ="/post/indireply")
//    public ResponseEntity<?> saveIndiReply(@RequestBody PostIndiReplyRequest params,Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.saveIndiReplyPost(params);
//        postService.updateIndiReplyCountPost(params);
//        Map<String, Object> obj = new HashMap<>();
//        List<PostIndiReplyIdResponse> posts = postService.findIndiReplyIdPost();
//        model.addAttribute("posts", posts);
//        obj.put("replyid", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//
//    }
//
//    // 개인 답글 수정
//    @PostMapping("/post/updateindireply")
//    public Long updateIndiReplyPost(@RequestBody PostIndiReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        return postService.updateIndiReplyPost(params);
//    }
//
//    // 개인 답글 삭제
//    @RequestMapping("/post/deleteindireply")
//    public Long deleteIndiReplyPost(@RequestBody PostIndiReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.updateDeleteIndiReplyCountPost(params);
//        return postService.deleteIndiReplyPost(params);
//    }
//
//    // 단체 답글 조회
//    @RequestMapping("/post/groupreplyinfo")
//    public ResponseEntity<?> groupReplyInfo(@RequestBody PostGroupReplyResponse inherentid, Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.findPostByGroupReplyInherentId(inherentid.getInherentid());
//        Map<String, Object> obj = new HashMap<>();
//        List<PostGroupReplyResponse> posts = postService.findPostByGroupReplyInherentId(inherentid.getInherentid());
//        model.addAttribute("posts", posts);
//        obj.put("reply", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//    }
//
//
//    // 단체 답글 등록 및 아이디 전송
//    @RequestMapping(method = RequestMethod.POST,path ="/post/groupreply")
//    public ResponseEntity<?> saveGroupReply(@RequestBody PostGroupReplyRequest params,Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.saveGroupReplyPost(params);
//        postService.updateGroupReplyCountPost(params);
//        Map<String, Object> obj = new HashMap<>();
//        List<PostGroupReplyIdResponse> posts = postService.findGroupReplyIdPost();
//        model.addAttribute("posts", posts);
//        obj.put("replyid", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//
//    }
//
//    // 단체 답글 수정
//    @PostMapping("/post/updategroupreply")
//    public Long updateGroupReplyPost(@RequestBody PostGroupReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        return postService.updateGroupReplyPost(params);
//    }
//
//    // 단체 답글 삭제
//    @RequestMapping("/post/deletegroupreply")
//    public Long deleteGroupReplyPost(@RequestBody PostGroupReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.updateDeleteGroupReplyCountPost(params);
//        return postService.deleteGroupReplyPost(params);
//    }
//
//
//
//    // 중고 답글 조회
//    @RequestMapping("/post/usedreplyinfo")
//    public ResponseEntity<?> usedReplyInfo(@RequestBody PostUsedReplyResponse inherentid, Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.findPostByUsedReplyInherentId(inherentid.getInherentid());
//        Map<String, Object> obj = new HashMap<>();
//        List<PostUsedReplyResponse> posts = postService.findPostByUsedReplyInherentId(inherentid.getInherentid());
//        model.addAttribute("posts", posts);
//        obj.put("reply", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//    }
//
//
//    // 중고 답글 등록 및 아이디 전송
//    @RequestMapping(method = RequestMethod.POST,path ="/post/usedreply")
//    public ResponseEntity<?> saveUsedReply(@RequestBody PostUsedReplyRequest params,Model model) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.saveUsedReplyPost(params);
//        postService.updateUsedReplyCountPost(params);
//        Map<String, Object> obj = new HashMap<>();
//        List<PostUsedReplyIdResponse> posts = postService.findUsedReplyIdPost();
//        model.addAttribute("posts", posts);
//        obj.put("replyid", posts);
//        return new ResponseEntity<>(obj, HttpStatus.OK);
//
//    }
//
//    // 중고 답글 수정
//    @PostMapping("/post/updateusedreply")
//    public Long updateUsedReplyPost(@RequestBody PostUsedReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        return postService.updateUsedReplyPost(params);
//    }
//
//    // 중고 답글 삭제
//    @RequestMapping("/post/deleteusedreply")
//    public Long deleteUsedReplyPost(@RequestBody PostUsedReplyRequest params) throws ApiException {
//        ExceptionEnum err = ExceptionEnum.SECURITY_01;
//        postService.updateDeleteUsedReplyCountPost(params);
//        return postService.deleteUsedReplyPost(params);
//    }




    // 특정 유저 프로필 조회
    @PostMapping("/post/userinfo")
    public ResponseEntity<?> openPostView(@RequestBody UserInfoResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostById(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<UserInfoResponse> posts = postService.findPostById(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("userinfo", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    // 내가 쓴 글 커뮤니티
    @PostMapping("/post/myboardwrite")
    public ResponseEntity<?> myBoardWrite(@RequestBody PostMyBoardResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyBoardResponse> posts = postService.findPostByMyUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("myboard", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 내가 쓴 글 단체
    @PostMapping("/post/mygroupwrite")
    public ResponseEntity<?> myBoardWrite(@RequestBody PostMyGroupResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyGroupUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyGroupResponse> posts = postService.findPostByMyGroupUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("groupwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    // 내가 쓴 글 개인
    @PostMapping("/post/myindividualwrite")
    public ResponseEntity<?> myBoardWrite(@RequestBody PostMyIndiResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyIndividualUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyIndiResponse> posts = postService.findPostByMyIndividualUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 내가 쓴 글 중고
    @PostMapping("/post/myusedwrite")
    public ResponseEntity<?> myBoardWrite(@RequestBody PostMyUsedResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyUsedUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyUsedResponse> posts = postService.findPostByMyUsedUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 내가 좋아요 누른 글 커뮤니티
    @PostMapping("/post/myboardliked")
    public ResponseEntity<?> myBoardLiked(@RequestBody PostMyLikedResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyLikedUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyLikedResponse> posts = postService.findPostByMyLikedUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("myboard", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 내가 좋아요 누른 글 개인
    @PostMapping("/post/myindividualliked")
    public ResponseEntity<?> myIndividualLiked(@RequestBody PostMyIndividualLikedResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyIndividualLikedUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyIndividualLikedResponse> posts = postService.findPostByMyIndividualLikedUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("individualwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }
    // 내가 좋아요 누른 글 중고
    @PostMapping("/post/myusedliked")
    public ResponseEntity<?> myUsedLiked(@RequestBody PostMyUsedLikedResponse userid, Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        postService.findPostByMyUsedLikedUserId(userid.getUserid());
        Map<String, Object> obj = new HashMap<>();
        List<PostMyUsedLikedResponse> posts = postService.findPostByMyUsedLikedUserId(userid.getUserid());
        model.addAttribute("posts", posts);
        obj.put("usedwrite", posts);
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }


    // 채팅방 넣기
    @PostMapping("/post/chatroom")
    public Long saveChatRoomPost(@RequestBody  PostChatRoomRequest params) throws ApiException{
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.saveChatRoomPost(params);

    }

    // 특정 채팅방 조회
    @PostMapping("/post/chatroominfo")
    public ResponseEntity<?> viewChatRoomPost(@RequestBody PostChatRoomResponse myid,Model model) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
            postService.findPostByMyId(myid.getMyid());
            Map<String, Object> obj = new HashMap<>();
            List<PostChatRoomResponse> posts = postService.findPostByMyId(myid.getMyid());
            model.addAttribute("posts", posts);
            obj.put("chatroom", posts);
            return new ResponseEntity<>(obj, HttpStatus.OK);
    }



    // 특정 채팅방 삭제
    @RequestMapping("/post/deleteroom")
    public String deleteChatRoomPost(@RequestBody PostChatRoomDeleteRequest params) throws ApiException {
        ExceptionEnum err = ExceptionEnum.RUNTIME_EXCEPTION;
        return postService.deleteChatRoomPost(params);
    }

    // 채팅 넣기
    @PostMapping("/chat")
    public String pushChatPost(@RequestBody ResponseDTO responseDTO)throws IOException{
        firebaseCloudChatMessageService.sendMessageTo(
                responseDTO.getToken(),
                responseDTO.getNickname());
        return String.valueOf(ResponseEntity.ok().build().getStatusCodeValue());

    }



}