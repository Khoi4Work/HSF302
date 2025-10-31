package sum25.se.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se.entity.Payment;
import sum25.se.repository.IPaymentRepository;

import java.util.List;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentRepository iPaymentRepository;

    @Override
    public List<Payment> getAllPayments() {
        return iPaymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return iPaymentRepository.getPaymentByPaymentId(id);
    }

    @Override
    public Payment createPayment(Payment payment) {
        return iPaymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Integer id, Payment payment) {
        Payment existing = getPaymentById(id);

        existing.setPaymentDate(payment.getPaymentDate());
        existing.setAmount(payment.getAmount());
        existing.setPaymentMethod(payment.getPaymentMethod());
        existing.setPaymentStatus(payment.getPaymentStatus());
        existing.setBooking(payment.getBooking());

        return iPaymentRepository.save(existing);
    }

    @Override
    public void deletePayment(Integer id) {
        iPaymentRepository.deleteById(id);
    }
}
