package com.rk.serviceImpl;

import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.rk.dto.RazorpayOrderResponse;
import com.rk.entity.Payment;
import com.rk.entity.User;
import com.rk.repository.HostelRepository;
import com.rk.repository.PaymentRepository;
import com.rk.repository.RoomTypeRepository;
import com.rk.repository.UserRepository;
import com.rk.response.PaymentResponse;
import com.rk.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeKey;

    @Value("${stripe.webhook.key}")
    private String webhookKey;
    
    @Value("${razorpay.key.id}")
    private String rozerKey;
    
    @Value("${razorpay.key.secret}")
    private String rozerSecrete;
    
    @Value("${razorpay.webhook-secret}")
    private String razorWebhookSecret;

    private final RoomTypeRepository roomTypeRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPaymentLink(Long paymentId) throws StripeException {
        Stripe.apiKey = stripeKey;

        Payment payment = getPaymentById(paymentId);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(payment.getBooking().getStudent().getEmail())
                .setSuccessUrl("http://localhost:5173/payment-success?paymentId=" + paymentId)
                .setCancelUrl("http://localhost:5173/payment-cancel")
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("paymentId", paymentId.toString())
                                .build()
                )
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("inr")
                                .setUnitAmount((long) (payment.getAmount() * 100))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Hostel Payment - " + payment.getBooking().getStudent().getFullName())
                                                .build()
                                )
                                .build()
                        )
                        .setQuantity(1L)
                        .build()
                )
                .build();

        Session session = Session.create(params);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());
        return res;
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }
    
    

@Override
public String handleStripeWebhook(String payload, String sigHeader) {
    try {
        Event event = Webhook.constructEvent(payload, sigHeader, webhookKey);

        if ("checkout.session.completed".equals(event.getType())) {

            // 1. Raw JSON
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            if (rawJson == null) {
                System.out.println("No raw JSON found for event");
                return "failure";
            }

            // 2. Extract session ID from JSON
            com.google.gson.JsonObject jsonObj = new com.google.gson.JsonParser().parse(rawJson).getAsJsonObject();
            String sessionId = jsonObj.get("id").getAsString();

            // 3. Retrieve session from Stripe
            Session session = Session.retrieve(sessionId);
            String paymentIntentId = session.getPaymentIntent();

            if (paymentIntentId == null) {
                throw new RuntimeException("PaymentIntent ID missing in session");
            }

            // 4. Retrieve PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // 5. Get paymentId from metadata
            String paymentIdStr = paymentIntent.getMetadata().get("paymentId");
            if (paymentIdStr == null) {
                throw new RuntimeException("paymentId metadata missing in PaymentIntent");
            }

            Long paymentId = Long.parseLong(paymentIdStr);
            Payment payment = getPaymentById(paymentId);

            // Update Payment
            payment.setStatus("PAID");
            payment.setPaidOn(LocalDateTime.now());
            paymentRepository.save(payment);

            // Update User
            User user = userRepository.findById(payment.getBooking().getStudent().getId())
                    .orElseThrow(() -> new RuntimeException("User not found for payment"));
            user.setPaymentStatus("PAID");
            user.setLastPaymentDate(LocalDateTime.now());
            userRepository.save(user);

            System.out.println("Payment successfully completed for paymentId: " + paymentId);

        } else {
            System.out.println("Unhandled event type: " + event.getType());
        }

        return "success";

    } catch (Exception e) {
        e.printStackTrace();
        return "failure";
    }
}

    private void handleSession(Session session) throws Exception {
        String paymentIntentId = session.getPaymentIntent();
        if (paymentIntentId == null) {
            throw new RuntimeException("PaymentIntent ID missing in session");
        }
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        handlePaymentIntent(paymentIntent);
    }

    private void handlePaymentIntent(PaymentIntent paymentIntent) throws Exception {
        String paymentIdStr = paymentIntent.getMetadata().get("paymentId");
        if (paymentIdStr == null) {
            throw new RuntimeException("paymentId metadata missing in PaymentIntent");
        }

        Long paymentId = Long.parseLong(paymentIdStr);
        Payment payment = getPaymentById(paymentId);

        // Update payment status
        payment.setStatus("PAID");
        payment.setPaidOn(LocalDateTime.now());
        paymentRepository.save(payment);

        // Update user
        User user = userRepository.findById(payment.getBooking().getStudent().getId())
                .orElseThrow(() -> new RuntimeException("User not found for payment"));
        user.setPaymentStatus("PAID");
        user.setLastPaymentDate(LocalDateTime.now());
        userRepository.save(user);

        System.out.println("Payment successfully completed for paymentId: " + paymentId);
    }

    
    
    //=============================  Rozer Pay ============================
    
    
    public RazorpayOrderResponse createRozerpayOrder(Long paymentId) throws Exception{
    	Payment payment = getPaymentById(paymentId);
    	
    	RazorpayClient client = new RazorpayClient(rozerKey,rozerSecrete);
    	
    	JSONObject orderRequest = new JSONObject();
    	
    	orderRequest.put("amount", (int) (payment.getAmount()*100));
    	orderRequest.put("currency", "INR");
    	orderRequest.put("notes", Map.of("paymentId", payment.getId()));
    	orderRequest.put("payment_capture", 1);

    	
//    	// Allowed payment methods
//    	JSONObject methodOptions = new JSONObject();
//    	methodOptions.put("upi", true);         // for Google Pay, PhonePe, etc.
//    	methodOptions.put("card", true);        // credit/debit cards
//    	methodOptions.put("netbanking", true);  // net banking
//    	methodOptions.put("wallet", true);      // wallets like Paytm
//    	
    	com.razorpay.Order order = client.orders.create(orderRequest);
    	
    	   // Student info add kar do
        String studentName = payment.getBooking().getStudent().getFullName();
        String studentEmail = payment.getBooking().getStudent().getEmail();
        String studentPhone = payment.getBooking().getStudent().getPhone();
    	
    	
    	return new RazorpayOrderResponse(order.get("id"),payment.getAmount(),"INR",payment.getId(),studentName,studentEmail,studentPhone);
    	
    }
    
    public boolean updateRazorpayWebhook(String payload, String sigHeader) throws Exception {
        try {
            Utils.verifyWebhookSignature(payload, sigHeader, razorWebhookSecret); // ✅ verify signature
            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");
            
            if ("payment.captured".equals(event)) {
                JSONObject paymentEntity = json.getJSONObject("payload")
                                           .getJSONObject("payment")
                                           .getJSONObject("entity");
                
                JSONObject notes = paymentEntity.getJSONObject("notes");
                long paymentId;
                Object obj = notes.get("paymentId");

                if (obj instanceof Integer) {
                    paymentId = ((Integer) obj).longValue();
                } else if (obj instanceof Long) {
                    paymentId = (Long) obj;
                } else {
                    paymentId = Long.parseLong(obj.toString());
                }
                Payment payment = getPaymentById(paymentId);
                
                payment.setStatus("PAID");
                payment.setPaidOn(LocalDateTime.now());
                paymentRepository.save(payment);
                
                User student = payment.getBooking().getStudent();
                student.setPaymentStatus("PAID");
                student.setLastPaymentDate(LocalDateTime.now());
                userRepository.save(student);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
