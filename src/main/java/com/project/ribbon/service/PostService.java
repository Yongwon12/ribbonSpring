package com.project.ribbon.service;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.mapper.PostMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
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
    public List<PostResponse> findAllPost() {
        return postMapper.findAll();
    }

    public List<PostResponse> findAllPost11() {
        return postMapper.findAll11();
    }

    // 커뮤니티 특정 게시글 조회
    public List<PostResponse> findOnePost(Long boardid) {
        return postMapper.findOne(boardid);
    }

    // 단체 게시글 조회
    public List<PostGroupResponse> findGroupAllPost() {
        return postMapper.findGroupAll();
    }

    // 단체 게시글 조회
    public List<PostGroupResponse> findGroupOnePost(Long groupid) {
        return postMapper.findGroupOne(groupid);
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

    // 개인 특정 게시글 조회
    public List<PostIndiResponse> findIndiOnePost(Long individualid) {
        return postMapper.findIndiOne(individualid);
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
    // 중고 특정 게시글 조회
    public List<PostUsedResponse> findUsedOnePost(Long usedid) {
        return postMapper.findUsedOne(usedid);
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
    // 기존 유저 정보 조회
    public List<PostUserRequest> findUserInfoAllPost(String email) {
        return postMapper.findUserInfoAll(email);
    }



    // 유저 정보 가입
    @Transactional
    public Long saveUserPost(final PostUserRequest params) {
        postMapper.saveUser(params);
        return params.getUserid();
    }




    // 유저 권한 정보 가입
    @Transactional
    public Long saveUserRolesPost(final PostUserRequest params) {
        postMapper.saveUserRoles(params);
        return params.getUserid();
    }

    // 유저 정보 수정
    @Transactional
    public String updateUserPost(final PostUserUpdateRequest params) {
        postMapper.updateUser(params);
        return null;
    }
    // 유저 수정된 프로필 사진
    public PostUserUpdateRequest findUserImagePost(Long userid) {
        return postMapper.findUserImage(userid);
    }



    // 유저 정보 삭제
    public Long deleteUserPost(final PostUserRequest params) {
        postMapper.deleteByUserId(params);
        return params.getUserid();
    }

    // 유저 정보 삭제
    public Long deleteUserRolesPost(final PostUserRequest params) {
        postMapper.deleteByUserRolesId(params);
        return params.getUserid();
    }


    // 좋아요 삽입
    @Transactional
    public Integer saveLikedPost(final PostLikedRequest params) {
        postMapper.saveLiked(params);
        return params.getInherentid();
    }


    // 좋아요 정보 수정
    @Transactional
    public Integer updateLikedPost(final PostLikedRequest params) {
        postMapper.updateLiked(params);
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteLikedPost(final PostLikedRequest params) {
        postMapper.updateDeleteLiked(params);
        return params.getInherentid();
    }
    // 좋아요 정보 삭제
    public Integer deleteLikedPost(final PostLikedRequest params) {
        postMapper.deleteByLikedId(params);
        return params.getInherentid();
    }
    // 좋아요 개인 삽입
    @Transactional
    public Integer saveIndividualLikedPost(final PostIndividualLikedRequest params) {
        postMapper.saveIndividualLiked(params);
        return params.getInherentid();
    }
    // 좋아요 개인 정보 수정
    @Transactional
    public Integer updateIndividualLikedPost(final PostIndividualLikedRequest params) {
        postMapper.updateIndividualLiked(params);
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        postMapper.updateDeleteIndividualLiked(params);
        return params.getInherentid();
    }
    // 좋아요 개인 정보 삭제
    public Integer deleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        postMapper.deleteByIndividualLikedId(params);
        return params.getInherentid();
    }
    // 좋아요 중고 삽입
    @Transactional
    public Integer saveUsedLikedPost(final PostUsedLikedRequest params) {
        postMapper.saveUsedLiked(params);
        return params.getInherentid();
    }
    // 좋아요 중고 정보 수정
    @Transactional
    public Integer updateUsedLikedPost(final PostUsedLikedRequest params) {
        postMapper.updateUsedLiked(params);
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteUsedLikedPost(final PostUsedLikedRequest params) {
        postMapper.updateDeleteUsedLiked(params);
        return params.getInherentid();
    }
    // 좋아요 중고 정보 삭제
    public Integer deleteUsedLikedPost(final PostUsedLikedRequest params) {
        postMapper.deleteByUsedLikedId(params);
        return params.getInherentid();
    }


    // 댓글 정보 조회
    public List<PostCommentsResponse> findPostByInherentId(final Long inherentid) {
        return postMapper.findByInherentId(inherentid);
    }
    // 댓글 작성자 아이디 조회
    public List<PostCommentsIdResponse> findCommentsIdPost() {
        return postMapper.findCommentsId();
    }

    // 댓글 기입
    @Transactional
    public Long saveCommentsPost(final PostCommentsRequest params) {
        postMapper.saveComments(params);
        return params.getInherentid();
    }
    // 댓글 정보 수정
    @Transactional
    public Long updateCommentsPost(final PostCommentsRequest params) {
        postMapper.updateComments(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateCommentsCountPost(final PostCommentsRequest params) {
        postMapper.updateCommentsCount(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteCommentsCountPost(final PostCommentsRequest params) {
        postMapper.updateDeleteCommentsCount(params);
        return params.getInherentid();
    }
    // 댓글 정보 삭제
    public Long deleteCommentsPost(final PostCommentsRequest params) {
        postMapper.deleteComments(params);
        return params.getInherentid();
    }

    // 개인 댓글 정보 조회
    public List<PostIndiCommentsResponse> findPostByIndiCommentsInherentId(final Long inherentid) {
        return postMapper.findByIndiCommentsInherentId(inherentid);
    }
    // 개인 댓글 작성자 아이디 조회
    public List<PostIndiCommentsIdResponse> findIndiCommentsIdPost() {
        return postMapper.findIndiCommentsId();
    }

    // 개인 댓글 기입
    @Transactional
    public Long saveIndiCommentsPost(final PostIndiCommentsRequest params) {
        postMapper.saveIndiComments(params);
        return params.getInherentid();
    }
    // 개인 댓글 정보 수정
    @Transactional
    public Long updateIndiCommentsPost(final PostIndiCommentsRequest params) {
        postMapper.updateIndiComments(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        postMapper.updateIndiCommentsCount(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        postMapper.updateDeleteIndiCommentsCount(params);
        return params.getInherentid();
    }
    // 개인 댓글 정보 삭제
    public Long deleteIndiCommentsPost(final PostIndiCommentsRequest params) {
        postMapper.deleteIndiComments(params);
        return params.getInherentid();
    }

    // 단체 댓글 정보 조회
    public List<PostGroupCommentsResponse> findPostByGroupCommentsInherentId(final Long inherentid) {
        return postMapper.findByGroupCommentsInherentId(inherentid);
    }
    // 단체 댓글 작성자 아이디 조회
    public List<PostGroupCommentsIdResponse> findGroupCommentsIdPost() {
        return postMapper.findGroupCommentsId();
    }

    // 단체 댓글 기입
    @Transactional
    public Long saveGroupCommentsPost(final PostGroupCommentsRequest params) {
        postMapper.saveGroupComments(params);
        return params.getInherentid();
    }
    // 단체 댓글 정보 수정
    @Transactional
    public Long updateGroupCommentsPost(final PostGroupCommentsRequest params) {
        postMapper.updateGroupComments(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        postMapper.updateGroupCommentsCount(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        postMapper.updateDeleteGroupCommentsCount(params);
        return params.getInherentid();
    }
    // 단체 댓글 정보 삭제
    public Long deleteGroupCommentsPost(final PostGroupCommentsRequest params) {
        postMapper.deleteGroupComments(params);
        return params.getInherentid();
    }



    // 중고 댓글 정보 조회
    public List<PostUsedCommentsResponse> findPostByUsedCommentsInherentId(final Long inherentid) {
        return postMapper.findByUsedCommentsInherentId(inherentid);
    }
    // 중고 댓글 작성자 아이디 조회
    public List<PostUsedCommentsIdResponse> findUsedCommentsIdPost() {
        return postMapper.findUsedCommentsId();
    }

    // 중고 댓글 기입
    @Transactional
    public Long saveUsedCommentsPost(final PostUsedCommentsRequest params) {
        postMapper.saveUsedComments(params);
        return params.getInherentid();
    }
    // 중고 댓글 정보 수정
    @Transactional
    public Long updateUsedCommentsPost(final PostUsedCommentsRequest params) {
        postMapper.updateUsedComments(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        postMapper.updateUsedCommentsCount(params);
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        postMapper.updateDeleteUsedCommentsCount(params);
        return params.getInherentid();
    }
    // 중고 댓글 정보 삭제
    public Long deleteUsedCommentsPost(final PostUsedCommentsRequest params) {
        postMapper.deleteUsedComments(params);
        return params.getInherentid();
    }



    // 특정 유저 프로필 조회
    public List<UserInfoResponse> findPostById(final Long id) {
        return postMapper.findById(id);
    }

    // 내가 쓴 글 커뮤니티
    public List<PostMyBoardResponse> findPostByMyUserId(final Long userid) {
        return postMapper.findByMyUserId(userid);
    }
    // 내가 쓴 글 단체
    public List<PostMyGroupResponse> findPostByMyGroupUserId(final Long userid) {
        return postMapper.findByMyGroupUserId(userid);
    }
    // 내가 쓴 글 개인
    public List<PostMyIndiResponse> findPostByMyIndividualUserId(final Long userid) {
        return postMapper.findByMyIndividualUserId(userid);
    }
    // 내가 쓴 글 중고
    public List<PostMyUsedResponse> findPostByMyUsedUserId(final Long userid) {
        return postMapper.findByMyUsedUserId(userid);
    }

    // 내가 좋아요 누른 글 커뮤니티
    public List<PostMyLikedResponse> findPostByMyLikedUserId(final Long userid) {
        return postMapper.findByMyLikedUserId(userid);
    }
    // 내가 좋아요 누른 글 개인
    public List<PostMyIndividualLikedResponse> findPostByMyIndividualLikedUserId(final Long userid) {
        return postMapper.findByMyIndividualLikedUserId(userid);
    }
    // 내가 좋아요 누른 글 중고
    public List<PostMyUsedLikedResponse> findPostByMyUsedLikedUserId(final Long userid) {
        return postMapper.findByMyUsedLikedUserId(userid);
    }

    // 특정 채팅방 조회
    public List<PostChatRoomResponse> findPostByMyId(final Integer myid) {
        return postMapper.findByMyId(myid);
    }



    // 채팅방 넣기
    @Transactional
    public Long saveChatRoomPost(final PostChatRoomRequest params) {
        postMapper.saveChatRoom(params);
        return params.getId();
    }

    // 채팅룸 삭제
    public String deleteChatRoomPost(final PostChatRoomDeleteRequest params) {
        postMapper.deleteByRoomName(params);
        return params.getRoomname();
    }

    // 채팅 넣기
    @Transactional
    public String saveChatPost(final PostChatMessage params) {
        postMapper.saveChat(params);
        return params.getRoomname();
    }

    // db꺼내오기 텣스트
    public List<PostResponse> findAllPostTest(final Integer id) {
        return postMapper.findAllTest(id);
    }

    // 신고 유저 조회
    public List<PostReportUserResponse> findReportUserAllPost() {
        return postMapper.findReportUserAll();
    }
    // 신고 유저 삭제
    public String deleteUserReportPost(final PostUserRequest params) {
        postMapper.deleteUserReport(params);
        return null;
    }

}