package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.db.entity.User;
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

public class JDBCUserDao implements UserDao {
    private static final Logger LOG = Logger.getLogger(JDBCUserDao.class);
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

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    private static User extractUser(ResultSet rs) throws SQLException {
        User user = User.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setLogin(rs.getString(Fields.USER_LOGIN))
                .setName(rs.getString(Fields.USER_NAME))
                .setEmail(rs.getString(Fields.USER_EMAIL))
                .setPassword(rs.getString(Fields.USER_PASSWORD))
                .setSalt(rs.getBytes(Fields.USER_SALT))
                .setRoleId(rs.getInt(Fields.USER_ROLE_ID))
                .build();

        return user;
    }

    @Override
    public User create(User user) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_CREATE_USER"), Statement.RETURN_GENERATED_KEYS);
            int index = 0;
            pstmt.setString(++index, user.getName());
            pstmt.setString(++index, user.getEmail());
            pstmt.setString(++index, user.getLogin());
            pstmt.setString(++index, user.getPassword());
            pstmt.setBytes(++index, user.getSalt());
            pstmt.setInt(++index, user.getRoleId());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long userId = rs.getLong(1);
                user.setId(userId);
            }

            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new DataProcessingException("User isn't created", e);
        } finally {
            close(connection, pstmt, rs);
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> userOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_USER_BY_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = extractUser(rs);
                userOptional = Optional.of(user);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return userOptional;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<User> userOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_USER_BY_LOGIN"));
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = extractUser(rs);
                userOptional = Optional.of(user);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return userOptional;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_USERS"));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = extractUser(rs);
                list.add(user);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException("Users aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public void update(User user) {
        String queryText = query.getProperty("SQL_UPDATE_USER");

        boolean isStripQuery = user.getPassword() == null || user.getPassword().isEmpty();
        if (isStripQuery)
            queryText = query.getProperty("SQL_UPDATE_USER_STRIP");

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(queryText);
            int index = 0;
            pstmt.setString(++index, user.getName());
            pstmt.setString(++index, user.getEmail());

            if (!isStripQuery) {
                pstmt.setString(++index, user.getPassword());
                pstmt.setBytes(++index, user.getSalt());
            }

            pstmt.setLong(++index, user.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            throw new DataProcessingException("The user " + user.getId() + " wasn't updated", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public void delete(Long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_DELETE_USER"));
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            throw new DataProcessingException("The user " + id + " wasn't deleted", ex);
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
