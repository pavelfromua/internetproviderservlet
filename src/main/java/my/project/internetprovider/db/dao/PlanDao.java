package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.entity.Plan;

import java.util.List;

public interface PlanDao extends GenericDao<Plan> {
    List<Plan> findAllByAccountId(Long accountId);

    List<Plan> findAllByProductId(Long productId);
}
