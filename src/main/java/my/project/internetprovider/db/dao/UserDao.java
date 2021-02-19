package my.project.internetprovider.db.dao;

import my.project.internetprovider.db.entity.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    Optional<User> findByLogin(String login);
}
