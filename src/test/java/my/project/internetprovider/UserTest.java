package my.project.internetprovider;

import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.service.AccountService;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.service.impl.AccountServiceImpl;
import my.project.internetprovider.service.impl.UserServiceImpl;
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
public class UserTest {
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
    public void _1userCreationIsExpected() {
        UserService userService = new UserServiceImpl(true);
        User user = null;
        try {
            user = userService.register("mock", "111", "mock", "mock@gmail.com");
            Assert.assertTrue(user.getId() != 0L);
        } catch (RegistrationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _2listOfUsersIsNotEmpty() {
        UserService userService = new UserServiceImpl(true);
        List<User> userList = userService.findAll();
        Assert.assertTrue(userList.size() > 0);
    }

    @Test
    public void _3userFoundedByItsId() {
        UserService userService = new UserServiceImpl(true);
        User user = null;
        try {
            user = userService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(user != null);
    }

    @Test
    public void _4userIsUpdated() {
        UserService userService = new UserServiceImpl(true);
        User user = null;
        try {
            user = userService.findById(1L);
            user.setName("update");
            userService.update(user);
            user = userService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UpdateException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(user != null && user.getName().equals("update"));
    }

    @Test
    public void _5userProfileIsUpdated() {
        UserService userService = new UserServiceImpl(true);
        User user = null;
        try {
            userService.updateProfile(1L, "profile", "profile@gmail.com", "333", "333");
            user = userService.findById(1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (UpdateException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(user != null && user.getName().equals("profile"));
    }

    @Test
    public void _6userIdLoggedIn() {
        UserService userService = new UserServiceImpl(true);
        User user = null;
        try {
            user = userService.login("mock", "111");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(user != null && user.getLogin().equals("mock"));
    }

    @Test
    public void _7userIsDeleted() {
        UserService userService = new UserServiceImpl(true);
        AccountService accountService = new AccountServiceImpl(true);
        try {
            Account account = accountService.findByUserId(1L);
            accountService.delete(account.getId());
            userService.delete(1L);
            userService.findById(1L);
        } catch (NotFoundException e) {
            Assert.assertTrue(true);
        }
    }
}
