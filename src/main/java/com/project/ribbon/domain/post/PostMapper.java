package com.project.ribbon.domain.post;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface PostMapper {

    // 커뮤니티 게시글 작성

    void save(PostRequest params);


    // 커뮤니티 게시글 수정
    void update(PostRequest params);

    // 커뮤니티 게시글 삭제
    void deleteById(PostRequest params);

    // 커뮤니티 게시글 조회

    List<PostResponse> findAll1();
    List<PostResponse> findAll2();
    List<PostResponse> findAll3();
    List<PostResponse> findAll4();
    List<PostResponse> findAll5();
    List<PostResponse> findAll6();
    List<PostResponse> findAll7();
    List<PostResponse> findAll8();
    List<PostResponse> findAll9();
    List<PostResponse> findAll10();
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
    // 유저 가입
    void saveUser(PostUserRequest params);
    // 유저 수정
    void updateUser(PostUserRequest params);
    // 유저 삭제
    void deleteByUserId(PostUserRequest params);



    // 좋아요
    void saveLiked(PostLikedRequest params);
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
    void updateDeleteCommentsCount(PostCommentsRequest params);
    // 댓글 삭제
    void deleteComments(PostCommentsRequest params);

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


    // 게시글 갯수 카운트
    int count();

}