package my.project.internetprovider.service;

import my.project.internetprovider.db.entity.Payment;

import java.util.List;

public interface PaymentService extends GenericService<Payment, Long> {
    List<Payment> findAllByAccountId(Long accountId);

    Double getBalanceByAccountId(Long accountId);
}
