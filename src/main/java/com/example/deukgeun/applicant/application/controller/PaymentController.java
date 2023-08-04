package com.example.deukgeun.applicant.application.controller;

import com.example.deukgeun.applicant.application.dto.request.CancelRequest;
import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.infrastructure.persistence.api.IamPortApiService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final IamportClient iamportClient;
    @Autowired
    private final ApplicantApplicationService applicantApplicationService;
    @Autowired
    private final IamPortApiService iamPortApiService;
    private final String iamPortApiKey;
    private final String iamPortApiSecret;

    public PaymentController(ApplicantApplicationService applicantApplicationService, IamPortApiService iamPortApiService, @Value("${iamPort.api.key}") String apiKey , @Value("${iamPort.api.secret}") String secretKey) {
        this.applicantApplicationService = applicantApplicationService;
        this.iamPortApiService = iamPortApiService;
        this.iamPortApiKey = apiKey;
        this.iamPortApiSecret = secretKey;

        iamportClient = new IamportClient(iamPortApiKey, iamPortApiSecret);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cancel")
    public ResponseEntity<?> cancel(@Valid CancelRequest request, BindingResult bindingResult) throws Exception {
        IamPortCancelResponse response = iamPortApiService.cancelIamPort(request);
        applicantApplicationService.cancel(request.getId(), response);

        return RestResponseUtil.ok("결제 취소 성공했습니다.", response.getResponse());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/token")
    public ResponseEntity<?> getIamPortAuthToken() {
        String authToken = iamPortApiService.getIamPortAuthToken(this.iamPortApiKey, this.iamPortApiSecret);

        return RestResponseUtil.ok("조회 성공했습니다.", authToken);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{applicantId}")
    public ResponseEntity<?> getPaymentInfo(@PathVariable(value= "applicantId") Long applicantId) {
        Applicant applicant = applicantApplicationService.findById(applicantId);
        PaymentInfo paymentInfo = applicant.getPaymentInfo();

        return RestResponseUtil.ok("조회 성공했습니다.", paymentInfo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{imp_uid}")
    public IamportResponse<Payment> payment(HttpSession session, @PathVariable(value= "imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(imp_uid);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> payment(@Valid PaymentInfoRequest request, BindingResult bindingResult) {
        applicantApplicationService.payment(request);

        return RestResponseUtil.ok("저장 성공했습니다.", null);
    }
}
