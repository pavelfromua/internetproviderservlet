package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.exception.Messages;
import my.project.internetprovider.util.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPlanDao implements PlanDao {
    private static final Logger LOG = Logger.getLogger(JDBCPlanDao.class);

    private Connection connection;

    public JDBCPlanDao(Connection connection) {
        this.connection = connection;
    }

    private static Plan extractPlan(ResultSet rs) throws SQLException {
        Plan plan = Plan.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setName(rs.getString(Fields.PLAN_NAME))
                .setPrice(rs.getDouble(Fields.PLAN_PRICE))
                .setProduct(Product.newBuilder()
                        .setId(rs.getLong(Fields.PLAN_PRODUCT_ID))
                        .setName(rs.getString(Fields.PLAN_PRODUCT_NAME))
                        .build())
                .build();

        return plan;
    }

    @Override
    public Plan create(Plan plan) {
        String query = "INSERT INTO plans (name, price, product_id) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, plan.getName());
            pstmt.setDouble(2, plan.getPrice());
            pstmt.setLong(3, plan.getProduct().getId());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long planId = rs.getLong(1);
                plan.setId(planId);
            }

            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new DataProcessingException("Plan isn't created", e);
        } finally {
            close(connection, pstmt, rs);
        }

        return plan;
    }

    @Override
    public Optional<Plan> findById(Long id) {
        Optional<Plan> planOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(ConnectionPool.SQL_FIND_PLAN_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Plan plan = extractPlan(rs);
                planOptional = Optional.of(plan);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PLAN_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return planOptional;
    }

    @Override
    public List<Plan> findAll() {
        List<Plan> list = new ArrayList<>();
        String query = "SELECT pl.id id, pl.name name, pl.price price, pl.product_id product_id, pr.name product_name FROM plans pl, products pr WHERE pl.product_id = pr.id";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Plans aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public void update(Plan plan) {
        String query = "UPDATE plans SET name = ?, price = ?, product_id = ? WHERE id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            int index = 0;
            pstmt.setString(++index, plan.getName());
            pstmt.setDouble(++index, plan.getPrice());
            pstmt.setLong(++index, plan.getProduct().getId());
            pstmt.setLong(++index, plan.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The plan " + plan.getId() + " wasn't updated", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM plans WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The plan " + id + " hasn't deleted", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public List<Plan> findAllByAccountId(Long accountId) {
        List<Plan> list = new ArrayList<>();
        String query = "SELECT pl.id id, pl.name name, pl.price price, pl.product_id product_id, pr.name product_name FROM plans pl, products pr WHERE pl.product_id = pr.id AND pl.id IN (SELECT plans_id FROM accounts_plans WHERE account_id=?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Plans aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public List<Plan> findAllByProductId(Long productId) {
        List<Plan> list = new ArrayList<>();
        String query = "SELECT pl.id id, pl.name name, pl.price price, pl.product_id product_id, pr.name product_name FROM plans pl, products pr WHERE pl.product_id=? AND pl.product_id = pr.id";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, productId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Plans aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public void close()  {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Closes resources.
     */
    public void close(Connection con, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(con);
    }

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
            }
        }
    }

    /**
     * Closes a statement object.
     */
    private void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
            }
        }
    }

    /**
     * Closes a result set object.
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
            }
        }
    }



    /**
     * Rollbacks a connection.
     *
     * @param con
     *            Connection to be rollbacked.
     */
    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                LOG.error("Cannot rollback transaction", ex);
            }
        }
    }


}
