package my.project.internetprovider.service;

import my.project.internetprovider.db.entity.Page;
import my.project.internetprovider.db.entity.Plan;
import java.util.List;

public interface PlanService extends GenericService<Plan, Long> {
    List<Plan> findAllByAccountId(Long accountId);

    Page<Plan> findAllForPage(int countPerPage, int currentPage, String sortedField, String sortDirection);
}
