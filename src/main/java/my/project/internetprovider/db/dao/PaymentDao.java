package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.entity.Payment;
import java.util.List;

public interface PaymentDao extends GenericDao<Payment> {
    List<Payment> findAllByAccountId(Long accountId);

    Double getBalanceByAccountId(Long accountId);
}
