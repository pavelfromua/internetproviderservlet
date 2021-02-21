package my.project.internetprovider.service;


import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;

import java.util.List;

public interface PlanService extends GenericService<Plan, Long> {
    List<Plan> findAllByAccountId(Long accountId);
}
