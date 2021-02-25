package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.NotFoundException;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.util.HashUtil;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private boolean testMode = false;
    DaoFactory daoFactory = DaoFactory.getInstance();

    public UserServiceImpl(){}

    public UserServiceImpl(boolean testMode) {
        this.testMode = testMode;
    }

    @Override
    public List<User> findAll() {
        List<User> users;

        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            users = dao.findAll();
        }

        return users;
    }

    @Override
    public User create(User user) {
        user.setSalt(HashUtil.getSalt());
        user.setPassword(HashUtil.hashPassword(user.getPassword(), user.getSalt()));

        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            dao.create(user);
        }

        try (AccountDao dao = daoFactory.createAccountDao(testMode)) {
            dao.create(Account.newBuilder()
                    .setActive(true)
                    .setUser(user.getId())
                    .build());
        }

        return user;
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            User user = dao.findById(id).orElseThrow(() ->
                    new NotFoundException("User not found"));

            return user;
        }
    }



    @Override
    public void update(User user) {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            dao.update(user);
        }
    }

    @Override
    public void delete(Long id) {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            dao.delete(id);
        }
    }

    public boolean isPasswordValid(User user, String password) {
        if (user.getPassword().equals(HashUtil.hashPassword(password, user.getSalt()))) {
            return true;
        }

        return false;
    }

    public User login(String login, String password) throws AuthenticationException {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            User user = dao.findByLogin(login).orElseThrow(() ->
                    new AuthenticationException("Incorrect login or password"));

            if (isPasswordValid(user, password)) {
                return user;
            }

            throw new AuthenticationException("Incorrect login or password");
        }
    }

    public User register(String login, String password, String name, String email) throws RegistrationException {
        if (name.isEmpty()) {
            throw new RegistrationException("Name can't be empty");
        }

        if (email.isEmpty()) {
            throw new RegistrationException("Email can't be empty");
        }

        if (login.isEmpty()) {
            throw new RegistrationException("Login can't be empty");
        }

        if (password.isEmpty()) {
            throw new RegistrationException("Password can't be empty");
        }

        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            Optional<User> userOptional = dao.findByLogin(login);
            if (userOptional.isPresent()) {
                throw new RegistrationException("Login is already taken");
            }

            User user = User.newBuilder()
                    .setLogin(login)
                    .setPassword(password)
                    .setName(name)
                    .setEmail(email)
                    .setRoleId(1)
                    .build();
            return create(user);
        }
    }

    @Override
    public void updateProfile(Long id, String name, String email, String password, String cpassword) throws UpdateException {
        if (name.isEmpty()) {
            throw new UpdateException("Name can't be empty");
        }

        if (email.isEmpty()) {
            throw new UpdateException("Email can't be empty");
        }

        if (!password.isEmpty() && !cpassword.equals(password)) {
            throw new UpdateException("Confirm password unequal to the password");
        }

        User user = User.newBuilder()
                .setId(id)
                .setName(name)
                .setEmail(email)
                .build();

        if (!password.isEmpty()) {
            user.setSalt(HashUtil.getSalt());
            user.setPassword(HashUtil.hashPassword(password, user.getSalt()));
        }

        update(user);
    }
}
