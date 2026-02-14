package com.fashion.payment.controller;

import com.fashion.payment.service.IyzicoPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/iyzico")
@RequiredArgsConstructor
public class IyzicoPaymentController {

    private final IyzicoPaymentService iyzicoPaymentService;

    @PostMapping("/start")
    public IyzicoPaymentService.CheckoutStartResponse start(
            @RequestParam Long orderId,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        return iyzicoPaymentService.start(orderId, ip);
    }

    // Iyzico ödeme sonrası buraya token gönderir
    @PostMapping("/callback")
    public void callback(@RequestParam String token) {
        // B2.2’de burada: retrieve + Payment SUCCESS/FAILED + Order PAID + stok düş
        iyzicoPaymentService.retrieve(token);
    }

    // Frontend sonucu sorgulamak isterse
    @GetMapping("/result")
    public IyzicoPaymentService.CheckoutResultResponse result(@RequestParam String token) {
        return iyzicoPaymentService.retrieve(token);
    }
}
