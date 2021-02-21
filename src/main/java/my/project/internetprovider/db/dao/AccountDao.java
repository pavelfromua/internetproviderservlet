package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.entity.Account;

import java.util.Optional;

public interface AccountDao extends GenericDao<Account> {
    Optional<Account> findByUserId(Long id);

    void assignPlanToAccount(Long accountId, Long planId, Long productId);
}
