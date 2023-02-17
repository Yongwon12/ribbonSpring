package com.project.ribbon.mapper;

import com.project.ribbon.domain.post.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper

public interface PostMapper {

    // 커뮤니티 게시글 작성

    void save(PostRequest params);


    // 커뮤니티 게시글 수정
    void update(PostRequest params);

    // 커뮤니티 게시글 삭제
    void deleteById(PostRequest params);

    // 커뮤니티 게시글 조회

    List<PostResponse> findAll();
    List<PostResponse> findAll11();

    // 단체 게시글 조회
    List<PostGroupResponse> findGroupAll();
    // 단체 게시글 작성
    void saveGroup(PostGroupRequest params);
    // 단체 게시글 수정
    void updateGroup(PostGroupRequest params);
    // 단체 게시글 삭제
    void deleteByGroupId(PostGroupRequest params);

    // 개인 게시글 조회
    List<PostIndiResponse> findIndiAll();
    // 개인 게시글 작성
    void saveIndi(PostIndiRequest params);
    // 개인 게시글 수정
    void updateIndi(PostIndiRequest params);
    // 개인 게시글 삭제
    void deleteByIndiId(PostIndiRequest params);
    // 중고 게시글 조회
    List<PostUsedResponse> findUsedAll();
    // 중고 게시글 작성
    void saveUsed(PostUsedRequest params);
    // 중고 게시글 수정
    void updateUsed(PostUsedRequest params);
    // 중고 게시글 삭제
    void deleteByUsedId(PostUsedRequest params);

    // 유저 조회
    List<PostUserResponse> findUserAll();
    // 기존 유저 조회

    List<PostUserRequest> findUserInfoAll(String email);


    // 유저 가입
    void saveUser(PostUserRequest params);

    // 유저 가입 권한
    void saveUserRoles(PostUserRequest params);
    // 유저 수정
    void updateUser(PostUserUpdateRequest params);


    // 유저 삭제
    void deleteByUserId(PostUserRequest params);



    // 좋아요
    void saveLiked(PostLikedRequest params);
    // 좋아요 알림


    // 좋아요 수정
    void updateDeleteLiked(PostLikedRequest params);
    void updateLiked(PostLikedRequest params);
    // 좋아요 삭제
    void deleteByLikedId(PostLikedRequest params);
    // 개인 좋아요
    void saveIndividualLiked(PostIndividualLikedRequest params);
    // 개인 좋아요 수정
    void updateDeleteIndividualLiked(PostIndividualLikedRequest params);
    void updateIndividualLiked(PostIndividualLikedRequest params);
    // 개인 좋아요 삭제
    void deleteByIndividualLikedId(PostIndividualLikedRequest params);
    // 중고 좋아요
    void saveUsedLiked(PostUsedLikedRequest params);
    // 중고 좋아요 수정
    void updateDeleteUsedLiked(PostUsedLikedRequest params);
    void updateUsedLiked(PostUsedLikedRequest params);
    // 중고 좋아요 삭제
    void deleteByUsedLikedId(PostUsedLikedRequest params);


    // 댓글 조회
    List<PostCommentsResponse> findByInherentId(Long inherentid);
    // 댓글 아이디 조회
    List<PostCommentsIdResponse> findCommentsId();
    // 댓글 기입
    void saveComments(PostCommentsRequest params);
    // 댓글 수정
    void updateComments(PostCommentsRequest params);
    void updateCommentsCount(PostCommentsRequest params);
    // 댓글 삭제
    void deleteComments(PostCommentsRequest params);
    void updateDeleteCommentsCount(PostCommentsRequest params);

    // 개인 댓글 조회
    List<PostIndiCommentsResponse> findByIndiCommentsInherentId(Long inherentid);
    // 개인 댓글 아이디 조회
    List<PostIndiCommentsIdResponse> findIndiCommentsId();
    // 개인 댓글 기입
    void saveIndiComments(PostIndiCommentsRequest params);
    // 개인 댓글 수정
    void updateIndiComments(PostIndiCommentsRequest params);
    void updateIndiCommentsCount(PostIndiCommentsRequest params);
    // 개인 댓글 삭제
    void deleteIndiComments(PostIndiCommentsRequest params);
    void updateDeleteIndiCommentsCount(PostIndiCommentsRequest params);

    // 단체 댓글 조회
    List<PostGroupCommentsResponse> findByGroupCommentsInherentId(Long inherentid);
    // 단체 댓글 아이디 조회
    List<PostGroupCommentsIdResponse> findGroupCommentsId();
    // 단체 댓글 기입
    void saveGroupComments(PostGroupCommentsRequest params);
    // 단체 댓글 수정
    void updateGroupComments(PostGroupCommentsRequest params);
    void updateGroupCommentsCount(PostGroupCommentsRequest params);
    // 단체 댓글 삭제
    void deleteGroupComments(PostGroupCommentsRequest params);
    void updateDeleteGroupCommentsCount(PostGroupCommentsRequest params);


    // 중고 댓글 조회
    List<PostUsedCommentsResponse> findByUsedCommentsInherentId(Long inherentid);
    // 중고 댓글 아이디 조회
    List<PostUsedCommentsIdResponse> findUsedCommentsId();
    // 중고 댓글 기입
    void saveUsedComments(PostUsedCommentsRequest params);
    // 중고 댓글 수정
    void updateUsedComments(PostUsedCommentsRequest params);
    void updateUsedCommentsCount(PostUsedCommentsRequest params);
    // 중고 댓글 삭제
    void deleteUsedComments(PostUsedCommentsRequest params);
    void updateDeleteUsedCommentsCount(PostUsedCommentsRequest params);




//    // 답글 조회
//    List<PostReplyResponse> findByReplyInherentId(Long inherentid);
//    // 답글 아이디 조회
//    List<PostReplyIdResponse> findReplyId();
//    // 답글 기입
//    void saveReply(PostReplyRequest params);
//    // 답글 수정
//    void updateReply(PostReplyRequest params);
//    void updateReplyCount(PostReplyRequest params);
//    // 답글 삭제
//    void deleteReply(PostReplyRequest params);
//    void updateDeleteReplyCount(PostReplyRequest params);
//
//
//    // 개인 답글 조회
//    List<PostIndiReplyResponse> findByIndiReplyInherentId(Long inherentid);
//    // 개인 답글 아이디 조회
//    List<PostIndiReplyIdResponse> findIndiReplyId();
//    // 개인 답글 기입
//    void saveIndiReply(PostIndiReplyRequest params);
//    // 개인 답글 수정
//    void updateIndiReply(PostIndiReplyRequest params);
//    void updateIndiReplyCount(PostIndiReplyRequest params);
//    // 개인 답글 삭제
//    void deleteIndiReply(PostIndiReplyRequest params);
//    void updateDeleteIndiReplyCount(PostIndiReplyRequest params);
//
//
//    // 단체 답글 조회
//    List<PostGroupReplyResponse> findByGroupReplyInherentId(Long inherentid);
//    // 단체 답글 아이디 조회
//    List<PostGroupReplyIdResponse> findGroupReplyId();
//    // 단체 답글 기입
//    void saveGroupReply(PostGroupReplyRequest params);
//    // 단체 답글 수정
//    void updateGroupReply(PostGroupReplyRequest params);
//    void updateGroupReplyCount(PostGroupReplyRequest params);
//    // 단체 답글 삭제
//    void deleteGroupReply(PostGroupReplyRequest params);
//    void updateDeleteGroupReplyCount(PostGroupReplyRequest params);
//
//    // 중고 답글 조회
//    List<PostUsedReplyResponse> findByUsedReplyInherentId(Long inherentid);
//    // 중고 답글 아이디 조회
//    List<PostUsedReplyIdResponse> findUsedReplyId();
//    // 중고 답글 기입
//    void saveUsedReply(PostUsedReplyRequest params);
//    // 중고 답글 수정
//    void updateUsedReply(PostUsedReplyRequest params);
//    void updateUsedReplyCount(PostUsedReplyRequest params);
//    // 중고 답글 삭제
//    void deleteUsedReply(PostUsedReplyRequest params);
//    void updateDeleteUsedReplyCount(PostUsedReplyRequest params);



    // 특정 유저 프로필 조회
    List<UserInfoResponse> findById(Long id);


    // 내가 쓴 글 커뮤니티
    List<PostMyBoardResponse> findByMyUserId(Long userid);
    // 내가 쓴 글 단체
    List<PostMyGroupResponse> findByMyGroupUserId(Long userid);
    // 내가 쓴 글 개인
    List<PostMyIndiResponse> findByMyIndividualUserId(Long userid);
    // 내가 쓴 글 중고
    List<PostMyUsedResponse> findByMyUsedUserId(Long userid);

    // 내가 좋아요 누른 글 커뮤니티
    List<PostMyLikedResponse> findByMyLikedUserId(Long userid);
    // 내가 좋아요 누른 글 개인
    List<PostMyIndividualLikedResponse> findByMyIndividualLikedUserId(Long userid);
    // 내가 좋아요 누른 글 중고
    List<PostMyUsedLikedResponse> findByMyUsedLikedUserId(Long userid);

    // 특정 채팅방 조회
    List<PostChatRoomResponse> findByMyId(Integer myid);



    // 채팅방 입력
    void saveChatRoom(PostChatRoomRequest params);

    // db꺼내오기 테스트
    List<PostResponse> findAllTest(Integer id);



    // 채팅룸 삭제
    void deleteByRoomName(PostChatRoomDeleteRequest params);


    // 채팅 입력
    void saveChat(PostChatMessage params);

    // 게시글 갯수 카운트
    int count();

}