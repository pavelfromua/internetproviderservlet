package my.project.internetprovider.service;

import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.CheckException;

public interface UserService extends GenericService<User, Long> {
    User login(String login, String password) throws CheckException;

    User register(String login, String password, String name, String email) throws CheckException;

    void updateProfile(Long id, String name, String email, String password, String confirmPassword) throws CheckException;
}
