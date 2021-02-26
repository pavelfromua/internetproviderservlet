package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PaymentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {
    private boolean testMode = false;
    DaoFactory daoFactory = DaoFactory.getInstance();

    public PaymentServiceImpl() {}

    public PaymentServiceImpl(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments;

        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            payments = dao.findAll();
        }

        return payments;
    }

    @Override
    public Payment create(Payment payment) throws CheckException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            return dao.create(payment);
        }
    }

    @Override
    public Payment findById(Long id) throws CheckException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            Payment payment = dao.findById(id).orElseThrow(() ->
                    new CheckException("paymentNotFound"));

            return payment;
        }
    }

    @Override
    public void update(Payment payment) throws CheckException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            Map<String, String> messages = new HashMap<>();

            if (payment.getName().isEmpty())
                messages.put("name", "paymentNameNotEmpty");

            if (payment.getAmount() <= 0)
                messages.put("name", "paymentAmountNotEmpty");

            if (messages.size() > 0)
                throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

            dao.update(payment);
        }
    }

    @Override
    public void delete(Long id) {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            dao.delete(id);
        }
    }

    @Override
    public List<Payment> findAllByAccountId(Long accountId) {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            return dao.findAllByAccountId(accountId);
        }
    }

    @Override
    public Double getBalanceByAccountId(Long accountId) {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            return dao.getBalanceByAccountId(accountId);
        }
    }
}
