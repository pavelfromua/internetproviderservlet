package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.PlanDao;
import my.project.internetprovider.db.entity.Page;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.exception.Messages;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class JDBCPlanDao implements PlanDao {
    private static final Logger LOG = Logger.getLogger(JDBCPlanDao.class);
    private static final Properties query;

    static {
        query = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("query.properties");
        try {
            query.load(stream);
        } catch (IOException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_QUERY_LIST);
        }
    }

    private Connection connection;

    public JDBCPlanDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns plan's entity created by the result set data.
     *
     * @return Plan entity.
     */
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

    /**
     * Returns newly created plan.
     *
     * @return Plan entity.
     */
    @Override
    public Plan create(Plan plan) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_CREATE_PLAN"), Statement.RETURN_GENERATED_KEYS);
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
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_CREATE_PLAN, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_CREATE_PLAN, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return plan;
    }

    /**
     * Returns plan obtained by its id.
     *
     * @return Plan entity like Optional.
     */
    @Override
    public Optional<Plan> findById(Long id) {
        Optional<Plan> planOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_PLAN_BY_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Plan plan = extractPlan(rs);
                planOptional = Optional.of(plan);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_PLAN_BY_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PLAN_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return planOptional;
    }

    /**
     * Returns all plans.
     *
     * @return List of plan entities.
     */
    @Override
    public List<Plan> findAll() {
        List<Plan> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PLANS"));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Updates plan in DB by the plan entity data.
     *
     * @return none.
     */
    @Override
    public void update(Plan plan) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_UPDATE_PLAN"));
            int index = 0;
            pstmt.setString(++index, plan.getName());
            pstmt.setDouble(++index, plan.getPrice());
            pstmt.setLong(++index, plan.getProduct().getId());
            pstmt.setLong(++index, plan.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_UPDATE_PLAN, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_UPDATE_PLAN, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Delete a plan from DB by its id.
     *
     * @return none.
     */
    @Override
    public void delete(Long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_DELETE_PLAN"));
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_DELETE_PLAN, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_DELETE_PLAN, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Find all plans assigned to an account by its id.
     *
     * @return List of plan entities.
     */
    @Override
    public List<Plan> findAllByAccountId(Long accountId) {
        List<Plan> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PLANS_BY_ACCOUNT_ID"));
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_BY_ACCOUNT_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_BY_ACCOUNT_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Find all plans assigned to a product by its id.
     *
     * @return List of plan entities.
     */
    @Override
    public List<Plan> findAllByProductId(Long productId) {
        List<Plan> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PLANS_BY_PRODUCT_ID"));
            pstmt.setLong(1, productId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_BY_PRODUCT_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_BY_PRODUCT_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Find all plans for a page when pagination is in use.
     *
     * @return List of plan entities.
     */
    @Override
    public Page findAllForPage(int countPerPage, int pageNumber, String sortedField, String sortDirection) {
        Page<Plan> page = new Page<>();
        page.setCurrentPage(pageNumber);

        int totalElements = 0;
        List<Plan> list = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_COUNT_OF_PLAN_ITEMS"));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalElements = rs.getInt(Fields.COUNT_OF_ITEMS);
            }

            page.setTotalElements(totalElements);

            int totalPages = totalElements / countPerPage;

            if (totalElements % countPerPage != 0)
                totalPages++;

            page.setTotalPages(totalPages);
            int startElement = pageNumber * countPerPage - countPerPage;

            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PLANS_FOR_PAGE")
            .replace("SORTED_FIELD", sortedField)
            .replace("SORT_DIRECTION", sortDirection));

            int index = 0;
            pstmt.setInt(++index, countPerPage);
            pstmt.setInt(++index, startElement);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Plan plan = extractPlan(rs);
                list.add(plan);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_FOR_PAGE, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PLANS_FOR_PAGE, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        page.setElements(list);

        return page;
    }

    /**
     * Closes connection.
     */
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
