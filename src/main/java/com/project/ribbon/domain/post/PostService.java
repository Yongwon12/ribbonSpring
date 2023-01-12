package com.project.ribbon.domain.post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    // 커뮤니티 게시글 작성
    @Transactional
    public Long savePost(final PostRequest params) {
        postMapper.save(params);
        return params.getBoardid();
    }

    // 커뮤니티 게시글 수정
    @Transactional
    public Long updatePost(final PostRequest params) {
        postMapper.update(params);
        return params.getBoardid();
    }

    // 커뮤니티 게시글 삭제
    public Long deletePost(final PostRequest params) {
        postMapper.deleteById(params);
        return params.getBoardid();
    }

    // 커뮤니티 게시글 조회
    public List<PostResponse> findAllPost1() {
        return postMapper.findAll1();
    }
    public List<PostResponse> findAllPost2() {
        return postMapper.findAll2();
    }
    public List<PostResponse> findAllPost3() {
        return postMapper.findAll3();
    }
    public List<PostResponse> findAllPost4() {
        return postMapper.findAll4();
    }
    public List<PostResponse> findAllPost5() {
        return postMapper.findAll5();
    }
    public List<PostResponse> findAllPost6() {
        return postMapper.findAll6();
    }
    public List<PostResponse> findAllPost7() {
        return postMapper.findAll7();
    }
    public List<PostResponse> findAllPost8() {
        return postMapper.findAll8();
    }
    public List<PostResponse> findAllPost9() {
        return postMapper.findAll9();
    }
    public List<PostResponse> findAllPost10() {
        return postMapper.findAll10();
    }
    public List<PostResponse> findAllPost11() {
        return postMapper.findAll11();
    }

    // 단체 게시글 조회
    public List<PostGroupResponse> findGroupAllPost() {
        return postMapper.findGroupAll();
    }
    // 단체 게시글 작성
    @Transactional
    public Long saveGroupPost(final PostGroupRequest params) {
        postMapper.saveGroup(params);
        return params.getGroupid();
    }
    // 단체 게시글 수정
    @Transactional
    public Long updateGroupPost(final PostGroupRequest params) {
        postMapper.updateGroup(params);
        return params.getGroupid();
    }
    // 단체 게시글 삭제
    public Long deleteGroupPost(final PostGroupRequest params) {
        postMapper.deleteByGroupId(params);
        return params.getGroupid();
    }

    // 개인 게시글 조회
    public List<PostIndiResponse> findIndiAllPost() {
        return postMapper.findIndiAll();
    }
    // 개인 게시글 작성
    @Transactional
    public Long saveIndiPost(final PostIndiRequest params) {
        postMapper.saveIndi(params);
        return params.getIndividualid();
    }
    // 개인 게시글 수정
    @Transactional
    public Long updateIndiPost(final PostIndiRequest params) {
        postMapper.updateIndi(params);
        return params.getIndividualid();
    }
    // 개인 게시글 삭제
    public Long deleteIndiPost(final PostIndiRequest params) {
        postMapper.deleteByIndiId(params);
        return params.getIndividualid();
    }
    // 중고 게시글 조회
    public List<PostUsedResponse> findUsedAllPost() {
        return postMapper.findUsedAll();
    }
    // 중고 게시글 작성
    @Transactional
    public Long saveUsedPost(final PostUsedRequest params) {
        postMapper.saveUsed(params);
        return params.getUsedid();
    }
    // 중고 게시글 수정
    @Transactional
    public Long updateUsedPost(final PostUsedRequest params) {
        postMapper.updateUsed(params);
        return params.getUsedid();
    }
    // 중고 게시글 삭제
    public Long deleteUsedPost(final PostUsedRequest params) {
        postMapper.deleteByUsedId(params);
        return params.getUsedid();
    }

    // 유저 정보 조회
    public List<PostUserResponse> findUserAllPost() {
        return postMapper.findUserAll();
    }
    // 유저 정보 가입
    @Transactional
    public Long saveUserPost(final PostUserRequest params) {
        postMapper.saveUser(params);
        return params.getId();
    }
    // 유저 정보 수정
    @Transactional
    public Long updateUserPost(final PostUserRequest params) {
        postMapper.updateUser(params);
        return params.getId();
    }
    // 유저 정보 삭제
    public Long deleteUserPost(final PostUserRequest params) {
        postMapper.deleteByUserId(params);
        return params.getId();
    }
}