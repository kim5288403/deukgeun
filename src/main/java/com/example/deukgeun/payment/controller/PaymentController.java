package com.example.deukgeun.payment.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.payment.request.PaymentInfoRequest;
import com.example.deukgeun.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PaymentController() {
        iamportClient = new IamportClient("6640244608024545", "TGJK7YEsO618StWIkXp0FIUErEzyNp2J3J8bQE9LexDvajoydGIuiZF1fL8BWe1xKZyDNxmbjX2YcHmv");
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
}
