package my.project.internetprovider;

import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
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
public class PlanTest {
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
    public void _1planCreationIsExpected() {
        PlanService planService = new PlanServiceImpl(true);
        ProductService productService = new ProductServiceImpl(true);

        Plan plan = null;
        try {
            plan = planService.create(Plan.newBuilder()
                    .setName("test_plan")
                    .setPrice(100.)
                    .setProduct(productService.findById(2L))
                    .build());
            Assert.assertTrue(plan.getId() != 0L);
        } catch (ValidationException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2listOfPlansIsNotEmpty() {
        PlanService planService = new PlanServiceImpl(true);
        List<Plan> planList = planService.findAll();
        Assert.assertTrue(planList.size() > 0);
    }

    @Test
    public void _3planFoundedByItsId() {
        PlanService planService = new PlanServiceImpl(true);
        Plan plan = null;
        try {
            plan = planService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(plan != null);
    }

    @Test
    public void _4planIsUpdated() {
        PlanService planService = new PlanServiceImpl(true);
        Plan plan = null;
        try {
            plan = planService.findById(1L);
            plan.setName("updateplanname");
            planService.update(plan);
            plan = planService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UpdateException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(plan != null && plan.getName().equals("updateplanname"));
    }

    @Test
    public void _5planIsDeleted() {
        PlanService planService = new PlanServiceImpl(true);
        try {
            planService.delete(1L);
            planService.findById(1L);
        } catch (NotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
