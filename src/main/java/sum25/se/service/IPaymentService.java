package sum25.se.service;

import sum25.se.entity.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllPayments();
    Payment getPaymentById(Integer id);
    Payment createPayment(Payment payment);
    Payment updatePayment(Integer id, Payment payment);
    void deletePayment(Integer id);
}
