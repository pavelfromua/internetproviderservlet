package my.project.internetprovider.service.impl;


import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Page;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.PlanService;

import java.util.List;

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
    public Plan create(Plan plan) throws ValidationException {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            if (plan.getName().isEmpty()) {
                throw new ValidationException("Name can't be empty");
            }

            if (plan.getPrice() <= 0) {
                throw new ValidationException("Price can't be less or equal 0");
            }

            if (plan.getProduct().getId() == 0L) {
                throw new ValidationException("Service can't be empty");
            }

            return dao.create(plan);
        }
    }

    @Override
    public Plan findById(Long id) throws NotFoundException {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            Plan plan = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Plan not found"));

            return plan;
        }
    }

    @Override
    public void update(Plan plan) throws UpdateException {
        try (PlanDao dao = daoFactory.createPlanDao(testMode)) {
            if (plan.getName().isEmpty()) {
                throw new UpdateException("Name can't be empty");
            }

            if (plan.getPrice() <= 0) {
                throw new UpdateException("Price can't be less or equal 0");
            }

            if (plan.getProduct().getId() == 0L) {
                throw new UpdateException("Service can't be empty");
            }

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
