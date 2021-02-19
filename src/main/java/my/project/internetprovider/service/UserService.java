package my.project.internetprovider.service;


import my.project.internetprovider.db.entity.User;
import my.project.internetprovider.exception.AuthenticationException;
import my.project.internetprovider.exception.RegistrationException;
import my.project.internetprovider.exception.UpdateException;

public interface UserService extends GenericService<User, Long> {
    User login(String login, String password) throws AuthenticationException;

    User register(String login, String password, String name, String email) throws RegistrationException;

    void updateProfile(Long id, String name, String email, String password, String confirmPassword) throws UpdateException;
}
