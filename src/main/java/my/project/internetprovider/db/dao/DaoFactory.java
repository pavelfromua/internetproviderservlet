package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.dao.impl.JDBCDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDao createUserDao();
    public abstract ProductDao createProductDao();
    public abstract PlanDao createPlanDao();
    public abstract AccountDao createAccountDao();
    public abstract PaymentDao createPaymentDao();

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
