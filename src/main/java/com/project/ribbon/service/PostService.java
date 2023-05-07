package com.project.ribbon.service;

import com.project.ribbon.domain.post.*;
import com.project.ribbon.dto.*;
import com.project.ribbon.mapper.PostMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    // 커뮤니티 게시글 작성
    @Transactional
    public ResponseEntity<?> savePost(final PostRequest params) {
        try {
            postMapper.save(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 커뮤니티 게시글 수정
    @Transactional
    public ResponseEntity<?> updatePost(final PostRequest params) {
        try {
            postMapper.update(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 커뮤니티 게시글 삭제
    public ResponseEntity<?> deletePost(final PostRequest params) {
        try {
            postMapper.deleteById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteBoardWriteCommentsPost(final PostRequest params) {
        try {
            postMapper.deleteBoardWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteBoardWriteLikedPost(final PostRequest params) {
        try {
            postMapper.deleteBoardWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
            throw new RuntimeException("특정 게시글 조회에 실패했습니다.", e);
        }
    }
    @Transactional
    public ResponseEntity<?> updateBoardInquiryPost(Long boardid) {
        try {
            postMapper.updateBoardInquiry(boardid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회수 업데이트에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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

    // 단체 특정 게시글 조회
    public List<PostGroupResponse> findGroupOnePost(Long groupid) {
        try {
            return postMapper.findGroupOne(groupid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 특정게시글 조회에 실패했습니다.", e);
        }
    }
    @Transactional
    public ResponseEntity<?> updateGroupInquiryPost(Long groupid) {
        try {
            postMapper.updateGroupInquiry(groupid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회수 업데이트에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 단체 게시글 작성
    @Transactional
    public ResponseEntity<?> saveGroupPost(final PostGroupRequest params) {
        try {
            postMapper.saveGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 단체 게시글 수정
    @Transactional
    public ResponseEntity<?> updateGroupPost(final PostGroupRequest params) {
        try {
            postMapper.updateGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 단체 게시글 삭제
    public ResponseEntity<?> deleteGroupPost(final PostGroupRequest params) {
        try {
            postMapper.deleteByGroupId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체게시글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteGroupWriteCommentsPost(final PostGroupRequest params) {
        try {
            postMapper.deleteGroupWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
            throw new RuntimeException("개인 특정 게시글 조회에 실패했습니다.", e);
        }
    }
    @Transactional
    public ResponseEntity<?> updateIndiInquiryPost(Long individualid) {
        try {
            postMapper.updateIndiInquiry(individualid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("게시글 조회수 업데이트에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 개인 게시글 작성
    @Transactional
    public ResponseEntity<?> saveIndiPost(final PostIndiRequest params) {
        try {
            postMapper.saveIndi(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 개인 게시글 수정
    @Transactional
    public ResponseEntity<?> updateIndiPost(final PostIndiRequest params) {
        try {
            postMapper.updateIndi(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 개인 게시글 삭제
    public ResponseEntity<?> deleteIndiPost(final PostIndiRequest params) {
        try {
            postMapper.deleteByIndiId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인게시글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteIndiWriteCommentsPost(final PostIndiRequest params) {
        try {
            postMapper.deleteIndiWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteIndiWriteLikedPost(final PostIndiRequest params) {
        try {
            postMapper.deleteIndiWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 게시글 조회
    public List<PostUsedResponse> findUsedAllPost() {
        try {
            return postMapper.findUsedAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 조회에 실패했습니다.", e);
        }
    }
    // 대여 특정 게시글 조회
    public List<PostUsedResponse> findUsedOnePost(Long usedid) {
        try {
            return postMapper.findUsedOne(usedid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 특정 게시글 조회에 실패했습니다.", e);
        }
    }
    @Transactional
    public ResponseEntity<?> updateUsedInquiryPost(Long usedid) {
        try {
            postMapper.updateUsedInquiry(usedid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 조회수 업데이트에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 게시글 작성
    @Transactional
    public ResponseEntity<?> saveUsedPost(final PostUsedRequest params) {
        try {
            postMapper.saveUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 게시글 작성
    @Transactional
    public ResponseEntity<?> saveRentalPost(final PostRentalRequest params) {
        try {
            postMapper.saveRental(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 게시글 수정
    @Transactional
    public ResponseEntity<?> updateUsedPost(final PostUsedRequest params) {
        try {
            postMapper.updateUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 게시글 삭제
    public ResponseEntity<?> deleteUsedPost(final PostUsedRequest params) {
        try {
            postMapper.deleteByUsedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 게시글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteUsedWriteCommentsPost(final PostUsedRequest params) {
        try {
            postMapper.deleteUsedWriteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteUsedWriteLikedPost(final PostUsedRequest params) {
        try {
            postMapper.deleteUsedWriteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> deleteRentalPriceAndProductNamePost(final PostUsedRequest params) {
        try {
            postMapper.deleteRentalPriceAndProductName(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 가격 및 상품명 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 결제 정보 저장
    @Transactional
    public ResponseEntity<?> savePaymentRentalInfoPost(final PaymentDTO paymentDTO) {
        try {
            postMapper.savePaymentRentalInfo(paymentDTO);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 결제 정보 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 대여 결제 정보 저장
    @Transactional
    public ResponseEntity<?> saveRentalInfoPost(final PostRentalInfoDTO postRentalInfoDTO) {
        try {
            postMapper.saveRentalInfo(postRentalInfoDTO);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 결제 정보 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 특정 멀천트 아이디 조회
    public List<PostRentalInfoDTO> findMerchantIdRentalOnePost(Long userid) {
        try {
            return postMapper.findMerchantIdRentalOne(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 특정 멀천트 아이디 조회에 실패했습니다.", e);
        }
    }
    // 대여 특정 게시글 가격 조회
    public List<PaymentRequest> findRentalOnePricePost(String merchantUid) {
        try {
            return postMapper.findRentalOnePrice(merchantUid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 특정 게시글 가격 조회에 실패했습니다.", e);
        }
    }
    // 대여 특정 게시글 날짜 및 시간 조회
    public List<PaymentCancelRequest> findRentalPayDateOnePost(String merchantUid) {
        try {
            return postMapper.findPayDateRentalOne(merchantUid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 특정 게시글 날짜 및 시간 조회에 실패했습니다.", e);
        }
    }
    // 대여 결제 정보 삭제(취소 및 환불)
    public ResponseEntity<?> deleteRentalInfoPost(final PostRentalInfoDTO params) {
        try {
            postMapper.deleteRentalInfoById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 결제 정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 대여 결제 정보 삭제(취소 및 환불)
    public ResponseEntity<?> deletePaidRentalInfoPost(final PostRentalInfoDTO params) {
        try {
            postMapper.deletePaidRentalInfoById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("대여 결제정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 멘토 게시글 작성
    @Transactional
    public ResponseEntity<?> saveWritementorPost(final PostWritementorDTO params) {
        try {
            postMapper.saveWritementor(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 게시글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 멘토 결제 정보 저장
    @Transactional
    public ResponseEntity<?> saveWritementorPaymentInfoPost(final PaymentDTO paymentDTO) {
        try {
            postMapper.savePaymentInfo(paymentDTO);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 결제 정보 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 멘토 구매자 결제 정보 저장
    @Transactional
    public ResponseEntity<?> saveBuyerInfoPost(final PostBuyerInfoDTO postBuyerInfoDTO) {
        try {
            postMapper.saveBuyerInfo(postBuyerInfoDTO);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 구매자 결제 정보 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 특정 멀천트 아이디 조회
    public List<PostBuyerInfoDTO> findMerchantIdOnePost(Long userid) {
        try {
            return postMapper.findMerchantIdOne(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("특정 멀천트 아이디 조회에 실패했습니다.", e);
        }
    }
    // 멘토 게시글 조회
    public List<PostWritementorDTO> findMentorAllPost() {
        try {
            return postMapper.findMentorAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 게시글 조회에 실패했습니다.", e);
        }
    }
    // 멘토 특정 게시글 가격 조회
    public List<PaymentRequest> findMentorOnePricePost(String merchantUid) {
        try {
            return postMapper.findMentorOnePrice(merchantUid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 특정 게시글 가격 조회에 실패했습니다.", e);
        }
    }
    // 멘토 특정 게시글 날짜 및 시간 조회
    public List<PaymentCancelRequest> findPayDateOnePost(String merchantUid) {
        try {
            return postMapper.findPayDateOne(merchantUid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 특정 게시글 날짜 및 시간 조회에 실패했습니다.", e);
        }
    }
    // 구매자 결제 정보 삭제(취소 및 환불)
    public ResponseEntity<?> deleteBuyerInfoPost(final PostBuyerInfoDTO params) {
        try {
            postMapper.deleteBuyerInfoById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("구매자 결제 정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 결제 정보 삭제(취소 및 환불)
    public ResponseEntity<?> deletePaidInfoPost(final PostBuyerInfoDTO params) {
        try {
            postMapper.deletePaidInfoById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("결제정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 멘토 특정 게시글 조회
    public List<PostWritementorDTO> findMentorOnePost(Long id) {
        try {
            return postMapper.findMentorOne(id);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 특정 게시글 조회에 실패했습니다.", e);
        }
    }
    // 멘토 특정 별점 및 리뷰 조회
    public List<PostWritementorDTO> findMentorReviewAppraisalOnePost(Long id) {
        try {
            return postMapper.findMentorReviewAppraisalOne(id);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 특정 별점 및 리뷰 조회에 실패했습니다.", e);
        }
    }


    // 멘토 내가 쓴 글
    public List<PostWritementorDTO> findMentorPostByMyUserId(final Long userid) {
        try {
            return postMapper.findMentorByMyUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 내가 쓴 글 조회에 실패했습니다.", e);
        }
    }

    // 멘토 내가 쓴 글
    public List<PostWritementorDTO> findMentorReviewAppraisalPostByMyUserId(final Long userid) {
        try {
            return postMapper.findMentorReviewAppraisalByMyUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 내가 쓴 리뷰 및 별점 조회에 실패했습니다.", e);
        }
    }

    // 멘토 게시글 수정
    @Transactional
    public ResponseEntity<?> updateMentorPost(final PostWritementorDTO params) {
        try {
            postMapper.updateMentor(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 게시글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 멘토 게시글 삭제
    public ResponseEntity<?> deleteMentorPost(final PostWritementorDTO params) {
        try {
            postMapper.deleteMentorById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("멘토 게시글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 리뷰 및 별점 작성
    @Transactional
    public ResponseEntity<?> saveWriteFeedBackPost(final PostWritementorDTO params) {
        try {
            postMapper.saveWriteFeedBack(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("별점 및 리뷰 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 내가 쓴 리뷰 및 별점
    public List<PostWritementorDTO> findFeedBackPostByMyUserId(final Long userid) {
        try {
            return postMapper.findFeedBackByMyUserId(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("내가 쓴 리뷰 및 별점 조회에 실패했습니다.", e);
        }
    }

    // 리뷰 수정
    @Transactional
    public ResponseEntity<?> updateFeedBackPost(final PostWritementorDTO params) {
        try {
            postMapper.updateFeedBack(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("리뷰 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 리뷰 및 별점 삭제
    public ResponseEntity<?> deleteFeedBackPost(final PostWritementorDTO params) {
        try {
            postMapper.deleteFeedBackById(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("별점 및 리뷰 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    // 기존 유저 권한 정보 조회
    public PostUserRequest findUserRolesInfoAllPost(String email) {
        try {
            return postMapper.findUserRolesInfoAll(email);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저권한 정보 조회에 실패했습니다.", e);
        }

    }




    // 유저 정보 가입
    @Transactional
    public ResponseEntity<?> saveUserPost(final PostUserRequest params) {
        try {
            postMapper.saveUser(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저정보 가입에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 유저 권한 정보 가입
    @Transactional
    public ResponseEntity<?> saveUserRolesPost(final PostUserRequest params) {
        try {
            postMapper.saveUserRoles(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 권한정보 가입에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 유저 첫 인증 수정
    @Transactional
    public void updateUserUniquePost(final PostUserUpdateRequest params) throws Exception {
        try {
            postMapper.updateUserUnique(params);
        } catch (Exception e) {
            throw new Exception("유저 인증 정보 업데이트에 실패하였습니다.");
        }
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
    public void updateInstructorUserPost(final PostUserRequest params) throws Exception {
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
    public ResponseEntity<?> deleteUserPost(final PostUserRequest params) {
        try {
            postMapper.deleteByUserId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 유저 정보 삭제
    public ResponseEntity<?> deleteUserRolesPost(final PostUserRequest params) {
        try {
            postMapper.deleteByUserRolesId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 권한 정보 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 좋아요 삽입
    @Transactional
    public ResponseEntity<?> saveLikedPost(final PostLikedRequest params) {
        try {
            postMapper.saveLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삽입에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 알림 조회
    public List<PostLikedRequest> findLikedAlarmPost(Long userid) {
        try {
            return postMapper.findLikedAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 알림 조회에 실패했습니다.", e);
        }
    }
    // 개인 좋아요 알림 조회
    public List<PostIndividualLikedRequest> findIndividualLikedAlarmPost(Long userid) {
        try {
            return postMapper.findIndividualLikedAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 좋아요 알림 조회에 실패했습니다.", e);
        }
    }
    // 중고 좋아요 알림 조회
    public List<PostUsedLikedRequest> findUsedLikedAlarmPost(Long userid) {
        try {
            return postMapper.findUsedLikedAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 좋아요 알림 조회에 실패했습니다.", e);
        }
    }



    // 좋아요 정보 수정
    @Transactional
    public ResponseEntity<?> updateLikedPost(final PostLikedRequest params) {
        try {
            postMapper.updateLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteLikedPost(final PostLikedRequest params) {
        try {
            postMapper.updateDeleteLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 정보 삭제
    public ResponseEntity<?> deleteLikedPost(final PostLikedRequest params) {
        try {
            postMapper.deleteByLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 개인 삽입
    @Transactional
    public ResponseEntity<?> saveIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.saveIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 삽입에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 개인 정보 수정
    @Transactional
    public ResponseEntity<?> updateIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.updateIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.updateDeleteIndividualLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 개인 정보 삭제
    public ResponseEntity<?> deleteIndividualLikedPost(final PostIndividualLikedRequest params) {
        try {
            postMapper.deleteByIndividualLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 중고 삽입
    @Transactional
    public ResponseEntity<?> saveUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.saveUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 좋아요 삽입에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 중고 정보 수정
    @Transactional
    public ResponseEntity<?> updateUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.updateUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.updateDeleteUsedLiked(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 좋아요 중고 정보 삭제
    public ResponseEntity<?> deleteUsedLikedPost(final PostUsedLikedRequest params) {
        try {
            postMapper.deleteByUsedLikedId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고좋아요 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 커뮤니티 댓글 알림 조회
    public List<PostCommentsRequest> findCommentsAlarmPost(Long userid) {
        try {
            return postMapper.findCommentsAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("커뮤니티 댓글 알림 조회에 실패했습니다.", e);
        }
    }
    // 개인 댓글 알림 조회
    public List<PostIndiCommentsRequest> findIndiCommentsAlarmPost(Long userid) {
        try {
            return postMapper.findIndiCommentsAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 단체 알림 조회에 실패했습니다.", e);
        }
    }
    // 단체 댓글 알림 조회
    public List<PostGroupCommentsRequest> findGroupCommentsAlarmPost(Long userid) {
        try {
            return postMapper.findGroupCommentsAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 알림 조회에 실패했습니다.", e);
        }
    }
    // 중고 댓글 알림 조회
    public List<PostUsedCommentsRequest> findUsedCommentsAlarmPost(Long userid) {
        try {
            return postMapper.findUsedCommentsAlarm(userid);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 알림 조회에 실패했습니다.", e);
        }
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
    public ResponseEntity<?> saveCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.saveComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 댓글 정보 수정
    @Transactional
    public ResponseEntity<?> updateCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.updateComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateCommentsCountPost(final PostCommentsRequest params) {
        try {
            postMapper.updateCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteCommentsCountPost(final PostCommentsRequest params) {
        try {
            postMapper.updateDeleteCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 댓글 정보 삭제
    public ResponseEntity<?> deleteCommentsPost(final PostCommentsRequest params) {
        try {
            postMapper.deleteComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.saveIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 개인 댓글 정보 수정
    @Transactional
    public ResponseEntity<?> updateIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateIndiCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteIndiCommentsCountPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.updateDeleteIndiCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 개인 댓글 정보 삭제
    public ResponseEntity<?> deleteIndiCommentsPost(final PostIndiCommentsRequest params) {
        try {
            postMapper.deleteIndiComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("개인 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.saveGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 단체 댓글 정보 수정
    @Transactional
    public ResponseEntity<?> updateGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteGroupCommentsCountPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.updateDeleteGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 갯수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 단체 댓글 정보 삭제
    public ResponseEntity<?> deleteGroupCommentsPost(final PostGroupCommentsRequest params) {
        try {
            postMapper.deleteGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("단체 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.saveUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 작성에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 중고 댓글 정보 수정
    @Transactional
    public ResponseEntity<?> updateUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 댓수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteUsedCommentsCountPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.updateDeleteUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 댓수 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 중고 댓글 정보 삭제
    public ResponseEntity<?> deleteUsedCommentsPost(final PostUsedCommentsRequest params) {
        try {
            postMapper.deleteUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("중고 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveChatRoomPost(final PostChatRoomRequest params) {
        try {
            postMapper.saveChatRoom(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("채팅방 넣기에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 채팅룸 삭제
    public ResponseEntity<?> deleteChatRoomPost(final PostChatRoomDeleteRequest params) {
        try {
            postMapper.deleteByRoomName(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("채팅방 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 유저 결제 정보 조회
    public List<PostPaymentInfoResponse> findPaymentInfoAllPost() {
        try {
            return postMapper.findPaymentInfoAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 결제 정보 조회에 실패했습니다.", e);
        }
    }
    // 유저 결제 정보 수정
    public ResponseEntity<?> updatePaymentInfoPost(final String params) {
        try {
            postMapper.updateByPaymentInfoId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("유저 결제 정보 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 결제 문의하기 정보 조회
    public List<PostPaymentInfoResponse> findInqueryPaymentInfoAllPost() {
        try {
            return postMapper.findInqueryPaymentInfoAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("결제 문의하기 정보 조회에 실패했습니다.", e);
        }
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
    public ResponseEntity<?> saveReportUserPost(final PostReportUserResponse params) {
        try {
            postMapper.saveReportUser(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 멘토 글 조회
    public List<PostWritementorDTO> findReportMentorAllPost() {
        try {
            return postMapper.findReportMentorAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 멘토글 조회에 실패했습니다.", e);
        }
    }
    // 신고 멘토 글 저장
    @Transactional
    public ResponseEntity<?> saveReportMentorPost(final PostWritementorDTO params) {
        try {
            postMapper.saveReportMentor(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 멘토 글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 신고 커뮤니티글 조회
    public List<PostReportBoardResponse> findReportBoardAllPost() {
        try {
            return postMapper.findReportBoardAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 글 조회에 실패했습니다.", e);
        }
    }
    // 신고 커뮤니티글 저장
    @Transactional
    public ResponseEntity<?> saveReportBoardPost(final PostReportBoardResponse params) {
        try {
            postMapper.saveReportBoard(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportIndividualPost(final PostReportIndividualResponse params) {
        try {
            postMapper.saveReportIndividual(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportGroupPost(final PostReportGroupResponse params) {
        try {
            postMapper.saveReportGroup(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportUsedPost(final PostReportUsedResponse params) {
        try {
            postMapper.saveReportUsed(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportIndividualCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportIndividualComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> saveReportGroupCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportGroupComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    // 문의하기 조회
    public List<PostReportBoardResponse> findInquiryAllPost() {
        try {
            return postMapper.findInquiryAll();
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("문의하기 조회에 실패했습니다.", e);
        }
    }
    // 신고 중고 댓글 저장
    @Transactional
    public ResponseEntity<?> saveReportUsedCommentsPost(final PostReportCommentsResponse params) {
        try {
            postMapper.saveReportUsedComments(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 관리자페이지 신고 유저 수정
    public ResponseEntity<?> updateUserReportPost(final String params) {
        try {
            postMapper.updateUserReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 유저 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고유저 정보 수정
    public ResponseEntity<?> updateReportUserPost(final String params) {
        try {
            postMapper.updateByReportUserId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 신고유저 권한정보 수정
    public ResponseEntity<?> updateReportUserRolesPost(final String params) {
        try {
            postMapper.updateByReportUserRolesId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 유저 권한 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 활성화 유저 삭제
    public ResponseEntity<?> deleteActivateUserPost(final String params) {
        try {
            postMapper.deleteByActivateUserId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 활성화 유저 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 활성화 유저 정보 수정
    public ResponseEntity<?> updateActivateUserPost(final String params) {
        try {
            postMapper.updateByActivateUserRolesId(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("활성화 유저 수정에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 멘토 글 삭제
    public ResponseEntity<?> deleteMentorReportPost(final String params) {
        try {
            postMapper.deleteMentorReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 멘토 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 멘토 글 삭제
    public ResponseEntity<?> deleteMentorWriteReportPost(final String params) {
        try {
            postMapper.deleteMentorWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 멘토 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }


    // 관리자페이지 신고 커뮤니티글 삭제
    public ResponseEntity<?> deleteBoardReportPost(final String params) {
        try {
            postMapper.deleteBoardReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 커뮤니티 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 커뮤니티글 삭제
    public ResponseEntity<?> deleteBoardWriteReportPost(final String params) {
        try {
            postMapper.deleteBoardWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 개인글 삭제
    public ResponseEntity<?> deleteIndividualReportPost(final String params) {
        try {
            postMapper.deleteIndividualReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자 페이지 신고 개인 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 개인글 삭제
    public ResponseEntity<?> deleteIndividualWriteReportPost(final String params) {
        try {
            postMapper.deleteIndividualWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 단체글 삭제
    public ResponseEntity<?> deleteGroupReportPost(final String params) {
        try {
            postMapper.deleteGroupReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 단체 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 단체글 삭제
    public ResponseEntity<?> deleteGroupWriteReportPost(final String params) {
        try {
            postMapper.deleteGroupWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 중고글 삭제
    public ResponseEntity<?> deleteUsedReportPost(final String params) {
        try {
            postMapper.deleteUsedReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 중고 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 중고글 삭제
    public ResponseEntity<?> deleteUsedWriteReportPost(final String params) {
        try {
            postMapper.deleteUsedWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 멘토글 리뷰 삭제
    public ResponseEntity<?> deleteMentorReviewWriteReportPost(final String params) {
        try {
            postMapper.deleteMentorReviewWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 멘토 글 리뷰 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }



    // 관리자페이지 신고 커뮤니티댓글 삭제
    public ResponseEntity<?> deleteBoardCommentsReportPost(final String params) {
        try {
            postMapper.deleteBoardCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 커뮤니티댓글 삭제
    public ResponseEntity<?> deleteBoardCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteBoardCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    public ResponseEntity<?> updateDeleteReportCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 커뮤니티 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 개인댓글 삭제
    public ResponseEntity<?> deleteIndividualCommentsReportPost(final String params) {
        try {
            postMapper.deleteIndividualCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 개인댓글 삭제
    public ResponseEntity<?> deleteIndividualCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteIndividualCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteReportIndividualCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportIndividualCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 개인 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 단체댓글 삭제
    public ResponseEntity<?> deleteGroupCommentsReportPost(final String params) {
        try {
            postMapper.deleteGroupCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 단체댓글 삭제
    public ResponseEntity<?> deleteGroupCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteGroupCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteReportGroupCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportGroupCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 단체 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 관리자페이지 신고 중고댓글 삭제
    public ResponseEntity<?> deleteUsedCommentsReportPost(final String params) {
        try {
            postMapper.deleteUsedCommentsReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    // 신고 중고댓글 삭제
    public ResponseEntity<?> deleteUsedCommentsWriteReportPost(final String params) {
        try {
            postMapper.deleteUsedCommentsWriteReport(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> updateDeleteReportUsedCommentsCountPost(final String params) {
        try {
            postMapper.updateDeleteReportUsedCommentsCount(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("신고 중고 댓글 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 공지사항 작성
    @Transactional
    public ResponseEntity<?> saveAnnouncementPost(final PostAnnouncementRequest params) {
        try {
            postMapper.saveAnnouncement(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("공지사항 저장에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> deleteAdminAnnouncementPost(final String params) {
        try {
            postMapper.deleteAnnouncement(params);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("관리자페이지 공지사항 삭제에 실패했습니다.", e);
        }
        return new ResponseEntity<>("요청이 성공적으로 처리되었습니다.", HttpStatus.OK);
    }

    // 검색기능
    public List<PostWritementorDTO> findPostByWriteMentorSearch(final String query) {
        try {
            return postMapper.findWriteMentorSearch(query);
        } catch (Exception e) {
            // 예외 처리 코드 작성
            throw new RuntimeException("검색에 실패했습니다.", e);
        }
    }

}