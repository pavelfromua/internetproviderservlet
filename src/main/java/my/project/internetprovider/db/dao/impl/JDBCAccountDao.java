package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.exception.Messages;
import my.project.internetprovider.util.ConnectionPool;
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
            e.printStackTrace(); //TODO log in case of exception
        }
    }

    private Connection connection;

    public JDBCAccountDao(Connection connection) {
        this.connection = connection;
    }

    private static Account extractAccount(ResultSet rs) throws SQLException {
        Account account = Account.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setActive(rs.getBoolean(Fields.ACCOUNT_ACTIVE))
                .setUser(rs.getLong(Fields.ACCOUNT_USER_ID))
                .build();

        return account;
    }

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
        } catch (SQLException e) {
            rollback(connection);
            throw new DataProcessingException("Account for user " + account.getUser() + " isn't created", e);
        } finally {
            close(connection, pstmt, rs);
        }

        return account;
    }

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
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return accountOptional;
    }

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
        } catch (SQLException ex) {
            throw new DataProcessingException("Accounts aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

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
            throw new DataProcessingException("The account " + account.getId() + " wasn't updated", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

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
            throw new DataProcessingException("The account " + id + " wasn't deleted", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

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
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return accountOptional;
    }

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
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ACCOUNT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }
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
