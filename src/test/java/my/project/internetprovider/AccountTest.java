package my.project.internetprovider;

import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.Plan;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.exception.ValidationException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
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
public class AccountTest {
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
    public void _1accountCreationIsExpected() {
        AccountService accountService = new AccountServiceImpl(true);
        Account account = null;
        try {
            account = accountService.create(Account.newBuilder()
                    .setActive(true)
                    .setUser(1L)
                    .build());
            Assert.assertTrue(account.getId() != 0L);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2listOfAccountsIsNotEmpty() {
        AccountService accountService = new AccountServiceImpl(true);
        List<Account> accountList = accountService.findAll();
        Assert.assertTrue(accountList.size() > 0);
    }

    @Test
    public void _3accountFoundedByItsId() {
        AccountService accountService = new AccountServiceImpl(true);
        Account account = null;
        try {
            account = accountService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(account != null);
    }

    @Test
    public void _4accountFoundedByUserId() {
        AccountService accountService = new AccountServiceImpl(true);
        Account account = null;
        try {
            account = accountService.findByUserId(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(account != null);
    }

    @Test
    public void _5foundedPlansByProductId() {
        AccountService accountService = new AccountServiceImpl(true);
        List<Plan> plansList = accountService.findPlansByProductId(1L);
        Assert.assertTrue(plansList.size() > 0);
    }

    @Test
    public void _6accountIsUpdated() {
        AccountService accountService = new AccountServiceImpl(true);
        Account account = null;
        try {
            account = accountService.findById(1L);
            account.setActive(false);
            accountService.update(account);
            account = accountService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UpdateException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(account != null && !account.isActive());
    }

    @Test
    public void _7accountIsDeleted() {
        AccountService accountService = new AccountServiceImpl(true);
        try {
            accountService.delete(1L);
            accountService.findById(1L);
        } catch (NotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
