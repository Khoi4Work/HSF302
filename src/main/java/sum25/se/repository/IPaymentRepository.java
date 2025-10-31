package sum25.se.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se.entity.Payment;

public interface IPaymentRepository extends JpaRepository<Payment,Integer> {
    Payment getPaymentByPaymentId(Integer paymentId);
}
