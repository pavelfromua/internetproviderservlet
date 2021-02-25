package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.entity.Account;
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

public class JDBCAccountDao implements AccountDao {
    private static final Logger LOG = Logger.getLogger(JDBCAccountDao.class);
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

    public JDBCAccountDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns account's entity created by the result set data.
     *
     * @return Account entity.
     */
    private static Account extractAccount(ResultSet rs) throws SQLException {
        Account account = Account.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setActive(rs.getBoolean(Fields.ACCOUNT_ACTIVE))
                .setUser(rs.getLong(Fields.ACCOUNT_USER_ID))
                .build();

        return account;
    }

    /**
     * Returns newly created account.
     *
     * @return Account entity.
     */
    @Override
    public Account create(Account account) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_CREATE_ACCOUNT"), Statement.RETURN_GENERATED_KEYS);
            pstmt.setBoolean(1, account.isActive());
            pstmt.setLong(2, account.getUser());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long accountId = rs.getLong(1);
                account.setId(accountId);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_CREATE_PLAN, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_CREATE_PLAN, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return account;
    }

    /**
     * Returns account obtained by its id.
     *
     * @return Account entity like Optional.
     */
    @Override
    public Optional<Account> findById(Long id) {
        Optional<Account> accountOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ACCOUNT_BY_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Account account = extractAccount(rs);
                accountOptional = Optional.of(account);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return accountOptional;
    }

    /**
     * Returns all accounts.
     *
     * @return List of account entities.
     */
    @Override
    public List<Account> findAll() {
        List<Account> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_ACCOUNTS"));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Account account = extractAccount(rs);
                list.add(account);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_ACCOUNTS, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_ACCOUNTS, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Updates account in DB by the account entity data.
     *
     * @return none.
     */
    @Override
    public void update(Account account) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_UPDATE_ACCOUNT"));
            int index = 0;
            pstmt.setBoolean(++index, account.isActive());
            pstmt.setLong(++index, account.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_UPDATE_ACCOUNT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_UPDATE_ACCOUNT, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Delete an account from DB by its id.
     *
     * @return none.
     */
    @Override
    public void delete(Long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_DELETE_ACCOUNT"));
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_DELETE_ACCOUNT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_DELETE_ACCOUNT, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Find an account by user id.
     *
     * @return Account entity like Optional.
     */
    @Override
    public Optional<Account> findByUserId(Long id) {
        Optional<Account> accountOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ACCOUNT_BY_USER_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Account account = extractAccount(rs);
                accountOptional = Optional.of(account);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_USER_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_USER_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return accountOptional;
    }

    /**
     * Assigns a plan to an account.
     *
     * @return none.
     */
    @Override
    public void assignPlanToAccount(Long accountId, Long planId, Long productId) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement("DELETE FROM accounts_plans WHERE plans_id IN(SELECT id FROM plans WHERE product_id = ?) AND account_id = ?");
            pstmt.setLong(1, productId);
            pstmt.setLong(2, accountId);
            pstmt.executeUpdate();

            pstmt = connection.prepareStatement("INSERT INTO accounts_plans(account_id, plans_id) VALUES(?, ?)");
            pstmt.setLong(1, accountId);
            pstmt.setLong(2, planId);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_ASSIGN_PLAN_TO_ACCOUNT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_ASSIGN_PLAN_TO_ACCOUNT, ex);
        } finally {
            close(connection, pstmt, rs);
        }
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
