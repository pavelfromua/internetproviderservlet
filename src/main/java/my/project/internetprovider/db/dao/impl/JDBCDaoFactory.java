package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.exception.DBException;
import my.project.internetprovider.util.DBManager;

import java.sql.Connection;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    private Connection getConnection(){
        try {
            return DBManager.getInstance().getConnection();
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }
}
