package my.project.internetprovider.db.dao.impl;

import my.project.internetprovider.db.Fields;
import my.project.internetprovider.db.dao.ProductDao;
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

public class JDBCProductDao implements ProductDao {
    private static final Logger LOG = Logger.getLogger(JDBCProductDao.class);
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

    public JDBCProductDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Returns product's entity created by the result set data.
     *
     * @return Product entity.
     */
    private static Product extractProduct(ResultSet rs) throws SQLException {
        Product product = Product.newBuilder()
                .setId(rs.getLong(Fields.ENTITY_ID))
                .setName(rs.getString(Fields.PRODUCT_NAME))
                .build();

        return product;
    }

    /**
     * Returns newly created product.
     *
     * @return Product entity.
     */
    @Override
    public Product create(Product product) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_CREATE_PRODUCT"), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long productId = rs.getLong(1);
                product.setId(productId);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_CREATE_PRODUCT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_CREATE_PRODUCT, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return product;
    }

    /**
     * Returns product obtained by its id.
     *
     * @return Product entity like Optional.
     */
    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> productOptional = Optional.empty();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_PRODUCT_BY_ID"));
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Product product = extractProduct(rs);
                productOptional = Optional.of(product);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return productOptional;
    }

    /**
     * Returns all products.
     *
     * @return List of product entities.
     */
    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_FIND_ALL_PRODUCTS"));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = extractProduct(rs);
                list.add(product);
            }

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ALL_PRODUCTS, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_OBTAIN_ALL_PRODUCTS, ex);
        } finally {
            close(connection, pstmt, rs);
        }

        return list;
    }

    /**
     * Updates product in DB by the product entity data.
     *
     * @return none.
     */
    @Override
    public void update(Product product) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_UPDATE_PRODUCT"));
            int index = 0;
            pstmt.setString(++index, product.getName());
            pstmt.setLong(++index, product.getId());
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
        } finally {
            close(connection, pstmt, rs);
        }
    }

    /**
     * Delete a product from DB by its id.
     *
     * @return none.
     */
    @Override
    public void delete(Long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(query.getProperty("SQL_DELETE_PRODUCT"));
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

            connection.commit();
        } catch (SQLException ex) {
            rollback(connection);
            LOG.error(Messages.ERR_CANNOT_DELETE_PRODUCT, ex);
            throw new DataProcessingException(Messages.ERR_CANNOT_DELETE_PRODUCT, ex);
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
