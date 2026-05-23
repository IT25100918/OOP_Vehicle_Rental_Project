package com.vehiclerental.payment;

import com.vehiclerental.shared.FileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository extends FileRepository<Payment> {

    private static final String FILE_PATH = "data/payments.txt";

    @Override protected String getFilePath() { return FILE_PATH; }
    @Override protected Payment fromLine(String line) { return Payment.fromFileString(line); }
    @Override protected String toLine(Payment p) { return p.toFileString(); }
}
