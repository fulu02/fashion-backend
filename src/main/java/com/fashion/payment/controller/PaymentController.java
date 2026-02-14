import com.fashion.payment.service.IyzicoPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// com.fashion.payment.controller.PaymentController
@RestController
@RequestMapping("/api/payments/iyzico")
@RequiredArgsConstructor
public class PaymentController {

    private final IyzicoPaymentService iyzicoPaymentService;

    @PostMapping("/start")
    public IyzicoPaymentService.CheckoutStartResponse start(@RequestParam Long orderId) {
        return iyzicoPaymentService.startCheckout(orderId);
    }

    // iyzico callback burayı vuracak (token gönderir)
    @PostMapping("/callback")
    public void callback(@RequestParam String token) {
        // istersen burada retrieve çağırıp status’ü DB’ye yazarsın
        iyzicoPaymentService.retrieve(token);
    }

    // frontend isterse sonucu sorgular
    @GetMapping("/result")
    public IyzicoPaymentService.CheckoutResultResponse result(@RequestParam String token) {
        return iyzicoPaymentService.retrieve(token);
    }
}
