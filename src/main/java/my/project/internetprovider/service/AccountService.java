package my.project.internetprovider.service;

import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;

import java.util.List;
import java.util.Map;

public interface AccountService extends GenericService<Account, Long> {
    Account findByUserId(Long id) throws NotFoundException;

    Map<String, ?> getDataForClientCabinet(Long userId);

    List<Plan> findPlansByProductId(Long productId);

    void assignPlanToAccount(Long accountId, Long planId, Long productId);

    void setStatus(Long accountId, Boolean status);

    void savePayment(Long accountId, Double amount);
}
