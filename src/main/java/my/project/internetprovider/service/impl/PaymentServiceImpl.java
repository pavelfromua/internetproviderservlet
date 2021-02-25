package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.PaymentService;

import java.util.List;

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
    public Payment create(Payment payment) throws ValidationException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            return dao.create(payment);
        }
    }

    @Override
    public Payment findById(Long id) throws NotFoundException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            Payment payment = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Payment not found"));

            return payment;
        }
    }

    @Override
    public void update(Payment payment) throws UpdateException {
        try (PaymentDao dao = daoFactory.createPaymentDao(testMode)) {
            if (payment.getName().isEmpty()) {
                throw new UpdateException("Name can't be empty");
            }

            if (payment.getAmount() <= 0) {
                throw new UpdateException("Payment can't be less or equal 0");
            }

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
