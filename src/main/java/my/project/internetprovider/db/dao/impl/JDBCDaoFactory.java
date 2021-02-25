package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.dao.ProductDao;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.util.ConnectionPool;
import my.project.internetprovider.util.ConnectionUtil;

import java.sql.Connection;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao(boolean test) {
        if (test)
            return new JDBCUserDao(ConnectionUtil.getConnection());

        return new JDBCUserDao(getConnection());
    }

    @Override
    public ProductDao createProductDao(boolean test) {
        if (test)
            return new JDBCProductDao(ConnectionUtil.getConnection());

        return new JDBCProductDao(getConnection());
    }

    @Override
    public PlanDao createPlanDao(boolean test) {
        if (test)
            return new JDBCPlanDao(ConnectionUtil.getConnection());

        return new JDBCPlanDao(getConnection());
    }

    @Override
    public AccountDao createAccountDao(boolean test) {
        if (test)
            return new JDBCAccountDao(ConnectionUtil.getConnection());

        return new JDBCAccountDao(getConnection());
    }

    @Override
    public PaymentDao createPaymentDao(boolean test) {
        if (test)
            return new JDBCPaymentDao(ConnectionUtil.getConnection());

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
