package my.project.internetprovider.service.impl;

import my.project.internetprovider.db.dao.AccountDao;
import my.project.internetprovider.db.dao.DaoFactory;
import my.project.internetprovider.db.dao.UserDao;
import my.project.internetprovider.db.entity.Account;
import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.CheckException;
import my.project.internetprovider.service.UserService;
import my.project.internetprovider.util.EmailValidator;
import my.project.internetprovider.util.HashUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public User findById(Long id) throws CheckException {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            User user = dao.findById(id).orElseThrow(() ->
                    new CheckException("userNotFound"));

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

    public User login(String login, String password) throws CheckException {
        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            User user = dao.findByLogin(login).orElseThrow(() ->
                    new CheckException("incorrectLoginOrPassword"));

            if (isPasswordValid(user, password)) {
                return user;
            }

            throw new CheckException("incorrectLoginOrPassword");
        }
    }

    public User register(String login, String password, String name, String email) throws CheckException {
        Map<String, String> messages = new HashMap<>();

        if (name.length() < 2 || name.length() > 120)
            messages.put("name", "new.user.name.size");

        if (name.isEmpty())
            messages.put("name", "new.user.name.notEmpty");

        if (!EmailValidator.validate(email))
            messages.put("email", "new.user.email.beValid");

        if (email.isEmpty())
            messages.put("email", "new.user.email.notEmpty");

        if (login.length() < 2 || login.length() > 30) {
            messages.put("login", "new.user.login.size");
        }

        if (login.isEmpty())
            messages.put("login", "new.user.login.notEmpty");

        if (password.isEmpty()) {
            messages.put("password", "new.user.password.notEmpty");
        }

        if (messages.size() > 0)
            throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

        try (UserDao dao = daoFactory.createUserDao(testMode)) {
            Optional<User> userOptional = dao.findByLogin(login);
            if (userOptional.isPresent()) {
                throw new CheckException("login=new.user.login.isTaken");
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
    public void updateProfile(Long id, String name, String email, String password, String cpassword) throws CheckException {
        Map<String, String> messages = new HashMap<>();

        if (name.length() < 2 || name.length() > 120)
            messages.put("name", "edit.user.name.size");

        if (name.isEmpty())
            messages.put("name", "edit.user.name.notEmpty");

        if (!EmailValidator.validate(email))
            messages.put("email", "edit.user.email.beValid");

        if (email.isEmpty())
            messages.put("email", "edit.user.email.notEmpty");


        if (!password.isEmpty() && !cpassword.equals(password)) {
            messages.put("password", "passwordConfirmError");
        }

        if (messages.size() > 0)
            throw new CheckException(CheckException.fromMultipleToSingleMessage(messages));

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
