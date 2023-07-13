package com.example.deukgeun.payment.controller;

import com.example.deukgeun.global.entity.PaymentInfo;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.payment.request.CancelRequest;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.response.IamPortCancelResponse;
import com.example.deukgeun.payment.service.PaymentService;
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
    private PaymentService paymentService;

    private final String iamPortApiKey;
    private final String iamPortApiSecret;

    public PaymentController(@Value("${iamPort.api.key}") String apiKey , @Value("${iamPort.api.secret}") String secretKey) {
        this.iamPortApiKey = apiKey;
        this.iamPortApiSecret = secretKey;

        iamportClient = new IamportClient(iamPortApiKey, iamPortApiSecret);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{imp_uid}")
    public IamportResponse<Payment> payment(HttpSession session, @PathVariable(value= "imp_uid") String imp_uid)
            throws IamportResponseException, IOException {

        return iamportClient.paymentByImpUid(imp_uid);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid PaymentInfoRequest request, BindingResult bindingResult) {
        paymentService.save(request);

        return RestResponseUtil.ok("저장 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/token")
    public ResponseEntity<?> getIamPortAuthToken() {
        String authToken = paymentService.getIamPortAuthToken(this.iamPortApiKey, this.iamPortApiSecret);

        return RestResponseUtil.ok("조회 성공했습니다.", authToken);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{applicantId}")
    public ResponseEntity<?> getPaymentInfo(@PathVariable(value= "applicantId") Long applicantId) {
        PaymentInfo paymentInfo = paymentService.getPaymentInfoByApplicantId(applicantId);

        return RestResponseUtil.ok("조회 성공했습니다.", paymentInfo);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cancel")
    public ResponseEntity<?> cancel(@Valid CancelRequest request, BindingResult bindingResult) throws Exception {
        IamPortCancelResponse response = paymentService.cancelIamPort(request);
        if (response.getCode() == 1) {
            throw new Exception(response.getMessage());
        }

        return RestResponseUtil.ok("결제 취소 성공했습니다.", response.getResponse());
    }




}
