package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Page;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PlanService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanServiceImpl implements PlanService {
    private boolean testMode = false;
    DaoFactory daoFactory = DaoFactory.getInstance();

    public PlanServiceImpl() {}

    public PlanServiceImpl(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public List<Plan> findAll() {
        List<Plan> plans;

        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            plans = dao.findAll();
        }

        return plans;
    }

    @Override
    public Plan create(Plan plan) throws CheckException {
        String name = plan.getName();

        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            Map<String, String> messages = new HashMap<>();

            if (name.length() < 2 || name.length() > 120)
                messages.put("name", "new.item.plan.name.size");

            if (name.isEmpty())
                messages.put("name", "new.item.plan.name.notEmpty");

            if (plan.getPrice() <= 0) {
                messages.put("price", "new.item.plan.price.notEmpty");
            }

            if (plan.getProduct().getId() == 0L) {
                messages.put("product", "serviceNotEmpty");
            }

            if (messages.size() > 0)
                throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

            return dao.create(plan);
        }
    }

    @Override
    public Plan findById(Long id) throws CheckException {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            Plan plan = dao.findById(id).orElseThrow(() ->
                    new CheckException("planNotFound"));

            return plan;
        }
    }

    @Override
    public void update(Plan plan) throws CheckException {
        String name = plan.getName();

        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            Map<String, String> messages = new HashMap<>();

            if (name.length() < 2 || name.length() > 120)
                messages.put("name", "edit.item.plan.name.size");

            if (name.isEmpty())
                messages.put("name", "edit.item.plan.name.notEmpty");

            if (plan.getPrice() <= 0) {
                messages.put("price", "edit.item.plan.price.notEmpty");
            }

            if (plan.getProduct().getId() == 0L) {
                messages.put("product", "serviceNotEmpty");
            }

            if (messages.size() > 0)
                throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

            dao.update(plan);
        }
    }

    @Override
    public void delete(Long id) {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            dao.delete(id);
        }
    }

    @Override
    public List<Plan> findAllByAccountId(Long accountId) {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            return dao.findAllByAccountId(accountId);
        }
    }

    @Override
    public Page<Plan> findAllForPage(int countPerPage, int currentPage, String sortedField, String sortDirection) {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            return dao.findAllForPage(countPerPage, currentPage, sortedField, sortDirection);
        }
    }
}
