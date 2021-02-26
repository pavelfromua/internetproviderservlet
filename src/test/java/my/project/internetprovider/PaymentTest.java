package my.project.internetprovider;

import my.project.internetprovider.db.entity.Payment;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.PaymentService;
import my.project.internetprovider.service.impl.PaymentServiceImpl;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaymentTest {
    @BeforeClass
    public static void beforeTest() throws SQLException {
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
    public void _1paymentCreationIsExpected() {
        PaymentService paymentService = new PaymentServiceImpl(true);

        try {
            Payment payment = paymentService.create(Payment.newBuilder()
                    .setAccount(1L)
                    .setAmount(100.)
                    .setDate(LocalDateTime.now())
                    .setName("test payment")
                    .build());
            Assert.assertTrue(payment.getId() != 0L);
        } catch (CheckException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2listOfPaymentsIsNotEmpty() {
        PaymentService paymentService = new PaymentServiceImpl(true);
        List<Payment> paymentList = paymentService.findAll();
        Assert.assertTrue(paymentList.size() > 0);
    }

    @Test
    public void _3paymentFoundedByItsId() {
        PaymentService paymentService = new PaymentServiceImpl(true);
        Payment payment = null;
        try {
            payment = paymentService.findById(1L);
        } catch (CheckException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(payment != null);
    }

    @Test
    public void _4paymentIsUpdated() {
        PaymentService paymentService = new PaymentServiceImpl(true);
        Payment payment = null;
        try {
            payment = paymentService.findById(1L);
            payment.setName("test");
            paymentService.update(payment);
            payment = paymentService.findById(1L);
        } catch (CheckException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(payment != null && payment.getName().equals("test"));
    }

    @Test
    public void _5paymentIsDeleted() {
        PaymentService paymentService = new PaymentServiceImpl(true);
        try {
            paymentService.delete(1L);
            paymentService.findById(1L);
        } catch (CheckException e) {
            Assert.assertTrue(true);
        }
    }
}
