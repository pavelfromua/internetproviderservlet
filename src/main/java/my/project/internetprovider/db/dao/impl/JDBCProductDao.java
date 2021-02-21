package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.ProductDao;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCProductDao implements ProductDao {
    private static final Logger LOG = Logger.getLogger(JDBCProductDao.class);

    private Connection connection;

    public JDBCProductDao(Connection connection) {
        this.connection = connection;
    }

    private static Product extractProduct(ResultSet rs) throws SQLException {
        Product product = Product.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setName(rs.getString(Fields.PRODUCT_NAME))
                .build();

        return product;
    }

    @Override
    public Product create(Product product) {
        String query = "INSERT INTO products (name) VALUES (?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long productId = rs.getLong(1);
                product.setId(productId);
            }

            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new DataProcessingException("Product " + product.getName() + " isn't created", e);
        } finally {
            close(connection, pstmt, rs);
        }

        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> productOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(ConnectionPool.SQL_FIND_PRODUCT_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = extractProduct(rs);
                productOptional = Optional.of(product);
            }
        } catch (SQLException ex) {
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return productOptional;
    }

    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = extractProduct(rs);
                list.add(product);
            }
        } catch (SQLException ex) {
            //throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
            throw new DataProcessingException("Products aren't gotten", ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    @Override
    public void update(Product product) {
        String query = "UPDATE products SET name = ? WHERE id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            int index = 0;
            pstmt.setString(++index, product.getName());
            pstmt.setLong(++index, product.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The product " + product.getId() + " wasn't updated", ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM products WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            //LOGGER.info("The user " + id + " hasn't deleted");
            throw new DataProcessingException("The product " + id + " hasn't deleted", ex);
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
