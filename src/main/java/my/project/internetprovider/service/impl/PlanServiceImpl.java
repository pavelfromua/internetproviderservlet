package my.project.internetprovider.service.impl;


import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.PlanService;

import java.util.List;

public class PlanServiceImpl implements PlanService {
    DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public List<Plan> findAll() {
        List<Plan> plans;

        try (PlanDao dao = daoFactory.createPlanDao()) {
            plans = dao.findAll();
        }

        return plans;
    }

    @Override
    public Plan create(Plan plan) throws ValidationException {
        try (PlanDao dao = daoFactory.createPlanDao()) {
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
        try (PlanDao dao = daoFactory.createPlanDao()) {
            Plan plan = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("Plan not found"));

            return plan;
        }
    }

    @Override
    public void update(Plan plan) throws UpdateException {
        try (PlanDao dao = daoFactory.createPlanDao()) {
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
        try (PlanDao dao = daoFactory.createPlanDao()) {
            dao.delete(id);
        }
    }

    @Override
    public List<Plan> findAllByAccountId(Long accountId) {
        try (PlanDao dao = daoFactory.createPlanDao()) {
            return dao.findAllByAccountId(accountId);
        }
    }
}