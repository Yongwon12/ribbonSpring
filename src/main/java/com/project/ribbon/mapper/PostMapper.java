package com.project.ribbon.mapper;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.*;
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
    void deleteBoardWriteByLikedId(PostRequest params);
    void deleteBoardWriteComments(PostRequest params);

    // 커뮤니티 게시글 조회

    List<PostResponse> findAll();
    List<PostResponse> findAll11();

    // 커뮤니티 특정 게시글 조회

    List<PostResponse> findOne(Long boardid);
    void updateBoardInquiry(Long boardid);

    // 단체 게시글 조회
    List<PostGroupResponse> findGroupAll();

    // 단체 특정 게시글 조회
    List<PostGroupResponse> findGroupOne(Long groupid);
    void updateGroupInquiry(Long groupid);
    // 단체 게시글 작성
    void saveGroup(PostGroupRequest params);
    // 단체 게시글 수정
    void updateGroup(PostGroupRequest params);
    // 단체 게시글 삭제
    void deleteByGroupId(PostGroupRequest params);
    void deleteGroupWriteComments(PostGroupRequest params);

    // 개인 게시글 조회
    List<PostIndiResponse> findIndiAll();

    // 개인 특정 게시글 조회
    List<PostIndiResponse> findIndiOne(Long individualid);
    void updateIndiInquiry(Long individualid);

    // 개인 게시글 작성
    void saveIndi(PostIndiRequest params);
    // 개인 게시글 수정
    void updateIndi(PostIndiRequest params);
    // 개인 게시글 삭제
    void deleteByIndiId(PostIndiRequest params);
    void deleteIndiWriteByLikedId(PostIndiRequest params);
    void deleteIndiWriteComments(PostIndiRequest params);

    // 대여 게시글 조회
    List<PostUsedResponse> findUsedAll();

    // 대여 특정 게시글 조회
    List<PostUsedResponse> findUsedOne(Long usedid);
    void updateUsedInquiry(Long usedid);

    // 대여 게시글 작성
    void saveUsed(PostUsedRequest params);
    // 대여 게시글 작성
    void saveRental(PostUsedRequest params);
    // 대여 게시글 수정
    void updateUsed(PostUsedRequest params);
    // 대여 게시글 삭제
    void deleteByUsedId(PostUsedRequest params);
    void deleteUsedWriteByLikedId(PostUsedRequest params);
    void deleteUsedWriteComments(PostUsedRequest params);
    void deleteRentalPriceAndProductName(PostUsedRequest params);


    // 대여 결제 정보 저장
    void savePaymentRentalInfo(PaymentDTO paymentDTO);
    // 대여 결제 정보 저장
    void saveRentalInfo(PostRentalInfoDTO postRentalInfoDTO);
    // 대여 특정 멀천트 아이디 조회
    List<PostRentalInfoDTO> findMerchantIdRentalOne(Long userid);
    // 대여 결제 정보 삭제(취소 및 환불)
    void deleteRentalInfoById(PostRentalInfoDTO params);
    // 대여 결제 정보 삭제(취소 및 환불)
    void deletePaidRentalInfoById(PostRentalInfoDTO params);
    // 대여 특정 게시글 가격 조회
    List<PaymentRequest> findRentalOnePrice(String merchantUid);
    // 대여 특정 게시글 날짜 및 시간 조회
    List<PaymentCancelRequest> findPayDateRentalOne(String merchantUid);


    // 멘토 게시글 작성
    void saveWritementor(PostWritementorDTO params);
    // 멘토 결제 정보 저장
    void savePaymentInfo(PaymentDTO paymentDTO);
    // 멘토 구매자 결제 정보 저장
    void saveBuyerInfo(PostBuyerInfoDTO postBuyerInfoDTO);
    // 특정 멀천트 아이디 조회
    List<PostBuyerInfoDTO> findMerchantIdOne(Long userid);
    // 구매자 결제 정보 삭제(취소 및 환불)
    void deleteBuyerInfoById(PostBuyerInfoDTO params);
    // 결제 정보 삭제(취소 및 환불)
    void deletePaidInfoById(PostBuyerInfoDTO params);

    // 멘토 게시글 조회
    List<PostWritementorDTO> findMentorAll();

    // 멘토 특정 게시글 조회
    List<PostWritementorDTO> findMentorOne(Long id);
    // 멘토 특정 별점 및 리뷰 조회
    List<PostWritementorDTO> findMentorReviewAppraisalOne(Long id);

    // 멘토 특정 게시글 가격 조회
    List<PaymentRequest> findMentorOnePrice(String merchantUid);
    // 멘토 특정 게시글 날짜 및 시간 조회
    List<PaymentCancelRequest> findPayDateOne(String merchantUid);

    // 멘토 내가 쓴 글
    List<PostWritementorDTO> findMentorByMyUserId(Long userid);
    // 멘토 특정 별점 및 리뷰 조회
    List<PostWritementorDTO> findMentorReviewAppraisalByMyUserId(Long userid);

    // 멘토 게시글 수정
    void updateMentor(PostWritementorDTO params);

    // 멘토 게시글 삭제
    void deleteMentorById(PostWritementorDTO params);


    // 리뷰 및 별점 작성
    void saveWriteFeedBack(PostWritementorDTO params);

    // 내가 쓴 리뷰 및 별점
    List<PostWritementorDTO> findFeedBackByMyUserId(Long userid);

    // 리뷰 수정
    void updateFeedBack(PostWritementorDTO params);

    // 리뷰 및 별점 삭제
    void deleteFeedBackById(PostWritementorDTO params);


    // 유저 조회
    List<PostUserResponse> findUserAll();
    // 기존 유저 조회
    List<PostUserRequest> findUserInfoAll(String email);
    // 기존 유저 권한 조회
    PostUserRequest findUserRolesInfoAll(String email);


    // 유저 가입
    void saveUser(PostUserRequest params);
    // 유저 첫 휴대폰 인증
    void updateUserUnique(PostUserUpdateRequest params);

    // 유저 가입 권한
    void saveUserRoles(PostUserRequest params);
    // 유저 수정
    void updateUser(PostUserUpdateRequest params);
    // 강사 수정
    void updateInstructorUser(PostUserRequest params);
    // 유저 수정된 프사
    PostUserUpdateRequest findUserImage(Long userid);





    // 유저 삭제
    void deleteByUserId(PostUserRequest params);

    // 유저 삭제
    void deleteByUserRolesId(PostUserRequest params);



    // 좋아요
    void saveLiked(PostLikedRequest params);
    // 좋아요 알림 조회
    List<PostLikedRequest> findLikedAlarm(Long userid);
    // 개인 좋아요 알림 조회
    List<PostIndividualLikedRequest> findIndividualLikedAlarm(Long userid);
    // 중고 좋아요 알림 조회
    List<PostUsedLikedRequest> findUsedLikedAlarm(Long userid);

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
    // 대여 좋아요
    void saveUsedLiked(PostUsedLikedRequest params);
    // 대여 좋아요 수정
    void updateDeleteUsedLiked(PostUsedLikedRequest params);
    void updateUsedLiked(PostUsedLikedRequest params);
    // 대여 좋아요 삭제
    void deleteByUsedLikedId(PostUsedLikedRequest params);

    // 커뮤니티 댓글 알림 조회
    List<PostCommentsRequest> findCommentsAlarm(Long userid);
    // 개인 댓글 알림 조회
    List<PostIndiCommentsRequest> findIndiCommentsAlarm(Long userid);
    // 단체 댓글 알림 조회
    List<PostGroupCommentsRequest> findGroupCommentsAlarm(Long userid);
    // 대여 댓글 알림 조회
    List<PostUsedCommentsRequest> findUsedCommentsAlarm(Long userid);

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
    PostChatRoomResponse findByMyId(Long myid);
    // 특정 채팅방 전체 조회
    List<PostChatRoomResponse> findByChatRoomMyId(Long myid);



    // 채팅방 입력
    void saveChatRoom(PostChatRoomRequest params);
    // 채팅정보 입력
    void saveChatInfo(ChatMessage params);
    // 특정 채팅내역 조회
    List<PostChatRoomResponse> findByChatInfoMyId(String roomname);





    // 채팅룸 삭제
    void deleteByRoomName(PostChatRoomDeleteRequest params);



    // 유저 결제 정보 조회
    List<PostPaymentInfoResponse> findPaymentInfoAll();
    // 유저 결제 정보 수정
    void updateByPaymentInfoId(String params);
    // 결제 문의하기 조회
    List<PostPaymentInfoResponse> findInqueryPaymentInfoAll();

    // 신고 유저 조회
    List<PostReportUserResponse> findReportUserAll();
    // 신고 유저 저장
    void saveReportUser(PostReportUserResponse params);
    // 신고 멘토 게시글 조회
    List<PostWritementorDTO> findReportMentorAll();
    // 신고 멘토 게시글 저장
    void saveReportMentor(PostWritementorDTO params);
    // 신고 커뮤니티 게시글 조회
    List<PostReportBoardResponse> findReportBoardAll();
    // 문의하기 조회
    List<PostReportBoardResponse> findInquiryAll();
    // 신고 커뮤니티 게시글 저장
    void saveReportBoard(PostReportBoardResponse params);
    // 신고 개인 게시글 조회
    List<PostReportIndividualResponse> findReportIndividualAll();
    // 신고 개인 게시글 저장
    void saveReportIndividual(PostReportIndividualResponse params);
    // 신고 단체 게시글 조회
    List<PostReportGroupResponse> findReportGroupAll();
    // 신고 단체 게시글 저장
    void saveReportGroup(PostReportGroupResponse params);
    // 신고 중고 게시글 조회
    List<PostReportUsedResponse> findReportUsedAll();
    // 신고 중고 게시글 저장
    void saveReportUsed(PostReportUsedResponse params);
    // 신고 커뮤니티 댓글 조회
    List<PostReportCommentsResponse> findReportCommentsAll();
    // 신고 커뮤니티 댓글 저장
    void saveReportComments(PostReportCommentsResponse params);
    // 신고 개인 댓글 조회
    List<PostReportCommentsResponse> findReportIndividualCommentsAll();
    // 신고 개인 댓글 저장
    void saveReportIndividualComments(PostReportCommentsResponse params);
    // 신고 단체 댓글 조회
    List<PostReportCommentsResponse> findReportGroupCommentsAll();
    // 신고 단체 댓글 저장
    void saveReportGroupComments(PostReportCommentsResponse params);
    // 신고 중고 댓글 조회
    List<PostReportCommentsResponse> findReportUsedCommentsAll();
    // 신고 중고 댓글 저장
    void saveReportUsedComments(PostReportCommentsResponse params);

    // 관리자페이지 신고 유저 수정
    void updateUserReport(String params);
    // 신고유저 수정
    void updateByReportUserId(String params);

    // 신고유저권한 수정
    void updateByReportUserRolesId(String params);

    // 관리자페이지 활성화 유저 삭제
    void deleteByActivateUserId(String params);

    // 활성화 유저 수정
    void updateByActivateUserRolesId(String params);
    // 관리자페이지 신고 멘토글 삭제
    void deleteMentorReport(String params);
    // 신고 멘토 글 삭제
    void deleteMentorWriteReport(String params);
    // 관리자페이지 신고 커뮤니티글 삭제
    void deleteBoardReport(String params);
    // 신고 커뮤니티글 삭제
    void deleteBoardWriteReport(String params);

    // 관리자페이지 신고 개인글 삭제
    void deleteIndividualReport(String params);
    // 신고 개인글 삭제
    void deleteIndividualWriteReport(String params);

    // 관리자페이지 신고 단체글 삭제
    void deleteGroupReport(String params);

    // 신고 단체글 삭제
    void deleteGroupWriteReport(String params);
    // 관리자페이지 신고 중고글 삭제
    void deleteUsedReport(String params);

    // 신고 중고글 삭제
    void deleteUsedWriteReport(String params);

    // 신고 멘토 글 리뷰 삭제
    void deleteMentorReviewWriteReport(String params);

    // 관리자페이지 신고 커뮤니티댓글 삭제
    void deleteBoardCommentsReport(String params);
    // 신고 커뮤니티댓글 삭제
    void deleteBoardCommentsWriteReport(String params);
    void updateDeleteReportCommentsCount(String params);


    // 관리자페이지 신고 개인댓글 삭제
    void deleteIndividualCommentsReport(String params);
    // 신고 개인댓글 삭제
    void deleteIndividualCommentsWriteReport(String params);
    void updateDeleteReportIndividualCommentsCount(String params);

    // 관리자페이지 신고 단체댓글 삭제
    void deleteGroupCommentsReport(String params);
    // 신고 단체댓글 삭제
    void deleteGroupCommentsWriteReport(String params);
    void updateDeleteReportGroupCommentsCount(String  params);

    // 관리자페이지 신고 중고댓글 삭제
    void deleteUsedCommentsReport(String params);
    // 신고 중고댓글 삭제
    void deleteUsedCommentsWriteReport(String params);
    void updateDeleteReportUsedCommentsCount(String params);

    // 공지사항 저장
    void saveAnnouncement(PostAnnouncementRequest params);
    // 공지사항 조회
    List<PostAnnouncementRequest> findAnnouncementAll();

    // 관리자페이지 공지사항 삭제
    void deleteAnnouncement(String params);

    // 검새기능
    List<PostWritementorDTO> findWriteMentorSearch(String query);

}