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
        try {
            postMapper.save(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 저장에 실패했습니다.", e);
        }
        return params.getBoardid();
    }


    // 커뮤니티 게시글 수정
    @Transactional
    public Long updatePost(final PostRequest params) {
        try {
            postMapper.update(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 수정에 실패했습니다.", e);
        }
        return params.getBoardid();
    }


    // 커뮤니티 게시글 삭제
    public Long deletePost(final PostRequest params) {
        try {
            postMapper.deleteById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 삭제에 실패했습니다.", e);
        }
        return params.getBoardid();
    }
    public Long deleteBoardWriteCommentsPost(final PostRequest params) {
        try {
            postMapper.deleteBoardWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return params.getBoardid();
    }
    public Long deleteBoardWriteLikedPost(final PostRequest params) {
        try {
            postMapper.deleteBoardWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return params.getBoardid();
    }


    // 커뮤니티 게시글 조회
    public List<PostResponse> findAllPost() {
        try {
            return postMapper.findAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회에 실패했습니다.", e);
        }
    }

    public List<PostResponse> findAllPost11() {
        try {
            return postMapper.findAll11();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회에 실패했습니다.", e);
        }
    }

    // 커뮤니티 특정 게시글 조회
    public List<PostResponse> findOnePost(Long boardid) {
        try {
            return postMapper.findOne(boardid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회에 실패했습니다.", e);
        }
    }

    // 단체 게시글 조회
    public List<PostGroupResponse> findGroupAllPost() {
        try {
            return postMapper.findGroupAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 조회에 실패했습니다.", e);
        }
    }

    // 단체 게시글 조회
    public List<PostGroupResponse> findGroupOnePost(Long groupid) {
        try {
            return postMapper.findGroupOne(groupid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 조회에 실패했습니다.", e);
        }
    }

    // 단체 게시글 작성
    @Transactional
    public Long saveGroupPost(final PostGroupRequest params) {
        try {
            postMapper.saveGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 작성에 실패했습니다.", e);
        }
        return params.getGroupid();
    }
    // 단체 게시글 수정
    @Transactional
    public Long updateGroupPost(final PostGroupRequest params) {
        try {
            postMapper.updateGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 수정에 실패했습니다.", e);
        }
        return params.getGroupid();
    }
    // 단체 게시글 삭제
    public Long deleteGroupPost(final PostGroupRequest params) {
        try {
            postMapper.deleteByGroupId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 삭제에 실패했습니다.", e);
        }
        return params.getGroupid();
    }
    public Long deleteGroupWriteCommentsPost(final PostGroupRequest params) {
        try {
            postMapper.deleteGroupWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return params.getGroupid();
    }
    // 개인 게시글 조회
    public List<PostIndiResponse> findIndiAllPost() {
        try {
            return postMapper.findIndiAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 조회에 실패했습니다.", e);
        }
    }

    // 개인 특정 게시글 조회
    public List<PostIndiResponse> findIndiOnePost(Long individualid) {
        try {
            return postMapper.findIndiOne(individualid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 조회에 실패했습니다.", e);
        }
    }

    // 개인 게시글 작성
    @Transactional
    public Long saveIndiPost(final PostIndiRequest params) {
        try {
            postMapper.saveIndi(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 작성에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    // 개인 게시글 수정
    @Transactional
    public Long updateIndiPost(final PostIndiRequest params) {
        try {
            postMapper.updateIndi(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 수정에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    // 개인 게시글 삭제
    public Long deleteIndiPost(final PostIndiRequest params) {
        try {
            postMapper.deleteByIndiId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 삭제에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    public Long deleteIndiWriteCommentsPost(final PostIndiRequest params) {
        try {
            postMapper.deleteIndiWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    public Long deleteIndiWriteLikedPost(final PostIndiRequest params) {
        try {
            postMapper.deleteIndiWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    // 중고 게시글 조회
    public List<PostUsedResponse> findUsedAllPost() {
        try {
            return postMapper.findUsedAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고게시글 조회에 실패했습니다.", e);
        }
    }
    // 중고 특정 게시글 조회
    public List<PostUsedResponse> findUsedOnePost(Long usedid) {
        try {
            return postMapper.findUsedOne(usedid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고게시글 조회에 실패했습니다.", e);
        }
    }
    // 중고 게시글 작성
    @Transactional
    public Long saveUsedPost(final PostUsedRequest params) {
        try {
            postMapper.saveUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고게시글 작성에 실패했습니다.", e);
        }
        return params.getUsedid();
    }
    // 중고 게시글 수정
    @Transactional
    public Long updateUsedPost(final PostUsedRequest params) {
        try {
            postMapper.updateUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고게시글 수정에 실패했습니다.", e);
        }
        return params.getUsedid();
    }
    // 중고 게시글 삭제
    public Long deleteUsedPost(final PostUsedRequest params) {
        try {
            postMapper.deleteByUsedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고게시글 삭제에 실패했습니다.", e);
        }
        return params.getUsedid();
    }
    public Long deleteUsedWriteCommentsPost(final PostUsedRequest params) {
        try {
            postMapper.deleteUsedWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return params.getUsedid();
    }
    public Long deleteUsedWriteLikedPost(final PostUsedRequest params) {
        try {
            postMapper.deleteUsedWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return params.getUsedid();
    }

    // 유저 정보 조회
    public List<PostUserResponse> findUserAllPost() {
        try {
            return postMapper.findUserAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저정보 조회에 실패했습니다.", e);
        }
    }
    // 기존 유저 정보 조회
    public List<PostUserRequest> findUserInfoAllPost(String email) {
        try {
            return postMapper.findUserInfoAll(email);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저정보 조회에 실패했습니다.", e);
        }

    }



    // 유저 정보 가입
    @Transactional
    public Long saveUserPost(final PostUserRequest params) {
        try {
            postMapper.saveUser(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저정보 가입에 실패했습니다.", e);
        }
        return params.getUserid();
    }

    // 유저 권한 정보 가입
    @Transactional
    public Long saveUserRolesPost(final PostUserRequest params) {
        try {
            postMapper.saveUserRoles(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 권한정보 가입에 실패했습니다.", e);
        }
        return params.getUserid();
    }

    // 유저 정보 수정
    @Transactional
    public void updateUserPost(final PostUserUpdateRequest params) throws Exception {
        try {
            postMapper.updateUser(params);
        } catch (Exception e) {
            throw new Exception("유저 정보 업데이트에 실패하였습니다.");
        }
    }
    // 강사 정보 수정
    @Transactional
    public void updateInstructorUserPost(final PostInstructorUserUpdateRequest params) throws Exception {
        try {
            postMapper.updateInstructorUser(params);
        } catch (Exception e) {
            throw new Exception("강사 정보 업데이트에 실패하였습니다.");
        }
    }


    // 유저 수정된 프로필 사진
    public PostUserUpdateRequest findUserImagePost(Long userid) {
        try {
            return postMapper.findUserImage(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 프로필 사진 정보 조회에 실패했습니다.", e);
        }
    }



    // 유저 정보 삭제
    public Long deleteUserPost(final PostUserRequest params) {
        try {
            postMapper.deleteByUserId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 정보 삭제에 실패했습니다.", e);
        }
        return params.getUserid();
    }

    // 유저 정보 삭제
    public Long deleteUserRolesPost(final PostUserRequest params) {
        try {
            postMapper.deleteByUserRolesId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 권한 정보 삭제에 실패했습니다.", e);
        }
        return params.getUserid();
    }


    // 좋아요 삽입
    @Transactional
    public Integer saveLikedPost(final PostLikedRequest params) {
        try {
            postMapper.saveLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삽입에 실패했습니다.", e);
        }
        return params.getInherentid();
    }


    // 좋아요 정보 수정
    @Transactional
    public Integer updateLikedPost(final PostLikedRequest params) {
        try {
            postMapper.updateLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteLikedPost(final PostLikedRequest params) {
        try {
            postMapper.updateDeleteLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 정보 삭제
    public Integer deleteLikedPost(final PostLikedRequest params) {
        try {
            postMapper.deleteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 개인 삽입
    @Transactional
    public Integer saveIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.saveIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 삽입에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 개인 정보 수정
    @Transactional
    public Integer updateIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.updateIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.updateDeleteIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 개인 정보 삭제
    public Integer deleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.deleteByIndividualLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 중고 삽입
    @Transactional
    public Integer saveUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.saveUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 좋아요 삽입에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 중고 정보 수정
    @Transactional
    public Integer updateUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.updateUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Integer updateDeleteUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.updateDeleteUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 좋아요 중고 정보 삭제
    public Integer deleteUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.deleteByUsedLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }


    // 댓글 정보 조회
    public List<PostCommentsResponse> findPostByInherentId(final Long inherentid) {
        try {
            return postMapper.findByInherentId(inherentid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 정보 조회에 실패했습니다.", e);
        }
    }
    // 댓글 작성자 아이디 조회
    public List<PostCommentsIdResponse> findCommentsIdPost() {
        try {
            return postMapper.findCommentsId();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 작성자 아이디 조회에 실패했습니다.", e);
        }
    }

    // 댓글 기입
    @Transactional
    public Long saveCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.saveComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 작성에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 댓글 정보 수정
    @Transactional
    public Long updateCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.updateComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateCommentsCountPost(final PostCommentsRequest params) {
        try {
            postMapper.updateCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteCommentsCountPost(final PostCommentsRequest params) {
        try {
            postMapper.updateDeleteCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 댓글 정보 삭제
    public Long deleteCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.deleteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }

    // 개인 댓글 정보 조회
    public List<PostIndiCommentsResponse> findPostByIndiCommentsInherentId(final Long inherentid) {
        try {
            return postMapper.findByIndiCommentsInherentId(inherentid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 조회에 실패했습니다.", e);
        }
    }
    // 개인 댓글 작성자 아이디 조회
    public List<PostIndiCommentsIdResponse> findIndiCommentsIdPost() {
        try {
            return postMapper.findIndiCommentsId();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 작성자 아이디 조회에 실패했습니다.", e);
        }
    }

    // 개인 댓글 기입
    @Transactional
    public Long saveIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.saveIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 작성에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 개인 댓글 정보 수정
    @Transactional
    public Long updateIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateIndiCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateDeleteIndiCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 개인 댓글 정보 삭제
    public Long deleteIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.deleteIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }

    // 단체 댓글 정보 조회
    public List<PostGroupCommentsResponse> findPostByGroupCommentsInherentId(final Long inherentid) {
        try {
            return postMapper.findByGroupCommentsInherentId(inherentid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 조회에 실패했습니다.", e);
        }
    }
    // 단체 댓글 작성자 아이디 조회
    public List<PostGroupCommentsIdResponse> findGroupCommentsIdPost() {
        try {
            return postMapper.findGroupCommentsId();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 작성자 아이디 조회에 실패했습니다.", e);
        }
    }

    // 단체 댓글 기입
    @Transactional
    public Long saveGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.saveGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 작성에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 단체 댓글 정보 수정
    @Transactional
    public Long updateGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateDeleteGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 갯수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 단체 댓글 정보 삭제
    public Long deleteGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.deleteGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }



    // 중고 댓글 정보 조회
    public List<PostUsedCommentsResponse> findPostByUsedCommentsInherentId(final Long inherentid) {
        try {
            return postMapper.findByUsedCommentsInherentId(inherentid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 조회에 실패했습니다.", e);
        }
    }
    // 중고 댓글 작성자 아이디 조회
    public List<PostUsedCommentsIdResponse> findUsedCommentsIdPost() {
        try {
            return postMapper.findUsedCommentsId();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 작성자 아이디 조회에 실패했습니다.", e);
        }
    }

    // 중고 댓글 기입
    @Transactional
    public Long saveUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.saveUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 작성에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 중고 댓글 정보 수정
    @Transactional
    public Long updateUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 댓수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    @Transactional
    public Long updateDeleteUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateDeleteUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 댓수 수정에 실패했습니다.", e);
        }
        return params.getInherentid();
    }
    // 중고 댓글 정보 삭제
    public Long deleteUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.deleteUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 삭제에 실패했습니다.", e);
        }
        return params.getInherentid();
    }


    // 특정 유저 프로필 조회
    public List<UserInfoResponse> findPostById(final Long id) {
        try {
            return postMapper.findById(id);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("특정 유저 프로필 조회에 실패했습니다.", e);
        }
    }

    // 내가 쓴 글 커뮤니티
    public List<PostMyBoardResponse> findPostByMyUserId(final Long userid) {
        try {
            return postMapper.findByMyUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("커뮤니티 내가 쓴 글 조회에 실패했습니다.", e);
        }
    }
    // 내가 쓴 글 단체
    public List<PostMyGroupResponse> findPostByMyGroupUserId(final Long userid) {
        try {
            return postMapper.findByMyGroupUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 내가 쓴 글 조회에 실패했습니다.", e);
        }
    }
    // 내가 쓴 글 개인
    public List<PostMyIndiResponse> findPostByMyIndividualUserId(final Long userid) {
        try {
            return postMapper.findByMyIndividualUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 내가 쓴 글 조회에 실패했습니다.", e);
        }
    }
    // 내가 쓴 글 중고
    public List<PostMyUsedResponse> findPostByMyUsedUserId(final Long userid) {
        try {
            return postMapper.findByMyUsedUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 내가 쓴 글 조회에 실패했습니다.", e);
        }
    }

    // 내가 좋아요 누른 글 커뮤니티
    public List<PostMyLikedResponse> findPostByMyLikedUserId(final Long userid) {
        try {
            return postMapper.findByMyLikedUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("커뮤니티 내가 좋아요 누른 글 조회에 실패했습니다.", e);
        }
    }
    // 내가 좋아요 누른 글 개인
    public List<PostMyIndividualLikedResponse> findPostByMyIndividualLikedUserId(final Long userid) {
        try {
            return postMapper.findByMyIndividualLikedUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 내가 좋아요 누른 글 조회에 실패했습니다.", e);
        }
    }
    // 내가 좋아요 누른 글 중고
    public List<PostMyUsedLikedResponse> findPostByMyUsedLikedUserId(final Long userid) {
        try {
            return postMapper.findByMyUsedLikedUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 내가 좋아요 누른 글 조회에 실패했습니다.", e);
        }
    }

    // 특정 채팅방 조회
    public List<PostChatRoomResponse> findPostByMyId(final Integer myid) {
        try {
            return postMapper.findByMyId(myid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("특정 채팅방 조회에 실패했습니다.", e);
        }
    }



    // 채팅방 넣기
    @Transactional
    public Long saveChatRoomPost(final PostChatRoomRequest params) {
        try {
            postMapper.saveChatRoom(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("채팅방 넣기에 실패했습니다.", e);
        }
        return params.getId();
    }

    // 채팅룸 삭제
    public String deleteChatRoomPost(final PostChatRoomDeleteRequest params) {
        try {
            postMapper.deleteByRoomName(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("채팅방 삭제에 실패했습니다.", e);
        }
        return params.getRoomname();
    }

    // 신고 유저 조회
    public List<PostReportUserResponse> findReportUserAllPost() {
        try {
            return postMapper.findReportUserAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 조회에 실패했습니다.", e);
        }
    }
    // 신고 유저 저장
    @Transactional
    public Long saveReportUserPost(final PostReportUserResponse params) {
        try {
            postMapper.saveReportUser(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 저장에 실패했습니다.", e);
        }
        return params.getUserid();
    }
    // 신고 커뮤니티글 조회
    public List<PostReportBoardResponse> findReportBoardAllPost() {
        try {
            return postMapper.findReportBoardAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 저장에 실패했습니다.", e);
        }
    }
    // 신고 커뮤니티글 저장
    @Transactional
    public Long saveReportBoardPost(final PostReportBoardResponse params) {
        try {
            postMapper.saveReportBoard(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 글 저장에 실패했습니다.", e);
        }
        return params.getBoardid();
    }
    // 신고 개인글 조회
    public List<PostReportIndividualResponse> findReportIndividualAllPost() {
        try {
            return postMapper.findReportIndividualAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 글 조회에 실패했습니다.", e);
        }
    }
    // 신고 개인글 저장
    @Transactional
    public Long saveReportIndividualPost(final PostReportIndividualResponse params) {
        try {
            postMapper.saveReportIndividual(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 글 저장에 실패했습니다.", e);
        }
        return params.getIndividualid();
    }
    // 신고 단체글 조회
    public List<PostReportGroupResponse> findReportGroupAllPost() {
        try {
            return postMapper.findReportGroupAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 글 조회에 실패했습니다.", e);
        }
    }
    // 신고 단체글 저장
    @Transactional
    public Long saveReportGroupPost(final PostReportGroupResponse params) {
        try {
            postMapper.saveReportGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 글 저장에 실패했습니다.", e);
        }
        return params.getGroupid();
    }
    // 신고 중고글 조회
    public List<PostReportUsedResponse> findReportUsedAllPost() {
        try {
            return postMapper.findReportUsedAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 글 조회에 실패했습니다.", e);
        }
    }
    // 신고 중고글 저장
    @Transactional
    public Long saveReportUsedPost(final PostReportUsedResponse params) {
        try {
            postMapper.saveReportUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 글 저장에 실패했습니다.", e);
        }
        return params.getUsedid();
    }
    // 신고 커뮤니티댓글 조회
    public List<PostReportCommentsResponse> findReportCommentsAllPost() {
        try {
            return postMapper.findReportCommentsAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 조회에 실패했습니다.", e);
        }
    }
    // 신고 커뮤니티 댓글 저장
    @Transactional
    public Long saveReportCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 저장에 실패했습니다.", e);
        }
        return params.getCommentsid();
    }
    // 신고 개인댓글 조회
    public List<PostReportCommentsResponse> findReportIndividualCommentsAllPost() {
        try {
            return postMapper.findReportIndividualCommentsAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 조회에 실패했습니다.", e);
        }
    }
    // 신고 개인 댓글 저장
    @Transactional
    public Long saveReportIndividualCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportIndividualComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 저장에 실패했습니다.", e);
        }
        return params.getCommentsid();
    }
    // 신고 단체댓글 조회
    public List<PostReportCommentsResponse> findReportGroupCommentsAllPost() {
        try {
            return postMapper.findReportGroupCommentsAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 조회에 실패했습니다.", e);
        }
    }
    // 신고 단체 댓글 저장
    @Transactional
    public Long saveReportGroupCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 저장에 실패했습니다.", e);
        }
        return params.getCommentsid();
    }
    // 신고 중고댓글 조회
    public List<PostReportCommentsResponse> findReportUsedCommentsAllPost() {
        try {
            return postMapper.findReportUsedCommentsAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 조회에 실패했습니다.", e);
        }
    }
    // 신고 중고 댓글 저장
    @Transactional
    public Long saveReportUsedCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 저장에 실패했습니다.", e);
        }
        return params.getCommentsid();
    }

    // 관리자페이지 신고 유저 삭제
    public PostReportUserRequest deleteUserReportPost(final String params) {
        try {
            postMapper.deleteUserReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 유저 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고유저 정보 삭제
    public PostReportUserRequest deleteReportUserPost(final String params) {
        try {
            postMapper.deleteByReportUserId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 삭제에 실패했습니다.", e);
        }
        return null;
    }

    // 신고유저 권한정보 삭제
    public PostReportUserRequest deleteReportUserRolesPost(final String params) {
        try {
            postMapper.deleteByReportUserRolesId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 권한 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 커뮤니티글 삭제
    public PostReportBoardRequest deleteBoardReportPost(final String params) {
        try {
            postMapper.deleteBoardReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 커뮤니티 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 커뮤니티글 삭제
    public PostReportBoardRequest deleteBoardWriteReportPost(final String params) {
        try {
            postMapper.deleteBoardWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 개인글 삭제
    public PostReportIndividualRequest deleteIndividualReportPost(final String params) {
        try {
            postMapper.deleteIndividualReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 개인 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 개인글 삭제
    public PostReportIndividualRequest deleteIndividualWriteReportPost(final String params) {
        try {
            postMapper.deleteIndividualWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 단체글 삭제
    public PostReportGroupRequest deleteGroupReportPost(final String params) {
        try {
            postMapper.deleteGroupReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 단체 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 단체글 삭제
    public PostReportGroupRequest deleteGroupWriteReportPost(final String params) {
        try {
            postMapper.deleteGroupWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 중고글 삭제
    public PostReportUsedRequest deleteUsedReportPost(final String params) {
        try {
            postMapper.deleteUsedReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 중고 글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 중고글 삭제
    public PostReportUsedRequest deleteUsedWriteReportPost(final String params) {
        try {
            postMapper.deleteUsedWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 글 삭제에 실패했습니다.", e);
        }
        return null;
    }



    // 관리자페이지 신고 커뮤니티댓글 삭제
    public PostReportBoardCommentsRequest deleteBoardCommentsReportPost(final String params) {
        try {
            postMapper.deleteBoardCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 커뮤니티댓글 삭제
    public PostReportBoardCommentsRequest deleteBoardCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteBoardCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    public PostReportCommentsCountRequest updateDeleteReportCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 개인댓글 삭제
    public PostReportIndividualCommentsRequest deleteIndividualCommentsReportPost(final String params) {
        try {
            postMapper.deleteIndividualCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 개인댓글 삭제
    public PostReportIndividualCommentsRequest deleteIndividualCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteIndividualCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    @Transactional
    public PostReportIndividualCommentsCountRequest updateDeleteReportIndividualCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportIndividualCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 단체댓글 삭제
    public PostReportGroupCommentsRequest deleteGroupCommentsReportPost(final String params) {
        try {
            postMapper.deleteGroupCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 단체댓글 삭제
    public PostReportGroupCommentsRequest deleteGroupCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteGroupCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    @Transactional
    public PostReportGroupCommentsCountRequest updateDeleteReportGroupCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 관리자페이지 신고 중고댓글 삭제
    public PostReportUsedCommentsRequest deleteUsedCommentsReportPost(final String params) {
        try {
            postMapper.deleteUsedCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    // 신고 중고댓글 삭제
    public PostReportUsedCommentsRequest deleteUsedCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteUsedCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }
    @Transactional
    public PostReportUsedCommentsCountRequest updateDeleteReportUsedCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return null;
    }

    // 공지사항 작성
    @Transactional
    public Long saveAnnouncementPost(final PostAnnouncementRequest params) {
        try {
            postMapper.saveAnnouncement(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("공지사항 저장에 실패했습니다.", e);
        }
        return params.getId();
    }

    // 공지사항 조회
    public List<PostAnnouncementRequest> findAnnouncementAllPost() {
        try {
            return postMapper.findAnnouncementAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("공지사 조회에 실패했습니다.", e);
        }
    }
    // 관리자페이지 공지사항삭제
    public PostAdminAnnouncementRequest deleteAdminAnnouncementPost(final String params) {
        try {
            postMapper.deleteAnnouncement(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 공지사항 삭제에 실패했습니다.", e);
        }
        return null;
    }
}