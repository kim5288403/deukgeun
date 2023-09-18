package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.dto.response.PaymentResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.infrastructure.api.IamPortApiService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final ApplicantApplicationService applicantApplicationService;
    private final IamPortApiService iamPortApiService;

    /**
     * 결제 취소를 수행하는 메서드입니다.
     *
     * @param request        결제 취소 요청 정보를 담고 있는 객체
     * @param bindingResult  요청 객체의 유효성 검사 결과
     * @return 결제 취소 결과에 대한 응답 ResponseEntity
     * @throws Exception 결제 취소 중에 발생할 수 있는 예외
     */
    @RequestMapping(method = RequestMethod.POST, path = "/cancel")
    public ResponseEntity<?> cancel(@Valid CancelRequest request, BindingResult bindingResult) throws Exception {
        // IamPort API를 사용하여 결제를 취소하고 그 결과를 받습니다.
        IamPortCancelResponse response = iamPortApiService.cancelIamPort(request);

        // 결제 취소 정보를 applicantApplicationService를 사용하여 처리합니다.
        applicantApplicationService.deleteMatchInfoById(request.getId());
        applicantApplicationService.updatePaymentCancelInfoById(request.getId(), response);

        return RestResponseUtil.ok("결제 취소 성공했습니다.", response.getResponse());
    }

    /**
     * IamPort API의 인증 토큰을 얻는 메서드입니다.
     *
     * @return IamPort API의 인증 토큰에 대한 응답 ResponseEntity
     */
    @RequestMapping(method = RequestMethod.GET, path = "/token")
    public ResponseEntity<?> getIamPortAuthToken() {
        // IamPort API 서비스를 사용하여 인증 토큰을 얻습니다.
        String authToken = iamPortApiService.getIamPortAuthToken();

        return RestResponseUtil.ok("조회 성공했습니다.", authToken);
    }

    /**
     * 지원자의 결제 정보를 조회하는 메서드.
     *
     * @param id 조회할 지원 내역 ID
     * @return 결제 정보 또는 오류 메시지를 포함하는 HTTP 응답
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getPaymentInfo(@PathVariable(value= "id") Long id) {
        // 지원자 정보 조회
        PaymentResponse.Info paymentInfo = applicantApplicationService.getPaymentInfo(id);

        return RestResponseUtil.ok("조회 성공했습니다.", paymentInfo);
    }

    /**
     * 지정된 'imp_uid'를 사용하여 결제 정보를 조회하는 메서드.
     *
     * @param imp_uid 조회할 결제 정보의 고유 ID ('imp_uid')
     * @return 조회된 결제 정보를 포함하는 IamportResponse 객체
     * @throws IamportResponseException Iamport API에서 발생하는 예외 처리
     * @throws IOException 입출력 관련 예외 처리
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{imp_uid}")
    public IamportResponse<Payment> payment(@PathVariable(value= "imp_uid") String imp_uid)
            throws IamportResponseException, IOException {

        return iamPortApiService.paymentByImpUid(imp_uid);
    }

    /**
     * 결제 정보를 저장하는 메서드.
     *
     * @param request 결제 정보를 나타내는 PaymentInfoRequest 객체
     * @param bindingResult 데이터 유효성 검사 결과를 담은 BindingResult 객체
     * @return 저장 성공 메시지를 포함하는 HTTP 응답
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> payment(@Valid PaymentInfoRequest request, BindingResult bindingResult) {
        applicantApplicationService.savePaymentInfo(request);

        return RestResponseUtil.ok("저장 성공했습니다.", null);
    }
}
