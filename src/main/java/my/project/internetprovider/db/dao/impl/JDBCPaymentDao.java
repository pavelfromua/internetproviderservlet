package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.PaymentDao;
import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.DataProcessingException;
import my.project.internetprovider.exception.Messages;
import my.project.internetprovider.util.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCPaymentDao implements PaymentDao {
    private static final Logger LOG = Logger.getLogger(JDBCPaymentDao.class);

    private Connection connection;

    public JDBCPaymentDao(Connection connection) {
        this.connection = connection;
    }

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

    @Override
    public Payment create(Payment payment) {
        String query = "INSERT INTO payments (name, amount, date, account_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
        } catch (SQLException e) {
            rollback(connection);
            throw new DataProcessingException("Payment " + payment.getName() + " isn't created", e);
        } finally {
            close(connection, pstmt, rs);
        }

        return payment;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        Optional<Payment> paymentOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(ConnectionPool.SQL_FIND_PAYMENT_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Payment payment = extractPayment(rs);
                paymentOptional = Optional.of(payment);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PAYMENT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return paymentOptional;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        String query = "SELECT * FROM payments";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Payment payment = extractPayment(rs);
                list.add(payment);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Payments aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public void update(Payment payment) {
        String query = "UPDATE payments SET name = ?, amount = ?, date = ?, account_id = ? WHERE id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            int index = 0;
            pstmt.setString(++index, payment.getName());
            pstmt.setDouble(++index, payment.getAmount());
            pstmt.setTimestamp(++index, Timestamp.valueOf(payment.getDate()));
            pstmt.setLong(++index, payment.getAccount());
            pstmt.setLong(++index, payment.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The payment " + payment.getId() + " wasn't updated", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM payments WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The payment " + id + " hasn't deleted", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public List<Payment> findAllByAccountId(Long accountId) {
        List<Payment> list = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE account_id=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Payment payment = extractPayment(rs);
                list.add(payment);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Payments aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public Double getBalanceByAccountId(Long accountId) {
        Double balance = 0.0;
        String query = "SELECT sum(amount) FROM payments WHERE account_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble(1);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Balance isn't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return balance;
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
