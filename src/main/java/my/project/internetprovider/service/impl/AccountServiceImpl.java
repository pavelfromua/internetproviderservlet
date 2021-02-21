package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.db.entity.maps.AccountProductPlanDto;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.PaymentService;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    private ProductService productService = new ProductServiceImpl();
    private PlanService planService = new PlanServiceImpl();
    private PaymentService paymentService = new PaymentServiceImpl();

    DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public List<Account> findAll() {
        List<Account> accounts;

        try (AccountDao dao = daoFactory.createAccountDao()) {
            accounts = dao.findAll();
        }

        return accounts;
    }

    @Override
    public Account create(Account account) throws ValidationException {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            return dao.create(account);
        }
    }

    @Override
    public Account findById(Long id) throws NotFoundException {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            Account account = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Account not found"));

            return account;
        }
    }

    @Override
    public void update(Account account) throws UpdateException {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            dao.update(account);
        }
    }

    @Override
    public void delete(Long id) {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            dao.delete(id);
        }
    }

    @Override
    public Account findByUserId(Long id) throws NotFoundException {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            Account account = dao.findByUserId(id).orElseThrow(() ->
                    new NotFoundException("Account not found"));

            return account;
        }
    }

    @Override
    public Map<String, ?> getDataForClientCabinet(Long userId) {
        Map<String, Object> accountData = new HashMap<>();

        Account account = null;
        try {
            account = findByUserId(userId);
        } catch (NotFoundException e) {
            e.printStackTrace(); //TODO обработать позже
        }

        List<Product> products = productService.findAll();
        List<Plan> plans = planService.findAllByAccountId(account.getId());
        AccountProductPlanDto accProductPlanDto;
        Plan planDummy = new Plan();
        planDummy.setName("not assigned");
        planDummy.setId(0L);

        List<AccountProductPlanDto> listProductPlan = new ArrayList<>();
        for (Product product: products) {
            accProductPlanDto = new AccountProductPlanDto();
            accProductPlanDto.setProduct(product);
            accProductPlanDto.setPlan(plans.stream().filter(plan -> plan.getProduct().getId() == product.getId())
                    .findFirst().orElse(planDummy));
            accProductPlanDto.setDescription(product + ": " + accProductPlanDto.getPlan());

            listProductPlan.add(accProductPlanDto);
        }

        List sortedPayments = new ArrayList(paymentService.findAllByAccountId(account.getId()));
        Collections.sort(sortedPayments, (Comparator<Payment>) (one, other) -> other.getDate().compareTo(one.getDate()));

        accountData.put("account", account);
        accountData.put("payments", sortedPayments);
        accountData.put("products", listProductPlan);
        accountData.put("balance", paymentService.getBalanceByAccountId(account.getId()));

        return accountData;
    }

    public List<Plan> findPlansByProductId(Long productId) {
        try (PlanDao dao = daoFactory.createPlanDao()) {
            return dao.findAllByProductId(productId);
        }
    }

    public Double getBalanceByAccountId(Long accountId) {
        try (PaymentDao dao = daoFactory.createPaymentDao()) {
            return dao.getBalanceByAccountId(accountId);
        }
    }

    public void setAccountStatusByBalance(Long accountId) {
        Optional<Account> accountOptional = Optional.empty();

        try (AccountDao dao = daoFactory.createAccountDao()) {
            accountOptional = dao.findById(accountId);
        }

        try (AccountDao dao = daoFactory.createAccountDao()) {
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                if (account.isActive() && getBalanceByAccountId(accountId) <= 0) {
                    account.setActive(false);
                    dao.update(account);
                }

                if (!account.isActive() && getBalanceByAccountId(accountId) > 0) {
                    account.setActive(true);
                    dao.update(account);
                }
            }
        }
    }

    @Override
    public void assignPlanToAccount(Long accountId, Long planId, Long productId) {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            dao.assignPlanToAccount(accountId, planId, productId);
        }

        try {
            Plan plan = planService.findById(planId);
            try (PaymentDao dao = daoFactory.createPaymentDao()){
                dao.create(Payment.newBuilder()
                        .setAccount(accountId)
                        .setAmount(-plan.getPrice())
                        .setName("Fee is charged for " + plan.getProduct() + ": " + plan)
                        .setDate(LocalDateTime.now())
                        .build());

                setAccountStatusByBalance(accountId);
            }
        } catch (NotFoundException e) {
            e.printStackTrace(); //TODO action in case of exception
        }
    }

    public void savePayment(Long accountId, Double amount) {
        try (PaymentDao dao = daoFactory.createPaymentDao()) {
            dao.create(Payment.newBuilder()
                    .setAccount(accountId)
                    .setAmount(amount)
                    .setName("Subscription fee")
                    .setDate(LocalDateTime.now())
                    .build());

            setAccountStatusByBalance(accountId);
        }
    }

    public void setStatus(Long accountId, Boolean status) {
        try (AccountDao dao = daoFactory.createAccountDao()) {
            dao.update(Account.newBuilder()
                    .setId(accountId)
                    .setActive(status)
                    .build());
        }
    }
}
