package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.dao.ProductDao;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.util.ConnectionPool;

import java.sql.Connection;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    @Override
    public ProductDao createProductDao() {
        return new JDBCProductDao(getConnection());
    }

    @Override
    public PlanDao createPlanDao() {
        return new JDBCPlanDao(getConnection());
    }

    @Override
    public AccountDao createAccountDao() {
        return new JDBCAccountDao(getConnection());
    }

    @Override
    public PaymentDao createPaymentDao() {
        return new JDBCPaymentDao(getConnection());
    }

    private Connection getConnection(){
        try {
            return ConnectionPool.getInstance().getConnection();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }
}
