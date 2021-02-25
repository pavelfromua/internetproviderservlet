package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.dao.impl.JDBCDaoFactory;

import java.sql.Connection;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDao createUserDao(boolean test);
    public abstract ProductDao createProductDao(boolean test);
    public abstract PlanDao createPlanDao(boolean test);
    public abstract AccountDao createAccountDao(boolean test);
    public abstract PaymentDao createPaymentDao(boolean test);

    public static DaoFactory getInstance(){
        if( daoFactory == null ){
            synchronized (DaoFactory.class){
                if(daoFactory==null){
                    DaoFactory temp = new JDBCDaoFactory();
                    daoFactory = temp;
                }
            }
        }
        return daoFactory;
    }
}
