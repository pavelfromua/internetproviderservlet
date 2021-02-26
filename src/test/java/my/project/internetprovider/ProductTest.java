package my.project.internetprovider;

import my.project.internetprovider.db.entity.Product;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PlanService;
import my.project.internetprovider.service.ProductService;
import my.project.internetprovider.service.impl.PlanServiceImpl;
import my.project.internetprovider.service.impl.ProductServiceImpl;
import my.project.internetprovider.util.ConnectionUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductTest {
    @BeforeClass
    public static void beforeTest() throws SQLException, ClassNotFoundException {
        String fileName = "test_dbcreate-mysql.sql";
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String[] queries = reader.lines().collect(Collectors.joining(System.lineSeparator())).split(";");
            try (Connection connection = ConnectionUtil.getConnection()) {
                for (String query: queries) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                    statement.close();
                }
            }
        }
        fileName = "test_data-mysql.sql";
        classLoader = ClassLoader.getSystemClassLoader();
        is = classLoader.getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String[] queries = reader.lines().collect(Collectors.joining(System.lineSeparator())).split(";");
            for (String query: queries) {
                query = query.trim();
                try (Connection connection = ConnectionUtil.getConnection()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                    statement.close();
                    connection.commit();
                }
            }
        }
    }

    @Test
    public void _1productCreationIsExpected() {
        ProductService productService = new ProductServiceImpl(true);
        Product product = null;
        try {
            product = productService.create(Product.newBuilder()
                    .setName("testproduct")
                    .build());
            Assert.assertTrue(product.getId() != 0L);
        } catch (CheckException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2listOfProductsIsNotEmpty() {
        ProductService productService = new ProductServiceImpl(true);
        List<Product> productList = productService.findAll();
        Assert.assertTrue(productList.size() > 0);
    }

    @Test
    public void _3productFoundedByItsId() {
        ProductService productService = new ProductServiceImpl(true);
        Product product = null;
        try {
            product = productService.findById(1L);
        } catch (CheckException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(product != null);
    }

    @Test
    public void _4productIsUpdated() {
        ProductService productService = new ProductServiceImpl(true);
        Product product = null;
        try {
            product = productService.findById(1L);
            product.setName("updateproductname");
            productService.update(product);
            product = productService.findById(1L);
        } catch (CheckException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(product != null && product.getName().equals("updateproductname"));
    }

    @Test
    public void _5productIsDeleted() {
        ProductService productService = new ProductServiceImpl(true);
        Product product = null;

        PlanService planService = new PlanServiceImpl(true);
        try {
            planService.delete(1L);
            productService.delete(1L);
            product = productService.findById(1L);
        } catch (CheckException e) {
            Assert.assertTrue(true);
        }
    }
}
