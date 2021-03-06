package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.entity.Payment;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class JDBCPaymentDao implements PaymentDao {
    private static final Logger LOG = Logger.getLogger(JDBCPaymentDao.class);
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

    public JDBCPaymentDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns payment's entity created by the result set data.
     *
     * @return Payment entity.
     */
    private static Payment extractPayment(ResultSet rs) throws SQLException {
        Payment payment = Payment.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setName(rs.getString(Fields.PAYMENT_NAME))
                .setAmount(rs.getDouble(Fields.PAYMENT_AMOUNT))
                .setDate(rs.getTimestamp(Fields.PAYMENT_DATE).toLocalDateTime())
                .setAccount(rs.getLong(Fields.PAYMENT_ACCOUNT_ID))
                .build();

        return payment;
    }

    /**
     * Returns newly created payment.
     *
     * @return Payment entity.
     */
    @Override
    public Payment create(Payment payment) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_CREATE_PAYMENT"), Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            pstmt.setString(++index, payment.getName());
            pstmt.setDouble(++index, payment.getAmount());
            pstmt.setTimestamp(++index, Timestamp.valueOf(payment.getDate()));
            pstmt.setLong(++index, payment.getAccount());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long paymentId = rs.getLong(1);
                payment.setId(paymentId);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_CREATE_PAYMENT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_CREATE_PAYMENT, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return payment;
    }

    /**
     * Returns payment obtained by its id.
     *
     * @return Payment entity like Optional.
     */
    @Override
    public Optional<Payment> findById(Long id) {
        Optional<Payment> paymentOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_PAYMENT_BY_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Payment payment = extractPayment(rs);
                paymentOptional = Optional.of(payment);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_PAYMENT_BY_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PAYMENT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return paymentOptional;
    }

    /**
     * Returns all payments.
     *
     * @return List of payment entities.
     */
    @Override
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PAYMENTS"));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Payment payment = extractPayment(rs);
                list.add(payment);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PAYMENTS, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PAYMENTS, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Updates payment in DB by the payment entity data.
     *
     * @return none.
     */
    @Override
    public void update(Payment payment) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_UPDATE_PAYMENT"));
            int index = 0;
            pstmt.setString(++index, payment.getName());
            pstmt.setDouble(++index, payment.getAmount());
            pstmt.setTimestamp(++index, Timestamp.valueOf(payment.getDate()));
            pstmt.setLong(++index, payment.getAccount());
            pstmt.setLong(++index, payment.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_UPDATE_PAYMENT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_UPDATE_PAYMENT, ex);
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
            pstmt = connection.prepareStatement(query.getProperty("SQL_DELETE_PAYMENT"));
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_DELETE_PAYMENT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_DELETE_PAYMENT, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Find all payments assigned to the account.
     *
     * @return List of payment entities.
     */
    @Override
    public List<Payment> findAllByAccountId(Long accountId) {
        List<Payment> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PAYMENTS_BY_ACCOUNT_ID"));
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Payment payment = extractPayment(rs);
                list.add(payment);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PAYMENTS_BY_ACCOUNT_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PAYMENTS_BY_ACCOUNT_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Get balance by account id.
     *
     * @return Sum of balance.
     */
    @Override
    public Double getBalanceByAccountId(Long accountId) {
        Double balance = 0.0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_GET_BALANCE_BY_ACCOUNT_ID"));
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble(1);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_BALANCE_BY_ACCOUNT_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_BALANCE_BY_ACCOUNT_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return balance;
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
