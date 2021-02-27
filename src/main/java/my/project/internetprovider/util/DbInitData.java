package my.project.internetprovider.util;

import my.project.internetprovider.db.dao.impl.JDBCAccountDao;
import my.project.internetprovider.db.dao.impl.JDBCPlanDao;
import my.project.internetprovider.db.dao.impl.JDBCProductDao;
import my.project.internetprovider.db.dao.impl.JDBCUserDao;
import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.db.entity.User;
import org.apache.log4j.Logger;

public class DbInitData {
    private static final Logger LOG = Logger.getLogger(DbInitData.class);

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/internetprovider?serverTimezone=UTC";
        JDBCUserDao userDao = new JDBCUserDao(ConnectionUtil.getConnection(url, "root", "root"));

        User user = User.newBuilder()
                .setEmail("admin@gmail.com")
                .setLogin("admin")
                .setRoleId(0)
                .setName("admin adminenko")
                .setPassword("111")
                .build();
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt()));

        try {
            user = userDao.create(user);
            LOG.info("The administrator is created");
        } catch (Exception e) {
            LOG.info("The administrator isn't created: " + e.getMessage());
        }

        if (user.getId() != 0L) {
            JDBCAccountDao accountDao = new JDBCAccountDao(ConnectionUtil.getConnection(url, "root", "root"));

            Account account = Account.newBuilder()
                    .setUser(user.getId())
                    .setActive(true)
                    .build();
            try {
                accountDao.create(account);

                LOG.info("An account is assigned to the administrator");
            } catch (Exception e) {
                LOG.info("An account is assigned to the administrator: " + e.getMessage());
            }
        }

        JDBCProductDao productDao = new JDBCProductDao(ConnectionUtil.getConnection(url, "root", "root"));

        Product product = Product.newBuilder()
                .setName("Internet")
                .build();

        try {
            product = productDao.create(product);

            LOG.info("A product is created");
        } catch (Exception e) {
            LOG.info("A product isn't created: " + e.getMessage());
        }

        if (product.getId() != 0L) {
            JDBCPlanDao planDao;

            Plan plan1 = Plan.newBuilder()
                    .setProduct(product)
                    .setPrice(140.0)
                    .setName("Maximum: 100Mb/s")
                    .build();

            Plan plan2 = Plan.newBuilder()
                    .setProduct(product)
                    .setPrice(115.0)
                    .setName("Optimal: 60Mb/s")
                    .build();

            Plan plan3 = Plan.newBuilder()
                    .setProduct(product)
                    .setPrice(60.0)
                    .setName("Social: 10Mb/s")
                    .build();

            try {
                planDao = new JDBCPlanDao(ConnectionUtil.getConnection(url, "root", "root"));
                planDao.create(plan1);

                planDao = new JDBCPlanDao(ConnectionUtil.getConnection(url, "root", "root"));
                planDao.create(plan2);

                planDao = new JDBCPlanDao(ConnectionUtil.getConnection(url, "root", "root"));
                planDao.create(plan3);

                LOG.info("Plans are created");
            } catch (Exception e) {
                LOG.info("Plans aren't created: " + e.getMessage());
            }
        }

    }
}
